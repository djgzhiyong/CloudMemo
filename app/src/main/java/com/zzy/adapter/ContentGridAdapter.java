package com.zzy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.youth.zzy.cloudmemo.R;
import com.zzy.model.MemoBean;
import com.zzy.utils.DateHelper;

import java.util.List;

public class ContentGridAdapter extends ArrayAdapter<MemoBean> {
    private static final int resourceID = R.layout.item_content_grid;
    private LayoutInflater mLayoutInflater;
    private Animation animIn, animOut;
    private boolean isDeleteShow = false;
    private Context context;

    public ContentGridAdapter(Context context, List<MemoBean> objects) {
        super(context, resourceID, objects);
        mLayoutInflater = LayoutInflater.from(context);
        animIn = AnimationUtils.loadAnimation(context, R.anim.check_in);
        animOut = AnimationUtils.loadAnimation(context, R.anim.time_out);
        this.context = context;
    }

    private class ViewHolder {
        TextView txtTitle;
        TextView txtTime;
        CheckBox cbDel;
        CardView cardView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(resourceID, parent, false);
            vh = new ViewHolder();
            vh.txtTitle = (TextView) convertView
                    .findViewById(R.id.main_txtTitle);
            vh.txtTime = (TextView) convertView.findViewById(R.id.main_txtTime);
            vh.cbDel = (CheckBox) convertView.findViewById(R.id.main_cbDel);
            vh.cardView = (CardView) convertView.findViewById(R.id.main_card);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final MemoBean memo = getItem(position);
        String temp = memo.content.trim();
        vh.txtTitle.setText(temp);
        vh.txtTime.setText(DateHelper.getTimeStr(memo.addTime));
        if (isDeleteShow) {
            if (vh.cbDel.getVisibility() == View.GONE) {
                vh.cbDel.setVisibility(View.VISIBLE);
                vh.cbDel.startAnimation(animIn);
            }
            boolean isDelete = memo.isDel == 1 ? true : false;
            vh.cbDel.setChecked(isDelete);
        } else {
            vh.cbDel.startAnimation(animOut);
            vh.cbDel.setVisibility(View.GONE);
        }
        vh.cardView.setCardBackgroundColor(Color.parseColor(memo.color));
        return convertView;
    }

    public boolean getIsDeleteShow() {
        return isDeleteShow;
    }

    public void setDeleteShow(boolean isDeleteShow) {
        this.isDeleteShow = isDeleteShow;
        notifyDataSetChanged();
    }

}
