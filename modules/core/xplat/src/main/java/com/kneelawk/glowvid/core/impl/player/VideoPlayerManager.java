package com.kneelawk.glowvid.core.impl.player;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import com.kneelawk.glowvid.core.api.VideoPlayer;

public class VideoPlayerManager {
    public static final Executor EXECUTOR = Executors.newCachedThreadPool();

    public static VideoPlayer start(String source) {
        PriorityBlockingQueue<VideoFrame> videoQueue = new PriorityBlockingQueue<>();
        PriorityBlockingQueue<AudioFrame> audioQueue = new PriorityBlockingQueue<>();
        
        VideoLoader loader = new VideoLoader(source, videoQueue, audioQueue);
        CompletableFuture<Void> loaderFuture = CompletableFuture.runAsync(loader, EXECUTOR);
        return new VideoPlayerImpl(loader, loaderFuture);
    }
}
