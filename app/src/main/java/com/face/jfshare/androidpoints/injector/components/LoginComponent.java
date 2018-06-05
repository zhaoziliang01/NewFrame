package com.face.jfshare.androidpoints.injector.components;

import com.face.jfshare.androidpoints.injector.PerActivity;
import com.face.jfshare.androidpoints.injector.modules.LoginModule;
import com.face.jfshare.androidpoints.module.login.MainActivity;

import dagger.Component;

/**
 * Created by zhaoziliang on 2018/6/4.
 */

@PerActivity
@Component(modules = LoginModule.class)
public interface LoginComponent {
    void inject(MainActivity mainActivity);
}
