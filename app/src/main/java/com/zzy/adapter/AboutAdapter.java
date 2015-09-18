package com.zzy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.youth.zzy.cloudmemo.R;
import com.zzy.bean.AboutBean;

public class AboutAdapter extends ArrayAdapter<AboutBean> {
	private static final int resourceID = R.layout.item_about;
	private LayoutInflater layoutIn;

	public AboutAdapter(Context context, List<AboutBean> objects) {
		super(context, resourceID, objects);
		layoutIn = LayoutInflater.from(context);
	}

	private class ViewHolder {
		TextView txtTitle;
		ImageView icon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = layoutIn.inflate(resourceID, parent, false);
			vh.txtTitle = (TextView) convertView.findViewById(R.id.about_title);
			vh.icon = (ImageView) convertView.findViewById(R.id.about_imgIcon);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		AboutBean ab = getItem(position);
		vh.icon.setImageResource(ab.icon);
		vh.txtTitle.setText(ab.title);
		return convertView;
	}
}
