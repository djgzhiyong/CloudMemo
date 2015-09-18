package com.zzy.cloudmemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.youth.zzy.cloudmemo.R;
import com.zzy.adapter.AboutAdapter;
import com.zzy.bean.AboutBean;
import com.zzy.utils.Helper;
import com.zzy.widget.MyToast;

public class AboutFragment extends Fragment {
	private ListView listview;
	private MyToast toast;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_about, null);
		listview = (ListView) view.findViewById(R.id.about_listview);
		toast = new MyToast(getActivity());
		setListData();
		return view;
	}

	private void setListData() {
		List<AboutBean> items = new ArrayList<AboutBean>();
		items.add(new AboutBean(R.drawable.email, "发邮件给我们"));
		items.add(new AboutBean(R.drawable.thumbsup, "点个赞"));
		// items.add(new AboutBean(R.drawable.rate, "检测更新"));
		listview.setAdapter(new AboutAdapter(getActivity(), items));
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					try {
						Helper.sendEmail(getActivity());
					} catch (Exception e) {
						toast.show("没有找到邮件客户端");
					}
					break;
				case 1:
					Helper.rate(getActivity());
					break;
				case 2:
					// BmobUpdateAgent.forceUpdate(getActivity());
					break;
				}
			}
		});
	}
}
