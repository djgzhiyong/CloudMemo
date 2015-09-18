package com.zzy.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class AnimListenr implements AnimationListener {
	private View view;

	public AnimListenr(View view) {
		this.view = view;
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (view.getVisibility() == View.VISIBLE) {
			view.setVisibility(View.GONE);
		} else {
			view.setVisibility(View.VISIBLE);
		}
	}
}
