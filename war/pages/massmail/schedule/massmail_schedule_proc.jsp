<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%@ page import="java.util.*"%>
<%@ page import="web.massmail.schedule.model.ScheduleCalendar"%>
<%@page import="web.common.util.LoginInfo"%>

<%	
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	
	
	String year = null;
	String month = null;  
	String day = "01";
	
	// 오늘 날짜;
	String today = DateUtils.getStrByPattern("yyyyMMdd");
	
	if ((year = request.getParameter("sYear")) == null)
	{
		year = DateUtils.getStrByPattern("yyyy");
	}
	if ((month = request.getParameter("sMonth")) == null)
	{
		month = DateUtils.getStrByPattern("MM");
	}
	if (DateUtils.getStrByPattern("yyyyMM").equals(year + month))
	{
		day = DateUtils.getStrByPattern("dd");
	}
	
	Calendar cal = Calendar.getInstance();

	// 현재 달의 1일 로 셋팅
	cal.set(Integer.parseInt(year), Integer.parseInt(month) - 1 , 1);

	// 현재 요일을 알아냄.
	int indent = cal.get(Calendar.DAY_OF_WEEK);

	// 현재 달의 마지막 날짜를 구하기 위한 연산
	cal.add(Calendar.MONTH, 1);
	cal.add(Calendar.DATE, -1);

	// 현재 달의 마지막 날짜
	int numberOfDays = cal.get(Calendar.DATE);
	
	// 앞 뒤로 달 이동을 위한 연산
	String prevYear = null;
	String prevMonth = null;
	String nextYear = null;
	String nextMonth = null;

	if (Integer.parseInt(month) == 1)
	{
		prevYear = String.valueOf(Integer.parseInt(year) - 1);
		prevMonth = "12";
	}
	else
	{
		String tempMonth = "0" + (Integer.parseInt(month) - 1);
		if (tempMonth.length() == 3) tempMonth = tempMonth.substring(1);
		prevYear = year;
		prevMonth = tempMonth;
	}

	
	if (Integer.parseInt(month) == 12)
	{
		nextYear = String.valueOf(Integer.parseInt(year) + 1);
		nextMonth = "01";
	}
	else
	{
		String tempMonth = "0" + (Integer.parseInt(month) + 1);
		if (tempMonth.length() == 3) tempMonth = tempMonth.substring(1);
		nextYear = year;
		nextMonth = tempMonth;
	}

	

	
	if(method.equals("list")) {
%>

<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
<jsp:useBean id="message" class="java.lang.String" scope="request" />

	<%
	
	List<ScheduleCalendar> scheduleList = (List<ScheduleCalendar>)request.getAttribute("scheduleList");
	
	if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
		
		<div class="schedule_wrapper">
			<div class="movedate">
				<a href="javascript:$('<%=id%>').goPrev(<%=prevYear %>,<%=prevMonth %>)" ><img src="images/btn_pre.gif" /> </a>
				<%=year %>년  <%=month %> 월  
				<a href="javascript:$('<%=id%>').goPrev(<%=nextYear %>,<%=nextMonth %>)" > <img src="images/btn_next.gif" /></a>
			</div>
			
			<table >

			<thead>
				<tr>
	       			<th width='14%' class="sun">일 Sun</font></th>
	       			<th width='14%'>월 Mon</th>
	       			<th width='14%'>화 Tue</th>
			        <th width='14%'>수 Wed</th>
			  		<th width='14%'>목 Thu</th>
		            <th width='14%'>금 Fri</th>
					<th width='14%'>토 Sat</th>
        		</tr>
			</thead>
			<tbody id="<%=id%>_tbody_content">


			<%
			if(month.length()==1)
				month = "0" + month;
			for (int i = 1; i < indent; i++) 
			{
				if (i == 1) 
					out.println("<tr>");
				out.println("<td bgcolor='#ffffff'>&nbsp;</td>");
			}
			for (int i = 1; i <= numberOfDays; i++)
			{
				StringBuffer table_html = new StringBuffer("");
				table_html.setLength(0);
				
				int size_mail = 0;
				int extra_mail = 0;
		
				day = "0" + i;
				if (day.length() == 3)
				{
					day = day.substring(1);
				}
				StringBuffer cell_html = new StringBuffer("");
				cell_html.setLength(0);
				
				StringBuffer send_html = new StringBuffer("");
				send_html.setLength(0);
				
				for (  int j = 0 ; j < scheduleList.size() ; j ++ )
				{
					
					// 해당 날짜가 발송 스케쥴에 등록이 되어 있을 경우
					if ( scheduleList.get(j).getSendScheduleDate().contains(year + "-" + month + "-" +day) )
					{	size_mail++;
						
						String title = "";
						String state = "";
						if(scheduleList.get(j).getMassMailTitle().length() >= 6)
							title = scheduleList.get(j).getMassMailTitle().substring(0,6)+"..";
						else
							title = scheduleList.get(j).getMassMailTitle();
						/*
						if(scheduleList.get(j).getState().equals("00")){state = "<img src=\"images/massmail/save.gif\" title=\"임시저장중\"/>";}
						else if(scheduleList.get(j).getState().equals("10")){state = "<img src=\"images/massmail/approve.gif\" title=\"승인대기중\"/>";}
						else if(scheduleList.get(j).getState().equals("11")){state = "<img src=\"images/massmail/ready.gif\" title=\"발송준비대기중\"/>";}
						else if(scheduleList.get(j).getState().equals("12")){state = "<img src=\"images/massmail/ready.gif\" title=\"발송준비중\"/>";}
						else if(scheduleList.get(j).getState().equals("13")){state = "<img src=\"images/massmail/ready.gif\" title=\"발송준비완료\"/>";}
						else if(scheduleList.get(j).getState().equals("14")){state = "<img src=\"images/massmail/sending.gif\" title=\"발송중\"/>";}
						else if(scheduleList.get(j).getState().equals("15")){state = "<img src=\"images/massmail/finish.gif\" title=\"발송완료\"/>";}
						else if(scheduleList.get(j).getState().equals("16")){state = "<img src=\"images/massmail/sending.gif\" title=\"오류자재발송중\"/>";}
						else if(scheduleList.get(j).getState().equals("22")){state = "<img src=\"images/massmail/error.gif\" title=\"발송준비중에러\"/>";}
						else if(scheduleList.get(j).getState().equals("24")){state = "<img src=\"images/massmail/error.gif\" title=\"발송중에러\"/>";}
						else if(scheduleList.get(j).getState().equals("26")){state = "<img src=\"images/massmail/error.gif\" title=\"오류자재발송중에러\"/>";}
						else if(scheduleList.get(j).getState().equals("32")){state = "<img src=\"images/massmail/stop.gif\" title=\"발송준비중정지\"/>";}
						else if(scheduleList.get(j).getState().equals("34")){state = "<img src=\"images/massmail/pause.gif\" title=\"발송일시정지\"/>";}
						else if(scheduleList.get(j).getState().equals("44")){state = "<img src=\"images/massmail/stop.gif\" title=\"발송정지\"/>";}
						*/
						if(scheduleList.get(j).getSendType()==1){state = state + "<img src=\"images/s_mail.gif\" title=\"즉시발송\"/>";}
						else if(scheduleList.get(j).getSendType()==2){state = state + "<img src=\"images/s_mail_reserve.gif\" title=\"예약발송\"/>";}
						else if(scheduleList.get(j).getSendType()==3){state = state + "<img src=\"images/s_reload_2.gif\" title=\"반복발송\"/>";}
						
						if(size_mail <5)
						{
								
							if(scheduleList.get(j).getUserID().equals(LoginInfo.getUserID(request)))
							{//내 캠페인							
								cell_html.append("<div class='my' title='");
								cell_html.append(scheduleList.get(j).getMassMailTitle() + "(" + scheduleList.get(j).getSendScheduleDate().substring(11,16)+" 발송"+scheduleList.get(j).getTargetTotalCount()+"통)'>");
								cell_html.append("<a href=\"javascript:$('"+id+"').viewWindow("+ scheduleList.get(j).getMassMailID()+","+ scheduleList.get(j).getScheduleID() +")\">");
								cell_html.append(state +"<font color='#ffffff'> " + title + "</font>" ); 
								cell_html.append("</a></div> ");
							
							}
							else if(scheduleList.get(j).getGroupID() == Integer.parseInt(LoginInfo.getGroupID(request)))
							{//내 그룹 캠페인
								cell_html.append("<div class='mygroup' title='");
								cell_html.append(scheduleList.get(j).getMassMailTitle() + "(" + scheduleList.get(j).getSendScheduleDate().substring(11,16)+" 발송"+scheduleList.get(j).getTargetTotalCount()+"통)'>");
								cell_html.append("<a href=\"javascript:$('"+id+"').viewWindow("+ scheduleList.get(j).getMassMailID()+","+ scheduleList.get(j).getScheduleID() +")\">");
								cell_html.append(state +"<font color='#ffffff'> " + title + "</font>" ); 
								cell_html.append("</a></div> ");
							}
							else 
							{//다른그룹 캠페인
								cell_html.append("<div class='outer' title='");
								cell_html.append(scheduleList.get(j).getMassMailTitle() + "(" + scheduleList.get(j).getSendScheduleDate().substring(11,16)+" 발송"+scheduleList.get(j).getTargetTotalCount()+"통)'>");
								cell_html.append("<a href=\"javascript:$('"+id+"').viewWindow("+ scheduleList.get(j).getMassMailID()+","+ scheduleList.get(j).getScheduleID() +")\">");
								cell_html.append(state +"<font color='#ffffff'> " + title + "</font>" ); 
								cell_html.append("</a></div> ");
							}
						}
						else
						{
							extra_mail++;
							
							if(scheduleList.get(j).getUserID().equals(LoginInfo.getUserID(request)))
							{							
								table_html.append("<div class='my' title='");
								table_html.append(scheduleList.get(j).getMassMailTitle() + "(" + scheduleList.get(j).getSendScheduleDate().substring(11,16)+" 발송"+scheduleList.get(j).getTargetTotalCount()+"통)'>");
								table_html.append("<a href=\"javascript:$('"+id+"').viewWindow("+ scheduleList.get(j).getMassMailID()+","+ scheduleList.get(j).getScheduleID() +")\">");
								table_html.append(state +"<font color='#ffffff'> " + title + "</font>" ); 
								table_html.append("</a></div> ");
							
							}
							else if(scheduleList.get(j).getGroupID() == Integer.parseInt(LoginInfo.getGroupID(request)))
							{
								table_html.append("<div class='mygroup' title='");
								table_html.append(scheduleList.get(j).getMassMailTitle() + "(" + scheduleList.get(j).getSendScheduleDate().substring(11,16)+" 발송"+scheduleList.get(j).getTargetTotalCount()+"통)'>");
								table_html.append("<a href=\"javascript:$('"+id+"').viewWindow("+ scheduleList.get(j).getMassMailID()+","+ scheduleList.get(j).getScheduleID() +")\">");
								table_html.append(state +"<font color='#ffffff'>  " + title + "</font>" ); 
								table_html.append("</a></div> ");
							}
							else 
							{
								table_html.append("<div class='outer' title='");
								table_html.append(scheduleList.get(j).getMassMailTitle() + "(" + scheduleList.get(j).getSendScheduleDate().substring(11,16)+" 발송"+scheduleList.get(j).getTargetTotalCount()+"통)'>");
								table_html.append("<a href=\"javascript:$('"+id+"').viewWindow("+ scheduleList.get(j).getMassMailID()+","+ scheduleList.get(j).getScheduleID() +")\">");
								table_html.append(state +"<font color='#ffffff'> " + title + "</font>" ); 
								table_html.append("</a></div> ");
							}
							
						}
					}
				}
				
				
				// 일요일
				if (((indent + i) - 1) % 7 == 1)
				{
					// 일요일에 <tr>
					out.println("<tr>");
					
				}
				// 토요일
				else if (((indent + i) - 1) % 7 == 0)
				{
					
				}

				// 현재 날짜 일 경우에는 바탕색깔을 줌.#FFFAE8
				if (today.equals(year + month + day))
					out.println("<td id='day_" + i + "' class='today' onmouseover=\"this.style.backgroundColor='#F1F1F1'\" onmouseout=\"this.style.backgroundColor='#F2EAEA'\" >");
				// 현재 날짜가 아닐 경우
				else
					out.println("<td id='day_" + i + "' onmouseover=\"this.style.backgroundColor='#F1F1F1'\" onmouseout=\"this.style.backgroundColor='#FAFAFA'\">");

				out.println("<div>");
				
				if(LoginInfo.getAuth_write_mail(request).equals("Y"))
				{
										
					if(DateUtils.daysBetween(today, year+month+day, "yyyyMMdd") >= 0)
						out.println("<a href=\"javascript:$('"+ id+"').viewWrite('"+ year+"-"+month+"-"+day+"')\" title='"+i+"일 메일 작성'> <strong>");
					if (((indent + i) - 1) % 7 == 1)
					{						
						out.println("<font color='#f65656'>"+ i+"</font>" + "</a>" );
					}
					else
					{						
						out.println("<font color='#6db6db'>"+ i+"</font>" + "</a>" );
					}
					if(DateUtils.daysBetween(today, year+month+day, "yyyyMMdd") >= 0)
						out.println("</strong>");
				}
				else
				{
					if (((indent + i) - 1) % 7 == 1)
						out.println("<font color='#f65656'>"+ i+"</font>" );
					else
						out.println("<font color='#6db6db'>"+ i+"</font>" );
				}
				if(extra_mail>0)
				{
					
					out.println("<a href=\"javascript:$('"+ id+"').show('"+ i+"')\" >");
					out.println( "(+"+extra_mail+")" + "</a>" );
				
				}
				out.println( "</div>" );
				
				
				
				out.println(cell_html.toString());
				if(extra_mail>0)
					out.println("<span ID='"+ id+"overDiv_" + i +"' style='display:none;'>"+table_html.toString()+"</span >");
				
				
				out.println("</td>");
				// 토요일에 </tr>
				if (((indent + i) - 1) % 7 == 0) out.println("</tr>");
				
				
			}
			// 현재 날짜가 금요일 이고 31일 까지 있거나
			// 현재 날짜가 토요일 이고 30일 까지 있을 때 한줄 더 출력 한다.
			if ( ( (indent == 6) && (numberOfDays > 30) ) || ( (indent == 7) && (numberOfDays > 29) ) )
			{
				for (int i = 42 - numberOfDays - indent; i > 0; i--)
				{
					out.println("<td bgcolor='#ffffff'>&nbsp;</td>");
				}
				out.println("</tr>");
			}
			// 현재 날짜가 28일 이 아니거나 1일이 일요일이 아닐 경우
			// 끝 공백을 채워준다.
			else if ( (numberOfDays != 28) || (indent > 1) )
			{
				if (36-numberOfDays-indent > 0)
				{
					for (int i = 36 - numberOfDays - indent; i > 0; i--)
					{
						out.println("<td bgcolor='#ffffff'>&nbsp;</td>");
					}
					out.println("</tr>");
				}
			}
			
			
			%>
			
			</tbody>
			</table>
		</div>
	
				
		
					
<%
}
//****************************************************************************************************/
//  편집 
//****************************************************************************************************/
if(method.equals("view")) {
%>

	<div style="margin-bottom:10px;width:100%">
		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
			<%ScheduleCalendar s = (ScheduleCalendar)request.getAttribute("schedule");
			
			%>
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="80px">대량메일명</td>
				<td class="ctbl td"><%=s.getMassMailTitle()%></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="80px">설명</td>
				<td class="ctbl td"><%=s.getDescription()%></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>				
				<td class="ctbl ttd1" width="100px">작성자</td>
				<td class="ctbl td"><%=s.getUserName()%></td>
				
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>
				<td class="ctbl ttd1" width="100px">예상발송통수</td>
				<td class="ctbl td"><%=s.getTargetTotalCount()%> 통</td>
			</tr>			
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1">발송일자</td>
				<td class="ctbl td"><%=s.getSendScheduleDate()%></td>
   			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1">발송타입</td>
				<%if(s.getSendType()==1){%>
				
					<td class="ctbl td">즉시발송</td>
				<%}else if(s.getSendType()==2){ %>
				
					<td class="ctbl td">예약발송</td>
				<%}else if(s.getSendType()==3){ %>
				
					<td class="ctbl td">반복발송</td>
			<%} %>
				
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1">상태</td>
				<td class="ctbl td"><%=ThunderUtil.descState(s.getState())%></td>
			</tr>		
			
			</tbody>
			</table>
		</form>
	</div>
	

	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	
	<%
	
	if( LoginInfo.getIsAdmin(request).equals("Y") ){%>
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').viewList('<%=s.getSendScheduleDate().substring(0,10)%>')"  class="web20button bigpink">리스트 보기</a></div>
	<%}
	
	
	else if(LoginInfo.getAuth_sub_menu(request).indexOf("21")>-1 && 
				(	s.getUserID().equals(LoginInfo.getUserID(request)) || 
					(s.getGroupID()==Integer.parseInt(LoginInfo.getGroupID(request)) && s.getStatisticsOpenType()==2) || 
					s.getStatisticsOpenType()==3
				) 
			)
	{
	%>
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').viewList('<%=s.getSendScheduleDate().substring(0,10)%>')"  class="web20button bigpink">리스트 보기</a></div>
	<%		
	}
	if(s.getSendType()==3 && ( LoginInfo.getAuth_sub_menu(request).indexOf("22")>-1 || LoginInfo.getIsAdmin(request).equals("Y")  )){%>
	
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').viewListRepeat('<%=s.getSendScheduleDate().substring(0,10)%>')"  class="web20button bigpink">반복메일 보기</a></div>

	<%}%>
<%
}
%>