<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="web.common.util.JSUtil"%>
<%@ page import="web.common.util.Constant"%>
<%@ page import="java.util.List"%>
<%@ page import="web.target.targetui.control.TargetUIController"%>
<%@ page import="web.target.targetui.control.TargetUIControlHelper"%>
<%@ page import="web.target.targetui.service.TargetUIService"%>
<%@ page import="web.target.targetui.model.TargetUIWhere"%>
<%@ page import="web.target.targetui.model.TargetList"%>
<%@ page import="web.target.targetui.model.TargetUIGeneralInfo"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%

String id = request.getParameter("id");
String method = request.getParameter("method");

//****************************************************************************************************/
//편집 
//****************************************************************************************************/
if(method.equals("edit")) {
	
	
	TargetUIService service = TargetUIControlHelper.getUserService(application);
	String targetID = request.getParameter("targetID")==null?"0":request.getParameter("targetID");
	String targetUIManagerID = request.getParameter("targetUIManagerID");
	List<TargetUIWhere> targetUIWhereList = service.getMakeWhere(Integer.parseInt(targetUIManagerID));
	TargetList targetList = new TargetList();
	List<TargetUIGeneralInfo> targetUIGeneralInfos = null;
	if(!targetID.equals("0"))
	{
		targetList = service.viewTargetList(Integer.parseInt(targetID));
		targetUIGeneralInfos = service.viewTargetUIGeneralInfo(Integer.parseInt(targetID), Integer.parseInt(targetUIManagerID));
	}
	else
	{
		targetList.setTargetName("");
		targetList.setDescription("");
		targetList.setTargetGroupID(1);
		targetList.setShareType("1");
		
	}
	StringBuffer tags = new StringBuffer();
	
	if(targetUIWhereList!=null && targetUIWhereList.size() != 0)
	{
		for(TargetUIWhere targetUIWhere:targetUIWhereList)
		{	
			int whereID = targetUIWhere.getWhereID();
			String whereUIName = targetUIWhere.getWhereUIName();
			String whereFieldName = targetUIWhere.getWhereFieldName();
			int whereType = targetUIWhere.getWhereType();
			tags.append("<tr id='eWhereID_"+whereID+"' name='eWhereID_"+whereID+"' >");
			tags.append("<input type='hidden' id='eWhereIDs' name='eWhereIDs' value='"+whereID+"'/>");
			tags.append("<input type='hidden' id='eWhereTypes' name='eWhereTypes' value='"+targetUIWhere.getWhereType()+"'/>");
			tags.append("<input type='hidden' id='eExceptYNs' name='eExceptYNs' value='"+targetUIWhere.getExceptYN()+"'/>");
			tags.append("<input type='hidden' id='eWhereFieldNames' name='eWhereFieldNames' value='"+targetUIWhere.getWhereFieldName()+"'/>");
			tags.append("<input type='hidden' id='eDataTypes' name='eDataTypes' value='"+targetUIWhere.getDataType()+"'/>");
			tags.append("<td class='ctbl ttd1' width='100px'>"+whereUIName+"</td>");
			tags.append("<td class='ctbl td'>");
			
			if(whereType==Constant.TARGET_UI_WHERETYPE_CHECK){
				
				String[] checkName = targetUIWhere.getCheckName().split(";");
				String[] checkValue = targetUIWhere.getCheckValue().split(";");
				String[] checkItems = null;
				if(!targetID.equals("0")){
					for(TargetUIGeneralInfo targetUIGeneralInfo:targetUIGeneralInfos)
					{
						if(targetUIGeneralInfo.getWhereID()==whereID)
						{
							checkItems = targetUIGeneralInfo.getCheckedItems().split(";");
						}
					}
				}
				for(int i =0; i < checkName.length; i++)
				{
					if(i>=5&&i%5==0)
						tags.append("<br>");
					if(!targetID.equals("0"))
						tags.append("<input type='checkbox' class='notBorder' id='eCheckValue_"+whereID+"' name='eCheckValue_"+whereID+"' value='"+checkValue[i]+"' "+ JSUtil.isForChecked(checkValue[i],checkItems)+" />"+checkName[i]+"&nbsp;&nbsp;");
					else
						tags.append("<input type='checkbox' class='notBorder' id='eCheckValue_"+whereID+"' name='eCheckValue_"+whereID+"' value='"+checkValue[i]+"'/>"+checkName[i]+"&nbsp;&nbsp;");
				}
				
			}else if(whereType==Constant.TARGET_UI_WHERETYPE_PERIOD){
				String periodStartValue = "";
				String periodEndValue = "";
				if(!targetID.equals("0")){
					for(TargetUIGeneralInfo targetUIGeneralInfo:targetUIGeneralInfos)
					{
						if(targetUIGeneralInfo.getWhereID()==whereID)
						{
							periodStartValue = targetUIGeneralInfo.getPeriodStartValue();
							periodEndValue = targetUIGeneralInfo.getPeriodEndValue();
						}
					}
				}
				String periodStartType = targetUIWhere.getPeriodStartType();
				String periodEndType = targetUIWhere.getPeriodEndType();
				tags.append("<input type='hidden' id='ePeriodStartType_"+whereID+"' name='ePeriodStartType_"+whereID+"' value='"+targetUIWhere.getPeriodStartType()+"'/>");
				tags.append("<input type='hidden' id='ePeriodEndType_"+whereID+"' name='ePeriodEndType_"+whereID+"' value='"+targetUIWhere.getPeriodEndType()+"'/>");
				tags.append("<input type='text' id='ePeriodStartValue_"+whereID+"' name='ePeriodStartValue_"+whereID+"' value='"+ periodStartValue +"'>");
				tags.append("&nbsp;&nbsp;~&nbsp;&nbsp;");
				tags.append("<input type='text' id='ePeriodEndValue_"+whereID+"' name='ePeriodEndValue_"+whereID+"' value='"+ periodEndValue +"'>");
			}else if(whereType==Constant.TARGET_UI_WHERETYPE_INPUT){
				String inputValue = "";
				if(!targetID.equals("0")){
					for(TargetUIGeneralInfo targetUIGeneralInfo:targetUIGeneralInfos)
					{
						if(targetUIGeneralInfo.getWhereID()==whereID)
							inputValue = targetUIGeneralInfo.getInputValue();
					}
				}
				tags.append("<input type='text' id='eInputValue_"+whereID+"' name='eInputValue_"+whereID+"' value='"+inputValue+"' />");
				
			}else if(whereType==Constant.TARGET_UI_WHERETYPE_LIKE){
				String inputValue = "";
				if(!targetID.equals("0")){
					for(TargetUIGeneralInfo targetUIGeneralInfo:targetUIGeneralInfos)
					{
						if(targetUIGeneralInfo.getWhereID()==whereID)
							inputValue = targetUIGeneralInfo.getInputValue();
					}
				}
				tags.append("<input type='text' id='eInputValue_"+whereID+"' name='eInputValue_"+whereID+"' value='"+inputValue+"' />");
			}
			tags.append("<br><div class='explain'>"+targetUIWhere.getDescription()+"</div>");
			tags.append("</td>");
			tags.append("<tr><td colspan='10' class='ctbl line'></td></tr>");
		}
	}
	

%>
<jsp:useBean id="message" class="java.lang.String" scope="request" />
<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	
	<div style="margin-bottom:10px;width:100%">
		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
		
		<input type="hidden" id="eTargetUIManagerID" name="eTargetUIManagerID" value="<c:out value="${targetUIList.targetUIManagerID}"/>" />
		<input type="hidden" id="eDbID" name="eDbID" value="<c:out value="${targetUIList.dbID}"/>" />
		<input type="hidden" id="eTargetID" name="eTargetID" value="<%=targetID%>" />
		<c:set var="tempQuery" value="${targetUIList.selectText} ${targetUIList.fromText} ${targetUIList.whereText}" /> 
		<input type="hidden" id="eQueryText" name="eQueryText" value="<c:out value="${tempQuery}"/>" />
		<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1 mustinput" width="100px" align="left">대상자그룹명</td>
					<td class="ctbl td">
						<div>
						<c:import url="../../../include_inc/target_group_inc.jsp">
						<c:param name="mustInput" value="Y"/>			
						<c:param name="targetGroupID" value="<%=String.valueOf(targetList.getTargetGroupID())%>"/>					
						</c:import>
						</div>
					<input type="text" id="eTargetName" name="eTargetName" value="<%=targetList.getTargetName()%>" size="50" mustInput="Y" msg="대량메일명을 입력하세요"/>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>	
			<tr>
				<td class="ctbl ttd1" width="100px">설명</td>
				<td class="ctbl td"><input type="text" id="eDescription" name="eDescription" value="<%=targetList.getDescription()%>" size="50"/></td>
			</tr>
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>				
			<tr>
				<td class="ctbl ttd1 mustinput" width="100px">공유여부</td>
				<td class="ctbl td">
				<ul id="eShareType"  class="selectBox" >
					<li data="1" <c:if test="${targetList.shareType=='1'}">select='YES'</c:if>>비공유</li>
					<li data="2" <c:if test="${targetList.shareType=='2'}">select='YES'</c:if>>그룹공유</li>
					<li data="3" <c:if test="${targetList.shareType=='3'}">select='YES'</c:if>>전체공유</li>												
				</ul>
				</td>
			</tr>	
			</tbody>
		</table>
		<br> 
		<table class="ctbl" width="100%">
			<tbody>
			<%=tags.toString() %>
			</tbody>
		</table>
		</form>
	</div>
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>'))" ><img src="images/close.png"/></a></div>
		<%if(targetID.equals("0")){ %>
		<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').addTarget(<c:out value="${targetUIList.targetUIManagerID}"/>,'0')" ><img src="images/add_target.png"/></a></div>
		<%}else{ %>
		<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').addTarget(<c:out value="${targetUIList.targetUIManagerID}"/>,'<%=targetID %>')" ><img src="images/save.png"/></a></div>
		<%} %>
	
	<script language="javascript">
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));
	</script>
		
	

<%
}
%>