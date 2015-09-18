package com.zzy.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

public class NetWorkState {
	/**
	 * 获取网络状态
	 */
	public static int getNetWorkState(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State moblie = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (moblie != null && moblie == State.CONNECTED) {
			return NETWORK_MOBILE;
		} else if (wifi != null && wifi == State.CONNECTED) {
			return NETWORK_WIFI;
		}
		return NETWORK_DISCONNECT;
	}

	/** 设置网络 */
	public static void setNetWork(final Context context) {
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setMessage("当前无网络连接,请检查网络状态");
		ad.setPositiveButton("检查网络", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
				context.startActivity(intent);
			}
		});
		ad.setNegativeButton("取消", null);
		ad.create().show();
	}

	/**
	 * 获取网络资源文件大小
	 */
	public static int getNetWorkResSize(String location) {
		try {
			URL url = new URL(location);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (conn.getResponseCode() == 200) {
				return conn.getContentLength();
			}
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 从URL获取输入流
	 * 
	 * @param path
	 *            URL地址
	 * @return
	 */
	public static InputStream getInputStreamByUrl(String path) {
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			return conn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 移动网络 */
	public static final int NETWORK_MOBILE = 0x1;
	/** WIFI网络 */
	public static final int NETWORK_WIFI = 0x2;
	/** 断开网络 */
	public static final int NETWORK_DISCONNECT = 0x0;
}
