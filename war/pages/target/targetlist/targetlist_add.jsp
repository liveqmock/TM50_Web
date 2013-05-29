<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.target.targetlist.control.*"%>
<%@ page import="web.target.targetlist.service.*"%>
<%@ page import="web.target.targetlist.model.*"%>
<%@ page import="web.common.util.*" %>    
<%@ page import="java.util.*" %>   

<%
	String id = request.getParameter("id");
	String preID = request.getParameter("preID");
	String method = request.getParameter("method");
	String targetID = request.getParameter("targetID");
	TargetListService service = TargetListControlHelper.getUserService(application);
	List<OneToOne> ontooneList = service.listAddOneToOne(new Integer(targetID));
	
	if(method.equals("editAddType")) {
		String targetTable= request.getParameter("targetTable");
%>
	<div  style="width:635px;border:1px #b5cde8 solid;">
	<form id="<%=id%>_form" name="<%=id%>_form" method="post">
	<input type="hidden" id="eTargetID" name="eTargetID" value="<%=targetID%>" />
	<input type="hidden" id="eOneToOneYN" name="eOneToOneYN" value="N" />
	<input type="hidden" id="eTargetTable" name="eTargetTable" value="<%=targetTable %>" />
	<div style="margin:2px">
		<table border="0" cellpadding="3" class="ctbl" width="100%">
			<tbody>
				<tr style="display: none;">
					<td class="ctbl ttd1 mustinput" width="100px" align="left">추가/제외선택</td>
					<td class="ctbl td">
					<input type="radio" id="eAddType" name="eAddType" class="notBorder" value="1">&nbsp;추가&nbsp;&nbsp;&nbsp;
					<input type="radio" id="eAddType" name="eAddType" class="notBorder" value="2">&nbsp;제외&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				<tr style="display: none;">
					<td colspan="10" class="ctbl line"></td>
				</tr>	
				<tr>
					<td class="ctbl ttd1 mustinput" width="100px">등록구분</td>
					<td class="ctbl td">
					<input type="radio" id="eAddTypeInput" name="eAddTypeInput" class="notBorder" value="1" >&nbsp;직접입력&nbsp;&nbsp;&nbsp;
					<input type="radio" id="eAddTypeInput" name="eAddTypeInput" class="notBorder" value="2" >&nbsp;파일업로드&nbsp;&nbsp;&nbsp;	
					<input type="radio" id="eAddTypeInput" name="eAddTypeInput" class="notBorder" value="3" >&nbsp;컬럼별입력&nbsp;&nbsp;&nbsp;	
					<br>&nbsp; * 파일로 업로드 할 경우, 파일의 각 필드명은 기존에 작성된 필드명과 일치해야 합니다.  				
					</td>
				</tr>				
			</tbody>
		</table>
	</div>
	<!-- ################## 직접입력 ##########################  -->
	<div id="<%=id %>_addDirect" style="margin:3px">
		<table class="ctbl" width="100%">
			<tbody>
				<tr>
					<td class="ctbl ttd1 mustinput" width="100px">직접입력창</td>
					<td class="ctbl td" >
					<div style="float:left">
					<textarea id="eDirectText" name="eDirectText" style="width:500px;height:100px"><%int fieldCount = 0; for(OneToOne onetoone: ontooneList) { if(fieldCount > 0){%>,<% }%><%=onetoone.getFieldDesc()%><% fieldCount++; } %></textarea>
					</div>
					<!-- <div style="float:left;margin:20px 0 0 15px"><br>
					<div style="float:left" class="btn_green"><a href="javascript:$('<%=id%>').showDirectOneToOne('<%=targetID %>')" style="cursor:pointer"><span>입력확인</span></a></div>
					</div>-->
					</td>
					</tr>					
			</tbody>
		</table>	
		<!-- ############ 직접입력 일대일 치환 매칭 ################# -->
		<table class="ctbl" style="width:100%;margin-top:3px;display:none">
			<tbody>
				<tr>
					<td>
						<div>
							<div class="gray_border" style="float:left;color:#4F7EB3;height:25px;margin-top:10px;margin-left:10px"><b>일대일 치환</b></div>
						</div>
				</td>
				</tr>
				<tr>
					<td style="padding:0">
						<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
							<thead>
								<tr>
									<th style="height:26px;width:10%">컬럼번호</th>
									<th style="height:26px;width:45%">컬럼명</th>
									<th style="height:26px;width:45%">일대일 치환명</th>
								</tr>
							</thead>
						</TABLE>
								
						<div style="vertical-align:top;overflow-y:scroll;width:100%" id="<%=id%>_directOneToOneWrapper">
							<TABLE style="border:0px" class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
								<tbody id="<%=id%>_directOneToOne" >
								</tbody>
							</TABLE>
						</div>
					</td>
				</tr>
			</tbody>
		</table>					
	</div>
	<!-- ################## 파일업로드 ##########################  -->
	<div id="<%=id %>_addFile" style="margin:3px">
		<table class="ctbl" width="100%">
			<tbody>
				<tr>
					<td class="ctbl ttd1 mustinput" width="100px">업로드할 파일</td>
					<td class="ctbl td">
					<div  style="float:left" id="<%=id%>uploadWrapper" ></div>
					<div style="float:left;padding:22px">
					<a id="<%=id%>_uploadBtn" href="javascript:$('<%=id%>').uploadFile('<%=targetID%>')" class="web20button bigblue" title="선택한 파일을 업로드 하신 후  일대일치환을 선택하세요">파일 업로드</a>
					</div>
					</td>
				</tr>					
				<tr>
					<td colspan="10" class="ctbl line"></td>
				</tr>
				<tr>
					<td class="ctbl ttd1" width="100px">업로드된 파일</td>
					<td class="ctbl td" style="padding:5px">
					<div  id="<%=id%>uploaded" >
					<div style="float:left">
					
					</div>	
					</div>
					</td>
				</tr>	
			</tbody>
		</table>
		<!-- ############ 파일 일대일 치환 매칭 ################# -->
		<table class="ctbl" style="width:100%;display:none" >
			<tbody>
				<tr>
					<td>
						<div>
							<div class="gray_border" style="float:left;color:#4F7EB3;height:25px;margin-top:10px;margin-left:10px"><b>일대일 치환</b></div>
						</div>
					</td>
				</tr>
				<tr>
					<td style="padding:0">
						<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
							<thead>
								<tr>
									<th style="height:26px;width:10%">컬럼번호</th>
									<th style="height:26px;width:45%">컬럼명</th>
									<th style="height:26px;width:45%">일대일 치환명</th>
								</tr>
							</thead>
						</TABLE>
						<div style="vertical-align:top;overflow-y:scroll;width:100%" id="<%=id%>_fileOneToOneWrapper">
							<TABLE style="border:0px" class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
								<tbody id="<%=id%>_fileOneToOne" >
								</tbody>
							</TABLE>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>		
		<!-- ############ 컬럼별입력 ################# -->
	<div id="<%=id %>_addFormType" style="margin:3px">
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%">
			<thead>
				<tr>
					<%for(OneToOne onetoone: ontooneList) { %><th style="height:30px;"><%=onetoone.getFieldDesc()%></th><%} %>																		
				</tr>
			</thead>
			<tbody>
				<tr>
					<%for(OneToOne onetoone: ontooneList) { %><td style="height:30px;"><input type="text" id="eAddFormType" name="eAddFormType"></td><%} %>																		
				</tr>
			</tbody>
		</TABLE>					
	</div>
	</form>
	</div>
	
	<p>						
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>'))"  class="web20button bigpink">닫 기</a></div>
	<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').checkSaveData(<%=targetID %>)" class="web20button bigblue">저 장</a></div>
	
	<script type="text/javascript">	

	
	/***********************************************/
	/* 파일 업로드 랜더링
	/***********************************************/
	$('<%=id%>').renderUpload = function() {
		
		$('<%=id%>').uploader = new SwFileUpload('<%=id%>UploadFlash', {
			width: 350,
			container: '<%=id%>uploadWrapper',
			fileTypeName: '파일업로드(csv,txt,xls,xlsx)',
			allowFileType: '*.csv;*.txt;*.xls;*.xlsx',
			uploadPage: 'target/targetlist/target.do?method=fileUpload',
			onComplete: function( isError, fileNameArray ) { // 업로드 완료

				$('<%=id%>_uploadBtn').setStyle('display','block');
				$('<%=id%>_saveBtn').setStyle('display','block');
				$('<%=id%>').showFileInfo( $('<%=id%>').showFileOneToOne );
				$('<%=id%>_form').eOneToOneYN.value='Y';
			},
			onNotExistsUploadFile: function() { // 업로드 할 파일이 없을때
				alert('업로드할 파일을 선택해 주시기 바랍니다');
			}
			
		}).render();

	}


	/***********************************************/
	/* 파일 업로드 저장 버튼 클릭
	/***********************************************/

	$('<%=id%>').uploadFile = function( eTargetID ) {
		
		// 추가 일때 필요한 key
		$('<%=id%>').uploader.uploadKey = '<%=request.getSession().getId()%>'+(new Date().getTime());
		
		// 파라미터 셋팅
		$('<%=id%>').uploader.setParameter({ 'id': '<%=id%>', 'targetID' : eTargetID });
		// 업로드
		$('<%=id%>').uploader.upload();
	}	
	
	/***********************************************/
	/* 파일 정보 보이기
	/***********************************************/
	$('<%=id%>').showFileInfo = function( nextFunc ) {

		// 업로드가 완료되면 처리될 action
		nemoRequest.init({
			busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window
			updateWindowId: $('<%=id%>uploaded'),  // 완료후 버튼,힌트 가 랜더링될 window
			url: 'target/targetlist/target.do?method=getFileInfo',
			update: $('<%=id%>uploaded'), // 완료후 content가 랜더링될 element
			
			onSuccess: function(html,els,resHTML,scripts) {

				if(nextFunc) nextFunc();
							
			}
		});
		nemoRequest.get({
			'id': '<%=id%>', 
			'targetID': $('<%=id%>_form').eTargetID.value,
			'uploadKey': $('<%=id%>').uploader.uploadKey
		});
	}
	
	/***********************************************/
	/* 직접입력 원투원 정보 불러오기
	/***********************************************/
	$('<%=id%>').showDirectOneToOne = function(targetID) {

		$('<%=id%>_fileOneToOne').empty();
		$('<%=id%>_directOneToOne').empty();

		// 파일정보 표시 이 후 컬럼 정보(원투원)를 불러온다.
		
		var frm = $('<%=id%>_form');
		if(frm.eDirectText.value==''){
			toolTip.showTipAtControl(frm.eDirectText,'먼저 직접입력을 하시기 바랍니다.');
			return;
		}
		
		nemoRequest.init({
			updateWindowId: $('<%=id%>_directOneToOne'),  // 완료후 버튼,힌트 가 랜더링될 window
			url: 'target/targetlist/target.do?method=getDirectHeader&id=<%=id%>&preType=add&targetID='+targetID,
			update: $('<%=id%>_directOneToOne'), // 완료후 content가 랜더링될 element
			onSuccess: function(html,els,resHTML,scripts) {
				$('<%=id%>_form').eOneToOneYN.value='Y';
			}
		});
		nemoRequest.post(frm);

	}

	/***********************************************/
	/* 파일 원투원 정보 불러오기
	/***********************************************/
	$('<%=id%>').showFileOneToOne = function( ) {

		$('<%=id%>_fileOneToOne').empty();
		$('<%=id%>_directOneToOne').empty();
		
		// 파일정보 표시 이 후 컬럼 정보(원투원)를 불러온다.
		nemoRequest.init({
			updateWindowId: $('<%=id%>_fileOneToOne'),  // 완료후 버튼,힌트 가 랜더링될 window
			url: 'target/targetlist/target.do?method=getFileHeader&preType=add',
			update: $('<%=id%>_fileOneToOne'), // 완료후 content가 랜더링될 element
			onSuccess: function(html,els,resHTML,scripts) {
			
			}
		});
		nemoRequest.get({
			'id': '<%=id%>', 
			'targetID': $('<%=id%>_form').eTargetID.value,
			'uploadKey': $('<%=id%>').uploader.uploadKey
		});		
	}
	/***********************************************/
	/* 저장버튼 클릭
	/***********************************************/
	$('<%=id%>').checkSaveData = function( targetID ) {
		var frm = $('<%=id%>_form');
		//직접입력 -------------------------------------------------------
		if(frm.eAddTypeInput[0].checked==true){
			$('<%=id%>').showDirectOneToOne(targetID);
		}
		// 파일업로드 ----------------------------------------------------
		else if(frm.eAddTypeInput[1].checked==true){
			$('<%=id%>').saveData(targetID);
		}
		// 폼별저장 ----------------------------------------------------
		else if(frm.eAddTypeInput[2].checked==true){
			$('<%=id%>').formTypeSave(targetID);
		} 
	}
	/***********************************************/
	/* 저장버튼 클릭
	/***********************************************/
	$('<%=id%>').saveData = function( targetID ) {
		var frm = $('<%=id%>_form');
		var goUrl = '';
		
		//직접입력 -------------------------------------------------------
		if(frm.eAddTypeInput[0].checked==true){
			if(frm.eDirectText.value==''){
				toolTip.showTipAtControl( frm.eDirectText,'직접입력창에 작성하세요');
				return;
			}
			goUrl = 'target/targetlist/target.do?id=<%=id%>&method=insertDirectAdd&targetID='+targetID;	
		}
		// 파일업로드 ----------------------------------------------------
		else if(frm.eAddTypeInput[1].checked==true){
			if(frm.eOneToOneYN.value=='N'){
				toolTip.showTipAtControl( $('<%=id%>_uploadBtn'),'파일업로드 버튼을 클릭하세요');
				return;
			}
			goUrl = 'target/targetlist/target.do?id=<%=id%>&method=insertFileAdd&targetID='+targetID+'&uploadKey='+$('<%=id%>').uploader.uploadKey;	
		}
		//---------------------------------------------------------------

		nemoRequest.init( 
		{
			busyWindowId: '<%=id%>'  // busy 를 표시할 window
			//updateWindowId: $('<%=id%>') // 완료후 버튼,힌트 가 랜더링될 window
			, url: goUrl
			, update: $('<%=id%>') // 완료후 content가 랜더링될 element
			, onSuccess: function(html,els,resHTML) {
				closeWindow( $('<%=id%>') );
				$('target_preview_modal').allList();
				$('target').allList();
			}
		});
		nemoRequest.post(frm);
		
	}

	/***********************************************/
	/* 폼 별  저장
	/***********************************************/
	$('<%=id%>').formTypeSave = function(targetID) {

		var frm = $('<%=id%>_form');

		if(frm.eAddFormType.length > 1){
			if(frm.eAddFormType[0].value == "" || frm.eAddFormType[0].value == null){
					alert('이메일은 필수 입력사항 입니다.');
					return;
			}
		}else{
			if(frm.eAddFormType.value == "" || frm.eAddFormType.value == null){
				alert('이메일은 필수 입력사항 입니다.');
				return;
			}
		}
		
		nemoRequest.init( 
			{
				busyWindowId: '<%=id%>'  // busy 를 표시할 window
				//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window
				, url: 'target/targetlist/target.do?method=formTypeSave&id=<%=id%>&tableName=<%=targetTable%>&targetID=<%=targetID%>'
				, update: $('<%=id%>') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {
					closeWindow( $('<%=id%>') );
					$('target_preview_modal').allList();
					$('target').allList();
				}
			});
		nemoRequest.post(frm);
		
	}

	/***********************************************/
	/* onetooen 이메일을 선택한 셀렉트 박스가 있는지 체크
	/***********************************************/
	$('<%=id%>').isSelectedOneToOne = function( oneToOneID ) {

		var frm = $('<%=id%>_form');
		var selectObj = $('<%=id%>_form').elements[oneToOneID];

		for(var i=0; i < selectObj.length; i++ ) {
			var valArr = selectObj[i].value.split('≠');
			if($('<%=id%>').ontooneEmailID == valArr[1]) { // 이메일을 선택한 셀렉트 박스가 있는지
				return true;
			}
		}

		return false;

	}
	/***********************************************/
	/* onetooen 중복선택 방지
	/***********************************************/
	$('<%=id%>').chkDuplicateSelectFile = function( selObj, selectObjsID, rownum ) {
		
		var frm = $('<%=id%>_form');
		var selectObjs = $('<%=id%>_form').elements[selectObjsID];
		
		if(selObj.selectedIndex == 0) {
			if(selObj.length==selectObjs.length)
			{
					//$('<%=id%>_form').eDescript.value = '';
					return;
			}	
			else
			{
				//$('<%=id%>_form').eDescript[rownum].value = '';
				return; 			
			}
		}
		if(selObj.length==selectObjs.length)
		{
			//$('<%=id%>_form').eDescript.value = selObj.options[selObj.selectedIndex].text;
			return;
		}	
				
		
		for(var i=0; i < selectObjs.length; i++ ) {
			
			if(selObj.selectedIndex == selectObjs[i].selectedIndex && selObj != selectObjs[i]) {
				toolTip.showTipAtControlOffset(selectObjs[i],'"'+selObj.options[selObj.selectedIndex].text+'" 는(은) 이미 선택 하셨습니다',{x:-10,y:+90});
				//$('<%=id%>_form').eDescript[rownum].value ='';
				selObj.selectedIndex = 0;
				return; 
			} 
			else{
				
				//$('<%=id%>_form').eDescript[rownum].value = selObj.options[selObj.selectedIndex].text;	
			}	
		}
		

		return false;

	}
	window.addEvent('domready', function(){		
		$('<%=id%>').renderUpload();
		var frm = $('<%=id%>_form');

		renderTableHeader( "<%=id %>_addFile" );
		renderTableHeader( "<%=id %>_addFormType" );		
		
		frm.eAddType[0].checked=true;

		frm.eAddTypeInput[0].checked=true;
		$('<%=id%>_addFile').setStyle('visibility','hidden');
		$('<%=id%>_addFile').setStyle('height','0');
		$('<%=id%>_addDirect').setStyle('display','block');
		$('<%=id%>_addFormType').setStyle('display','none');
		
		//직접입력
		$(frm.eAddTypeInput[0]).addEvent('click', function(){
			$('<%=id%>_addFile').setStyle('visibility','hidden');
			$('<%=id%>_addFile').setStyle('height','0');
			$('<%=id%>_addDirect').setStyle('display','block');
			$('<%=id%>_addFormType').setStyle('display','none');
		});	
		//파일입력
		$(frm.eAddTypeInput[1]).addEvent('click', function(){
			$('<%=id%>_addFile').setStyle('visibility','');
			$('<%=id%>_addFile').setStyle('height','100%');
			$('<%=id%>_addDirect').setStyle('display','none');
			$('<%=id%>_addFormType').setStyle('display','none');
		});	

		//폼별입력
		$(frm.eAddTypeInput[2]).addEvent('click', function(){
			$('<%=id%>_addFile').setStyle('visibility','hidden');
			$('<%=id%>_addFile').setStyle('height','0');
			$('<%=id%>_addDirect').setStyle('display','none');
			$('<%=id%>_addFormType').setStyle('display','block');
		});
	});	
	
	</script>
<%}
	if(method.equals("getDirectHeader")) {
		// 필수 입력 이메일 아이디를 변수에 저장
		int cnt = 0;
%>
	<c:forEach items="${directHeaderInfoList}" var="fileHeaderInfo">
	<TR class="tbl_tr" >
		<TD class="tbl_td dotted" style="width:60px;height:26px"><b><c:out value="${fileHeaderInfo.columnNumber}" /></b></TD>
		<TD class="tbl_td dotted"><c:out value="${fileHeaderInfo.title}" escapeXml="true"/></TD>
		<TD class="tbl_td dotted" style="width:250px">
			<select id="eDirectOneToOne" name="eDirectOneToOne" onChange="$('<%=id%>').chkDuplicateSelectFile(this,'eDirectOneToOne','<c:out value="${list.index}" />')">
				<option value="">이 필드는 제외</option>
			<%
				int ontooneID = -1;
				cnt ++;
				List<OnetooneTarget> onetooneTargetList = service.getOnetoOneTargetByColumnPos(Integer.parseInt(targetID),cnt);
				if(onetooneTargetList != null && onetooneTargetList.size() > 0) {
					ontooneID = onetooneTargetList.get(0).getOnetooneID();					
				}
				for(OneToOne onetoone: ontooneList) {
			%>
				<option value="<c:out value="${fileHeaderInfo.columnNumber}" />≠<%=onetoone.getOnetooneID()%>≠<c:out value="${fileHeaderInfo.title}" escapeXml="true"/>" <%=((ontooneID==onetoone.getOnetooneID())?"selected":"")%>><%=onetoone.getOnetooneName()%></option>
			<%
				}
			%>
			</select>
		</TD>
	</TR>	
	</c:forEach>
	<script type="text/javascript">
	window.addEvent('domready', function(){
		$('<%=id%>').saveData('<%=targetID%>');
	});
	</script>
<%}
//****************************************************************************************************/
// 파일 헤더 및 원투원 불러오기 
//****************************************************************************************************/
if(method.equals("getFileHeader")) {

	String uploadKey = ServletUtil.getParamStringDefault(request, "uploadKey","");
	int cnt = 0;
%>
	<c:forEach items="${fileHeaderInfoList}" var="fileHeaderInfo">
		<TR class="tbl_tr" >
			<TD class="tbl_td dotted" style="width:60px;height:26px"><b><c:out value="${fileHeaderInfo.columnNumber}" /></b></TD>		
			<TD class="tbl_td dotted"><c:out value="${fileHeaderInfo.title}" escapeXml="true"/></TD>
			<TD class="tbl_td dotted" style="width:250px">
				<select id="eFileOneToOne" name="eFileOneToOne" onChange="$('<%=id%>').chkDuplicateSelectFile(this,'eFileOneToOne','<c:out value="${list.index}" />')">
					<option value="">이 필드는 제외</option>
				
				<%
					int ontooneID = -1;
					cnt ++;
					List<OnetooneTarget> onetooneTargetList = service.getOnetoOneTargetByColumnPos(Integer.parseInt(targetID),cnt);
					if(onetooneTargetList != null && onetooneTargetList.size() > 0) {
						ontooneID = onetooneTargetList.get(0).getOnetooneID();					
					}
					for(OneToOne onetoone: ontooneList) {
				%>
					<option value="<c:out value="${fileHeaderInfo.columnNumber}" />≠<%=onetoone.getOnetooneID()%>≠<c:out value="${fileHeaderInfo.title}" escapeXml="true"/>" <%=((ontooneID==onetoone.getOnetooneID())?"selected":"")%>><%=onetoone.getOnetooneName()%></option>
				<%
					}
				%>
				</select>
				
			</TD>
		</TR>	
	</c:forEach>
<%}
if(method.equals("addHistory")) {%>
	<div style="clear:both;width:440px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
	<thead>
		<tr>
			<th style="height:30px;width:50px">구분</th>
			<th style="height:30px;width:50px">등록구분</th>
			<th style="height:30px;">입력데이터</th>	
			<th style="height:30px;width:150px"">등록일자</th>	
		</tr>
	</thead>
	<tbody id="<%=id%>_grid_content">
	
	</tbody>
	</table>
	</form>
</div>
<script type="text/javascript">
	/***********************************************/
	/* 리스트 
	/***********************************************/
	
	$('<%=id%>').list = function ( forPage ) {	
	
		nemoRequest.init( 
		{
			busyWindowId: '<%=id%>',  // busy 를 표시할 window
			updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window
	
			url: 'target/targetlist/target.do?method=addHistory&id=<%=id%>&targetID=<%=targetID%>', 
			update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
			onSuccess: function(html,els,resHTML,scripts) {

			}
		});
		nemoRequest.post($('<%=id%>_list_form'));
	}

	/* 리스트 표시 */
	window.addEvent("domready",function () {	
		$('<%=id%>').list(); 
	});
</script>
<%}
if(method.equals("list")) {
%>
	<c:forEach items="${addLists}" var="addList">
		<TR>
			<TD class="tbl_td" align="center">
				<c:if test="${addList.addType == '1'}" >
					추가
				</c:if>
				<c:if test="${addList.addType == '2'}" >
					제외
				</c:if>
			</TD>
			<TD class="tbl_td" align="center">
				<c:if test="${addList.addTypeInput == '1'}" >
					<img src="images/trees/book_icon.gif" title="직접입력"/>
				</c:if>
				<c:if test="${addList.addTypeInput == '2'}" >
					<img src="images/trees/csv_file.gif" title="파일업로드"/>
				</c:if>
			</TD>
			<TD class="tbl_td">
				<c:if test="${addList.addTypeInput == '1'}" >
					<a href="pages/target/targetlist/targetlist_add_down.jsp?targetAddID=<c:out value="${addList.targetAddID}"/>">
						입력정보
					</a>
				</c:if>
				<c:if test="${addList.addTypeInput == '2'}" >
					<a href="target/targetlist/target.do?method=fileDownload&uploadKey=<c:out value="${addList.uploadKey}"/>">
						<c:out value="${addList.realFileName}"/><c:out value="${addList.targetAddID}"/>
					</a>
				</c:if>
			
			</TD>
			<TD class="tbl_td">
				<c:out value="${addList.registDate}"/>
			</TD>
		</TR>
	</c:forEach>
	
	<script type="text/javascript">

		window.addEvent('domready', function(){
			
			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				,focus: true  // 마우스 이동시 포커스 여부
				,cursor: 'default' // 커서
				,select: true // 마우스 클릭시 셀렉트 표시 여부
			});
			$('<%=id%>').grid_content.render();
			
		});
	</script>
<%} %>		

