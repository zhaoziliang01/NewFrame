package com.face.jfshare.androidpoints.module.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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
 * Created by long on 2016/8/19.
 * 基类Activity
 */
public abstract class BaseActivity<T extends IBasePresenter> extends FragmentActivity implements IBaseView {
    private static final String TAG = BaseActivity.class.getSimpleName();

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
    //    @BindView(R.id.ib_actionbar_left_back)
    ImageButton ib_back;
    //    @BindView(R.id.tv_actionbar_title)
    TextView tv_title;
    //    @BindView(R.id.iv_actionbar_more)
    ImageView iv_more;
    View view4BadNet;
    //    @BindView(R.id.frame_content)
    LinearLayout frame_content;

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

    private LoadingDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        ib_back = findViewById(R.id.ib_actionbar_left_back);
        tv_title = findViewById(R.id.tv_actionbar_title);
        iv_more = findViewById(R.id.iv_actionbar_more);
        frame_content = findViewById(R.id.frame_content);
        initInjector();
        initViews();
        updateViews(false);
        // 注册网络监听
        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetUtils.NetType type) {
                super.onNetConnected(type);
                Log.d(TAG, "网络连接上");
                onNetworkConnected();
            }

            @Override
            public void onNetDisConnect() {
                super.onNetDisConnect();
//                mPresenter.onNetworkDisConnected();
                Log.d(TAG, "网络未连接");
                onNetworkDisConnected();
            }
        };

        NetStateReceiver.registerObserver(mNetChangeObserver);
        mProgressDialog = new LoadingDialog(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        boolean isConnect = NetUtils.isNetworkConnected(this);
        if (!isConnect) {// 网络是否连接上
//            mPresenter.onNetworkDisConnected();
            Log.d(TAG, "网络未连接");
            onNetworkDisConnected();

        } else {
//            mPresenter.onNetworkConnected();
            Log.d(TAG, "网络连接上");
            onNetworkConnected();

        }

    }

    /**
     * 通过泛型来简化findViewById
     *
     * @param id
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    protected final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            Log.e(TAG, "Could not cast View to concrete class.", ex);
            throw ex;
        }
    }

    @Override
    public void showLoading() {
        if (this.isFinishing()) {
            return;
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setCanceledOnTouchOutside(false);

            if (!this.isFinishing()) {
                mProgressDialog.show();
            }
        }
    }

    @Override
    public void hideLoading() {
        if (this.isFinishing()) {
            return;
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            if (!this.isFinishing()) {
                mProgressDialog.dismiss();
            }
        }
    }
    public void showToast(String content){

        new CustomToast(this).makeText(content);

    }

    public void onNetworkDisConnected() {
        if (null == view4BadNet) {
            ViewStub viewStub = getView(R.id.bad_network);
            view4BadNet = viewStub.inflate();
        } else {
            view4BadNet.setVisibility(View.VISIBLE);
        }
        view4BadNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开网络设置
                NetUtils.openSetting(BaseActivity.this);
            }
        });
    }

    public void onNetworkConnected() {
        if (view4BadNet != null) {
            view4BadNet.setVisibility(View.GONE);
        }
    }


    /**
     * 这里用户重写了setContentView 方法， 这样用户继承这个baseActivity之后，就可以实现这个标题在上，内容在下的格式
     */
    @Override
    public void setContentView(int paramInt) {
        if (this.frame_content == null)
            throw new RuntimeException("Please execute super.onCreate before anything in your onCreate!!");
        View localView = LayoutInflater.from(this).inflate(paramInt, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.frame_content.addView(localView, layoutParams);
    }

    /**
     * 这里用户重写了setContentView 方法， 这样用户继承这个baseActivity之后，就可以实现这个标题在上，内容在下的格式
     */
    @Override
    public void setContentView(View paramView) {
        if (this.frame_content == null)
            throw new RuntimeException("Please execute super.onCreate before anything in your onCreate!!");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.frame_content.addView(paramView, layoutParams);
    }

    @Override
    public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
        if (this.frame_content == null)
            throw new RuntimeException("Please execute super.onCreate before anything in your onCreate!!");
        this.frame_content.addView(paramView, paramLayoutParams);
    }


    /**
     * 获取 ApplicationComponent
     *
     * @return ApplicationComponent
     */
    protected ApplicationComponent getAppComponent() {
        return applicationComponent;
//        return ((AndroidApplication) getApplication()).getAppComponent();
    }
//
//    /**
//     * 获取 ActivityModule
//     *
//     * @return ActivityModule
//     */
//    protected ActivityModule getActivityModule() {
//        return new ActivityModule(this);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清除网络监听
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
    }

}
