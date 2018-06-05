package com.face.jfshare.androidpoints.injector.modules;

import com.face.jfshare.androidpoints.injector.PerActivity;
import com.face.jfshare.androidpoints.module.base.IBasePresenter;
import com.face.jfshare.androidpoints.module.login.ILoginPresenter;
import com.face.jfshare.androidpoints.module.login.ILoginView;
import com.face.jfshare.androidpoints.module.login.LoginPresenter;
import com.face.jfshare.androidpoints.module.login.MainActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhaoziliang on 2018/6/4.
 */
@Module
public class LoginModule {
    private final ILoginView mView;

    public LoginModule(ILoginView view) {
        this.mView = view;
    }

    @PerActivity
    @Provides
    public LoginPresenter providePresenter() {
        return new LoginPresenter(mView);
    }
}
