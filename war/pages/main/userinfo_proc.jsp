<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>    
<%@page import="web.common.util.LoginInfo"%>
<%@ page import="web.admin.usergroup.model.User"%>
<%@ page import="web.admin.usergroup.service.UserGroupService"%>
<%@ page import="web.admin.usergroup.control.UserGroupControlHelper"%>
<%@ page import="web.admin.systemset.control.SystemSetControllerHelper"%>
<%@ page import="web.admin.systemset.service.SystemSetService"%>
<%@page import="web.common.util.LoginInfo"%>
<%
	String userID =  LoginInfo.getUserID(request);
	UserGroupService service = UserGroupControlHelper.getUserService(application);
	User user = service.viewUser(userID);
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	
	if(method.equals("basicinfo")){
%>
	<div class="show_wrapper">
		<div>
			<img src="images/trees/user_group.gif"> <b><%=user.getUserName()%>(<%=userID %>)</b>님  안녕하세요!
		</div>
		<div>
			&nbsp; <img src="images/arrow-right2.gif"> 그룹: <%=user.getGroupName() %>
		</div>
		<div>
			&nbsp; <img src="images/arrow-right2.gif"> 권한: <%=user.getLevelName() %>
		</div>
		<div>
			&nbsp; <img src="images/arrow-right2.gif"> E-Mail: <%=StringUtil.head(user.getEmail(),20) %>
		</div>
		<div>
			&nbsp; <img src="images/arrow-right2.gif"> 휴대폰: <%=user.getCellPhone() %>
		</div>
		<div>
			<div class="btn_b" style="float:right"><a id="addFolder" href="javascript:$('<%=id%>').editUserWindow()" style ="cursor:pointer"><span>내 정보수정</span></a></div>
		</div>
	</div>
<% 
}
if(method.equals("editinfo")){
%>
<div style="margin-bottom:10px;width:100%">
	<form id="<%=id%>_form" name="<%=id%>_form" method="post">
	<input type="hidden" id="eUserID" name="eUserID" value="<%=user.getUserID()%>" />
	<input type="hidden" id="eIsHelper" name="eIsHelper" value="<%=user.getIsHelper()%>" />
		<table class="ctbl" width="100%" style="border:none">
		<tbody>
			
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				
				<td class="ctbl ttd1 mustinput" width="90px">사용자아이디</td>
				<td class="ctbl td"><%=user.getUserID()%></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1 mustinput" width="90px">사용자명</td>
				<td class="ctbl td"><input type="text" id="eUserName" name="eUserName" value="<%=user.getUserName()%>" mustInput="Y" msg="사용자명 입력"  maxlength="30"/></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td> 
			</tr>
			<tr>
				<td class="ctbl ttd1 mustinput" width="90px">비밀번호</td>
				<td class="ctbl td"><input type="password" id="eUserPWD" name="eUserPWD" value=""  mustInput="Y" msg="비밀번호 입력"  maxlength="20"/></td>
			</tr>
			<tr>
				<td class="ctbl ttd1 mustinput" width="90px">비밀번호확인</td>
				<td class="ctbl td"><input type="password" id="eUserPWD2" name="eUserPWD2" value=""  mustInput="Y" msg="비밀번호 입력"  maxlength="20"/></td>
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
			<td class="ctbl ttd2 mustinput">소속그룹</td>
			<td class="ctbl td"><%=user.getGroupName()%>
				<input type="hidden" name="eGroupID" id="eGroupID" value="<%=user.getGroupID()%>"/>
			</td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd2 mustinput">계정권한</td>
			<td class="ctbl td"><%=user.getLevelName()%>
				<input type="hidden" name="eUserLevel" id="eUserLevel" value="<%=user.getUserLevel()%>"/>
			</td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd2">e-mail</td>
			<td class="ctbl td"><input type="text" id="eEmail" name="eEmail" size="40" value="<%=user.getEmail()%>"/>
				<a title="<div style='text-align:left'>												
				<img src='images/exclamation.png'>&nbsp; 대량메일의 발송완료 메일 또는<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 발송 승인요청, 반려, 완료 메일이 오는 메일 입니다.<br>
				<div style='color:red'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; * 발송 승인요청, 반려, 완료메일은<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 관리자, 그룹관리자만 해당됩니다.</div>
				</div>">								
				<img src='images/exclamation.png'>
				</a>
			</td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd2">휴대폰</td>
			<td class="ctbl td"><input type="text" id="eCellPhone" name="eCellPhone" size="40" value="<%=user.getCellPhone()%>"/>
				<a title="<div style='text-align:left'>												
				<img src='images/exclamation.png'>&nbsp; SMS 발송완료 문자 또는<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 발송 승인요청, 반려, 완료 문자가 오는 번호 입니다.<br>
				<div style='color:red'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; * SMS연동 되어 있을 경우에만 사용됩니다.</div>
				</div>">								
				<img src='images/exclamation.png'>
				</a>
			</td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd2">보내는이름</td>
			<td class="ctbl td"><input type="text" id="eSenderName" name="eSenderName" size="40" value="<%=user.getSenderName()%>"/>
				<a title="<div style='text-align:left'>												
				<img src='images/exclamation.png'>&nbsp; 대량메일 → 대량메일작성 에서 '보내는 사람명'으로<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 자동입력되는 이름 입니다.<br>
				<div style='color:red'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; * 환경설정 에서 '보내는사람 이름'과 동일하지만<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 우선적용 되기 때문에 계정별로 다르게 설정 가능합니다.</div>
				</div>">								
				<img src='images/exclamation.png'>
				</a>
			</td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd2">보내는 e-mail</td>
			<td class="ctbl td"><input type="text" id="eSenderEmail" name="eSenderEmail" size="40" value="<%=user.getSenderEmail()%>"/>
				<a title="<div style='text-align:left'>												
				<img src='images/exclamation.png'>&nbsp; 대량메일 → 대량메일작성 에서 '보내는 이메일'로<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 자동입력되는 메일주소 입니다.<br>
				<div style='color:red'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; * 환경설정 에서 '회신메일계정'과 동일하지만<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 우선적용 되기 때문에 계정별로 다르게 설정 가능합니다.</div>
				</div>">								
				<img src='images/exclamation.png'>
				</a>
			</td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd2">보내는 휴대폰</td>
			<td class="ctbl td"><input type="text" id="eSenderCellPhone" name="eSenderCellPhone" size="40" value="<%=user.getSenderCellPhone()%>"/>
				<a title="<div style='text-align:left'>												
				<img src='images/exclamation.png'>&nbsp; 대량SMS → 대량SMS작성 에서 '보내는 전화번호'로<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 자동입력되는 번호 입니다.<br>
				<div style='color:red'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; * SMS연동 되어 있을 경우에만 사용됩니다.</div>
				</div>">								
				<img src='images/exclamation.png'>
				</a>
			</td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd2">비고</td>
			<td class="ctbl td"><input type="text" id="eDescription" name="eDescription" size="65" value="<%=user.getDescription()%>"/></td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		
		</tbody>
		</table>
	</form>
</div>
<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">취 소</a></div>
<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveUserData()" class="web20button bigblue">저 장 </a></div>

<% 
}
if(method.equals("pwdEdit")){
	SystemSetService systemSetservice = SystemSetControllerHelper.getUserService(application);
	String pwdCheckDate = systemSetservice.getSystemSetInfo("1","pwdCheckDate");
	LoginInfo.setPWDChangeNoticeYN(request,"N");
%>

<div style="margin-bottom:10px;width:98%">
	<form id="<%=id%>_form" name="<%=id%>_form" method="post">
	<div style="margin-bottom:10px;width:98%">
<img src="images/tag_blue.png" alt="Tips "> 회원님은 <strong><%=pwdCheckDate %>개월간</strong> 비밀번호를 변경하지 않아 <strong>비밀번호 변경 안내 대상</strong>입니다.<br/>
<img src="images/tag_blue.png" alt="Tips "> 개인정보를 안전하게 보호하고, 개인정보도용으로 인한 피해를 예방하기 위해<br/> 
											&nbsp; &nbsp; &nbsp;&nbsp;<strong>비밀번호 변경을요청</strong>드립니다.<br/>
<img src="images/tag_blue.png" alt="Tips "> 비밀번호 변경을 원하지 않을 경우 <strong>'다음에 변경하기'</strong> 버튼을 눌러 <strong><%=pwdCheckDate %>개월 동안</strong><br/>
 											&nbsp; &nbsp; &nbsp;&nbsp;안내를 받지 않을 수 있습니다											
</div>
	<input type="hidden" id="eUserID" name="eUserID" value="<%=user.getUserID()%>" />
	
	<input type="hidden" id="eUserName" name="eUserName" value="<%=user.getUserName()%>" />
	<input type="hidden" id="eCheckUserPWD" name="eCheckUserPWD" value="<%=user.getUserPWD()%>" />
	<input type="hidden" id="eGroupID" name="eGroupID" value="<%=user.getGroupID()%>" />
	<input type="hidden" id="eUserLevel" name="eUserLevel" value="<%=user.getUserLevel()%>" />
	<input type="hidden" id="eEmail" name="eEmail" value="<%=user.getEmail()%>" />
	<input type="hidden" id="eCellPhone" name="eCellPhone" value="<%=user.getCellPhone()%>" />
	<input type="hidden" id="eSenderEmail" name="eSenderEmail" value="<%=user.getSenderEmail()%>" />
	<input type="hidden" id="eSenderName" name="eSenderName" value="<%=user.getSenderName()%>" />
	<input type="hidden" id="eSenderCellPhone" name="eSenderCellPhone" value="<%=user.getSenderCellPhone()%>" />
	<input type="hidden" id="eDescription" name="eDescription" value="<%=user.getDescription()%>" />
	<input type="hidden" id="eIsHelper" name="eIsHelper" value="<%=user.getIsHelper()%>" />
	<table class="ctbl" width="100%" style="border:none">
		<tbody>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1 mustinput" width="90px">현재 비밀번호</td>
				<td class="ctbl td"><input type="password" id="eOldUserPWD" name="eOldUserPWD" size="60"  mustInput="Y" msg="비밀번호 입력"  maxlength="20"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1 mustinput" width="90px">새로운 비밀번호</td>
				<td class="ctbl td"><input type="password" id="eUserPWD" name="eUserPWD" size="60"  mustInput="Y" msg="새로운 비밀번호 입력"  maxlength="20"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1 mustinput" width="90px">비밀번호확인</td>
				<td class="ctbl td"><input type="password" id="eUserPWD2" name="eUserPWD2"size="60" mustInput="Y" msg="비밀번호확인 입력"  maxlength="20"/></td>
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
		</tbody>
	</table>
	</form>
</div>
<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫기</a></div>
<div style="float:right;padding-right:5px" ><a href="javascript:$('userpanel').savePassword('next')" class="web20button bigblue">다음에 변경하기 </a></div>
<div style="float:right;padding-right:5px" ><a href="javascript:$('<%=id%>').savePassword()" class="web20button bigblue">변  경 </a></div>


<%}%>