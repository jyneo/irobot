package com.arcsoft.irobot.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.arcsoft.irobot.R;
import com.arcsoft.irobot.SensorDataPackage.Create2;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * Created by yj2595 on 2018/3/5.
 */

public class RobotSystemFragment extends Fragment {

    private static final boolean DEBUG = true;
    private final String TAG = this.getClass().getSimpleName();
    private final Object mStreamSync = new Object();
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;

    boolean isConnect = true; // 连接还是断开
    private static final int SEND = 1;

    EditText editText; // 定义信息输出框
    static Socket socket = null; // 定义socket
    InputStream inputStream;
    OutputStream outputStream;
    Create2 create2;

    public RobotSystemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        //if (DEBUG) Log.d(TAG, "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //if (DEBUG) Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (DEBUG) Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_robot_system, container, false);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_robot_control, new RobotControlFragment())
                .commit();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.arcsoft.irobot.setVelocity");
        localReceiver = new LocalReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Button connect_button = getActivity().findViewById(R.id.wifi_connect_button);
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
                
//                if (isConnect) { // 标志位 = true表示连接
//                    connect_button.setText("DISCONNECT WIFI"); // 按钮上显示--断开
//
//                    //启动连接线程
//                    Connect_Thread connect_Thread = new Connect_Thread();
//                    connect_Thread.start();
//
//                    isConnect = false; // 置为false
//                } else { // 标志位 = false表示退出连接
//                    isConnect = true; // 置为true
//                    connect_button.setText("CONNECT WIFI"); // 按钮上显示连接
//                    try {
//                        socket.close(); // 关闭连接
//                        socket = null;
//                        outputStream = null;
//                        inputStream = null;
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
            }
        });

        final Button send_button = getActivity().findViewById(R.id.send_message_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText = getActivity().findViewById(R.id.send_message_text);
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
        //if (DEBUG) Log.v(TAG, "onResume");
        // Update status when navigating back
    }

    @Override
    public void onDetach() {
        //if (DEBUG) Log.d(TAG, "onDetach");
        super.onDetach();
    }

    @Override
    public void onDestroy(){
        localBroadcastManager.unregisterReceiver(localReceiver);
        super.onDestroy();
    }

    public void doConnect(){
        Log.d(TAG, "doConnect: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                create2 = new Create2(getContext());
                if (create2.connect()){
//                    create2.stream(mStreamCallback);
                    Log.d(TAG, "run: 11111111111111" + " " + create2.isConnecting());
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

    private final Create2.StreamCallback mStreamCallback = new Create2.StreamCallback() {
        @Override
        public void onStream(float[] result) {

        }
    };

//    // 连接线程
//    class Connect_Thread extends Thread {
//        public void run() {
//            try {
//                if (socket == null) {
//                    // 用InetAddress方法获取ip地址
////                    String IP = "192.168.123.1";
//                    String IP = "192.168.100.1";
//                    String PORT = "8888";
//                    InetAddress ipAddress = InetAddress.getByName(IP);
//                    int port = Integer.valueOf(PORT); // 获取端口号
//                    Log.d(TAG, "run: " + ipAddress + "  " + port);
//                    socket = new Socket(ipAddress, port); // 创建连接地址和端口
//
//                    // 在创建完连接后启动接收线程
//                    Receive_Thread receive_Thread = new Receive_Thread();
//                    receive_Thread.start();
//                }
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // 接收线程
//    class Receive_Thread extends Thread {
//        public void run() {
//            try {
//                while (true) {
//                    final byte[] buffer = new byte[1024]; // 创建接收缓冲区
//                    inputStream = socket.getInputStream();
//                    final int length = inputStream.read(buffer); // 数据读出来，并且返回数据的长度
//                    getActivity().runOnUiThread(new Runnable() { // 不允许其他线程直接操作组件，用提供的此方法可以
//                        public void run() {
//                            // TODO Auto-generated method stub
//
//                        }
//                    });
//                }
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        }
//    }

    public class LocalReceiver extends BroadcastReceiver {

        public static final String TAG = "MyBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            int left = intent.getIntExtra("left",0);
            int right = intent.getIntExtra("right", 0);
            if (create2 != null && create2.isConnecting()){
//                Log.d(TAG, "onReceive: " + left + " " + right);
                Log.d(TAG, "onReceive: " + isConnect);
                create2.driveWheels(left, right);
//                Message message = handler.obtainMessage();
//                message.what = SEND;
//                Bundle bundle = new Bundle();
//                bundle.putInt("left", left);
//                bundle.putInt("right", right);
//                message.setData(bundle);
//                message.sendToTarget();
            }
        }
    }

//    public static Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message message) {
//            Bundle bundle = message.getData();
//            int left = bundle.getInt("left");
//            int right = bundle.getInt("right");
//
//            Log.d("1111111", "handleMessage: " + left + "  " + right);
//            return false;
//        }
//    });

//    public boolean driveWheels(int leftVelocity, int rightVelocity) {
//        return send(SensorCommand.kDriveWheels, rightVelocity, leftVelocity);
//    }
//
//    private boolean send(int opcode) {
//        return write(new byte[] { (byte) opcode });
//    }
//
//    private boolean send(int opcode, byte data) {
//        return write(new byte[] {
//                (byte) opcode,
//                data,
//        });
//    }
//
//    private boolean send(int opcode, int data0, int data1) {
//        Log.d(TAG, "send: " + opcode + " " + data0 + " " + data1);
//
//        String data = opcode + "" + data0 + "" + data1;
//        Log.d(TAG, "send: " + data);
//        return write(data.getBytes());
////        return write(new byte[] {
////                (byte) opcode & 0xFF,
////                (byte) ((data0 >> 8) & 0xFF),
////                (byte) ((data0) & 0xFF),
////                (byte) ((data1 >> 8) & 0xFF),
////                (byte) ((data1) & 0xFF),
////        });
//    }
//
//    private boolean sendWithPackageIds(int opcode, int[] packetIds) {
//        byte[] cmd_bytes = new byte[2 + packetIds.length];
//        cmd_bytes[0] = (byte) opcode;
//        cmd_bytes[1] = (byte) packetIds.length;
//        for (int i = 0; i < packetIds.length; i++) {
//            cmd_bytes[i+2] = (byte) packetIds[i];
//        }
//        return write(cmd_bytes);
//    }
//
//    public boolean write(byte data[]) {
//        try {
//            synchronized (mStreamSync) {
//                Log.d(TAG, "write: " + socket + "   " + outputStream);
//                outputStream = socket.getOutputStream();
////                Log.d(TAG, "write: " + StringUtils.bytesToHex(data));
//                Log.d(TAG, "write: " + data[0] + " " + data[1] + " " + data[2] + " " + data[3] + " " + data[4]);
//                outputStream.write(data);
//                outputStream.flush();
//                //mOutStream.flush(); deadlock sometime when enabled
//            }
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
}
