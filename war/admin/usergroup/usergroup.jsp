<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="web.common.util.*"%>
<%
	String id = request.getParameter("id");

String isAdmin = LoginInfo.getIsAdmin(request);

if(isAdmin.equals("Y")){ // 관리자 계정이 아닐 경우 URL 접근 시 접근불가 페이지 출력
%>
<form name="<%=id%>_sform"  id="<%=id%>_sform" style="display:none">
	<input type="hidden" id="IDs" name="IDs" />
	<input type="hidden" id="GroupIDs" name="GroupIDs" />
	<input type="hidden" id="authJSON" name="authJSON" />
</form>

<div>
	<div  style="float:left;width:400px">
		<div class="panel-header">
			<div class="panel-header-ball" >
				<div class="panel-expand icon16" style="width: 16px; height: 16px;"/>
				</div>
			</div>
			<div class="panel-headerContent">
				<h2 id="panel2_title">그룹 및 계정</h2>
			</div>
		</div>
		<div class="panel expanded">
			<div id="<%=id%>_leftPanel"  class="panel expanded" style="padding:8px"/>
				<div style="height:20px">
					<div style="float:right" class="btn_green"><a href="javascript:$('<%=id%>').editGroupWindow('0')" style ="cursor:pointer"><span>그룹추가</span></a></div>
					<div style="float:right" class="btn_b"><a href="javascript:$('<%=id%>').editGroupWindow()" style ="cursor:pointer"><span>그룹수정</span></a></div>
					<div style="float:right" class="btn_r"><a href="javascript:$('<%=id%>').deleteGroup()" style ="cursor:pointer"><span>그룹삭제</span></a></div>
					<div style="float:right" class="btn_green" ><a href="javascript:$('<%=id%>').editUserWindow('insert')" style ="cursor:pointer"><span>사용자추가</span></a></div>
					<div style="float:right" class="btn_b"><a href="javascript:$('<%=id%>').editUserWindow('update')" style ="cursor:pointer"><span>사용자수정</span></a></div>
					<div style="float:right" class="btn_r"><a href="javascript:$('<%=id%>').deleteUser()" style ="cursor:pointer"><span>사용자삭제</span></a></div>
				</div>
				<div style="height:20px;margin-top:7px">
					<div style="float:left">
					<img style="margin-right:5px" src="images/search_person.gif" title="이름/아이디로 검색"/><input type="text" id="<%=id%>_search" size="40" title="검색어의 일부를 입력하세요"/>
					</div>
					<div style="float:right" class="btn_r"><a href="javascript:$('<%=id%>').refreshData()" style ="cursor:pointer"><span>새로고침</span></a>
					</div>
					<div style="float:right" class="btn_green"><a href="javascript:$('<%=id%>').unSelectAll()" style ="cursor:pointer"><span>선택해지</span></a>
					</div>
				</div>
				<div id="<%=id%>_userTreePanel" class="blue-border" style="overflow:auto;height:200px;margin-top:3px">
				</div>
				<div class="blue-border" style="height:200px;margin-top:8px">
					<div class="panel-header">
						<div class="panel-header-ball" >
							<div class="panel-expand icon16" style="width: 16px; height: 16px;"/>
							</div>
						</div>
						<div class="panel-headerContent">
							<h2 id="panel2_title">사용자 정보</h2>
						</div>
					</div>
					<div id="<%=id%>_userInfoPanel" style="padding:3px">
					
					</div>
					
				</div>
				
			
			</div>
		</div>
	</div>
	
	<div id="<%=id%>_divider" class="columnGauge">
		<div class="handleIcon"/>
		</div>
	</div>
	
	
	<div>
		<div class="panel-header">
			<div class="panel-header-ball" >
				<div class="panel-expand icon16" style="width: 16px; height: 16px;"/>
				</div>
			</div>
			<div class="panel-headerContent">
				<h2 id="panel2_title">메뉴 및 데이타  권한</h2>
			</div>
		</div>
		<div id="<%=id%>_menuTreeWrapper" class="panel expanded" style="padding:8px">
		
			<div style="height:24px;">
				<div style="float:left;color:green">권한 적용 대상 : <span style="color:#548CBA;font-weight:bold" id="<%=id%>_userName"></span>
				</div>
				<div style="float:right"><div  class="btn_b"><a id="<%=id%>_authSaveBtn" href="javascript:$('<%=id%>').applyAuth()" style ="cursor:pointer"><span>권한 저장</span></a></div></div>
			</div>
			
			<div id="<%=id%>_rightPanel" class="blue-border" style="overflow:auto">
			</div>
			
			
		</div>		
	</div>
	
</div>
 

<script type="text/javascript">

var <%=id%>Tree = null;

/***********************************************/
/* 컨트롤 초기화
/***********************************************/

$('<%=id%>').init = function() {

	// 판넬 높이 자동
	dynamicResizeHeight([$('<%=id%>_userTreePanel')
		                 ,$('<%=id%>_rightPanel')
		                 ,$('<%=id%>_divider')]
		                 ,'<%=id%>'
		                 ,[302, 75, 2]);

    // 자동완성 **************
    new _bsn.AutoSuggest('<%=id%>_search', {
    	script: 'admin/usergroup/usergroup.do?method=quickSearchUserList&',
    	varname: 'input',  // get id
    	json: true,  // json 데이타인지 
    	timeout:100000,
    	zIndex: getZIndex($('<%=id%>_search')),
    	shownoresults: false, // 결과 없음을 표시 하지 않는다
    	indicatorClass: 'autocompleter-loading',
    	callback: $('<%=id%>').searchUserSelectFnc  // 클릭시 실행할 함수
    });
    /*****************************/
    
}

/***********************************************/
/*자동완성 사용자 검색 후 선택
/***********************************************/
$('<%=id%>').searchUserSelectFnc = function(json) {
	
	var nodeArr = getChildNodes(<%=id%>_tree, json.id.groupid);

	if(nodeArr != null) {
		node = findNode(nodeArr, json.id.userid );

		if(node != null) {
			<%=id%>_tree.expandTo( node );
			<%=id%>_tree.scrollTo( node );
			<%=id%>_tree.select(node);
			//node['switch']();
		}
	}
	
}
/***********************************************/
/* 권한 저장 버튼 클릭
/***********************************************/
$('<%=id%>').applyAuth = function() {

	var checkedCount = <%=id%>_tree.getCheckedCount('group_user|user');
	var node = <%=id%>_tree.getSelected();
	var goUrl = 'admin/usergroup/usergroup.do?id=<%=id%>&method=';
	var frm = $('<%=id%>_sform');
	
	if(node.isRoot() && checkedCount==0) {
		alert('권한을 부여할 그룹이나 사용자를 선택 또는 체크하세요');
		return;
	}
	// 체크가  있을경우
	if(checkedCount > 0) {

		var userArr = [];
		if(!confirm('선택하신 '+checkedCount+'명의 사용자의 권한의 일부를 변경 하시겠습니까?')) return;
		<%=id%>_tree.root.recursive(function(){
			if(!this.isRoot() && this.data.mode == 'user' && this.state.checked == 'checked') {
				userArr.push( this.data.id );
			}
		});
		
		frm.authJSON.value = $('<%=id%>').getCheckedMenuAuthJSON();
		frm.IDs.value = JSON.encode(userArr);
		
		goUrl = goUrl + 'updateSelectedUserAuth';
		 
	} else {
		if(!node) {
			toolTip.showTipAtControl($('<%=id%>_userTreePanel'),'권한을 부여할 그룹이나 사용자를 선택 또는 체크하세요');
			return;
		} else {
			// 메뉴아이디 JSON array 가져오기 ```
			frm.authJSON.value = $('<%=id%>').getMenuAuthJSON();
			
			// 그룹권한 수정
			
			if(node.data.mode == 'group') {
				goUrl = goUrl + 'updateGroupAuth&groupID=' + node.data.id; 
			} else {
			// 개인권한 수정
				// 메뉴아이디 JSON array 가져오기 ```
				frm.authJSON.value = $('<%=id%>').getMenuAuthJSON();
				goUrl = goUrl + 'updateUserAuth&userID=' + node.data.id; 
				
			}

			if(node.data.mode == 'group' && node.data.id==100 )
			{
				alert('수퍼관리자그룹의 권한은 수정할 수 없습니다.');
				return;
			}
			
			if(node.data.mode != 'group' && node.getParent().data.id==100 )
			{
				alert('수퍼관리자의 권한은 수정할 수 없습니다.');
				return;
			}
		}
	}
	
	
	showBusy($('<%=id%>_userTreePanel'));
	showBusy($('<%=id%>_rightPanel'));
	
	nemoRequest.init({
		 url: goUrl
		, onSuccess: function(html,els,resHTML) {
			hideBusy($('<%=id%>_userTreePanel'));
			hideBusy($('<%=id%>_rightPanel'));
		}
	});
	nemoRequest.post(frm);

}

/***********************************************/
/* 한레코드 (계정,그룹) 선택 저장을 위한 메뉴 json arry 생성
/***********************************************/
$('<%=id%>').getMenuAuthJSON = function() {

	var json = [{submenu:[], auth:[], dbset:[]}];

	<%=id%>_MenuTree.root.recursive(function(){
		
		if(this.isRoot() || this.type == 'folder' || this.state.checked == 'unchecked' ) return;

		switch (this.data.mode) {
			case 'sub_menu' :
					json[0].submenu.push( this.data.id ); 
					break;
			case 'auth' :
					json[0].auth.push( this.data.id ); 
					break;
			case 'dbset' :
					json[0].dbset.push( this.data.id ); 
					break;
		}
		
	});

	return JSON.encode( json );

}

/***********************************************/
/* 복수 선택  (계정,그룹) 선택 저장을 위한 메뉴 json arry 생성
/***********************************************/
$('<%=id%>').unSelectAll = function() {

	<%=id%>_tree.root.recursive(function(){
		this['switch']('unchecked');
	});

	$('<%=id%>').checkedUserFnc(<%=id%>_tree);
		
	//node = <%=id%>_tree.getSelected();
	//<%=id%>_tree.fireEvent('select', [node]).fireEvent('selectChange', [node, true]);
	
	
}

/***********************************************/
/* 복수 선택  (계정,그룹) 선택 저장을 위한 메뉴 json arry 생성
/***********************************************/
$('<%=id%>').getCheckedMenuAuthJSON = function() {

	var json = [{insert_submenu:[], delete_submenu:[], insert_auth:[],delete_auth:[], insert_dbset:[], delete_dbset:[]}];

	// 재귀호출 하위 노드까지 전부 uncheck인지 판단. (그래야 진정한 uncheck)
	var isChkFunc = function(node) {
		if(!node.hasCheckbox) return 'noaction';
		
		if(node.state.checked == 'checked' || node.state.checked == 'partially') return 'insert';

		if(node.state.checked == 'group-nochecked') return 'noaction'

		// uncheck 일경우
		var childs = node.getChildren();
		for(var i=0; i < childs.length; i++ ) {
			if(isChkFunc( childs[i] ) == 'noaction')
				return 'noaction'; 
		}
		return 'delete';
	};

	<%=id%>_MenuTree.root.recursive(function(){
		
		if(this.isRoot() || this.type == 'folder' ) return;
		var action = '';

		switch (this.data.mode) {
			case 'sub_menu' :
					action = isChkFunc( this );
					if(action == 'insert') {
						json[0].insert_submenu.push( this.data.id );
					} else if(action == 'delete'|| action == 'noaction') {
						json[0].delete_submenu.push( this.data.id );
					}
					break;
			case 'auth' :
					action = isChkFunc( this );
					if(action == 'insert') {
						json[0].insert_auth.push( this.data.id );
					} else if(action == 'delete'|| action == 'noaction') {
						json[0].delete_auth.push( this.data.id );
					}
						 
					break;
			case 'dbset' :
					action = isChkFunc( this );
					if(action == 'insert') {
						json[0].insert_dbset.push( this.data.id );
					} else if(action == 'delete'|| action == 'noaction') {
						json[0].delete_dbset.push( this.data.id );
					}
					break;
		}
		
	});

	//alert(JSON.encode( json ));

	return JSON.encode( json );

}
/***********************************************/
/* 그룹/계정 트리 초기화
/***********************************************/
$('<%=id%>').userTree = function() {
	
	showBusy($('<%=id%>_userTreePanel'));
	try {
	
	<%=id%>_tree = new Mif.Tree({
		container: $('<%=id%>_userTreePanel'),
		forest: false,  // root node 표시
		initialize: function(){
			this.initCheckbox('deps');
			new Mif.Tree.KeyNav(this);

			new Mif.Tree.Drag(this, {
				onDrag: function(){
					//inject book inside book not allowed;
					if(this.where == 'after' || this.where == 'before') {
						this.where='notAllowed';
						return;
					}
					if(this.target && this.target.type=='folder' && this.current.type=='folder'){
						this.where='notAllowed';
						return;
					}
					
					if(this.target && (this.current.type == 'super_user' || this.current.type == 'system_user')){
						this.where='notAllowed';
						return;
					}
					
					
					if(this.target && this.target.type != 'folder'){
						this.where='notAllowed';
						return;
					}
					// 자신의 그룹에 드래그
					if(this.target && this.target.type == 'folder' && this.current.data.mode == 'user' && this.current.getParent().data.id == this.target.data.id){
						this.where='notAllowed';
						return;
					}
					// 수퍼 그룹에 드래그
					if(this.target && this.target.type == 'folder' && this.target.data.isAdmin){
						this.where='notAllowed';
						return;
					}
					
				},
				onStart: function(){
					//$('source').innerHTML=this.current.name;
				},
				onComplete: function(){
					// 드롭후 저장
					if(this.where != 'notAllowed') {
						nemoRequest.init( 
						{
							url: 'admin/usergroup/usergroup.do?method=changeUserGroupID&id=<%=id%>' 
						});
						nemoRequest.get({'groupID':this.target.data.id, 'userID':this.current.data.id});
					}	
				}
					
			});
			
		},
		types: {
			root: {
				openIcon: 'mif-tree-root-open-icon',
				closeIcon: 'mif-tree-root-open-icon'
			},
			folder:{
				openIcon: 'blue-open-icon',
				closeIcon: 'blue-close-icon'
			},
			user:{
				openIcon: 'person-icon',
				closeIcon: 'person-icon'
			},
			super_user:{
				openIcon: 'super_person-icon',
				closeIcon: 'super_person-icon'
			},
			system_user:{
				openIcon: 'system_person-icon',
				closeIcon: 'system_person-icon'
			},
			group_user:{
				openIcon: 'group_person-icon',
				closeIcon: 'group_person-icon'
			}
		},
		dfltType:'folder'
	});

	
	//tree.initSortable();
	<%=id%>_tree.load({	
		url: 'admin/usergroup/usergroup.do?method=treeView'
		,onLoad: function() {
			hideBusy($('<%=id%>_userTreePanel'));
			<%=id%>_tree.root.toggle();
		}	
	});

	

	// 체크박스를 클릭했을때 이벤트
	<%=id%>_tree.addEvent('checkboxclick', function(node,checked){
		
		$('<%=id%>').checkedUserFnc( this, node, checked );
		
	});
	
	<%=id%>_tree.addEvent('select',$('<%=id%>').selectUserFnc);

	// 사용자 다중선택 판별을 위하여
	<%=id%>_tree.firstCheck = true;

	} catch (exception) {
		hideBusy($('<%=id%>_userTreePanel'));
	
	}
	
	
}

/***********************************************/
/* 메뉴 트리 초기화
/***********************************************/
$('<%=id%>').menuTree = function() {

	try {	

	<%=id%>_MenuTree = new Mif.Tree({
		container: $('<%=id%>_rightPanel'),
		forest: false,  // root node 표시
		initialize: function(){
			this.initCheckbox('deps');
			new Mif.Tree.KeyNav(this);
		},
		types: {
			root: {
				openIcon: 'computer-icon',
				closeIcon: 'computer-icon'
			},
			folder:{
				openIcon: 'yellow-open-icon',
				closeIcon: 'yellow-close-icon'
			},
			menu:{
				openIcon: 'file-icon',
				closeIcon: 'file-icon'
			},
			auth_csv: {
				openIcon: 'csv-icon',
				closeIcon: 'csv-icon'
			},
			auth_query: {
				openIcon: 'query-icon',
				closeIcon: 'query-icon'
			},
			auth_direct: {
				openIcon: 'file-icon',
				closeIcon: 'file-icon'
			},
			auth_related: {
				openIcon: 'file-icon',
				closeIcon: 'file-icon'
			},
			dbset: {
				openIcon: 'dbset-icon',
				closeIcon: 'dbset-icon'
			},
			auth_write_mail: {
				openIcon: 'write-mail-icon',
				closeIcon: 'write-mail-icon'
			},
			auth_send_mail: {
				openIcon: 'send-mail-icon',
				closeIcon: 'send-mail-icon'
			},
			auth_write_sms: {
				openIcon: 'write-mail-icon',
				closeIcon: 'write-mail-icon'
			},
			auth_send_sms: {
				openIcon: 'send-mail-icon',
				closeIcon: 'send-mail-icon'
			}			
				
		},
		dfltType:'folder'
	});

	
	//tree.initSortable();
	<%=id%>_MenuTree.load({	
		url: 'admin/menu/menu.do?method=treeView'
		,onLoad: function() {
			<%=id%>_MenuTree.root.recursive(function(){
				this.toggle();
			});
		} 
	});

	

	<%=id%>_MenuTree.addEvent('checkboxclick', $('<%=id%>').selectMenuFnc);

	} catch (exception) {
	}

	
}		

/***********************************************/
/* 사용자 트리 체크
/***********************************************/
$('<%=id%>').checkedUserFnc = function(tree, checkedNode, checked) {

	var checkedCount = tree.getCheckedCount();


	if(<%=id%>_tree.firstCheck) { // 처음체크이면 다중 선택 시작
		<%=id%>_MenuTree.initGroupCheckbox();
	}
	
	// 체크가  하나도 없으면 사용자 선택으로..
	if(checkedCount == 0) {
		<%=id%>_tree.firstCheck = true;
		var node = tree.getSelected();
		if(node) { 
			tree.expandTo( node );
			tree.scrollTo( node );
			tree.select(node);
			$('<%=id%>').selectUserFnc(node); //셀렉트의 이동이 없었으므로 강제로 이벤트 콜
		}
		return;
	} else {
			

		<%=id%>_tree.firstCheck = false;
	}

	// 권한설정과 삭제가 체크한 노드대상이 된다.
	//1. 권한대상이름 변경
	$('<%=id%>_userName').set('text','선택된 자료');

	
	
}

/***********************************************/
/* 사용자 트리 선택
/***********************************************/
$('<%=id%>').selectUserFnc = function(node) {

	if(<%=id%>_tree.drag.draging) return; // 드래그 중이면 return
		
	var checkedCount = <%=id%>_tree.getCheckedCount(); 
	
	$('<%=id%>_userInfoPanel').empty();

	if( checkedCount == 0 ) { 
				
		$('<%=id%>_userName').set('text','');
	}
	
	if(node.isRoot()) { 
		return;
	}

	
		// 그룹, 사용자
		if( checkedCount == 0 ) {
			if(node.data.mode == 'user') {
				$('<%=id%>_userName').set('text',node.name);
			} else if(node.data.mode == 'group') {
				$('<%=id%>_userName').set('text',node.name+' 의 구성원이 추가될 때 복사될 권한 ');
			}
		}

		if(node.data.mode == 'user' )
			$('<%=id%>').viewUserInfo( node.data.id );

		// 시스템관리자 또는 어드민이면 메뉴화면을 감춘다
		//if(checkedCount == 0 && node.type != 'system_user' && !node.data.isAdmin) {
					
			<%=id%>_MenuTree.root.recursive( function(){ this['switch']('unchecked'); } );  // uncheck
			
			$('<%=id%>').viewMenuAuth( (node.data.mode == 'user') ? node.data.id: '_group_'+node.data.id);	
								
		
}

/***********************************************/
/* 메뉴권한  정보
/***********************************************/
$('<%=id%>').viewMenuAuth = function( userID ) {

	nemoRequest.init( 
	{
		//busyWindowId: $('<%=id%>_userInfoPanel'),  // busy 를 표시할 window
		//updateWindowId: $('<%=id%>_userInfoPanel'),  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'admin/usergroup/usergroup.do?method=userMenuAuth&id=<%=id%>', 
		//update: $('<%=id%>_userInfoPanel'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			if(resHTML.trim() == '' && scripts.trim() != '') { // 로그아웃 상태이면 return
				return;
			}

			try {
				var json
				try 
				{
					json = JSON.decode(resHTML);
					
				} catch (exception) {
					return;
				}
				var index = 0;
				
				//if(json.menu.length == 0) return;
	
				// 메뉴권한 재귀호출 함수
				var recursiveFnc = function() {
										
					if(index == json.menu.length) return;
					if(this.type=='root' || this.type=='folder') return;
					if(this.data.mode == 'sub_menu' && json.menu[index] == this.data.id && !this.data.hasAuth) {
						this['switch']('checked');
						index ++;
					};
				};
				<%=id%>_MenuTree.root.recursive(recursiveFnc); 
	
				//if(json.auth.length == 0) return;
				index = 0;
	
				// 기타권한 재귀호출 함수
				var recursiveFnc = function() {
					if(index == json.auth.length) return;
					if(this.type=='root' || this.type=='folder' || this.type == 'sub_menu') return;
					
					if(this.data.mode == 'auth' && json.auth[index] == this.data.id && !this.data.hasAuth) {
						this['switch']('checked');
						index ++;
					};
				};
				<%=id%>_MenuTree.root.recursive(recursiveFnc); 
				 
				//if(json.dbset.length == 0) return;
				index = 0;
	
				// db권한 재귀호출 함수
				var recursiveFnc = function() {
					if(index == json.dbset.length) return;
					if(this.type=='root' || this.type=='folder' || this.type == 'sub_menu') return;
	
					if(this.data.mode == 'dbset' && json.dbset[index] == this.data.id ) {
						this['switch']('checked');
						index ++;
					};
				};
				<%=id%>_MenuTree.root.recursive(recursiveFnc);
			} catch (excaption) {
			} 
			
		}
	});
	nemoRequest.get({'userID':userID});	

}



/***********************************************/
/* 사용자 정보
/***********************************************/
$('<%=id%>').viewUserInfo = function( userID ) {

	nemoRequest.init( 
	{
		//busyWindowId: $('<%=id%>_userInfoPanel'),  // busy 를 표시할 window
		updateWindowId: $('<%=id%>_userInfoPanel'),  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'admin/usergroup/usergroup.do?method=viewUser&id=<%=id%>', 
		update: $('<%=id%>_userInfoPanel'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.get({'userID':userID});	

}


/***********************************************/
/* 메뉴 트리 선택 이벤트
/***********************************************/
$('<%=id%>').selectMenuFnc = function(node) {
	
	if(node.data.id=='auth_send_mail'){
		if(node.state.checked=='checked')
			node.getNext()['switch']('checked');
	}

	if(node.data.id=='auth_write_mail'){
		if(node.state.checked=='unchecked' && node.getPrevious().state.checked=='checked')
		{
			alert('메일 발송 권한에는  메일 작성 권한이 포함되어야 합니다. \n\n메일 발송 권한을 해제한 후  적용하세요');
			node['switch']('checked');
		 	return;
		}
	}
}

/***********************************************/
/* 사용자 추가/수정창 열기
/***********************************************/
$('<%=id%>').editUserWindow = function( editMode ) {

	var userID = "";
	var groupID = "";
	var node = <%=id%>_tree.getSelected();
	var groupChange = true;
	if(editMode == "update" ) { // (트리에서 select된 요저가 수정할유저)
		if(node == null || node == <%=id%>_tree.root || node.data.mode == 'group') { // 선택한 노드가 없으면
			alert('수정할 사용자를 먼저 선택하세요');
			return;
		}
		groupChange = (node.type != 'super_user'); 
		userID = node.data.id;
	} else if (editMode == 'insert') { // 추가일경우 현재 선택한 그룹을 기본 그룹으로 정한다
		
		if(node != null && node != <%=id%>_tree.root) { // 선택한 노드가 있으면
			if(node.data.mode == 'user') { // 선택한 노드가 그룹이 아니라면
				groupID = node.getParent().data.id;
			} else {
				groupID = node.data.id;
			}
		}
	}

	nemoWindow(
		{
			'id': '<%=id%>_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 500,
			//height: $('mainColumn').style.height.toInt(),
			height: 450,
			title: '사용자 추가/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'admin/usergroup/usergroup.do?method=editUser&id=<%=id%>&userID='+encodeURI(userID)+'&defGroupID='+groupID+'&groupChange='+groupChange
		}
	);
	
}
/***********************************************/
/* 사용자 입력 화면에서 그룹셀렉트 변경시
/***********************************************/
$('<%=id%>').changeGroup = function() {

	var frm = $('<%=id%>_form');

	// 시스템그룹인데 일반계정이면
	if(frm.eGroupID.value == '100' && '23'.test(frm.eUserLevel.value) && frm.eUserLevel.value) {
		alert('그룹관리자 및 일반 계정은 수퍼관리자 그룹에 속할 수 없습니다');
		frm.eGroupID.setSelect(0);
		return;
	}   

	if(frm.eUserLevel.value == '1' && !frm.eGroupID.value) {
		frm.eGroupID.setSelect(1);
		return;
	}   
	
	if(frm.eUserLevel.value == '1' && frm.eGroupID.value != '100') {
		alert('수퍼관리자는 수퍼관리자 그룹에만 속할 수 있습니다');
		frm.eGroupID.setSelect(1);
		return;
	}   
	
	
}

/***********************************************/
/* 사용자 삭제
/***********************************************/
$('<%=id%>').deleteUser = function() {

	var checkedCount = <%=id%>_tree.getCheckedCount('group_user|user');
	var node = <%=id%>_tree.getSelected();
	var userArr = [];
	var groupArr = [];

	if(checkedCount == 0 && (!node || node.isRoot() || node.data.mode != 'user')) {
		alert('사용자를 삭제하시려면 체크박스에 체크를 하시거나 \n\n사용자를 마우스로 선택하신 후 다시 삭제버튼을 클릭해주세요');
		return; 
	}

	if(checkedCount > 0) {

		if(!confirm('선택하신 아이디와 동일한 아이디는 다시 생성할 수 없습니다.\n'+checkedCount+'명의 사용자를 정말로 삭제 하시겠습니까?')) return;
		<%=id%>_tree.root.recursive(function(){
			if(!this.isRoot() && this.data.mode == 'user' && this.state.checked == 'checked') {
				userArr.push( this.data.id );
				groupArr.push( this.getParent().data.id);
			}
		});
	} else {
		
		if(node.data.id == 'admin') {
			alert('최고 관리자는  삭제하실 수 없습니다');
			return;
		}
		
		if(!confirm('['+node.name+'] 사용자를 정말로 삭제 하시겠습니까?')) return;
		userArr.push( node.data.id );
		groupArr.push( node.getParent().data.id);
		
	}

	var userIDsJson = JSON.encode(userArr);
	var GroupIDsJson = JSON.encode(groupArr);
	
	$('<%=id%>_sform').IDs.value = userIDsJson;
	$('<%=id%>_sform').GroupIDs.value = GroupIDsJson;

	nemoRequest.init({
		busyWindowId: $('<%=id%>_userTreePanel')  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window
		, url: 'admin/usergroup/usergroup.do?id=<%=id%>&method=deleteUser'
		//, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
		}
	});
	nemoRequest.post($('<%=id%>_sform'));
		 

}

/***********************************************/
/* 사용자 저장버튼 클릭
/***********************************************/
$('<%=id%>').saveUserData = function( saveAndAppend ) {

	var frm = $('<%=id%>_form');
	var editMode = frm.eEditMode.value;

	if(frm.eCellPhone.value=="휴대폰번호는 '-'를 포함합니다")
	{		
		frm.eCellPhone.value = "";
		
	}
	if(frm.eSenderCellPhone.value=="휴대폰번호는 '-'를 포함합니다")
	{
		frm.eSenderCellPhone.value = "";
		
	}
	
	//필수입력 조건 체크
	if(!checkFormValue(frm)) {
		return;
	} 
	if(frm.eEmail.value && !CheckEmail(frm.eEmail.value)) {
		toolTip.showTipAtControl(frm.eEmail,'이메일 형식이 틀립니다');
		return;
	}

	if(frm.eCellPhone.value && !CheckTel(frm.eCellPhone.value)) {
		toolTip.showTipAtControl(frm.eCellPhone,'휴대전화 형식이 틀립니다');
		return;
	}
	if(frm.eUserPWD2.value != frm.eUserPWD.value) {
		toolTip.showTipAtControl(frm.eUserPWD2,'패스워드가 틀립니다');
		return;
	}
	for( var i = 0; i < frm.eUserID.value.length;i++)
	{
		var c = frm.eUserID.value.charCodeAt(i);
		if( !( (  0x61 <= c && c <= 0x7A ) || ( 0x41 <= c && c <= 0x5A ) || (  0x30 <= c && c <= 0x39 ) ) )
		{
			toolTip.showTipAtControl(frm.eUserID,'사용자아이디 는 영문 또는 숫자만 입력해야 합니다 ');
			return;
		}

	}
	
	

	if(editMode == 'insert') {
		goUrl = 'admin/usergroup/usergroup.do?id=<%=id%>&method=insertUser&saveAndAppend='+saveAndAppend;
	} else {
		goUrl = 'admin/usergroup/usergroup.do?id=<%=id%>&method=updateUser&saveAndAppend='+saveAndAppend;
	}
	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: goUrl
		//, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			//$('tips-panel').loadContent();
		}
	});
	nemoRequest.post(frm);
	
}
/***********************************************/
/* 그룹 삭제
/***********************************************/
$('<%=id%>').deleteGroup = function() {

	var checkedCount = <%=id%>_tree.getCheckedCount('folder');
	var node = <%=id%>_tree.getSelected();
	var groupArr = [];

	if(checkedCount == 0 && (!node || node.isRoot() || node.data.mode != 'group')) {
		alert('그룹을 삭제하시려면 체크박스에 체크를 하시거나 \n\n그룹을 마우스로 선택하신 후 다시 삭제버튼을 클릭해주세요');
		return; 
	}

	if(checkedCount == 0 && !node && node.data.isAdmin) {
		alert(node.name+' 그룹은 삭제하실 수 없습니다');
		return;
	}

	if(checkedCount > 0) {
		var stop = false;
		<%=id%>_tree.root.recursive(function(){
			if(!stop && !this.isRoot() && this.data.mode == 'group' && this.state.checked == 'checked') {
				if(this.getChildren().length > 0) {
					alert('사용자가 속한 그룹은 삭제할 수 없습니다\n\n사용자를 먼저 삭제하세요');
					stop = true;
				}
			}
		});

		if(stop) return;
		
		if(!confirm('선택하신 '+checkedCount+'개의 그룹을 정말로 삭제 하시겠습니까?')) return;
		<%=id%>_tree.root.recursive(function(){
			if(!this.isRoot() && this.data.mode == 'group' && this.state.checked == 'checked')
				groupArr.push( this.data.id );
		});
	} else {

		if(node.getChildren().length > 0) {
			alert('사용자가 속한 그룹은 삭제할 수 없습니다\n\n사용자를 먼저 삭제하세요');
			return;
		}

		if(!confirm('['+node.name+'] 그룹을 정말로 삭제 하시겠습니까?')) return;
		groupArr.push( node.data.id );
		
	}

	var GroupIDsJson = JSON.encode(groupArr);
	$('<%=id%>_sform').IDs.value = GroupIDsJson;


	nemoRequest.init({
		busyWindowId: $('<%=id%>_userTreePanel')  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window
		, url: 'admin/usergroup/usergroup.do?id=<%=id%>&method=deleteGroup'
		//, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
		}
	});
	nemoRequest.post($('<%=id%>_sform'));
	

		 

}
/***********************************************/
/* 그룹 추가/수정창 열기
/***********************************************/
$('<%=id%>').editGroupWindow = function( groupID ) {

	if(!groupID) { // groupID가없으면 수정 (트리에서 select된 그룹이 수정할 그룹)
		var node = <%=id%>_tree.getSelected();
		if(node == null || node == <%=id%>_tree.root) { // 선택한 노드가 없으면
			alert('수정할 그룹을 먼저 선택하세요');
			return;
		}
		
		if(node.data.mode == 'user') { // 선택한 노드가 그룹이 아니라면
			//groupID = node.getParent().data.id;
			alert('수정할 그룹을 선택하세요');
			return;
		} else {
			groupID = node.data.id;
		}
	}

	nemoWindow(
		{
			'id': '<%=id%>_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 400,
			//height: $('mainColumn').style.height.toInt(),
			height: 180,
			title: '그룹 추가/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'admin/usergroup/usergroup.do?method=editGroup&id=<%=id%>&groupID='+groupID
		}
	);
	
}

/***********************************************/
/* 그룹 저장버튼 클릭
/***********************************************/
$('<%=id%>').saveGroupData = function() {

	var frm = $('<%=id%>_form');
	var editMode = 'edit';
	if(frm.elements['eGroupID'].value == '0') editMode = 'insert';
	
	//필수입력 조건 체크
	if(!checkFormValue(frm)) {
		return;
	}
	//copyForm( $('<%=id%>_rform'), frm );

	if(editMode == 'insert') {
		goUrl = 'admin/usergroup/usergroup.do?id=<%=id%>&method=insertGroup';
	} else {
		goUrl = 'admin/usergroup/usergroup.do?id=<%=id%>&method=updateGroup';
	}
	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: goUrl
		//, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
		}
	});
	nemoRequest.post(frm);
	
}
/***********************************************/
/* 제한 해제 버튼 클릭
/***********************************************/
$('<%=id%>').derestrict = function() {
	var frm = $('<%=id%>_form');
	if(!confirm('사용 제한을 해제 하시겠습니까?')) return;
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window
		, url: 'admin/usergroup/usergroup.do?id=<%=id%>&method=derestrict'
		, onSuccess: function(html,els,resHTML) {
			$('<%=id%>_derestrict').setStyle('display','none');
			$('<%=id%>_derestrictLine').setStyle('display','none');
			$('<%=id%>_viewDerestrict').setStyle('display','none');
		}
	});
	nemoRequest.post(frm);
}

/***********************************************/
/* 핸드폰 번호 
/***********************************************/
$('<%=id%>').deleteValue = function ( str ) {	
	
	var frm = $('<%=id%>_form');

	str.value = '';


	
}
/***********************************************/
/* 새로고침
/***********************************************/

$('<%=id%>').refreshData = function() {
	$('<%=id%>').userTree(); 
	$('<%=id%>').menuTree(); 

}

/***********************************************/
/* 초기화
/***********************************************/

window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').userTree(); 
	$('<%=id%>').menuTree(); 
});

	
</script>

<%}else{%>
<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td align="center" valign="middle">
			<center><img src="../../images/error.jpg" /></center>
		</td>
	</tr>
</table>
<%}%>