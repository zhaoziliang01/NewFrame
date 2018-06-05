package com.face.jfshare.androidpoints.api.response;

/**
 * Created by wenheng on 16/4/14.
 * 功能： 获取头像返回的内容
 */
public class Res4Avatar extends BaseResponse{

    /**  返回上传的头像是否成功的标志*/
    public boolean result;

    /**  返回的个人信息的头像imageKey */
    public String title;

    @Override
    public String toString() {
        return "Res4Avatar{" +
                "result=" + result +
                ", title='" + title + '\'' +
                '}';
    }
}