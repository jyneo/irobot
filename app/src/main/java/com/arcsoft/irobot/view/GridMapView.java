package com.arcsoft.irobot.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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

    private float mOrientation = 0;
    private Paint mStrokePaint; // 箭头圆环
    private Path mArrowPath; // 箭头路径
    Path mPath;

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

        mPath = new Path();
        // 初始化箭头路径
        mArrowPath = new Path();
        mArrowPath.arcTo(new RectF(-arrowR, -arrowR, arrowR, arrowR), 0, -180);
        mArrowPath.lineTo(0, -3 * arrowR);
        mArrowPath.close();

//        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Log.d("GridMapView", "initArrowPath: " + mBitmap);
        mSrcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        mDstRect = new Rect(0, 0, getWidth(), getHeight());
        mArrowRect = new RectF(-arrowR * 0.8f, -arrowR * 0.8f, arrowR * 0.8f, arrowR * 0.8f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null || mBitmap == null)
            return;
        Log.d("GridMapView", "onDraw: " + mBitmap);
        canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, null); // 将mBitmap绘到canLock

        // 画点
        for (PointF pointF : mPointList) {
            canvas.drawCircle(pointF.x, pointF.y, cR, mPaint);

            mPath.moveTo(mCurX, mCurY);
            mPath.lineTo(pointF.x, pointF.y);
            canvas.drawPath(mPath, mPaint);
        }

        // 画箭头
        canvas.save(); // 保存画布
        canvas.translate(mCurX, mCurY); // 平移画布
        canvas.rotate(mOrientation); // 转动画布
        canvas.drawPath(mArrowPath, mPaint);
        canvas.drawArc(mArrowRect, 0, 360, false, mStrokePaint);
        canvas.restore(); // 恢复画布

        super.onDraw(canvas);
    }

    public void setGridMap(PointF pointF, float orientation) {
//        mDestinationPosition = pointF;
        if (orientation < 0)
            orientation = orientation + 360;
        mOrientation = orientation;
        mCurX = pointF.x;
        mCurY = pointF.y;
        mPointList.add(new PointF(mCurX, mCurY));
        Log.d("GridMapView", "setGridMap: " + mCurX + " " + mCurY + " " + mOrientation);
//        mPath.moveTo(mCurrentPosition.x, mCurrentPosition.y);
//
//        连线
//        mPath.quadTo(mCurrentPosition.x, mCurrentPosition.y, mDestinationPosition.x, mDestinationPosition.y);
//        mCurrentPosition = mDestinationPosition;

        //刷新view
        invalidate();
    }

}
