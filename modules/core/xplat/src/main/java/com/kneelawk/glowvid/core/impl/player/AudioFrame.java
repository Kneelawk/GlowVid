package com.kneelawk.glowvid.core.impl.player;

import org.jetbrains.annotations.NotNull;

public record AudioFrame(byte[] data, double pts) implements Comparable<AudioFrame> {
    private static final double ELIPSON = 0.0001;

    @Override
    public int compareTo(@NotNull AudioFrame o) {
        if (Math.abs(pts - o.pts) < ELIPSON) return 0;
        if (pts < o.pts) return -1;
        return 1;
    }
}
