package com.arcsoft.irobot.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcsoft.irobot.R;

/**
 *
 * Created by yj2595 on 2018/3/5.
 */

public class RobotUtilityFragment extends Fragment {

    private static final boolean DEBUG = true;	// TODO set false on release
    private final String TAG = this.getClass().getSimpleName();

    private View.OnClickListener mOnClickListener;
    //    private ToggleButton mRecordButton;
    private TextView mDataDirTextView;
    private TextView mDurationTextView;

    public RobotUtilityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_robot_utility, container, false);
//        int[] buttonIds = {
//                R.id.load_map_btn,
//                R.id.save_map_btn,
//        };

//        for (int i = 0; i < buttonIds.length; i++) {
//            view.findViewById(buttonIds[i]).setOnClickListener(mOnClickListener);
//        }
//        mRecordButton = (ToggleButton) view.findViewById(R.id.record_toggle_button);
        mDataDirTextView = (TextView) view.findViewById(R.id.record_data_path_text);
        mDurationTextView = (TextView) view.findViewById(R.id.record_duration_text);

        TextView httpLabel = (TextView) view.findViewById(R.id.http_server_address_label);
//        httpLabel.setText("Server: http://" + NetworkUtils.getLocalIpAddress(true) + ":" + MainActivity.HTTP_PORT);

        TextView sdkVersionLabel = (TextView) view.findViewById(R.id.sdk_version_label);
        sdkVersionLabel.setText("Version: ");
        sdkVersionLabel.setSelected(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DEBUG) Log.v(TAG, "onResume");
        // Update status when navigating back
    }
}
