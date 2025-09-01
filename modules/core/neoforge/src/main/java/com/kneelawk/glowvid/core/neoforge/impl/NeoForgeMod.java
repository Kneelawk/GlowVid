package com.kneelawk.glowvid.core.neoforge.impl;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import com.kneelawk.glowvid.core.impl.GVCConstants;
import com.kneelawk.glowvid.core.impl.GlowVidMod;

@Mod(GVCConstants.MOD_ID)
public class NeoForgeMod {
    public NeoForgeMod(ModContainer mod) {
        GlowVidMod.init(mod.getModInfo().getVersion().toString());
    }
}
