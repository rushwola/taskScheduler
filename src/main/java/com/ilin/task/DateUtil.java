package com.ilin.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;


/**
 * @author Administrator
 *时间工具类
 */
public class DateUtil {
	private static final Logger logger = Logger.getLogger(DateUtil.class);

	public static String formatTime(Date now) {
		SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return fullDateFormat.format(now);
	}
	public static String getStr(ArrayList cols, ArrayList values) {
		String rtn = "";
		if (cols.size() == values.size()) {
			rtn += cols.get(0).toString() + "=" + values.get(0).toString();
			for (int i = 1; i < cols.size(); i++)
			{
				rtn += "#" + cols.get(i).toString() + "=" + values.get(i).toString();
			}
		}
		return rtn;
	}

	public static String getHour(String startTime) {
		SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = new Date();
		try {
			start = fullDateFormat.parse(startTime);
		}
		catch (ParseException e) {
			logger.error(e,e);
		}
		return new SimpleDateFormat("HH").format(start);
	}
	public static String getWeek(String startTime) {
		SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = new Date();
		try {
			start = fullDateFormat.parse(startTime);
		}
		catch (ParseException e) {
			logger.error(e,e);
		}
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(start);
		return Integer.toString(rightNow.get(Calendar.DAY_OF_WEEK) - 1);
	}
	public static String getDay(String startTime) {
		SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = new Date();
		try {
			start = fullDateFormat.parse(startTime);
		} catch (ParseException e) {
			logger.error(e, e);
		}
		SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
		return dayFormat.format(start);
	}
}
