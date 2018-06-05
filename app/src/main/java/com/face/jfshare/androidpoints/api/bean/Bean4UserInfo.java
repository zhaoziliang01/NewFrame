package com.face.jfshare.androidpoints.api.bean;

import java.io.Serializable;

/**
 * Created by wenheng on 16/4/26.
 * 功能： 获取个人信息
 */
public class Bean4UserInfo extends BaseBean implements Serializable {




    public int userId;//用户id
    public String userName="";//用户昵称
    /**  验证身份使用*/
    public String ppInfo;
    /**  验证身份使用*/
    public String token;

    public String browser;

    /**    clienttype 的分配为android 1，ios 2，H5 2，web4 */
    public int clientType=1;
    public String version;


    @Override
    public String toString() {
        return "Bean4UserInfo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", ppInfo='" + ppInfo + '\'' +
                ", token='" + token + '\'' +
                ", browser='" + browser + '\'' +
                ", clientType=" + clientType +
                ", version='" + version + '\'' +
                "} " + super.toString();
    }
}
