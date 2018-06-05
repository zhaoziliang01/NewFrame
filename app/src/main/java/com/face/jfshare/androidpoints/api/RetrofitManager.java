package com.face.jfshare.androidpoints.api;

import com.face.jfshare.androidpoints.utils.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit管理类
 * Created by zhaoziliang
 */
public class RetrofitManager {
    private static RetrofitManager mRetrofitManager;
    public static final String BASE_URL = "http://testproxy.jfshare.com/buyer/";
    private Retrofit mRetrofit;

    private RetrofitManager() {
        initRetrofit();
    }

    public static  RetrofitManager getInstance() {
        if (mRetrofitManager == null) {

            synchronized(RetrofitManager.class){
                if (mRetrofitManager == null) {
                    mRetrofitManager = new RetrofitManager();
                }
            }

        }

        return mRetrofitManager;
    }


    private void initRetrofit() {

//        HttpLoggingInterceptor LoginInterceptor = new HttpLoggingInterceptor();
        LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(loggingInterceptor); //添加retrofit日志打印
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
//        builder.addInterceptor(new HeaderInterceptor());

        OkHttpClient client = builder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)// 设置网络请求的Url地址
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 支持RxJava平台
                .addConverterFactory(GsonConverterFactory.create())// 设置数据解析器
                .client(client)
                .build();
    }

    public ApiService getApiService() {
        return mRetrofit.create(ApiService.class);
    }

    public <T> T createReq(Class<T> reqServer) {
        return mRetrofit.create(reqServer);
    }

}
