<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<HTML>
<H1> file upload test page 1</H1><p>
<HR><center><P>

<!-- 엑티브엑스 컨트롤 -->
<OBJECT id="TestActiveX" name="TestActiveX"
	  classid="clsid:81B386CF-ECFC-4BDE-9996-CB0D9C5FB92A"
	  codebase="JiniWorksXEFileUploadX.cab#version=1,0,27,0"
	  width=600
	  height=250
	  align=center
	  hspace=0
	  vspace=0
>
<!-- 업로드 url -->
<param name='upload_url' value="http://www.andwise.com/upload_test/upload.php"/>
<!-- 서버 페이지에서 성공적으로 완료되면 페이지에 표시할 문자 -->
<param name='compare_success_text' value="SUCCESS"/>
<!-- 파일 컨트롤 아이디 -->
<param name='file_control_id' value="upload_file"/>

<!-- 컨트롤  사용여부 -->
<param name='control_enabled' value="false"/>

<!-- 취소버튼 사용여부 -->
<param name='use_cancel' value="true"/>

<!-- 리스트에 중복 추가 가능 여부 -->
<param name='allow_duplicate' value="false"/>

<!-- 서버에 파일 중복일 경우 오버라이트 여부 -->
<param name='duplicate_overwrite' value="true"/>

<!-- 로컬에 파일이 없거나 리스트에 중복일 경우 메시지 표시 여부 -->
<param name='show_warning' value="true"/>

</OBJECT>

<p>
<input type="button" onclick="upload()" value="업로드"></p>

<p>
<a href="javascript:visible()">컨트롤 토글</a>
</p>
<p>
<a href="javascript:add_file()">파일 추가</a>
</p>
<p>
<a href="javascript:delete_file()">파일 삭제</a>
</p>


<form id="frm" method="post" action="about:blank">
</form>


<div align="left">
<h3>1. 전달 파라미터</h3>
<ul>
	<li><p><b>upload_url</b> : 업로드 url<br>
		사용예 : &lt;param name='upload_url' value="http://www.andwise.com/upload_test/upload.php"/&gt;</p>
	</li>
	<li><p><b>compare_success_text</b> : 서버 페이지에서 성공적으로 완료되면 페이지에 표시할 문자<br/>
		사용예 : &lt;param name='compare_success_text' value="SUCCESS"/&gt;<br/>
		jsp 페이지에서 업로드를 처리한 후 out.print("SUCCESS");<p>
	</li>
		
	<li><p><b>file_control_id</b> : html 파일 컨트롤 아이디<br/>
		사용예 : &lt;input type="file" id="file_control_id" &gt; 를 사용할 때와 같이 id를 넘겨준다
		</p>

	</li>
	
	<li><p><b>control_enabled</b> :  컨트롤  활성화 여부<br/>
		사용예 : &lt;param name='control_enabled' value="false"&gt; 
		</p>

	</li>
	
</ul>
</div>

<div align="left">
<h3>2. 이벤트</h3>
<ul>
	<li><b>OnComplete</b> : 업로드가 완료되면 호출되는 이벤트</li>
	<li><b>OnCancel</b> : 사용자가 취소하면 호출되는 이벤트</li>
	<li><b>OnFailure</b> : 업로드가 실패하면 호출되는 이벤트</li>
</ul>
</div>

<div align="left">
<h3>3. 속성</h3>
<ul>
	<li><b>success_file_count</b> : 성공한 업로드 파일 갯수</li>
	<li><b>total_file_count</b> : 업로드할 총 파일 갯수</li>
	<li><b>total_byte</b> : 업로드할 총 바이트</li>
	<li><b>error_text</b> : 오류 메시지</li>
	<li><b>result_code </b> : 오류 결과 코드
		<ul>
			<li>0: 에러없음</li>
			<li>1: 서버에서 처리된 결과 문자를 받지 못함</li>
			<li>2: 통신오류</li>
			<li>3: 그 밖의 오류</li>
			<li>4: 사용자 취소</li>
			<li>5: 서버에서 처리 실패</li>
		</ul>
	</li>
</ul>
</div>


<div align="left">
<h3>4. 메쏘드</h3>
<ul>
	<li><b>add_file( String filename )</b> : 파일 추가</li>
	<li><b>delete_file( String filename )</b> : 파일 삭제</li>
</ul>
</div>


</HTML>
<script type="text/javascript">

function upload() {

	var obj = document.getElementById("TestActiveX");
	obj.upload();

}


</script>


<!-- 업로드가 완료되면 호출되는 이벤트 -->
<script for="TestActiveX" event="OnComplete">

	var obj = document.getElementById("TestActiveX");
	var form = document.getElementById("frm");

	if(obj.total_file_count == 0) {
		alert('업로드할 파일이 없습니다');
		return;
	}

	alert( obj.total_file_count+'개 중 '+obj.success_file_count+'개의 파일업로드 완료');

	frm.submit();
	
</script>

<!-- 사용자가 취소하면 호출되는 이벤트 -->
<script for="TestActiveX" event="OnCancel">
	var obj = document.getElementById("TestActiveX");
	alert( '사용자에의하여 취소');
	alert( obj.total_file_count+'개 중 '+obj.success_file_count+'개의 파일업로드 완료');
</script>


<!-- 업로드가 실패하면 호출되는 이벤트 -->
<script for="TestActiveX" event="OnFailure">

	var obj = document.getElementById("TestActiveX");

	if(obj.result_code == '1') // 결과값 틀림
		alert( '완료 결과값이 틀림' );
	else if(obj.result_code == '2') // 통신 오류
		alert( '통신오류' );
	else if(obj.result_code == '3') // 통신 오류
		alert( '그 밖의 오류' );

	alert( '오류 메시지 '+obj.error_text );
	alert( obj.total_file_count+'개 중 '+obj.success_file_count+'개의 파일업로드 완료');

</script>


<script>
	function add_file() {
		obj = document.getElementById("TestActiveX");
		obj.add_file("G:\\InetSDK\\Bin\\NoteAmpUploadXtest.htm");
	}

	function visible() {
		obj = document.getElementById("TestActiveX");
		obj.control_enabled = !obj.control_enabled;
	}
	function delete_file() {
		obj = document.getElementById("TestActiveX");
		obj.delete_file("G:\\InetSDK\\Bin\\NoteAmpUploadXtest.htm");
	}
</script>

