package com.face.jfshare.androidpoints.api.response;


import java.io.Serializable;

/**
 * Created by wenheng on 16/4/14.
 * 功能： 基础的response --其他业务response 要继承这个类
 */
public class BaseResponse implements Serializable {

    /**  501 鉴权失败*/
    public int code;
//    public Bean4Page page;
    public String desc;


}
