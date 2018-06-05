package com.face.jfshare.androidpoints.api;


import com.face.jfshare.androidpoints.api.params.Params4CommonLogin;
import com.face.jfshare.androidpoints.api.response.Res4Avatar;
import com.face.jfshare.androidpoints.api.response.Res4UserInfo;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * Created by zhaoziliang on 2018/4/17.
 */
public interface ApiService {
    //登陆   JSON请求
    @POST("buyer/login2")
    Observable<Res4UserInfo> doLogin(@Body Params4CommonLogin params);

    /**
     * d. @Part & @PartMap
     作用：发送 Post请求 时提交请求的表单字段
     与@Field的区别：功能相同，但携带的参数类型更加丰富，包括数据流，所以适用于 有文件上传 的场景*/

    //单张图片上传
    @Multipart
    @POST
    Observable<Res4Avatar> updateImage(@Url String url, @Part MultipartBody.Part file);

}
