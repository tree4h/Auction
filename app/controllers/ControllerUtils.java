package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ControllerUtils {
	
	//現在時刻からendで指定された日時までの残り時間を返す
	//60分以上は時間で、1分から60分までは分で、1分未満は秒で、0秒以下は"終了"を返す
	public static String getAvailableTime(Date end) {
		long d1 = (new Date()).getTime();
		long d2 = end.getTime();
		long diffSecond = (d2-d1)/1000;
		if(diffSecond >= 3600) {
			return Long.toString(diffSecond/3600)+"時間";
		}
		else if(diffSecond >= 60) {
			return Long.toString(diffSecond/60)+"分";
		}
		else if(diffSecond > 0) {
			return Long.toString(diffSecond)+"秒";
		}
		else {
			return "終了";
		}
	}
	
	//Itemの出品開始日表示用
	public static String format2(Date date) {
		if(date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			return sdf.format(date);
		}
		return "";
	}
	//Itemの出品期間表示用
	public static String range(Date d1, Date d2) {
		if(d1 != null || d2 != null) {
			Long start = d1.getTime();
			Long end = d2.getTime();
			int range = (int)((end-start)/1000);
			return Integer.toString(range);
		}
		return "";
	}

	//Formのinputデータを標準出力する
	public static void printInput(Map<String,String> input) {
		for (String key : input.keySet()) {
			System.out.println("key=" + key + ", value=" + input.get(key));
		}
	}

	public static Date makeDate(int year,int month,int day,int hour,int minute,int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(year,month-1,day,hour,minute,second);
		return cal.getTime();
	}

	public static Date makeDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date today = new Date();
		String today_str = sdf.format(today);
		if(today_str.equals(date)) {
			return today;
		}
		else {
			return sdf.parse(date);
		}
	}

	public static String format(Date date) {
		//TODO ここでnullチェック？
		if(date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			return sdf.format(date);
		}
		return "";
	}
}
