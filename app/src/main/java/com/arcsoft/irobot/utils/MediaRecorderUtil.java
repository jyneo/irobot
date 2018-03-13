package com.arcsoft.irobot.utils;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.IOException;

/**
 *
 * Created by yj2595 on 2018/3/12.
 */

public class MediaRecorderUtil {

    private MediaRecorder mediaRecorder;
    // 录像camera
    private Camera camera;
    // 用于判断前置摄像头还是后置摄像头
    private int cameraPosition = 1;
    // 判断前置摄像头还是后置摄像头
    private boolean isCheck;

    private SurfaceHolder.Callback callback;
    // 上下文
    private Context context;
    // 容器
    private SurfaceView surfaceView;

    /**
     * 创建录制环境
     *
     * @param surfaceView
     *            需要用到的surfaceview
     * @param context
     *            上下文
     */
    public void create(SurfaceView surfaceView, Context context) {
        this.context = context;
        this.surfaceView = surfaceView;
        surfaceView.getHolder()
                .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.setKeepScreenOn(true);
        callback = new SurfaceHolder.Callback() {

            // 在控件创建的时候，进行相应的初始化工作
            public void surfaceCreated(SurfaceHolder holder) {
                // 打开相机，同时进行各种控件的初始化mediaRecord等
                camera = Camera.open();
                mediaRecorder = new MediaRecorder();
            }

            // 当控件发生变化的时候调用，进行surfaceView和camera进行绑定，可以进行画面的显示
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
                doChange(holder);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

        };
        // 为SurfaceView设置回调函数
        surfaceView.getHolder().addCallback(callback);
    }

    // 当我们的程序开始运行，即使我们没有开始录制视频，我们的surFaceView中也要显示当前摄像头显示的内容
    private void doChange(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            // 设置surfaceView旋转的角度，系统默认的录制是横向的画面，把这句话注释掉运行你就会发现这行代码的作用
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换摄像头
     */
    public void changeCamara() {
        // 切换前后摄像头
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
            if (cameraPosition == 1) {
                // 现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
                    // CAMERA_FACING_BACK后置
                    camera.stopPreview();// 停掉原来摄像头的预览
                    camera.release();// 释放资源
                    camera = null;// 取消原来摄像头
                    camera = Camera.open(i);// 打开当前选中的摄像头
                    try {

                        camera.setPreviewDisplay(surfaceView.getHolder());// 通过surfaceview显示取景画面
                        camera.setDisplayOrientation(90);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    camera.startPreview();// 开始预览
                    cameraPosition = 0;
                    isCheck = true;
                    break;
                }
            } else {
                // 现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
                    // CAMERA_FACING_BACK后置
                    camera.stopPreview();// 停掉原来摄像头的预览
                    camera.release();// 释放资源
                    camera = null;// 取消原来摄像头
                    camera = Camera.open(i);// 打开当前选中的摄像头
                    try {

                        camera.setPreviewDisplay(surfaceView.getHolder());// 通过surfaceview显示取景画面
                        camera.setDisplayOrientation(90);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    camera.startPreview();// 开始预览
                    cameraPosition = 1;
                    isCheck = false;
                    break;
                }
            }

        }
    }

    public void stopRecord() {
        // 当结束录制之后，就将当前的资源都释放
        mediaRecorder.release();
        camera.release();
        mediaRecorder = null;
        // 然后再重新初始化所有的必须的控件和对象
        camera = Camera.open();
        if (isCheck) {

        } else {

        }
        mediaRecorder = new MediaRecorder();
        doChange(surfaceView.getHolder());
    }

    /**
     * 开始录制
     *
     * @param path
     *            录制保存的路径
     * @param name
     *            文件名称
     */
    public void startRecord(String path, String name) {
        // 先释放被占用的camera，在将其设置为mediaRecorder所用的camera
        // 解锁并将摄像头指向MediaRecorder
        camera.unlock();
        mediaRecorder.setCamera(camera);

        // 指定输入源
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // mediaRecorder.setCamera(camera)如果不用这个方式 那么下面这句话可以加入用来设置视频清晰度的 不然会产生冲突
        // 报错 我也不知道是为什么
        // mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        // 设置输出格式和编码格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // 设置编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // 这句必须加上 用来搞视频清晰度的
        mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
        // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
        mediaRecorder.setVideoSize(1280, 720);
        // 还有个设置焦距的 发现没卵用 我就不加了 自己找去
        //
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 保存的路径以及文件名称
        mediaRecorder.setOutputFile(path + "/" + name + ".mp4");

        // 预览输出
        mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());

        // 判断是前置摄像头还是后置摄像头 然后设置视频旋转 如果不加上 后置摄像头没有问题 但是前置摄像头录制的视频会导致上下翻转
        if (isCheck) {
            mediaRecorder.setOrientationHint(180);
        } else {
            mediaRecorder.setOrientationHint(90);
        }
        // 开始录制
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
