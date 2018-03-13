package com.arcsoft.irobot.object;

import java.nio.ByteBuffer;
import java.util.Locale;

/**
 *
 * Created by yj2595 on 2018/3/9.
 */

public class GridMap {

    public long timestamp;
    public int width;
    public int height;
    public ByteBuffer data;
    public float scale = 1.0f;
    public final float[] origin = new float[] { 0, 0 };

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "GridMap: timestamp=%d, width=%d, height=%d, scale=%f, origin=(%f,%f)",
                timestamp, width, height, scale, origin[0], origin[1]);
    }
}
