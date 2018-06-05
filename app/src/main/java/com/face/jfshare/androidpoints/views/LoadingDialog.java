package com.face.jfshare.androidpoints.views;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import com.face.jfshare.androidpoints.R;

import java.util.concurrent.atomic.AtomicInteger;


public class LoadingDialog extends Dialog {

	private final Runnable mAction;
	private TextView mTextView;
	private Context mContext;
	private String content = "加载中";
	//线程安全的整型
	private AtomicInteger counter = new AtomicInteger(1);


	public LoadingDialog(Context context) {
		super(context, R.style.WinDialog);
		setContentView(R.layout.dialog_loading);
		this.mContext = context;
		mTextView = (TextView) findViewById(android.R.id.message);
		mAction = new Runnable() {
			@Override
			public void run() {
				switch (counter.get()) {
					case 1:
						mTextView.setText(content + " .  ");
						break;
					case 2:
						mTextView.setText(content + " .. ");
						break;
					case 3:
						mTextView.setText(content + " ...");
						break;
				}
				if (counter.get() == 3) {
					counter.set(1);
				}else {
					counter.set(counter.get() + 1);
				}
				//Logger.t("loading").e("就看看你执行没有");
				mTextView.postDelayed(this, 400);
			}
		};

	}
	@Override
	public void show() {
		super.show();
		/*if (mAction != null)
			mTextView.post(mAction);*/
	}
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		/*if (mAction != null)
			mTextView.removeCallbacks(mAction);*/
		super.dismiss();
	}
	public void setText(String s)
	{
		if(mTextView != null)
		{
			mTextView.setText(s);
			mTextView.setVisibility(View.VISIBLE);
		}
	}
	public void setText(int res)
	{
		if(mTextView != null)
		{
			mTextView.setText(res);
			mTextView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			return false;
		}
		return super.onTouchEvent(event);
	}

	public void setCancelEnable(boolean enable){
		setCanceledOnTouchOutside(enable);
		setCancelable(enable);
	}

}
