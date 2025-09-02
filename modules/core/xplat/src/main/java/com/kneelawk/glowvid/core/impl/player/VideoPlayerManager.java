package com.kneelawk.glowvid.core.impl.player;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kneelawk.glowvid.core.api.VideoPlayer;

public class VideoPlayerManager {
    public static final Executor EXECUTOR = Executors.newCachedThreadPool();

    public static VideoPlayer start(String source) {
        return new VideoPlayerImpl(source, EXECUTOR);
    }
}
