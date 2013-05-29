package web.common.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * <p>Title: 날짜 관련 유틸리티</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: BIXON technology</p>
 *
 * @author 김남규
 * @version 1.0
 */
public class DateUtils {
	
    /**
     * 주어진 날짜에 주어진 수만큼 더한 날짜를 표시한다.
     * 
     * @param date "yyyyMMdd" format 유형의 Date String
     * @param amount 더할 날짜 수
     * @return 주어진 날짜에 주어진 수만큼 더한 날짜를 "yyyyMMdd" format의 값을 반환한다.
     */
	public static String addDays(String date, int amount) throws java.text.ParseException{
		return addDays(date, amount, "yyyyMMdd");
	}
	
    /**
     * 주어진 format의 날짜에 주어진 수만큼 더한 날짜를 표시한다.
     * 
     * @param activityDate format형식에 맞는 날짜
     * @param amount 더할 날짜 수
     * @param format 날짜의 format 형식 ex> "yyyyMMdd" 또는 "yyyy/MM/dd"
     * @return 주어진 날짜에 주어진 수만큼 더한 날짜를 format형식에 맞우어 값을 반환한다.
     */
    public static String addDays(String activityDate, int amountDay, String format) throws java.text.ParseException
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		java.util.Date date = check(activityDate, format);

		date.setTime(date.getTime() + ((long) amountDay * 1000 * 60 * 60 * 24));
		return formatter.format(date);
	}

    
	/**
	 * 주어진 날짜에 format에 맞게 시간을 더한다.  
	 * 
	 * @param s 날짜
	 * @param amountHour 더할 시간
	 * @param format Date format(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */
	public static String addHours(String s, int amountHour, String format) throws java.text.ParseException
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		java.util.Date date = check(s, format);

		date.setTime(date.getTime() + ((long) amountHour * 1000 * 60 * 60));
		return formatter.format(date);
	}

	/**
	 * 주어진 날짜에 format에 맞게 분을 더한다.  
	 * 
	 * @param s 날짜
	 * @param minute 더할 분수
	 * @param format Date format(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */
	public static String addMinutes(String s, int minute, String format)
		throws java.text.ParseException
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);

		java.util.Date date = check(s, format);

		date.setTime(date.getTime() + ((long) minute * 1000 * 60));

		return formatter.format(date);
	}

	/**
	 * "yyyyMMdd"형식의 날짜에 월을 더한다.
	 * 
	 * @param s 날짜
	 * @param month 더할 월수
	 * @return 
	 * @throws Exception
	 */
	public static String addMonths(String s, int month) throws Exception
	{
		return addMonths(s, month, "yyyyMMdd");
	}

	/**
	 * 날짜 형식에 맞는 날짜에 월수를 더한다. 
	 * 
	 * @param s 날짜
	 * @param addMonth 더할 월수
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws Exception
	 */
	public static String addMonths(String s, int addMonth, String format) throws Exception
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		java.util.Date date = check(s, format);

		java.text.SimpleDateFormat yearFormat =
			new java.text.SimpleDateFormat("yyyy", java.util.Locale.KOREA);
		java.text.SimpleDateFormat monthFormat =
			new java.text.SimpleDateFormat("MM", java.util.Locale.KOREA);
		java.text.SimpleDateFormat dayFormat =
			new java.text.SimpleDateFormat("dd", java.util.Locale.KOREA);
		int year = Integer.parseInt(yearFormat.format(date));
		int month = Integer.parseInt(monthFormat.format(date));
		int day = Integer.parseInt(dayFormat.format(date));

		month += addMonth;
		if (addMonth > 0)
		{
			while (month > 12)
			{
				month -= 12;
				year += 1;
			}
		}
		else
		{
			while (month <= 0)
			{
				month += 12;
				year -= 1;
			}
		}
		java.text.DecimalFormat fourDf = new java.text.DecimalFormat("0000");
		java.text.DecimalFormat twoDf = new java.text.DecimalFormat("00");
		String tempDate =
			String.valueOf(fourDf.format(year))
				+ String.valueOf(twoDf.format(month))
				+ String.valueOf(twoDf.format(day));
		java.util.Date targetDate = null;

		try
		{
			targetDate = check(tempDate, "yyyyMMdd");
		}
		catch (java.text.ParseException pe)
		{
			day = lastDay(year, month);
			tempDate =
				String.valueOf(fourDf.format(year))
					+ String.valueOf(twoDf.format(month))
					+ String.valueOf(twoDf.format(day));
			targetDate = check(tempDate, "yyyyMMdd");
		}

		return formatter.format(targetDate);
	}
	
	
	/**
	 * "yyyyMMdd"형식의 날짜에 년수를 더한다.
	 * 
	 * @param s 날짜
	 * @param year 더할 년수
	 * @return
	 * @throws java.text.ParseException
	 */
	public static String addYears(String s, int year) throws java.text.ParseException
	{
		return addYears(s, year, "yyyyMMdd");
	}

	/**
	 * 날짜 형식에 맞는 날짜에 년수를 더한다.
	 * 
	 * @param s 날짜
	 * @param year 더할 년수
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */
	public static String addYears(String s, int year, String format)
		throws java.text.ParseException
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		java.util.Date date = check(s, format);
		date.setTime(date.getTime() + ((long) year * 1000 * 60 * 60 * 24 * (365 + 1)));
		return formatter.format(date);
	}

	/**
	 * 날짜형식 "yyyyMMdd"에 맞는 두 날짜사이의 나이를 반환한다. 
	 * 
	 * @param from 첫번째 기준 날짜
	 * @param to 두번째 기준 날짜
	 * @return
	 * @throws java.text.ParseException
	 */
	public static int ageBetween(String from, String to) throws java.text.ParseException
	{
		return ageBetween(from, to, "yyyyMMdd");
	}

	/**
	 * 주어진 날짜형식에 맞는 두 날짜사이의 나이를 반환한다. 
	 * 
	 * @param from 첫번째 날짜
	 * @param to 두번째 날짜
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */
	public static int ageBetween(String from, String to, String format)
		throws java.text.ParseException
	{
		return (int) (daysBetween(from, to, format) / 365);
	}

	/**
	 * 주어진 날짜 문자열을 "yyyyMMdd" 형식의 Date객체를 반환한다.
	 * 
	 * @param s 날짜 문자열
	 * @return
	 * @throws java.text.ParseException
	 */
	public static java.util.Date check(String s) throws java.text.ParseException
	{
		return check(s, "yyyyMMdd");
	}

	/**
	 * 날짜를 특정 날짜 형식으로 변환한 Date객체를 반환한다.
	 * 
	 * @param s 날짜
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */
	public static java.util.Date check(String s, String format) throws java.text.ParseException
	{
		if (s == null)
			throw new java.text.ParseException("date string to check is null", 0);
		if (format == null)
			throw new java.text.ParseException("format string to check date is null", 0);

		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		java.util.Date date = null;

		try
		{
			date = formatter.parse(s);
		}
		catch (java.text.ParseException e)
		{
			throw new java.text.ParseException(
				" wrong date:\"" + s + "\" with format \"" + format + "\"",
				0);
		}

		if (!formatter.format(date).equals(s))
			throw new java.text.ParseException(
				"Out of bound date:\"" + s + "\" with format \"" + format + "\"",
				0);
		return date;
	}

	/**
	 * 특정 날짜 형식에 맞는 두 날짜의 차이를 반환한다.
	 * 
	 * @param curDate 기준 날짜
	 * @param compareDate 비교 날짜
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return long 두 날짜 차이
	 * @throws java.text.ParseException
	 */
	public static long compareTime(String curDate, String compareDate, String format)
		throws java.text.ParseException
	{

		java.util.Date d1 = check(curDate, format);
		java.util.Date d2 = check(compareDate, format);
		long duration = d2.getTime() - d1.getTime();

		return duration;
	}

	/**
	 * long 타입의 시간을 특정 날짜 형식으로 변환한다.
	 * 
	 * @param time 시간
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 */
	public static String convert2Date(long time, String format)
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		java.util.Date date = new java.util.Date();

		date.setTime(time);
		return formatter.format(date);
	}

	/**
	 * "yyyyMMdd"형식의 두 날짜에서 차이 년수를 구한다.
	 * 
	 * @param from 기준 날짜
	 * @param to 비교 날짜
	 * @return
	 * @throws java.text.ParseException
	 */
	public static int daysBetween(String from, String to) throws java.text.ParseException
	{
		return daysBetween(from, to, "yyyyMMdd");
	}

	/**
	 * 특정 날짜 형식의 두 날짜에서 차이 년수를 구한다.
	 * 
	 * @param from 기준 날짜
	 * @param to 비교 날짜
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */
	public static int daysBetween(String from, String to, String format)
		throws java.text.ParseException
	{
		java.util.Date d1 = check(from, format);
		java.util.Date d2 = check(to, format);

		long duration = d2.getTime() - d1.getTime();

		return (int) (duration / (1000 * 60 * 60 * 24));
		// seconds in 1 day : 1000 milliseconds = 1 second
	}
	
	/**
	 * 특정 날짜 형식의 두 날짜에서 차이 년수를 구한다.
	 * 
	 * @param from 기준 날짜
	 * @param to 비교 날짜
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */
	public static int daysBetween1(String from, String to)
		throws java.text.ParseException
	{
		Calendar cal = Calendar.getInstance ( );
		int nTotalDate1 = 0, nTotalDate2 = 0, nDiffOfYear = 0, nDiffOfDay = 0;
		int nYear1 = new Integer(to.substring(0, 4));
		int nYear2 = new Integer(from.substring(0, 4));
		int nMonth1 = new Integer(to.substring(4, 6));
		int nMonth2 = new Integer(from.substring(4, 6));
		int nDate1 = new Integer(to.substring(6, 8));
		int nDate2 = new Integer(from.substring(6, 8));
		if ( nYear1 > nYear2 )
		{
		for ( int i = nYear2; i < nYear1; i++ ) 
		{
		cal.set ( i, 12, 0 );
		nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
		}
		nTotalDate1 += nDiffOfYear;
		}
		else if ( nYear1 < nYear2 )
		{
		for ( int i = nYear1; i < nYear2; i++ )
		{
		cal.set ( i, 12, 0 );
		nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
		}
		nTotalDate2 += nDiffOfYear;
		}

		cal.set ( nYear1, nMonth1-1, nDate1 );
		nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
		nTotalDate1 += nDiffOfDay;

		cal.set ( nYear2, nMonth2-1, nDate2 );
		nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
		nTotalDate2 += nDiffOfDay;
		//System.out.println(nTotalDate1-nTotalDate2);
		return nTotalDate1-nTotalDate2;

	}
	
	/**
	 * 특정 날짜 형식의 두 날짜에서 차이 년수를 구한다.
	 * 
	 * @param from 기준 날짜
	 * @param to 비교 날짜
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */
	public static int monthBetween(String from, String to)
		throws java.text.ParseException
	{
		Calendar cal = Calendar.getInstance ( );
		int nTotalDate1 = 0, nTotalDate2 = 0, nDiffOfYear = 0, nDiffOfDay = 0;
		int nYear1 = new Integer(to.substring(0, 4));
		int nYear2 = new Integer(from.substring(0, 4));
		int nMonth1 = new Integer(to.substring(4, 6));
		int nMonth2 = new Integer(from.substring(4, 6));
		int nDate1 = 1;
		int nDate2 = 1;
		if ( nYear1 > nYear2 )
		{
		for ( int i = nYear2; i < nYear1; i++ ) 
		{
		cal.set ( i, 12, 0 );
		nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
		}
		nTotalDate1 += nDiffOfYear;
		}
		else if ( nYear1 < nYear2 )
		{
		for ( int i = nYear1; i < nYear2; i++ )
		{
		cal.set ( i, 12, 0 );
		nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
		}
		nTotalDate2 += nDiffOfYear;
		}

		cal.set ( nYear1, nMonth1-1, nDate1 );
		nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
		nTotalDate1 += nDiffOfDay;

		cal.set ( nYear2, nMonth2-1, nDate2 );
		nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
		nTotalDate2 += nDiffOfDay;
		//System.out.println(nTotalDate1-nTotalDate2);
		return (nTotalDate1-nTotalDate2)/30;

	}

	/**
	 * 메일 발송시 Date : 태그에 찍을 내용을 만든다.
	 *	예) "Mon, 7 Apr 2003 13:27:19 +0900" 와 같이 넘어온다.
	 * 
	 * @return
	 */
	public static String getDate()
	{
		String resultDate = "";

		Calendar rightNow = Calendar.getInstance();
		int yoil = rightNow.get(Calendar.DAY_OF_WEEK); // 현재 요일

		if (yoil == 1)
		{
			resultDate += "Sun, ";
		}
		else if (yoil == 2)
		{
			resultDate += "Mon, ";
		}
		else if (yoil == 3)
		{
			resultDate += "Tue, ";
		}
		else if (yoil == 4)
		{
			resultDate += "Wed, ";
		}
		else if (yoil == 5)
		{
			resultDate += "Thu, ";
		}
		else if (yoil == 6)
		{
			resultDate += "Fri, ";
		}
		else if (yoil == 7)
		{
			resultDate += "Sat, ";
		}

		resultDate += rightNow.get(Calendar.DATE);

		resultDate += " ";

		int dal = rightNow.get(Calendar.MONTH);
		if (dal == 0)
		{
			resultDate += "Jan ";
		}
		else if (dal == 1)
		{
			resultDate += "Feb ";
		}
		else if (dal == 2)
		{
			resultDate += "Mar ";
		}
		else if (dal == 3)
		{
			resultDate += "Apr ";
		}
		else if (dal == 4)
		{
			resultDate += "May ";
		}
		else if (dal == 5)
		{
			resultDate += "Jun ";
		}
		else if (dal == 6)
		{
			resultDate += "Jul ";
		}
		else if (dal == 7)
		{
			resultDate += "Aug ";
		}
		else if (dal == 8)
		{
			resultDate += "Sep ";
		}
		else if (dal == 9)
		{
			resultDate += "Oct ";
		}
		else if (dal == 10)
		{
			resultDate += "Nov ";
		}
		else if (dal == 11)
		{
			resultDate += "Dec ";
		}

		resultDate += rightNow.get(Calendar.YEAR);

		resultDate += " ";

		resultDate += getTimeString();

		resultDate += " +0900";

		return resultDate;
	}

	/**
	 * "yyyy-MM-dd"형식의 현재 날짜를 구한다.
	 * 
	 * @return
	 */
	public static String getDateString()
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}
	
	
	
	
	/**
	 * "yyyy-MM-dd hh:mm:ss"형식의 현재 날짜를 구한다.
	 * 
	 * @return
	 */
	public static String getDateTimeString()
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}


	/**
	 * 주어진 날짜 형식의 날짜를 int값으로 반환한다.
	 * 
	 * @param pattern 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 */
	public static int getNumberByPattern(String pattern)
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat(pattern, java.util.Locale.KOREA);
		String dateString = formatter.format(new java.util.Date());
		return Integer.parseInt(dateString);
	}
	
	/**
	 * "dd"형식의 현재 일(day)를 구한다.
	 * @return
	 */
	public static int getDay()
	{
		return getNumberByPattern("dd");
	}

	/**
	 * 주어진 날짜 형식의 현재 날짜를 구한다.
	 * 
	 * @param pattern 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 */
	public static String getFormatString(String pattern)
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat(pattern, java.util.Locale.KOREA);
		String dateString = formatter.format(new java.util.Date());
		return dateString;
	}

	/**
	 * "MM"형식의 현재 월을 구한다.
	 * 
	 * @return
	 */
	public static int getMonth()
	{
		return getNumberByPattern("MM");
	}

	/**
	 * 주어진 월에서 다음 월을 구한다.
	 * 
	 * @param month 기준 월
	 * @return
	 */
	public static int getNextMonth(int month)
	{
		if (month == 12)
			return 1;
		else
			return month + 1;
	}

	/**
	 * 다음 달의 년도를 구한다.
	 * 
	 * @param year 현재 년도
	 * @param month 현재 월
	 * @return
	 */
	public static int getNextYear(int year, int month)
	{
		if (month == 12)
			return year + 1;
		else
			return year;
	}

	/**
	 * 이전 월을 구한다.
	 * 
	 * @param month 현재 월
	 * @return
	 */
	public static int getPrevMonth(int month)
	{
		if (month == 1)
			return 12;
		else
			return month - 1;
	}

	/**
	 * Get previous year
	 */
	
	/**
	 * 현재 년/월에서 이전 월의 년도를 구한다.
	 * 
	 * @param year 현재 년
	 * @param month 현재 월
	 * @return
	 */
	public static int getPrevYear(int year, int month)
	{
		if (month == 1)
			return year - 1;
		else
			return year;
	}

	/**
	 * "yyyyMMdd"형식의 현재 날짜를 구한다.
	 * 
	 * @return
	 */
	public static String getShortDateString()
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}

	/**
	 * "HHmmss" 형식의 현재 시간을 구한다.
	 * 
	 * @return
	 */
	public static String getShortTimeString()
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat("HHmmss", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}

	/**
	 * 주어진 날짜형식의 현재 날짜를 구한다.
	 * 
	 * @param pattern 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 */
	public static String getStrByPattern(String pattern)
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat(pattern, java.util.Locale.KOREA);
		String dateString = formatter.format(new java.util.Date());
		return dateString;
	}

	/**
	 * "dd" 형식의 현재 일을 구한다.
	 * 
	 * @return
	 */
	public static String getStrDay()
	{
		return getStrByPattern("dd");
	}

	/**
	 * "MM"형식의 현재 월을 구한다.
	 * 
	 * @return
	 */
	public static String getStrMonth()
	{
		return getStrByPattern("MM");
	}

	/**
	 * "yyyy" 형식의 현재 년도를 구한다.
	 * 
	 * @return
	 */
	public static String getStrYear()
	{
		return getStrByPattern("yyyy");
	}

	/**
	 * "yyyy-MM-dd-HH:mm:ss" 형식의 현재 날짜를 구한다.
	 * 
	 * @return
	 */
	public static String getTimeStampString()
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}
	
	/**
	 * "HH:mm:ss"형식의 현재 시간을 구한다.
	 * 
	 * @return
	 */
	public static String getTimeString()
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}

	/**
	 * int유형의 현재 년도를 구한다.
	 * 
	 * @return
	 */
	public static int getYear()
	{
		return getNumberByPattern("yyyy");
	}

	/**
	 * 주어진 날짜형식의 두 날짜에서 시간차이를 구한다.
	 *  
	 * @param from 기준 날짜
	 * @param to 비교 날짜
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */
	public static int hoursBetween(String from, String to, String format)
		throws java.text.ParseException
	{
		java.util.Date d1 = check(from, format);
		java.util.Date d2 = check(to, format);

		long duration = d2.getTime() - d1.getTime();

		return (int) (duration / (1000 * 60 * 60));
	}

	/**
	 * 주어진 날짜 문자가 "yyyyMMdd"형식의 날짜인지 체크한다.
	 * 
	 * @param s 체크할 날짜
	 * @return
	 * @throws Exception
	 */
	public static boolean isValid(String s) throws Exception
	{
		return DateUtils.isValid(s, "yyyyMMdd");
	}

	/**
	 * 주어진 날짜가 특정 날짜 형식의 날짜인지 체크한다.
	 * 
	 * @param s 체크할 날짜
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 */
	public static boolean isValid(String s, String format)
	{

		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		java.util.Date date = null;
		try
		{
			date = formatter.parse(s);
		}
		catch (java.text.ParseException e)
		{
			return false;
		}

		if (!formatter.format(date).equals(s))
			return false;

		return true;
	}

	/**
	 * 주어진 년/월의 마지막 일을 구한다.
	 * 
	 * @param year 년
	 * @param month 월
	 * @return
	 * @throws java.text.ParseException
	 */
	public static int lastDay(int year, int month) throws java.text.ParseException
	{
		int day = 0;
		switch (month)
		{
			case 1 :
			case 3 :
			case 5 :
			case 7 :
			case 8 :
			case 10 :
			case 12 :
				day = 31;
				break;
			case 2 :
				if ((year % 4) == 0)
				{
					if ((year % 100) == 0 && (year % 400) != 0)
					{
						day = 28;
					}
					else
					{
						day = 29;
					}
				}
				else
				{
					day = 28;
				}
				break;
			default :
				day = 30;
		}
		return day;
	}

	/**
	 * "yyyyMMdd"형식의 날짜에서 마지막 일을 구한다.
	 * 
	 * @param src 날짜
	 * @return
	 * @throws java.text.ParseException
	 */
	public static String lastDayOfMonth(String src) throws java.text.ParseException
	{
		return lastDayOfMonth(src, "yyyyMMdd");
	}

	/**
	 * 특정 형식의 날짜에서 마지막 일을 구한다.
	 * 
	 * @param src 날짜
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */
	public static String lastDayOfMonth(String src, String format) throws java.text.ParseException
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		java.util.Date date = check(src, format);

		java.text.SimpleDateFormat yearFormat =
			new java.text.SimpleDateFormat("yyyy", java.util.Locale.KOREA);
		java.text.SimpleDateFormat monthFormat =
			new java.text.SimpleDateFormat("MM", java.util.Locale.KOREA);

		int year = Integer.parseInt(yearFormat.format(date));
		int month = Integer.parseInt(monthFormat.format(date));
		int day = lastDay(year, month);

		java.text.DecimalFormat fourDf = new java.text.DecimalFormat("0000");
		java.text.DecimalFormat twoDf = new java.text.DecimalFormat("00");
		String tempDate =
			String.valueOf(fourDf.format(year))
				+ String.valueOf(twoDf.format(month))
				+ String.valueOf(twoDf.format(day));
		date = check(tempDate, format);

		return formatter.format(date);
	}

	/**
	 * 특정 날짜형식의 두 날짜에서 시간차이(분) 을 구한다.
	 * 
	 * @param from 기준 날짜
	 * @param to 비교 날짜
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */
	public static int minutesBetween(String from, String to, String format)
		throws java.text.ParseException
	{
		java.util.Date d1 = check(from, format);
		java.util.Date d2 = check(to, format);

		long duration = d2.getTime() - d1.getTime();

		return (int) (duration / (1000 * 60));
	}

	/**
	 * "yyyyMMdd" 형식의 두 날짜에서 차이 월수를 구한다.
	 * 
	 * @param from 기준날짜
	 * @param to 비교날짜
	 * @return
	 * @throws java.text.ParseException
	 */
	public static int monthsBetween(String from, String to) throws java.text.ParseException
	{
		return monthsBetween(from, to, "yyyyMMdd");
	}

	/**
	 * 특정 형식의 두 날짜에서 차이 월수를 구한다.
	 * 
	 * @param from 기준 날짜
	 * @param to 비교 날짜
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */	
	public static int monthsBetween(String from, String to, String format)
		throws java.text.ParseException
	{
		java.util.Date fromDate = check(from, format);
		java.util.Date toDate = check(to, format);

		// if two date are same, return 0.
		if (fromDate.compareTo(toDate) == 0)
			return 0;

		java.text.SimpleDateFormat yearFormat =
			new java.text.SimpleDateFormat("yyyy", java.util.Locale.KOREA);
		java.text.SimpleDateFormat monthFormat =
			new java.text.SimpleDateFormat("MM", java.util.Locale.KOREA);
		java.text.SimpleDateFormat dayFormat =
			new java.text.SimpleDateFormat("dd", java.util.Locale.KOREA);

		int fromYear = Integer.parseInt(yearFormat.format(fromDate));
		int toYear = Integer.parseInt(yearFormat.format(toDate));
		int fromMonth = Integer.parseInt(monthFormat.format(fromDate));
		int toMonth = Integer.parseInt(monthFormat.format(toDate));
		int fromDay = Integer.parseInt(dayFormat.format(fromDate));
		int toDay = Integer.parseInt(dayFormat.format(toDate));

		int result = 0;
		result += ((toYear - fromYear) * 12);
		result += (toMonth - fromMonth);

		//        if (((toDay - fromDay) < 0) ) result += fromDate.compareTo(toDate);
		// ceil과 floor의 효과
		if (((toDay - fromDay) > 0))
			result += toDate.compareTo(fromDate);

		return result;
	}

	/**
	 * date string 을 입력받아 올바른 날짜인지 체크하고 바른 형식이라면
	 * 입력된 새로운 format 으로 date string 을 parsing하여 리턴한다.
	 *
	 * @param	strDate		String		date string
	 * @param	oldFormat	String		old date string format
	 * @param	newFormat	String		new date string format
	 * @last updated	2002.04.18
	 */
	
	/**
	 * 주어진 날짜의 날짜 형식을 새로운 날짜 형식으로 변경한다.
	 * 
	 * @param strDate 변경할 날짜
	 * @param oldFormat 이전 날짜 형식
	 * @param newFormat 새로운 날짜 형식
	 * @return
	 * @throws java.text.ParseException
	 */
	public static String reFormat(String strDate, String oldFormat, String newFormat)
		throws java.text.ParseException
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat(oldFormat, java.util.Locale.KOREA);
		java.util.Date date = null;

		try
		{
			date = formatter.parse(strDate);
		}
		catch (java.text.ParseException e)
		{
			throw new java.text.ParseException(
				"You inputed wrong date:\"" + strDate + "\" with format \"" + oldFormat + "\"",
				0);
		}

		if (!formatter.format(date).equals(strDate))
			throw new java.text.ParseException(
				"You inputed wrong date:\"" + strDate + "\" with format \"" + oldFormat + "\"",
				0);

		formatter = new java.text.SimpleDateFormat(newFormat, java.util.Locale.KOREA);

		return formatter.format(date);
	}

	/**
	 * 특정형식의 두 날짜에서 시간차이 (초)를 구한다.
	 * 
	 * @param from 기준 날짜
	 * @param to 비교 날짜
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */
	public static int secondsBetween(String from, String to, String format)
		throws java.text.ParseException
	{
		java.util.Date d1 = check(from, format);
		java.util.Date d2 = check(to, format);

		long duration = d2.getTime() - d1.getTime();

		return (int) (duration / (1000));
		// seconds in 1 day : 1000 milliseconds = 1 second
	}

	/**
	 * 특정형식의 두 날짜에서 시간차이 (밀리초)를 구한다.
	 * 
	 * @param from 기준 날짜
	 * @param to 비교 날짜
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */
	public static long millisecondsBetween(String from, String to, String format)
	throws java.text.ParseException
	{
		java.util.Date d1 = check(from, format);
		java.util.Date d2 = check(to, format);
	
		long duration = d2.getTime() - d1.getTime();
	
		return duration ;
		// seconds in 1 day : 1000 milliseconds = 1 second
	}

	/**
	 * 두 날짜를 비교해서 주어진 분만큼의 차이 날짜를 만들어 배열로 반환한다.
	 * 
	 * @param startDate 기준 날짜
	 * @param endDate 비교 날짜
	 * @param minute 차이 분
	 * @return
	 * @throws java.text.ParseException
	 */
	@SuppressWarnings("unchecked")
	public static String[] splitDate(String startDate, String endDate, int minute)
		throws java.text.ParseException
	{
		ArrayList result = new ArrayList();

		if (secondsBetween(startDate, endDate, "yyyy-MM-dd HH:mm:ss") > 0)
		{
			String temp_startDate = startDate;
			String temp_endDate = "";

			result.add(temp_startDate);

			for (;;)
			{
				temp_endDate = addMinutes(temp_startDate, minute, "yyyy-MM-dd HH:mm:ss");

				if (secondsBetween(temp_startDate, temp_endDate, "yyyy-MM-dd HH:mm:ss") < 0)
				{
					result.add(endDate);
					break;
				}
				else
				{
					result.add(temp_endDate);
				}
			}

		}

		return (String[]) result.toArray(new String[0]);
	}

	/**
	 * int유형의 년 월 일 값을 받아 'yyyy-MM-dd' 형식의 날짜를 만들어 반환한다.
	 * 
	 * @param year 년
	 * @param month 월 
	 * @param day 일 
	 * @return
	 */
	public static String toDateString(int year, int month, int day)
	{
		StringBuffer strBuf = new StringBuffer();

		// Append year
		strBuf.append(year).append("-");

		// Append month
		if (month < 10)
			strBuf.append("0").append(String.valueOf(month)).append("-");
		else
			strBuf.append(String.valueOf(month)).append("-");

		// Append day
		if (day < 10)
			strBuf.append("0").append(String.valueOf(day));
		else
			strBuf.append(String.valueOf(day));

		return strBuf.toString();
	}

	/**
	 * String 유형의 년 월 일 을 받아 'yyyy-MM-dd'유형의 날짜를 반환한다.
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String toDateString(String year, String month, String day)
	{
		StringBuffer strBuf = new StringBuffer();

		strBuf.append(year).append("-").append(month).append("-").append(day);

		return strBuf.toString();
	}

	/**
	 * "yyyyMMdd'형식의 주어진 날짜의 요일을 구한다.
	 * 1: 일요일 (java.util.Calendar.SUNDAY 와 비교)
	 *          2: 월요일 (java.util.Calendar.MONDAY 와 비교)
	 *          3: 화요일 (java.util.Calendar.TUESDAY 와 비교)
	 *          4: 수요일 (java.util.Calendar.WENDESDAY 와 비교)
	 *          5: 목요일 (java.util.Calendar.THURSDAY 와 비교)
	 *          6: 금요일 (java.util.Calendar.FRIDAY 와 비교)
	 *          7: 토요일 (java.util.Calendar.SATURDAY 와 비교)
	 * 예) String s = "20000529";
	 *  int dayOfWeek = whichDay(s, format);
	 *  if (dayOfWeek == java.util.Calendar.MONDAY)
	 *      System.out.println(" 월요일: " + dayOfWeek);
	 *  if (dayOfWeek == java.util.Calendar.TUESDAY)
	 *      System.out.println(" 화요일: " + dayOfWeek);
	 *      
	 * @param s 날짜
	 * @return
	 * @throws java.text.ParseException
	 */
	public static int whichDay(String s) throws java.text.ParseException
	{
		return whichDay(s, "yyyyMMdd");
	}

	/**
	 * 특정 형식의 주어진 날짜의 요일을 구한다.
	 * 1: 일요일 (java.util.Calendar.SUNDAY 와 비교)
	 *          2: 월요일 (java.util.Calendar.MONDAY 와 비교)
	 *          3: 화요일 (java.util.Calendar.TUESDAY 와 비교)
	 *          4: 수요일 (java.util.Calendar.WENDESDAY 와 비교)
	 *          5: 목요일 (java.util.Calendar.THURSDAY 와 비교)
	 *          6: 금요일 (java.util.Calendar.FRIDAY 와 비교)
	 *          7: 토요일 (java.util.Calendar.SATURDAY 와 비교)
	 * 예) String s = "20000529";
	 *  int dayOfWeek = whichDay(s, format);
	 *  if (dayOfWeek == java.util.Calendar.MONDAY)
	 *      System.out.println(" 월요일: " + dayOfWeek);
	 *  if (dayOfWeek == java.util.Calendar.TUESDAY)
	 *      System.out.println(" 화요일: " + dayOfWeek);
	 *      
	 * @param s 날짜
	 * @param format 날짜 형식(For example, "yyyy-MM-dd".)
	 * @return
	 * @throws java.text.ParseException
	 */
	static int whichDay(String s, String format) throws java.text.ParseException
	{
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		java.util.Date date = check(s, format);

		java.util.Calendar calendar = formatter.getCalendar();
		calendar.setTime(date);
		return calendar.get(java.util.Calendar.DAY_OF_WEEK);
	}

	/**
	 * 'yyyyMMdd'형식의 날짜가 몇번째 주인지를 반환한다.
	 * 
	 * @param s 날짜
	 * @return
	 * @throws java.text.ParseException
	 */
    public static int whichWeek(String s) throws java.text.ParseException
    {
        return whichDay(s, "yyyyMMdd");
    }

    /**
	 * 특정 형식의 날짜가 몇번째 주인지를 반환한다.
	 * 
	 * @param s 날짜
     * @param format 날짜 형식(For example, "yyyy-MM-dd".)
     * @return
     * @throws java.text.ParseException
     */
    public static int whichWeek(String s, String format) throws java.text.ParseException
    {
        java.text.SimpleDateFormat formatter =
            new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
        java.util.Date date = check(s, format);

        java.util.Calendar calendar = formatter.getCalendar();
        calendar.setTime(date);
        return calendar.get(java.util.Calendar.WEEK_OF_MONTH);
    }


	/**
     * 주어진 날짜에 주어진 수만큼 더한 날짜를 표시한다.
     * 
     * @param _date
     * @param _amount
     * @return
     * @throws java.text.ParseException
     */
    public static String getWeekDays(String _date, int _amount) throws java.text.ParseException {
        String res;

        int year = Integer.parseInt(_date.substring(0, 4));
        int month = Integer.parseInt(_date.substring(4, 6));
        int day = Integer.parseInt(_date.substring(6, 8));

        Calendar wCal = Calendar.getInstance();
        wCal.set(year, month - 1, day);
        wCal.add(Calendar.DATE, _amount);

        year = wCal.get(Calendar.YEAR);
        month = wCal.get(Calendar.MONTH) + 1;
        day = wCal.get(Calendar.DATE);

        res = Integer.toString(year);

        if (month < 10)
            res += "0" + Integer.toString(month);
        else
            res += Integer.toString(month);

        if (day < 10)
            res += "0";

        res += Integer.toString(day);
        
        res = res.substring(0,4) + "-" + res.substring(4, 6) + "-" + res.substring(6, 8);
        
        return res;
    }
    
    /**
     * 해당 날짜에 속하는 주의 첫번째 날을 구한다.
     *
     * @param date
     * @return
     */
    public static String getStartDate(String date) {
        String startDate;

        int year = 0;
        int month = 0;
        int day = 0;

        if(date == null || date.trim().length() == 0){
            year = getYear();
            month = getMonth();
            day = getDay();
        }else{
            year = getYear(date);
            month = getMonth(date);
            day = getDay(date);
        }

        // 현재 날에 해당되는 요일을 구한다.
        int curDayOfWeek = getDayOfWeek(year, month, day);

        // 현재 날에 Add할 숫자를 구한다.
        int startTmp = 1 - curDayOfWeek;

        Calendar tmpCalendar = Calendar.getInstance();

        // 해당 Week의 첫번째 날을 구한다.
        tmpCalendar.set(year, month - 1, day);
        tmpCalendar.add(Calendar.DATE, startTmp);

        startDate = Integer.toString(tmpCalendar.get(Calendar.YEAR));

        if ( (tmpCalendar.get(Calendar.MONTH) + 1) < 10)
            startDate += "0";

        startDate += Integer.toString( (tmpCalendar.get(Calendar.MONTH)) + 1);

        if (tmpCalendar.get(Calendar.DATE) < 10)
            startDate += "0";

        startDate += Integer.toString(tmpCalendar.get(Calendar.DATE));

        return startDate;

    }

    /**
     * 년도를 얻는다.
     *
     * @param		date		String
     * @return	int
     */
    public static int getYear(String date) {
        return Integer.parseInt(date.substring(0, 4));
    }

    /**
     * 달을 얻는다.
     *
     * @param		date		String
     * @return	int
     */
    public static int getMonth(String date) {
    	
    	date = date.replaceAll("-", "");
    	
        return Integer.parseInt(date.substring(4, 6));
    }

    /**
     * 일을 얻는다.
     *
     * @param		date		String
     * @return	int
     */
    public static int getDay(String date) {
    	
    	date = date.replaceAll("-", "");
    	
        return Integer.parseInt(date.substring(6, 8));
    }


    /**
     * 현재 일에 해당되는 요일을 구한다.
     *
     * @param		_year		int
     * @param		_month		int
     * @param		_day			int
     * @return	int	Day Of WEEK
     */
    public static int getDayOfWeek(int _year, int _month, int _day) {

        Calendar cal = Calendar.getInstance();
        cal.set(_year, _month - 1, _day);

        return cal.get(Calendar.DAY_OF_WEEK);
    }
    
    /**
     * "yyyy-MM-dd HH:mm:ss" 을 넘겨 받아서 해당 날짜가 무슨 요일인지를
	 * return 한다.
	 * 
     * @param date
     * @return
     */
	public static int getDayOfWeek(String date)
	{
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(getYear(date), getMonth(date) - 1, getDay(date));
		int yoil = rightNow.get(Calendar.DAY_OF_WEEK); // 현재 요일
		return yoil;
	}
    
    public static void main(String args[]){
    	System.out.print(getDayOfWeek("2006-10-23 17:36:33"));
    }
    
    /**
     * 해당 년도, 달의 첫번째날짜(1일)의 요일을 구한다.
     *
     * @param			_year		int
     * @param			_month		int
     * @return		int
     */
    public static int getFirstDayOfWeek(int year, int month) {
        Calendar tmpCal = Calendar.getInstance();
        tmpCal.set(year, month - 1, 1);

        return (tmpCal.get(Calendar.DAY_OF_WEEK) - 1);
    }
    
    /**
     *  int형의 날짜를 String으로 넘겨준다.
     * @param year		int
     * @param month		int
     * @param day		int
     * @return String YYYYMMDD
     */
    public static String getDateOfInt(int year, int month, int day) {

        String strDate = Integer.toString(year);

        strDate += "-";

        if (month < 10)
            strDate += "0";

        strDate += Integer.toString(month);
        strDate += "-";

        if (day < 10)
            strDate += "0";

        strDate += Integer.toString(day);

        return strDate;
    }
    
    /**
     * 년도,달에 해당되는 Date들을 구한다.
     *
     * @param		date		String
     * @return		int[][]
     */
    public static int[][] getMonthDays(String date) {
        int[][] months = new int[6][7];
        int days = 1;

        int year = getYear(date);
        int month = getMonth(date);
        int day = getDay(date);

        for (int i = getFirstDayOfWeek(year, month); i < 7; i++) {
            months[0][i] = days;
            days++;
        }

        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (days <= LastDateInMonth(year, month, day)) {
                    months[i][j] = days;
                    days++;
                }
                else
                    break;
            }
        }

        return months;
    }
    
    /**
     * 달의 마지막날을 구한다.
     *  
     * @param		year		int
     * @param		month		int
     * @param		day			int
     * @return		int
     */
    public static int LastDateInMonth(int year, int month, int day) {
        Calendar tmpCal = Calendar.getInstance();
        tmpCal.set(Calendar.YEAR, year);
        tmpCal.set(Calendar.MONTH, month);
        tmpCal.set(Calendar.DATE, 0);

        return (tmpCal.get(Calendar.DATE));
    }
    
    /**
     * 해당 날짜에 속하는 주의 월요일 날을 구한다.
     *
     * @param date String
     * @return String Date
     */
    public static String getMondayDate(String date) {
        String startDate;

        int year = getYear(date);
        int month = getMonth(date);
        int day = getDay(date);

        // 현재 날에 해당되는 요일을 구한다.
        int curDayOfWeek = getDayOfWeek(year, month, day);

        // 현재 날에 Add할 숫자를 구한다.
        int startTmp = 2 - curDayOfWeek;

        if (startTmp == 0)
            return date;

        if (startTmp > 0) {
            startTmp = startTmp - 7;
        }

        Calendar tmpCalendar = Calendar.getInstance();

        // 해당 Week의 첫번째 날을 구한다.
        tmpCalendar.set(year, month - 1, day);

        tmpCalendar.add(Calendar.DATE, startTmp);

        startDate = Integer.toString(tmpCalendar.get(Calendar.YEAR));

        if ( (tmpCalendar.get(Calendar.MONTH) + 1) < 10)
            startDate += "0";

        startDate += Integer.toString( (tmpCalendar.get(Calendar.MONTH)) + 1);

        if (tmpCalendar.get(Calendar.DATE) < 10)
            startDate += "0";

        startDate += Integer.toString(tmpCalendar.get(Calendar.DATE));

        return startDate;
    }
    
    /**
     * 해당 날짜에 속하는 주의 일주일 날짜를 구한다.
     *
     * @param		date		String
     * @return		String		Date
     */
    public static String[] getWeekDate(String date)  throws java.text.ParseException{
        String[] weekDays = new String[7];
        weekDays[0] = getMondayDate(date);
        weekDays[1] = addDays(weekDays[0], 1);
        weekDays[2] = addDays(weekDays[0], 2);
        weekDays[3] = addDays(weekDays[0], 3);
        weekDays[4] = addDays(weekDays[0], 4);
        weekDays[5] = addDays(weekDays[0], 5);
        weekDays[6] = addDays(weekDays[0], 6);

        return weekDays;
    }
    
    /**
     * 해당 날짜에 속하는 주의 일요일 날짜를 구한다.
     *
     * @param		date		String
     * @return		String		Date
     */
    public static String getSundayDate(String date) {
        String endDate;

        int year = getYear(date);
        int month = getMonth(date);
        int day = getDay(date);

        // 현재 날에 해당되는 요일을 구한다.
        int curDayOfWeek = getDayOfWeek(year, month, day);

        // 현재 날에 Add할 숫자를 구한다.
        int endTmp = 8 - curDayOfWeek;

        if (endTmp >= 7) {
            endTmp -= 7;
        }

        Calendar tmpCalendar = Calendar.getInstance();

        // 해당 Week의 마지막 날을 구한다.
        tmpCalendar.set(year, month - 1, day);
        tmpCalendar.add(Calendar.DATE, endTmp);

        endDate = Integer.toString(tmpCalendar.get(Calendar.YEAR));

        if ( (tmpCalendar.get(Calendar.MONTH) + 1) < 10)
            endDate += "0";

        endDate += Integer.toString( (tmpCalendar.get(Calendar.MONTH)) + 1);

        if (tmpCalendar.get(Calendar.DATE) < 10)
            endDate += "0";

        endDate += Integer.toString(tmpCalendar.get(Calendar.DATE));

        return endDate;
    }

    /**
     * 해당 날짜가 몇번째주인지를 구한다.
     * 
     * @param date
     * @return
     */
    public static int getSeqWeek(String date) {
        int seqWeek = 0;
        int curDate = getDay(date);
        int[][] month = getMonthDays(date);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (month[i][j] != 0) {
                    if (month[i][j] == curDate)
                        seqWeek = i + 1;
                }
            }
        }
        
        return seqWeek;
    }
    
    /**
	 * yyyy#mm#dd 형식의 날짜 문자열을 받아 Date형으로 반환한다.
	 * @param yyyymmdd 구분자를 포함하는 날짜 문자열
	 * @param delimiter #에 해당하는 구분자
	 * @return 날짜 표기에 적절치 않은 문자열 이라면 null을 반환한다.
	 */
	public static Date getDateWithDelimiter(String yyyymmdd, char delimiter) {
		if (yyyymmdd != null && !yyyymmdd.equals("")) {
			StringTokenizer st = new StringTokenizer(yyyymmdd, String.valueOf(delimiter)+"/.-");
			String yyyy = st.nextToken();
			String mm = st.nextToken();
			String dd = st.nextToken();

			if (mm.length() < 2)
				mm = "0" + mm;
			if (dd.length() < 2)
				dd = "0" + dd;

			String temp = yyyy + mm + dd;
			if (isDigit(temp))
				return getDate(temp);
		}

		return null;
	}
	
	/**
	 * yyyymmdd형식의 날짜 문자열을 받아 Date형으로 반환한다.
	 * @param yyyymmdd 년월일(예:20030329 -> 2003년 3월 29일)
	 * @return 날짜 표기에 적절치 않은 문자열 이라면 null을 반환한다.
	 */
	public static Date getDate(String yyyymmdd) {
		if (yyyymmdd != null && yyyymmdd.length() == 8 && isDigit(yyyymmdd)) {
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(yyyymmdd.substring(0,4)),
					Integer.parseInt(yyyymmdd.substring(4,6))-1,
					Integer.parseInt(yyyymmdd.substring(6,8)));

			return cal.getTime();
		}

		return null;
	}
	
	/**
	 * 문자열이 숫자인지 체크
	 * @param digitStr 숫자로 구성된 문자열
	 * @return 숫자로 구성되어 있으며 true, 아니면 false
	 */
	private static boolean isDigit(String digitStr) {
		if (digitStr != null) {
			for (int i=0; i<digitStr.length(); i++)
				if (!Character.isDigit(digitStr.charAt(i)))
					return false;
		}
		return true;
	}
	
    /**
     * 년도를 String으로  얻는다.
     *
     * @param		date		String
     * @return	String
     */
    public static String getYearMonthDayStr(String sysdate) {
    	//String sysdate = "2008-07-09 17:20:00.0";
    	String result ="";
    	if(sysdate!=null && !sysdate.equals("") && !sysdate.equals("null")){
    		try{    			
    			result = sysdate.substring(0, 10);
    		}catch(Exception e){
    			System.out.println(e);
    			result="";
    		}
    	}
        return result;
    }

    
    /**
     * 월을  String으로  얻는다.
     *
     * @param		sysdate		String
     * @return	String
     */
    public static String getMonthStr(String sysdate) {    	
    	//String sysdate = "2008-07-09 17:20:00.0";
    	String result ="";
    	if(sysdate!=null && !sysdate.equals("") && !sysdate.equals("null")){
    		try{
    			result = sysdate.substring(5, 7);
    		}catch(Exception e){
    			System.out.println("sysdate="+sysdate+":"+e);
    			result="";
    		}
    	}
        return result;
    }
    
    /**
     * 일을  String으로  얻는다.
     *
     * @param		sysdate		String
     * @return	String
     */
    public static String getDayStr(String sysdate) {
    	//String sysdate = "2008-07-09 17:20:00.0";
    	String result ="";
    	if(sysdate!=null && !sysdate.equals("") && !sysdate.equals("null")){
    		try{
    			result = sysdate.substring(8, 10);
    		}catch(Exception e){
    			System.out.println("sysdate="+sysdate+":"+e);
    			result="";
    		}
    	}
        return result;
    }
    
    /**
     * 시간을  String으로  얻는다.
     *
     * @param		sysdate		String
     * @return	String
     */
    public static String getHourStr(String sysdate) {
    	//String sysdate = "2008-07-09 17:20:00.0";
    	String result ="";
    	if(sysdate!=null && !sysdate.equals("") && !sysdate.equals("null")){
    		try{
    			result = sysdate.substring(11, 13);
    		}catch(Exception e){
    			System.out.println("sysdate="+sysdate+":"+e);
    			result="";
    		}
    	}
        return result;
    }
    
    
    /**
     * 분을  String으로  얻는다.
     *
     * @param		sysdate		String
     * @return	String
     */
    public static String getMinuteStr(String sysdate) {
    	//String sysdate = "2008-07-09 17:20:00.0";
    	String result ="";
    	if(sysdate!=null && !sysdate.equals("") && !sysdate.equals("null")){
    		try{
    			result = sysdate.substring(14, 16);
    		}catch(Exception e){
    			System.out.println("sysdate="+sysdate+":"+e);
    			result="";
    		}
    	}
        return result;
    }

    
	/**
	 * <p>fromStr에서 toStr까지의 매일날짜를 리턴한다.
	 * @param fromStr
	 * @param toStr
	 * @return
	 * @throws Exception
	 * 김유근 추가 
	 */
	public static ArrayList<String> getDateDifferDay(String fromStr, String toStr) throws Exception{
		ArrayList<String> resultDate = new ArrayList<String>();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fromDate = df.parse(fromStr);
		Date toDate = df.parse(toStr);
		
		Calendar fromC = Calendar.getInstance();
		Calendar toC = Calendar.getInstance();
		fromC.setTime(fromDate);
		toC.setTime(toDate);
	
		
		while(fromC.compareTo(toC)!=1){
			
			//System.out.println(df.format(fromC.getTime()));
			resultDate.add(df.format(fromC.getTime()));
			fromC.add(Calendar.DATE, 1);
		}	
		
		
		return resultDate;
	}
	
	
	/**
	 * <p>fromStr에서 toStr까지의 특정요일의 날짜만을  리턴한다.
	 * @param fromStr
	 * @param toStr
	 * @return
	 * @throws Exception
	 * 김유근 추가 
	 */
	public static ArrayList<String> getDateDifferWeek(String fromStr, String toStr, String[] weeks) throws Exception{
		ArrayList<String> resultDate = new ArrayList<String>();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fromDate = df.parse(fromStr);
		Date toDate = df.parse(toStr);
	
		Calendar fromC = Calendar.getInstance();
		Calendar toC = Calendar.getInstance();
		fromC.setTime(fromDate);
		toC.setTime(toDate);
		while(fromC.compareTo(toC)!=1){
			
			for(String weekday : weeks){
				if(fromC.get(Calendar.DAY_OF_WEEK)==Integer.parseInt(weekday)){						
					//System.out.println(df.format(fromC.getTime()));
					resultDate.add(df.format(fromC.getTime()));					
				}
				
			}
			fromC.add(Calendar.DATE, 1);		
		}

		return resultDate;
	}	
	
	
	/**
	 *  <p>fromStr에서 toStr까지의 특정날짜의 날들을 리턴한다.
	 * @param fromStr
	 * @param toStr
	 * @param dd
	 * @return
	 * @throws Exception
	 * 김유근 추가 
	 */
	public static ArrayList<String> getDateDifferMonth(String fromStr, String toStr, int dd) throws Exception{
		ArrayList<String> resultDate = new ArrayList<String>();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fromDate = df.parse(fromStr);
		Date toDate = df.parse(toStr);
		
		Calendar fromC = Calendar.getInstance();
		Calendar toC = Calendar.getInstance();
		
		fromC.setTime(fromDate);
		toC.setTime(toDate);
	
		while(fromC.compareTo(toC)!=1){
			if(fromC.get(Calendar.DAY_OF_MONTH)==dd){						
				//System.out.println(df.format(fromC.getTime()));
				resultDate.add(df.format(fromC.getTime()));
			}
			fromC.add(Calendar.DATE, 1);		
		}

		return resultDate;
	}
	
	
	/**
	 * <p>주어진 날짜에 주어진 일을 더한 날짜를 yyyy-MM-dd hh:mm:ss 형식으로 리턴 
	 * @param dateStr
	 * @param days
	 * @return
	 * @throws Exception
	 * 김유근추가 
	 */
	public static String getDateAddDays(String dateStr, int days) throws Exception{
		String result = "";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = df.parse(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		result = df.format(cal.getTime());	   
		return result;
	}
		
	
	/**
	 *  <p>주어진 날짜에 주어진 월을 더한 날짜를 yyyy-MM-dd HH:mm:ss 형식으로 리턴 
	 * @param dateStr
	 * @param months
	 * @return
	 * @throws Exception
	 * 김유근 추가 
	 */
	public static String getDateAddMonths(String dateStr, int months) throws Exception{
		String result = "";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = df.parse(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
		result = df.format(cal.getTime());	   
		return result;
	}
	
	/**
	 * <p>현재 날짜에  주어진 일을 더한 날짜를 yyyy-MM-dd형식으로 리턴 
	 * @param dateStr
	 * @param days
	 * @return
	 * @throws Exception
	 * 김유근추가 
	 */
	public static String getNowAddShortDate(int days){
		String result = "";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");	
		Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, days);
		result = df.format(cal.getTime());	  	
 
		return result;
	}
	
	
	/**
	 * <p>현재 날짜에서 주어진 요일의 날짜를 구한다. 만약 오늘날짜보다 같거나 작다면 다음주의 날짜가 출력된다.
	 * @param dd
	 * @return
	 */
	public static String getDateNextWeek(int dd){
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calnow = Calendar.getInstance();	
		Calendar calnext = Calendar.getInstance();
		calnext.set(Calendar.DAY_OF_WEEK,dd);
		
		Date date = null;
		
		if(calnext.getTimeInMillis()<=calnow.getTimeInMillis()){
			date = calnext.getTime();
			date.setTime(date.getTime() + ((long) 7 * 1000 * 60 * 60 *24));
			calnext.setTime(date);
		}
		
		return df.format(calnext.getTime());

	}
	
	/**
	 * <p>현재 날짜에서 주어진 날의 날짜를 구한다. 만약 오늘날짜보다 같거나 작다면 다음달의 날짜가 출력된다.
	 * @param dd
	 * @return
	 */
	public static String getDateNextMonth(int dd){
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calnow = Calendar.getInstance();	
		Calendar calnext = Calendar.getInstance();
		calnext.set(Calendar.DAY_OF_WEEK,dd);	
		
		if(calnext.getTimeInMillis()<=calnow.getTimeInMillis()){
			calnext.add(Calendar.MONTH, 1);
		}
		
		return df.format(calnext.getTime());

	}
	
	/**
	 * <p>mysql등에서 .0이 붙어 나오므로 정확히 19자리만(yyyy-MM-dd HH:mm:ss) 가져온다.
	 * @param date
	 * @return
	 */	
	public static String getDateSubstring(String date){
		String result = "";
		if(date!=null && !date.equals("") && date.length()>19){
			result = date.substring(0,19);			
		}else{
			result = date;
		}
		return result;
		
	}
	
	/**
	 * <p>현재 날짜를 YYYYMM로 가져온다. 
	 * @return
	 */
	public static String getYearMonth(){
		String yearMonth = "";		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");		
		yearMonth = sdf.format(cal.getTime());	
		return yearMonth;		
	}
	
	/**
	 * <p>2010-01-30 의 년월만 리턴(201001)
	 * @param dateStr
	 * @return
	 */
	public static String getYearMonth(String dateStr){
		if(dateStr==null || dateStr.equals("")){
			return "";
		}
		return dateStr.substring(0,4)+dateStr.substring(5,7);
		
	}
	
	
	/**
	 * <p>주어진 시작날짜(yyyyMM)에서 끝난날짜(yyyyMM)사이에 월 출력 
	 * @param fromStr
	 * @param toStr
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> getDifferentYearMonth(String fromStr, String toStr)  throws Exception{
		ArrayList<String> resultDate = new ArrayList<String>();

		DateFormat df = new SimpleDateFormat("yyyyMM");
		Date fromDate = df.parse(fromStr);
		Date toDate = df.parse(toStr);
		
		Calendar fromC = Calendar.getInstance();
		Calendar toC = Calendar.getInstance();
		
		fromC.setTime(fromDate);
		toC.setTime(toDate);
		resultDate.add(df.format(fromC.getTime()));
		while(fromC.compareTo(toC)<0){
			fromC.add(Calendar.MONTH,1);				
			resultDate.add(df.format(fromC.getTime()));			
		}
		return resultDate;
		
	}
	
	/**
	 * <p>날짜를 yyyy-mm-dd HH:mm 형태로 출력한다.
	 * @param dateStr
	 * @return
	 */
	public static String getStringDate(String dateStr){
		String result ="";
		if(dateStr==null || dateStr.equals("") || dateStr.equals("null")){
			result =  "";
		}
		else if(dateStr.length()>16){
			result = dateStr.substring(0,16);
		}else{
			result = dateStr;
		}
		return result;
		
	}
	
	/**
	 * <p>날짜를 yyyy-mm-dd 형태로 출력한다.
	 * @param dateStr
	 * @return
	 */
	public static String getStringShortDate(String dateStr){
		String result ="";
		if(dateStr==null || dateStr.equals("") || dateStr.equals("null")){
			result =  "";
		}
		else if(dateStr.length()>10){
			result = dateStr.substring(0,10);
		}else{
			result = dateStr;
		}
		return result;
		
	}
}    
	
