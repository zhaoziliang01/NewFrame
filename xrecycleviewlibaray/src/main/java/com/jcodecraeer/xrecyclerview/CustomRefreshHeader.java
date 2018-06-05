package com.jcodecraeer.xrecyclerview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/2/28.
 */

public class CustomRefreshHeader extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_RELEASE_TO_REFRESH = 1;
    public final static int STATE_REFRESHING = 2;
    public final static int STATE_DONE = 3;
    private View mContainer;
    private RingRefresh mRingRefresh;
    private TextView mTvText;
    private int mMeasuredHeight;
    private int mState = STATE_NORMAL;
    public ShowHomeTitle showHomeSearch;

    public CustomRefreshHeader(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContainer = LayoutInflater.from(getContext()).inflate(R.layout.custom_listview_header_x,null);
        mRingRefresh = (RingRefresh) mContainer.findViewById(R.id.ring_refresh);
        mTvText = (TextView) mContainer.findViewById(R.id.tv_text);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);
        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);

        measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();

        ViewGroup.LayoutParams layoutParams = mContainer.getLayoutParams();
        Log.d("CustomRefreshHeader","mContainer高： " + layoutParams.height);
        Log.d("CustomRefreshHeader","CustomRefreshHeader高： " + mMeasuredHeight);
    }

    public void setState(int state) {
        if (mState == state)return;
        switch (state) {
            case STATE_NORMAL:
                mTvText.setText("轻轻下拉刷新信息...");
                break;
            case STATE_RELEASE_TO_REFRESH:
                mTvText.setText("放手吧我要刷新啦...");
                break;
            case STATE_REFRESHING:
                mRingRefresh.starRefresh();
                mTvText.setText("玩命加载中...");
                break;
            case STATE_DONE:
                mRingRefresh.completeRefresh();
                mTvText.setText("刷新完成");
                break;
        }
        Log.e("刷新状态","state:" + state);
        mState = state;
    }

    public void onMove(float delta) {
        if (mState == STATE_REFRESHING ) return;
        if (getVisiableHeight() > 0 || delta > 0) {
            setVisiableHeight((int) (getVisiableHeight() + delta));
            if (getVisiableHeight() > mMeasuredHeight) {
                setState(STATE_RELEASE_TO_REFRESH);
            }else {
                setState(STATE_NORMAL);
            }
        }
    }


    public boolean releaseAction() {
        boolean isOnRefresh = false;
       /* int height = getVisiableHeight();
        if (height == 0) // not visible.
            isOnRefresh = false;

        if(getVisiableHeight() > mMeasuredHeight &&  mState < STATE_REFRESHING){
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mState == STATE_REFRESHING && height <=  mMeasuredHeight) {
            //return;
        }*/
       if (mState == STATE_RELEASE_TO_REFRESH) {
           isOnRefresh = true;
           setState(STATE_REFRESHING);
       }
        int destHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mState == STATE_REFRESHING) {
            destHeight = mMeasuredHeight;
        }
        smoothScrollTo(destHeight);

        return isOnRefresh;
    }

    public int getVisiableHeight() {
        int height = 0;
        ViewGroup.LayoutParams layoutParams = mContainer.getLayoutParams();
        height = layoutParams.height;
        return height;
    }

    public void setVisiableHeight(int height) {
        if (height < 0 ) height = 0;
       // Log.e("高度","height:" + height);
        ViewGroup.LayoutParams layoutParams = mContainer.getLayoutParams();
        layoutParams.height = height;

        if(height>0){
            if(showHomeSearch!=null){
                showHomeSearch.isShow4Title(false);
            }

        }else{
            if(showHomeSearch!=null){
                showHomeSearch.isShow4Title(true);
            }

        }
        mContainer.setLayoutParams(layoutParams);
        if (mState == STATE_NORMAL) {
            float sweepAngle = getVisiableHeight()*1.0f/mMeasuredHeight;
            mRingRefresh.setSweepAngle(sweepAngle);
        }
    }

    public void refreshComplate(){
        setState(STATE_DONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                smoothScrollTo(0);
            }
        }, 500);
    }

    private void smoothScrollTo(int destHeight) {
        final int state = mState;
        ValueAnimator animator = ValueAnimator.ofInt(getVisiableHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisiableHeight((int) animation.getAnimatedValue());
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (state == STATE_DONE) {

                    setState(STATE_NORMAL);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public int getState() {
        return mState;
    }


    public interface ShowHomeTitle {
        void isShow4Title(boolean isShow);
    }

    public void setOnShowListener(ShowHomeTitle showHomeSearch) {
        this.showHomeSearch = showHomeSearch;

    }





}
