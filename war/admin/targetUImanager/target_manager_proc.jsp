<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="web.admin.targetmanager.model.*"%>
<%@ page import="web.admin.targetmanager.service.*"%>
<%@ page import="web.admin.targetmanager.control.*"%>
<%@ page import="java.util.*"%>  
<%@ page import="web.common.util.*" %>
<%@ page import="web.admin.dbjdbcset.model.DbJdbcSet"%> 

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<%

String id = request.getParameter("id");
String method = request.getParameter("method");



if(method.equals("list")) {
%>

	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	<!-- 메시지 가있으면 메시지를 뿌려준다 -->
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	<c:forEach items="${targetmanagerlist}" var="targetmanagerlist">
		
		<TR class="tbl_tr" targetUIManagerID="<c:out value="${targetmanagerlist.targetUIManagerID}"/>"  >
			<TD class="tbl_td" align='center'><input type="checkbox" class="notBorder" id="eTargetUIManagerID" name="eTargetUIManagerID" value="<c:out value="${targetmanagerlist.targetUIManagerID}" />" /></TD>
 			<TD class="tbl_td"><b><c:out value="${targetmanagerlist.targetUIManagerID}"/></b></TD>			
			<TD class="tbl_td" align="left"><a href="javascript:$('<%=id%>').editWindow('<c:out value="${targetmanagerlist.targetUIManagerID}"/>')"><c:out value="${targetmanagerlist.targetUIManagerName}"/></a></TD>	
			<TD class="tbl_td"><c:out value="${targetmanagerlist.useYN}"/></TD>	
			<TD class="tbl_td"><c:out value="${targetmanagerlist.defaultYN}"/></TD>		
			
		</TR>
		
	</c:forEach>
	
	<script type="text/javascript">

		window.addEvent('domready', function(){


			// 페이징 표시
			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>'  // busy 를 표시할 window
				,url: 'pages/common/pagingStr.jsp'
				,update: $('<%=id%>_paging') // 완료후 content가 랜더링될 element
			});
			nemoRequest.get({
				'id':'<%=id%>', 
				'curPage': '<%=curPage%>', 
				'iLineCnt': '<%=iLineCnt%>', 
				'iTotalCnt': '<%=iTotalCnt%>', 
				'iPageCnt': '10' 
			});
			
		
			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
				,popup: $('<%=id%>').popup // 마우스 클릭시 사용할 팝업메뉴
			});
			$('<%=id%>').grid_content.render();
			
		});

	</script>




<%
}
//****************************************************************************************************/
//편집 
//****************************************************************************************************/
if(method.equals("edit")) {
	String targetUIManagerID = ServletUtil.getParamStringDefault(request, "targetUIManagerID","0");
	String userID = LoginInfo.getUserID(request);
	
	TargetManagerService service = TargetManagerControlHelper.getUserService(application);
	
	List<DbJdbcSet> dbJdbcSetList = null;
	dbJdbcSetList = service.getDBList(); 
	
	List<OneToOne> ontooneList = service.listOneToOne();
	List<TargetUIManagerSelect> targetUIManagerSelectList = null;
	
	List<String> columns = null; 
	columns = new ArrayList();
	
	
%>
	<%@page import="java.util.List"%>
	<jsp:useBean id="targetmanager" class="web.admin.targetmanager.model.TargetUIManager" scope="request" />
	<c:set var="userID" value="<%=userID %>"/>
	<div style="margin-bottom:10px;width:100%">		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
						
			<table class="ctbl" width="100%">
			
			
			<tbody>
				
			<tr>	
				<td class="ctbl ttd1" style="width:270px">회원검색UI명</td>
				<td class="ctbl td" ><input type="text" id="eTargetUIManagerName" name="eTargetUIManagerName" value="<c:out value="${targetmanager.targetUIManagerName}"/>" size="40"  maxlength="50" mustInput="Y" msg="회원검색UI명 입력"/></td>
				
				<td class="ctbl ttd1" style="width:110px">설명</td>
				<td class="ctbl td" ><input type="text" id="eDescription" name="eDescription" value="<c:out value="${targetmanager.description}"/>" size="40"  maxlength="50"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
					 
			<tr>
				<td class="ctbl ttd1" style="width:270px">DB설정</td>
				<td class="ctbl td">
					<div style="float:left">
					<select id="eDbID" name="eDbID">
						<option value="">DB 선택 </option>
						<%for(DbJdbcSet dbJdbcSet : dbJdbcSetList) { %>
						<option value="<%=dbJdbcSet.getDbID()%>" <% if(Integer.parseInt(dbJdbcSet.getDbID())==targetmanager.getDbID() || dbJdbcSet.getDefaultYN().equals("Y") ){ %> selected <%} %>><%=dbJdbcSet.getDbName()%></option>
						<%}%>
					</select>
					</div>
				</td>
			
				<td class="ctbl ttd1" style="width:110px">사용여부</td>
				<td class="ctbl td">
					<ul id="eUseYN"  class="selectBox">
						<li data="Y" <c:if test="${targetmanager.useYN=='Y'}">select='Y'</c:if>>Y</li>
						<li data="N" <c:if test="${targetmanager.useYN=='N'}">select='Y'</c:if>>N</li>
					</ul>
				</td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" style="width:270px">from 구문 입력</td>
				<td class="ctbl td" colspan="3" >
					<div style="float:left">
					<textarea id="eFromText" name="eFromText" style="width:620px;height:30px"  mustInput="Y" msg="from 구문 입력" ><c:out value="${targetmanager.fromText}"/></textarea>
					</div>					
				</td>
				
			</tr>
		
				
			</tbody>
			</table>
			<div class="panel-header">
				<div class="panel-header-ball" >
					<div class="panel-expand icon16" style="width:16px"/></div>
				</div>
				<div class="panel-headerContent">
					<h2 id="panel2_title">select 구문 설정</h2>
				</div>
			</div>
			<TABLE class="ctbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
				<thead>
					<tr>
						
						<td class="ctbl ttd1" style="width:280px">필드명</td>
						<td class="ctbl ttd1" style="width:150px">일대일 치환명</td>
						<td class="ctbl ttd1" >설명</td>
					</tr>
				</thead>
				<tbody id="<%=id%>_setSelect" >
				<%
				
				if(!targetUIManagerID.equals("0")) {
	
					targetUIManagerSelectList = service.getTargetUIManagerSelect( Integer.parseInt(targetUIManagerID) );
					
					
					for(TargetUIManagerSelect targetUIManagerSelect: targetUIManagerSelectList) {
						columns.add( targetUIManagerSelect.getSelectFieldName() );				
					}
					
				
				}
				else
				{
					columns.add("");
				}
				
				
		%>
				
				<c:set var="columns" value="<%=columns%>"/>
				
		<%
	
				int cnt = 0;
	
				// 필수 입력 이메일 아이디를 변수에 저장
				for(OneToOne onetoone: ontooneList) {
					if(onetoone.getOnetooneName().equals("[이메일]")) {
		%>
						<script type="text/javascript">
							$('<%=id%>').ontooneEmailID = '<%=onetoone.getOnetooneID()%>';
						</script>
		
		<%
					}else if(onetoone.getOnetooneName().equals("[휴대폰]")){
		%>
						<script type="text/javascript">
							$('<%=id%>').ontooneHpID = '<%=onetoone.getOnetooneID()%>';
						</script>
			
		<%							
					}
				}
		%>
	
				<c:forEach items="${columns}" var="column" varStatus="list">

				<TR class="tbl_tr" id="eSelect<%=++cnt%>" name="eSelect<%=cnt%>">
				
				<TD class="tbl_td dotted">
					<input type="text" id="eSelectFieldName<%=cnt%>" name="eSelectFieldName<%=cnt%>" value="<c:out value="${column}" escapeXml="true"/>" size="35"><a href="javascript:$('<%=id%>').addSelect(<%=cnt%>)"><c:if test="${list.last}">추가</c:if></a>&nbsp;<a href="javascript:$('<%=id%>').delSelect(<%=cnt%>)" style ="cursor:pointer"><c:if test="${list.last}">제거</c:if></a></TD>
				<TD class="tbl_td dotted" style="width:150px">
				<select id="eOneToOne<%=cnt%>" name="eOneToOne<%=cnt%>" >
				<option value="">선택</option>
			
		<%
					int ontooneID = -1;
			
					if(!targetUIManagerID.equals("0")) {  // 수정이면서 새로운 업로드가 아닐때 셀렉트를 지정한다.
						ontooneID = targetUIManagerSelectList.get(cnt-1).getOnetooneID();
					}
				
					for(OneToOne onetoone: ontooneList) {
		%>
			
				<option value="<%=onetoone.getOnetooneID()%>" <%=((ontooneID==onetoone.getOnetooneID())?"selected":"")%>><%=onetoone.getOnetooneName()%></option>
				
		<%
					}
		%>
				</select>
			
				
				</TD>
				<TD class="tbl_td dotted" style="width:300px">
					<input type="text" id="eSelectDescript<%=cnt%>" name="eSelectDescript<%=cnt%>" value="<%=(!targetUIManagerID.equals("0")?targetUIManagerSelectList.get(cnt-1).getSelectDescription():"")%>" size="48"/>
				</TD>
				
				</TR>	
	
				</c:forEach>
				<input type="hidden" id="eSelectCount" name="eSelectCount" value="<%=cnt %>">
				
				<script type="text/javascript">
				window.addEvent('domready', function(){

				makeSelectBox.render( $('<%=id%>_setSelect') );
				});

	
				function settingDescript(){
		
				var index = $('<%=id%>_form').eQueryOneToOne.selectedIndex;
				index = index -1;
				if(index<0){
					$('<%=id%>_form').eDescript.value="";
			
				return;
				}

				$('<%=id%>_form').eDescript.value = $('<%=id%>_form').eFieldDesc[index].value;
		
			
				}
	
				</script>
				</tbody>
			</TABLE>
			
			<div class="panel-header">
				<div class="panel-header-ball" >
					<div class="panel-expand icon16" style="width:16px"/></div>
				</div>
				<div class="panel-headerContent">
					<h2 id="panel2_title">where 조건 설정</h2>
				</div>
			</div>
			<TABLE class="ctbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
				<thead>
					<tr>
						<td class="ctbl ttd1" style="width:220px">컬럼명</td>
						<td class="ctbl ttd1" >조건이름</td>
						<td class="ctbl ttd1" style="width:100px">조건형식</td>
						<td class="ctbl ttd1" style="width:145px">값 입력 1</td>
						<td class="ctbl ttd1" style="width:145px">값 입력 2</td>
					</tr>
				</thead>
				<tbody id="<%=id%>_setWhere" >
				
				<%
				List<TargetUIManagerWhere> targetUIManagerWhereList = null;
				List<String> columnsWhere = null; 
				columnsWhere = new ArrayList();
				
				if(!targetUIManagerID.equals("0")) {
					
					targetUIManagerWhereList = service.getTargetUIManagerWhere( Integer.parseInt(targetUIManagerID) ); 
					
		
					for(TargetUIManagerWhere targetUIManagerWhere: targetUIManagerWhereList) {
						columnsWhere.add(targetUIManagerWhere.getWhereFieldName());	
						
					}
				
				}
				else
				{
					columnsWhere.add( "" );	
				}
				int cntWhere = 0;
				%>
				<c:set var="columnsWhere" value="<%=columnsWhere%>"/>
				<c:forEach items="${columnsWhere}" var="columnWhere" varStatus="wherelist">
				<TR class="tbl_tr" id="eWhere<%=++cntWhere%>" name="eWhere<%=cntWhere%>">
		
		<TD class="ctbl td">
			<input type=text id="eWhereFieldName<%=cntWhere%>" name="eWhereFieldName<%=cntWhere%>" size="20" value="<c:out value="${columnWhere}" escapeXml="true"/>"><a href="javascript:$('<%=id%>').addWhere(<%=cntWhere%>)" style ="cursor:pointer"><c:if test="${wherelist.last}">추가</c:if></a>&nbsp;<a href="javascript:$('<%=id%>').delWhere(<%=cntWhere%>)" style ="cursor:pointer"><c:if test="${wherelist.last}">제거</c:if></a></TD>
		<TD class="ctbl td">
			<input type="text" id="eWhereName<%=cntWhere%>" name="eWhereName<%=cntWhere%>" value="<%=(!targetUIManagerID.equals("0")?targetUIManagerWhereList.get(cntWhere-1).getWhereUIName():"") %>" size="15"/>
		</TD>
		
		<TD class="ctbl td">
			<select id="eWhereType<%=cntWhere%>" name="eWhereType<%=cntWhere%>" onChange="">
				<option value="">선택</option>
				<option value="1" <%=((!targetUIManagerID.equals("0")&&targetUIManagerWhereList.get(cntWhere-1).getWhereType()==1)?"selected":"")%> >in(다중선택)</option>
				<option value="2" <%=((!targetUIManagerID.equals("0")&&targetUIManagerWhereList.get(cntWhere-1).getWhereType()==2)?"selected":"")%> >범위</option>
				<option value="3" <%=((!targetUIManagerID.equals("0")&&targetUIManagerWhereList.get(cntWhere-1).getWhereType()==3)?"selected":"")%> >값 입력 =</option>
				<option value="4" <%=((!targetUIManagerID.equals("0")&&targetUIManagerWhereList.get(cntWhere-1).getWhereType()==4)?"selected":"")%> >값 입력 like</option>
			</select>
		</TD>
		<%if(!targetUIManagerID.equals("0"))
		{	
			if(targetUIManagerWhereList.get(cntWhere-1).getWhereType()==1)
			{
		%>
			<TD class="ctbl td">			
				<input type="text" id="eWhere1Value<%=cntWhere%>" name="eWhere1Value<%=cntWhere%>" value="<%=(!targetUIManagerID.equals("0")?targetUIManagerWhereList.get(cntWhere-1).getCheckName():"") %>" size="20"/>
			</TD>
			<TD class="ctbl td">			
				<input type="text" id="eWhere2Value<%=cntWhere%>" name="eWhere2Value<%=cntWhere%>" value="<%=(!targetUIManagerID.equals("0")?targetUIManagerWhereList.get(cntWhere-1).getCheckValue():"") %>" size="20"/>
			</TD>
		<%	}
			else if(targetUIManagerWhereList.get(cntWhere-1).getWhereType()==2)
			{
		%>
			<TD class="ctbl td">			
				<input type="text" id="eWhere1Value<%=cntWhere%>" name="eWhere1Value<%=cntWhere%>" value="<%=(!targetUIManagerID.equals("0")?targetUIManagerWhereList.get(cntWhere-1).getPeriodStartType():"") %>" size="20"/>
			</TD>
			<TD class="ctbl td">			
				<input type="text" id="eWhere2Value<%=cntWhere%>" name="eWhere2Value<%=cntWhere%>" value="<%=(!targetUIManagerID.equals("0")?targetUIManagerWhereList.get(cntWhere-1).getPeriodEndType():"") %>" size="20"/>
			</TD>
		<% }
			else if(targetUIManagerWhereList.get(cntWhere-1).getWhereType()==3||targetUIManagerWhereList.get(cntWhere-1).getWhereType()==4)
			{
		%>
			<TD class="ctbl td">			
				<input type="text" id="eWhere1Value<%=cntWhere%>" name="eWhere1Value<%=cntWhere%>" value="" size="20"/>
			</TD>
			<TD class="ctbl td">			
				<input type="text" id="eWhere2Value<%=cntWhere%>" name="eWhere2Value<%=cntWhere%>" value="" size="20"/>
			</TD>
		<%
			}
		}else{ %>
			<TD class="ctbl td">			
				<input type="text" id="eWhere1Value<%=cntWhere%>" name="eWhere1Value<%=cntWhere%>" value="" size="20"/>
			</TD>
			<TD class="ctbl td">			
				<input type="text" id="eWhere2Value<%=cntWhere%>" name="eWhere2Value<%=cntWhere%>" value="" size="20"/>
			</TD>
		<%} %>
		
	</TR>
	
	
	</c:forEach>
	<input type="hidden" id="eWhereCount" name="eWhereCount" value="<%=cntWhere %>">
	
	<script type="text/javascript">
		window.addEvent('domready', function(){

		makeSelectBox.render( $('<%=id%>_setWhere') );
		});
	</script>		
		</tbody>
	</TABLE>	
	
	
	<table class="ctbl" width="100%">
			
			
			<tbody>
			<tr>
				<td class="ctbl ttd1">where 구문 <br>입력</td>
				<td class="ctbl td" colspan="3" >
					<div style="float:left">
					대상자 검색UI 에는 표시되지 않으나, 쿼리 조건에는 포함되는 값입니다.<br>
					<textarea id="eWhereText" name="eWhereText" style="width:620px;height:50px"><c:out value="${targetmanager.whereText}"/></textarea>
					</div>					
				</td>
				
			</tr>
		
				
			</tbody>
			</table>	
			
	
	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	
	<c:if test="${targetmanager.targetUIManagerID != 0}" >
		<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${targetmanager.targetUIManagerID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveData(<c:out value="${targetmanager.targetUIManagerID}"/>)" class="web20button bigblue">저 장</a></div>
	</form>
	</div>
	<script language="javascript">
		
		makeSelectBox.render($('<%=id%>_form'));

		
		
	</script>





<%
}

%>