package com.kneelawk.glowvid.core.impl.ffmpeg;

import java.nio.ByteBuffer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.NativeResource;
import org.lwjgl.system.Struct;

import static org.lwjgl.system.MemoryUtil.memGetAddress;

public class AVInputFormat extends Struct<AVInputFormat> implements NativeResource {
    /**
     * The struct size in bytes.
     */
    public static final int SIZEOF;

    /**
     * The struct alignment in bytes.
     */
    public static final int ALIGNOF;

    public static final int NAME,
        LONG_NAME,
        FLAGS,
        EXTENSIONS,
        CODEC_TAG,
        PRIV_CLASS,
        MIME_TYPE;

    static {
        Layout layout = __struct(
            __member(POINTER_SIZE), // name
            __member(POINTER_SIZE), // long_name
            __member(4), // flags
            __member(POINTER_SIZE), // extensions
            __member(POINTER_SIZE), // codec_tag
            __member(POINTER_SIZE), // priv_class
            __member(POINTER_SIZE) // mime_type
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        NAME = layout.offsetof(0);
        LONG_NAME = layout.offsetof(1);
        FLAGS = layout.offsetof(2);
        EXTENSIONS = layout.offsetof(3);
        CODEC_TAG = layout.offsetof(4);
        PRIV_CLASS = layout.offsetof(5);
        MIME_TYPE = layout.offsetof(6);
    }

    /**
     * Creates a struct instance at the specified address.
     *
     * @param address   the struct memory address
     * @param container an optional container buffer, to be referenced strongly by the struct instance.
     */
    protected AVInputFormat(long address, @Nullable ByteBuffer container) {
        super(address, container);
    }

    @Override
    protected @NotNull AVInputFormat create(long address, @Nullable ByteBuffer container) {
        return new AVInputFormat(address, container);
    }

    public static AVInputFormat create(long address) {
        return new AVInputFormat(address, null);
    }

    @Override
    public int sizeof() {
        return SIZEOF;
    }

    public String long_name() {
        return MemoryUtil.memASCII(nlong_name(address()));
    }

    public static long nlong_name(long struct) {return memGetAddress(struct + LONG_NAME);}
}
