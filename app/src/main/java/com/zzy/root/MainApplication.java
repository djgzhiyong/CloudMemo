package com.zzy.root;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.zzy.dao.MemoDaoHelper;
import com.zzy.model.MemoBean;
import com.zzy.utils.BaseSave;
import com.zzy.utils.Des;

public class MainApplication extends Application {
	private MemoDaoHelper dbHelper;
	public boolean isUpload, isDownload;
	private List<MemoBean> localData;
	private List<MemoBean> cloudData;
	private List<Long> addTimeList;
	private String userName;

	@Override
	public void onCreate() {
		super.onCreate();
		cloudData = new ArrayList<MemoBean>();
		dbHelper = new MemoDaoHelper(this);
	}

	public void syncData() {
		userName = BaseSave.getUserName(this);
		if (userName == null) {
			return;
		}
		if (isUpload || isDownload) {
			return;
		}
		addTimeList = dbHelper.findAddTime();
		localData = dbHelper.findUploadData();
		if (localData != null && localData.size() > 0) {
			Log.i("zzy", "开始上传数据");
			initCloudData(userName, true);
		} else {
			initCloudData(userName, false);
		}
	}

	private void startUpload() {
		isUpload = true;
		new AsyncTask<Void, Void, Void>() {

			protected Void doInBackground(Void... params) {
				for (final MemoBean memo : localData) {
					Log.i("zzy", "本地数据:" + memo.toString());
					memo.userName = userName;
					if (memo.isDel == 1) {
						Log.i("zzy", "删除了一条数据");
						deleteCloudBean(memo);
						break;
					}
					if (memo.isUpload == 1) {
						Log.i("zzy", "更新了一条数据");
						updateCloudBean(memo);
						break;
					}
					saveCloudBean(memo);
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				isUpload = false;
			};
		}.execute();
	}

	private void saveCloudBean(final MemoBean memo) {
		memo.content = Des.enCrypto(memo.content);
		memo.save(this, new SaveListener() {

			public void onSuccess() {
				dbHelper.updateMemoUpload(memo.id);
				Log.i("zzy", "保存成功 " + memo.content);
			}

			public void onFailure(int arg0, String arg1) {
				Log.i("zzy", "保存失败：" + arg1);
			}
		});
	}

	private void deleteCloudBean(final MemoBean memo) {
		String objectId = getObjectId(memo.addTime);
		if (objectId != null) {
			memo.setObjectId(objectId);
			memo.delete(this, new DeleteListener() {
				public void onSuccess() {
					dbHelper.deleteMemoTrue(memo.id);
					Log.i("zzy", "删除成功");
				}

				@Override
				public void onFailure(int arg0, String str) {
					Log.i("zzy", "删除失败：" + str);
				}
			});
		} else {
			dbHelper.deleteMemoTrue(memo.id);
			Log.i("zzy", "仅本地删除成功");
		}
	}

	private void initCloudData(String userName, final boolean isUpload) {
		BmobQuery<MemoBean> query = new BmobQuery<MemoBean>();
		query.addWhereEqualTo("userName", userName);
		query.findObjects(this, new FindListener<MemoBean>() {

			@Override
			public void onSuccess(List<MemoBean> arg0) {
				cloudData = arg0;
				if (isUpload) {
					startUpload();
				}
				if (cloudData != null && cloudData.size() > 0) {
					Log.i("zzy", "云端数据数量：" + cloudData.size());
					isDownload = true;
					saveDataToLocal();
				}
			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});
	}

	private void updateCloudBean(final MemoBean memo) {
		String objectId = getObjectId(memo.addTime);
		if (objectId != null) {
			memo.content = Des.enCrypto(memo.content);
			memo.setObjectId(objectId);
			memo.update(this, new UpdateListener() {
				@Override
				public void onSuccess() {
					dbHelper.updateMemoUpdateState(memo.id, 0);
					Log.i("zzy", "更新成功");
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					Log.i("zzy", "更新失败");
				}
			});
		} else {
			dbHelper.updateMemoUpdateState(memo.id, 0);
			saveCloudBean(memo);
			Log.i("zzy", "仅本地更新成功");
		}
	}

	private String getObjectId(long addTime) {
		for (MemoBean memo : cloudData) {
			if (addTime == memo.addTime) {
				return memo.getObjectId();
			}
		}
		return null;
	}

	private void saveDataToLocal() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				int cloudSize = cloudData.size();
				for (int i = 0; i < cloudSize; i++) {
					MemoBean memo = cloudData.get(i);
					if (!isExistsLocal(memo.addTime)) {
						memo.content = Des.deCrypto(memo.content);
						dbHelper.saveCloudData(memo);
					}
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				isDownload = false;
			};

		}.execute();

	}

	private boolean isExistsLocal(long addTime) {
		if (addTimeList != null && addTimeList.size() > 0) {
			int size = addTimeList.size();
			for (int i = 0; i < size; i++) {
				if (addTime == addTimeList.get(i)) {
					return true;
				}
			}
		}
		return false;
	}

}
