package com.zzy.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zzy.model.MemoBean;

public class MemoDaoHelper {
	private MemoDao mMemoDao;
	private SQLiteDatabase mSdb;

	public MemoDaoHelper(Context context) {
		mMemoDao = new MemoDao(context);
	}

	public void save(MemoBean memo) {
		mSdb = mMemoDao.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("content", memo.content);
		values.put("addTime", memo.addTime);
		values.put("color", memo.color);
		mSdb.insert(MemoDao.TAB_NAME, null, values);
		mSdb.close();
	}

	public void saveCloudData(MemoBean memo) {
		mSdb = mMemoDao.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("content", memo.content);
		values.put("addTime", memo.addTime);
		values.put("color", memo.color);
		values.put("isUpload", 1);
		values.put("isUpdate", 0);
		mSdb.insert(MemoDao.TAB_NAME, null, values);
		mSdb.close();
	}

	public List<MemoBean> findAll() {
		mSdb = mMemoDao.getReadableDatabase();
		Cursor cursor = mSdb.query(MemoDao.TAB_NAME, null, "isDel=0", null,
				null, null, "addTime desc");
		List<MemoBean> memoBeanList = null;
		if (cursor != null && cursor.getCount() > 0) {
			memoBeanList = new ArrayList<MemoBean>();
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String content = cursor.getString(cursor
						.getColumnIndex("content"));
				long addTime = cursor.getLong(cursor.getColumnIndex("addTime"));
				String color = cursor.getString(cursor.getColumnIndex("color"));
				memoBeanList.add(new MemoBean(id, content, addTime, 0, 0, 0,
						color));
			}
			cursor.close();
		}
		mSdb.close();
		return memoBeanList;
	}

	public void deleteMemo(int id) {
		mSdb = mMemoDao.getReadableDatabase();
		String sql = "update " + MemoDao.TAB_NAME + " set isDel=1 where id="
				+ id;
		mSdb.execSQL(sql);
		Log.i("zzy", "deleteMemo:" + sql);
		mSdb.close();
	}

	public void deleteMemoTrue(int id) {
		mSdb = mMemoDao.getReadableDatabase();
		String sql = "delete from " + MemoDao.TAB_NAME + " where id=" + id;
		mSdb.execSQL(sql);
		mSdb.close();
	}

	public void updateMemo(int id, String content, String color) {
		mSdb = mMemoDao.getReadableDatabase();
		String sql = "update " + MemoDao.TAB_NAME + " set content='" + content
				+ "',color='" + color + "',isUpdate=1 " + " where id=" + id;
		mSdb.execSQL(sql);
		mSdb.close();
	}

	public List<MemoBean> findUploadData() {
		mSdb = mMemoDao.getReadableDatabase();
		Cursor cursor = mSdb.query(MemoDao.TAB_NAME, null,
				"isUpload=0 or isDel=1 or isUpdate=1", null, null, null, null);
		List<MemoBean> memoBeanList = null;
		if (cursor != null && cursor.getCount() > 0) {
			memoBeanList = new ArrayList<MemoBean>();
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String content = cursor.getString(cursor
						.getColumnIndex("content"));
				long addTime = cursor.getLong(cursor.getColumnIndex("addTime"));
				String color = cursor.getString(cursor.getColumnIndex("color"));
				int isUpload = cursor.getInt(cursor.getColumnIndex("isUpload"));
				int isDel = cursor.getInt(cursor.getColumnIndex("isDel"));
				int isUpdate = cursor.getInt(cursor.getColumnIndex("isUpdate"));
				memoBeanList.add(new MemoBean(id, content, addTime, isUpload,
						isDel, isUpdate, color));
			}
			cursor.close();
		}
		mSdb.close();
		return memoBeanList;
	}

	public void updateMemoUpload(int id) {
		mSdb = mMemoDao.getReadableDatabase();
		String sql = "update " + MemoDao.TAB_NAME + " set isUpload=1 where id="
				+ id;
		mSdb.execSQL(sql);
		mSdb.close();
	}

	public void updateMemoUpdateState(int id, int state) {
		mSdb = mMemoDao.getReadableDatabase();
		String sql = "update " + MemoDao.TAB_NAME + " set isUpdate=" + state
				+ " where id=" + id;
		mSdb.execSQL(sql);
		mSdb.close();
	}

	public List<Long> findAddTime() {
		mSdb = mMemoDao.getReadableDatabase();
		Cursor cursor = mSdb.query(MemoDao.TAB_NAME,
				new String[] { "addTime" }, null, null, null, null, null);
		List<Long> data = null;
		if (cursor != null && cursor.getCount() > 0) {
			data = new ArrayList<Long>();
			while (cursor.moveToNext()) {
				long addTime = cursor.getLong(cursor.getColumnIndex("addTime"));
				data.add(addTime);
			}
			cursor.close();
		}
		mSdb.close();
		return data;
	}
}
