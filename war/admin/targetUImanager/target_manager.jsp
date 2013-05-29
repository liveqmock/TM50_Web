<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	String id = request.getParameter("id");
%>


<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:100%">

	<div class="right">
				<a href="javascript:$('<%=id%>').deleteSelectedData()" class="web20button bigpink">선택 삭제</a>
	</div>
	<div class="right">
			<a href="javascript:$('<%=id%>').editWindow( 0 )" class="web20button bigblue">추가</a>
	</div>
</div>

</form>

<div style="clear:both;width:100%">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
	<thead>
		<tr>
		<th style="height:30px;width:50px"><input id="sCheckAll" class="notBorder" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_list_form').eTargetUIManagerID,this.checked)"/></th>
		<th style="height:30px;width:50px">ID</th>
		<th style="height:30px;">회원검색UI명</th>
		<th style="height:30px;width:50px">사용여부</th>
		<th style="height:30px;width:50px">디폴트여부</th>
		
		</tr>
	</thead>
	<tbody id="<%=id%>_grid_content">
	
	</tbody>
	</table>
	</form>
</div>
    
<div id="<%=id%>_paging" class="page_wrapper"></div>


<script type="text/javascript">

/***********************************************/
/* 셀렉트 필드 추가
/***********************************************/
$('<%=id%>').addSelect = function(row){
	var rowNew = row+1;
	var tagTr = document.getElementById("eSelect"+row);
	
	var tagTrNew = document.createElement("tr");
	tagTrNew.setAttribute("id","eSelect"+rowNew);
	tagTrNew.setAttribute("name","eSelect"+rowNew);
	var node = tagTr.childNodes;
	
	var len = node.length;
	
	for(var i = 0; i<len;i++)
	{
		
		var c = node.item(i).cloneNode(true);
		
		if(i==0)
		{
			c.firstChild.setAttribute("id","eSelectFieldName"+rowNew);
			c.firstChild.setAttribute("name","eSelectFieldName"+rowNew);
			c.firstChild.nextSibling.setAttribute("href","javascript:$('targetmanager').addSelect("+rowNew+")");
			c.firstChild.nextSibling.nextSibling.nextSibling.setAttribute("href","javascript:$('targetmanager').delSelect("+rowNew+")");
			c.firstChild.setAttribute("value","");
		}
		else if(i==1)
		{
			c.firstChild.setAttribute("id","eOneToOne"+rowNew);
			c.firstChild.setAttribute("name","eOneToOne"+rowNew);
			
		}	
		else if(i==2)
		{
			c.firstChild.setAttribute("id","eSelectDescript"+rowNew);
			c.firstChild.setAttribute("name","eSelectDescript"+rowNew);
			c.firstChild.setAttribute("value","");
		}	
		tagTrNew.appendChild(c);
		
		
	}
	
	var tagTbody = document.getElementById("<%=id%>_setSelect");
	tagTbody.appendChild(tagTrNew);
	
	var a = document.getElementById("eSelectFieldName"+row);
	tagTr.firstChild.removeChild(a.nextSibling.nextSibling.nextSibling);
	tagTr.firstChild.removeChild(a.nextSibling);
	document.getElementById("eSelectCount").setAttribute("value",rowNew);
	
	
	
}


/***********************************************/
/* 셀렉트 필드 제거
/***********************************************/
$('<%=id%>').delSelect = function(row){
	
	var rowBefore = row-1;
	var tagTr = document.getElementById("eSelect"+row);
	var tagTrBefore = document.getElementById("eSelect"+rowBefore);

	var aNodeAdd = document.createElement("a");
	var textAdd = document.createTextNode("추가");
	aNodeAdd.appendChild(textAdd);
	
	var aNodeDel = document.createElement("a");
	var textDel = document.createTextNode("제거");
	aNodeDel.appendChild(textDel);
	
	aNodeAdd.setAttribute("href","javascript:$('targetmanager').addSelect("+rowBefore+")");
	aNodeDel.setAttribute("href","javascript:$('targetmanager').delSelect("+rowBefore+")");

	var nodeSpace = tagTrBefore.firstChild.firstChild.nextSibling;
	tagTrBefore.firstChild.insertBefore(aNodeAdd,nodeSpace);
	tagTrBefore.firstChild.appendChild(aNodeDel);

	var tagTbody = document.getElementById("<%=id%>_setSelect");
	tagTbody.removeChild(tagTr);
	
	

	document.getElementById("eSelectCount").setAttribute("value",rowBefore);
	
	
	
}

/***********************************************/
/* where 필드 추가
/***********************************************/
$('<%=id%>').addWhere = function(row){
	var rowNew = row+1;
	var tagTr = document.getElementById("eWhere"+row);
	
	var tagTrNew = document.createElement("tr");
	tagTrNew.setAttribute("id","eWhere"+rowNew);
	tagTrNew.setAttribute("name","eWhere"+rowNew);
	
	var node = tagTr.childNodes;
	
	var len = node.length;
	
	for(var i = 0; i<len;i++)
	{
		
		var c = node.item(i).cloneNode(true);
		if(i==0)
		{
			c.firstChild.setAttribute("id","eWhereFieldName"+rowNew);
			c.firstChild.setAttribute("name","eWhereFieldName"+rowNew);
			c.firstChild.setAttribute("value","");
			c.firstChild.nextSibling.setAttribute("href","javascript:$('targetmanager').addWhere("+rowNew+")");
			c.firstChild.nextSibling.nextSibling.nextSibling.setAttribute("href","javascript:$('targetmanager').delWhere("+rowNew+")");
		}
		else if(i==1)
		{
			c.firstChild.setAttribute("id","eWhereName"+rowNew);
			c.firstChild.setAttribute("name","eWhereName"+rowNew);
			c.firstChild.setAttribute("value","");
		}
		else if(i==2)
		{
			c.firstChild.setAttribute("id","eWhereType"+rowNew);
			c.firstChild.setAttribute("name","eWhereType"+rowNew);
			
		}
		else if(i==3)
		{
			c.firstChild.setAttribute("id","eWhere1Value"+rowNew);
			c.firstChild.setAttribute("name","eWhere1Value"+rowNew);
			c.firstChild.setAttribute("value","");
		}	
		else if(i==4)
		{
			c.firstChild.setAttribute("id","eWhere2Value"+rowNew);
			c.firstChild.setAttribute("name","eWhere2Value"+rowNew);
			c.firstChild.setAttribute("value","");
		}	
		tagTrNew.appendChild(c);
		
		
	}
	
	var tagTbody = document.getElementById("<%=id%>_setWhere");
	tagTbody.appendChild(tagTrNew);
	
	var a = document.getElementById("eWhereFieldName"+row);
	tagTr.firstChild.removeChild(a.nextSibling.nextSibling.nextSibling);
	tagTr.firstChild.removeChild(a.nextSibling);
	

	document.getElementById("eWhereCount").setAttribute("value",rowNew);
	
}

/***********************************************/
/* where 필드 삭제
/***********************************************/
$('<%=id%>').delWhere = function(row){
	var rowBefore = row-1;
	var tagTr = document.getElementById("eWhere"+row);
	var tagTrBefore = document.getElementById("eWhere"+rowBefore);

	var aNodeAdd = document.createElement("a");
	var textAdd = document.createTextNode("추가");
	aNodeAdd.appendChild(textAdd);
	
	var aNodeDel = document.createElement("a");
	var textDel = document.createTextNode("제거");
	aNodeDel.appendChild(textDel);
	
	aNodeAdd.setAttribute("href","javascript:$('targetmanager').addWhere("+rowBefore+")");
	aNodeDel.setAttribute("href","javascript:$('targetmanager').delWhere("+rowBefore+")");

	var nodeSpace = tagTrBefore.firstChild.firstChild.nextSibling;
	tagTrBefore.firstChild.insertBefore(aNodeAdd,nodeSpace);
	tagTrBefore.firstChild.appendChild(aNodeDel);

	var tagTbody = document.getElementById("<%=id%>_setWhere");
	tagTbody.removeChild(tagTr);
	
	document.getElementById("eWhereCount").setAttribute("value",rowBefore);
	
}

/***********************************************/
/* 저장버튼 클릭
/***********************************************/
$('<%=id%>').saveData = function( targetManagerID ) {
	var frm = $('<%=id%>_form');
	//필수입력 조건 체크
	if(!checkFormValue(frm)) {
		return;
	}
	
	copyForm( $('<%=id%>_rform'), frm );
	//alert(frm.eFromText.value);
	
	
	if(frm.eDbID.value=='')
	{
		alert('DB를 선택하세요. ');
		frm.eDbID.focus();
		return;
	}
	if(frm.eFromText.value.indexOf('from') < 0 )
	{
		alert('from 구문이 잘못되었습니다. 다시 입력하세요. ');
		frm.eFromText.focus();
		return;
	}
	
	
	if(frm.eWhereText.value!='' && frm.eWhereText.value.indexOf('where') < 0 )
	{
		alert('where 구문이 잘못되었습니다. 다시 입력하세요. ');
		frm.eWhereText.focus();
		return;
	}
		
	
	var method = "";
	if(targetManagerID>0){
		if(!confirm("사용중인 회원정보UI를 수정하면 대상자관리에서 사용할 수 없습니다.\n 수정하시겠습니까? ")) return;
		method = "update";
	}
	else{
		method = "insert";
	}


	nemoRequest.init( 
			{
				busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
				//updateWindowId: $('<%=id%>') // 완료후 버튼,힌트 가 랜더링될 window
				, url: 'admin/targetmanager/targetmanager.do?id=<%=id%>&method='+method+'&targetManagerID='+targetManagerID 
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {
					closeWindow( $('<%=id%>_modal') );
					$('<%=id%>').list();
				}
			});
			nemoRequest.post(frm);
	
	
}

/***********************************************/
/* 삭제
/***********************************************/
$('<%=id%>').deleteData = function( targetManagerID ) {

	if(!confirm("사용중인 회원정보UI를 삭제하면 대상자관리에서 사용할 수 없습니다.\n 정말로 삭제 하시겠습니까?")) return;

	nemoRequest.init( 
			{
				busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
				//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window
				, url: 'admin/targetmanager/targetmanager.do?method=delete&id=<%=id%>&eTargetUIManagerID='+ targetManagerID
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {
				// 수정창을 닫는다
				if($('<%=id%>_modal')) 
					closeWindow( $('<%=id%>_modal') );
				$('<%=id%>').list();
			}
	});
	nemoRequest.post($('<%=id%>_rform'));



	
}
/***********************************************/
/* 선택 - 삭제
/***********************************************/
$('<%=id%>').deleteSelectedData = function(){
	

	var frm = $('<%=id%>_list_form');
	var sfrm = $('<%=id%>_sform');
	var checked = isChecked( frm.elements['eTargetUIManagerID']  );
	
	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 선택하세요');
		return;
	}
	if(!confirm("사용중인 회원정보UI를 삭제하면 대상자관리에서 사용할 수 없습니다.\n 선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

	// 마지막 페이지 에서 전부 삭제 했으면 페이지를 가감
	if(frm.elements['eTargetUIManagerID'].length == checked) {
		$('<%=id%>_rform').elements["curPage"].value = $('<%=id%>_rform').elements["curPage"].value -1;  
	}

	copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'admin/targetmanager/targetmanager.do?method=delete&id=<%=id%>'
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {

			$('<%=id%>_list_form').sCheckAll.checked = false;
			$('<%=id%>').list();

		}
	});
	nemoRequest.post(frm);
	
}


/***********************************************/
/* 팝업메뉴 create
/***********************************************/
$('<%=id%>').createPopup = function() {

	$('<%=id%>').popup = new PopupMenu('<%=id%>');

	$('<%=id%>').popup.add('수정', 
		function(target,e) {
			$('<%=id%>').editWindow( $('<%=id%>').grid_content.getSelectedRow().getAttribute("targetUIManagerID") );
		}
	);

	$('<%=id%>').popup.addSeparator();

	$('<%=id%>').popup.add('삭제', function(target,e) { 
			$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("targetUIManagerID") ); 
		}
	);

	$('<%=id%>').popup.setSize(150, 0);

}
/***********************************************/
/* 전체목록
/***********************************************/
$('<%=id%>').allList = function(){
	initFormValue($('<%=id%>_sform'));
	$('<%=id%>').list ();
}

/***********************************************/
/* 검색 조건 컨트롤 초기화
/***********************************************/

$('<%=id%>').init = function() {

	var frm = $('<%=id%>_sform');

	// 셀렉트 박스 렌더링
	makeSelectBox.render( frm );

	// 키보드 엔터 검색 만들기
	keyUpEvent( '<%=id%>', frm );

}

/***********************************************/
/* 입력창 열기
/***********************************************/
$('<%=id%>').editWindow = function( seq ) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 750,
			height: $('mainColumn').style.height.toInt(),
			//height: 320,
			title: '회원검색UI 등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'admin/targetmanager/targetmanager.do?id=<%=id%>&method=edit&targetUIManagerID='+seq
		}
	);
	
}


/***********************************************/
/* 리스트 
/***********************************************/

$('<%=id%>').list = function ( forPage ) {

	var frm = $('<%=id%>_sform');

	//페이징 클릭에서 리스트 표시는 기존 검색을 따른다
	if(!forPage) {
		//검색 조건 체크
		if(!checkFormValue(frm)) {
			return;
		}
		// 검색 값을 새로운 폼값(검색후 픽스될) 에 담는다.
		cloneForm($('<%=id%>_sform'), '<%=id%>_rform', '<%=id%>',   $('<%=id%>_grid_content'));
	}

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'admin/targetmanager/targetmanager.do?method=list&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}





/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').createPopup();
	$('<%=id%>').list(); 
	
	 
});

</script>