package com.arcsoft.irobot.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by yj2595 on 2018/4/3.
 */

public class MySurfaceView  extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "MySurfaceView";
    // 每30帧刷新一次屏幕
    public static final int TIME_IN_FRAME = 30;
    // SurfaceHolder
    private SurfaceHolder mSurfaceHolder;
    // 用于绘图的Canvas
    private Canvas mCanvas;
    // 子线程标志位
    private boolean mIsRunning;

    private Paint mCirclePaint;
    private Paint mTextPaint;
    private Paint mPathPaint;
    private PointF mCurrentPosition;

    private float mOrientation = 0;
    private Paint mArrowPaint; // 箭头圆环
    private Path mArrowPath; // 箭头路径

    private int cR = 10; // 圆点半径
    private int arrowR = 20; // 箭头半径

    private float mCurX = 200;
    private float mCurY = 200;

    private RectF mArrowRect;

    private Bitmap mBitmap;
    private List<PointF> mPointList = new ArrayList<>();

    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
        initPaint();     // 初始化画笔
        initArrowPath(); // 初始化箭头路径
    }

    private void initView() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        //mHolder.setFormat(PixelFormat.OPAQUE);
    }

    private void initPaint() {
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(40);

        mPathPaint = new Paint();
        mPathPaint.setColor(Color.GREEN);
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStyle(Paint.Style.FILL);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.GREEN);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mArrowPaint = new Paint(mCirclePaint);
        mArrowPaint.setStyle(Paint.Style.STROKE);
        mArrowPaint.setStrokeWidth(5);
    }

    /**
     * 初始化箭头路径
     */
    private void initArrowPath() {

        mCurrentPosition = new PointF(mCurX, mCurY);

        // 初始化箭头路径
        mArrowPath = new Path();
        mArrowPath.arcTo(new RectF(-arrowR, -arrowR, arrowR, arrowR), 0, -180);
        mArrowPath.lineTo(0, -3 * arrowR);
        mArrowPath.close();

        mArrowRect = new RectF(-arrowR * 0.8f, -arrowR * 0.8f, arrowR * 0.8f, arrowR * 0.8f);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsRunning = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsRunning = false;
    }

    @Override
    public void run() {
        while (mIsRunning) {

            // 取得更新之前的时间
            long startTime = System.currentTimeMillis();

            // 在这里加上线程安全锁
            synchronized (mSurfaceHolder) {
                // 拿到当前画布,然后锁定
                mCanvas = mSurfaceHolder.lockCanvas();
                draw();
                // 绘制结束后解锁显示在屏幕上
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }

            // 取得更新结束的时间
            long endTime = System.currentTimeMillis();

            // 计算出一次更新的毫秒数
            int diffTime  = (int)(endTime - startTime);

            // 确保每次更新时间为30帧
            while(diffTime <= TIME_IN_FRAME) {
                diffTime = (int)(System.currentTimeMillis() - startTime);
                // 线程等待, 切换到其它线程执行
                Thread.yield();
            }

        }
    }

    //绘图操作
    private void draw() {

    }
}
