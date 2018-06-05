package com.face.jfshare.androidpoints.utils;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 自定义的日志拦截器
 * 模拟HttpLoggingInterceptor获取数据的方法
 * Created by zhaoziliang on 2018/6/5.
 */
public class LoggingInterceptor implements Interceptor {
    private final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
//这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();
        RequestBody requestBody = request.body();
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        Charset charset = UTF8;
        MediaType contentType = requestBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        //打印请求参数
        Logger.t(request.url().toString()).json(buffer.readString(charset));


        Response response = chain.proceed(request);
        //打印响应数据
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer4response = source.buffer();
        Logger.t(response.request().url().toString()).json(buffer4response.clone().readString(charset));

        return response;
    }

}