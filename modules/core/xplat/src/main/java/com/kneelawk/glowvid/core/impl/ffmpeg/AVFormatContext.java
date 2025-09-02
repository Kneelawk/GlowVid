package com.kneelawk.glowvid.core.impl.ffmpeg;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.Pointer;

public class AVFormatContext implements Pointer {
    private final long pointer;

    public AVFormatContext(long pointer) {this.pointer = pointer;}

    @Override
    public long address() {
        return pointer;
    }
}
