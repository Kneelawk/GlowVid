package com.kneelawk.glowvid.core.impl;

import com.kneelawk.glowvid.core.impl.player.VideoPlayerManager;
import com.kneelawk.glowvid.core.impl.version.FFmpegFinder;

public class GlowVidClientMod {
    public static void init() {
        GVCLog.LOG.info("Initializing GlowVid Core Client");
        GVOS.print();
        FFmpegFinder.install();
        VideoPlayerManager.start("/home/cyan/Videos/2025-08-26_03-47-47.webm");
        GVCLog.LOG.info("GlowVid Core Client initialized");
    }
}
