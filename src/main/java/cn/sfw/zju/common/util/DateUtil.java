package cn.sfw.zju.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Long convertTimeToLong(String time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
			date = dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}
	
	public static Long convertTimeToLong(Date date){
		return date.getTime();
	}
	
	public static String convertLongToTime(Long time){
		Date date= new Date(Long.valueOf(time));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s=dateFormat.format(date);
		return s;
	}
	
	public static Date resetDate(Date date,int day){
		 Calendar calendar=Calendar.getInstance();
	     calendar.setTime(date);
	     calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+day);
	     return calendar.getTime();
	}
	
	public static void main(String args[]){
		Long time= convertTimeToLong("2015-09-20 00:10:00");
		System.out.println(time);
		System.out.println(convertLongToTime(time));
		//1442678400000
		System.out.println(time-1442678400000L);
		System.out.println(convertLongToTime(1415875800000L));
		System.out.println(resetDate(new Date(),10).toLocaleString());
	}
	
	
}