package com.kneelawk.glowvid.core.impl;

import com.kneelawk.glowvid.core.impl.version.FFmpegFinder;

public class GlowVidClientMod {
    public static void init() {
        GVCLog.LOG.info("Initializing GlowVid Core Client");
        GVOS.print();
        FFmpegFinder.init();
        GVCLog.LOG.info("GlowVid Core Client initialized");
    }
}
