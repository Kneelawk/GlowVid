package com.kneelawk.glowvid.core.impl.ffmpeg;

import java.nio.ByteBuffer;
import java.util.Objects;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.NativeResource;
import org.lwjgl.system.Struct;

import static org.lwjgl.system.MemoryUtil.memGetAddress;
import static org.lwjgl.system.MemoryUtil.memGetLong;

public final class AVFormatContext extends Struct<AVFormatContext> implements NativeResource {
    /**
     * The struct size in bytes.
     */
    public static final int SIZEOF;

    /**
     * The struct alignment in bytes.
     */
    public static final int ALIGNOF;

    public static final int IFORMAT, DURATION;

    static {
        Layout layout = __struct(
            __member(POINTER_SIZE), // av_class
            __member(POINTER_SIZE), // iformat
            __member(POINTER_SIZE), // oformat
            __member(POINTER_SIZE), // priv_data
            __member(POINTER_SIZE), // pb
            __member(4), // ctx_flags
            __member(4), // nb_streams
            __member(POINTER_SIZE), // streams
            __member(4), // nb_stream_groups
            __member(POINTER_SIZE), // stream_groups
            __member(4), // nb_chapters
            __member(POINTER_SIZE), // chapters
            __member(POINTER_SIZE), // url
            __member(8), // start_time
            __member(8), // duration
            __member(8), // bit_rate
            __member(4), // packet_size
            __member(4), // max_delay
            __member(4), // flags
            __member(8), // probesize
            __member(8), // max_analyze_duration
            __member(POINTER_SIZE), // key
            __member(4), // keylen
            __member(4), // nb_programs
            __member(POINTER_SIZE), // programs
            __member(4), // video_codec_id
            __member(4), // audio_codec_id
            __member(4), // subtitle_codec_id
            __member(4), // data_codec_id
            __member(POINTER_SIZE), // metadata
            __member(8), // start_time_realtime
            __member(4), // fps_probe_size
            __member(4), // error_recognition
            __member(AVIOInterruptCB.SIZEOF, AVIOInterruptCB.ALIGNOF), // interrupt_callback
            __member(4), // debug
            __member(4), // max_streams
            __member(4), // max_index_size
            __member(4), // max_picture_buffer
            __member(8), // max_interleave_delta
            __member(4), // max_ts_probe
            __member(4), // max_chunk_duration
            __member(4), // max_chunk_size
            __member(4), // max_probe_packets
            __member(4), // strict_std_compliance
            __member(4), // event_flags
            __member(4), // avoid_negative_ts
            __member(4), // audio_preload
            __member(4), // use_wallclock_as_timestamps
            __member(4), // skip_estimate_duration_from_pts
            __member(4), // avio_flags
            __member(4), // duration_estimation_method
            __member(8), // skip_initial_bytes
            __member(4), // correct_ts_overflow
            __member(4), // seek2any
            __member(4), // flush_packets
            __member(4), // probe_score
            __member(4), // format_probesize
            __member(POINTER_SIZE), // codec_whitelist
            __member(POINTER_SIZE), // format_whitelist
            __member(POINTER_SIZE), // protocol_whitelist
            __member(POINTER_SIZE), // protocol_blacklist
            __member(4), // io_repositioned
            __member(POINTER_SIZE), // video_codec
            __member(POINTER_SIZE), // audio_codec
            __member(POINTER_SIZE), // subtitle_codec
            __member(POINTER_SIZE), // data_codec
            __member(4), // metadata_header_padding
            __member(POINTER_SIZE), // opaque
            __member(POINTER_SIZE), // control_message_cb
            __member(8), // output_ts_offset
            __member(POINTER_SIZE), // dump_separator
            __member(POINTER_SIZE), // io_open
            __member(POINTER_SIZE), // io_close2
            __member(8) // duration_probesize
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        IFORMAT = layout.offsetof(1);
        DURATION = layout.offsetof(14);

        System.out.println("AVFormatContext SIZEOF: " + SIZEOF + ", ALIGNOF: " + ALIGNOF + ", IFORMAT: " + IFORMAT + ", DURATION: " + DURATION);
    }

    public AVFormatContext(long address, @Nullable ByteBuffer container) {
        super(address, container);
    }

    @Override
    protected @NotNull AVFormatContext create(long address, @Nullable ByteBuffer container) {
        return new AVFormatContext(address, container);
    }

    @Override
    public int sizeof() {
        return SIZEOF;
    }

    @Override
    public void free() {
        FFmpeg.avformatFreeContext(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AVFormatContext) obj;
        return this.address == that.address;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    public AVInputFormat iformat() {
        return AVInputFormat.create(niformat(address()));
    }

    public long duration() {
        return nduration(address());
    }

    public static long niformat(long struct) {return memGetAddress(struct + IFORMAT);}

    public static long nduration(long struct) {return memGetLong(struct + DURATION);}
}
