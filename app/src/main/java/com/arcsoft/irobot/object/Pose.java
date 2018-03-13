package com.arcsoft.irobot.object;

import android.util.Log;

/**
 *
 * Created by yj2595 on 2018/3/9.
 */

public class Pose {

    private static final String TAG = Pose.class.getSimpleName();

    public static final int AS3DT_POSE_VALID            = 0;    /// The pose of this estimate is valid.
    public static final int AS3DT_POSE_NOT_INITIALIZED  = 1;    /// Motion estimation is not initialized or initializing failed.
    public static final int AS3DT_POSE_INITIALIZING     = 2;    /// Motion estimation is being initialized.
    public static final int AS3DT_POSE_LOST             = 3;    /// Tracking is lost.
    public static final int AS3DT_POSE_UNKNOWN          = 4;    /// Could not estimate pose at this time.

    public int statusCode;
    public long timestamp;
    public final float[] orientation = new float[4];
    public final float[] translation = new float[3];
    public float accuracy;

    public void setTranslation(float x, float y, float z) {
        translation[0] = x;
        translation[1] = y;
        orientation[2] = z;
    }

    public void setOrientation(float w, float x, float y, float z) {
        orientation[0] = w;
        orientation[1] = x;
        orientation[2] = y;
        orientation[3] = z;
    }

    public static String statusCodeToString(int status_code) {
        switch (status_code) {
            case AS3DT_POSE_VALID: return "VALID";
            case AS3DT_POSE_NOT_INITIALIZED: return "NOT_INITIALIZED";
            case AS3DT_POSE_INITIALIZING: return "INITIALIZING";
            case AS3DT_POSE_LOST: return "LOST";
            case AS3DT_POSE_UNKNOWN: return "UNKNOWN";
            default:
                Log.e(TAG, "Invalid status code: " + status_code);
                return "[CANNOT HAPPENED]";
        }
    }
}
