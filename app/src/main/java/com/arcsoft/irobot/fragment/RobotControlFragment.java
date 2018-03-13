package com.arcsoft.irobot.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arcsoft.irobot.R;
import com.arcsoft.irobot.SensorDataPackage.Create2;
import com.arcsoft.irobot.interfaces.IRobot;
import com.arcsoft.irobot.view.RobotJoyStick;

/**
 * Robot control panel, e.g. driving control.
 * Created by yj2595 on 2018/3/5.
 */

public class RobotControlFragment extends Fragment {

    private static final boolean DEBUG = true;  // TODO set false on release
    private final String TAG = this.getClass().getSimpleName();

    private IRobot mIRobot;
    private Create2 create2;
//    private ToggleButton mRecordButton;
    private LocalBroadcastManager localBroadcastManager;

    public RobotControlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        //if (DEBUG) Log.d(TAG, "onAttach");
        super.onAttach(context);
        if (context instanceof IRobot) {
            mIRobot = (IRobot) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IRobot");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //if (DEBUG) Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_robot_control, container, false);
        RobotJoyStick mRobotJoyStick = view.findViewById(R.id.rocker_view);
        mRobotJoyStick.setCallback(new RobotJoyStick.RockerCallBack() {
            @Override
            public void setVelocityLR(int left, int right) {
                Intent intent = new Intent("com.arcsoft.irobot.setVelocity");
                intent.putExtra("left", left);
                intent.putExtra("right", right);
                localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        create2 = new Create2(getContext());

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
        if (DEBUG) Log.v(TAG, "onResume");
        // Update status when navigating back
//        ((MainActivity) getActivity()).addRobotListener(mRobotListener);
    }

    @Override
    public void onPause() {
        //if (DEBUG) Log.v(TAG, "onPause");
//        ((MainActivity) getActivity()).removeRobotListener(mRobotListener);
        super.onPause();
    }

    @Override
    public void onDetach() {
        //if (DEBUG) Log.d(TAG, "onDetach");
        super.onDetach();
    }

}
