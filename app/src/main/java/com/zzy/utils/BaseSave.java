package com.zzy.utils;

import com.zzy.root.Constance;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class BaseSave {

	public static void saveUserName(Context context, String userName) {
		SharedPreferences sp = getSharedPreferences(context, BASIC);
		Editor editor = sp.edit();
		editor.putString("userName", userName);
		editor.commit();
	}

	public static String getUserName(Context context) {
		SharedPreferences sp = getSharedPreferences(context, BASIC);
		return sp.getString("userName", null);
	}

	public static void clearUserName(Context context) {
		SharedPreferences sp = getSharedPreferences(context, BASIC);
		Editor editor = sp.edit();
		editor.remove("userName");
		editor.commit();
	}

	private static SharedPreferences getSharedPreferences(Context context,
			String name) {
		return context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	private static final String BASIC = "basic";
}
