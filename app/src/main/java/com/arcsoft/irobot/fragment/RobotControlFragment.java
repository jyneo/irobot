package com.arcsoft.irobot.fragment;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
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

    private final String TAG = this.getClass().getSimpleName();

    private Create2 create2;

    public RobotControlFragment() {
        // Required empty public constructor
    }

    public static RobotControlFragment newInstance() {
        return new RobotControlFragment();
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
        RobotJoyStick mRobotJoyStick = (RobotJoyStick) view.findViewById(R.id.rocker_view);

        create2 = new Create2();

        mRobotJoyStick.setCallback(new RobotJoyStick.RockerCallBack() {
            @Override
            public void setVelocityLR(final int left, final int right) {
                if (create2 != null && create2.isConnecting()) {
                    Log.d(TAG, "setVelocityLR: " + left + " " + right);

                    try {
                        Thread.sleep(20);
                        create2.driveWheels(left, right);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Button stop_button = (Button) getActivity().findViewById(R.id.irobot_reset);
        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()){
                    Log.d(TAG, "onClick: " + create2.isConnecting());
                    create2.reset();
                }
            }
        });

        Button start_button = (Button) getActivity().findViewById(R.id.irobot_start);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()){
                    Log.d(TAG, "onClick: " + create2.isConnecting());
                    create2.start();
                }
            }
        });

        Button reset_button = (Button) getActivity().findViewById(R.id.irobot_stop);
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()){
                    Log.d(TAG, "onClick: " + create2.isConnecting());
                    create2.stop();
                }
            }
        });

        Button safe_button = (Button) getActivity().findViewById(R.id.irobot_safe_mode);
        safe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()){
                    Log.d(TAG, "onClick: " + create2.isConnecting());
                    create2.safe();
                }
            }
        });

        Button full_button = (Button) getActivity().findViewById(R.id.irobot_full_mode);
        full_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()){
                    Log.d(TAG, "onClick: " + create2.isConnecting());
                    create2.full();
                }
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
