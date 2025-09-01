package com.kneelawk.glowvid.core.impl;

import net.minecraft.resources.ResourceLocation;

public class GVCConstants {
    public static final String MOD_ID = "glowvid_core";
    public static final String MODULE = "com.kneelawk.glowvid";

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
