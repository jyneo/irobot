package com.arcsoft.irobot.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *
 * Created by yj2595 on 2018/3/9.
 */

public class GridMapView extends View {

    private static final String TAG = "GridMapView";
    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mPathPaint;
    private PointF mCurrentPosition;

    private float mOrientation = 0;
    private Paint mStrokePaint; // 箭头圆环
    private Path mArrowPath; // 箭头路径

    private int cR = 10; // 圆点半径
    private int arrowR = 20; // 箭头半径

    private float mCurX = 200;
    private float mCurY = 200;

    private Rect mSrcRect;
    private Rect mDstRect;
    private RectF mArrowRect;

    private Bitmap mBitmap;
    private List<PointF> mPointList = new ArrayList<>();

    public GridMapView(Context context){
        this(context, null);
    }

    public GridMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();     // 初始化画笔
        initArrowPath(); // 初始化箭头路径
    }

    private void initPaint() {
        Log.d("GridMapView", "initPaint: ");
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(40);

        mPathPaint = new Paint();
        mPathPaint.setColor(Color.GREEN);
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStyle(Paint.Style.FILL);

        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mStrokePaint = new Paint(mPaint);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(5);
    }

    /**
     * 初始化箭头
     */
    private void initArrowPath() {

        mCurrentPosition = new PointF(mCurX, mCurY);

        // 初始化箭头路径
        mArrowPath = new Path();
        mArrowPath.arcTo(new RectF(-arrowR, -arrowR, arrowR, arrowR), 0, -180);
        mArrowPath.lineTo(0, -3 * arrowR);
        mArrowPath.close();

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        Log.d(TAG, "initArrowPath: " + width / density + " " + height / density);
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

//        mSrcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
//        mDstRect = new Rect(0, 0, getWidth(), getHeight());
        mArrowRect = new RectF(-arrowR * 0.8f, -arrowR * 0.8f, arrowR * 0.8f, arrowR * 0.8f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null || mBitmap == null)
            return;

        canvas.save();

        int offsetX = 10;
        int offsetY = 40;

        if (mBitmap != null) {
            canvas.drawText("[map bitmap: width=" + mBitmap.getWidth() + "  height=" + mBitmap.getHeight() + "]", offsetX, offsetY, mTextPaint);
        } else {
            canvas.drawText("[no map]", offsetX, offsetY, mTextPaint);
        }

        if (mCurrentPosition != null) {
            offsetY += 50;
            canvas.drawText("[current position: x=" + mCurrentPosition.x + "  y=" + mCurrentPosition.y + "]", offsetX, offsetY, mTextPaint);
        }

        if (mOrientation >= 0 && mOrientation < 360) {
            offsetY += 50;
            canvas.drawText("[current orientation: angle=" + mOrientation + "]", offsetX, offsetY, mTextPaint);
        }

        Log.d(TAG, "onDraw: " + Arrays.toString(mTrajectoryPath));
        Log.d(TAG, "onDraw: " + mPointList.toString());
        if (mTrajectoryPath != null) {
            canvas.drawLines(mTrajectoryPath, mPathPaint);
        }

        canvas.drawBitmap(mBitmap, 0, 0, null); // 将mBitmap绘到canLock

        // 画点
        for (PointF pointF : mPointList) {
            canvas.drawCircle(pointF.x, pointF.y, cR, mPaint);

            mCurX = pointF.x;
            mCurY = pointF.y;

        }

        // 画箭头
        canvas.save(); // 保存画布
        canvas.translate(mCurX, mCurY); // 平移画布
        canvas.rotate(mOrientation); // 转动画布
        canvas.drawPath(mArrowPath, mPaint);
        canvas.drawArc(mArrowRect, 0, 360, false, mStrokePaint);
        canvas.restore(); // 恢复画布

        canvas.restore();
    }

    private float[] mTrajectoryPath;
    public void setGridMap1(Float[] array) {
        // 存储轨迹的点数据
        float[] position = new float[array.length - array.length / 3];
        for (int i = 0; i < array.length / 3; i++) {
            position[i * 2] = array[i * 3];
            position[i * 2 + 1] = array[i * 3 + 1];
        }

        // 轨迹的点数据转换成canvas path格式的数组
        mTrajectoryPath = getPath(position);

        for (int i = 0; i < array.length / 3; i++) {
            if (array[i * 3 + 2] < 0)
                mOrientation = array[i * 3 + 2] + 360; // 获取方向
            mOrientation = array[i * 3 + 2];

            float x = array[i * 3];
            float y = array[i * 3 + 1];

            mCurrentPosition.x = x;
            mCurrentPosition.y = y;
            mPointList.add(new PointF(x, y)); // 存储所有轨迹点
        }

        // 刷新view
        invalidate();
    }

    public void clearData() {
        mBitmap = null;
        mTrajectoryPath = null;
        mCurrentPosition = null;
        mOrientation = 0;
    }

    private float[] getPath(float[] position) {
        if (position.length > 4) {
            int count = (position.length / 2 - 2) * 2 + 2;
            Log.d(TAG, "getPath: " + position.length + " " + count);
            float[] path = new float[count * 2];
            for (int i = 0; i < position.length / 2; i++) {
                if (i == 0) {
                    path[((i * 2 - 1) + 1) * 2] = position[i * 2];
                    path[((i * 2 - 1) + 1) * 2 + 1] = position[i * 2 + 1];
                } else if (i == position.length / 2 - 1 ) {
                    path[(i * 2 - 1) * 2] = position[i * 2];
                    path[(i * 2 - 1) * 2 + 1] = position[i * 2 + 1];
                } else {
                    path[(i * 2 - 1) * 2] = position[i * 2];
                    path[(i * 2 - 1) * 2 + 1] = position[i * 2 + 1];
                    path[((i * 2 - 1) + 1) * 2] = position[i * 2];
                    path[((i * 2 - 1) + 1) * 2 + 1] = position[i * 2 + 1];
                }
            }
            return path;
        } else {
            return null;
        }
    }

    public void setGridMap(Point pointF, int orientation) {
        if (orientation < 0)
            orientation = orientation + 360;
        mOrientation = orientation;

        if (!mPointList.isEmpty()){
            mCurX = mPointList.get(0).x;
            mCurY = mPointList.get(0).y;
        }

        Log.d("GridMapView", "setGridMap: " + pointF.x + " " + pointF.y + " " + mOrientation);
        mPointList.add(new PointF(pointF.x + getWidth() / 2, pointF.y + getHeight() / 2));

        // 刷新view
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

}
