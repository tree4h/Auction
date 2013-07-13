package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFactory {
	
	public Date newDate() {
		return new Date();
	}
	
	public static Date makeDate(int year,int month,int day,int hour,int minute,int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(year,month-1,day,hour,minute,second);
		return cal.getTime();
	}

	public static Date makeDate(String date) {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date result = new Date();
		try {
			result = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String format(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.format(date);
	}
}
