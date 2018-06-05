package com.face.jfshare.androidpoints.api.response;

/**
 * Created by wenheng on 16/4/14.
 * 功能： 获取个人信息 登录 ---
 */
public class Res4UserInfo extends BaseResponse {


    public int userId;//用户id
    /**  验证身份使用*/
    public String ppInfo;
    /**  验证身份使用*/
    public String token;

    /**  微信登录返回的手机号*/
    public String loginName;
    /**  1003 用户不存在;3002 accessToken 错误*/
    public String failCode;

    /**  有效时间  ---*/
    public String logoutTime;

    public String remark;

    @Override
    public String toString() {
        return "Res4UserInfo{" +
                "userId=" + userId +
                ", ppInfo='" + ppInfo + '\'' +
                ", token='" + token + '\'' +
                ", loginName='" + loginName + '\'' +
                ", failCode='" + failCode + '\'' +
                ", logoutTime='" + logoutTime + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
