package cn.ancore.dazzlealbum.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.TextUtils;
import android.util.Log;

/**
 * 日期工具�?
 * @author ruanzijie
 * @version 1.0 2012-12-14
 */
public class DateUtil {

	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	private static final String TAG = DateUtil.class.getSimpleName();

	public static String getDateTimePattern() {
		return DEFAULT_DATE_PATTERN + " HH:mm:ss.S";
	}

	public static String getToday() {
		Date today = new Date();
		return format(today);
	}

	public static String format(Date date) {
		return date == null ? "" : format(date, DEFAULT_DATE_PATTERN);
	}

	public static String format(Date date, String pattern) {
		return date == null ? "" : new SimpleDateFormat(pattern).format(date);
	}

	public static Date parse(String strDate) {
		return TextUtils.isEmpty(strDate) ? null : parse(strDate,
				DEFAULT_DATE_PATTERN);
	}

	public static Date parse(String strDate, String pattern) {
		try {
			return TextUtils.isEmpty(strDate) ? null : new SimpleDateFormat(
					pattern).parse(strDate);
		} catch (ParseException e) {
			Log.e(TAG, "Date parse Exception ", e);
		}
		return null;
	}

	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}

	public static Date addDay(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, n);
		return cal.getTime();
	}

	public static Date addHour(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, n);
		return cal.getTime();
	}

	public static Date addMinute(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, n);
		return cal.getTime();
	}

	public static long betDate(Date date, Date otherDate) {
		return date.getTime() - otherDate.getTime();
	}

	public static Date makeDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date makeDate(int year, int month, int day, int hour,
			int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static int getWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int week = calendar.get(Calendar.DAY_OF_WEEK);

		if (week == 1) {
			return 7;
		}
		return week - 1;
	}

	public static int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static Date getLastDateOfMonth(Date date) {
		int year = getYear(date);
		int month = getMonth(date);
		return addDay(addMonth(makeDate(year, month, 1), 1), -1);
	}

	public static Date getFirstDateOfMonth(Date date) {
		int year = getYear(date);
		int month = getMonth(date);
		return makeDate(year, month, 1);
	}

	public static Date getDayOfWeek(Date date, int resultWeek) {
		if ((resultWeek < 1) || (resultWeek > 7))
			throw new IllegalArgumentException("resultWeek must in 1-7");
		int week = getWeek(date);
		return addDay(date, resultWeek - week);
	}

}
