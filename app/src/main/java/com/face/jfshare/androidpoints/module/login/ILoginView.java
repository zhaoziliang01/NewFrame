package com.face.jfshare.androidpoints.module.login;

import com.face.jfshare.androidpoints.api.response.Res4UserInfo;
import com.face.jfshare.androidpoints.module.base.IBaseView;

/**
 * Created by zhaoziliang on 2018/6/4.
 */

public interface ILoginView extends IBaseView {
    void onSuccess(Res4UserInfo res);
    void onError();
}
