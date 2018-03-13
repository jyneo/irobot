package com.arcsoft.irobot.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.arcsoft.irobot.R;
import com.arcsoft.irobot.interfaces.IRobot;

/**
 * Robot status panel to show all status of robot.
 * Created by yj2595 on 2018/3/5.
 */

public class RobotStatusFragment extends Fragment {

    private static final boolean DEBUG = true;	// TODO set false on release
    private final String TAG = this.getClass().getSimpleName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_VALUES = "values";

    private IRobot mCreate2Parent;
    private TextView[] mValueTextViews;

    public RobotStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        //if (DEBUG) Log.d(TAG, "onAttach");
        super.onAttach(context);
//        if (context instanceof IRobot) {
//            mCreate2Parent = (IRobot) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement IRobotParent");
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //if (DEBUG) Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_robot_status, container, false);
//        createStatusTableView(rootView);
//        setStatus(savedInstanceState == null ? null : savedInstanceState.getIntArray(ARG_VALUES));
        rootView.findViewById(R.id.update_status_button).setOnClickListener(mOnClickListener);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //if (DEBUG) Log.d(TAG, "onSaveInstanceState");
//        int[] values = new int[mValueTextViews.length];
//        for (int i = 0; i < mValueTextViews.length; i++) {
//            try {
//                values[i] = Integer.parseInt(mValueTextViews[i].getText().toString(), 10);
//            } catch (NumberFormatException e) {
//                values = null;
//                break;
//            }
//        }
//        outState.putIntArray(ARG_VALUES, values);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        //if (DEBUG) Log.v(TAG, "onResume");
        super.onResume();
        // Update status when navigating back
    }

    @Override
    public void onDetach() {
        // if (DEBUG) Log.d(TAG, "onDetach");
        super.onDetach();
        mCreate2Parent = null;
    }

//    private void createStatusTableView(View rootView) {
//        Context context = getActivity();
//        TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.status_table_layout);
//        mValueTextViews = new TextView[SensorPacket.kNum];
//        for (int id = SensorPacket.kMin; id <= SensorPacket.kMax; id++) {
//            SensorPacket.Info info = SensorPacket.getInfo(id);
//
//            TableRow row = new TableRow(context);
//            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//
//            TextView view = createCellTextView(context);
//            view.setText(String.valueOf(id));
//            row.addView(view);
//
//            view = createCellTextView(context);
//            view.setText(info.name);
//            row.addView(view);
//
//            view = createCellTextView(context);
//            view.setText(String.valueOf(info.min));
//            row.addView(view);
//
//            view = createCellTextView(context);
//            view.setText(String.valueOf(info.max));
//            row.addView(view);
//
//            view = createCellTextView(context);
//            row.addView(view);
//            mValueTextViews[id - SensorPacket.kMin] = view;
//
//            tableLayout.addView(row);
//        }
//    }

    private TextView createCellTextView(final Context context) {
        TextView view = new TextView(context);
        view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        view.setTextSize(11);
        view.setPadding(1, 0, 1, 0);
        return view;
    }

//    public void updateStatus() {
//        Create2 robot = mCreate2Parent.getCreate2();
//        SensorResult[] results = robot.query(SensorGroupPackage.kGroup_100);
//        if (results == null) {
//            Toast.makeText(getActivity(), "Querying failed!", Toast.LENGTH_SHORT).show();
//            setStatus(null);
//        } else {
//            for (int i = 0; i < results.length; i++) {
//                SensorResult result = results[i];
//                //Log.d(TAG, String.format("updateStatus: id=%1$d, value=%2$d", result.id, result.value));
//                final int idx = result.id - SensorPacket.kMin;
//                mValueTextViews[idx].setText(String.valueOf(result.getFinalValue()));
//            }
//        }
//    }

    private void setStatus(final int[] values) {
        //if (DEBUG) Log.d(TAG, "setStatus: values=" + values);
//        if (values == null) {
//            for (int i = 0; i < mValueTextViews.length; i++) {
//                mValueTextViews[i].setText(""); // Reset to empty
//            }
//        } else {
//            for (int i = 0; i < mValueTextViews.length; i++) {
//                mValueTextViews[i].setText(String.valueOf(values[i])); // Reset to empty
//            }
//        }
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.update_status_button:
//                    updateStatus();
                    break;
            }
        }
    };
}
