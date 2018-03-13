package com.arcsoft.irobot.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 * Created by yj2595 on 2018/3/5.
 */

public class RobotJoyStick extends View {

    private static final boolean DEBUG = false;
    private final String TAG = this.getClass().getSimpleName();

    private Paint mPaint;
    private RockerCallBack mCallback;

    private float mSmallCircleCx = -1;
    private float mSmallCircleCy = -1;

    public interface RockerCallBack {
        void setVelocityLR(int left, int right);
    }

    public RobotJoyStick(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public void setCallback(RockerCallBack mCallback) {
        this.mCallback = mCallback;
    }

    public void setVelocity(float rad, int velocityMultiple) {
        int velocityL;
        int velocityR;
        int rotateDeviation; // 旋转时左右速度差值
        int straightDeviation = velocityCorrection(velocityMultiple); // 直行时左右轮速度差值

        // finished: (1)细分区域范围，标注在摇杆上; (2)速度标定直线行驶
        // 角速度设定为 rad/1.5s
        if (rad == 0 && velocityMultiple == 0) { // ACTION_UP事件重置左右轮速度为0
            velocityL = 0;
            velocityR = 0;
        } else if (rad <= 0.3489 && rad > -0.3489) { // 右旋转
            velocityL = velocityMultiple;
            velocityR = -velocityMultiple;
        } else if (rad <= 1.2211 && rad > 0.3489) { // 右下旋转
            rotateDeviation = (int) (235 * rad / 3);
            velocityL = -velocityMultiple - rotateDeviation;
            velocityR = -(velocityMultiple + straightDeviation) + rotateDeviation;
        } else if (rad <= 1.9189 && rad > 1.2211) { // 后退
            velocityL = -velocityMultiple;
            velocityR = -(velocityMultiple + straightDeviation);
        } else if (rad <= 2.7911 && rad > 1.9189) { // 左下旋转
            rotateDeviation = (int) (235 * (3.14 - rad) / 3);
            velocityL = -velocityMultiple + rotateDeviation;
            velocityR = -(velocityMultiple + straightDeviation)- rotateDeviation;
        } else if (rad <= -0.3489 && rad > -1.2211) { // 右上旋转
            rotateDeviation = (int) (235 * Math.abs(rad) / 3);
            velocityL = velocityMultiple + rotateDeviation;
            velocityR = velocityMultiple + straightDeviation - rotateDeviation;
        } else if (rad <= -1.2211 && rad > -1.9189) { // 前进
            velocityL = velocityMultiple;
            velocityR = velocityMultiple + straightDeviation;
        } else if (rad <= -1.9189 && rad > -2.7911){ // 左上旋转
            rotateDeviation = (int) (235 * (rad + 3.14) / 3);
            velocityL = velocityMultiple - rotateDeviation;
            velocityR = velocityMultiple + straightDeviation + rotateDeviation;
        } else { // 左旋转
            velocityL = -velocityMultiple;
            velocityR = velocityMultiple;
        }
        if (this.mCallback != null){
            mCallback.setVelocityLR(velocityL, velocityR);
        }

        if (DEBUG) Log.d(TAG, "velocity: L=" + velocityL + ", R=" + velocityR);
        this.invalidate();
    }

    public void resetVelocity() {
        setVelocity(0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float cx = getWidth() * 0.5f;
        float cy = getHeight() * 0.5f;
        float r = Math.min(getWidth(), getHeight()) * 0.45f; // 背景圆形的X,Y坐标以及半径r

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            // 触摸点在0.3个半径内不产生移动
            if ((event.getX() - cx) * (event.getX() - cx) + (event.getY() - cy) * (event.getY() - cy) < (r * 0.3f) * (r * 0.3f)) {
                setVelocity(0, 0);
                mSmallCircleCx = event.getX();
                mSmallCircleCy = event.getY();
            } else {
                float dx = event.getX() - cx;
                float dy = event.getY() - cy;

                float angle = (float) Math.atan2(dy, dx);
                float length = (float) Math.sqrt(dx*dx + dy*dy);

                int mMaxVelocity = 100;
                float velocity = mMaxVelocity * Math.min(1.0f, (length - r * 0.3f) / (r - r * 0.3f));
                setVelocity(angle, (int) velocity);

                if (length >= r) {
                    mSmallCircleCx = r * (float) Math.cos(angle) + cx;
                    mSmallCircleCy = r * (float) Math.sin(angle) + cy;
                } else {
                    mSmallCircleCx = event.getX();
                    mSmallCircleCy = event.getY();
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mSmallCircleCx = cx;
            mSmallCircleCy = cy;
            resetVelocity();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float cx = getWidth() * 0.5f;
        float cy = getHeight() * 0.5f;
        float r = Math.min(getWidth(), getHeight()) * 0.45f;

        if (mSmallCircleCx < 0) {
            mSmallCircleCx = cx;
        }
        if (mSmallCircleCy < 0) {
            mSmallCircleCy = cy;
        }

        canvas.drawColor(Color.BLACK);

        mPaint.setAntiAlias(true);
        mPaint.setColor(0x70a0a0a0);
        canvas.drawCircle(mSmallCircleCx, mSmallCircleCy, r * 0.5f, mPaint);

        mPaint.setAntiAlias(true);
        mPaint.setColor(0x70404040);
        canvas.drawCircle(cx, cy, r, mPaint);

        //设置刻度线线宽
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.WHITE);
        //将坐标原点移到圆心处
        canvas.translate(cx, cy);
        for (int i = 0; i < 360; i++) {
            //刻度线长度设置为100
            if (i == 20){
                canvas.rotate(20);
                canvas.drawLine(r - 50, 0, r + 50, 0, mPaint);
                canvas.rotate(50);
            }
            if (i == 70){
                canvas.drawLine(r - 50, 0, r + 50, 0, mPaint);
                canvas.rotate(40);
            }
            if (i == 110){
                canvas.drawLine(r - 50, 0, r + 50, 0, mPaint);
                canvas.rotate(50);
            }
            if (i == 160){
                canvas.drawLine(r - 50, 0, r + 50, 0, mPaint);
                canvas.rotate(40);
            }
            if (i == 200){
                canvas.drawLine(r - 50, 0, r + 50, 0, mPaint);
                canvas.rotate(50);
            }
            if (i == 250){
                canvas.drawLine(r - 50, 0, r + 50, 0, mPaint);
                canvas.rotate(40);
            }
            if (i == 290){
                canvas.drawLine(r - 50, 0, r + 50, 0, mPaint);
                canvas.rotate(50);
            }
            if (i == 340){
                canvas.drawLine(r - 50, 0, r + 50, 0, mPaint);
                canvas.rotate(20);
            }
        }
    }

    // 直行时速度相同向右偏，纠正右轮速度以尽可能地直线行驶
    private int velocityCorrection(int velocity){
        int deviation;
        // velocityL: 0 25 50 75 100 125 150 175 200 225 250 275 300
        // velocityR: 0 40 68 96 140 153 153 182 210 239 275 300 340
        if (velocity >= 0 && velocity < 25){
            deviation = calculate(0, 0, 25, 40, velocity);
        } else if (velocity > 25 && velocity <= 50) {
            deviation = calculate(25, 40, 50, 68, velocity);
        } else if (velocity > 50 && velocity <= 75) {
            deviation = calculate(50, 68, 75, 96, velocity);
        } else if (velocity > 75 && velocity <= 100) {
            deviation = calculate(75, 96, 100, 140, velocity);
        } else if (velocity > 100 && velocity <= 125) {
            deviation = calculate(100, 140, 125, 153, velocity);
        } else if (velocity > 125 && velocity <= 150) {
            deviation = calculate(125, 153, 150, 153, velocity);
        } else if (velocity > 150 && velocity <= 175) {
            deviation = calculate(150, 153, 175, 182, velocity);
        } else if (velocity > 175 && velocity <= 200) {
            deviation = calculate(175, 182, 200, 210, velocity);
        } else if (velocity > 200 && velocity <= 225) {
            deviation = calculate(200, 210, 225, 239, velocity);
        } else if (velocity > 225 && velocity <= 250) {
            deviation = calculate(225, 239, 250, 275, velocity);
        } else if (velocity > 250 && velocity <= 275) {
            deviation = calculate(250, 275, 275, 300, velocity);
        } else {
            deviation = calculate(275, 300, 300, 340, velocity);
        }
        return deviation;
    }

    private int calculate(float x1, float y1, float x2, float y2, float x){
        int deviation;
        deviation = (int) ((y2- y1) / (x2 - x1) * (x - x1));
        return deviation;
    }
}
