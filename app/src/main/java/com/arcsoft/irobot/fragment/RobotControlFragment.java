package com.arcsoft.irobot.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arcsoft.irobot.R;
import com.arcsoft.irobot.SensorDataPackage.Create2;
import com.arcsoft.irobot.view.RobotJoyStick;

/**
 * Robot control panel, e.g. driving control.
 * Created by yj2595 on 2018/3/5.
 */

public class RobotControlFragment extends Fragment {

    private static final boolean DEBUG = true;  // TODO set false on release
    private final String TAG = this.getClass().getSimpleName();
    private static final int MSG_UPDATE_GRID_VIEW = 0;

    private Create2 create2;
    Handler mHandler;

    public RobotControlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_robot_control, container, false);
        RobotJoyStick mRobotJoyStick = view.findViewById(R.id.rocker_view);

        mHandler = new Handler();
        mRobotJoyStick.setCallback(new RobotJoyStick.RockerCallBack() {
            @Override
            public void setVelocityLR(final int left, final int right) {
                if (create2 != null && create2.isConnecting()) {
                    Log.d(TAG, "setVelocityLR: " + left + " " + right);

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            create2.driveWheels(left, right);
                        }
                    }, 100);

//                    mMapViewUpdateTimer = new Timer();
//                    mMapViewUpdateTimer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
////                            Message message = mHandler.obtainMessage();
////                            message.what = MSG_UPDATE_GRID_VIEW;
////                            Bundle bundle = new Bundle();
////                            bundle.putInt("left", left);
////                            bundle.putInt("right", right);
////                            message.setData(bundle);
////                            message.sendToTarget();
////                            mHandler.sendEmptyMessage(MSG_UPDATE_GRID_VIEW);
//                        }
//                    }, 0, 100);
//                    try{
//                        Thread.sleep(100);
//                        create2.driveWheels(left, right);
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
                }
            }
        });
        return view;
    }

//    private Timer mMapViewUpdateTimer;
//    private final Handler mHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message message) {
//            switch (message.what){
//                case MSG_UPDATE_GRID_VIEW:
//                    Bundle bundle = message.getData();
//                    create2.driveWheels(bundle.getInt("left"),bundle.getInt("right"));
//                    break;
//            }
//            return false;
//        }
//    });

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        create2 = new Create2();

        Button stop_button = getActivity().findViewById(R.id.irobot_reset);
        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()){
                    Log.d(TAG, "onClick: " + create2.isConnecting());
                    create2.reset();
                }
            }
        });

        Button start_button = getActivity().findViewById(R.id.irobot_start);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()){
                    Log.d(TAG, "onClick: " + create2.isConnecting());
                    create2.start();
                }
            }
        });

        Button reset_button = getActivity().findViewById(R.id.irobot_stop);
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()){
                    Log.d(TAG, "onClick: " + create2.isConnecting());
                    create2.stop();
                }
            }
        });

        Button safe_button = getActivity().findViewById(R.id.irobot_safe_mode);
        safe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()){
                    Log.d(TAG, "onClick: " + create2.isConnecting());
                    create2.safe();
                }
            }
        });

        Button full_button = getActivity().findViewById(R.id.irobot_full_mode);
        full_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()){
                    Log.d(TAG, "onClick: " + create2.isConnecting());
                    create2.full();
                }
            }
        });

        Button record_button = getActivity().findViewById(R.id.record_toggle_button);
        record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
