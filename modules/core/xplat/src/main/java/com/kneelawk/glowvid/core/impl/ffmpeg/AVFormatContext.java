package com.kneelawk.glowvid.core.impl.ffmpeg;

import org.lwjgl.system.Pointer;

public record AVFormatContext(long address) implements Pointer, AutoCloseable {
    @Override
    public void close() {
        FFmpeg.avformatFreeContext(this);
    }
}
