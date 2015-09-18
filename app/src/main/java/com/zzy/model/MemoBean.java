package com.zzy.model;

import cn.bmob.v3.BmobObject;


/** 文本备忘录 */
public class MemoBean extends BmobObject {
	private static final long serialVersionUID = 1L;
	public int id;
	public String content;
	public long addTime;
	public int isUpload;
	public int isDel;
	public int isUpdate;
	public String color;
	public String userName;

	public MemoBean(int id, String content, long addTime, int isUpload,
			int isDel, int isUpdate, String color) {
		this.id = id;
		this.content = content;
		this.addTime = addTime;
		this.isUpload = isUpload;
		this.isUpdate = isUpdate;
		this.isDel = isDel;
		this.color = color;
	}

	public MemoBean(String content, long addTime, String color) {
		this.content = content;
		this.addTime = addTime;
		this.color = color;
	}

	public MemoBean() {
	}

	@Override
	public String toString() {
		return "MemoBean [id=" + id + ", content=" + content + ", addTime="
				+ addTime + ", isUpload=" + isUpload + ", isDel=" + isDel
				+ ", color=" + color + ", userName=" + userName + "]";
	}

	public static final int DELETE = 0x1;

	public static final int RETAIN = 0x0;
}
