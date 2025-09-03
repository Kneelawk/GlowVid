package com.kneelawk.glowvid.core.impl.player;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;

import org.lwjgl.system.MemoryStack;

import com.kneelawk.glowvid.core.impl.GVCLog;
import com.kneelawk.glowvid.core.impl.ffmpeg.AVFormatContext;
import com.kneelawk.glowvid.core.impl.ffmpeg.FFmpeg;

public class VideoLoader implements Runnable {
    private final String source;
    private final PriorityBlockingQueue<VideoFrame> videoQueue;
    private final Semaphore videoCap;
    private final PriorityBlockingQueue<AudioFrame> audioQueue;
    private final Semaphore audioCap;

    public VideoLoader(String source, PriorityBlockingQueue<VideoFrame> videoQueue, Semaphore videoCap,
                       PriorityBlockingQueue<AudioFrame> audioQueue, Semaphore audioCap) {
        this.source = source;
        this.videoQueue = videoQueue;
        this.videoCap = videoCap;
        this.audioQueue = audioQueue;
        this.audioCap = audioCap;
    }

    @Override
    public void run() {
        GVCLog.LOG.info("[GlowVid] Loading '{}'...", source);

        AVFormatContext formatCtx = FFmpeg.avformatAllocContext();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            System.out.println("Opening Input...");
            if (FFmpeg.avformatOpenInput(stack.pointers(formatCtx), source, null, null) != 0) {
                // On error, formatCtx gets freed by avformat_open_input, so we should not free it again
                formatCtx = null;
            }
            System.out.println("Printing stats...");

            GVCLog.LOG.info("[GlowVid] Video format: {}, duration: {}", formatCtx.iformat().long_name(),
                formatCtx.duration());
        } finally {
            if (formatCtx != null) {
                formatCtx.close();
            }
        }
    }
}
