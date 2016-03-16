package cn.com.incardata.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateCompute {
	private final static String Tag = "DataCompute";
	private static DateCompute instance;

	public static DateCompute getInstance(){
		if (instance == null) {
			instance = new DateCompute();
		}
		return instance;
	}

	/** 输入某年某月某日，判断相差几天 yyyy年MM月dd日 HH:mm */
	public int getDateDays (String pattern, String fresh, String old)
	{       
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA); 
		long betweenTime = 0;
		try { 
			Date date = sdf.parse(fresh);//通过日期格式的parse（）方法将字符串转成日期              
			Date dateBegin = sdf.parse(old);
			betweenTime = date.getTime() - dateBegin.getTime(); 
			betweenTime  = betweenTime  / 1000 / 60 / 60 / 24; 
		} catch(Exception e){
			e.printStackTrace();
			Log.e(Tag, "date format wrong");
		}
		return (int)betweenTime; 
	}

	/**
	 * 得到系统当前时间
	 * <p> yyyy年MM月dd日 HH:mm</p>
	 * @return 
	 */
	public String getNewTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		return formatter.format(curDate);
	}
	
	/**
	 * 得到系统当前时间，日期格式自定义
	 * <p> 例如：yyyy－MM－dd HH:mm</p>
	 * @return 
	 */
	public String getNewTime(String template) {
		SimpleDateFormat formatter = new SimpleDateFormat(template, Locale.CHINA);
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		return formatter.format(curDate);
	}
	
	/**
	 * 得到系统当前日期
	 * <p> yyyy年MM月dd日</p>
	 * @return 
	 */
	public String getNewDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		return formatter.format(curDate);
	}


	/**
	 * 得到系统当前日期(补充)
	 * <p> yyyy-MM-dd</p>
	 * @return
     */
	public static String getCurrentYearMonthDay(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		return formatter.format(curDate);
	}


	/**
	 * 获取日期是星期几(补充)
	 * @return 日期是星期几
     */
	public static String getWeekOfDate(Date date){
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	
	/**取出年,月与天
	 * @param date 日期
	 * @return
	 */
	public static String getYearMonthDay(String date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		try {
			return formatter.format(formatter.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	/**取出月与天
	 * @param date 日期
	 * @return
	 */
	public static String getMonthAndDay(String date){
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd", Locale.CHINA);
		try {
			return formatter.format(formatter.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/*** 
     * 当前日期加减天数
     *  
     * @param option 
     *            传入类型 pro：日期减option天，next：日期加option天 
     * @return  
     */  
    public static String getUpDay(int option) {  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);  
        Calendar cl = Calendar.getInstance();  
//        cl.setTime(new Date());  
        cl.add(Calendar.DAY_OF_MONTH, option);  
        return sdf.format(cl.getTime());  
    }

	/**
	 * 获取毫秒的日期及时间
	 * @param time 毫秒
	 * @return
	 */
	public static String getDate(long time){
		Date d = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(d);
	}

	/**
	 * 将时间戳转为日期字符串
	 * @param timeStamp
	 */
	public static String timeStampToDate(long timeStamp){
		Date date=new Date(timeStamp);
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str=format.format(date);
		return str;
	}

	/**
	 * 当前时间
	 * @return 年月日 星期几
	 */
	public static String getWeekOfDate() {
		Date date = new Date();
		//格式化日期，EEEE为星期几格式化
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd EEEE");
		return dateFm.format(date);
	}
}
