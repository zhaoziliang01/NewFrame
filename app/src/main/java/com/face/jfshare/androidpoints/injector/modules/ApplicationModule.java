package com.face.jfshare.androidpoints.injector.modules;

import android.content.Context;


import com.face.jfshare.androidpoints.MyApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/8/19.
 * Application Module
 */
@Module
public class ApplicationModule {

    private Context mContext;

    public ApplicationModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mContext;
    }

}
