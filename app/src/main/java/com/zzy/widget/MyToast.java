package com.zzy.widget;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
	private Toast toast;
	private Context context;

	public MyToast(Context context) {
		this.context = context;
	}

	public void show(String text) {
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		}
		toast.setText(text);
		toast.show();
	}
}
