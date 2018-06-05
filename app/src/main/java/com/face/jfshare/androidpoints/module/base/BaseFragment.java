package com.face.jfshare.androidpoints.module.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.face.jfshare.androidpoints.R;
import com.face.jfshare.androidpoints.injector.components.ApplicationComponent;
import com.face.jfshare.androidpoints.receiver.NetChangeObserver;
import com.face.jfshare.androidpoints.receiver.NetStateReceiver;
import com.face.jfshare.androidpoints.receiver.NetUtils;
import com.face.jfshare.androidpoints.views.CustomToast;
import com.face.jfshare.androidpoints.views.LoadingDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.face.jfshare.androidpoints.MyApplication.applicationComponent;

/**
 * Created by long on 2016/5/31.
 * 碎片基类
 */
public abstract class BaseFragment<T extends IBasePresenter> extends Fragment implements IBaseView{
    private static final String TAG = BaseFragment.class.getSimpleName();

    /**
     * network status  监听网络状态
     */
    protected NetChangeObserver mNetChangeObserver = null;
    /**
     * 把 Presenter 提取到基类需要配合基类的 initInjector() 进行注入，如果继承这个基类则必定要提供一个 Presenter 注入方法，
     * 该APP所有 Presenter 都是在 Module 提供注入实现，也可以选择提供另外不带 Presenter 的基类
     */
    @Inject
    protected T mPresenter;
    View view4BadNet;

    /**
     * Dagger 注入
     */
    protected abstract void initInjector();

    /**
     * 初始化视图控件
     */
    protected abstract void initViews();

    /**
     * 更新视图控件
     */
    protected abstract void updateViews(boolean isRefresh);
    /**
     * 绑定布局文件
     * @return  布局文件ID
     */
    protected abstract int attachLayoutRes();

    private LoadingDialog mProgressDialog;
    //缓存Fragment view
    private View mRootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(attachLayoutRes(), null);
            ButterKnife.bind(this, mRootView);
            initInjector();
            initViews();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProgressDialog = new LoadingDialog(getActivity());
    }


    @Override
    public void showLoading() {
        //这里activity有可能为null，需要加这个判断
        if (getActivity() == null ||getActivity().isFinishing()) {
            return;
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setCanceledOnTouchOutside(false);

            if (!getActivity().isFinishing()) {
                mProgressDialog.show();
            }
        }
    }

    @Override
    public void hideLoading() {
        //有可能出现actvity为null的 需要做这个判断
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            if (!getActivity().isFinishing()) {
                mProgressDialog.dismiss();
            }
        }
    }
    public void showToast(String content){
        if (getActivity() != null) {
            new CustomToast(getActivity()).makeText(content);
        }
    }

}
