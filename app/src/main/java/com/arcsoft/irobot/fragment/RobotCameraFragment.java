package com.arcsoft.irobot.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcsoft.irobot.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * Created by yj2595 on 2018/3/5.
 */

public class RobotCameraFragment extends Fragment {

    private static final boolean DEBUG = true;
    private final String TAG = this.getClass().getSimpleName();

    private TextView mStatusLabel;

    public RobotCameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_robot_camera, container, false);

        mStatusLabel = (TextView) view.findViewById(R.id.slam_status_label);
//        mTrackedFrameView = (TrackedFrameView) view.findViewById(R.id.tracked_frame_view);
//        mSLAM = ((MainActivity) getActivity()).getSLAM();

        return view;
    }

    @Override
    public void onResume() {
        //if (DEBUG) Log.d(TAG, "onResume");
        super.onResume();
        mFrameViewUpdateTimer = new Timer();
        mFrameViewUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //if (DEBUG) Log.d(TAG, "TimerTask");
                if (isVisible()) {
                    mHandler.sendEmptyMessage(MSG_UPDATE_FRAME_VIEW);
                }
            }
        }, 0, 50);
    }

    @Override
    public void onPause() {
        //if (DEBUG) Log.d(TAG, "onPause");
        if (mFrameViewUpdateTimer != null) {
            mFrameViewUpdateTimer.cancel();
        }
        super.onPause();
    }

    private static final int MSG_UPDATE_FRAME_VIEW = 1;

    private Timer mFrameViewUpdateTimer;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            //if (DEBUG) Log.d(TAG, "handleMessage: what=" + msg.what);
//            switch (msg.what) {
//                case MSG_UPDATE_FRAME_VIEW:
//                    if (mSLAM.isValid()) {
//                        ShortBuffer trackedPoints = mSLAM.getKeyPoints();
//                        int[] feedback = mSLAM.getNavigationFeedback();
//                        String feedbackStr = feedback != null
//                                ? String.format(Locale.ENGLISH, "[%d %d]", feedback[0], feedback[1])
//                                : "[null]";
//                        mStatusLabel.setText(String.format(Locale.ENGLISH,
//                                "Status: %s, tracked points: %d, feedback: %s",
//                                Pose.statusCodeToString(mSLAM.getStatus()),
//                                trackedPoints != null ? trackedPoints.capacity()/2 : 0,
//                                feedbackStr));
//                        mTrackedFrameView.setTrackedPoints(trackedPoints, mSLAM.isKeypointsInPair());
//                    } else {
//                        mStatusLabel.setText("[SLAM is not ready]");
//                        mTrackedFrameView.setTrackedPoints(null, false);
//                    }
//                    if (mTrackedFrameView.isShown()) {
//                        Bitmap frame = ((MainActivity) getActivity()).getFrameBitmap();
//                        mTrackedFrameView.setFrame(frame);
//                        if (frame != null) {
//                            mTrackedFrameView.setAspectRatio(frame.getWidth(), frame.getHeight());
//                        }
//                    }
//                    break;
//            }
            super.handleMessage(msg);
        }
    };
}
