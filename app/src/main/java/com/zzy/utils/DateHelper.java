package com.zzy.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

	public static String getTimeStr(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String timeStr = sdf.format(new Date(time));
		String[] temp = timeStr.split(" ");
		String[] dateStrs = temp[0].split("-");
		// String[] timeStrs = temp[1].split(":");

		String nowTimeStr = sdf.format(new Date());
		String[] nowTemp = nowTimeStr.split(" ");
		String[] nowDateStrs = nowTemp[0].split("-");
		// String[] nowTimeStrs = nowTemp[1].split(":");

		int year = Integer.parseInt(dateStrs[0]);
		int nowYear = Integer.parseInt(nowDateStrs[0]);
		if (year == nowYear) {
			int month = Integer.parseInt(dateStrs[1]);
			int nowMonth = Integer.parseInt(nowDateStrs[1]);
			if (month == nowMonth) {
				int day = Integer.parseInt(dateStrs[2]);
				int nowDay = Integer.parseInt(nowDateStrs[2]);
				if (day == nowDay) {
					return "今天 " + temp[1];
				} else {
					if (nowDay - day == 1) {
						return "昨天 " + temp[1];
					}
					return Integer.parseInt(dateStrs[2]) + "日 " + temp[1];
				}
			} else {
				return timeStr.substring(timeStr.indexOf("-") + 1,
						timeStr.length());
			}
		} else {
			return timeStr;
		}
	}

}
