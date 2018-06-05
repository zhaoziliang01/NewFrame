package com.face.jfshare.androidpoints.api.params;

/**
 * Created by zhaoziliang on 2016/5/15.
 */
public class Params4CommonLogin extends BaseParams {
    /**手机号*/
    public String mobile;
    /**密码*/
    public String pwdEnc;
    /**type为1时可以快捷登录不需要图形验证码*/
    public int type;


    @Override
    public String toString() {
        return "Params4CommonLogin{" +
                ", mobile='" + mobile + '\'' +
                ", pwdEnc='" + pwdEnc + '\'' +
                ", type=" + type +
                "userId=" + userId +
                ", token='" + token + '\'' +
                ", browser='" + browser + '\'' +
                ", ppInfo='" + ppInfo + '\'' +
                ", clientType='" + clientType + '\'' +
                ", version='" + version + '\'' +
                "} " + super.toString();
    }
}
