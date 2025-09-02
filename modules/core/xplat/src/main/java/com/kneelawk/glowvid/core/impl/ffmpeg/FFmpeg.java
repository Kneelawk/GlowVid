package com.kneelawk.glowvid.core.impl.ffmpeg;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.Library;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Pointer;
import org.lwjgl.system.SharedLibrary;
import org.lwjgl.system.libffi.FFICIF;
import org.lwjgl.system.libffi.LibFFI;

import com.kneelawk.glowvid.core.impl.FFIUtil;
import com.kneelawk.glowvid.core.impl.GVCConstants;
import com.kneelawk.glowvid.core.impl.GVCLog;

import static org.lwjgl.system.MemoryUtil.memAddressSafe;

public class FFmpeg {
    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static SharedLibrary avutil = null;
    private static int avutilVersion = 0;
    private static SharedLibrary avformat = null;
    private static int avformatVersion = 0;
    private static SharedLibrary avcodec = null;
    private static int avcodecVersion = 0;

    private static final FFICIF versionCIF = FFIUtil.create(LibFFI.ffi_type_uint);
    private static final FFICIF stringCIF = FFIUtil.create(LibFFI.ffi_type_pointer);

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
        LOCK.readLock().lock();
        try {
            return avutil != null && avformat != null && avcodec != null;
        } finally {
            LOCK.readLock().unlock();
        }
    }

    public static void load(Path avutilPath, Path avformatPath, Path avcodecPath) {
        LOCK.writeLock().lock();
        try {
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
                GVCLog.LOG.info("[GlowVid] Unloading avcodec {}.{}.{}", getAvcodecVersionMajor(),
                    getAvcodecVersionMinor(),
                    getAvcodecVersionPatch());
                avcodec.close();
            }

            avutil = Library.loadNative(GVCConstants.MODULE, avutilPath.toString());
            avformat = Library.loadNative(GVCConstants.MODULE, avformatPath.toString());
            avcodec = Library.loadNative(GVCConstants.MODULE, avcodecPath.toString());

            avutilVersion = getVersion(avutil, "avutil_version");
            avformatVersion = getVersion(avformat, "avformat_version");
            avcodecVersion = getVersion(avcodec, "avcodec_version");

            GVCLog.LOG.info("[GlowVid] Loading avutil {}.{}.{} license: '{}', flags: {}", getAvutilVersionMajor(),
                getAvutilVersionMinor(), getAvutilVersionPatch(), getString(avutil, "avutil_license"),
                getString(avutil, "avutil_configuration"));
            GVCLog.LOG.info("[GlowVid] Loading avformat {}.{}.{} license: '{}', flags: {}", getAvformatVersionMajor(),
                getAvformatVersionMinor(), getAvformatVersionPatch(), getString(avformat, "avformat_license"),
                getString(avformat, "avformat_configuration"));
            GVCLog.LOG.info("[GlowVid] Loading avcodec {}.{}.{} license: '{}', flags: {}", getAvcodecVersionMajor(),
                getAvcodecVersionMinor(), getAvcodecVersionPatch(), getString(avcodec, "avcodec_license"),
                getString(avcodec, "avcodec_configuration"));

            initPointers();
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    private static void initPointers() {
        avformat_alloc_context = mustGetFunction(avformat, "avformat_alloc_context");
        avformat_free_context = mustGetFunction(avformat, "avformat_free_context");
        avformat_open_input = mustGetFunction(avformat, "avformat_open_input");
    }

    private static long mustGetFunction(SharedLibrary lib, String name) {
        long address = lib.getFunctionAddress(name);
        if (address == 0) throw new UnsatisfiedLinkError("Shared library " + lib + " missing function '" + name + "'");
        return address;
    }

    private static int getVersion(SharedLibrary lib, String func) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer retBuf = stack.calloc(4);
            IntBuffer ret = retBuf.asIntBuffer();
            long versionPtr = lib.getFunctionAddress(func);
            LibFFI.ffi_call(versionCIF, versionPtr, retBuf, null);
            return ret.get(0);
        }
    }

    private static String getString(SharedLibrary lib, String func) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer retBuf = stack.calloc(8);
            PointerBuffer ret = PointerBuffer.create(retBuf);
            long funcPtr = lib.getFunctionAddress(func);
            LibFFI.ffi_call(stringCIF, funcPtr, retBuf, null);
            return ret.getStringASCII(0);
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

    private static long avformat_alloc_context = 0;
    private static final FFICIF avformat_alloc_context_CIF = FFIUtil.create(LibFFI.ffi_type_pointer);

    public static AVFormatContext avformatAllocContext() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer retBuf = stack.calloc(8);
            PointerBuffer ret = PointerBuffer.create(retBuf);
            LibFFI.ffi_call(avformat_alloc_context_CIF, avformat_alloc_context, retBuf, null);
            return new AVFormatContext(ret.get(0));
        }
    }

    private static long avformat_free_context = 0;
    private static final FFICIF avformat_free_context_CIF =
        FFIUtil.create(LibFFI.ffi_type_void, LibFFI.ffi_type_pointer);

    public static void avformatFreeContext(AVFormatContext ctx) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            LibFFI.ffi_call(avformat_free_context_CIF, avformat_free_context, null, stack.pointers(ctx));
        }
    }

    private static long avformat_open_input = 0;
    private static final FFICIF avformat_open_input_CIF =
        FFIUtil.create(LibFFI.ffi_type_sint, LibFFI.ffi_type_pointer, LibFFI.ffi_type_pointer, LibFFI.ffi_type_pointer,
            LibFFI.ffi_type_pointer);

    public static int avformatOpenInput(PointerBuffer ps, CharSequence url, @Nullable Pointer fmt,
                                        @Nullable PointerBuffer options) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer retBuf = stack.calloc(4);
            IntBuffer ret = retBuf.asIntBuffer();
            LibFFI.ffi_call(avformat_open_input_CIF, avformat_open_input, retBuf,
                stack.pointers(memAddressSafe(ps), memAddressSafe(stack.UTF8(url)), memAddressSafe(fmt),
                    memAddressSafe(options)));
            return ret.get(0);
        }
    }
}
