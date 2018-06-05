package com.jcodecraeer.xrecyclerview;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.R;

/**
 * 加载底部footer
 * Created by liran on 2016/5/3.
 */
public class FootView {

    public Context context;
    private  Animation animation;
    private ImageView rotateImage;

    public FootView(Context context) {
        this.context=context;

    }

    public View addFooterView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progress_footer, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_load_mes);// 加载布局
        TextView mShowContent = (TextView) v.findViewById(R.id.tv_show_progress);
        // main.xml中的ImageView
         rotateImage = (ImageView) v.findViewById(R.id.iv_rotate);
        // 加载动画
        animation = AnimationUtils.loadAnimation(
                context, R.anim.dialog_loading);
        rotateImage.setAnimation(animation);
        animation.setInterpolator(new LinearInterpolator());
        // 使用ImageView显示动画

        return v;
    }
    public void startAnimaltion(){

        rotateImage.post(new Runnable() {
            @Override
            public void run() {
                rotateImage.setAnimation(animation);
                animation.startNow();

            }
        });


    }
    public void stopAnimaltion(){
        animation.cancel();

    }
}
