package cn.chaoren.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 类说明：
 * 
 * @创建时间 2011-6-26 下午09:40:09
 * @创建人： 陆林
 * @邮箱：15301586841@189.cn
 */
public class StringUtils {
	public static final String ENCODING = "utf-8";

	public static String encode(String data) {
		try {
			return URLEncoder.encode(data, ENCODING);
		} catch (UnsupportedEncodingException e) {
			return data;
		}
	}
	
	public static String getString(String string) {
		if(string==null||string.equals("")) return "-";
		return string;
	}

	public static String getPreTime(String time) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTimeInMillis(new SimpleDateFormat("yyyy-MM-dd").parse(time)
					.getTime() - 24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getTime(c);
	}

	public static String getDatePre(int day, String time) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTimeInMillis(new SimpleDateFormat("yyyy-MM-dd").parse(time)
					.getTime() - day * 24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getTime(c);
	}

	public static String getNextTime(String time) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTimeInMillis(new SimpleDateFormat("yyyy-MM-dd").parse(time)
					.getTime() + 24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getTime(c);
	}

	private static String getTime(Calendar c) {
		StringBuffer buf = new StringBuffer();
		buf.append(c.get(Calendar.YEAR));
		buf.append("-");
		int month = c.get(Calendar.MONTH) + 1;
		if (month < 10) {
			buf.append("0" + month);
		} else {
			buf.append(month);
		}
		buf.append("-");
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (day < 10) {
			buf.append("0" + day);
		} else {
			buf.append(day);
		}
		return buf.toString();
	}

	public static String getTime() {
		return getTime(Calendar.getInstance());
	}

	private final static Map<Integer, String> WEEK = new HashMap<Integer, String>();
	static {
		WEEK.put(Calendar.MONDAY, "周一");
		WEEK.put(Calendar.TUESDAY, "周二");
		WEEK.put(Calendar.WEDNESDAY, "周三");
		WEEK.put(Calendar.THURSDAY, "周四");
		WEEK.put(Calendar.FRIDAY, "周五");
		WEEK.put(Calendar.SATURDAY, "周六");
		WEEK.put(Calendar.SUNDAY, "周日");
	}

	public final static Map<String, String> WEEK1 = new HashMap<String, String>();
	static {
		WEEK1.put("1", "周一");
		WEEK1.put("2", "周二");
		WEEK1.put("3", "周三");
		WEEK1.put("4", "周四");
		WEEK1.put("5", "周五");
		WEEK1.put("6", "周六");
		WEEK1.put("7", "周日");
	}

	public static String getWeek() {
		return WEEK.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
	}

	public static String getWeekKey(String week) {
		Iterator<String> iter = WEEK1.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (WEEK1.get(key).equals(week)) {
				return key;
			}
		}
		return "";
	}
}
