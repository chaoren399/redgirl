package cn.chaoren.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import android.widget.TextView;

public class StatusAction {
    
	private static String foramt = "yyyy-MM-dd hh:mm:ss";
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(foramt);
	public StatusAction() {
		
	}
	
	public static String format(Date date)
	{
		return dateFormat.format(date);
	}
	
	public static String format(Date date,String format)
	{
		SimpleDateFormat mDateFormat = new SimpleDateFormat(format);
		return mDateFormat.format(date);
	}
	
	public static Date parse(String date,String format)
	{
		SimpleDateFormat mDateFormat = new SimpleDateFormat(format);
		try {
			return mDateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date parse(String date)
	{
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 是否直播；是返回true；否则返回false
	 * @param text
	 * @param statuList
	 * @param nowDate
	 * @param status
	 * @param timeMap
	 * @return
	 */
	public boolean setStatus(TextView text,String[]statuList,String status,Map<String, String> timeMap)
	{
		String startDate = timeMap.get("startDate");
		/*int count = 0;*/
		/*System.out.println("nowdate:" + nowDate);
		System.out.println("startdate:" + startDate);*/
		/*String startPosition = timeMap.get("startPosition");*///0，代表未开赛;1,
		
		/*String lastHalfDate = timeMap.get("lastHalfDate");
		String nextHalfDate = timeMap.get("nextHalfDate");
		String nowDateStr = timeMap.get("nowDate");*/
		/*String stop = "";*/
		
		/*String add = timeMap.get("add");*/
		if (startDate == null) {
			timeMap.put("startDate", new Date().getTime()+"");
		}
		int nowDiff;
		try {
			nowDiff = new Integer(timeMap.get("nowDiff"));
		} catch (NumberFormatException e) {
			nowDiff = 0;
		}
		long start = new Long(timeMap.get("startDate"));
		int diff = (int) (new Date().getTime()-start);
		int score = (nowDiff+diff)/(60*1000);
		if ("0".equals(status)) {
			/*stop = "false";*/
			timeMap.remove("startDate");
			text.setText(statuList[0]);
			return true;
			
		}else if ("1".equals(status)) {
			if (score>45) {
				text.setText("45+'");
			}else
			{
				text.setText(score+"'");
			}
			return true;
					
		}else if ("2".equals(status)) {
			timeMap.remove("startDate");
		
			text.setText(statuList[2]);
			
			return true;
		}else if ("3".equals(status)) {
			if (score>45) {
				text.setText("90+'");
			}else
			{
				text.setText((45+score)+"'");
			}
			return true;
			
		}else if ("-14".equals(status)) {
			
			text.setText(statuList[4]);
			return false;
		}else if ("-13".equals(status)) {
			
			text.setText(statuList[5]);
			return false;
		}else if ("-12".equals(status)) {
			
			text.setText(statuList[6]);
			return false;
		}else if ("-11".equals(status)) {
			
			text.setText("" + statuList[7]);
			return false;
		}else if ("-10".equals(status)) {
			
			text.setText("" + statuList[8]);
			return false;
		}else if ("-1".equals(status)) {
			
			text.setText("" + statuList[9]);
			return false;
		}
		
		
		
		return true;
	}
	
}
