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

    private static final String TAG = "com.arcsoft.irobot";
    private static final int RECEIVE = 1;
    private final Object mSocketSync = new Object();
    private final Object mStreamSync = new Object();
    private static boolean mConnecting = false; // 连接还是断开
    private static Socket socket = null; // 定义socket
    private static InputStream inputStream;
    private static OutputStream outputStream;

    private static StreamCallback mStreamCallback;

    public interface StreamCallback {
        void onStream(final float[] results);
    }

    public Create2(){
    }

    public synchronized boolean connect(){
        Log.d(TAG, "connect: ");
        try {
            mConnecting = true;
            if (socket == null) {
                // 用InetAddress方法获取ip地址
                String IP = "192.168.123.1";
//                String IP = "192.168.100.1";
                String PORT = "8888";
                InetAddress ipAddress = InetAddress.getByName(IP);
                int port = Integer.valueOf(PORT); // 获取端口号
                Log.d(TAG, "run: " + ipAddress + "  " + port);
                socket = new Socket(ipAddress, port); // 创建连接地址和端口
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();

                Log.d(TAG, "run: " + socket + "   " + outputStream + " " + inputStream);

                // 在创建完连接后启动接收线程
                Receive_Thread receive_Thread = new Receive_Thread();
                receive_Thread.start();
            }
            Log.d(TAG, "connect: " + mConnecting);
            return true;
        } catch (Exception e) {
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
        if (null != socket) {
            synchronized (mSocketSync) {
                try {
                    outputStream.flush();
                    inputStream.close();
                    outputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    socket = null;
                }
            }
        }
        Log.d(TAG, "disconnect: " + mConnecting);
        mConnecting = false;
    }

    public void stream(StreamCallback callback){
        mStreamCallback = callback;
    }

    public synchronized boolean isConnecting() {
        return mConnecting;
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
                    byte[] buffer = new byte[1024]; // 创建接收缓冲区
                    float[] Matrix_4x4 = new float[16]; // 创建接收缓冲区
                    int length = inputStream.read(buffer); // 数据读出来，并且返回数据的长度

                    Log.d(TAG, "run: " + length);
                    if (length > 0) { // 如果数据不为空，每16个读成一个 4*4 矩阵

                        Thread.sleep(30);

                        for (int i = 0; i < 16; i++) {
                            Matrix_4x4[i] = byte2float(buffer, 4 * i);
                        }

                        if (mStreamCallback != null && isConnecting()) {
                            Message message = handler.obtainMessage();
                            message.what = RECEIVE;
                            Bundle bundle = new Bundle();
                            bundle.putFloatArray("buffer", Matrix_4x4);
                            message.setData(bundle);
                            message.sendToTarget();
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case RECEIVE:
                    Bundle bundle = message.getData();
                    float[] receive_buffer = bundle.getFloatArray("buffer");
                    mStreamCallback.onStream(receive_buffer);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

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

    public boolean driveWheels(int leftVelocity, int rightVelocity) {
        return send(SensorCommand.kDriveWheels, rightVelocity, leftVelocity);
    }

    public boolean reset() {
        return send(SensorCommand.kReset);
    }

    public boolean start() {
        return send(SensorCommand.kStart);
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

    private boolean send(int opcode) {
        return write(new byte[] { (byte) (opcode & 0xFF) });
    }

    private boolean send(int opcode, byte data) {
        return write(new byte[] {
                (byte) opcode,
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
}
