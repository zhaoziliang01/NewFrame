package com.face.jfshare.androidpoints.module.login;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.face.jfshare.androidpoints.R;
import com.face.jfshare.androidpoints.api.RetrofitManager;
import com.face.jfshare.androidpoints.api.SimpleObserver;
import com.face.jfshare.androidpoints.api.params.Params4CommonLogin;
import com.face.jfshare.androidpoints.api.response.Res4Avatar;
import com.face.jfshare.androidpoints.api.response.Res4UserInfo;
import com.face.jfshare.androidpoints.injector.components.DaggerLoginComponent;
import com.face.jfshare.androidpoints.injector.modules.LoginModule;
import com.face.jfshare.androidpoints.module.base.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends BaseActivity<LoginPresenter> implements ILoginView {
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.btn_imge)
    Button btnImge;

    @Override
    protected void initInjector() {
        DaggerLoginComponent.builder().loginModule(new LoginModule(this)).build().inject(this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn, R.id.btn_imge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn:
                Params4CommonLogin params = new Params4CommonLogin();
                params.mobile = "15731144738";
                params.pwdEnc = "333333";
                params.type = 1;
                params.clientType = 1;
                params.userId = 0;
                params.ppInfo = null;
                params.version = "23.0.0001.0719";
                params.browser = "imeisimSerialNumberandroid23";

//                Gson mGson=new GsonBuilder().serializeNulls().create();
//
//                String paramsStr=mGson.toJson(params);
//                Logger.t("http 请求的参数").json(paramsStr);

                mPresenter.doLogin(params);

                break;
            case R.id.btn_imge:
                //1、根据地址拿到File
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/tupian/xhdpi/shouye1.png");

//        2、创建RequestBody，其中`multipart/form-data`为编码类型
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

//        3、创建`MultipartBody.Part`，其中需要注意第一个参数`Filedata`需要与服务器对应,也就是`键`!!!!   也就是需要与服务器端商量
                MultipartBody.Part part = MultipartBody.Part.createFormData("Filedata", file.getName(), requestFile);
                RetrofitManager.getInstance().getApiService()
                        .updateImage("http://testproxy.jfshare.com/system/upload", part)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SimpleObserver<Res4Avatar>() {
                            @Override
                            public void success(Res4Avatar res) {
                                if (res.result) {
                                    Toast.makeText(MainActivity.this, "图片上传成功", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "图片上传失败", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void error(Throwable e) {
                                Toast.makeText(MainActivity.this, "网络超时,请重试" + e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
                break;
        }
    }

    @Override
    public void onSuccess(Res4UserInfo res) {
        if (res.code == 200) {
            showToast("code 200");

        } else {
            showToast("" + res.code + res.desc);

        }
    }

    @Override
    public void onError() {
        showToast("网络超时,请重试");
    }

}
