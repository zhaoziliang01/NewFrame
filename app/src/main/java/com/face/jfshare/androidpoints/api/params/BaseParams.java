package com.face.jfshare.androidpoints.api.params;

import java.io.Serializable;

/**
 * Created by wenheng on 16/5/10.
 * 功能： dobyJson 参数的超类
 */
public class BaseParams implements Serializable {


    public  int  userId;
    public String token;
    public String browser;
    public String ppInfo;
    /**    clienttype 的分配为android 1，ios 2，H5 2，web4 */
    public int clientType=1;
    public String version;


    @Override
    public String toString() {
        return "BaseParams{" +
                "userId=" + userId +
                ", token='" + token + '\'' +
                ", browser='" + browser + '\'' +
                ", ppInfo='" + ppInfo + '\'' +
                ", clientType='" + clientType + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
