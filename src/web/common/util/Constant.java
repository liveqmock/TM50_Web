package web.common.util;

public class Constant {

	//대량메일 업로드 파일경로  
	public static final String UPLOAD_PATH_MASSMAIL = "/upload/massmail/";
	//대상자 업로드 파일 경로 
	public static final String UPLOAD_PATH_TARGET = "/upload/target/";
	
	public static final String CONFIGURE_PATH = "/properties/";		//properties폴더명
	public static final String PROPERTIES_EXT = ".properties";		//properties확장자명 
	
	public static final int  DB_CHAR2ID = 10;			// 테이블의  char(2)인 ID값의 초기값은 10 
	public static final int  DB_CHAR3ID = 100;			// 테이블의 char(2)인 ID값의 초기값은 100 
	
	public static final String TM5_DBID = "10";			//초기 썬더메일 설치 DBID 값 
	
	
	public static final String TARGET_TYPE_CSV		= "1";  //CSV파일업로드 
	public static final String TARGET_TYPE_QUERY		= "2";  //QUERY 
	
	public static final String TARGET_TYPE_FILE		= "1";  //파일업로드 
	public static final String TARGET_TYPE_DIRECT	= "2";  //직접입력 
	public static final String TARGET_TYPE_DB	    = "3";  //DB추출
	public static final String TARGET_TYPE_SENDED	= "4";  //기존발송추출
	public static final String TARGET_TYPE_SENDHISTORY	= "5";  //발송히스토리 추출
	public static final String TARGET_TYPE_UI	= "6";  	//회원정보UI

	//회원정보UI WHERE 절 타입 
	public static final int TARGET_UI_WHERETYPE_CHECK	= 1;  	//체크박스
	public static final int TARGET_UI_WHERETYPE_PERIOD	= 2;  	//기간 
	public static final int TARGET_UI_WHERETYPE_INPUT	= 3;  	//값 입력 =
	public static final int TARGET_UI_WHERETYPE_LIKE	= 4;  	//값 입력 like
	
	
	
	
	public static final String TARGET_STATE_READY  = "1"; //등록중
	public static final String TARGET_STATE_ERROR  = "2"; //등록중에러
	public static final String TARGET_STATE_FINISH  = "3"; //등록완료
	
	
	public static final String CONFIG_FLAG_MASSMAIL 		= "1";		//대량메일 환경설정
	public static final String CONFIG_FLAG_AUTOMAIL 		= "2";		//자동메일 환경설정
	public static final String CONFIG_FLAG_INTERMAIL 		= "3";		//연동메일 환경설정
	public static final String CONFIG_FLAG_MASSSMS 			= "4";		//대량SMS 환경설정
	public static final String CONFIG_FLAG_AUTOSMS 		= "5";		//자동SMS 환경설정
	
	//---------------------------------------------------------------------------------------------------------------------------//
	// 테이블 tm_massmail_state의 코드값 
	//---------------------------------------------------------------------------------------------------------------------------//	
	//메일 발송 tm_massmail_state 의 state 값 
	public static final String STATE_WRITE 							= "00";	//임시저장
	public static final String STATE_APPREADY						= "10";	//승인대기중
	public static final String STATE_PREPARE_READY	 			= "11";		//발송준비대기중
	public static final String STATE_PREPAREING					= "12";		//발송준비중
	public static final String STATE_PREPARE_FINISH 				= "13";	//발송준비완료(발송대기중)
	public static final String STATE_SENDING 						= "14";	//발송중
	public static final String STATE_SEND_FINISH 					= "15";	//발송완료
	public static final String STATE_RETRY_SENDING 				= "16";		//오류자재발송중
	public static final String STATE_PREPAREING_ERROR			= "22";		//발송준비중 오류
	public static final String STATE_SENDING_ERROR 				= "24";		//발송중 오류
	public static final String STATE_RETRY_SENDING_ERROR	= "26";			//오류자재발송중 에러 
	public static final String STATE_PREPARE_STOP 				= "32";		//발송준비중일때 정지
	public static final String STATE_SENDING_STOP				= "34";		//(일반)발송일시중지
	public static final String STATE_RETRY_SENDING_STOP				= "36";	//(재)발송일시중지
	public static final String STATE_SENDING_PAUSE			 	= "44";		//발송완전중지		
	public static final String STATE_SENDING_READY			 	= "45";		//발송중 대기 (사용자가 일시정지 한 것 아님, 우선순위 발송에 의해 시스템에서 일시정지한 상태, 발송할 순서가 되면 자동으로 발송중 상태로 변경됨)
	
	//대량SMS  tm_massms_schedule 의 state 값
	public static final String SMS_STATE_WRITE							= "00";		//임시저장
	public static final String SMS_STATE_APPREADY 						= "10";		//승인대기중
	public static final String SMS_STATE_PREPARE_READY	 			= "11";		//발송준비대기중
	public static final String SMS_STATE_PREPAREING						= "12";		//발송준비중
	public static final String SMS_STATE_PREPARE_FINISH 				= "13";		//발송준비완료(발송대기중)
	public static final String SMS_STATE_SENDING 						= "14";		//발송중
	public static final String SMS_STATE_SEND_FINISH 					= "15";		//발송완료			
	public static final String SMS_STATE_PREPAREING_ERROR			= "22";		//발송준비중 오류
	public static final String SMS_STATE_SENDING_ERROR 				= "24";		//발송중 오류	 
	public static final String SMS_STATE_PREPARE_STOP 				= "32";		//발송준비중일때 정지
	public static final String SMS_STATE_SENDING_STOP					= "34";		//발송일시중지
	public static final String SMS_STATE_SENDING_PAUSE			 	= "44";		//발송완전중지
	
	
	//연동메일 tm_intermail_event의 state 값	
	public static final String INTER_STATE_READY					= "00";			//발송대기중
	public static final String INTER_STATE_PREPARERING			= "10";			//발송준비중
	public static final String INTER_STATE_PREPARED				= "11";			//발송준비완료
	public static final String INTER_STATE_SENDING				= "12";			//발송중
	public static final String INTER_STATE_FINISHED				= "13";			//발송완료
	public static final String INTER_STATE_APPROVE				= "14";			//발송승인대기
	public static final String INTER_STATE_RETRY_SENDING		= "15";			//오류자재발송중
	public static final String INTER_STATE_READY_PAUSE 		= "20";			//발송대기중 일시중지
	public static final String INTER_STATE_PAUSE 					= "21";			//일시중지
	public static final String INTER_STATE_STOP 					= "22";			//완전중지(데이터인서트불가)
	public static final String INTER_STATE_READY_ERROR		= "40";			//발송준비중에러 
	public static final String INTER_STATE_SENDING_ERROR		= "41";			//발송중에러
	public static final String INTER_STATE_RETRYING_ERROR	= "42";			//오류자재발송중에러 
	
	
	
	//메일발송타입
	public static final String SEND_TYPE_TEST						= "0";				//테스트발송
	public static final String SEND_TYPE_NOW						= "1";				//즉시발송
	public static final String SEND_TYPE_RESERVE 					= "2";				//예약발송
	public static final String SEND_TYPE_REPEAT					= "3";				//반복발송 
	
	
	//반복발송타입
	public static final String REPEAT_SEND_TYPE_0				= "0";				//반복발송중지
	public static final String REPEAT_SEND_TYPE_1				= "1";				//매일반복발송
	public static final String REPEAT_SEND_TYPE_2				= "2";				//매주반복발송
	public static final String REPEAT_SEND_TYPE_3				= "3";				//매월반복발송
	

	
	//반복발송특정일 
	public static final String REPEAT_SEND_DAY_MON			= "1";				//월요일 
	public static final String REPEAT_SEND_DAY_TUE			= "2";				//화요일
	public static final String REPEAT_SEND_DAY_WED			= "3";				//수요일
	public static final String REPEAT_SEND_DAY_THU			= "4";				//목요일
	public static final String REPEAT_SEND_DAY_FRI			= "5";				//금요일
	public static final String REPEAT_SEND_DAY_SAT			= "6";				//토요일
	public static final String REPEAT_SEND_DAY_SUN			= "7";				//일요일
	
	
	//필터링제안
	public static final String FILTER_TYPE_SEND			= "1";					//발송통수제한
	public static final String FILTER_TYPE_NOT				= "2";					//미오픈제한
	public static final String FILTER_TYPE_ALL				= "3";					//모두제한 
	
	
	//대량SMS 필터링제안
	public static final String SMS_FILTER_TYPE_SUCCESS			= "1";					//발송통수제한
	public static final String SMS_FILTER_TYPE_FAIL				= "2";					//미오픈제한
	public static final String SMS_FILTER_TYPE_ALL				= "3";					//모두제한 
	
	//오픈타입 
	public static final String OPEN_TYPE_MONTH					= "1";				//최근 한달
	public static final String OPEN_TYPE_WEEK						= "2";				//최근 한주 
	
	//미오픈타입 
	public static final String NOT_OPEN_TYPE_MONTH			= "1";				//최근 한달
	public static final String NOT_OPEN_TYPE_WEEK			= "2";				//최근 한주
	
	//수신거부타입
	public static final String REJECT_TYPE_ALL							= "1";				//전체수신거부자제외 
	public static final String REJECT_TYPE_MGROUP					= "2";				//해당 대량메일발송그룹만 수신거부자제외
	public static final String REJECT_TYPE_IGNORE					= "3";				//수신거부대상자무시 
	public static final String REJECT_TYPE_UGROUP					= "4";				//소속그룹수신거부자만 제외
	
	
	//중복여부 
	public static final String DUPLICATION_Y							= "Y";					//중복허용
	public static final String DUPLICATION_N						= "N";					//중복불가 
	
	//재발송완료여부 
	public static final String RETRY_FINISH_Y						= "Y";					//재발송완료됨 
	public static final String RETRY_FINISH_N						= "N";					//재발송미완료 
	
	public static final String STATISTICS_TYPE_WEEK				= "1";					//통계수집기간 한주
	public static final String STATISTICS_TYPE_MONTH			= "2";					//통계수집기간 한달
	
	//통계공유여부 
	public static final String STATISTICS_OPEN_TYPE_NOT			= "1";				//비공유 
	public static final String STATISTICS_OPEN_TYPE_GROUP		= "2";				//그룹공유
	public static final String STATISTICS_OPEN_TYPE_ALL			= "3";				//전체공유

	
	//공유타입
	public static final String SHARE_TYPE_D								= "4";				//지정공유
	public static final String SHARE_TYPE_ALL							= "3";				//전체공유
	public static final String SHARE_TYPE_GROUP						= "2";				//그룹공유
	public static final String SHARE_TYPE_NOT							= "1";				//비공유
	
	//---------------------------------------------------------------------------------------------------------------------------//
	// tm_user_auth 
	//---------------------------------------------------------------------------------------------------------------------------//	
	//권한그룹 
	public static final String USER_LEVEL_ADMIN					= "1";				//시스템사용자
	public static final String USER_LEVEL_MASTER					= "2";				//소속관리자 
	public static final String USER_LEVEL_USER						= "3";				//일반사용자 
	
	
	public static final String LINK_TYPE_NORMAL					=	"1";				//일반링크 
	public static final String LINK_TYPE_REJECT					= "2";				//수신거부용 링크 
	
	
	
	//태그처리 
	public static final String TAG_SPLITER							= "Ð";		//원투원 구분태그 
	public static final String VALUE_SPLITER							= "æ"; 	//원투원 값구분 
	public static final String REPEAT_GROUP_START_SPLITER	= "Æ";	//반복그룹명구분자
	public static final String REPEAT_GROUP_ROW_SPLITER		= "ø";    		//반복그룹내에 ROW구분 
	public static final String REPEAT_GROUP_END_SPLITER		= "ð"; 		//반복그룹구분자 	
	public static final String ONETOONE_NAME_TAG = "[$name]";
	
	//반복구문그룹 
	public  static final String LOOPDATA_START = "<!--ThunderMail_LoopDataStart_";    //반복구문 시작주석 
	public  static final String LOOPDATA_END = "<!--ThunderMail_LoopDataEnd_";    //반복구문 시작주석
	public  static final String LOOPDATA_CLOSE="-->";
	
	
	//자동메일상태값 
	public static final String AUTOMAIL_STATE_RUNNING 	= "1";		//가동중 
	public static final String AUTOMAIL_STATE_PAUSE			= "2";			//일시중지
	public static final String AUTOMAIL_STATE_STOP 			= "3";			//완전중지 
	public static final String AUTOMAIL_STATE_ERROR		= "4";		//가동중에러발생 
	
	
	//시스템공지구분값
	public static final String SYSTEM_NOTIFY_AUTO				= "1"; 		//[TM: 시스템알림]
	public static final String SYSTEM_NOTIFY_APPROVE			= "2"; 		//[TM: 승인요청]
	public static final String SYSTEM_NOTIFY_TESTER			= "3"; 		//[TM: 테스트전송]
	public static final String SYSTEM_NOTIFY_THUNDERMAIL	= "4"; 	    //[TM: 고객사알림]
	
	
	public static final String SYSTEM_NOTIFY_AUTO_DESC 					= "[TM: 시스템알림]";
	public static final String SYSTEM_NOTIFY_APPROVE_DESC 				= "[TM: 승인요청]";
	public static final String SYSTEM_NOTIFY_TESTER_DESC 				= "[TM: 테스트전송]";
	public static final String SYSTEM_NOTIFY_THUNDERMAIL_DESC 		= "[TM: 고객사알림]";
	
	
	//시스템공지타입
	public static final String SYSTEM_NOTIFY_TYPE_EMAIL		= "1";		//EMAIL
	public static final String SYSTEM_NOTIFY_TYPE_SMS		= "2";		//SMS
	

	//DB연동 
	public static final String CUSTOMER_TABLE 					= "tm_customer";
	public static final String CONNECT_UPDATE_ONE 			= "1";				//DB연동 1회수집
	public static final String CONNECT_UPDATE_WEEK	 		= "2";				//DB연동 매주 수집
	public static final String CONNECT_UPDATE_MONTH 		= "3";				//DB연동 매월 수집
		
	
	//파일 임포트 테이블
	public static final String FILE_TABLE = "tm_fileimport";
	
	//발송결과테이블
	public static final String EZ_MASSMAIL_SENDRESULT_TBL = "tm_massmail_sendresult";
	
	
	public static final String POLL_STATE_WRITE 	=	"1";			//작성중(임시저장)
	public static final String POLL_STATE_COMPLETE 	=	"2";			//작성완료
	

	//설문치환변수
	//제목 
	public static final String POLL_TEMPLATE_TITLE = "[$TM_POLL_TITLE]";
	
	//머릿말
	public static final String POLL_TEMPLATE_HEAD = "[$TM_POLL_HEAD]";
	//질문들
	public static final String POLL_TEMPLATE_QUESTIONS = "[$TM_POLL_QUESTIONS]";
	//맺음말
	public static final String POLL_TEMPLATE_TAIL = "[$TM_POLL_TAIL]";
	
	
	//설문제목시작-종료
	public static final String POLL_SUBJECT_START = "<!-- #TM_SUBJECT_START -->";
	public static final String POLL_SUBJECT_END = "<!-- #TM_SUBJECT_END -->";
	
	
	//설문 시작문구 시작-종료 
	public static final String POLL_STARTTITLE_START = "<!-- #TM_STARTTITLE_START -->";
	public static final String POLL_STARTTITLE_END = "<!-- #TM_STARTTITLE_END -->";
	
	
	//설문 종료문구 시작-종료
	public static final String POLL_ENDTITLE_START = "<!-- #TM_ENDTITLE_START -->";
	public static final String POLL_ENDTITLE_END = "<!-- #TM_ENDTITLE_END -->";
	
	
	public static final String POLL_QUESTION_DES = "설문 "; //끝에 공백.. ^^
	
	//설문 전송 이미지 
	public static final String POLL_SUBMIT_IMG="/images/poll_submit.gif";
	
	
	//오픈파라미터 구분 
	public static final String OPEN_TAG = "/open";	
	public static final String OPEN_EQAUL_CHAR = "/EQ/";
	public static final String OPEN_AMP_CHAR = "/AP/";
	
	
	//반복그룹 사용타입 
	//tm_intermail_event.repeatGroupType
	public static final String INTERMAIL_REPEAT_TYPE_NO_ALL 		= "1";		//모두사용안함
	public static final String INTERMAIL_REPEAT_TYPE_FILE 			= "2";		//첨부파일에만 사용
	public static final String INTERMAIL_REPEAT_TYPE_CONTENT 		= "3";		//메일본문에만 사용
	public static final String INTERMAIL_REPEAT_TYPE_ALL 				= "4";		//모두사용(첨부파일+메일본문)
	
	
}
