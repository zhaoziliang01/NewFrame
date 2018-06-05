package com.face.jfshare.androidpoints;

import android.app.Application;

import com.face.jfshare.androidpoints.injector.components.ApplicationComponent;
import com.face.jfshare.androidpoints.injector.components.DaggerApplicationComponent;
import com.face.jfshare.androidpoints.injector.modules.ApplicationModule;
import com.face.jfshare.androidpoints.receiver.NetStateReceiver;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created by zhaoziliang on 2018/6/4.
 */

public class MyApplication extends Application {
public static ApplicationComponent applicationComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        // 注册有无网络的广播
        NetStateReceiver.registerNetworkStateReceiver(this);
        applicationComponent= DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  //（可选）是否显示线程信息。 默认值为true
                .methodCount(2)         // （可选）要显示的方法行数。 默认2
                .methodOffset(7)        // （可选）隐藏内部方法调用到偏移量。 默认5
//                .logStrategy(customLog) //（可选）更改要打印的日志策略。 默认LogCat
                .tag("zzl")   //（可选）每个日志的全局标记。 默认PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        }

}
