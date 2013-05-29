<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.CheckServerIP" %>
<%@ page import="web.common.util.SystemExec"%>
<%@ page import="web.common.util.WebTail"%>
<%@ page import="web.common.util.PropertiesUtil"%>
<%@ page import="web.common.util.DateUtils"%>
<%@ page import="web.common.util.CheckServer"%>
<%@ page import="web.admin.systemset.control.SystemSetControllerHelper"%>
<%@ page import="web.admin.systemset.service.SystemSetService"%>
<%@ page import="web.common.util.*"%>
<%@ page import="java.io.*" %>
<%
	
	String id = request.getParameter("id");
	String sMode = request.getParameter("mode");
	SystemSetService service = SystemSetControllerHelper.getUserService(application);
	String localServerIP = service.getSystemSetInfo("1","sendIP");
	String domainName = service.getSystemSetInfo("1","domainName");
	
	String isAdmin = LoginInfo.getIsAdmin(request);

	if(isAdmin.equals("Y")){ // 관리자 계정이 아닐 경우 URL 접근 시 접근불가 페이지 출력	
	
	if(sMode.equals("kisa")) {
		String kisaURL = service.getSystemSetInfo("1","kisarbl");
	/***********************************************/
	/* KISA RBL 관리 : MAIN 화면
	/***********************************************/
%>
		
	
<%@page import="web.common.util.DateUtils"%><tr>
		<td>
			<a href="https://www.kisarbl.or.kr/" target="_blank">
				<img src="images/kisa-logo.jpg" width="178" height="55">
			</a>
		</td>
		
	</tr>
	<tr>
		<td>
			<a href="https://www.kisarbl.or.kr/" target="_blank" title="한국정보보호진흥원 홈페이지 바로가기"><b>KISA-RBL</b></a>은 메일서버를 운영하는 누구나 손쉽게 효과적으로 스팸을 차단하는데 이용할 수 있도록<br>
			<b>한국정보보호진흥원(KISA)</b>에서 무료로 관리ㆍ운영하여 제공하는 실시간 스팸 차단 리스트입니다.<br>
			국내외로부터 스팸정보를 실시간으로 취합하고 이를 다양한 기준에 따라 분석한 결과,스팸전송에<br>
			 관련된 것으로 확인된 IP들을 리스트로 생성하여 1시간 단위로 제공합니다.<br>
			 대부분의 국내 대형 포털에서는  KISA RBL을 참고하여 해당 IP의 메일 수신 허용/차단을 관리하므로<br>
			 LIST에 등록 되지 않도록 화이트 도메인 등록을 꼭 해주셔야 합니다.<br><br>
		</td>
	</tr>
	<tr>
		<td>
			<table>
				<tr>
					<td width="30px" align="center">
						<b>IP</b> 
					</td>
					<td>
						<b>:</b> <input type="text" id="ip" name="ip" value="<%=localServerIP%>" title="KISA-RBL 등록 여부를 확인 할 IP를 입력하세요." size="50"/>
					</td>
					<td>
						<div class="btn_b"><a href="javascript:$('<%=id%>').checkRBL()" title="팝업 창으로 KISA-RBL 등록여부 확인 결과가 열립니다."><span>확인</span></a></div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<!-- 
	<tr>
		<td>
			<table>
				<tr>
					<td width="60px">
						<b>도메인명</b> 
					</td>
					<td>
						<b>:</b> <input type="text" id="domain" name="domain" value="<%=domainName%>" title="SPF 출판을 확인할 DOMAIN을 입력하세요." size="50"/>
					</td>
					<td>
						<div class="btn_b"><a href="javascript:$('<%=id%>').checkSPF()" title="하단에 SPF 출판 확인 결과가 열립니다."><span>확인</span></a></div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table border="0" cellspacing="0" cellpadding="0" width="600px" >
				<tbody id="<%=id%>_result_content">
				</tbody>
			</table>
		</td>
	</tr>
	 -->
	<script type="text/javascript">
		/***********************************************/
		/* KISA RBL IP 확인 버튼 클릭 - 팝업
		/***********************************************/
		$('<%=id%>').checkRBL = function() {
			if(document.<%=id%>_form.ip.value ==""){
				alert("IP를 입력하세요");
				return;
			}else{
				var url ="<%=kisaURL%>="+document.<%=id%>_form.ip.value;
				window.open(url ,"popup","menubar=0,scrollbars=yes, resizable=yes, width=950, height=700");
			}
		}
		/***********************************************/
		/* SPF 출판  확인 버튼 클릭 
		/***********************************************/
		$('<%=id%>').checkSPF= function() {
			if(document.<%=id%>_form.domain.value ==""){
				alert("도메인을 입력하세요");
				return;
			}else{
				nemoRequest.init( 
						{
							busyWindowId: '<%=id%>',  
							updateWindowId: '<%=id%>',  
							url: 'admin/manager/manager_proc.jsp?id=<%=id%>&mode=checkspf&domain='+document.<%=id%>_form.domain.value, 
							update: $('<%=id%>_result_content'), 
							onSuccess: function(html,els,resHTML,scripts) {
							}
						});
						nemoRequest.post($('<%=id%>_rform'));
			    }
		}
	</script>
<%}else if(sMode.equals("checkspf")) {
	/***********************************************/
	/* KISA RBL 관리 : SPF 출판  확인 결과
	/***********************************************/
	String domain = request.getParameter("domain");
	String[] cmd = { "nslookup","-type=txt", domain};
	String procLog = SystemExec.getCMD(cmd);  
%>
	<tr>
		<td>
			<div id="files-panel_handle" class="horizontalHandle" style="display: block;">
				<div id="files-panel_handle_icon" class="handleIcon"/>
				</div>
			</div>
			<div class="blue-border" style="height:100%;margin-top:8px">
				<div class="panel-header">
					<div class="panel-header-ball" >
						<div class="panel-expand icon16" style="width: 16px; height: 16px;"/>
						</div>
					</div>
					<div class="panel-headerContent">
						<h2 id="panel2_title">[ <%=domain %> ] SPF 확인 결과 </h2>
					</div>
				</div>
				<div id="<%=id%>_userInfoPanel" style="padding:3px">
					<table>
						<tr>
							<td>
								<%=procLog %>
							</td>
						</tr>
					</tabe>
				</div>	
			</div>
		</td>
	</tr>
<%} else if(sMode.equals("cblrbl")) {
	/***********************************************/
	/* CBL / RBL 관리 : MAIN 화면
	/***********************************************/
%>
	<tr> 
		<td>
			<img src="images/sh_logo1.jpg" width="260" height="32"><br><br>
			<!-- <img src="images/cbl-logo.gif" width="170" height="64"><br><br>-->
		    <!-- <img src="images/rbl-logo.jpg" width="164" height="36"><br><br>-->
		</td>
	</tr>
	<tr>
		<td>
			<!-- <b><a href ="http://cbl.abuseat.org/lookup.cgi" target='_blank' title="CBL 바로가기">CBL</a> / <a href ="http://dnsbl.net.au/lookup/" target='_blank' title="RBL 바로가기">RBL</a></b>은 스팸 지수가 높은 IP를 수집하는 싸이트 입니다.<br>
			<b><a href ="http://cbl.abuseat.org/lookup.cgi" target='_blank' title="CBL 바로가기">CBL</a> / <a href ="http://dnsbl.net.au/lookup/" target='_blank' title="RBL 바로가기">RBL</a></b>에 IP가 등록되는 것 자체만으로 차단 되는 것은 아니지만, 각 메일 수신서버의  <br>
			스팸차단 솔루션이 <b><a href ="http://cbl.abuseat.org/lookup.cgi" target='_blank' title="CBL 바로가기">CBL</a> / <a href ="http://dnsbl.net.au/lookup/" target='_blank' title="RBL 바로가기">RBL</a></b>을 참고하여 해당 IP의 메일 수신 허용/차단을 관리하므로<br> 
			<b><a href ="http://cbl.abuseat.org/lookup.cgi" target='_blank' title="CBL 바로가기">CBL</a> / <a href ="http://dnsbl.net.au/lookup/" target='_blank' title="RBL 바로가기">RBL</a></b>에 IP가 등록되면 대다수 사이트의	 대량메일 발송이 완전 차단되게 됩니다.<br><br>
			<b><a href ="http://cbl.abuseat.org/lookup.cgi" target='_blank' title="CBL 바로가기">CBL</a></b>은 스팸 지수가 높은 IP를 수집하는 싸이트 입니다.<br>
			<b><a href ="http://cbl.abuseat.org/lookup.cgi" target='_blank' title="CBL 바로가기">CBL</a></b>에 IP가 등록되는 것 자체만으로 차단 되는 것은 아니지만, 각 메일 수신서버의  <br>
			스팸차단 솔루션이 <b><a href ="http://cbl.abuseat.org/lookup.cgi" target='_blank' title="CBL 바로가기">CBL</a> </b>을 참고하여 해당 IP의 메일 수신 허용/차단을 관리하므로<br> 
			<b><a href ="http://cbl.abuseat.org/lookup.cgi" target='_blank' title="CBL 바로가기">CBL</a></b>에 IP가 등록되면 대다수 사이트의	 대량메일 발송이 완전 차단되게 됩니다.<br><br>-->
			<b><a href ="http://www.spamhaus.org/lookup/" target='_blank' title="SPAMHAUS 바로가기">SPAMHAUS</a></b>은 스팸 지수가 높은 IP를 수집하는 싸이트 입니다.<br>
			<b><a href ="http://www.spamhaus.org/lookup/" target='_blank' title="SPAMHAUS 바로가기">SPAMHAUS</a></b>에 IP가 등록되는 것 자체만으로 차단 되는 것은 아니지만, 각 메일 수신서버의  <br>
			스팸차단 솔루션이 <b><a href ="http://www.spamhaus.org/lookup/" target='_blank' title="SPAMHAUS 바로가기">SPAMHAUS</a> </b>을 참고하여 해당 IP의 메일 수신 허용/차단을 관리하므로<br> 
			<b><a href ="http://www.spamhaus.org/lookup/" target='_blank' title="SPAMHAUS 바로가기">SPAMHAUS</a></b>에 IP가 등록되면 대다수 사이트의	 대량메일 발송이 완전 차단되게 됩니다.<br><br>
		</td>
	</tr>
	<tr>
		<td>
			<table>
				<tr>
					<td width="30px" align="center">
						<b>IP</b> 
					</td>
					<td>
						<b>:</b> <input type="text" id="ip" name="ip" value="<%=localServerIP%>" size="50" title="SPAMHAUS의 스팸 리스트 등록  여부를 확인 할 IP를 입력하세요."/>
					</td>
					<td>
						<div class="btn_b"><a href="javascript:$('<%=id%>').check_ip('cbl')" title="하단에 SPAMHAUS의 스팸 리스트 등록 여부 확인 결과가 열립니다."><span>확인</span></a></div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table border="0" cellspacing="0" cellpadding="0" width="600px" >
				<tbody id="<%=id%>_result_content">
				</tbody>
			</table>
		</td>
	</tr>
	<script type="text/javascript">
		/***********************************************/
		/* CBL / RBL 확인 버튼 클릭
		/***********************************************/
		$('<%=id%>').check_ip = function( type ) {
			if(document.<%=id%>_form.ip.value ==""){
				alert("IP를 입력하세요");
				return;
			}else{
				nemoRequest.init( 
				{
					
					busyWindowId: '<%=id%>',  
					updateWindowId: '<%=id%>',  
					url: 'admin/manager/manager_proc.jsp?id=<%=id%>&mode=checkresult&type='+type+'&ip='+document.<%=id%>_form.ip.value, 
					update: $('<%=id%>_result_content'), 
					onSuccess: function(html,els,resHTML,scripts) {
					}
				});
				nemoRequest.post($('<%=id%>_rform'));
			}
		}
	</script>
<%}else if(sMode.equals("checkresult")) {
	/***********************************************/
	/* CBL / RBL 관리 : 결과 확인
	/***********************************************/
	String sType = request.getParameter("type");
	String checkIP = request.getParameter("ip");
	int cbl = CheckServerIP.checkSpamHouse(checkIP);
	String cblURL = service.getSystemSetInfo("1","cbl");
%>
	<tr>
		<td>
			<div id="files-panel_handle" class="horizontalHandle" style="display: block;">
				<div id="files-panel_handle_icon" class="handleIcon"/>
				</div>
			</div>
			<div class="blue-border" style="height:208px;margin-top:8px">
				<div class="panel-header">
					<div class="panel-header-ball" >
						<div class="panel-expand icon16" style="width: 16px; height: 16px;"/>
						</div>
					</div>
					<div class="panel-headerContent">
						<h2 id="panel2_title">SPAMHAUS 결과 확인 </h2>
					</div>
				</div>
				<div id="<%=id%>_userInfoPanel" style="padding:3px">
					<%if(cbl == 1){%>	
						<table>
							<tr>
								<td>
									<img src="images/emoticon_grin.png" align="top"> <b>SPAMHAUS</b>에 등록 되지 않았습니다.
								</td>
								<td>
									 <div class="btn_b"><a href ="<%=cblURL%><%=checkIP%>" target='_blank' title="팝업창으로 SPAMHAUS 확인 결과 화면이 열립니다."><span>결과확인</span></a></div>
								</td>
							</tr>
						</tabe>
			  		<%}else if(cbl == 2){ %>
			  			<table>
							<tr>
								<td>
									<img src="images/emoticon_unhappy.png" align="top"> <font color = "red"><b>SPAMHAUS</b> 에 등록 되었습니다.</font><br>
								</td>
							</tr>
							<tr>
								<td>
									 <img src="images/house.png" align="top"> <a href ="<%=cblURL%><%=checkIP%>" target="_blank" title="스팸 리스트 해제 요청 하러 가기"> http://cbl.abuseat.org/lookup.cg</a>에서 등록 해지를 하시기 바랍니다.<br>
								</td>
							</tr>
						</tabe>
			  		<%}else{ %>
			  			<table>
							<tr>
								<td>
									 <img src="images/emoticon_unhappy.png" align="top"> SPAMHAUS 결과 확인에 실패 하였습니다. (IP : <%=checkIP%> )<br>
			  						 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;IP정보가 정상적으로 입력 되어있는지 확인해 주십시오.
								</td>
							</tr>
							<tr>
								<td>
									<div class="btn_b" style="float:right;"><a href ="<%=cblURL%><%=checkIP%>" target='_blank' title="팝업창으로 SPAMHAUS 확인 결과 화면이 열립니다."><span>결과확인</span></a></div>
								</td>
							</tr>
						</tabe>
			  		<%} %>
				</div>	
			</div>
		</td>
	</tr>
<%} else if(sMode.equals("engine")) {
	/***********************************************/
	/* Engine 관리 : MAIN 화면
	/***********************************************/
%>
	<tr> 
		<td>
			<h2>Engine 관리</h2>
		</td>
	</tr>
	<tr> 
		<td height="20"></td>
	</tr>
	<tr> 
		<td>
			※ ThundweMail 5.0  발송 엔진의 상태 확인 및 시작 / 정지를 하실 수 있습니다.<br>
		           ※ 엔진이 정지 상태에 있을 경우 메일이 발송 되지 않습니다.<br>
		    &nbsp;<font color='red'>* 발송 중 엔진을 정지 시키면 에러가 발생 할 수 있습니다. </font><br>
		          ※ 상태 표시 정보<br> 
		    &nbsp; <font color="#BB66D4">◎</font> 동작중 :&nbsp; <img src="images/spinner.gif" align="top"><br>
		    &nbsp; <font color="#BB66D4">◎</font> 정&nbsp;&nbsp;&nbsp;&nbsp;지 :&nbsp; <img src="images/spinner-placeholder-red.gif" align="top"><br>
		</td>
	</tr>
	<tr> 
		<td height="10"></td>
	</tr>
	<tr>
		<td>
			<div style="height:25px">
				<div class="btn_b" style="float:right"><a href="javascript:$('<%=id%>').webTail('web')" style ="cursor:pointer" title="팝업으로 로그 확인 화면이 열립니다."><span>WEB 로그 확인</span></a></div>
				<input type="hidden" id="<%=id %>_web_logPath" value="">
				<input type="hidden" id="<%=id %>_web_logName" value="">
			</div>
		</td>
	</tr>
		<tr> 
		<td height="3"></td>
	</tr>
	<tr> 
		<td>
			<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="600px" >
			<thead>
				<tr>
					<th style="height:30px;width:40px">ID</th>
					<th style="height:30px;">엔진명</th>
					<th style="height:30px;width:140px">설명</th>
					<th style="height:30px;width:50px">엔진상태</th>
					<th style="height:30px;width:50px">오류발생수</th>
					<th style="height:30px;width:150px">동작</th>
				</tr>
			</thead>
				<tbody id="<%=id%>_engine_grid_content">
				</tbody>
			</TABLE>	
		</td>
	</tr>
	<tr>
		<td>
			<table border="0" cellspacing="0" cellpadding="0" width="600px" >
				<tbody id="<%=id%>_result_content">
				</tbody>
			</table>
		</td>
	</tr>

	<script type="text/javascript">
		/***********************************************/
		/* 동작 버튼 클릭 (재시작/시작/정지)
		/***********************************************/
		$('<%=id%>').enginStartStop = function( status, engine, engineid ) {
			nemoRequest.init( 
			{
				
				updateWindowId: '<%=id%>',  
				url: 'admin/manager/manager.do?id=<%=id%>&method=checkSend&status='+status+'&engine='+engine, 
				update: $('<%=id%>_result_content'), 
				onSuccess: function(html,els,resHTML,scripts) {
					
						$('<%=id%>').engineStatusupdate( status, engineid );
					 
				}
			});
			nemoRequest.post($('<%=id%>_rform'));
		}
		/***********************************************/
		/* 로그확인 버튼 클릭 
		/***********************************************/
		$('<%=id%>').webTail = function( engine ) {
			new MochaUI.Window({
				id: '<%=id%>'+engine,
				width: 650,
				height: $('mainColumn').style.height.toInt(),
				title: 'VIEW LOG',
				loadMethod: 'xhr',
				contentURL: 'admin/manager/manager_proc.jsp?id=<%=id%>&mode=tail&engine='+engine+'&logPath='+$('<%=id %>_'+engine+'_logPath').value+'&logName='+$('<%=id %>_'+engine+'_logName').value
			});
		}

		$('<%=id%>').engineList = function () {

			var frm = $('<%=id%>_sform');

			nemoRequest.init( 
			{
				
				updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

				url: 'admin/manager/manager.do?id=<%=id%>&method=list', 
				update: $('<%=id%>_engine_grid_content'), // 완료후 content가 랜더링될 element
				onSuccess: function(html,els,resHTML,scripts) {
					
				}
			});
			nemoRequest.post($('<%=id%>_rform'));
			<%=id%>ServerTimer_list = $('<%=id%>').showState.periodical(10000);
			MochaUI.Windows.instances.get('<%=id%>').addEvent('onClose',function() { $clear(<%=id%>ServerTimer_list) } );
			//$('<%=id%>').chkState();
		}
		$('<%=id%>').engineStatusupdate = function ( status, engineid ) {

			var frm = $('<%=id%>_sform');
			nemoRequest.init( 
			{
				
				updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

				url: 'admin/manager/manager.do?id=<%=id%>&method=enginStatusUpdate&engineID='+engineid+'&engineStatus='+status, 
				update: $('<%=id%>_engine_grid_content'), // 완료후 content가 랜더링될 element
				onSuccess: function(html,els,resHTML,scripts) { 
					
				}
			});
			nemoRequest.post($('<%=id%>_rform'));
			
		}

		$('<%=id%>').chkState = function()
		{
			setInterval("$('<%=id%>').showState()", 10000);
		}
		var els = null;
		var httpRequest = null;

		$('<%=id%>').showState = function() {
			
			var temp = document.getElementById("<%=id%>_engine_grid_content");
			var list = temp.childNodes;
			var json = [{engine_id:[], server_ip:[]}];
			for(var i=0;i<list.length;i++)
			{
				els = list.item(i);
				if(els.nodeName=='TR')
				{	
					json[0].engine_id.push(els.getAttribute("engine_id"));
					json[0].server_ip.push(els.getAttribute("server_ip"));
				}	
			}
			$('<%=id%>').sendRequest("returnEngineState.jsp", "engine_id="+JSON.encode( json ), $('<%=id%>').callbackFunction, "GET");
			
		}


		$('<%=id%>').callbackFunction = function() {
			
			if(httpRequest.readyState==4)
			{
				if(httpRequest.status==200)
				{			
					var st = eval("("+httpRequest.responseText+")");
					for(var i=0;i<st[0].engine_id.length;i++)
					{
						var tagDiv = document.getElementById("<%=id%>_eEngineImg"+st[0].engine_id[i]);
						var tagTr = document.getElementById("<%=id%>_eEngine"+st[0].engine_id[i]);
						var t = tagDiv.childNodes;
						for(var y =0; y < t.length ;y++)
						{	var stemp = t.item(y);
							if(tagTr.getAttribute("state")!= st[0].state[i])
							{
								if(stemp.nodeName=='IMG')
								{
									var tagImg = stemp;
									if(st[0].state[i]=='1')	
									{
										tagImg.setAttribute("src","images/spinner.gif");					
										tagImg.setAttribute("title","정상동작");	
										$('<%=id%>_eStop'+st[0].engine_id[i]).setStyle('display','block');			

										
										
									}
									else if(st[0].state[i]=='2')
									{
										tagImg.setAttribute("src","images/spinner-placeholder-red.gif");					
										tagImg.setAttribute("title","정지상태");
										$('<%=id%>_eStop'+st[0].engine_id[i]).setStyle('display','none');							
										
											
										
									}
									toolTip.init(tagDiv);
									tagTr.setAttribute("state",st[0].state[i]); 
									var tagDiv_ErrorCount = document.getElementById("<%=id%>_eEngineError"+st[0].engine_id[i]);							
									tagDiv_ErrorCount.innerHTML=st[0].errorCount[i]; 
									tagDiv_ErrorCount.setAttribute("title","최종 확인일 : "+st[0].state[i]); 
									toolTip.init(tagDiv_ErrorCount);
								}
							}							
						}
					}
				}	
			}	
		}

		

		$('<%=id%>').getXMLHttpRequest= function(){
			if (window.ActiveXObject) {
				try {
					return new ActiveXObject("Msxml2.XMLHTTP");
				} catch(e) {
					try {
						return new ActiveXObject("Microsoft.XMLHTTP");
					} catch(e1) { return null; }
				}
			} else if (window.XMLHttpRequest) {
				return new XMLHttpRequest();
			} else {
				return null;
			}
		}


		$('<%=id%>').sendRequest = function(url, params, callback, method) {
			
			httpRequest = $('<%=id%>').getXMLHttpRequest();
			
			var httpMethod = method ? method : 'GET';
			if (httpMethod != 'GET' && httpMethod != 'POST') {
				httpMethod = 'GET';
			}
			var httpParams = (params == null || params == '') ? null : params;
			var httpUrl = url;
			if (httpMethod == 'GET' && httpParams != null) {
				httpUrl = httpUrl + "?" + httpParams;
			}
			
			httpRequest.open(httpMethod, httpUrl, true);
			httpRequest.setRequestHeader(
				'Content-Type', 'application/x-www-form-urlencoded');
			httpRequest.onreadystatechange = callback;

			httpRequest.send(httpMethod == 'POST' ? httpParams : null);

		}
		
		/* 화면 표시 */
		window.addEvent("domready",function () {
			$('<%=id%>').engineList(); 
		});
		
	</script>
<%} else if(sMode.equals("enginelist")) {
	/***********************************************/
	/* Engine 관리 : MAIN 화면
	/***********************************************/
%>
				<c:forEach items="${engineList}" var="engine">
					<TR id="<%=id%>_eEngine<c:out value="${engine.engineID}"/>" class="tbl_tr" style="height:30px" engineName="<c:out value="${engine.engineName}"/>" server_ip="<c:out value="${engine.serverIP}"/>" engine_id="<c:out value="${engine.engineID}"/>" state="${engine.engineStatus}">
						<TD class="tbl_td"><c:out value="${engine.engineID}"/></TD>
						<TD class="tbl_td"><div title="Server IP : <c:out value="${engine.serverIP}"/>"><c:out value="${engine.engineName}"/></div></TD>
						<TD class="tbl_td"><c:out value="${engine.engineDesc}"/></TD>
						<TD class="tbl_td">
						<div id="<%=id%>_eEngineImg<c:out value="${engine.engineID}"/>" >
							<c:if test="${engine.engineStatus == 1}">
								<img src="images/spinner.gif" align="top" title="정상동작">
							</c:if>
							<c:if test="${engine.engineStatus == 2}">
								<img src="images/spinner-placeholder-red.gif" align="top" title="정지상태">
							</c:if>
						</div>
						</TD>
						<TD><div id="<%=id%>_eEngineError<c:out value="${engine.engineID}"/>" title="최종 확인일 : <c:out value="${engine.updateDate}"/>"><c:out value="${engine.errorCount}"/></div></TD>
						<TD class="tbl_td">
							<div id="<%=id%>_eStart_Stop<c:out value="${engine.engineID}"/>" style="height:20px">
								<div class="btn_green"><a href="javascript:$('<%=id%>').enginStartStop('restart','<c:out value="${engine.engineName}"/>','<c:out value="${engine.engineID}"/>')" style ="cursor:pointer" title="<c:out value="${engine.engineName}"/> 엔진을 재시작합니다."><span>시작</span></a></div>
								<c:if test="${engine.engineStatus == 1}">
									<div id="<%=id%>_eStop<c:out value="${engine.engineID}"/>" class="btn_r" style="display:block"><a href="javascript:$('<%=id%>').enginStartStop('stop','<c:out value="${engine.engineName}"/>','<c:out value="${engine.engineID}"/>')" style ="cursor:pointer" title="<c:out value="${engine.engineName}"/> 엔진을 정지합니다."><span>정지</span></a></div>
								</c:if>
								<c:if test="${engine.engineStatus == 2}">
									<div id="<%=id%>_eStop<c:out value="${engine.engineID}"/>" class="btn_r" style="display:none"><a href="javascript:$('<%=id%>').enginStartStop('stop','<c:out value="${engine.engineName}"/>','<c:out value="${engine.engineID}"/>')" style ="cursor:pointer" title="<c:out value="${engine.engineName}"/> 엔진을 정지합니다."><span>정지</span></a></div>
								</c:if>
								<div class="btn_b" style="float:right"><a href="javascript:$('<%=id%>').webTail('<c:out value="${engine.engineName}"/>')" style ="cursor:pointer" title="팝업으로 로그 확인 화면이 열립니다."><span>로그확인</span></a></div>
							</div>
							<input type="hidden" id="<%=id %>_<c:out value="${engine.engineName}"/>_logPath" value="<c:out value="${engine.logPath}"/>">
							<input type="hidden" id="<%=id %>_<c:out value="${engine.engineName}"/>_logName" value="<c:out value="${engine.logName}"/>">
						</TD>
					</TR>
				</c:forEach>
<%}else if(sMode.equals("enginStartStop")) {
	/***********************************************/
	/* Engine 관리 : Engine 시작/정지 및 결과 확인 
	/***********************************************/
	String status = request.getParameter("status");
	
	String engine = request.getParameter("engine");

	String result ="";
	String engine_name =""; 
	
		if(status.equals("restart")){
			result = web.common.util.TmInstaller.stopServiceReturn(engine);
			result += web.common.util.TmInstaller.startServiceReturn(engine);
		}else if(status.equals("start")){
			result = web.common.util.TmInstaller.startServiceReturn(engine);	
		}else if(status.equals("stop")){
			result = web.common.util.TmInstaller.stopServiceReturn(engine);	
		}
	

	
%>
	<tr>
		<td>
			<div id="files-panel_handle" class="horizontalHandle" style="display: block;">
				<div id="files-panel_handle_icon" class="handleIcon"/>
				</div>
			</div>
			<div class="blue-border" style="height:208px;margin-top:8px">
				<div class="panel-header">
					<div class="panel-header-ball" >
						<div class="panel-expand icon16" style="width: 16px; height: 16px;"/>
						</div>
					</div>
					<div class="panel-headerContent">
						<h2 id="panel2_title">발송엔진 <%=status %></h2>
					</div>
				</div>
				<div id="<%=id%>_userInfoPanel" style="padding:3px">
					<table>
						<tr>
							<td>
								<%=engine %>
							</td>
						</tr>
						
						<tr>
							<td>
								<%=result %>
							</td>
						</tr>
						
					</tabe>
				</div>	
			</div>
		</td>
	</tr>
<%}else if(sMode.equals("tail")) {
	/**************************************************/
	/* Engine 관리 : 로그확인 MAIN - 파일 선택 및 다운로드
	/**************************************************/
	String engine = request.getParameter("engine");
	String logPath = request.getParameter("logPath");
	String logName = request.getParameter("logName");
	if(logPath.equals("")){
		logPath = PropertiesUtil.getStringProperties("configure", "weblogs_path");
		logName = "tm_web.log";
	}
%>
<div style="clear:both;width:550px">
	<form name="<%=id%>_<%=engine%>_log_form" id="<%=id%>_<%=engine%>_log_form">
	<input type="hidden" id="logPath" value="<%=logPath %>">
	<table border="0" cellspacing="0" cellpadding="0" width="600px" >
		<tr>
			<td>
				<div style="height:25px;margin-top:7px">
					<div style="float:left">
					<img style="margin-right:5px" src="images/search_person.gif" title="파일경로"/><input type="text" id="logName" name="logName" size="88" value="<%=logName %>" title="확인하고자 하는 파일 명를 입력하세요"/>
					</div>
					<div style="float:right" class="btn_b"><a href="javascript:$('<%=id%><%=engine%>').downLog()" style ="cursor:pointer"><span>파일다운</span></a>
					</div>
					<div style="float:right" class="btn_green"><a href="javascript:$('<%=id%><%=engine%>').viewLog()" style ="cursor:pointer"><span>확인</span></a>
					</div>
				</div>
			</td>
		</tr>
		<tbody id="<%=id%>_log_<%=engine%>_content">
		</tbody>
		<iframe id="_filedown" name="_filedown" style="display:none"></iframe>
	</table>
	</form>
	</div>
	<script type="text/javascript">
		/***********************************************/
		/* 확인 버튼 클릭
		/***********************************************/
		$('<%=id%><%=engine%>').viewLog = function() {
			nemoRequest.init( 
			{
				updateWindowId: '<%=id%><%=engine%>',  
				url: 'admin/manager/manager_proc.jsp?id=<%=id%>&mode=viewlog&logpath='+document.<%=id%>_<%=engine%>_log_form.logPath.value+document.<%=id%>_<%=engine%>_log_form.logName.value+'&engine=<%=engine%>', 
				update: $('<%=id%>_log_<%=engine%>_content'), 
				onSuccess: function(html,els,resHTML,scripts) {
				}
			});
			nemoRequest.post($('<%=id%>_rform'));
		}
		<%=id%><%=engine%>timer = $('<%=id%><%=engine%>').viewLog.periodical(1000);
		MochaUI.Windows.instances.get('<%=id%><%=engine%>').addEvent('onClose',function() { $clear(<%=id%><%=engine%>timer) } );

		/***********************************************/
		/* 파일 다운 버튼 클릭
		/***********************************************/
		$('<%=id%><%=engine%>').downLog = function() {
			var url = "admin/manager/manager.do?method=fileDownload&logpath="+document.<%=id%>_<%=engine%>_log_form.logPath.value+document.<%=id%>_<%=engine%>_log_form.logName.value;
			window.open(url,'_filedown');
		}
	</script>
<%}else if(sMode.equals("viewlog")) {
	/*****************************************************/
	/* Engine 관리 : 로그 확인 -  실제 파일  내용 보여주는 화면 
	/*****************************************************/
	String logpath = request.getParameter("logpath");
	String [] cmd = {logpath};
  	String log = "";
  	if(!logpath.equals("")){
  		log = WebTail.startmain(cmd);
  	}
%>
	<tr>
		<td>
			<div id="files-panel_handle" class="horizontalHandle" style="display: block;">
				<div id="files-panel_handle_icon" class="handleIcon"/>
				</div>
			</div>
			<div class="blue-border" style="height:100%;margin-top:8px">
				<div class="panel-header">
					<div class="panel-header-ball" >
						<div class="panel-expand icon16" style="width: 16px; height: 16px;"/>
						</div>
					</div>
					<div class="panel-headerContent">
						<h2 id="panel2_title"><%=logpath%></h2>
					</div>
				</div>
				<div id="<%=id%>_userInfoPanel" style="padding:3px">
					<table>
						<tr>
							<td>
								<%=log %>
							</td>
						</tr>
					</tabe>
				</div>	
			</div>
		</td>
	</tr>
<%} else if(sMode.equals("server")) {
	/***********************************************/
	/* Server 성능 관리 : MAIN 화면
	/***********************************************/
%>
	<tr> 
		<td>
			<h2>Server 성능 관리</h2>
		</td>
	</tr>
	<tr> 
		<td height="10"></td>
	</tr>
	<tr>
		<td>
			<table border="0" cellspacing="0" cellpadding="0" width="600px" >
				<tbody id="<%=id%>_check_result_content">
				</tbody>
			</table>
		</td>
	</tr>
	<div id="files-panel_handle" class="horizontalHandle" style="display: block;">
				<div id="files-panel_handle_icon" class="handleIcon"/>
				</div>
	</div>
	<tr>
		<td>
			<table border="0" cellspacing="0" cellpadding="0" width="600px" >
				<tbody id="<%=id%>_spaces_result_content">
				</tbody>
			</table>
		</td>
	</tr>
	<script type="text/javascript">
		/***********************************************/
		/* Server 디스크 용량  확인
		/***********************************************/
		$('<%=id%>').check_spaces = function() {
			nemoRequest.init( 
			{
				updateWindowId: '<%=id%>',  
				url: 'admin/manager/manager_proc.jsp?id=<%=id%>&mode=spacescheck', 
				update: $('<%=id%>_spaces_result_content'), 
				onSuccess: function(html,els,resHTML,scripts) {
				}
			});
			nemoRequest.post($('<%=id%>_rform'));
			
		}
		/***********************************************/
		/* Server 성능 확인
		/***********************************************/
		$('<%=id%>').check_server = function() {
			nemoRequest.init( 
			{
				updateWindowId: '<%=id%>',  
				url: 'admin/manager/manager_proc.jsp?id=<%=id%>&mode=checkserver', 
				update: $('<%=id%>_check_result_content'), 
				onSuccess: function(html,els,resHTML,scripts) {
				}
			});
			nemoRequest.post($('<%=id%>_rform'));
			
		}
		
		/* 화면 표시 */
		window.addEvent("domready",function () {
			
			$('<%=id%>').check_server();
			$('<%=id%>').check_spaces(); 
			
		});

	</script>
<%} else if(sMode.equals("checkserver")) {
	/***********************************************/
	/* Server 성능 관리 : 확인 결과 화면
	/***********************************************/

	long totalSwapSpaceSize = CheckServer.getTotalSwapSpaceSize();
	long freeSwapSpaceSize = CheckServer.getFreeSwapSpaceSize();
	long useSwapSpaceSize = CheckServer.getTotalSwapSpaceSize() - CheckServer.getFreeSwapSpaceSize();
	float swapSpaceRatio = ((float)useSwapSpaceSize/totalSwapSpaceSize) * 100;
	//System.out.println(DateTime.getTimeStampString());
%>
	<tr>
		<td>
			<div class="blue-border" style="height:208px;margin-top:8px">
				<div class="panel-header">
					<div class="panel-header-ball" >
						<div class="panel-expand icon16" style="width: 16px; height: 16px;"/>
						</div>
					</div>
					<div class="panel-headerContent">
						<h2 id="panel1_title">Server 메모리 사용 </h2>
					</div>
				</div>
				<div id="<%=id%>_userInfoPanel1" style="padding:3px">
					
					<!-- &nbsp;총 할당 메모리 : <%=CheckServer.floatToMB(totalSwapSpaceSize) %> <br>
					&nbsp;사용 메모리 : <%=CheckServer.floatToMB(useSwapSpaceSize) %> <br>
					&nbsp;사용 가능  메모리 : <%=CheckServer.floatToMB(freeSwapSpaceSize) %> <br>
					&nbsp;<%=String.format("%.2f",swapSpaceRatio) %>% <br>-->
					<table>
						<tr>
							<td width="45%" align="center">
								<div class="dash_box_content">
									<div id="<%=id%>MemoryChart" ></div>
								</div>
								<table border="0" cellspacing="0" cellpadding="0" width="30%" height="150px" align="center">
									<tr bgcolor="#FFCC00" height="<%=100 - swapSpaceRatio %>%">
										<td align="center"><%=String.format("%.3f",100 - swapSpaceRatio) %>%</td>
									</tr>
									<tr bgcolor="#000000" height="<%=swapSpaceRatio%>%">
										<td align="center"><font color="#FFFFFF"><%=String.format("%.3f",swapSpaceRatio)%>%</font></td>
									</tr>
								</table>
							</td>
							<td>
								<table>
									<tr>
										<td bgcolor="#000000" width="10px">&nbsp;</td>
										<td>사용중인 메모리 :</td>
										<td align="right"><%=useSwapSpaceSize %> 바이트 </td>
										<td align="right"><%=CheckServer.toMB(useSwapSpaceSize) %></td>
									</tr>
									<tr>
										<td bgcolor="#FFCC00" width="10px">&nbsp;</td>
										<td>사용 가능한 메모리 :</td>
										<td align="right"><%=freeSwapSpaceSize%> 바이트</td>
										<td align="right"><%=CheckServer.toMB(freeSwapSpaceSize) %></td>
									</tr>
									<tr bgcolor="F7F7F7">
										<td colspan="4" height="1" bgcolor="#111111"></td>
									</tr>
									<tr>
										<td></td>
										<td>총 할당 메모리 :</td>
										<td align="right"><%=totalSwapSpaceSize %> 바이트</td>
										<td align="right"><%=CheckServer.toMB(totalSwapSpaceSize) %></td>
									</tr>
								</table>
							</td>
							
						</tr>
					</table>
					
				</div>	
			</div>
		</td>
	</tr>
	

<%} else if(sMode.equals("spacescheck")) {
	String install_path = PropertiesUtil.getStringProperties("configure", "install_path");
	long hardTotalSpace = CheckServer.getHardTotalSpace(install_path);
	long hardUsableSpace = CheckServer.getHardUsableSpace(install_path);
	long hardUseSpace = hardTotalSpace - hardUsableSpace;
	float swapSpaceRatio = ((float)hardUsableSpace/hardTotalSpace) * 100;
%>
	<tr>
		<td>
			<div class="blue-border" style="height:208px;margin-top:8px">
				<div class="panel-header">
					<div class="panel-header-ball" >
						<div class="panel-expand icon16" style="width: 16px; height: 16px;"/>
						</div>
					</div>
					<div class="panel-headerContent">
						<h2 id="panel2_title">Server 디스크 용량 </h2>
					</div>
				</div>
				<div id="<%=id%>_userInfoPanel2" style="padding:3px">
					<table>
						<tr>
							<td width="45%" align="center">
								<div class="dash_box_content">
									
								<table border="0" cellspacing="0" cellpadding="0" width="30%" height="150px" align="center">
									<tr bgcolor="#FFCC00" height="<%=swapSpaceRatio%>%">
										<td align="center"><%=String.format("%.3f",swapSpaceRatio)%>%</td>
									</tr>
									<tr bgcolor="#000000" height="<%=100 - swapSpaceRatio %>%">
										<td align="center"><font color="#FFFFFF"><%=String.format("%.3f",100 - swapSpaceRatio) %>%</font></td>
									</tr>
								</table>
								</div>
							</td>
							<td>
								<table>
									<tr>
										<td bgcolor="#000000" width="10px">&nbsp;</td>
										<td>사용중인 공간 :</td>
										<td align="right"><%=hardUseSpace %> 바이트 </td>
										<td align="right"><%=String.format("%.2f",CheckServer.floatToGB(hardUseSpace)) %>(GB)</td>
									</tr>
									<tr>
										<td bgcolor="#FFCC00" width="10px">&nbsp;</td>
										<td>사용 가능한 공간 :</td>
										<td align="right"><%=hardUsableSpace%> 바이트</td>
										<td align="right"><%=String.format("%.2f",CheckServer.floatToGB(hardUsableSpace)) %>(GB)</td>
									</tr>
									<tr bgcolor="F7F7F7">
										<td colspan="4" height="1" bgcolor="#111111"></td>
									</tr>
									<tr>
										<td></td>
										<td>총 할당 공간 :</td>
										<td align="right"><%=hardTotalSpace %> 바이트</td>
										<td align="right"><%=String.format("%.2f",CheckServer.floatToGB(hardTotalSpace)) %>(GB)</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>	
			</div>
		</td>
	</tr>
	<script type="text/javascript">
	$('<%=id%>').test = function(file) {
		nemoRequest.init( 
				{
					busyWindowId: '<%=id%>',  // busy 를 표시할 window
					updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

					url: 'pages/automail/test/'+file, 
					onSuccess: function(html,els,resHTML,scripts) {
						//$('<%=id%>SpacesChart').style.width = '100%';
						//$('<%=id%>SpacesChart').style.height = '150px';
						//$('<%=id%>SpacesChart').load( resHTML );
					}
				}
		);
		nemoRequest.get({});
		}

		
	
	</script>
<%} else if(sMode.equals("spacschart")) {
	String hardUsableSpace = request.getParameter("hardUsableSpace");
	String hardUseSpace = request.getParameter("hardUseSpace");
%>
{
  "elements" : [
    {
      
      "colours" : [
        "#FFCC00",
        "#000000"
      ],
      "alpha" : "0.8",
      "start_angle" : 135,
      "animate" : false,
      "values" : [
      {
          "value" : <%=hardUsableSpace%>,
          "label" : "사용가능한공간"
        },
        {
          "value" : <%=hardUseSpace %>,
          "label" : "사용중인공간"
        }
        
      ],
      "type" : "pie",
      "border" : "2"
    }
  ],
  "bg_colour" : "#FAFAFA"
  }
}
	
<%	}%>
	
	
<%}else{%>
<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td align="center" valign="middle">
			<center><img src="../../images/error.jpg" /></center>
		</td>
	</tr>
</table>
<%}%>