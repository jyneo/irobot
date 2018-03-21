package com.arcsoft.irobot.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arcsoft.irobot.R;
import com.arcsoft.irobot.SensorDataPackage.Create2;
import com.arcsoft.irobot.view.GridMapView;

import java.util.Random;

/**
 *
 * Created by yj2595 on 2018/3/8.
 */

public class RobotGridMapFragment extends Fragment{

    private GridMapView mMapView;
    Create2 create2;

    public RobotGridMapFragment() {
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
        mMapView = rootView.findViewById(R.id.grid_map_view);

        create2 = new Create2();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Button modeZ_button = getActivity().findViewById(R.id.modeZ);
        modeZ_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()) {
                    create2.mode(1);
                }
            }
        });

        Button modeG_button = getActivity().findViewById(R.id.modeG);
        modeG_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create2.isConnecting()) {
                    create2.mode(2);
                }
            }
        });

        Button cancel_button = getActivity().findViewById(R.id.modeCancel);
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

        create2.stream(mStreamCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private final Create2.StreamCallback mStreamCallback = new Create2.StreamCallback() {
        @Override
        public void onStream(float[] result) {
            for (int i = 0; i < 4; i++) {
                Log.d("RobotGridMapFragment", result[i] + " " + result[4 + i] + " " + result[8 + i] + " " + result[12 + i]);
            }

            Log.d("RobotGridMapFragment", "onStream: " + result[12] + " " + result[13]);
            Point position = new Point(0,0);
            // todo: 坐标转换
//            position.x = (int) (result[12] * 1000);
//            position.y = (int) (result[13] * 1000);
//
//            int orientation = (int) Math.acos(result[0]);
//            mMapView.setGridMap(position, orientation);
            Log.d("RobotGridMapFragment", "onStream: " + "callback success");
            Random random = new Random();
            position.x = random.nextInt(1000) - 500;
            position.y = random.nextInt(1000) - 500;
            int orientation = random.nextInt(360);
            mMapView.setGridMap(position, orientation);
        }
    };

}
