<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page language="java" import="java.sql.*,java.util.*,javax.servlet.http.* "%>
<%
	String id = request.getParameter("id");
%>
<div >
<div class="accordianWrapper" style="float:left;border:1px solid #A3B9D1">
<div class="mochaAccordion">
	<h3 class="accordianToggler topToggler">입력1</h3>
	<div class="accordianElement">
		<div class="accordianContent">
			<h3>입력1</h3>
			<p><input type="text"/>asdfasdfasdf asfda fdsa fdfasdfasd fsdafasd</p>
			<p><input type="text"/></p>
			<p><input type="text"/></p>
			<p><input type="text"/></p>
		</div>
	</div>

	<h3 class="accordianToggler">입력2</h3>
	<div class="accordianElement">
		<div class="accordianContent">
			<h3>입력2</h3>
			<p><input type="checkbox" />asdf asdf asdf asdf</p> 
			<p><input type="checkbox" />asdf asdf asdf asdf</p> 
			<p><input type="checkbox" />asdf asdf asdf asdf</p> 
			<p><input type="checkbox" />asdf asdf asdf asdf</p> 
		</div>
	</div>
	
	<h3 class="accordianToggler">입력32</h3>
	<div class="accordianElement">
		<div class="accordianContent">
			<h3>입력3</h3>
			<p><input type="checkbox" />asdf asdf asdf asdf</p> 
			<p><input type="checkbox" />asdf asdf asdf asdf</p> 
			<p><input type="checkbox" />asdf asdf asdf asdf</p> 
			<p><input type="checkbox" />asdf asdf asdf asdf</p> 
		</div>
	</div>
	

	<h3 class="accordianToggler bottomToggler">입력5</h3>
	<div class="accordianElement">
		<div class="accordianContent">
			<h3>입력3</h3>
			<p><input type="radio" />asafsdaji fjkdls;a jfklsdafasdf</p> 
			<p><input type="radio" />asafsdaji fjkdls;a jfklsdafasdf</p> 
			<p><input type="radio" />asafsdaji fjkdls;a jfklsdafasdf</p> 
			<p><input type="radio" />asafsdaji fjkdls;a jfklsdafasdf</p> 
			<p><input type="radio" />asafsdaji fjkdls;a jfklsdafasdf</p> 
		</div>
	</div>
</div>
</div>


<div>
	<a id="dddddddddd" href="" onclick="javascript:fire()">저장</a>
</div>
</div>

<script>
	$('dddddddddd').addEvent('onFire',function(txt) { alert(txt); } );
	function fire() {
		$('dddddddddd').fireEvent('onFire',['fire event'],10);
	}
</script>
