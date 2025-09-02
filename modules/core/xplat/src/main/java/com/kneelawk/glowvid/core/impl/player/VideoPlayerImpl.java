package com.kneelawk.glowvid.core.impl.player;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;

import com.kneelawk.glowvid.core.api.VideoPlayer;

public class VideoPlayerImpl implements VideoPlayer {
    private final VideoLoader loader;
    private final CompletableFuture<Void> loaderFuture;
    private final PriorityBlockingQueue<VideoFrame> videoQueue;
    private final Semaphore videoCap;
    private final PriorityBlockingQueue<AudioFrame> audioQueue;
    private final Semaphore audioCap;

    public VideoPlayerImpl(String source, Executor executor) {
        videoQueue = new PriorityBlockingQueue<>();
        audioQueue = new PriorityBlockingQueue<>();
        videoCap = new Semaphore(100);
        audioCap = new Semaphore(100);

        loader = new VideoLoader(source, videoQueue, videoCap, audioQueue, audioCap);
        loaderFuture = CompletableFuture.runAsync(loader, executor);
    }

    @Override
    public void update() {
    }
}
