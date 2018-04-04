package com.arcsoft.irobot.fragment;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.arcsoft.irobot.R;
import com.arcsoft.irobot.SensorDataPackage.Create2;

/**
 *
 * Created by yj2595 on 2018/3/5.
 */

public class RobotSystemFragment extends Fragment {

    private static final boolean DEBUG = true;
    private final String TAG = this.getClass().getSimpleName();
    private final Object mStreamSync = new Object();

    EditText editText; // 定义信息输出框
    Create2 create2;

    public RobotSystemFragment() {
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
        if (DEBUG) Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_robot_system, container, false);

        getFragmentManager().beginTransaction()
                .replace(R.id.container_control, RobotControlFragment.newInstance())
                .replace(R.id.container_camera, RobotCameraFragment.newInstance())
                .commit();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Button connect_button = (Button) getActivity().findViewById(R.id.wifi_connect_button);
        connect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2 == null || !create2.isConnecting()) {
                    connect_button.setText("DISCONNECT WIFI"); // 按钮上显示--断开
                    doConnect();
                } else if (create2.isConnecting()) {
                    connect_button.setText("CONNECT WIFI");
                    doDisConnect();
                }
            }
        });

        final Button send_button = (Button) getActivity().findViewById(R.id.send_message_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText = (EditText) getActivity().findViewById(R.id.send_message_text);
                try {
                    if (create2 != null && create2.isConnecting()) {
                        create2.write(editText.getText().toString().getBytes());
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update status when navigating back
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public void doConnect(){
        Log.d(TAG, "doConnect: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                create2 = new Create2();
                if (create2.connect()){
                    Log.d(TAG, "doConnect: " + " " + create2.isConnecting());
                }
            }
        }).start();
    }

    public void doDisConnect(){
        if (create2 != null && create2.isConnecting()) {
            create2.stop();
            create2.disconnect();
        }
    }
}
