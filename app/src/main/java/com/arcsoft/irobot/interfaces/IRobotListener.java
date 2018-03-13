package com.arcsoft.irobot.interfaces;

import android.support.annotation.NonNull;

/**
 *
 * Created by yj2595 on 2018/3/5.
 */

public interface IRobotListener {
    void onFrame(long timeNs, @NonNull final byte[] data, final int width, final int height);
    void onWheelTicks(long timeNs, @NonNull final short[] ticks);
    void onBumps(long timeNs, @NonNull final boolean[] bumps);
    void onLightBumps(long timeNs, @NonNull final boolean[] lightBumps);
}
