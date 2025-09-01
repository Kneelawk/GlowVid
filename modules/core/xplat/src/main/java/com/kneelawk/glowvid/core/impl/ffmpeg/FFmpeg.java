package com.kneelawk.glowvid.core.impl.ffmpeg;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;

import org.lwjgl.system.Library;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.SharedLibrary;
import org.lwjgl.system.libffi.FFICIF;
import org.lwjgl.system.libffi.LibFFI;

import com.kneelawk.glowvid.core.impl.FFIUtil;
import com.kneelawk.glowvid.core.impl.GVCConstants;
import com.kneelawk.glowvid.core.impl.GVCLog;

public class FFmpeg {
    private static SharedLibrary avutil = null;
    private static int avutilVersion = -1;
    private static SharedLibrary avformat = null;
    private static int avformatVersion = -1;
    private static SharedLibrary avcodec = null;
    private static int avcodecVersion = -1;

    private static final FFICIF versionCIF = FFIUtil.create(LibFFI.ffi_type_uint);

    private static int ffmpegMajor(int v) {
        return v >> 16;
    }

    private static int ffmpegMinor(int v) {
        return (v & 0xFF00) >> 8;
    }

    private static int ffmpegPatch(int v) {
        return v & 0xFF;
    }

    public static boolean isLoaded() {
        return avutil != null && avformat != null && avcodec != null;
    }

    public static void load(Path avutilPath, Path avformatPath, Path avcodecPath) {
        if (avutil != null) {
            GVCLog.LOG.info("[GlowVid] Unloading avutil {}.{}.{}", getAvutilVersionMajor(), getAvutilVersionMinor(),
                getAvutilVersionPatch());
            avutil.close();
        }
        if (avformat != null) {
            GVCLog.LOG.info("[GlowVid] Unloading avformat {}.{}.{}", getAvformatVersionMajor(),
                getAvformatVersionMinor(), getAvformatVersionPatch());
            avformat.close();
        }
        if (avcodec != null) {
            GVCLog.LOG.info("[GlowVid] Unloading avcodec {}.{}.{}", getAvcodecVersionMajor(), getAvcodecVersionMinor(),
                getAvcodecVersionPatch());
            avcodec.close();
        }

        avutil = Library.loadNative(GVCConstants.MODULE, avutilPath.toString());
        avformat = Library.loadNative(GVCConstants.MODULE, avformatPath.toString());
        avcodec = Library.loadNative(GVCConstants.MODULE, avcodecPath.toString());

        avutilVersion = getVersion(avutil, "avutil_version");
        avformatVersion = getVersion(avformat, "avformat_version");
        avcodecVersion = getVersion(avcodec, "avcodec_version");

        GVCLog.LOG.info("[GlowVid] Loading avutil {}.{}.{}", getAvutilVersionMajor(), getAvutilVersionMinor(),
            getAvutilVersionPatch());
        GVCLog.LOG.info("[GlowVid] Loading avformat {}.{}.{}", getAvformatVersionMajor(), getAvformatVersionMinor(),
            getAvformatVersionPatch());
        GVCLog.LOG.info("[GlowVid] Loading avcodec {}.{}.{}", getAvcodecVersionMajor(), getAvcodecVersionMinor(),
            getAvcodecVersionPatch());
    }

    private static int getVersion(SharedLibrary lib, String func) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer retBuf = stack.calloc(4);
            IntBuffer ret = retBuf.asIntBuffer();
            long avutilVersionPtr = lib.getFunctionAddress(func);
            LibFFI.ffi_call(versionCIF, avutilVersionPtr, retBuf, stack.callocPointer(0));
            return ret.get(0);
        }
    }

    public static int getAvutilVersionMajor() {
        return ffmpegMajor(avutilVersion);
    }

    public static int getAvutilVersionMinor() {
        return ffmpegMinor(avutilVersion);
    }

    public static int getAvutilVersionPatch() {
        return ffmpegPatch(avutilVersion);
    }

    public static int getAvformatVersionMajor() {
        return ffmpegMajor(avformatVersion);
    }

    public static int getAvformatVersionMinor() {
        return ffmpegMinor(avformatVersion);
    }

    public static int getAvformatVersionPatch() {
        return ffmpegPatch(avformatVersion);
    }

    public static int getAvcodecVersionMajor() {
        return ffmpegMajor(avcodecVersion);
    }

    public static int getAvcodecVersionMinor() {
        return ffmpegMinor(avcodecVersion);
    }

    public static int getAvcodecVersionPatch() {
        return ffmpegPatch(avcodecVersion);
    }
}
