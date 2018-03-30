package com.arcsoft.irobot.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.irobot.R;
import com.arcsoft.irobot.SensorDataPackage.Create2;

/**
 * Robot status panel to show all status of robot.
 * Created by yj2595 on 2018/3/5.
 */

public class RobotStatusFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private static final String ARG_VALUES = "values";

    private TextView[] mValueTextViews;

    private Create2 create2;

    public RobotStatusFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_robot_status, container, false);
        createStatusTableView(rootView);
        setStatus(savedInstanceState == null ? null : savedInstanceState.getIntArray(ARG_VALUES));

        create2 = new Create2();
        create2.robotStatus(mRobotStatusCallback);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

    private void createStatusTableView(View rootView) {
        Context context = getActivity();
        TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.status_table_layout);
        mValueTextViews = new TextView[2];

        String[] name = new String[]{"state", "feedback"};

        for (int id = 0; id <= 1; id++) {

            TableRow row = new TableRow(context);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView view = createCellTextView(context);
            view.setText(String.valueOf(id));
            row.addView(view);

            view = createCellTextView(context);
            view.setText(name[id]);
            row.addView(view);

            view = createCellTextView(context);
            row.addView(view);
            mValueTextViews[id] = view;

            tableLayout.addView(row);
        }
    }

    private TextView createCellTextView(final Context context) {
        TextView view = new TextView(context);
        view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        view.setTextSize(11);
        view.setPadding(1, 0, 1, 0);
        return view;
    }

    public void updateStatus(byte[] results) {
        if (results == null) {
            Toast.makeText(getActivity(), "Status Querying failed!", Toast.LENGTH_SHORT).show();
            setStatus(null);
        } else {
            for (byte id : results) {
                mValueTextViews[id].setText(String.valueOf(results[id]));
            }
        }
    }

    private void setStatus(final int[] values) {
        if (values == null) {
            for (int i = 0; i < mValueTextViews.length; i++) {
                mValueTextViews[i].setText(""); // Reset to empty
            }
        } else {
            for (int i = 0; i < mValueTextViews.length; i++) {
                mValueTextViews[i].setText(String.valueOf(values[i])); // Reset to empty
            }
        }
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

    private static byte[] results;
    private final Create2.RobotStatusCallback mRobotStatusCallback = new Create2.RobotStatusCallback() {
        @Override
        public void onRobotStatus(byte[] results) {
            byte[] result = new byte[]{0, 1};
            if (isVisible) {
                updateStatus(result);
            }
        }
    };
}
