package com.zzy.widget;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class ScrollListener implements OnScrollListener {

	private scrollCallback mCallback;

	public ScrollListener(scrollCallback callback) {
		mCallback = callback;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			System.out.println("view.getMeasuredHeight()="
					+ view.getMeasuredHeight() + "   view.getScrollY()="
					+ view.getY()+ "  height=" + view.getHeight());
			if (view.getMeasuredHeight() == (view.getScrollY() + view
					.getHeight())) {
				if (mCallback != null) {
					mCallback.scrollBottom();
				}
				System.out.println("到达底部");
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	public interface scrollCallback {
		public void scrollBottom();
	}
}
