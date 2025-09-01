package com.kneelawk.glowvid.core.fabric.impl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import com.kneelawk.glowvid.core.impl.GVCConstants;
import com.kneelawk.glowvid.core.impl.GlowVidMod;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        GlowVidMod.init(FabricLoader.getInstance().getModContainer(GVCConstants.MOD_ID).get().getMetadata().getVersion()
            .getFriendlyString());
    }
}
