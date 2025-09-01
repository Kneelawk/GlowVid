package com.kneelawk.glowvid.core.fabric.impl;

import net.fabricmc.api.ClientModInitializer;

import com.kneelawk.glowvid.core.impl.GlowVidClientMod;

public class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        GlowVidClientMod.init();
    }
}
