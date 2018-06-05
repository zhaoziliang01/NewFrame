package com.face.jfshare.androidpoints.views;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.face.jfshare.androidpoints.R;


/**
 * 自定义Toast
 * Created by liran on 2016/5/17.
 */
public class CustomToast extends Toast {
    private Context context;

    public CustomToast(Context context) {
        super(context);
        this.context = context;
    }

    public void makeText(String text) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_toast_custom, null);
        TextView toastText = (TextView) layout.findViewById(R.id.tv_show_toast);
        toastText.setText(text);
        this.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        this.setDuration(Toast.LENGTH_SHORT);
        this.setView(layout);
        this.show();

    }
    public void setText(int text){
        View layout = LayoutInflater.from(context).inflate(R.layout.item_toast_custom, null);
        TextView toastText = (TextView) layout.findViewById(R.id.tv_show_toast);
        toastText.setText(text);
        this.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        this.setDuration(Toast.LENGTH_SHORT);
        this.setView(layout);
        this.show();
    }
//    public void makeWhithToast(String text) {
//        View layout = LayoutInflater.from(context).inflate(R.layout.white_toast_custom,null);
//        TextView textView = (TextView) layout.findViewById(R.id.tv_show_toast);
//        textView.setText(text);
//        this.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//        this.setDuration(Toast.LENGTH_SHORT);
//        this.setView(layout);
//        this.show();
//    }
}
