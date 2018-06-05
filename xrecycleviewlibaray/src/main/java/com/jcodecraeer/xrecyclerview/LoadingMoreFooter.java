package com.jcodecraeer.xrecyclerview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.progressindicator.AVLoadingIndicatorView;

public class LoadingMoreFooter extends LinearLayout {

    public final static int STATE_LAODING = 0;//  加载中
    public final static int STATE_NORMAL = 1;// 正常状态
    public final static int STATE_COMPLETE = 2; // 加载完成
    public final static int STATE_NOMORE = 3; // 没有了
    public final static int STATE_BAD_NET = 4; //当前网络异常
    public final static int STATE_LOAD_MORE = 5; //首页加载更多
    public final static int STATE_LOAD_MORE_SHOPCART = 6;
    public final static int STATE_LOAT_ALL_DATA_COMPLETE = 7;//数据全部加载完成
    public final static int STATE_LOAT_ALL_DATA_NOCOMPLETE=8;
    public int curState;
    private SimpleViewSwithcer progressCon;
    private Context mContext;
    private TextView mText;
    /**  当前已经没有更多数据*/
    private View mFootView4NoMore;
    /**  网络异常*/
    private View mFootView4BadNet;
    /**首页加载全部商品*/
    private View mFootViewScannAll;
    /**购物车为空时加载更多的按钮*/
    private View mFootView4ShopCart;
    private View mFootView4LoadDataComplete;

    private View  mLoadView;
    private FootView footView;
    private TextView  tvLoadAll ;
    /**  点击重试 逻辑 */
    private XRecyclerView.LoadingListener mLoadingListener;
    private int mOrigHeight;

    public LoadingMoreFooter(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setLoadingListener(XRecyclerView.LoadingListener mLoadingListener) {
        this.mLoadingListener = mLoadingListener;
    }

    public void initView(Context context){
        mContext = context;
        setGravity(Gravity.CENTER);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        footView = new FootView(context);
//        mLoadView = footView.addFooterView();
//        addView(mLoadView);
        progressCon = new SimpleViewSwithcer(context);
        progressCon.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 100));
        AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(this.getContext());
        progressView.setIndicatorColor(0xffB5B5B5);
        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
        progressCon.setView(progressView);
        //自定义的下拉加载更多
        addView(progressCon);
        mText = new TextView(context);
        mText.setText("正在加载...");

//        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
        layoutParams.setMargins((int) getResources().getDimension(R.dimen.textandiconmargin), 0, 0, 0);
        mText.setGravity(Gravity.CENTER);
        mText.setLayoutParams(layoutParams);
        addView(mText);
        mOrigHeight = layoutParams.height;

    }

    public void setProgressStyle(int style) {
        if(style == ProgressStyle.SysProgress){
            progressCon.setView(new ProgressBar(mContext, null, android.R.attr.progressBarStyle));
        }else{
            AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xffB5B5B5);
            progressView.setIndicatorId(style);
            progressCon.setView(progressView);
        }
    }
    public TextView getLoadMoreBtn(){

        if(mFootViewScannAll!=null){
             tvLoadAll= ((TextView) mFootViewScannAll.findViewById(R.id.tv_footer_scann));
            return tvLoadAll;
        }
        return null;
    }
    /**获得购物车的点击更多按钮*/
    public TextView getLoadMoreBtnShopCart() {
        if (mFootView4ShopCart != null) {
            tvLoadAll = (TextView) mFootView4ShopCart.findViewById(R.id.tv_footer_scann);
            return tvLoadAll;
        }
        return null;
    }

    public void  setState(int state) {
        hideLayout();
        curState = state;
        switch(state) {
            case STATE_NORMAL:
//                footView.stopAnimaltion();
                progressCon.setVisibility(View.GONE);
                mText.setVisibility(View.VISIBLE);
                mText.setText("上滑加载更多");
//                mLoadView.setVisibility(View.GONE);
                if (mFootView4NoMore!=null){
                    mFootView4NoMore.setVisibility(View.GONE);
                }
                if (mFootViewScannAll!=null){
                    mFootViewScannAll.setVisibility(View.GONE);
                }
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_LAODING:
                progressCon.setVisibility(View.VISIBLE);
                mText.setText(mContext.getText(R.string.listview_loading));
                mText.setVisibility(View.VISIBLE);
                if (mFootViewScannAll!=null){
                    mFootViewScannAll.setVisibility(View.GONE);
                }
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
//                mLoadView.setVisibility(View.GONE);
                mText.setText(mContext.getText(R.string.listview_loading));

                this.setVisibility(View.GONE);
                if (footView != null)
                    footView.stopAnimaltion();
                if (mFootViewScannAll!=null){
                    mFootViewScannAll.setVisibility(View.GONE);
                }
                break;
            case STATE_NOMORE:
                //左右添加两条线
                if (mFootView4NoMore==null){
                    mFootView4NoMore = LayoutInflater.from(mContext).inflate(R.layout.item_no_more_goods, null);
                    addView(mFootView4NoMore);
                }
                if (mFootView4NoMore!=null){
                    mFootView4NoMore.setVisibility(View.VISIBLE);
                }
                mText.setVisibility(View.GONE);
                progressCon.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_LOAD_MORE:
                if(mFootViewScannAll==null){
                    mFootViewScannAll = LayoutInflater.from(mContext).inflate(R.layout.item_scann_product_btn, null);
                    addView(mFootViewScannAll);
                }

                if (mFootViewScannAll!=null){
                    mFootViewScannAll.setVisibility(View.VISIBLE);
                }
                if (mFootView4NoMore!=null){
                    mFootView4NoMore.setVisibility(View.GONE);
                }
                mText.setVisibility(View.GONE);
                progressCon.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);

                break;
            case STATE_BAD_NET:// 当前网络异常

                showBadNetLayout();

                mText.setVisibility(View.GONE);
                progressCon.setVisibility(View.GONE);
                if(null!=footView){// 再无更多数据的demo
                    footView.stopAnimaltion();
                    mFootView4NoMore.setVisibility(View.GONE);
                }
                break;
            case STATE_LOAD_MORE_SHOPCART:
                if (mFootView4ShopCart == null) {
                    mFootView4ShopCart = LayoutInflater.from(mContext).inflate(R.layout.item_scann_product_btn_shopcart,null);
                    addView(mFootView4ShopCart);
                }
                if (mFootView4ShopCart != null) {
                    mFootView4ShopCart.setVisibility(View.VISIBLE);
                }
                if (mFootView4NoMore!=null){
                    mFootView4NoMore.setVisibility(View.GONE);
                }
                mText.setVisibility(View.GONE);
                progressCon.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_LOAT_ALL_DATA_COMPLETE:
                if (mFootView4LoadDataComplete == null) {
                    mFootView4LoadDataComplete = LayoutInflater.from(mContext).inflate(R.layout.item_footer_load_data_complete,null);
                    addView(mFootView4LoadDataComplete);
                }else {
                    mFootView4LoadDataComplete.setVisibility(View.VISIBLE);
                }
                if (mFootView4NoMore!=null){
                    mFootView4NoMore.setVisibility(View.GONE);
                }
                mText.setVisibility(View.GONE);
                progressCon.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_LOAT_ALL_DATA_NOCOMPLETE:
                if (mFootView4LoadDataComplete == null) {
                    mFootView4LoadDataComplete = LayoutInflater.from(mContext).inflate(R.layout.item_footer_load_data_nocomplete,null);
                    addView(mFootView4LoadDataComplete);
                }else {
                    mFootView4LoadDataComplete.setVisibility(View.VISIBLE);
                }
                if (mFootView4NoMore!=null){
                    mFootView4NoMore.setVisibility(View.GONE);
                }
                mText.setVisibility(View.GONE);
                progressCon.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }

    }

    /**
     * 显示指定的view
     * @param view 需要显示的view
     */
    public void showAppointView(View view) {
        if (mFootView4NoMore!=null){
            mFootView4NoMore.setVisibility(View.GONE);
        }
        mText.setVisibility(View.GONE);
        progressCon.setVisibility(View.GONE);
        addView(view);
        this.setVisibility(View.VISIBLE);
    }


    /**   显示网络异常--的布局 */
    public void showBadNetLayout(){
        if (mFootView4BadNet==null){
            mFootView4BadNet = LayoutInflater.from(mContext).inflate(R.layout.item_bad_netword, null);
            addView(mFootView4BadNet);
        }else{
            mFootView4BadNet.setVisibility(View.VISIBLE);
        }
        // 点击重试
        mFootView4BadNet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLoadingListener!=null){
                    //mLoadingListener.onRefresh();
                    setState(LoadingMoreFooter.STATE_LAODING);
                    mLoadingListener.onLoadMore();//这里应该是加载更多
                }
            }
        });



    }
    /**   显示网络异常--的布局 */
    public void hideLayout(){
        if (mFootView4BadNet!=null){
            mFootView4BadNet.setVisibility(View.GONE);
        }
        if (mFootView4LoadDataComplete != null) {
            mFootView4LoadDataComplete.setVisibility(View.GONE);
        }
    }

    public boolean onMove(float delta) {
        if (curState == STATE_NORMAL) {

            if (getVisiableHeight() == mOrigHeight && delta <=0 ) {
                return false;
            }
            int height = (int) (getVisiableHeight() + delta);
            if (height < mOrigHeight) {
                height = mOrigHeight;
                mText.setText("上拉加载更多");
            }else if (height > mOrigHeight + mOrigHeight/2) {
                mText.setText("释放立即加载更多");
            }else {
                mText.setText("上拉加载更多");
            }
            setVisiableHeight(height);
            return true;
        }
        return false;
    }

    public int getVisiableHeight() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        return layoutParams.height >0? layoutParams.height:mOrigHeight;
    }

    public void setVisiableHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
    }
    public boolean reset(){
        boolean isLoadMore = false;
        if (getVisiableHeight() > mOrigHeight + mOrigHeight /2) {
            mText.setText("上拉加载更多");
            setState(STATE_LAODING);
            isLoadMore = true;
        }
        smoothScrollTo(mOrigHeight);
        return isLoadMore;
    }
    private void smoothScrollTo(int destHeight) {
        int tempHeight = getVisiableHeight();
        if (tempHeight == destHeight) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofInt(tempHeight, destHeight);
        animator.setDuration(300);
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
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                setLayoutParams(layoutParams);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                setLayoutParams(layoutParams);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public int getOrigHeight () {
        return mOrigHeight;
    }

}
