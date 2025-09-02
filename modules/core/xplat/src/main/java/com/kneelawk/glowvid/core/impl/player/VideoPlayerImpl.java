package com.kneelawk.glowvid.core.impl.player;

import java.util.concurrent.CompletableFuture;

import com.kneelawk.glowvid.core.api.VideoPlayer;

public class VideoPlayerImpl implements VideoPlayer {
    private final VideoLoader loader;
    private final CompletableFuture<Void> loaderFuture;

    public VideoPlayerImpl(VideoLoader loader, CompletableFuture<Void> loaderFuture) {
        this.loader = loader;
        this.loaderFuture = loaderFuture;
    }

    @Override
    public void update() {
    }
}
