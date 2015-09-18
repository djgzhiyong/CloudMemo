package com.zzy.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoDao extends SQLiteOpenHelper {

	public static final String DAO_NAME = "cloudMemo.db";
	public static final String TAB_NAME = "memo";

	public MemoDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public MemoDao(Context context) {
		super(context, DAO_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase sdb) {
		String sql = "create table "
				+ TAB_NAME
				+ "(id integer primary key autoincrement,content text,addTime long,isUpload integer default 0,isDel integer default 0,color varchar(20) default '#FFFFFF',isUpdate integer default 0)";
		sdb.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
