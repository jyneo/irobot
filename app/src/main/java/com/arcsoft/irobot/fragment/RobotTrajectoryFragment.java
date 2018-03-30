package com.arcsoft.irobot.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arcsoft.irobot.R;
import com.arcsoft.irobot.SensorDataPackage.Create2;
import com.arcsoft.irobot.view.GridMapView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * Created by yj2595 on 2018/3/8.
 */

public class RobotTrajectoryFragment extends Fragment{

    private static final String TAG = "RobotTrajectoryFragment";

    private GridMapView mMapView;
    private Button modeZ_button;
    private Button modeG_button;
    private Button cancel_button;
    private Create2 create2;

    public RobotTrajectoryFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_robot_gridmap, container, false);
        mMapView = (GridMapView) rootView.findViewById(R.id.grid_map_view);
        modeZ_button = (Button) rootView.findViewById(R.id.modeZ);
        modeG_button = (Button) rootView.findViewById(R.id.modeG);
        cancel_button = (Button) rootView.findViewById(R.id.modeCancel);

        create2 = new Create2();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        create2.trajectoryStream(mTrajectoryStreamCallback);
        Log.d(TAG, "onActivityCreated: " + mTrajectoryStreamCallback);

        modeZ_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()) {
                    create2.mode(1);
                }
            }
        });

        modeG_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()) {
                    create2.mode(2);
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()) {
                    create2.mode(3);
                }
             }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapViewUpdateTimer = new Timer();
        mMapViewUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (create2.isConnecting()) {
                    handler.sendEmptyMessage(MSG_UPDATE_GRID_VIEW);
                }
            }
        }, 0, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapViewUpdateTimer != null) {
            mMapViewUpdateTimer.cancel();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    /** Fragment当前状态是否可见 */
    private boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
        } else {
            isVisible = false;
        }
    }

    List<Float> list = new ArrayList<>();
    private final Create2.TrajectoryStreamCallback mTrajectoryStreamCallback = new Create2.TrajectoryStreamCallback() {
        @Override
        public void onTrajectoryStream(float[] result) {
            for (int i = 0; i < 4; i++) {
                Log.d("RobotTrajectoryFragment", result[i] + " " + result[4 + i] + " " + result[8 + i] + " " + result[12 + i]);
            }

            Log.d("RobotTrajectoryFragment", "onTrajectoryStream: " + result[12] + " " + result[13]);
//            Point position = new Point(0,0);
//            position.x = (int) (result[12] * 1000);
//            position.y = (int) (result[13] * 1000);
//
//            int orientation = (int) Math.acos(result[0]);
//            mMapView.setGridMap(position, orientation);

            Log.d("RobotTrajectoryFragment", "onTrajectoryStream: " + "callback success");

//            Random random = new Random();
//            position.x = random.nextInt(1000) - 500;
//            position.y = random.nextInt(1000) - 500;
//            int orientation = random.nextInt(360);
//            mMapView.setGridMap(position, orientation);

            list.add(result[12] * 300 + 720);
            list.add(result[13] * 300 + 954);
            list.add(result[0]);

//            Random random = new Random();
//            Float a = (float) ((random.nextInt(6) - 3) * 200 + 720);
//            Float b = (float) ((random.nextInt(6) - 3) * 200 + 954);
//            Float c = (float) random.nextInt(360);
//            list.add(a);
//            list.add(b);
//            list.add(c);
        }
    };

    private Timer mMapViewUpdateTimer;
    private static final int MSG_UPDATE_GRID_VIEW = 0;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case MSG_UPDATE_GRID_VIEW:
                    if (isVisible) {
                        mMapView.setGridMap1(listToArray(list));
                    } else {
                        mMapView.clearData();
                    }
                    break;
            }
            return false;
        }
    });

    private Float[] listToArray(List<Float> list) {
        return list.toArray(new Float[0]);
    }

    public FloatBuffer floatToBuffer(float[] results) {
        // 先初始化 buffer，数组的长度 * 4，因为一个 float 占 4 个字节
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(results.length * 4);
        // 数组排序用nativeOrder,根据本地的排列顺序，指定存储方式，
        // 1． Little endian（小头）：将低序字节存储在起始地址
        // 2． Big endian（大头）：将高序字节存储在起始地址
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(results);
        floatBuffer.position(0);
        return floatBuffer;
    }
}
