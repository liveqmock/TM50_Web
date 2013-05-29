package web.common.util;

import java.io.*;


 
/**
 * @(#) JSUtility.java		2001.07
 *
 * JavaScript 와 JSP 사용을 편리하게 하기 위한 유틸리티를 제공한다.
 * 
 * @author	Cage, Minkoo Kim, fire34@hanmail.net
 */

public final class JSUtil
{
	/**
	 * html의 checkbox가 선택되어 있다면 format value 를 반환하고 
	 * 그 외의 경우는 ''를 반환한다.
	 * <pre>
	 * format value의 예는 다음과 같다.
	 *	  'YES', 'yes', '사용중', 'O', '예'
	 * 사용 예.
	 *    <%= check2String(checkboxValue, "수신") %>
	 * </pre>
	 *
	 * @param	value		boolean
	 * @param	format		String		[ YES | yes | Yes | Y | y | 1 | etc... ]
	 * @return	String
	 */
	public static String check2String(String value, String format)
	{
		if (isChecked(value).equals("checked"))
			return format;
		else
			return "";
	}

	/**
	 * Print out year, month, day use SELECT object
	 *
	 * ex) <select name="year"><%= displayDate(2001, 2002, iYear) %></select>년
	 */
	public static String displayDate(int start, int end, int now)
	{
		StringBuffer buf = new StringBuffer();
		String j = "";

		for (int i = start; i <= end; i++)
		{
			if (i < 10)
				j = "0" + String.valueOf(i);
			else
				j = String.valueOf(i);

			if (now == i)
				buf.append("<option value='" + j + "' SELECTED>" + j + "</option>");
			else
				buf.append("<option value='" + j + "'>" + j + "</option>");
		}
		return buf.toString();
	}

	/**
	 * Print out year, month, day use SELECT object step by interval
	 *
	 * ex) <select name="year"><%= displayDate(1, 59, iMin, 10) %></select>분
	 */
	public static String displayDate(int start, int end, int now, int interval)
	{
		StringBuffer buf = new StringBuffer();
		String j = "";

		for (int i = start; i <= end; i += interval)
		{
			if (i < 10)
				j = "0" + String.valueOf(i);
			else
				j = String.valueOf(i);

			if (now == i)
				buf.append("<option value='" + j + "' SELECTED>" + j + "</option>");
			else
				buf.append("<option value='" + j + "'>" + j + "</option>");
		}
		return buf.toString();
	}

	public static String displayDate(int start, int end, String strNow)
	{
		StringBuffer buf = new StringBuffer();
		String j = "";
		int now = 0;

		// Parsing string into int.
		try
		{
			now = Integer.parseInt(strNow);
		}
		catch (Exception e)
		{
			System.out.print("JSUtility@displayDate : " + e.getMessage());
			return "parsing error";
		}

		// Display date
		for (int i = start; i <= end; i++)
		{
			if (i < 10)
				j = "0" + String.valueOf(i);
			else
				j = String.valueOf(i);

			if (now == i)
				buf.append("<option value='" + j + "' SELECTED>" + j + "</option>");
			else
				buf.append("<option value='" + j + "'>" + j + "</option>");
		}
		return buf.toString();
	}

	public static String displayDate(int start, int end, String strNow, int interval)
	{
		StringBuffer buf = new StringBuffer();
		String j = "";
		int now = 0;

		// Parsing string into int.
		try
		{
			now = Integer.parseInt(strNow);
		}
		catch (Exception e)
		{
			System.out.print("JSUtility@displayDate : " + e.getMessage());
			return "parsing error";
		}

		// Display date
		for (int i = start; i <= end; i += interval)
		{
			if (i < 10)
				j = "0" + String.valueOf(i);
			else
				j = String.valueOf(i);

			if (now == i)
				buf.append("<option value='" + j + "' SELECTED>" + j + "</option>");
			else
				buf.append("<option value='" + j + "'>" + j + "</option>");
		}
		return buf.toString();
	}

	/**
	 * Print out year, month, day use SELECT object
	 *
	  * ex) <select name="year"><%= displayDate("2001", "2003", strYear) %></select>년
	 */
	public static String displayDate(String start, String end, String now)
	{
		StringBuffer buf = new StringBuffer();
		String j = ""; // Tempory string
		int iStart = 0; // start index
		int iEnd = 0; // end index
		int iNow = 0; // current index

		// Parsing data
		try
		{
			iStart = Integer.parseInt(start);
			iEnd = Integer.parseInt(end);
			iNow = Integer.parseInt(now);
		}
		catch (Exception e)
		{
			System.out.println("JSUtility@displayDate(String, String, String) : " + e.toString());
			return "";
		}

		// Make string
		for (int i = iStart; i <= iEnd; i++)
		{
			if (i < 10)
				j = "0" + String.valueOf(i);
			else
				j = String.valueOf(i);

			if (iNow == i)
				buf.append("<option value='" + j + "' SELECTED>" + j + "</option>");
			else
				buf.append("<option value='" + j + "'>" + j + "</option>");
		}
		return buf.toString();
	}

	/**
	 * String 타입의 값을 html의 checkbox에 설정하는 값인 'checked'와 ''로 변환한다.
	 * 입력되는 값이 대소문자 상관없이 'ON', 'YES', 'Y', 'TRUE', '1' 인경우 
	 * 'checked'를 반환하고 이외의 경우 ''를 반환한다.
	 * 
	 * @param	value		boolean		판단할 String 타입 값
	 * @return	String
	 */
	public static String isChecked(String value)
	{
		if (value == null)
			return "";

		if ((value.toLowerCase().equals("on"))
			|| (value.toLowerCase().equals("yes"))
			|| (value.toLowerCase().equals("y"))
			|| (value.toLowerCase().equals("true"))
			|| (value.toLowerCase().equals("1")))
		{
			return "checked";
		}
		else
		{
			return "";
		}
	}

	/** 
	 * Return the string 'checked', only if both values are equal.
	 * 
	 * @param	value		String		current value
	 * @param	compare		String		value to compare
	 * @return	String
	 */
	public static String isChecked(String value, String compare)
	{
		if (value == null || compare == null)
			return "";

		if (value.trim().equalsIgnoreCase(compare.trim())){
			return "checked";
		}
		else
		{
			return "";
		}
	}

	/** 
	 * Return the string 'checked', only if compare contains value
	 * 
	 * @param	value		String		current value
	 * @param	compare		String[]	value to compare
	 * @return	String
	 * @author 김영문 2003.03.11
	 */
	public static String isChecked(String value, String[] compare)
	{
		String result = "";
		if (value == null || compare == null)
			return "";

		for (int i = 0; i < compare.length; i++)
		{
			if (value.equals(compare[i]))
			{
				result = "checked";
			}
		}

		return result;
	}

	/** 
	 * Return the string 'checked', only if compare contains value
	 * 
	 * @param	value		String[]	value current value
	 * @param	compare	to	 compare
	 * @return	String
	 * @author 김민정 2004.06.02
	 */
	public static String isChecked(String[] value, String compare)
	{
		String result = "";
		if (value == null || compare == null)
			return "";

		for (int i = 0; i < value.length; i++)
		{
			if (value[i].equals(compare))
			{
				result = "checked";
			}
		}
		return result;
	}

	/** 
	 * Return the string 'checked', only if both values are equal.
	 * 
	 * @param	value		String		current value
	 * @param	compare		String		value to compare
	 * @return	String
	 */
	public static String isCheckedConatin(String value, String compare)
	{
		if (value == null || compare == null)
			return "";

		if (value.indexOf("," + compare + ",") > -1)
			return "checked";
		else
			return "";
	}

	/** 
	 * Return the 'selected' string if the both of value are equal.
	 */
	public static String isSelected(int value, int compare)
	{
		
		if (value == compare)
			return "selected";
		else
			return "";
	}

	/**
	 * String 타입의 값을 html의 select에 설정하는 값인 'selected'와 ''로 변환한다.
	 * 입력되는 값이 대소문자 관계없이 'YES', 'Y', 'TRUE', '1' 인경우 
	 * 'selected'를 반환하고 이외의 경우 ''를 반환한다.
	 * 
	 * @param	value		boolean		판단할 String 타입 값
	 * @return	boolean
	 */
	public static String isSelected(String value)
	{
		if ((value.toLowerCase().equals("yes"))
			|| (value.toLowerCase().equals("y"))
			|| (value.toLowerCase().equals("true"))
			|| (value.toLowerCase().equals("1")))
		{
			return "selected";
		}
		else
		{
			return "";
		}
	}

	/** 
	 * Return the 'selected' string if the both of value are equal.
	 */
	public static String isSelected(String value, String compare)
	{
		if (value == null || compare == null)
			return "";

		if (value.equals(compare))
			return "selected";
		else
			return "";
	}

	/**
	 * Make option field in the list menu.
	 */
	public static String listMenu(String value, String curValue)
	{
		if (value.equals(curValue))
			return "<option value=" + value + " selected>";
		else
			return "<option value=" + value + ">";
	}

	/**
	 * Make option field use in radio button
	 */
	public static String radioButton(String name, String value, String curValue)
	{
		if (value.equals(curValue))
			return "<input type=radio name=" + name + " value=" + value + " selected>";
		else
			return "<input type=radio name=" + name + " value=" + value + ">";
	}

	/**
	 * Redirect to the inputed URL
	 */
	public static String redirect(String url)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Script language='JavaScript'>");
		sb.append("window.location.href='" + url + "';");
		sb.append("</Script>");
		return sb.toString();
	}

	/**
	 * Make JavaScript
	 */
	public static String script(String s)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Script Language=JavaScript>");
		sb.append(s);
		sb.append("</Script>");

		return sb.toString();
	}

	/**
	 * Show JavaScript alert MSG
	 */
	public static String showMsg(String msg)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Script language='JavaScript'>");
		sb.append("alert('" + msg + "');");
		sb.append("</Script>");
		return sb.toString();
	}

	/**
	 * Show JavaScript alert MSG and redirect to the specified URL with specified target.
	 */
	public static String showMsgNGoBack(String msg, int history)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Script language='JavaScript'>");
		sb.append("alert('" + msg + "');");
		sb.append("history.back(" + history + ");");
		sb.append("</Script>");
		return sb.toString();
	}

	/**
	 * Show JavaScript alert MSG and redirect to the inputed URL
	 */
	public static String showMsgNRedirect(String msg, String url)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Script language='JavaScript'>");
		sb.append("alert('" + msg + "');");
		sb.append("window.location.href='" + url + "';");
		sb.append("</Script>");
		return sb.toString();
	}

	/**
	 * Show JavaScript alert MSG and redirect to the inputed URL with specified target.
	 */
	public static String showMsgNRedirect(String msg, String url, String target)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Script language='JavaScript'>");
		sb.append("alert('" + msg + "');");
		sb.append("parent." + target + ".window.location.href='" + url + "';");
		sb.append("</Script>");
		return sb.toString();
	}

	/**
	 * Prevent JavaScript.. ; Is it work? -_-
	 */
	public final static String translateJS(String s)
	{
		if (s == null)
			return null;

		StringBuffer buf = new StringBuffer();
		char[] c = s.toCharArray();
		int len = c.length;

		for (int i = 0; i < len; i++)
		{
			if (c[i] == '&')
				buf.append("\"+&amp;+\"");
			else if (c[i] == '<')
				buf.append("\"+&lt;+\"");
			else if (c[i] == '>')
				buf.append("\"+&gt;+\"");
			else if (c[i] == '"')
				buf.append("\"+&quot;+\"");
			else if (c[i] == '\'')
				buf.append("\"+&#039;+\"");
			else
				buf.append(c[i]);
		}
		return buf.toString();
	}
	
	/** You can't call the constructor */
	private JSUtil()
	{
	}
	
	/*
	 * 체크박스 체크 
	 * 김유근 작성 
	 */
	public static String isForChecked(String value, String[] arrvalue){			
		
		String result="";
		if(value!=null && arrvalue!=null){
			for(int i=0;i<arrvalue.length;i++){
				if(value.equals(arrvalue[i])){
					result = " checked ";
					break; 
				}
			}
		}
		return result;			
	}	
	
	/**
	 * <p> 최종 인코딩 정보를 통해 인코딩을 변환해 준다. 여기서 lastEncodeYN 는 원투원 Encoding이다. 
	 * @param lastEncodeYN
	 * @param str
	 * @return
	 * @author 김유근 
	 */
	public static String convertEncoding(String lastEncodeYN, String str){
		String resultStr="";		
		
		if(lastEncodeYN!=null && str!=null){
	
			if(lastEncodeYN.equalsIgnoreCase("Y")){
				try{
					resultStr = new String(str.getBytes("ISO-8859-1"),"EUC-KR"); 
					}catch(UnsupportedEncodingException e){}			
			}else{
				resultStr = str;
			}
		}
		return resultStr;
	}
	
	
	public static String getChecked(String str,String value){

		if(str != null && str.indexOf(value) != -1)

			return "checked";

		else

			return "";
	}
}