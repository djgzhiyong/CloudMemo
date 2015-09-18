package com.zzy.utils;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.ViewConfiguration;

public class Helper {

	public static void dialogIsDismiss(DialogInterface dialog, boolean dismiss) {
		try {
			Field field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, dismiss);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendEmail(Context context) {
		Intent data = new Intent(Intent.ACTION_SENDTO);
		data.setData(Uri.parse("mailto:djgzhiyong@gmail.com"));
		data.putExtra(Intent.EXTRA_SUBJECT, "yonth云备忘录");
		data.putExtra(Intent.EXTRA_TEXT, "在此输入内容");
		context.startActivity(data);
	}

	public static void noMenuKey(Context context) {
		ViewConfiguration config = ViewConfiguration.get(context);
		try {
			Field field = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (field != null) {
				field.setAccessible(true);
				field.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean checkEmail(String email) {
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		return matcher.matches();
	}

	public static void rate(Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://details?id=com.youth.zzy.cloudmemo"));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}
