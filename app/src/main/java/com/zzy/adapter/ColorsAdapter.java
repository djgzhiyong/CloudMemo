package com.zzy.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import com.youth.zzy.cloudmemo.R;
import com.zzy.bean.ColorSelect;

public class ColorsAdapter extends ArrayAdapter<ColorSelect> {
	private static final int resourceID = R.layout.item_color;
	private LayoutInflater layoutIn;
	private Context context;

	public ColorsAdapter(Context context, List<ColorSelect> objects) {
		super(context, resourceID, objects);
		layoutIn = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RadioButton rb;
		if (convertView == null) {
			convertView = layoutIn.inflate(resourceID, parent, false);
			rb = (RadioButton) convertView.findViewById(R.id.color_rb);
			convertView.setTag(rb);
		} else {
			rb = (RadioButton) convertView.getTag();
		}
		ColorSelect cs = getItem(position);
		rb.setBackgroundDrawable(getColor(cs.colorId));
		rb.setChecked(cs.isSelect);
		return convertView;
	}

	private Drawable getColor(int colorId) {
		switch (colorId) {
		case 0:
			return context.getResources().getDrawable(R.drawable.color1_select);
		case 1:
			return context.getResources().getDrawable(R.drawable.color2_select);
		case 2:
			return context.getResources().getDrawable(R.drawable.color3_select);
		case 3:
			return context.getResources().getDrawable(R.drawable.color4_select);
		case 4:
			return context.getResources().getDrawable(R.drawable.color5_select);
		case 5:
			return context.getResources().getDrawable(R.drawable.color6_select);
		case 6:
			return context.getResources().getDrawable(R.drawable.color7_select);
		case 7:
			return context.getResources().getDrawable(R.drawable.color8_select);
		}
		return context.getResources().getDrawable(R.drawable.color8_select);
	}
}
