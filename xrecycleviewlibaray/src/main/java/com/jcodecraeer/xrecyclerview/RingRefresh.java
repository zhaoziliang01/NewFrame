package com.jcodecraeer.xrecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/2/27.
 * 刷新的圆环动画
 */

public class RingRefresh extends View {
    private final static int IDLE = 0;
    private final static int REFRESHING = 1;
    private float mRadius;
    private PointF mCenterPoint;
    private RectF mArcRect;
    private Paint mPaint;
    private int state = IDLE;
    private float mStartAngle;
    private float mSweepAngle;
    private ValueAnimator mValueAnimator;
    private float mPaintWidth = 10;
    private int mRingBackground = 0xffff0000;
    private int mRingColor = 0xffffff00;

    public RingRefresh(Context context) {
        super(context);
        init(context,null);
    }

    public RingRefresh(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RingRefresh(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {

        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setColor(mRingBackground);
        createAnimation();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mRadius = (width < height? width/2.0f : height/2.0f) - mPaintWidth;
        mCenterPoint = new PointF(width/2.0f,height/2.0f);
        mArcRect = new RectF(mCenterPoint.x -mRadius, mCenterPoint.y - mRadius,mCenterPoint.x +mRadius, mCenterPoint.y + mRadius);
        setMeasuredDimension(width,height);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mPaint.setColor(mRingBackground);
        canvas.drawCircle(mCenterPoint.x,mCenterPoint.y,mRadius,mPaint);
        mPaint.setColor(mRingColor);
        if (state == IDLE) {
            canvas.drawArc(mArcRect,-90,mSweepAngle,false,mPaint);
        }else if (state == REFRESHING) {
            canvas.drawArc(mArcRect,mStartAngle, (180/5.0f),false,mPaint);
        }
        canvas.restore();

    }

    public void setSweepAngle(float sweepAngleRatio) {
        if (sweepAngleRatio < 0)sweepAngleRatio = 0;
        if (sweepAngleRatio >1) sweepAngleRatio = 1;
        mSweepAngle = 360*sweepAngleRatio;
        postInvalidate();
    }

    public void starRefresh() {
        state = REFRESHING;
        mValueAnimator.start();
    }

    private void createAnimation() {
        mValueAnimator = ValueAnimator.ofFloat(0,1);
        mValueAnimator.setDuration(1500);
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStartAngle  = (float) animation.getAnimatedValue() *360 -90;
                postInvalidate();
            }
        });
    }

    public void completeRefresh() {
        state = IDLE;
        mValueAnimator.end();
        mSweepAngle = 360;
        postInvalidate();
    }
}
