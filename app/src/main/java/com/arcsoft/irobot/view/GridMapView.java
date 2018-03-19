package com.arcsoft.irobot.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.arcsoft.irobot.R;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * Created by yj2595 on 2018/3/9.
 */

public class GridMapView extends View {

    private Paint mPaint;

//    private ScaleGestureDetector mScaleGestureDetector = null;

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
//        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureListener());
    }

    private void initPaint() {
        Log.d("GridMapView", "initPaint: ");
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
        Log.d("GridMapView", "initArrowPath: ");

        // 初始化箭头路径
        mArrowPath = new Path();
        mArrowPath.arcTo(new RectF(-arrowR, -arrowR, arrowR, arrowR), 0, -180);
        mArrowPath.lineTo(0, -3 * arrowR);
        mArrowPath.close();

//        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Log.d("GridMapView", "initArrowPath: " + mBitmap);
        mSrcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        Log.d("0000000", "initArrowPath: " + mBitmap.getWidth() + " " + getWidth());
        // 计算左边位置
//        int left = mTotalWidth / 2 - mBitmap.getWidth() / 2;
        // 计算上边位置
//        int top = mTotalHeight / 2 - mBitmap.getHeight() / 2;
//        mDstRect = new Rect(left, top, mBitmap.getWidth() + left, mBitmap.getHeight() + top);
        mDstRect = new Rect(0, 0, getWidth(), getHeight());
        mArrowRect = new RectF(-arrowR * 0.8f, -arrowR * 0.8f, arrowR * 0.8f, arrowR * 0.8f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null || mBitmap == null)
            return;
        Log.d("GridMapView", "onDraw: " + mBitmap);
        canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, null); // 将mBitmap绘到canLock

        // 画点
        for (PointF pointF : mPointList) {
            canvas.drawCircle(pointF.x, pointF.y, cR, mPaint);

            canvas.drawLine(mCurX, mCurY, pointF.x, pointF.y, mPaint);

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

        //刷新view
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

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        // 返回给ScaleGestureDetector来处理
//        return mScaleGestureDetector.onTouchEvent(event);
//    }
//
//    public class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
//
//        private float scale;
//        private float preScale = 1; // 默认前一次缩放比例为1
//        GridMapView gridMapView = getRootView().findViewById(R.id.grid_map_view);
//
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//
//            float previousSpan = detector.getPreviousSpan();
//            float currentSpan = detector.getCurrentSpan();
//            if (currentSpan < previousSpan) {
//                // 缩小
//                // scale = preScale-detector.getScaleFactor()/3;
//                scale = preScale - (previousSpan - currentSpan) / 1000;
//            } else {
//                // 放大
//                // scale = preScale+detector.getScaleFactor()/3;
//                scale = preScale + (currentSpan - previousSpan) / 1000;
//            }
//
//            // 缩放view
//            ViewHelper.setScaleX(gridMapView, scale ); // x方向上缩小
//            ViewHelper.setScaleY(gridMapView, scale ); // y方向上缩小
//
//            return false;
//        }
//
//        @Override
//        public boolean onScaleBegin(ScaleGestureDetector detector) {
//            // 一定要返回true才会进入onScale()这个函数
//            return true;
//        }
//
//        @Override
//        public void onScaleEnd(ScaleGestureDetector detector) {
//            preScale = scale; //记录本次缩放比例
//        }
//    }

}
