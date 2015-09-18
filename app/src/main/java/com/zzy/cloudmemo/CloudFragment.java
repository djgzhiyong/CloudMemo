package com.zzy.cloudmemo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youth.zzy.cloudmemo.R;
import com.zzy.utils.BaseSave;

public class CloudFragment extends Fragment {
	private TextView txtNoLogin, txtUserName;
	private String userName;
	private RelativeLayout contentLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userName = BaseSave.getUserName(getActivity());
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cloud, null);
		contentLayout = (RelativeLayout) view.findViewById(R.id.cloud_content);
		txtNoLogin = (TextView) view.findViewById(R.id.cloud_noLogin);
		txtNoLogin.setOnClickListener(noLoginClick);
		txtUserName = (TextView) view.findViewById(R.id.cloud_userName);
		txtUserName.setOnClickListener(loginExit);
		refreshUserState();
		return view;
	}

	OnClickListener noLoginClick = new OnClickListener() {
		public void onClick(View v) {
			startActivityForResult(new Intent(getActivity(),
					LoginActivity.class), RESULT_LOGIN);
		}
	};

	OnClickListener loginExit = new OnClickListener() {
		public void onClick(View v) {
			AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
			ad.setMessage("你想要？");
			ad.setPositiveButton("登出", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					BaseSave.clearUserName(getActivity());
					userName = null;
					refreshUserState();
				}
			});
			ad.setNegativeButton("取消", null);
			ad.create().show();
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_LOGIN) {
			userName = BaseSave.getUserName(getActivity());
			refreshUserState();
		}
	}

	private void refreshUserState() {
		if (userName != null) {
			txtUserName.setText(userName);
			txtNoLogin.setVisibility(View.GONE);
			contentLayout.setVisibility(View.VISIBLE);
		} else {
			txtNoLogin.setVisibility(View.VISIBLE);
			contentLayout.setVisibility(View.GONE);
		}
	};

	public static final int RESULT_LOGIN = 0x1;
}
