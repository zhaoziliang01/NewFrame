package com.jcodecraeer.xrecyclerview;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import java.util.ArrayList;

public class XRecyclerView extends RecyclerView {

    public static final int TYPE_REFRESH_HEADER = -5;
    public static final int TYPE_HEADER = -4;
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_FOOTER = -3;
    private static final float DRAG_RATE = 2;
    private Context mContext;
    private boolean isLoadingData = false;
    private boolean isnomore = false;
    private boolean isDefaultLoadMore = true;
    private int mRefreshProgressStyle = ProgressStyle.SysProgress;
    private int mLoadingMoreProgressStyle = ProgressStyle.SysProgress;
    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFootViews = new ArrayList<>();
    private Adapter mAdapter;
    private Adapter mWrapAdapter;
    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            mWrapAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }


    };
    private float mLastY = -1;
    private float mStartY = -1;
    private LoadingListener mLoadingListener;
    private ArrowRefreshHeader mRefreshHeader;
    //private CustomRefreshHeader mRefreshHeader;
    private boolean pullRefreshEnabled = true;
    private boolean loadingMoreEnabled = true;
    private int previousTotal = 0;
    private int mPageCount = 0;
    private boolean isLoadingData1;
    private float mDowX = 0;
    private float mDownY = 0;


    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        if (pullRefreshEnabled) {
            ArrowRefreshHeader refreshHeader = new ArrowRefreshHeader(mContext);
            //CustomRefreshHeader refreshHeader = new CustomRefreshHeader(mContext);
            mHeaderViews.add(0, refreshHeader);
            mRefreshHeader = refreshHeader;
            mRefreshHeader.setProgressStyle(mRefreshProgressStyle);
        }
        LoadingMoreFooter footView = new LoadingMoreFooter(mContext);
        footView.setProgressStyle(mLoadingMoreProgressStyle);
        addFootView(footView);
        mFootViews.get(0).setVisibility(GONE);

    }

    public void addHeaderView(View view) {
        if (pullRefreshEnabled && !(mHeaderViews.get(0) instanceof ArrowRefreshHeader)) {
            ArrowRefreshHeader refreshHeader = new ArrowRefreshHeader(mContext);
            //CustomRefreshHeader refreshHeader = new CustomRefreshHeader(mContext);
            mHeaderViews.add(0, refreshHeader);
            mRefreshHeader = refreshHeader;
            mRefreshHeader.setProgressStyle(mRefreshProgressStyle);
        }
        mHeaderViews.add(view);
    }

    public void addFootView(final View view) {
        mFootViews.clear();
        mFootViews.add(view);
        if (getAdapter() != null)
            getAdapter().notifyDataSetChanged();
    }
    public LoadingMoreFooter getFootView(){
        if (mFootViews != null) {
            if (mFootViews.size() > 0) {
                View footView = mFootViews.get(0);
                if (footView instanceof LoadingMoreFooter) {
                    return (LoadingMoreFooter)footView;
                }
            }
        }
        return  null;
    }


    public void loadHomeConfigProduct() {
        if (mFootViews != null) {
            if (mFootViews.size() > 0) {
                View footView = mFootViews.get(0);
                if (footView instanceof LoadingMoreFooter) {
                    ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_LOAD_MORE);
                } else {
                    footView.setVisibility(View.GONE);
                }
            }
        }
    }

    public void loadShopCartConfigProduct() {
        if (mFootViews != null) {
            if (mFootViews.size() >0) {
                View footView = mFootViews.get(0);
                if (footView instanceof  LoadingMoreFooter) {
                    ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_LOAD_MORE_SHOPCART);
                }else {
                    footView.setVisibility(View.GONE);
                }
            }
        }
    }

    public void showLoadAllDataComplete() {
        if (mFootViews != null) {
            if (mFootViews.size() > 0) {
                View footView = mFootViews.get(0);
                if (footView instanceof LoadingMoreFooter) {
                    ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_LOAT_ALL_DATA_COMPLETE);
                }else {
                    footView.setVisibility(View.GONE);
                }
            }
        }
    }
    public void showLoadNOAllDataComplete() {
        if (mFootViews != null) {
            if (mFootViews.size() > 0) {
                View footView = mFootViews.get(0);
                if (footView instanceof LoadingMoreFooter) {
                    ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_LOAT_ALL_DATA_NOCOMPLETE);
                }else {
                    footView.setVisibility(View.GONE);
                }
            }
        }
    }

    public void loadMoreComplete() {
        isLoadingData1 = false;
        if (mFootViews != null) {
            if (mFootViews.size() > 0) {
                View footView = mFootViews.get(0);
                if (footView instanceof LoadingMoreFooter) {
                    if (loadingMoreEnabled) {
                        ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_NORMAL);
                    }
                } else {
                    footView.setVisibility(View.GONE);
                }
            }
        }

    }

    public void noMoreLoading() {
        isLoadingData = false;
        if (mFootViews.size() == 0) {
            addDefaultFooterView();
        }
        View footView = mFootViews.get(0);
        isnomore = true;
        if (footView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_NOMORE);
        } else {
            footView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示当前网络异常
     */
    public void showBadNetWork() {
        isLoadingData = false;
        if (mFootViews.size() == 0) {
            addDefaultFooterView();
        }
        View footView = mFootViews.get(0);
        isnomore = true;
        if (footView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_BAD_NET);
            ((LoadingMoreFooter) footView).setLoadingListener(mLoadingListener);//  添加点击重试的功能
        } else {
            footView.setVisibility(View.GONE);
        }
    }

    public ArrowRefreshHeader getHeadView() {
        return mRefreshHeader;

    }

    public void refreshComplete() {
        mRefreshHeader.refreshComplate();
        isLoadingData = false;
    }


    public void setPullRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
    }

    /**
     * 刷新状态结束
     * 加载更多状态结束
     */
    public void stopAll() {
        refreshComplete();
        loadMoreComplete();
    }

    /**
     * 是否可以加载更多
     *
     * @param enabled
     */
    public void setLoadingMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;
        if (mFootViews.size() == 0) {  //  如果没有脚布局  就添加默认的脚布局
            addDefaultFooterView();
        }
        View footView = mFootViews.get(0);
        if (enabled) {
            footView.setVisibility(View.VISIBLE);
            if (footView instanceof LoadingMoreFooter) {
                ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_NORMAL);
            }
        } else {
            footView.setVisibility(View.VISIBLE);
            if (footView instanceof LoadingMoreFooter) {
                ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_NOMORE);
            }
        }

    }

    /**
     * 将底部footer隐藏
     */
    public void setFootViewInvisible() {
       /* View footView = mFootViews.get(0);
        footView.setVisibility(View.GONE);*/
       //隐藏脚布局，肯定不需要上拉加载更多
        loadingMoreEnabled = false;
        mFootViews.clear();
        if (getAdapter() != null)
            getAdapter().notifyDataSetChanged();
    }

    /**
     * 添加默认的
     */
    public void addDefaultFooterView() {
        LoadingMoreFooter footView = new LoadingMoreFooter(mContext);
        footView.setProgressStyle(mLoadingMoreProgressStyle);
        addFootView(footView);
    }

    public void setRefreshProgressStyle(int style) {
        mRefreshProgressStyle = style;
        if (mRefreshHeader != null) {
            mRefreshHeader.setProgressStyle(style);
        }
    }



    public void setLaodingMoreProgressStyle(int style) {
        mLoadingMoreProgressStyle = style;
        if (mFootViews.size() > 0 && mFootViews.get(0) instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mFootViews.get(0)).setProgressStyle(style);
        }
    }

    public void setArrowImageView(int resid) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setArrowImageView(resid);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        mWrapAdapter = new WrapAdapter(mHeaderViews, mFootViews, adapter);
        super.setAdapter(mWrapAdapter);
        mAdapter.registerAdapterDataObserver(mDataObserver);

    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

       /* Log.e(" onScrollStateChanged", "onScrollStateChanged");
        Log.e(" RecyclerView.", (state == RecyclerView.SCROLL_STATE_IDLE) + "");
        Log.e(" RecyclerView.", "state = "+state + "");
        Log.e(" mLoadingListener ", (mLoadingListener != null) + "");
        Log.e("isLoadingData ", (isLoadingData) + "");
        Log.e("loadingMoreEnabled", (loadingMoreEnabled) + "");*/
        //isLoadingData =false;
        /*LayoutManager layoutManager = getLayoutManager();
        int lastVisibleItemPosition = 0;
        if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            Log.e("LinearLayoutManager","我不等于空啊  这种方法行不");
        }else if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            Log.e("GridLayoutManager","我不等于空啊  这种方法行不");
        }else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int lastPositions[] = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
            lastVisibleItemPosition = findMax(lastPositions);
            Log.d("StaggeredGridLayout","我不等于空啊  这种方法行不");
        }
        if (lastVisibleItemPosition < layoutManager.getItemCount() - 1) {
            Log.d("你的滑动没有效果","做的无用功哦");
            return;
        }*/
//        if (!isBottom()) return;
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingListener != null && !isLoadingData && loadingMoreEnabled && isDefaultLoadMore) {
            if (mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                View footView = mFootViews.get(0);

                if (footView instanceof LoadingMoreFooter) {
                    ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_LAODING);
                } else {
                    footView.setVisibility(View.VISIBLE);
                }
                isLoadingData = true;
                mLoadingListener.onLoadMore();
            }
        }
    }

//    private boolean isBottom() {
//        View view = mFootViews.get(0);
//        ViewParent parent = view.getParent();
//        if (parent == null) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDowX = e.getX();
                mDownY = e.getY();

                break;

            case MotionEvent.ACTION_MOVE:
                float moveX = e.getX();
                float moveY = e.getY();

                float dx = Math.abs(moveX - mDowX);
                float dy = Math.abs(moveY - mDownY);
                ViewConfiguration configuration = ViewConfiguration.get(mContext);
                //   用户需要滑动的最小距离
                int mixTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

                // 代表当前用户水平移动趋势
                if (dx > dy && dx > mixTouchSlop) {
                    return false;
                }

                break;

            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (isOnTop() && pullRefreshEnabled) {
                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
                    if (mRefreshHeader.getVisiableHeight() > 0 && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                        //Log.i("getVisiableHeight", "getVisiableHeight = " + mRefreshHeader.getVisiableHeight());
                        //Log.i("getVisiableHeight", " mRefreshHeader.getState() = " + mRefreshHeader.getState());
                        return false;
                    }
                }
                if (loadingMoreEnabled && !isLoadingData && !isDefaultLoadMore) {
                    if (mStartY == -1) {
                        mStartY = ev.getRawY();
                    }
                    float deffY = mStartY - ev.getRawY();
                    View view = mFootViews.get(0);
                    if (view instanceof LoadingMoreFooter) {
                        if (deffY > ((LoadingMoreFooter) view).getOrigHeight()) {
                            if (((LoadingMoreFooter) view).onMove(-deltaY)) {
                                //scrollToPosition(getAdapter().getItemCount()-1);
                                Log.e("自己处理事件","-deltaY/DRAG_RATE --- >" + (-deltaY));
                                scrollBy(0, (int) (-deltaY));
                                return false;
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mLastY = -1; // reset
                mStartY = -1;
                if (isOnTop() && pullRefreshEnabled) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener.onRefresh();
                            isLoadingData = true;
                            isnomore = false;
                            previousTotal = 0;
                        }
                    }
                }
                if (loadingMoreEnabled && !isLoadingData && !isDefaultLoadMore) {
                    View view = mFootViews.get(0);
                    if (view instanceof LoadingMoreFooter) {
                        if(((LoadingMoreFooter) view).reset()){
                            if (mLoadingListener != null) {
                                isLoadingData = true;
                                mLoadingListener.onLoadMore();
                            }
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    private boolean isOnTop() {
        if (mHeaderViews == null || mHeaderViews.isEmpty()) {
            return false;
        }

        View view = mHeaderViews.get(0);
        if (view.getParent() != null) {
            return true;
        } else {
            return false;
        }
//        LayoutManager layoutManager = getLayoutManager();
//        int firstVisibleItemPosition;
//        if (layoutManager instanceof GridLayoutManager) {
//            firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
//        } else if ( layoutManager instanceof StaggeredGridLayoutManager ) {
//            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
//            ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(into);
//            firstVisibleItemPosition = findMin(into);
//        } else {
//            firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//        }
//        if ( firstVisibleItemPosition <= 1 ) {
//             return true;
//        }
//        return false;
    }
    public void setDefaultLoadMore(boolean isDefaultLoadMore) {
        this.isDefaultLoadMore = isDefaultLoadMore;
    }
    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public interface LoadingListener {

        void onRefresh();

        void onLoadMore();
    }

    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {

        private RecyclerView.Adapter adapter;

        private ArrayList<View> mHeaderViews;

        private ArrayList<View> mFootViews;

        private int headerPosition = 1;

        public WrapAdapter(ArrayList<View> headerViews, ArrayList<View> footViews, RecyclerView.Adapter adapter) {
            this.adapter = adapter;
            this.mHeaderViews = headerViews;
            this.mFootViews = footViews;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {   //返回值代表该条目跨几列
                        return (isHeader(position) || isFooter(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        public boolean isHeader(int position) {
            return position >= 0 && position < mHeaderViews.size();
        }

        public boolean isFooter(int position) {
            return position < getItemCount() && position >= getItemCount() - mFootViews.size();
        }

        public boolean isRefreshHeader(int position) {
            return position == 0;
        }

        public int getHeadersCount() {
            return mHeaderViews.size();
        }

        public int getFootersCount() {
            return mFootViews.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER) {
                return new SimpleViewHolder(mHeaderViews.get(0));
            } else if (viewType == TYPE_HEADER) {
                return new SimpleViewHolder(mHeaderViews.get(headerPosition++));
            } else if (viewType == TYPE_FOOTER) {
                return new SimpleViewHolder(mFootViews.get(0));
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (isHeader(position)) {
                return;
            }
            int adjPosition = position - getHeadersCount();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                    return;
                }
            }
        }

        @Override
        public int getItemCount() {
            if (adapter != null) {
                return getHeadersCount() + getFootersCount() + adapter.getItemCount();
            } else {
                return getHeadersCount() + getFootersCount();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            if (isHeader(position)) {
                return TYPE_HEADER;
            }
            if (isFooter(position)) {
                return TYPE_FOOTER;
            }
            int adjPosition = position - getHeadersCount();
            ;
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemViewType(adjPosition);
                }
            }
            return TYPE_NORMAL;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getHeadersCount()) {
                int adjPosition = position - getHeadersCount();
                int adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }


        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.unregisterAdapterDataObserver(observer);
            }
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.registerAdapterDataObserver(observer);
            }
        }

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}