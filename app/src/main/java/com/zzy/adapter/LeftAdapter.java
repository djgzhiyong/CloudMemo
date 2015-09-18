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
import com.zzy.bean.LeftItemBean;

public class LeftAdapter extends ArrayAdapter<LeftItemBean> {
	private static final int resourceID = R.layout.item_left;
	private LayoutInflater layoutIn;
	private Context context;

	public LeftAdapter(Context context, List<LeftItemBean> objects) {
		super(context, resourceID, objects);
		layoutIn = LayoutInflater.from(context);
		this.context = context;
	}

	private class ViewHolder {
		ImageView imgIcon;
		TextView txtLabel;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = layoutIn.inflate(resourceID, parent, false);
			vh = new ViewHolder();
			vh.imgIcon = (ImageView) convertView.findViewById(R.id.left_icon);
			vh.txtLabel = (TextView) convertView.findViewById(R.id.left_label);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		LeftItemBean item = getItem(position);
		vh.txtLabel.setText(item.label);
		vh.imgIcon.setImageDrawable(context.getResources().getDrawable(
				item.icon));
		return convertView;
	}
}
