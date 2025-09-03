package com.kneelawk.glowvid.core.impl.ffmpeg;

import java.nio.ByteBuffer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NativeResource;
import org.lwjgl.system.Struct;

public class AVIOInterruptCB extends Struct<AVIOInterruptCB> implements NativeResource {
    /**
     * The struct size in bytes.
     */
    public static final int SIZEOF;

    /**
     * The struct alignment in bytes.
     */
    public static final int ALIGNOF;

    public static final int CALLBACK, OPAQUE;

    static {
        Layout layout = __struct(
            __member(POINTER_SIZE), // callback
            __member(POINTER_SIZE) // opaque
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        CALLBACK = layout.offsetof(0);
        OPAQUE = layout.offsetof(1);
    }

    /**
     * Creates a struct instance at the specified address.
     *
     * @param address   the struct memory address
     * @param container an optional container buffer, to be referenced strongly by the struct instance.
     */
    protected AVIOInterruptCB(long address, @Nullable ByteBuffer container) {
        super(address, container);
    }

    @Override
    protected @NotNull AVIOInterruptCB create(long address, @Nullable ByteBuffer container) {
        return new AVIOInterruptCB(address, container);
    }

    @Override
    public int sizeof() {
        return SIZEOF;
    }
}
