package com.arcsoft.irobot.SensorDataPackage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * Created by yj2595 on 2018/3/7.
 */

public class Create2 {

    private static final String TAG = "Create2";
    private static final int TRAJECTORY = 1;
    private static final int MAPPING = 2;
    private static final int ROBOT_STATUS = 3;
    private static final int control = 1;
    private static final int query_data = 2;

    private final Object mSocketSync = new Object();
    private final Object mStreamSync = new Object();
    private static boolean mIsConnecting = false; // 连接还是断开
    private Socket socket; // 定义socket
    private InputStream inputStream;
    private OutputStream outputStream;

    private static TrajectoryStreamCallback mTrajectoryStreamCallback;
    private static MappingStreamCallback mMappingStreamCallback;
    private static RobotStatusCallback mRobotStatusCallback;

    public interface TrajectoryStreamCallback {
        void onTrajectoryStream(final float[] results);
    }

    public interface MappingStreamCallback {
        void onMappingStream(final byte[] results);
    }

    public interface RobotStatusCallback {
        void onRobotStatus(final byte[] results);
    }

    public Create2(){
    }

    public synchronized boolean connect(){
        Log.d(TAG, "connect: ");
        try {
            mIsConnecting = true;
            if (socket == null) {
                // 用InetAddress方法获取ip地址
//                String IP = "192.168.123.1";
//                String IP = "192.168.100.1";
                String IP = "172.25.31.117";
                String PORT = "8888";
                InetAddress ipAddress = InetAddress.getByName(IP);
                int port = Integer.valueOf(PORT); // 获取端口号
                socket = new Socket(ipAddress, port); // 创建连接地址和端口
                Log.d(TAG, "run: " + ipAddress + "  " + port + " " + socket);
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();

                Log.d(TAG, "run: " + socket + "   " + outputStream + " " + inputStream);

                // 在创建完连接后启动接收线程
                Receive_Thread receive_Thread = new Receive_Thread();
                receive_Thread.start();
            }
            Log.d(TAG, "connect: " + mIsConnecting);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            synchronized (mSocketSync) {
                socket = null;
                inputStream = null;
                outputStream = null;
            }
            return false;
        }
    }

    public void disconnect(){
        if (socket != null) {
            synchronized (mSocketSync) {
                try {
                    socket.close();
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    socket = null;
                }
            }
        }
        mIsConnecting = false;
    }

    public void trajectoryStream(TrajectoryStreamCallback callback){
        mTrajectoryStreamCallback = callback;
    }

    public void mappingStream(MappingStreamCallback callback){
        mMappingStreamCallback = callback;
    }

    public void robotStatus(RobotStatusCallback callback){
        mRobotStatusCallback = callback;
    }

    public synchronized boolean isConnecting() {
        return mIsConnecting;
    }

    private static float byte2float(byte[] b, int index) {
        int l;
        l = b[index];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    // 接收线程
    class Receive_Thread extends Thread {
        public void run() {
            SendData send_data = new SendData();
            SendData rev_data = new SendData();

            rev_data.head[0] = (byte) (0xa5 & 0xff);
            rev_data.head[1] = (byte) (0x5a & 0xFF);
            rev_data.data = null;

            send_data.head[0] = (byte) (0xa5 & 0xff);
            send_data.head[1] = (byte) (0x5a & 0xFF);
            send_data.query_id = 2;

            while(isConnecting() && !socket.isClosed()) {
                try {
                    byte[] buffer = new byte[1024 * 1024]; // 创建接收缓冲区
                    float[] Matrix_4x4 = new float[16]; // 创建接收缓冲区

                    // java.net.SocketException: Socket closed
                    inputStream = socket.getInputStream();
                    if (inputStream != null) {
                        int length = inputStream.read(buffer); // 数据读出来，并且返回数据的长度

                        if (length > 0) {

                            Thread.sleep(30);

                            Log.d(TAG, "run: " + buffer[0] + " " + buffer[1]);
                            if((buffer[0] == rev_data.head[0]) && (buffer[1] == rev_data.head[1])) {
                                rev_data.length = buffer[6]<<24 & 0xff000000 + buffer[5]<<16 & 0xff0000 + buffer[4]<<8 & 0xff00 + buffer[3];

                                if (buffer[2] == query_data) {
                                    rev_data.query_id = buffer[2];
                                    if (buffer[7] == 90 && buffer[8] == -91) {
                                        Log.d(TAG,"trajectory data correct");

                                        // 接收线程解析数据，多次调用耗费时间
                                        for (int i = 0; i < 16; i++)
                                            Matrix_4x4[i] = byte2float(buffer, 4 * i + 9);

                                        if (mTrajectoryStreamCallback != null && isConnecting()) {
                                            Message message = handler.obtainMessage();
                                            message.what = TRAJECTORY;
                                            Bundle bundle = new Bundle();
                                            bundle.putFloatArray("trajectory_buffer", Matrix_4x4);
                                            message.setData(bundle);
                                            message.sendToTarget();
                                        }
                                    }

                                    if (buffer[7] == -91 && buffer[8] == 90) {
                                        Log.d(TAG,"map data correct");

                                        if (mMappingStreamCallback != null && isConnecting()) {
                                            Message message = handler.obtainMessage();
                                            message.what = MAPPING;
                                            Bundle bundle = new Bundle();
                                            bundle.putByteArray("mapping_buffer", buffer);
                                            message.setData(bundle);
                                            message.sendToTarget();
                                        }
                                    }
                                }
                            }
                        }
                        inputStream.close();
                    }

//                        for (int i = 0; i < 16; i++) {
//                            Matrix_4x4[i] = byte2float(buffer, 4 * i);
//                        }
//
//                        Log.d(TAG, "run: " + mTrajectoryStreamCallback + " " + mMappingStreamCallback);

//                        if (mTrajectoryStreamCallback != null && isConnecting()) {
//                            Message message = handler.obtainMessage();
//                            message.what = TRAJECTORY;
//                            Bundle bundle = new Bundle();
//                            bundle.putFloatArray("trajectory_buffer", Matrix_4x4);
//                            message.setData(bundle);
//                            message.sendToTarget();
//                        }

//                        byte[] mapping_buffer = new byte[90000];
//                        if (mMappingStreamCallback != null && isConnecting()) {
//                            Log.d(TAG, "run: 触发");
//                            Message message = handler.obtainMessage();
//                            message.what = MAPPING;
//                            Bundle bundle = new Bundle();
//                            bundle.putByteArray("mapping_buffer", mapping_buffer);
//                            message.setData(bundle);
//                            message.sendToTarget();
//                        }

//                        if (mRobotStatusCallback != null && isConnecting()) {
//                            Message message = handler.obtainMessage();
//                            message.what = ROBOT_STATUS;
//                            Bundle bundle = new Bundle();
//                            bundle.putFloatArray("status_buffer", Matrix_4x4);
//                            message.setData(bundle);
//                            message.sendToTarget();
//                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // delete static: error: Can't create handler inside thread that has not called Looper.prepare()
    private static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            Log.d(TAG, "handleMessage: " + " " + Thread.currentThread().getName());
            switch (message.what){
                case TRAJECTORY:
                    Bundle bundle = message.getData();
                    float[] trajectory_buffer = bundle.getFloatArray("trajectory_buffer");
                    mTrajectoryStreamCallback.onTrajectoryStream(trajectory_buffer);
                    break;
                case MAPPING:
                    bundle = message.getData();
                    byte[] mapping_buffer = bundle.getByteArray("mapping_buffer");
                    mMappingStreamCallback.onMappingStream(mapping_buffer);
                    break;
//                case ROBOT_STATUS:
//                    bundle = message.getData();
//                    byte[] status_buffer = bundle.getByteArray("status_buffer");
//                    mMappingStreamCallback.onMappingStream(status_buffer);
//                    break;
                default:
                    break;
            }
            return false;
        }
    });

    public boolean driveWheels(int leftVelocity, int rightVelocity) {
        return send(SensorCommand.kDriveWheels, rightVelocity, leftVelocity);
    }

    public boolean reset() {
        return send(SensorCommand.kReset);
    }

    public boolean start() {
        return send(SensorCommand.kStart);
    }

    public boolean startRecording() {
        return send(SensorCommand.kStartRecording);
    }

    public boolean stopRecording() {
        return send(SensorCommand.kStopRecording);
    }

    public boolean stop() {
        return send(SensorCommand.kStop);
    }

    public boolean safe() {
        return send(SensorCommand.kSafe);
    }

    public boolean full() {
        return send(SensorCommand.kFull);
    }

    public boolean mode(int opcode) {
        return send(opcode);
    }

    private boolean send(int opcode) {
//        return write(new byte[] { (byte) (opcode & 0xFF) });
        return write(new byte[] {
                (byte) (0xa5 & 0xFF), // head
                (byte) (0x5a & 0xFF),
                (byte) (2 & 0xFF), // query_id
                (byte) (1 & 0xFF), // length
                (byte) (0),
                (byte) (0),
                (byte) (0),
                (byte) (opcode & 0xFF) // data
        });
    }

    public boolean request(int a, int b) {
//        return write(new byte[] { (byte) (opcode & 0xFF) });
        return write(new byte[] {
                (byte) (0xa5 & 0xFF), // head
                (byte) (0x5a & 0xFF),
                (byte) (2 & 0xFF), // query_id
                (byte) (2 & 0xFF), // length
                (byte) (0),
                (byte) (0),
                (byte) (0),
                (byte) (a & 0xFF), // data
                (byte) (b & 0xFF)
        });
    }

    private boolean send(int opcode, byte data) {
        return write(new byte[] {
                (byte) (opcode & 0xFF),
                data,
        });
    }

    private boolean send(int opcode, int data0, int data1) {
//        return write(new byte[] {
//                (byte) (opcode & 0xFF),
//                (byte) ((data0 >> 8) & 0xFF),
//                (byte) ((data0) & 0xFF),
//                (byte) ((data1 >> 8) & 0xFF),
//                (byte) ((data1) & 0xFF),
//        });
        return write(new byte[] {
                (byte) (0xa5 & 0xFF), // head
                (byte) (0x5a & 0xFF),
                (byte) (2 & 0xFF), // query_id
                (byte) (4 & 0xFF), // length
                (byte) (0),
                (byte) (0),
                (byte) (0),
                (byte) (opcode & 0xFF), // data
                (byte) ((data0 >> 8) & 0xFF),
                (byte) ((data0) & 0xFF),
                (byte) ((data1 >> 8) & 0xFF),
                (byte) ((data1) & 0xFF)
        });
    }

    public boolean write(byte data[]) {
        try {
            synchronized (mStreamSync) {
                Log.d(TAG, "write: " + socket + "   " + outputStream);
                Log.d(TAG, "write: " + data[0] + " " + data[1]);
                outputStream.write(data);
                outputStream.flush();
                outputStream.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
