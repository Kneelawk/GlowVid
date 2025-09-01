package com.kneelawk.glowvid.core.impl;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libffi.FFICIF;
import org.lwjgl.system.libffi.FFIType;
import org.lwjgl.system.libffi.LibFFI;

public class FFIUtil {
    public static FFICIF create(FFIType ret, FFIType... args) {
        FFICIF cif = FFICIF.create();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            LibFFI.ffi_prep_cif(cif, LibFFI.FFI_DEFAULT_ABI, ret, stack.pointers(args));
        }
        return cif;
    }
}
