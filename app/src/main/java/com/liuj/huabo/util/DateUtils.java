package com.liuj.huabo.util;



import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author <a href="mailto:acerge@163.com">gebiao(acerge)</a>
 * @since 2007-9-28 02:05:17
 */
public class DateUtils {
	public static final DateUtils instance = new DateUtils();// for any tools
	public static final long m_second = 1000;
	public static final long m_minute = m_second * 60;
	public static final long m_hour = m_minute * 60;
	public static final long m_day = m_hour * 24;
	public static final String FORMAT_FULLTIME = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_COMPACT_DAY = "yyyyMMdd";
	public static final String FORMAT_COMPACT_MONTH = "yyyyMM";


	/**
	 * <p>
	 * DateUtil instances should NOT be constructed in standard programming.
	 * </p>
	 * <p>
	 * This constructor is public to permit tools that require a JavaBean instance
	 * to operate.
	 * </p>
	 */
	public DateUtils() {
	}

	/**
	 * 获取系统时间戳，毫秒级
	 * 
	 * @return
	 */
	public static final long timeMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * 当前日期字符串，yyyy-MM-dd
	 * 
	 * @return
	 */
	public static final String currentDateStr() {
		return formatDate(currentTime());
	}

	/**
	 * 获取当前日期 <br>
	 * 参见{@link #timeMillis()}
	 * 
	 * @return
	 */
	public static final Date currentTime() {
		return new Date();
	}

	/**
	 * 当前timestamp字符串，yyyy-MM-dd HH:mm:ss <br>
	 * 参见{@link #format(Date, String)}
	 * 
	 * @return
	 */
	public static final String getCurFullTimestampStr() {
		return formatTimestamp(getCurFullTimestamp());
	}

	/**
	 * 当前timestamp <br>
	 * 字符串类型返回，参见{@link #getCurFullTimestampStr()}
	 * 
	 * @return
	 */
	public static final Timestamp getCurFullTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 当前月份的下一个月 <br>
	 * 1月份的下一个月为 2，12月份的下一个月为1
	 * 
	 * @return
	 */
	public static final int nextMonth() {
		int nextMonth = getMonth(new Date()) + 1;
		if (nextMonth == 13)
			return 1;
		return nextMonth;
	}

	/**
	 * parse date using default pattern yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return 失败返回null
	 */
	public static final Date parseDate(String strDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = dateFormat.parse(strDate);
			return date;
		} catch (Exception pe) {
			return null;
		}
	}

	public static int getWeekOfYear(Timestamp time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		int week = cal.get(Calendar.WEEK_OF_YEAR);
		return week;
	}

	/**
	 * 中国传统意义的周，周一做为开始
	 * 
	 * @param time
	 * @return
	 */
	public static int getCnWeekOfYear(Timestamp time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		int week = cal.get(Calendar.WEEK_OF_YEAR);
		return week;
	}

	/**
	 * 根据date字符串，获取timestamp
	 * 
	 * @param strDate
	 *            必须为 yyyy-MM-dd HH:mm:ss[.fffffffff]格式
	 * @return 失败返回null
	 */
	public static final Timestamp parseTimestamp(String strDate) {
		try {
			if (strDate.length() >= 19) {
				// Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]
				Timestamp result = Timestamp.valueOf(strDate);
				return result;
			} else {
				String pattern = FORMAT_FULLTIME.substring(0, strDate.length());
				return parseTimestamp(strDate, pattern);
			}
		} catch (Exception pe) {
			return null;
		}
	}

	/**
	 * @param strDate
	 * @param pattern
	 * @return
	 */
	public static final Timestamp parseTimestamp(String strDate, String pattern) {
		Date date = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			date = dateFormat.parse(strDate);
			return new Timestamp(date.getTime());
		} catch (Exception pe) {
			return null;
		}
	}

	/**
	 * @param strDate
	 * @param pattern
	 * @return
	 */
	public static final Date parseDate(String strDate, String pattern) {

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			Date date = dateFormat.parse(strDate);
			return date;
		} catch (Exception pe) {
			return null;
		}
	}

	/**
	 * @param date
	 * @return formated date by yyyy-MM-dd
	 */
	public static final <T extends Date> String formatDate(T date) {
		if (date == null)
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	/**
	 * @param date
	 * @return formated time by HH:mm:ss
	 */
	public static final <T extends Date> String formatTime(T date) {
		if (date == null)
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		return dateFormat.format(date);
	}

	/**
	 * @param date
	 * @return formated time by yyyy-MM-dd HH:mm:ss
	 */
	public static final <T extends Date> String formatTimestamp(T date) {
		if (date == null)
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	public static final String formatTimestamp(Long mills) {
		return formatTimestamp(new Date(mills));
	}

	/**
	 * @param date
	 * @param pattern:
	 *            Date format pattern
	 * @return
	 */
	public static final <T extends Date> String format(T date, String pattern) {
		if (date == null)
			return null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			String result = dateFormat.format(date);
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param original
	 * @param days
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return original+day+hour+minutes+seconds
	 */
	public static final <T extends Date> T addTime(T original, int days, int hours, int minutes, int seconds) {
		if (original == null)
			return null;
		long newTime = original.getTime() + m_day * days + m_hour * hours + m_minute * minutes + m_second * seconds;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}

	public static <T extends Date> Date addMonth(T original, int month) {
		if (original == null)
			return null;
		Calendar calender = Calendar.getInstance();
		calender.setTime(original);
		calender.add(Calendar.MONTH, month);
		return calender.getTime();
	}

	public static final <T extends Date> T addDay(T original, int days) {
		if (original == null)
			return null;
		long newTime = original.getTime() + m_day * days;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}

	public static final <T extends Date> T addHour(T original, int hours) {
		if (original == null)
			return null;
		long newTime = original.getTime() + m_hour * hours;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}

	public static final <T extends Date> T addMinute(T original, int minutes) {
		if (original == null)
			return null;
		long newTime = original.getTime() + m_minute * minutes;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}

	public static final <T extends Date> T addSecond(T original, int second) {
		if (original == null)
			return null;
		long newTime = original.getTime() + m_second * second;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}

	/**
	 * @param day
	 * @return for example ,1997/01/02 22:03:00,return 1997/01/02 00:00:00.0
	 */
	public static final <T extends Date> T getBeginningTimeOfDay(T day) {
		if (day == null)
			return null;
		T another = (T) day.clone();
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		another.setTime(c.getTimeInMillis());
		return another;
	}

	/**
	 * @param day
	 * @return for example ,1997/01/02 22:03:00,return 1997/01/02 23:59:59.999
	 */
	public static final <T extends Date> T getLastTimeOfDay(T day) {
		if (day == null)
			return null;
		T another = (T) day.clone();
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		another.setTime(c.getTimeInMillis());
		return another;
	}

	public static boolean isTomorrow(Date date) {
		if (date == null) {
			return false;
		}
		long last = getLastTimeOfDay(new Date()).getTime();
		return date.getTime() > last && date.getTime() <= last + DateUtils.m_day;
	}

	public static boolean isToday(Date date) {
		if (date == null) {
			return false;
		}
		long begin = getBeginningTimeOfDay(new Date()).getTime();
		return date.getTime() >= begin && date.getTime() < begin + DateUtils.m_day;
	}
	/***
	 * @param date
	 * @return 1,2,3,4,5,6,7
	 */
	private static int[] chweek = new int[] { 0, 7, 1, 2, 3, 4, 5, 6 };

	/**
	 * @param date
	 * @return 1,2,3,4,5,6,7
	 */
	public static Integer getWeek(Date date) {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return chweek[c.get(Calendar.DAY_OF_WEEK)];
	}

	private static String[] cnweek = new String[] { "", "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
	private static String[] cnSimpleweek = new String[] { "", "日", "一", "二", "三", "四", "五", "六" };

	/**
	 * @param date
	 * @return "周日", "周一", "周二", "周三", "周四", "周五", "周六"
	 */
	public static String getCnWeek(Date date) {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return cnweek[c.get(Calendar.DAY_OF_WEEK)];
	}

	/**
	 * @param date
	 * @return "日", "一", "二", "三", "四", "五", "六"
	 */
	public static String getCnSimpleWeek(Date date) {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return cnSimpleweek[c.get(Calendar.DAY_OF_WEEK)];
	}

	/**
	 * @return 当天是几号的数字
	 */
	public static Integer getCurDayOfMonth() {
		return getDay(new Date());
	}

	public static Integer getCurMonthOfYear() {
		return getMonth(new Date());
	}

	public static Integer getCurYear() {
		return getYear(new Date());
	}

	public static Integer getYear(Date date) {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	public static Integer getDay(Date date) {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @param date
	 * @return 日期所在月份(一月 返回 1，十二月返回12)
	 */
	public static Integer getMonth(Date date) {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// public final static int JANUARY = 0;
		return c.get(Calendar.MONTH) + 1;
	}

	public static String getCurDateStr() {
		return DateUtils.formatDate(new Date());
	}

	public static String getCurTimeStr() {
		return DateUtils.formatTimestamp(new Date());
	}

	public static boolean isAfter(Date date) {
		if (date == null)
			return false;
		return date.after(new Date());
	}

	/**
	 * 获取date所在月份的星期为weektype且日期在date之后（或等于）的所有日期
	 * 
	 * @param weektype
	 * @return
	 */
	public static List<Date> getWeekDateList(Date date, String weektype) {
		int curMonth = getMonth(date);
		int week = Integer.parseInt(weektype);
		int curWeek = getWeek(date);
		int sub = (7 + week - curWeek) % 7;
		Date next = addDay(date, sub);
		List<Date> result = new ArrayList();
		while (getMonth(next) == curMonth) {
			result.add(next);
			next = addDay(next, 7);
		}
		return result;
	}

	/**
	 * 获取date之后(包括date)的num个星期为weektype日期（不限制月份）
	 * 
	 * @param weektype
	 * @return
	 */
	public static List<Date> getWeekDateList(Date date, String weektype, int num) {
		int week = Integer.parseInt(weektype);
		int curWeek = getWeek(date);
		List<Date> result = new ArrayList();
		int sub = (7 + week - curWeek) % 7;
		Date next = addDay(date, sub);
		for (int i = 0; i < num; i++) {
			result.add(next);
			next = addDay(next, 7);
		}
		return result;
	}

	/**
	 * 获取date所在星期的周一至周日的日期
	 * 
	 * @param date
	 * @return
	 */
	public static List<Date> getCurWeekDateList(Date date) {
		int curWeek = getWeek(date);
		List<Date> dateList = new ArrayList();
		for (int i = 1; i <= 7; i++)
			dateList.add(DateUtils.addDay(date, -curWeek + i));
		return dateList;
	}

	public static Date getWeekLastDay(Date date) {
		int curWeek = getWeek(date);
		return DateUtils.addDay(date, 7 - curWeek);
	}

	public static Date getCurDate() {
		return getBeginningTimeOfDay(new Date());
	}

	/**
	 * 获取日期所在月份的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static <T extends Date> T getMonthFirstDay(T date) {
		if (date == null)
			return null;
		String dateStr = format(date, "yyyy-MM") + "-01";
		Long mill = parseDate(dateStr).getTime();
		T another = (T) date.clone();
		another.setTime(mill);
		return another;
	}

	public static <T extends Date> T getNextMonthFirstDay(T day) {
		if (day == null)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month + 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		//TODO:refact set hh,mm,ss
		String datefor = format(calendar.getTime(), "yyyy-MM-dd");
		Long mill = parseDate(datefor).getTime();
		T another = (T) day.clone();
		another.setTime(mill);
		return another;
	}

	/**
	 * 获取日期所在月份的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static <T extends Date> T getMonthLastDay(T date) {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String dateStr = format(date, "yyyy-MM") + "-" + c.getActualMaximum(Calendar.DAY_OF_MONTH);
		Long mill = parseDate(dateStr).getTime();
		T another = (T) date.clone();
		another.setTime(mill);
		return another;
	}

	public static String formatDate(int days) {
		return formatDate(addDay(new Date(), days));
	}

	/**
	 * 截取时分秒后的时间
	 * 
	 * @return
	 */
	public static Timestamp getCurTruncTimestamp() {
		return getBeginningTimeOfDay(new Timestamp(System.currentTimeMillis()));
	}

	public static Integer getHour(Date date) {
		if (date == null)
			return null;
		//TODO:fix
		String hour = format(date, "H");
		return Integer.parseInt(hour);
	}

	public static Integer getMinute(Date date) {
		if (date == null)
			return null;
		//TODO:fix
		String m = format(date, "m");
		return Integer.parseInt(m);
	}

	public static String getTimeDesc(Timestamp time) {
		if (time == null)
			return "";
		String timeContent;
		Long ss = System.currentTimeMillis() - time.getTime();
		Long minute = ss / 60000;
		if (minute < 1) {
			Long second = ss / 1000;
			timeContent = second + "秒前";
		} else if (minute >= 60) {
			Long hour = minute / 60;
			if (hour >= 24) {
				if (hour > 720)
					timeContent = "1月前";
				else if (hour > 168 && hour <= 720)
					timeContent = (hour / 168) + "周前";
				else
					timeContent = (hour / 24) + "天前";
			} else {
				timeContent = hour + "小时前";
			}
		} else {
			timeContent = minute + "分钟前";
		}
		return timeContent;
	}

	public static String getDateDesc(Date time) {
		if (time == null)
			return "";
		String timeContent;
		Long ss = System.currentTimeMillis() - time.getTime();
		Long minute = ss / 60000;
		if (minute < 1) {
			Long second = ss / 1000;
			timeContent = second + "秒前";
		} else if (minute >= 60) {
			Long hour = minute / 60;
			if (hour >= 24) {
				if (hour > 720)
					timeContent = "1月前";
				else if (hour > 168 && hour <= 720)
					timeContent = (hour / 168) + "周前";
				else
					timeContent = (hour / 24) + "天前";
			} else {
				timeContent = hour + "小时前";
			}
		} else {
			timeContent = minute + "分钟前";
		}
		return timeContent;
	}

	/**
	 * author: bob date: 20100729 截取日期, 去掉年份 param: date1 eg. 传入"1986-07-28", 返回
	 * 07-28
	 */
	public static String getMonthAndDay(Date date) {
		return formatDate(date).substring(5);
	}

	public static Date getMillDate() {
		return new Date();
	}

	/**
	 * 时间差：day1-day2
	 * 
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static final <T extends Date> String getDiffDayStr(T day1, T day2) {
		if (day1 == null || day2 == null)
			return "---";
		long diff = day1.getTime() - day2.getTime();
		long sign = diff / Math.abs(diff);
		if (sign < 0)
			return "已经过期";
		diff = Math.abs(diff) / 1000;
		long day = diff / 3600 / 24;
		long hour = (diff - (day * 3600 * 24)) / 3600;
		long minu = diff % 3600 / 60;
		return (day == 0 ? "" : day + "天") + (hour == 0 ? "" : hour + "小时") + (minu == 0 ? "" : minu + "分");
	}

	/**
	 * 时间差：day1-day2
	 * 
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static final <T extends Date> String getDiffStr(T day1, T day2) {
		if (day1 == null || day2 == null)
			return "---";
		long diff = day1.getTime() - day2.getTime();
		long sign = diff / Math.abs(diff);
		diff = Math.abs(diff) / 1000;
		long hour = diff / 3600;
		long minu = diff % 3600 / 60;
		long second = diff % 60;
		return (sign < 0 ? "-" : "+") + (hour == 0 ? "" : hour + "小时") + (minu == 0 ? "" : minu + "分")
		        + (second == 0 ? "" : second + "秒");
	}

	/**
	 * 时间差（秒）：day1-day2
	 * 
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static final <T extends Date> long getDiffSecond(T day1, T day2) {
		if (day1 == null || day2 == null)
			return 0;
		long diff = day1.getTime() - day2.getTime();
		if (diff == 0)
			return 0;
		long sign = diff / Math.abs(diff);
		diff = Math.abs(diff) / 1000;
		return sign * diff;
	}

	/**
	 * 时间差（分钟）：day1-day2
	 * 
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static final <T extends Date> double getDiffMinu(T day1, T day2) {
		if (day1 == null || day2 == null)
			return 0;
		long diff = day1.getTime() - day2.getTime();
		if (diff == 0)
			return 0;
		long sign = diff / Math.abs(diff);
		diff = Math.abs(diff) / 1000;
		return Math.round(diff * 1.0d * 10 / 6.0) / 100.0 * sign;// 两位小数
	}

	/**
	 * 时间差（分）：time1 - time2
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static final double getMillDiffMinu(long time1, long time2) {
		long diff = time1 - time2;
		if (diff == 0)
			return 0;
		long sign = diff / Math.abs(diff);
		diff = Math.abs(diff) / 1000;
		return Math.round(diff * 1.0d * 10 / 6.0) / 100.0 * sign;// 两位小数
	}

	/**
	 * 时间差（小时）：day1 - day2
	 * 
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static final <T extends Date> double getDiffHour(T day1, T day2) {
		if (day1 == null || day2 == null)
			return 0;
		long diff = day1.getTime() - day2.getTime();
		long sign = diff / Math.abs(diff);
		diff = Math.abs(diff) / 1000;
		return Math.round(diff * 1.0d / 3.6) / 1000.0 * sign;// 三位小数
	}

	/**
	 * @param day1
	 * @param day2
	 * @return 日期相差整数round(abs（day1-day2))
	 */
	public static final <T extends Date> int getDiffDay(T day1, T day2) {
		if (day1 == null || day2 == null)
			return 0;
		long diff = day1.getTime() - day2.getTime();
		diff = Math.abs(diff) / 1000;
		return Math.round(diff / (3600 * 24));
	}

	public static boolean isAfterOneHour(Date date, String time) {
		String datetime = formatDate(date) + " " + time + ":00";
		return addHour(parseTimestamp(datetime), -1).getTime() > System.currentTimeMillis();
	}

	public static boolean isValidDate(String fyrq) {
		return DateUtils.parseDate(fyrq) != null;
	}

	/**
	 * eg. 1997/01/02 22:03:00,return 1997/01/02 00:00:00.0
	 **/
	public static Timestamp getBeginTimestamp(Date date) {
		return new Timestamp(getBeginningTimeOfDay(date).getTime());
	}

	public static Timestamp getEndTimestamp(Date date) {
		return new Timestamp(getLastTimeOfDay(date).getTime());
	}

	/**
	 * @param timestamp
	 * @return
	 */
	public static Date getDateFromTimestamp(Timestamp timestamp) {
		if (timestamp == null)
			return null;
		return new Date(timestamp.getTime());
	}

	public static int after(Date date1, Date date2) {
		date1 = getBeginningTimeOfDay(date1);
		date2 = getBeginningTimeOfDay(date2);
		return date1.compareTo(date2);
	}

	public static Timestamp mill2Timestamp(Long mill) {
		if (mill == null)
			return null;
		return new Timestamp(mill);
	}

	public static int subCurTimeSend() {
		Timestamp curtime = DateUtils.getCurFullTimestamp();
		Timestamp endtime = DateUtils.getLastTimeOfDay(curtime);
		Long scopeSecond = DateUtils.getDiffSecond(endtime, curtime);
		return scopeSecond.intValue();
	}

	/**
	 * @param date
	 * @param pattern:
	 *            Date format pattern
	 * @return
	 */
	public static final <T extends Date> String formatEn(T date, String pattern) {
		if (date == null)
			return null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
			String result = dateFormat.format(date);
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 当前时间加hour个小时后的时间
	 * @param hour
	 * @return
	 */
	public static String getCurAddedHour(int hour) {
		return formatTimestamp(System.currentTimeMillis() + m_hour * hour);
	}

	public static String getCurAddedDay(int day) {
		return formatTimestamp(System.currentTimeMillis() + m_day * day);
	}
	
	public static Timestamp getMin(Timestamp time1, Timestamp... timen) {
		if (timen == null || timen.length < 1) {
			return time1;
		}
		Timestamp time = time1;
		for (Timestamp t : timen) {
			if (t == null) {
				continue;
			}
			if (time == null) {
				time = t;
			} else if (t.before(time)) {
				time = t;
			}
		}
		return time;
	}

	public static Timestamp getMax(Timestamp time1, Timestamp... timen) {
		if (timen == null || timen.length < 1) {
			return time1;
		}
		Timestamp time = time1;
		for (Timestamp t : timen) {
			if (t == null) {
				continue;
			}
			if (time == null) {
				time = t;
			} else if (t.after(time)) {
				time = t;
			}
		}
		return time;
	}
	/**
	 * 只做简单的判断是否是xxxx-xx-xx这种日期格式
	 * @param date
	 * @return
	 */
	public static boolean isDateFormat(String date) {
		return date != null && date.length() == 10 && date.charAt(4) == '-' && date.charAt(7) == '-';
	}
	

	/**
	 * @param date
	 * @return yyyyMM格式
	 */
	public static String getCompactMonthStr(String date) {
		if(isDateFormat(date)) {
			return date.substring(0, 4) + date.substring(5, 7);
		}
		throw new IllegalArgumentException("error date format");
	}

    public static String converToString(Date date) {
	    return format(date, "yyyy:MM:dd");
    }

    public static Date convertToDate(String strDate) throws Exception {
	    return parseDate(strDate,"yyyy:MM:dd");
    }

    public static String converToString(long timeMillion) {
        return format(new Date(timeMillion), "yyyy:MM:dd");
    }
}
