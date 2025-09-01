package com.kneelawk.glowvid.core.impl.ffmpeg;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;

import org.lwjgl.system.Library;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.SharedLibrary;
import org.lwjgl.system.libffi.FFICIF;
import org.lwjgl.system.libffi.LibFFI;

import com.kneelawk.glowvid.core.impl.GVCConstants;

public class FFmpeg {
    private static SharedLibrary avutil = null;
    private static int avutilVersion = -1;
    private static SharedLibrary avformat = null;
    private static int avformatVersion = -1;
    private static SharedLibrary avcodec = null;
    private static int avcodecVersion = -1;

    private static final FFICIF versionCIF = FFICIF.create();

    static {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            LibFFI.ffi_prep_cif(versionCIF, LibFFI.FFI_DEFAULT_ABI, LibFFI.ffi_type_uint, stack.callocPointer(0));
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

    public static boolean isLoaded() {
        return avutil != null && avformat != null && avcodec != null;
    }

    public static void load(Path avutilPath, Path avformatPath, Path avcodecPath) {
        if (avutil != null) {
            avutil.close();
        }
        if (avformat != null) {
            avformat.close();
        }
        if (avcodec != null) {
            avcodec.close();
        }

        avutil = Library.loadNative(GVCConstants.MODULE, avutilPath.toString());
        avformat = Library.loadNative(GVCConstants.MODULE, avformatPath.toString());
        avcodec = Library.loadNative(GVCConstants.MODULE, avcodecPath.toString());

        avutilVersion = getVersion(avutil, "avutil_version");
        avformatVersion = getVersion(avformat, "avformat_version");
        avcodecVersion = getVersion(avcodec, "avcodec_version");
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
}
