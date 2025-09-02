package com.kneelawk.glowvid.core.impl.player;

import java.util.concurrent.PriorityBlockingQueue;

public class VideoLoader implements Runnable {
    private final String source;
    private final PriorityBlockingQueue<VideoFrame> videoQueue;
    private final PriorityBlockingQueue<AudioFrame> audioQueue;

    public VideoLoader(String source, PriorityBlockingQueue<VideoFrame> videoQueue,
                       PriorityBlockingQueue<AudioFrame> audioQueue) {
        this.source = source;
        this.videoQueue = videoQueue;
        this.audioQueue = audioQueue;
    }

    @Override
    public void run() {
        
    }
}
