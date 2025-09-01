package com.kneelawk.glowvid.core.impl.version;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.lwjgl.system.Library;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.SharedLibrary;
import org.lwjgl.system.libffi.FFICIF;
import org.lwjgl.system.libffi.LibFFI;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;

import com.kneelawk.glowvid.core.impl.GVCConstants;
import com.kneelawk.glowvid.core.impl.GVCLog;
import com.kneelawk.glowvid.core.impl.GVOS;

public class FFmpegFinder {
    public record FFmpegPaths(Path avutil, Path avformat, Path avcodec) {}

    public static void init() {
        FFmpegPaths systemFFmpeg = findSystemFFmpeg();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FFICIF cif = FFICIF.calloc(stack);
            LibFFI.ffi_prep_cif(cif, LibFFI.FFI_DEFAULT_ABI, LibFFI.ffi_type_uint, stack.callocPointer(0));

            printVersion(systemFFmpeg.avutil(), "avutil_version", "avutil", stack, cif);
            printVersion(systemFFmpeg.avformat(), "avformat_version", "avformat", stack, cif);
            printVersion(systemFFmpeg.avcodec(), "avcodec_version", "avcodec", stack, cif);
        }
    }

    private static void printVersion(@Nullable Path lib, String versionFunc, String name, MemoryStack stack,
                                     FFICIF cif) {
        if (lib != null) {
            try (SharedLibrary avutil = Library.loadNative(GVCConstants.MODULE, lib.toString())) {
                ByteBuffer retBuf = stack.calloc(4);
                IntBuffer ret = retBuf.asIntBuffer();
                long avutilVersionPtr = avutil.getFunctionAddress(versionFunc);
                LibFFI.ffi_call(cif, avutilVersionPtr, retBuf, stack.callocPointer(0));

                int avutilVersion = ret.get(0);
                GVCLog.LOG.info("[GlowVid] {} version: {}.{}.{}", name, ffmpegMajor(avutilVersion),
                    ffmpegMinor(avutilVersion), ffmpegPatch(avutilVersion));
            }
        }
    }

    private static int ffmpegMajor(int v) {
        return v >> 16;
    }

    private static int ffmpegMinor(int v) {
        return (v & 0xFF00) >> 8;
    }

    private static int ffmpegPatch(int v) {
        return v & 0xFF;
    }

    public static FFmpegPaths findSystemFFmpeg() {
        // TODO mac and windows support
        Path avutilPath = null, avformatPath = null, avcodecPath = null;

        if (GVOS.OS_LINUX) {
            for (String searchStr : getLinuxLibraryPaths()) {
                try (Stream<Path> s = Files.list(Path.of(searchStr))) {
                    Iterator<Path> iterator = s.iterator();
                    while (iterator.hasNext()) {
                        Path path = iterator.next();
                        if (Files.isRegularFile(path)) {
                            if (path.getFileName().startsWith("libavutil.so") && avutilPath == null) {
                                avutilPath = followLinks(path);
                            } else if (path.getFileName().startsWith("libavformat.so") && avformatPath == null) {
                                avformatPath = followLinks(path);
                            } else if (path.getFileName().startsWith("libavcodec.so") && avcodecPath == null) {
                                avcodecPath = followLinks(path);
                            }
                        }
                    }
                } catch (IOException e) {
                    GVCLog.LOG.error("Error iterating over library dir '{}' contents", searchStr, e);
                }
            }
        }

        if (avutilPath == null) {
            GVCLog.LOG.warn("[GlowVid] No system avutil found. You will have to download ffmpeg.");
        } else {
            GVCLog.LOG.info("[GlowVid] System avutil: {}", avutilPath);
        }
        if (avformatPath == null) {
            GVCLog.LOG.warn("[GlowVid] No system avformat found. You will have to download ffmpeg.");
        } else {
            GVCLog.LOG.info("[GlowVid] System avformat: {}", avformatPath);
        }
        if (avcodecPath == null) {
            GVCLog.LOG.warn("[GlowVid] No system avcodec found. You will have to download ffmpeg.");
        } else {
            GVCLog.LOG.info("[GlowVid] System avcodec: {}", avcodecPath);
        }

        return new FFmpegPaths(avutilPath, avformatPath, avcodecPath);
    }

    private static Path followLinks(Path path) {
        while (Files.isSymbolicLink(path)) {
            try {
                path = Files.readSymbolicLink(path);
            } catch (IOException e) {
                GVCLog.LOG.error("Error expanding symbolic link '{}'", path, e);
                return path;
            }
        }
        return path;
    }

    private static Set<String> getLinuxLibraryPaths() {
        Set<String> result = new ObjectLinkedOpenHashSet<>();
        String ldLibraryPath = System.getenv("LD_LIBRARY_PATH");
        if (ldLibraryPath != null) {
            result.addAll(Arrays.asList(ldLibraryPath.split(":")));
        }
        result.addAll(parseLdConfig(Path.of("/etc/ld.so.conf")));
        result.add("/usr/local/lib");
        result.add("/usr/lib");
        result.add("/lib");
        return result;
    }

    private static Set<String> parseLdConfig(Path cfgFile) {
        Set<String> result = new ObjectLinkedOpenHashSet<>();
        try (BufferedReader reader = Files.newBufferedReader(cfgFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#") && !line.isBlank()) {
                    if (line.startsWith("include ")) {
                        for (Path path : expandGlob(line.substring("include ".length()))) {
                            result.addAll(parseLdConfig(path));
                        }
                    } else {
                        result.add(line);
                    }
                }
            }
        } catch (IOException e) {
            GVCLog.LOG.error("Error loading ld config file {}", cfgFile, e);
        }
        return result;
    }

    private static Set<Path> expandGlob(String glob) {
        int idx = 0;
        boolean found = false;
        boolean root = glob.startsWith("/");
        String[] split;
        if (root) {
            split = glob.substring(1).split("/");
        } else {
            split = glob.split("/");
        }
        for (; idx < split.length; idx++) {
            String seg = split[idx];
            if (seg.contains("*") || seg.contains("?") || seg.contains("[")) {
                found = true;
                break;
            }
        }
        if (idx == 0) {
            GVCLog.LOG.error("Attempted to expand malicious glob: '{}'", glob);
            return Set.of(Path.of(glob));
        }
        if (!found) {
            // no glob patterns detected
            return Set.of(Path.of(glob));
        }
        Path parent;
        if (idx < 2) {
            parent = Path.of((root ? "/" : "") + split[0]);
        } else {
            String[] next = new String[idx - 1];
            System.arraycopy(split, 1, next, 0, idx - 1);
            parent = Path.of((root ? "/" : "") + split[0], next);
        }

        PathMatcher matcher;
        try {
            matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
        } catch (Exception e) {
            GVCLog.LOG.error("Error obtaining path matcher for glob '{}'", glob, e);
            return Set.of(Path.of(glob));
        }

        try (Stream<Path> s = Files.walk(parent)) {
            Set<Path> matching = new ObjectLinkedOpenHashSet<>();
            Iterator<Path> it = s.iterator();
            while (it.hasNext()) {
                try {
                    Path path = it.next();
                    if (matcher.matches(path)) {
                        matching.add(path);
                    }
                } catch (Exception e) {
                    GVCLog.LOG.error("Error walking to next directory, contents of '{}'", parent, e);
                }
            }
            return matching;
        } catch (Exception e) {
            GVCLog.LOG.error("Error walking contents of '{}'", parent, e);
            return Set.of(Path.of(glob));
        }
    }
}
