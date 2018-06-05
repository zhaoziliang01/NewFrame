package com.face.jfshare.androidpoints.module.login;

import com.face.jfshare.androidpoints.api.params.Params4CommonLogin;
import com.face.jfshare.androidpoints.module.base.IBasePresenter;

/**
 * Created by zhaoziliang on 2018/6/4.
 */

public interface ILoginPresenter extends IBasePresenter{
    void doLogin(Params4CommonLogin params4CommonLogin);
}
