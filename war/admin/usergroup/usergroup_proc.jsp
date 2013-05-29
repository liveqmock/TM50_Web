<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="web.admin.usergroup.model.User"%>
<%@ page import="web.admin.usergroup.model.Group"%>
<%@ page import="web.admin.usergroup.service.UserGroupService"%>
<%@ page import="web.admin.usergroup.control.UserGroupControlHelper"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="web.admin.systemset.control.SystemSetControllerHelper"%>
<%@ page import="web.admin.systemset.service.SystemSetService"%>
<%@ page import="web.common.util.*"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%

String id = request.getParameter("id");
String sMethod = request.getParameter("method");

String isAdmin = LoginInfo.getIsAdmin(request);

if(isAdmin.equals("Y")){ // 관리자 계정이 아닐 경우 URL 접근 시 접근불가 페이지 출력

//****************************************************************************************************/
//사용자 추가/수정 윈도우 
//****************************************************************************************************/
if(sMethod.equals("edit_user")) {
	
	String groupChange = request.getParameter("groupChange"); // 그룹을 바꿀 수 있는지 여부
	// 그룹을 가져온다
	UserGroupService service = UserGroupControlHelper.getUserService(application);
	List<Group> groupList = service.listGroup();
	String defGroupID = request.getParameter("defGroupID");
	
	SystemSetService systemSetservice = SystemSetControllerHelper.getUserService(application);
	int loginFialCheckCount = Integer.parseInt(systemSetservice.getSystemSetInfo("1","loginFialCheckCount"));

%>

	<div style="margin-bottom:10px;width:100%">
		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">

		<table class="ctbl" width="100%" style="border:none">
		<tbody>
		<tr>
			<td colspan="10" class="ctbl line"></td> 
		</tr>
		
		<tr>
			<td class="ctbl ttd1 mustinput" width="100px">사용자명</td>
			<td class="ctbl td"><input type="text" id="eUserName" name="eUserName" value="<c:out value="${user.userName}"/>" mustInput="Y" msg="사용자명 입력"  maxlength="30"/></td>
		</tr>				
		
		<c:if test="${user.userID == ''}" > <!-- 추가이면  -->
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1 mustinput" width="100px">사용자아이디</td>
				<td class="ctbl td">
					<input type="text" id="eUserID" name="eUserID" value="" mustInput="Y" msg="사용자아이디 입력" maxlength="20"/>
					
				</td>
				<input type="hidden" id="eEditMode" name="eEditMode" value="insert" />
				
			</tr>
		</c:if>

		<c:if test="${user.userID != ''}" > <!-- 수정이면  -->
			<input type="hidden" id="eUserID" name="eUserID" value="<c:out value="${user.userID}"/>" />
			<input type="hidden" id="eEditMode" name="eEditMode" value="update" />
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1 mustinput" width="100px">사용자아이디</td>
				<td class="ctbl td"><c:out value="${user.userID}"/></td>
			</tr>
		</c:if>
						
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd1 mustinput" width="100px">비밀번호</td>
			<td class="ctbl td"><input type="password" id="eUserPWD" name="eUserPWD" value=""  mustInput="Y" msg="비밀번호 입력"  maxlength="20"/></td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd1 mustinput" width="100px">비밀번호확인</td>
			<td class="ctbl td"><input type="password" id="eUserPWD2" name="eUserPWD2" value=""  mustInput="Y" msg="비밀번호 입력"  maxlength="20"/></td>
		</tr>				
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
		<%if(groupChange.equals("false")) { %>
			<td class="ctbl ttd2 mustinput">소속그룹</td>
			<td class="ctbl td"><c:out value="${user.groupName}"/></td>
			<input type="hidden" name="eGroupID" id="eGroupID" value="<c:out value="${user.groupID}"/>"/>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd2 mustinput">계정권한</td>
			<td class="ctbl td"><c:out value="${user.levelName}"/></td>
			<input type="hidden" name="eUserLevel" id="eUserLevel" value="<c:out value="${user.userLevel}"/>"/>
		
		<%} else {%>
			<td class="ctbl ttd2 mustinput">소속그룹</td>
			<td class="ctbl td">
				<ul id="eGroupID" class="selectBox" mustInput="Y" msg="소속그룹 선택" onchange="$('<%=id%>').changeGroup()">
					<li data="">그룹선택</li>
					<%
					for(Group group: groupList) {
					%>
						<c:set var="currentGroupID" value="<%=group.getGroupID()%>" />
						<c:if test="${user.userID != ''}" > <!-- 수정이면  -->
							<li data="<%=group.getGroupID()%>" <c:if test="${user.groupID == currentGroupID}">select="yes"</c:if>><%=group.getGroupName()%></li>
						</c:if>
					
						<c:if test="${user.userID == ''}" > <!-- 추가이면  -->
							<li data="<%=group.getGroupID()%>" <%=(group.getGroupID().equals(defGroupID)?"select=yes":"")%>><%=group.getGroupName()%></li>
						</c:if>
					
					<%
					}
					%>
				</ul>
				
			</td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd2 mustinput">계정권한</td>
			<td class="ctbl td">
				<ul id="eUserLevel"  class="selectBox"  mustInput="Y" msg="계정권한 선택" onchange="$('<%=id%>').changeGroup()">
					<li data="">계정권한선택</li>
					<li data="1" <c:if test="${user.userLevel == '1'}" >select="yes"</c:if>>수퍼관리자</li>
					<li data="2" <c:if test="${user.userLevel == '2'}" >select="yes"</c:if>>그룹관리자</li>
					<li data="3" <c:if test="${user.userLevel == '3'}" >select="yes"</c:if>>일반사용자</li>
				</ul>
			</td>
		<%}%>
			
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<!-- 
		<tr>
			<td class="ctbl ttd2">담당자</td>
			<td class="ctbl td">
				<ul id="eIsHelper"  class="selectBox"  mustInput="Y" msg="담당자 설정" >
					<li data="Y" <c:if test="${user.isHelper == 'Y'}" >select="yes"</c:if>>설정함</li>
					<li data="N" <c:if test="${user.isHelper == 'N'}" >select="yes"</c:if>>설정안함</li>					
				</ul>
			</td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		 -->
		<tr>
			<td class="ctbl ttd2">e-mail</td>
			<td class="ctbl td"><input type="text" id="eEmail" name="eEmail" size="40" value="<c:out value="${user.email}"/>"/></td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd2">휴대폰</td>
			<td class="ctbl td"><input type="text" id="eCellPhone" name="eCellPhone" size="40" 
				<c:if test="${user.cellPhone==''}">value="휴대폰번호는 '-'를 포함합니다" onclick="javascript:$('<%=id%>').deleteValue(this)"</c:if>
				<c:if test="${user.cellPhone!=''}">value="<c:out value="${user.cellPhone}"/>"</c:if>
			/></td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd2" width="100px">보내는 사람명</td>
			<td class="ctbl td"><input type="text" id="eSenderName" name="eSenderName" size="40" value="<c:out value="${user.senderName}"/>"   maxlength="30"/></td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>	
		<tr>
			<td class="ctbl ttd2">보내는 e-mail</td>
			<td class="ctbl td"><input type="text" id="eSenderEmail" name="eSenderEmail" size="40" value="<c:out value="${user.senderEmail}"/>"/></td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd2">보내는 휴대폰 </td>
			<td class="ctbl td"><input type="text" id="eSenderCellPhone" name="eSenderCellPhone" size="40"
				<c:if test="${user.senderCellPhone==''}">value="휴대폰번호는 '-'를 포함합니다" onclick="javascript:$('<%=id%>').deleteValue(this)"</c:if>
				<c:if test="${user.senderCellPhone!=''}">value="<c:out value="${user.senderCellPhone}"/>"</c:if>
			/></td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd2">비고</td>
			<td class="ctbl td"><input type="text" id="eDescription" name="eDescription" size="65" value="<c:out value="${user.description}"/>"/></td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<c:set var="setLoginFailCount" value="<%=loginFialCheckCount%>" />
		<c:if test="${user.userID != '' && setLoginFailCount > 0 && user.failCount > setLoginFailCount}" >
			<tr id="<%=id %>_derestrict">
				<td class="ctbl ttd2" style="color:red">사용 불가 아이디</td>
				<td class="ctbl td">
					<div style="float:left">비밀 번호 입력 오류 횟수 : <c:out value="${user.failCount}"/>회 (제한 <%=loginFialCheckCount %>회)</div>
					<div style="float:right" class="btn_r"><a href="javascript:$('<%=id%>').derestrict()" style ="cursor:pointer"><span>제한해제</span></a></div> 
				</td>
			</tr>
			<tr id="<%=id %>_derestrictLine">
				<td colspan="10" class="ctbl line"></td>
			</tr>
		</c:if>
		</tbody>
		</table>
		</form>
	</div>
	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">취 소</a></div>
	
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveUserData('Y')" class="web20button bigblue">저 장 후 추가</a></div>
	
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveUserData('N')" class="web20button bigblue">저 장 후 닫기</a></div>
	<script type="text/javascript">
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));
		
		<c:if test="${user.userID == ''}" > 
			if($('<%=id%>_form').eGroupID.value != '100')  // 수퍼관리자가 아니면 일반계정으로 초기화 
				$('<%=id%>_form').eUserLevel.setSelect(3);
		</c:if>
		
	</script>

<% }

//****************************************************************************************************/
//그룹 추가/수정 윈도우 
//****************************************************************************************************/
if(sMethod.equals("edit_group")) {
%>

	<div style="margin-bottom:10px;width:100%">
		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">

			<input type="hidden" id="eGroupID" name="eGroupID" value="<c:out value="${group.groupID}"/>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="80px">그룹명</td>
				<td class="ctbl td"><input type="text" id="eGroupName" name="eGroupName" value="<c:out value="${group.groupName}"/>" size="40" mustInput="Y" msg="그룹명  입력"/></td>
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd2">설명</td>
				<td class="ctbl td">
					<textarea id="eDescription" name="eDescription" style="width:300px;height:80px"  /><c:out value="${group.description}"/></textarea>
				</td>
			</tr>
			</tbody>
			</table>
		</form>
	</div>
	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveGroupData()" class="web20button bigblue">저 장</a></div>

<% }
//****************************************************************************************************/
//사용자 추가 후 action 
//****************************************************************************************************/
if(sMethod.equals("insert_user")) {
	String saveAndAppend = request.getParameter("saveAndAppend");

%>
	<script type="text/javascript">

		// 그룹 노드를 검색
		var groupNode = findGroupNode(<%=id%>_tree, '<c:out value="${user.groupID}"/>');
		groupNode.toggle('true');

		<c:if test="${user.userLevel == '1'}" >
			var userType = 'super_user';
			var hasCheckBox = false;
		</c:if>
			
		<c:if test="${user.userLevel == '2'}" >
			var userType = 'group_user';
			var hasCheckBox = true;
		</c:if>

		<c:if test="${user.userLevel == '3'}" >
			var userType = 'user';
			var hasCheckBox = true;
		</c:if>
		
		// 노드를 추가한다. (추가된 노드는 자동으로 선택된다)
		<%=id%>_tree.add({
			'property': {
				'name': '<c:out value="${user.userName}"/>',
				'hasCheckBox': hasCheckBox
			},
			'type': userType,
			'data': {
				'id': '<c:out value="${user.userID}"/>',
				'mode': 'user',
				'isAdmin': false
			}
		}, groupNode , 'inside');
		var node = <%=id%>_tree.getSelected();
		node.enableCheckBox( hasCheckBox );
		closeWindow( $('<%=id%>_modal') );
				
		
		<%if(saveAndAppend.equals("Y")) { %>
			$('<%=id%>').editUserWindow('insert'); // 저장 후 추가
		<%}%>
		
		
	</script>
	
<% } 	
//****************************************************************************************************/
//사용자 수정 후 action 
//****************************************************************************************************/
if(sMethod.equals("update_user")) {
	String saveAndAppend = request.getParameter("saveAndAppend");

	%>
		<script type="text/javascript">

			// 그룹 노드를 검색
			var groupNode = findGroupNode(<%=id%>_tree, '<c:out value="${user.groupID}"/>');
			var node = <%=id%>_tree.getSelected();
			var json = {};

			//node.removeType( node.type ); // 타입이 바뀌면 삭제
			<c:if test="${user.userLevel == '1'}" >
				var userType = 'super_user';
				var checkBox = false;
			</c:if>
				
			<c:if test="${user.userLevel == '2'}" >
				var userType = 'group_user';
				var checkBox = true;
			</c:if>

			<c:if test="${user.userLevel == '3'}" >
				var userType = 'user';
				var checkBox = true;
			</c:if>
			//node.addType( userType ); // 바뀐타입을 추가

			//node.name = '<c:out value="${user.userName}"/>';
			json = {
					property: {
					name: '<c:out value="${user.userName}"/>',
					hasCheckBox: checkBox
				},
				type: userType,
				data: {
					mode: node.data.mode,
					id: node.data.id,
					isAdmin: node.data.isAdmin
				}
			};

			node.set(json);
			//node.hasCheckBox = checkBox;
			//node['switch']('checked');
			node.enableCheckBox( checkBox );


			// 체크박스 상태

			if(groupNode != node.getParent()) { // 그룹이 바꼈으면
				groupNode.toggle( 'true' );
				//<%=id%>_tree.remove( node );
				
				<%=id%>_tree.move(node,groupNode,'inside');
				//Mif.Tree.Draw.update(node);
				//Mif.Tree.Draw.update(node);
			} else {

				<%=id%>_tree.select(<%=id%>_tree.root);
				<%=id%>_tree.select(node);
				
			}
			Mif.Tree.Draw.update(node);

			
			closeWindow( $('<%=id%>_modal') );
			
			<%if(saveAndAppend.equals("Y")) { %>
				$('<%=id%>').editUserWindow('insert'); // 저장 후 추가
			<%}%>
			
			
		</script>
<% 
}
//****************************************************************************************************/
//사용자 삭제 후 action 
//****************************************************************************************************/
if(sMethod.equals("deleteUser")) {

%>
	<script type="text/javascript">

		var GroupIDs = JSON.decode("<%=JSONObject.escape(request.getParameter("GroupIDs"))%>");
		var IDs = JSON.decode("<%=JSONObject.escape(request.getParameter("IDs"))%>");
		var node;

		for(var i=0; i < GroupIDs.length; i ++ ) {
			var nodeArr = getChildNodes(<%=id%>_tree, GroupIDs[i]);
			
			if(nodeArr != null) {
				node = findNode(nodeArr, IDs[i] );
				if(node) {
					node.remove();
				}
			}
		}

		// 그룹노드의 체크를 뺀다
		for(var i=0; i < GroupIDs.length; i ++ ) {
			node = findGroupNode( <%=id%>_tree, GroupIDs[i]);
			if(node) node['switch']('unchecked');
		}

		// 선택된 노드가  없어지면 그룹을 선택한다
		node = <%=id%>_tree.getSelected();
		if(!node) {
			node = findGroupNode( <%=id%>_tree, GroupIDs[0]);
			if(node) {
				<%=id%>_tree.expandTo( node );
				<%=id%>_tree.scrollTo( node );
				<%=id%>_tree.select(node);
			}
		} 
			

	</script>
	
<% } 				

//****************************************************************************************************/
//사용자 정보 
//****************************************************************************************************/
if(sMethod.equals("view_user")) {
	SystemSetService systemSetservice = SystemSetControllerHelper.getUserService(application);
	int loginFialCheckCount = Integer.parseInt(systemSetservice.getSystemSetInfo("1","loginFialCheckCount"));
%>

		<table class="ctbl" width="100%" style="border:none">
		<tbody>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd1" width="90px">사용자명 / ID</td>
			<td class="ctbl td"><c:out value="${user.userName}"/> / <c:out value="${user.userID}"/></td>
		</tr>				
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd_sm">소속그룹</td>
			<td class="ctbl td"><c:out value="${user.groupName}"/></td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd_sm">계정권한</td>
			<td class="ctbl td">
				<c:out value="${user.levelName}"/>
			</td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd_sm">e-mail</td>
			<td class="ctbl td"><c:out value="${user.email}"/></td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd_sm">휴대폰</td>
			<td class="ctbl td"><c:out value="${user.cellPhone}"/></td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<tr>
			<td class="ctbl ttd_sm">비고</td>
			<td class="ctbl td"><c:out value="${user.description}"/></td>
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		<c:set var="setLoginFailCount" value="<%=loginFialCheckCount%>" />
		<c:if test="${user.userID != '' && setLoginFailCount > 0 && user.failCount > setLoginFailCount}" >
			<tr id="<%=id %>_viewDerestrict">
				<td class="ctbl ttd2" style="color:red">사용 불가 아이디</td>
				<td class="ctbl td">
					비밀 번호 입력 오류 횟수 : <c:out value="${user.failCount}"/>회 (제한 <%=loginFialCheckCount %>회)
				</td>
			</tr>
		</c:if>
		</tbody>
		</table>

<% } 		
//****************************************************************************************************/
//그룹 추가 후 action 
//****************************************************************************************************/
if(sMethod.equals("insert_group")) {

%>
	<script type="text/javascript">

		// 노드를 추가한다. (추가된 노드는 자동으로 선택된다)
		var node = <%=id%>_tree.add({
			property: {
				name: '<c:out value="${group.groupName}"/>'
			},
			type: 'folder',
			data: {
				id: '<c:out value="${group.groupID}"/>',
				mode: 'group',
				isAdmin: false
			}
		}, <%=id%>_tree.root , 'inside');
		closeWindow( $('<%=id%>_modal') );

		//alert('그룹에 권한을 부여 하시면  그룹에 속한 사용자가 추가될 때\n\n그룹의 권한이 사용자에게 복사 됩니다');

		toolTip.showTipAtControl($('<%=id%>_authSaveBtn'),'그룹에 권한을 부여 하시면  그룹에 속한 사용자가 추가될 때 그룹의 권한이 사용자에게 복사 됩니다',100000);
		

	</script>
	
<% } 		
//****************************************************************************************************/
//그룹 수정 후 action 
//****************************************************************************************************/
if(sMethod.equals("update_group")) {

%>
	<script type="text/javascript">
	
		// 노드의 이름을 바꾼다
		var node = <%=id%>_tree.selected;
		if(node != null) {
			node.name = '<c:out value="${group.groupName}"/>';
			Mif.Tree.Draw.update(node);
			
		}
		closeWindow( $('<%=id%>_modal') );
		
	</script>
	
<% } 
//****************************************************************************************************/
//그룹 삭제 후 action 
//****************************************************************************************************/
if(sMethod.equals("deleteGroup")) {

%>
	<script type="text/javascript">

		var IDs = JSON.decode("<%=JSONObject.escape(request.getParameter("IDs"))%>");

		for(var i=0; i < IDs.length; i ++ ) {
			var groupNode = findGroupNode(<%=id%>_tree, IDs[i]);
			groupNode.remove();
		}  

		<%=id%>_tree.root['switch']('unchecked');
		
	</script>
	
<% } %>		


<%}else{%>
<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td align="center" valign="middle">
			<center><img src="../../images/error.jpg" /></center>
		</td>
	</tr>
</table>
<%}%>