package com.face.jfshare.androidpoints.receiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.util.Locale;

/**
 * 跟网络相关的工具类
 * 
 * @author chiwenheng
 *
 */
public class NetUtils
{
	private NetUtils()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}


	/**
	 * 打开网络设置界面
	 */
	public static void openSetting(Activity activity)
	{
		Intent intent=null;
		//判断手机系统的版本  即API大于10 就是3.0或以上版本
		if(android.os.Build.VERSION.SDK_INT>10){
			intent=new Intent(android.provider.Settings.ACTION_SETTINGS);
//			intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		}else{
//			intent = new Intent();
//			ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
//			intent.setComponent(component);
//			intent.setAction("android.intent.action.VIEW");
			intent=new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);

		}
		activity.startActivity(intent);
	}
	/**枚举，表示网络状态*/
	public static enum NetType {
		WIFI, CMNET, CMWAP, NONE
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 网络是否连接上
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	public static NetType getAPNType(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return NetType.NONE;
		}
		int nType = networkInfo.getType();

		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo=networkInfo.getExtraInfo();
			if (!TextUtils.isEmpty(extraInfo)){
				if (extraInfo.toLowerCase(Locale.getDefault()).equals("cmnet")) {
					return NetType.CMNET;
				}else {
					return NetType.CMWAP;
				}
			}

		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			return NetType.WIFI;
		}
		return NetType.NONE;
	}


}