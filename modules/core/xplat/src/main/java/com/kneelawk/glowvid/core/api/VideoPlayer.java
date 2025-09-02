package com.kneelawk.glowvid.core.api;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface VideoPlayer {
    /**
     * Called on the render thread to present the current frame of video and audio.
     */
    void update();
}
