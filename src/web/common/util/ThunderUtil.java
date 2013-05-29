package web.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

import web.common.util.Base64;



/**
 * <p>썬더메일5.0에서 사용하는 코드값에 대한 처리 유틸이다. 
 * @author 김유근
 *
 */
public class ThunderUtil {
	
	
	
	/**
	 * <p>발송타입코드에 해당되는 설명값을 리턴한다. 
	 * @param code
	 * @return
	 */
	public static String descSendType(String code){
		String result = "";
		if(code.equals(Constant.SEND_TYPE_NOW)){
			result = "즉시발송";
		}else if(code.equals(Constant.SEND_TYPE_RESERVE)){
			result = "예약발송";
		}else if(code.equals(Constant.SEND_TYPE_REPEAT)){
			result = "반복발송";
		}	
		return result;
	}
	
	
	/**
	 * <p>반복발송타입 설명 
	 * @param code
	 * @return
	 */
	public static String descRepeatSendType(String code){
		String result = "";
		if(code.equals(Constant.REPEAT_SEND_TYPE_0)){
			result = "반복발송중지";
		}else if(code.equals(Constant.REPEAT_SEND_TYPE_1)){
			result = "매일반복발송";
		}else if(code.equals(Constant.REPEAT_SEND_TYPE_2)){
			result = "매주반복발송";
		}else if(code.equals(Constant.REPEAT_SEND_TYPE_3)){
			result = "매월반복발송";
		}
		return result;
	}
	
	
	
	/**
	 * <p>반복발송특정일 출력 
	 * @param code
	 * @return
	 */
	public static String descRepeatSendDay(String code){
		String result = "";
		String[] resultArray = code.split(":");
		for(int i=0;i<resultArray.length;i++){
			result += funcRepeatSendDay(resultArray[i])+"/";
		}
		return result;
	}
	
	/**
	 * <p>반복발송특정일 출력 
	 * @param code
	 * @return
	 */
	public static String funcRepeatSendDay(String code){
		String result = "";
		
		if(code.equals(Constant.REPEAT_SEND_DAY_MON)){
			result = "월";
		}else if(code.equals(Constant.REPEAT_SEND_DAY_TUE)){
			result = "화";
		}else if(code.equals(Constant.REPEAT_SEND_DAY_WED)){
			result = "수";
		}else if(code.equals(Constant.REPEAT_SEND_DAY_THU)){
			result = "목";
		}else if(code.equals(Constant.REPEAT_SEND_DAY_FRI)){
			result = "금";
		}else if(code.equals(Constant.REPEAT_SEND_DAY_SAT)){
			result = "토";
		}else if(code.equals(Constant.REPEAT_SEND_DAY_SUN)){
			result = "일";
		}
		return result;
	}
	
	/**
	 * <p>오픈타입 출력 
	 * @param code
	 * @return
	 */
	public static String descOpenType(String code){
		String result = "사용안함";
		if(code.equals(Constant.OPEN_TYPE_MONTH)){
			result = "최근 한달";
		}else if(code.equals(Constant.OPEN_TYPE_WEEK)){
			result = "최근 한주";
		}
		return result;
	}
	
	/**
	 * <p>오픈타입 출력 
	 * @param code
	 * @return
	 */
	public static String descNotOpenType(String code){
		String result = "사용안함";
		if(code.equals(Constant.NOT_OPEN_TYPE_MONTH)){
			result = "최근 한달";
		}else if(code.equals(Constant.NOT_OPEN_TYPE_WEEK)){
			result = "최근 한주";
		}
		return result;
	}
	
	
	/**
	 * <p>수신거부타입
	 * @param code
	 * @return
	 */
	public static String descRejectType(String code){
		String result = "";
		if(code.equals(Constant.REJECT_TYPE_ALL)){
			result = "전체 수신거부자 제외";
		}else if(code.equals(Constant.REJECT_TYPE_MGROUP)){
			result = "해당 대량메일발송그룹만 수신거부자제외";
		}else if(code.equals(Constant.REJECT_TYPE_UGROUP)){
			result = "소속그룹수신거부자제외";
		}else if(code.equals(Constant.REJECT_TYPE_IGNORE)){
			result = "수신거부자무시";
		}
		return result;
	}
	
	/**
	 * <p>중복여부 
	 * @param code
	 * @return
	 */
	public static String descDuplicationYN(String code){
		String result = "";
		if(code.equals(Constant.DUPLICATION_Y)){
			result = "중복허용";
		}else if(code.equals(Constant.DUPLICATION_N)){
			result = "중복불가";
		}
		return result;
	}
	
	/**
	 * <p>통계공유여부 
	 * @param code
	 * @return
	 */
	public static String descStatisticsOpenType(String code){
		String result = "";
		if(code.equals(Constant.STATISTICS_OPEN_TYPE_NOT)){
			result = "비공유";
		}else if(code.equals(Constant.STATISTICS_OPEN_TYPE_GROUP)){
			result = "그룹공유";
		}else if(code.equals(Constant.STATISTICS_OPEN_TYPE_ALL)){
			result = "전체공유";
		}
		return result;
	}
	
	/**	
	 * <p>링크분석에서 링크타입분류 
	 * @param linkType
	 * @return
	 */
	public static String descLinkType(String linkType){
		String result = "";
		if(linkType.equals(Constant.LINK_TYPE_NORMAL)){
			result="일반링크";
		}else if(linkType.equals(Constant.LINK_TYPE_REJECT)){
			result= "수신거부";
		}
		return result;
	}
	
	/**
	 * <p>공유타입설명 
	 * @param shareType
	 * @return
	 */
	public static String descShareType(String shareType){
		String result = "";
		if(shareType.equals(Constant.SHARE_TYPE_ALL)){
			result = "전체공유";
		}else if(shareType.equals(Constant.SHARE_TYPE_GROUP)){
			result = "그룹공유";
		}else if(shareType.equals(Constant.SHARE_TYPE_NOT)){
			result = "비공유";
		}
		return result;
	}
	
	
	/**
	 * <p>첨부파일을 Base64로 읽어들임 
	 */
	public static  String getAttachedFileToBase64(String savePath, String fileName){
			
		String filePath =  savePath  + File.separator + fileName;
		
		// 파일 내용을 인코딩한 결과를 저장할 변수
		StringBuffer fileContents = new StringBuffer();
		
		FileInputStream fis = null;
		InputStreamReader reader = null;
		BufferedReader buffer = null;
		Base64.InputStream b64is = null;
		
		try{
			// 한줄 한줄 저장할 변수
			String line = null;

			// 파일을 읽어옴
			fis = new FileInputStream(filePath);

			// BASE64 로 인코딩
			b64is = new Base64.InputStream(fis, Base64.ENCODE);
			reader = new InputStreamReader(b64is);
			buffer = new BufferedReader(reader);

			while ((line = buffer.readLine()) != null){
				fileContents.append(line + "\r\n");
			}
			
		}catch(Exception e){
			return null;
		}finally{
			try{buffer.close();}catch(Exception e){}
			try{reader.close();}catch(Exception e){}
			try{fis.close();}catch(Exception e){}
		}
		
		return fileContents.toString();
		
	}
	
	
	/**
	 * <p>원투원 치환작업처리 
	 * @write : 김유근 
	 * @param onetoneinfo
	 * @param sourceStr
	 * @return
	 */
	public static String replaceOnetoone(String onetooneInfo, String sourceStr){
		
		if(onetooneInfo==null || onetooneInfo.equals("")){
			return sourceStr;
		}
		if(sourceStr==null || sourceStr.equals("")){
			return "";
		}
		
		
		try{
			String tempStr1[] = onetooneInfo.split(Constant.VALUE_SPLITER);
			String tempStr2[] = null;
			for(int i=0;i<tempStr1.length;i++){
				tempStr2 = tempStr1[i].split(Constant.TAG_SPLITER);
				if(tempStr2.length<2){  //원투원이 잘못되어 있다면 
					if(sourceStr.indexOf(tempStr2[0])!=-1){	//메일본문에 잘못된 원투원을 사용한다면 
						sourceStr = null;
					}
				}else{				
					sourceStr = sourceStr.replace(tempStr2[0],tempStr2[1]);
				}
			}					
		}catch(Exception e){
			//logger.error(e);
			sourceStr = null;
		}

		return sourceStr;
	}
	/**
	 * <p>원투원 치환작업처리 
	 * @write : 임영호 
	 * @param onetoneinfo
	 * @param key
	 * @return
	 */
	public static String getOnetooneValue(String onetooneInfo, String key){
		String result="";
		try{		
			String tempStr1[] = onetooneInfo.split(Constant.VALUE_SPLITER);
			String tempStr2[] = null;
			
			for(int i=0;i<tempStr1.length;i++){
				tempStr2 = tempStr1[i].split(Constant.TAG_SPLITER);
				if(tempStr2.length > 1){
					if(tempStr2[0].equals(key)) result = tempStr2[1];
				}
			}			
		}catch(Exception e){}

		return result;
	}
	
	/**
	 * <p>원투원 치환작업처리 
	 * @write : 임영호 
	 * @param onetoneinfo
	 * @param key
	 * @return
	 */
	public static String getOnetooneValueSpace(String onetooneInfo, String key){
		String result=" ";
		try{		
			String tempStr1[] = onetooneInfo.split(Constant.VALUE_SPLITER);
			String tempStr2[] = null;
			
			for(int i=0;i<tempStr1.length;i++){
				tempStr2 = tempStr1[i].split(Constant.TAG_SPLITER);
				if(tempStr2.length > 1){
					if(tempStr2[0].equals(key)) result = tempStr2[1];
				}
			}			
		}catch(Exception e){}
		if(result.equalsIgnoreCase("null"))result=" ";
		return result;
	}
	
	/**
	 * <p>html내에 반복구문을 처리해준다. 
	 * @write : 김유근 
	 * @param fileContent
	 * @param repeatGroupYN
	 * @return
	 */
	public static String replaceOnetooneRepeat(String onetooneInfo, String content){
		
		
		//System.out.println("onetooneInfo=="+onetooneInfo);
		
		
		if(onetooneInfo==null || onetooneInfo.equals("")){
			return content;
		}
		if(content==null || content.equals("")){
			return "";
		}
		
		
		//반복그룹구분자가 없다면. 그냥 치환한다.
		if(onetooneInfo.indexOf(Constant.REPEAT_GROUP_END_SPLITER)==-1){
			content =  replaceOnetoone(onetooneInfo,content);
			return content;			
		}
		
		try{
			
			String[] loopGroupStr = onetooneInfo.split(Constant.REPEAT_GROUP_END_SPLITER);  //ð 를 구분으로 자른다.(반복그룹구분자)
			
			
			//1. 먼저 반복그룹먼저 처리해준다.
			// (매우중요)onetoeoneInfo에는 반드시 반복그룹이 먼저 입력되어 있어야  한다. 반복그룹을 작성하고 일반내용을  나중에 작성한다.			
			
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
			e.printStackTrace();
			content = null;
		}
	
		//System.out.println("content=="+content);
		
		return content;
	}
	

	
	
	/**
	 * <p>대량메일 상태
	 * @param code
	 * @return
	 */
	public static String descState(String code){
		String result = "";
		if(code.equals(Constant.STATE_WRITE)){
			result = "임시저장";
		}else if(code.equals(Constant.STATE_APPREADY)){
			result = "승인대기중";
		}else if(code.equals(Constant.REJECT_TYPE_UGROUP)){
			result = "소속그룹수신거부자제외";
		}else if(code.equals(Constant.REJECT_TYPE_IGNORE)){
			result = "수신거부자무시";
		}else if(code.equals(Constant.STATE_PREPARE_READY)){
			result = "발송준비대기중";
		}else if(code.equals(Constant.STATE_PREPAREING)){			
			result = "발송준비중";
		}else if(code.equals(Constant.STATE_PREPARE_FINISH)){ 				
			result = "발송준비완료(발송대기중)";
		}else if(code.equals(Constant.STATE_SENDING)){				
			result = "발송중";
		}else if(code.equals(Constant.STATE_SEND_FINISH)){ 					
			result = "발송완료";
		}else if(code.equals(Constant.STATE_RETRY_SENDING)){ 			
			result = "오류자재발송중";
		}else if(code.equals(Constant.STATE_PREPAREING_ERROR)){			
			result = "발송준비중 오류";
		}else if(code.equals(Constant.STATE_SENDING_ERROR)){			
			result = "발송중 오류";
		}else if(code.equals(Constant.STATE_RETRY_SENDING_ERROR)){	
			result = "오류자재발송중 에러";
		}else if(code.equals(Constant.STATE_PREPARE_STOP)){ 		
			result = "발송준비중일때 정지";
		}else if(code.equals(Constant.STATE_SENDING_STOP)){			
			result = "발송일시정지";
		}else if(code.equals(Constant.STATE_SENDING_PAUSE)){		
			result = "발송완전정지	";
		}
		return result;
	}
	
	/**
	 * <p>대량메일 필터 타입
	 * @param code
	 * @return
	 */
	public static String descFilterType(String code){
		String result = "";
		if(code.equals("0")){
			result = "제외타게팅";
		}else if(code.equals("1")){
			result = "메일형식오류";
		}else if(code.equals("2")){
			result = "중복필터";
		}else if(code.equals("3")){
			result = "수신거부필터";
		}else if(code.equals("4")){
			result = "발송제한필터";
		}else if(code.equals("5")){
			result = "미오픈필터";
		}else if(code.equals("6")){
			result = "영구적인오류필터";
		}else if(code.equals("7")){
			result = "영구적인실패도메인";
		}
		return result;
	}
	
	
	/**
	 * <p>메일 코드 대분류 
	 * @param smtpCodeType
	 * @return
	 */
	public static String descSmtpCodeType(String smtpCodeType){
		String result = "";
		if(smtpCodeType.equals("0")){
			result = "성공";
		}else if(smtpCodeType.equals("1")){
			result = "일시적인 오류";
		}else if(smtpCodeType.equals("2")){
			result = "영구적인 오류";
		}else if(smtpCodeType.equals("3")){
			result = "기타적인 오류";
		}else if(smtpCodeType.equals("4")){
			result = "결과확인 불명";
		}
		return result;
	}
	
	
	/**
	 * <p>실패원인 대분류
	 * @param failCauseCode
	 * @return
	 */
	public static String descFailCauseCodeType(String failCauseCode){
		String result = "";
		if(failCauseCode.equals("10")){
			result = "스펨차단/수신서버차단";
		}else if(failCauseCode.equals("11")){
			result = "수신서버와 통신실패";
		}else if(failCauseCode.equals("12")){
			result = "메일 용량 초과";
		}else if(failCauseCode.equals("20")){
			result = "알수없는 도메인";
		}else if(failCauseCode.equals("21")){
			result = "알수없는 사용자";
		}else if(failCauseCode.equals("22")){
			result = "휴면계정";
		}else if(failCauseCode.equals("30")){
			result = "기타SMTP오류";
		}else if(failCauseCode.equals("31")){
			result = "MXRECORD 오류";
		}else if(failCauseCode.equals("40")){
			result = "성공유무파악안됨";
		}
		return result;
	}
	
	public static String getMD5Hexa(String _str)

	 {
		 MessageDigest md = null;
		 try{
			 md = MessageDigest.getInstance("MD5");
		 }
		 catch(Exception ex){}

		 return getHexa(md.digest(_str.getBytes()));		
	 }

	
	 public static String getHexa(byte[] b)
	 {
		 StringBuffer sb = new StringBuffer(b.length * 2);	
		 for(int i=0; i < b.length; i++){
			 int v = b[i] < 0 ? (int)b[i] + 0x100 : (int)b[i];	                  //음수를 양수로 변환
																			    // (int)b[i]+256와 같다.
			 String s = Integer.toHexString(v);					 //16진수로	
			 if(s.length() == 1) sb.append('0');					 //한자리면 앞에 0을 붙여준다.
			 sb.append(s);			
		 }
		 return sb.toString();
	 }
}
