package com.face.jfshare.androidpoints.module.login;

import android.widget.Toast;

import com.face.jfshare.androidpoints.api.RetrofitManager;
import com.face.jfshare.androidpoints.api.SimpleObserver;
import com.face.jfshare.androidpoints.api.params.Params4CommonLogin;
import com.face.jfshare.androidpoints.api.response.Res4UserInfo;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhaoziliang on 2018/6/4.
 */

public class LoginPresenter implements ILoginPresenter {
    ILoginView mView;

    public LoginPresenter(ILoginView view) {
        this.mView = view;
    }


    @Override
    public void doLogin(Params4CommonLogin params4CommonLogin) {
        mView.showLoading();
        RetrofitManager.getInstance().getApiService().doLogin(params4CommonLogin)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<Res4UserInfo>() {
                    @Override
                    public void success(Res4UserInfo res4UserInfo) {
                        mView.hideLoading();
                        mView.onSuccess(res4UserInfo);
                    }

                    @Override
                    public void error(Throwable e) {
                        mView.hideLoading();
                        mView.onError();
                    }
                });
    }

    @Override
    public void getData() {

    }
}
