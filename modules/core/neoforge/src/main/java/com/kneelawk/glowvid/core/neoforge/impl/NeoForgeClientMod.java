package com.kneelawk.glowvid.core.neoforge.impl;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

import com.kneelawk.glowvid.core.impl.GVCConstants;
import com.kneelawk.glowvid.core.impl.GlowVidClientMod;

@Mod(value = GVCConstants.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeClientMod {
    public NeoForgeClientMod() {
        GlowVidClientMod.init();
    }
}
