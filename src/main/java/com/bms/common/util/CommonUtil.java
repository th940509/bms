package com.bms.common.util;

import java.text.DecimalFormat;
import java.util.Calendar;

import org.springframework.stereotype.Component;

@Component
public class CommonUtil  {
	
	public String calcSearchPeriod(String fixedSearchPeriod) {
		
		String beginDate  = "";
		String endDate    = "";
		String endYear    = "";
		String endMonth   = "";
		String endDay     = "";
		String beginYear  = "";
		String beginMonth = "";
		String beginDay   = "";
		
		DecimalFormat df = new DecimalFormat("00"); // 10진수의 값 원하는 모양으로 포맷 (00모양으로)
		Calendar cal = Calendar.getInstance(); //Calender 객체명 = Calendar.getInstance() / 시스템의 *현재 날짜와 시간정보를 얻기 위해 getinstance()메서드 사용 / 객체가 갖고 있는 날짜 시간 정보는 get()메소드를 이용하여 사용.
		
		endYear   = Integer.toString(cal.get(Calendar.YEAR)); // 현재 년도 가져오기
		endMonth  = df.format(cal.get(Calendar.MONTH) + 1); // 월 가져오기 /  월은 0부터 시작 
		//0~11 이라는 것이다. 그래서 get(Calendar.MONTH)로 얻오오는 값이 0이면 1월을 의미하고 11이면 12월을 의미
		endDay    = df.format(cal.get(Calendar.DATE)); // 일 가져오기
		endDate   = endYear + "-" + endMonth + "-" + endDay;
		
		if 		(fixedSearchPeriod == null) 				cal.add(cal.MONTH,-4); // 기본
		else if (fixedSearchPeriod.equals("one_week")) 		cal.add(Calendar.DAY_OF_YEAR, -7); // 1주전 이므로 -7
		else if (fixedSearchPeriod.equals("two_week")) 		cal.add(Calendar.DAY_OF_YEAR, -14); // 2주전
		else if (fixedSearchPeriod.equals("one_month")) 	cal.add(cal.MONTH,-1); // 1달
		else if (fixedSearchPeriod.equals("two_month")) 	cal.add(cal.MONTH,-2); // 2달
		else if (fixedSearchPeriod.equals("three_month")) 	cal.add(cal.MONTH,-3); // 3달
		else if (fixedSearchPeriod.equals("four_month")) 	cal.add(cal.MONTH,-4); // 4달
		
		beginYear   = Integer.toString(cal.get(Calendar.YEAR));
		beginMonth  = df.format(cal.get(Calendar.MONTH) + 1);
		beginDay    = df.format(cal.get(Calendar.DATE));
		beginDate   = beginYear + "-" + beginMonth + "-" + beginDay; // 위의 값을 대입한 시작 날짜
		
		return beginDate + "," + endDate;
		
	}
	
}
