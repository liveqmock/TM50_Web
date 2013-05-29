package web.common.util;


import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.text.DecimalFormat;



/**
 * <p>스트링 처리 유틸
 * @author coolang
 *
 */

public class StringUtil {

	/**
	 * <p>메일 유효성 체크 맞으면 true 리턴
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
	     if (email==null || email.equals("")) return false;
	     boolean b = Pattern.matches(
	         "[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", 
	         email.trim());
	     return b;
	 }

	
	/**
	 * <p>이메일에 해당되는 도메인을 가져온다. 
	 * @param sourceStr
	 * @param startStr
	 * @return
	 */
	public static String getEmailDomain(String email){		
		String resultStr = "";		
		if(email!=null && !email.equals("")){
			int r = email.indexOf("@");
			resultStr = email.substring(r+1);
		}
		return resultStr;
	}
	

	/**
	 * <p>메일 제목 및 보내는 사람명 인코딩 
	 * @param uniType
	 * @param str
	 * @return
	 */
	public static String replaceEncodeingHead(String uniType, String str){
		String resultStr = "";
		try{		
			byte[] eucjisStr =str.getBytes(uniType);
			resultStr = Base64.encodeBytes(eucjisStr);		
			resultStr = replace(resultStr,"\n", "?=\r\n =?"+uniType+"?B?");
			resultStr = "=?"+uniType+"?B?" + resultStr + "?=";
		}catch(Exception e){}
		return resultStr;
	}
	
	/**
	 * <p>메일 본문 인코딩 
	 * @param uniType
	 * @param str
	 * @return
	 */
	public static String replaceEncodingContent(String uniType, String str){
		String resultStr = "";
		try{		
			byte[] eucjisStr =str.getBytes(uniType);
			resultStr = Base64.encodeBytes(eucjisStr);
		}catch(Exception e){}
		return resultStr;
	}
	
	
	/**
	 * 문자열을 16진수 유니코드로 변경
	 * 
	 * @author  김영문 (moon@bixon.com)
	 * @date    2004. 7. 22. 오전 9:52:47
	 * @param str
	 * @return
	 */
	public static String strtoUni(String str)
	{
		String uni = "";

		for (int i = 0; i < str.length(); i++)
		{
			char chr = str.charAt(i);
			String hex = Integer.toHexString(chr);
			uni += "\\u" + hex;
		}

		return uni;
	}
	
	
	/**
	 * <p>문자열을 html 16진수로 변경한다. 
	 * @param str
	 * @return
	 */
	public static String strtoUniHtml(String str)
	{
		String uni = "";

		for (int i = 0; i < str.length(); i++)
		{
			char chr = str.charAt(i);		
			String hex = Integer.toHexString(chr);		
			
			uni += "&#x" + hex.toUpperCase() + ";";
		}

		return uni;
	}
	
	
	/**
	 * 16진수 유니코드를 문자열로 변경
	 * 
	 * @author  김영문 (moon@bixon.com)
	 * @date    2004. 7. 22. 오전 9:54:34
	 * @param uni
	 * @return
	 */
	public static String unitoStr(String uni)
	{
		String str = "";
//		UTF-8 지원 모듈 추가 2006-10-18 KJT
        if(uni.indexOf("&#x") == -1) {
            return uni;
        }
        
		StringTokenizer str1 = new StringTokenizer(uni, "&#x");

		while (str1.hasMoreTokens())
		{
			String str2 = str1.nextToken();
			System.out.println(str2);
			try
			{
				if (str2.length() >= 5)
				{
					int i = Integer.parseInt(str2.substring(0, 4), 16);
					str += (char) i;
					if (str2.length() > 5)
					{
						str += str2.substring(5);
					}
				}
				else
				{
					str += str2;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				str += str2;
			}

		}
		return str;
	}
	
	/**
	 * <p>문자열 교체
	 * @param sourceStr
	 * @param findStr
	 * @param replaceStr
	 * @return
	 */
	public static String replace(String sourceStr, String findStr, String replaceStr)
	{
		if (sourceStr == null)
			return "";
		int i = 0;
	
		StringBuffer stringbuffer = new StringBuffer();
		for (int j = sourceStr.indexOf(findStr, i); j >= 0; j = sourceStr.indexOf(findStr, i))
		{
			stringbuffer.append(sourceStr.substring(i, j));
			stringbuffer.append(replaceStr);
			i = j + findStr.length();
		}

		if (i <= sourceStr.length())
			stringbuffer.append(sourceStr.substring(i, sourceStr.length()));
		return stringbuffer.toString();
	}
	
	
	/**
	 * <p>원투원 치환작업처리 
	 * @param onetoneinfo
	 * @param sourceStr
	 * @return
	 */
	public static String replaceOnetoone(String onetoneinfo, String sourceStr, String VALUE_SPLITER, String TAG_SPLITER){
		
		String tempStr1[] = onetoneinfo.split(VALUE_SPLITER);
		String tempStr2[] = null;
		for(int i=0;i<tempStr1.length;i++){
			tempStr2 = tempStr1[i].split(TAG_SPLITER);
			sourceStr = replace(sourceStr,"[$"+tempStr2[0]+"]",tempStr2[1]);
		}
		
		return sourceStr;
	}
	
	
	/**
	 * <p>String 값을 Spliter를 통해 String Array 값으로 만들어 준다. 
	 * @param source
	 * @param spliter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String[] stringToStringArray(String source, String spliter){ 
		
		Collection array = new ArrayList();
		int count = -1;
		count = source.indexOf(spliter);

		while (count > -1)
		{
			array.add(source.substring(0, count).trim());
			//s = s.substring( count + 1 );
			source = source.substring(count + spliter.length());
			count = source.indexOf(spliter);
		}
		String temp = source.trim();
		if (temp.length() > 0)
			array.add(temp.trim());
		
		return (String[]) array.toArray(new String[0]);
	}
	
	/**
	 * <p> String Array를 In Query 형식에 맞는 String으로 변환 시킨다. 
	 * <p>option=0은 작은 따옴표 없는 int형, 1은 작은 따옴표 있는 str형 
	 * @param sourceArray
	 * @param option
	 * @return
	 */
	public static String stringArrayToInQueryString(String[] sourceArray, int option){ 
		
		String returnString = "("; 
		
		for(int i=0; i<sourceArray.length; i++){ 
			
			if(option==0) returnString +=sourceArray[i]; 
				else returnString +="'"+sourceArray[i]+"'";
			
			if(i<sourceArray.length-1) returnString +=","; 
		}
		returnString += ")"; 
		
		return returnString; 
	}
	
	
	public static String formatPrice(int price)
	{
		//		ava.text.DecimalFormat df = new java.text.DecimalFormat("###,##0"); 
		//		String won = df.format(w);

		String price_str = new Integer(price).toString();

		int intval = price_str.length();

		int intval1 = intval / 3;
		int intval2 = intval % 3;

		StringBuffer sb = new StringBuffer();

		String str = "";

		if (intval1 <= 0)
		{
			return price_str;
		}

		if (intval2 != 0)
		{
			sb.append(price_str.substring(0, intval2));
			str = price_str.substring(intval2);
			sb.append(",");
		}
		else
		{
			str = price_str;
		}

		for (int i = 0; i < intval1; i++)
		{
			sb.append(str.substring((3 * i), 3 * (i + 1)));
			if (i != (intval1 - 1))
			{
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	
	public static String formatPrice(String val)
	{
		//		ava.text.DecimalFormat df = new java.text.DecimalFormat("###,##0"); 
		//		String won = df.format(w);
		
		if(val==null ||val.equals("")){
			return "";
		}

		String price_str = new Integer(val).toString();

		int intval = price_str.length();

		int intval1 = intval / 3;
		int intval2 = intval % 3;

		StringBuffer sb = new StringBuffer();

		String str = "";

		if (intval1 <= 0)
		{
			return price_str;
		}

		if (intval2 != 0)
		{
			sb.append(price_str.substring(0, intval2));
			str = price_str.substring(intval2);
			sb.append(",");
		}
		else
		{
			str = price_str;
		}

		for (int i = 0; i < intval1; i++)
		{
			sb.append(str.substring((3 * i), 3 * (i + 1)));
			if (i != (intval1 - 1))
			{
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * <p>DB종류에 따라 쿼리 추출정보가 달라진다. 넘겨진 쿼리에 맞게 카운트 쿼리를 수정한다. 
	 * @param query
	 * @param driveName
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String parsingQueryByDBType(String query, String driverName, int start, int end){
		String resultQuery = "";
		//MYSQL
		String mysqlQuery= " LIMIT "+start+","+end;
		//ORACLE
		String oracleQuery = " ROWNUM ";
		//MSSQL
		String mssqlQuery = " TOP ";
		
		if(driverName.toLowerCase().indexOf("mysql")!=-1){
			resultQuery = query + mysqlQuery;
		}else if(driverName.toLowerCase().indexOf("oracle")!=-1){
			resultQuery = query + oracleQuery +">="+start+" AND "+oracleQuery+"<="+end;
		}else if(driverName.toLowerCase().indexOf("mssql")!=-1){	//mssql은 end만 필요하다. 
			int select = query.toLowerCase().indexOf("select ");
			int from = query.toLowerCase().indexOf(" from ");
			String selects = query.substring(select, "select".length()+2);
			String froms = query.substring(selects.length(), query.length());
			resultQuery = selects + mssqlQuery + end + froms;
		}else{
			resultQuery = driverName;
		}
		
		return resultQuery;
	}
	
	/**
	@auth	: 백광현
	@story	: 전체 메일 내용을 받아서 첫번째 인자값의 내용을 두번째 인자값으로 바꾼다
	@date	: 2002-06-20

	@str	: 받아온 전체 문자
	@n1		: 찾을문자
	@n2		: 바뀔 문자
*/
	public static String rplc(String str, String n1, String n2)
	{
		int itmp = 0;
		if (str == null)
			return "";
	
		String tmp = str;
		StringBuffer sb = new StringBuffer();
		sb.append("");
	
		while (tmp.indexOf(n1) > -1)
		{
			itmp = tmp.indexOf(n1); // 첫번째 찾은(n1)까지의 index
			sb.append(tmp.substring(0, itmp)); // 전체글 처음부터 찾은 index까지를 sb에 추가
			sb.append(n2); // 바뀌어야 할 글 추가
			tmp = tmp.substring(itmp + n1.length());
			// 첫번째 찾은(n1)까지의 index부터 n1의 길이의 index부터 전체글 시작
		}
		sb.append(tmp);
		return sb.toString();
	}
	
	
	/**
	 * <p>원투원 치환작업처리 
	 * @write : 김유근 
	 * @param onetoneinfo
	 * @param sourceStr
	 * @return
	 */
	public static String replaceOnetoone(String onetoneinfo, String sourceStr){
		
		try{
			String tempStr1[] = onetoneinfo.split(Constant.VALUE_SPLITER);
			String tempStr2[] = null;
			for(int i=0;i<tempStr1.length;i++){
				tempStr2 = tempStr1[i].split(Constant.TAG_SPLITER);
				sourceStr = replace(sourceStr,tempStr2[0],tempStr2[1]);
			}
					
		}catch(Exception e){
			sourceStr = null;
		}

		return sourceStr;
	}
	
	
	/**
	 * <p>html내에 반복구문을 처리해준다. 
	 * @write : 김유근 
	 * @param fileContent
	 * @param repeatGroupYN
	 * @return
	 */
	public static String replaceOnetooneRepeat(String onetooneInfo, String content){

	
		try{
			
			//1. 먼저 반복그룹먼저 처리해준다. 
			// (매우중요)onetoeoneInfo에는 반드시 반복그룹이 먼저 입력되어 있어야  한다. 반복그룹을 작성하고 일반내용을  나중에 작성한다.
		
			String[] loopGroupStr = onetooneInfo.split(Constant.REPEAT_GROUP_END_SPLITER);  //ð 를 구분으로 자른다.(반복그룹구분자)			
			
			for(int i=0;i<loopGroupStr.length;i++){
				
				//Æ를 구분으로 다시 자른다. 이때 loopGroup[0]은 반복그룹명이고 loopGroup[1]은 반복데이타이다.
				String[] loopGroup = loopGroupStr[i].split(Constant.REPEAT_GROUP_START_SPLITER);   
				
				//중요)html안에 반드시 반복그룹을 표시하는 <!--LoopDataStart_LoopName1-->   <!--LoopDataEnd_LoopName1-->가 표시되어야 한다. 첫번째 반복그룹은 LoopName1,, 그다음은 LoopName2..
				//대소문자와 공백등의 표시에 유의할 것!!!
				
				//html내에 반복그룹시작명 보통 <!--LoopDataStart_LoopName1-->으로 시작되는 부분
				String loopNamesStart = Constant.LOOPDATA_START + loopGroup[0] + Constant.LOOPDATA_CLOSE;		
				
				//html내에 반복그룹 종료명 보통 <!--LoopDataEnd_LoopName1-->으로 시작되는 부분
				String loopNamesEnd = Constant.LOOPDATA_END + loopGroup[0] + Constant.LOOPDATA_CLOSE;			 
			
				//위에 주석문을 제거한 실제적인 row부분이다. 즉 <tr>등으로 시작하는 부분 
				int groupRowStartPosition  = content.indexOf(loopNamesStart) + loopNamesStart.length();
				//위에 주석문을 제거한 실제적인 row부분이다. 즉 </tr>등으로 끝나는 부분 
				int groupRowEndPosition = content.indexOf(loopNamesEnd);			
				
				//주석문을 포함한 반복그룹시작부분 
				int groupRoopingStart  = content.indexOf(loopNamesStart);
				
				//주석문을 포함한 반복그룹종료부분 
				int groupRoopingEnd = content.indexOf(loopNamesEnd)+ loopNamesEnd.length();		
				
				//반복데이타가 시작하는 row부분 
				String groupRowContent = content.substring(groupRowStartPosition, groupRowEndPosition);
				
				//반복데이타가 시작하는 주석문으로 포함한 부분 
				String groupRowContent2= content.substring(groupRoopingStart, groupRoopingEnd); 				
				
				//변경된 값이 담겨질 부분 
				String groupRowContent3="";
				
				String[] loopGroupRows = loopGroup[1].split(Constant.REPEAT_GROUP_ROW_SPLITER);  //ø를 구분으로 자른다. (row구분자)
				for(int j=0;j<loopGroupRows.length;j++){
					groupRowContent3+=replaceOnetoone(loopGroupRows[j],groupRowContent);	//주석문을 제외한 실제적인 row부분을 치환한다. 				
				}				
				
				content = content.replace(groupRowContent2, groupRowContent3); //전체적인 반복그룹(주석문포함된)을 치환해준다. 
			}
			
			//2.반복그룹을 모두 처리하였다면 나머지 반복되는 않은 것을 처리해준다.
			content =  replaceOnetoone(onetooneInfo,content);
		}catch(Exception e){
			content = null;
		}
	
		
		return content;
	}

	/*
	 *  String 을 받아서 null이면 defaultValue 리턴
	 */

	public static String toStr(String value, String defaultValue) {
		if(value == null) return defaultValue;
		return value;
	}
	
	public static String toStr(String value) {
		return toStr(value,"");
	}
	
	/*
	 * 숫자를 포맷된 스트링으로
	 */
	public static String toFormatStr(String format, Double value) {
		DecimalFormat df = new DecimalFormat(format);
		return df.format(value);
	}
	
	/**   
	* Number형 숫자앞에 원하는 size의 자릿수에 맞게 '0'를 붙인다. 예) 000000000 (일억자리수)에 345를 넣었을   
	* 경우 '000000345'반환   
	*   
	* @param num   
	* 대상 Number형 숫자   
	* @param size   
	* 원하는 자릿수   
	* @return 대상문자열에 자릿수 만큼의 '0'를 붙인 문자열   
	*/   
	public static String zerofill(Number num, int size) throws Exception {   
		String zero = "";   
		for (int i = 0; i < size; i++) {   
		  zero += "0";   
		}   
		DecimalFormat df = new DecimalFormat(zero);   
		return df.format(num);   
	}   	
	
	
	/**
	 * <p>비율 출력 
	 * @param numChild
	 * @param numParent
	 * @return
	 */
	public static String getRatioToString(int numChild, int numParent){
		DecimalFormat df = new DecimalFormat("#.#");
		return df.format( ((double)numChild/(double)numParent) * 100 );
	}
	
	public static String head(String s, int i)
	{
		if (s == null)
			return "";

		String s1 = null;
		if (s.length() > i)
			s1 = s.substring(0, i)+" ..";
		else
			s1 = s;

		return s1;
	}

	public static String tail(String s, int i)
	{
		if (s == null)
			return "";

		String s1 = null;
		if (s.length() > i)
			s1 = s.substring(s.length() - i);
		else
			s1 = s;

		return s1;
	}
	
	public static String delete(String s, String s1)
	{
		return replace(s, s1, "");
	}
	
	/**
	 * 입력받은 문자열을 구분자로 나눠서 n번째 문자열만 반환
	 * 다른 메쏘드로 싸서 사용하길 권함( 구분자로 나눈 갯수보다 n이 크면 빈 문자열 출력 )
	 * 
	 * @param str 대상 문자열
	 * @param delimiter 구분자
	 * @param n n번째
	 * @return str을 delimiter나누었을 때 n번째 문자열
	 */
	public static String getNthString( String str, String delimiter, int n ) 
			throws Exception {
		
        if (str == null || str.equals("")) return "";
        
		StringTokenizer aStringTokenizer = new StringTokenizer(str,delimiter);
		for( int i=1; aStringTokenizer.hasMoreTokens(); i++ ) {
			if( i == n ){
				return aStringTokenizer.nextToken();
			}else{
				aStringTokenizer.nextToken();
			}
		}
		return "";
	}
	
	/**
	 * <p>핸드폰번호 유효성 체크 
	 * @param phone
	 * @return
	 */
	public static boolean isPhoneNumber(String phone) {		 
		boolean resultBool = true;
	     if (phone==null || phone.equals("")){ 
	    	 resultBool =  false;	     
	     }else if(phone.length()<10 || phone.length()>12){
	    	 resultBool =  false;	     
	     }
	     
	     if(!resultBool){
	    	 return false;
	     }
	     
	     resultBool = false;
	     
	     String[] phoneHeads = {"010","011","016","017","018","019","0130"};
	     for(String ph :phoneHeads){
	    	 if(phone.startsWith(ph)){
	    		 resultBool = true;
	    		 break;
	    	 }
	     }
	     
	     if(!resultBool){
	    	 return false;
	     }
	 
	     boolean b = Pattern.matches(
	         "[0-9]+", 
	         phone.trim());
	     return b;
	 }
	
	
	/**
	 * String형 키값 3개를 받고 16자리로 만들거나 끊어서 반환
	 * 
	 * @param key1 
	 * @param key2
	 * @param key3
	 * @return
	 */
	public static String createSecurityKey(String keyOne, String keyTwo, String keyThree) {
		String key = keyOne + keyTwo + keyThree;
		if(key.length() < 16) {
			for(int i = key.length(); i < 16; i++) {
				key += "0";
			}
		}
		if(key.length() > 16) {
			key = key.substring(0, 16);
		}
		return key;
	}
	
}
