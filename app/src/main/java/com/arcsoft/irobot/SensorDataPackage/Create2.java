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
    private final Object mSocketSync = new Object();
    private final Object mStreamSync = new Object();
    private static boolean mIsConnecting = false; // 连接还是断开
    private static Socket socket; // 定义socket
    private static InputStream inputStream;
    private static OutputStream outputStream;

    private static TrajectoryStreamCallback mTrajectoryStreamCallback;
    private static MappingStreamCallback mMappingStreamCallback;

    public interface TrajectoryStreamCallback {
        void onTrajectoryStream(final float[] results);
    }

    public interface MappingStreamCallback {
        void onMappingStream(final byte[] results);
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
                String IP = "10.16.202.178";
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
        } finally {
//            mIsConnecting = false;
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
            while(true) {
                try {
                    byte[] buffer = new byte[1024 * 1024]; // 创建接收缓冲区
                    float[] Matrix_4x4 = new float[16]; // 创建接收缓冲区
                    inputStream = socket.getInputStream();
                    int length = inputStream.read(buffer); // 数据读出来，并且返回数据的长度

                    Log.d(TAG, "length: " + length);
                    if (length > 0) { // 如果数据不为空，每16个读成一个 4*4 矩阵

                        Thread.sleep(30);

                        Log.d(TAG, "run: " + buffer[0] + " " + buffer[1]);
//                        if(buffer[0] == -91 && buffer[1] == 90) {
//
//                            Log.d(TAG, "matrix data header currect");
//                            for (int i = 0; i < 16; i++) {
//                                Matrix_4x4[i] = byte2float(buffer, 4 * i + 2);
//                            }
//
//                            if (mTrajectoryStreamCallback != null && isConnecting()) {
//                                Message message = handler.obtainMessage();
//                                message.what = TRAJECTORY;
//                                Bundle bundle = new Bundle();
//                                bundle.putFloatArray("trajectory_buffer", Matrix_4x4);
//                                message.setData(bundle);
//                                message.sendToTarget();
//                            }
//                        }

//                        if(length == 40010) {
//                            Log.d(TAG, "run: " + buffer[0] + " " + buffer[1]);
//                            if (buffer[0] == 90 && buffer[1] == -91) {
//                                Log.d(TAG,"slam map data header currect");
//
//                                if (mMappingStreamCallback != null && isConnecting()) {
//                                    Message message = handler.obtainMessage();
//                                    message.what = MAPPING;
//                                    Bundle bundle = new Bundle();
//                                    bundle.putByteArray("mapping_buffer", buffer);
//                                    message.setData(bundle);
//                                    message.sendToTarget();
//                                }
//                            }
//                        }

//                        for (int i = 0; i < 16; i++) {
//                            Matrix_4x4[i] = byte2float(buffer, 4 * i);
//                        }
//
//                        Log.d(TAG, "run: " + mTrajectoryStreamCallback + " " + mMappingStreamCallback);
//
                        if (mTrajectoryStreamCallback != null && isConnecting()) {
                            Message message = handler.obtainMessage();
                            message.what = TRAJECTORY;
                            Bundle bundle = new Bundle();
                            bundle.putFloatArray("trajectory_buffer", Matrix_4x4);
                            message.setData(bundle);
                            message.sendToTarget();
                        }
//
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

                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    // delete static: error: Can't create handler inside thread that has not called Looper.prepare()
    private static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
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
        return write(new byte[] { (byte) (opcode & 0xFF) });
    }

    private boolean send(int opcode, byte data) {
        return write(new byte[] {
                (byte) (opcode & 0xFF),
                data,
        });
    }

    private boolean send(int opcode, int data0, int data1) {
        return write(new byte[] {
                (byte) (opcode & 0xFF),
                (byte) ((data0 >> 8) & 0xFF),
                (byte) ((data0) & 0xFF),
                (byte) ((data1 >> 8) & 0xFF),
                (byte) ((data1) & 0xFF),
        });
    }

    public boolean write(byte data[]) {
        try {
            synchronized (mStreamSync) {
                Log.d(TAG, "write: " + socket + "   " + outputStream);
                Log.d(TAG, "write: " + data[0]);
                outputStream.write(data);
                outputStream.flush();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
