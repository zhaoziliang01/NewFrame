package com.face.jfshare.androidpoints.injector.components;

import android.content.Context;


import com.face.jfshare.androidpoints.injector.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by long on 2016/8/19.
 * Application Component
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

//    void inject(BaseActivity baseActivity);

    // provide
    Context getContext();
}
