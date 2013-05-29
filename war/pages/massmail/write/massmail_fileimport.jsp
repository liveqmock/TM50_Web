<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>   
<%@ page import="java.util.*" %>  
<%@ page import="org.springframework.web.multipart.MultipartHttpServletRequest"%>
<%@ page import="org.springframework.web.multipart.MultipartFile"%> 

<%
	String preID = request.getParameter("preID");
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	

//****************************************************************************************************/
//파일 선택 
//****************************************************************************************************/
if(method.equals("fileImportForm")) {
%>

<div style="width:620px">
	<form name="<%=id%>_fileimport_form" id="<%=id%>_fileimport_form">
	<input type="hidden" id="eFileKey" name="eFileKey">
<div class="massmail_wrapper">
	<div class="left"><img src="images/tag_blue.png" alt="HTML 파일 불러오기 ">htm, html, txt 형식의 파일에 작성 된 HTML 소스를 불러올 수 있습니다.</div>
</div>
<div style="clear:both;width:100%">
	<table border="0" cellpadding="3" class="ctbl" width="100%">
		<tbody>
			<tr>
				<td class="ctbl ttd1" width="100px" align="left">파일 선택</td>
				<td class="ctbl td">
					<div  style="float:left" id="<%=id%>uploadWrapper" ></div>
					<div style="float:left;padding:22px">
					<a id="<%=id%>_uploadBtn" href="javascript:$('<%=id%>').uploadFile()" class="web20button bigblue" >선택파일입력</a>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
	</form>
</div>
<div class="massmail_wrapper">
	<div class="right"><a href="javascript:closeWindow($('<%=id%>'))"  class="web20button bigpink">닫 기</a></div>
</div>
<div id="<%=id%>_grid_content"></div>
<script type="text/javascript">
/***********************************************/
/* 파일 업로드 랜더링
/***********************************************/
$('<%=id%>').renderUpload = function() {
	
	  var frm = $('<%=id%>_fileimport_form');
	$('<%=id%>').uploader = new SwFileUpload('<%=id%>UploadFlash', {
		width: 350,
		container: '<%=id%>uploadWrapper',
		fileTypeName: '파일불러오기(htm,html,txt)',
		allowFileType: '*.htm;*.html;*.txt;',
		uploadPage: 'massmail/write/massmail.do?method=uploadFile',
		onComplete: function( isError, fileNameArray ) { // 업로드 완료
			frm.eFileKey.value =$('<%=id%>').uploader.uploadKey	
			$('<%=id%>').saveData();
		},
		onNotExistsUploadFile: function() { // 업로드 할 파일이 없을때
			alert('업로드할 파일을 선택해 주시기 바랍니다');
		}
		
	}).render();

}
/***********************************************/
/* 파일 업로드 저장 버튼 클릭
/***********************************************/

$('<%=id%>').uploadFile = function() {
	 // 추가 일때 필요한 key
 	$('<%=id%>').uploader.uploadKey = '<%=request.getSession().getId()%>'+(new Date().getTime());
	// 파라미터 셋팅
	$('<%=id%>').uploader.setParameter({ 'id': '<%=id%>', 'preID' : '<%=preID%>' });
	// 업로드
	$('<%=id%>').uploader.upload();
}	

$('<%=id%>').saveData = function(){
	  	var frm = $('<%=id%>_fileimport_form');
		nemoRequest.init( 
		{
				busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window

				, url: 'massmail/write/massmail.do?method=fileImport&id=<%=id%>&preID=<%=preID%>'
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {

				}
		});
		nemoRequest.post(frm);
}

window.addEvent('domready', function(){		
	$('<%=id%>').renderUpload();

});	

</script>
<%
}
if(method.equals("fileImport")) {
	
%>
<jsp:useBean id="fileContent" class="java.lang.String" scope="request" />
<form name="<%=id%>_fileimport" id="<%=id%>_fileimport">
<input type="hidden" id="fileContent" name="fileContent" value="<c:out value="${fileContent}"/>">
</form>
<%//System.out.println(fileContent); %>
<script type="text/javascript">
/* 리스트 표시 */
window.addEvent("domready",function () {
	var html = $('<%=id%>_fileimport').fileContent.value;
	$('<%=preID%>_ifrmFckEditor').contentWindow.setFCKHtml(html);	
	closeWindow($('<%=id%>'));
});
	
</script>
<%
}
%>