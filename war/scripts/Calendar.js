<!--
var _target;	// 호출한 Object의 저장
var _stime;
var _isDateClear = true;

var _chkdate;    //비교 대상 Object의 저장
var _type;

if( !document.getElementById("minical") ) {
	document.writeln('<div oncontextmenu="return false" onselectstart="return false" id="minical" ondragstart="return false" '+
	'style="border:1px solid #558BAC;PADDING-RIGHT: 4px; MARGIN-TOP: 1px; '+
	'DISPLAY: none; PADDING-LEFT: 4px; Z-INDEX: 120000; BACKGROUND: #D8E6EF; PADDING-BOTTOM: 4px; '+
	'PADDING-TOP: 4px; POSITION: absolute;'+
	'filter:progid:DXImageTransform.Microsoft.Shadow(color=#737373,direction=135,strength=5)" '+
	'minYear="2007"></div>');

	document.addEvent('click', function(e) { $('minical').setStyle('display','none') } );
}


function Calendar( e, src, chksrc, type, isClear )
{
	var obj;
	var chkobj;

	obj = src;
	chkobj = chksrc;
	e = new Event(e);

	if(isClear == 'N') {
		_isDateClear = false;
	} else {
		_isDateClear = true;
	}

	if (typeof obj == 'object')
	{
		// jucke
		var now = obj.value.split("-");
		if(type == 'start' || type == 'end'){
			_chkdate = chkobj.value;
			_type = type;
		}else{
			_chkdate = "";
		}
		var x, y;
		
		_target = obj;		// Object 저장;
		if(	document.getElementById("minical").style.display == "block") {
			document.getElementById("minical").style.display = "none";
			return;
		}

		// 마우스 위치 확인
		x = e.page.x;
		y = e.page.y;

		// 마우스 위치 확인
		//x = (document.layers) ? loc.pageX : event.clientX;
		//y = (document.layers) ? loc.pageY : event.clientY;
		
		// 스크롤 영역 보정
		//x = document.body.scrollLeft + x;
		//y = document.body.scrollTop + y;
		
		$("minical").style.top	= (y+10)+'px';
		$("minical").style.left	= (x+15)+'px';
		$("minical").style.display = (document.getElementById("minical").style.display == "block") ? "none" : "block";

		// 정확한지 검사
		
		
		
		if (now.length == 3)
		{
			var now1 = now[0].replace(/^\s*/,'').replace(/\s*$/, '');
			var now2 = now[1].replace(/^\s*/,'').replace(/\s*$/, '');
			var now3 = now[2].replace(/^\s*/,'').replace(/\s*$/, '');

			Show_cal(now1,now2,now3);		// 넘어온 값을 년월일로 분리
		}
		else
		{
			now = new Date();
			Show_cal(now.getFullYear(), now.getMonth()+1, now.getDate());		// 현재 년/월/일을 설정하여 넘김.
		}
	}
	else
	{
		return;
	}

	e.stop();
}
	
// 마우스가 칼렌다위에 있으면
function doOver(el)
{
	//var el = window.event.srcElement;
	//var el = this;
	/*
	cal_Day = el.getAttribute('title');

	if (cal_Day.length > 7) 
	{
		// 날자 값이 있으면.
		el.style.borderTopColor = el.style.borderLeftColor = "buttonhighlight";
		el.style.borderRightColor = el.style.borderBottomColor = "buttonshadow";
	}
	
	// Clear
	window.clearTimeout(_stime);
	*/
}

function doClick( e, el )
{															// 날자를 선택하였을 경우
	//cal_Day = window.event.srcElement.title;
	cal_Day = el.getAttribute('title');
	//window.event.srcElement.style.borderColor = "red";							// 테두리 색을 빨간색으로
	el.style.borderColor = "red";
	
	if (cal_Day.length > 7) {													// 날자 값이있으면
		
		if(_chkdate != ''){
			var dt1 = cal_Day.split("-");
			var dt2 = _chkdate.split("-");
			var chkdt1 =  new Date(dt1[0],dt1[1],dt1[2]).valueOf();
			var chkdt2 =  new Date(dt2[0],dt2[1],dt2[2]).valueOf();
			var result = chkdt1 - chkdt2;
			
			if(_type == 'end'){
				if(result < 0){
					alert("시작 날짜 보다 이전 날짜는 설정 하실 수 없습니다.");
					return;
				}
			}
			if(_type == 'start'){
				if(result > 0){
					alert("종료 날짜  보다 이후 날짜는 설정 하실 수 없습니다.");
					return;
				}
			}
		}
		_target.value=cal_Day													// 값 설정
	} else {
		_target.value="";													// 값 설정
	}
	$("minical").style.display='none';												// 화면에서 지움

	new Event(e).stop();
	
}

function doOut(el)
{
	//var el = window.event.fromElement;
	//var el = this;
	//alert(el.title);
/*
	var cal_Day = el.getAttribute('title');

	if(el.getAttribute('title')) {

	if (cal_Day.length > 7)
	{
		el.style.borderColor = "white";
	}
	}
*/
}

function day2(d) {																// 2자리 숫자료 변경
	var str = new String();
	
	if (parseInt(d) < 10) {
		str = "0" + parseInt(d);
	} else {
		str = "" + parseInt(d);
	}
	return str;
}

function Show_cal(sYear, sMonth, sDay)
{
	var Months_day = new Array(0,31,28,31,30,31,30,31,31,30,31,30,31)
	var Weekday_name = new Array("일", "월", "화", "수", "목", "금", "토");
	var intThisYear = new Number(), intThisMonth = new Number(), intThisDay = new Number();
	document.getElementById("minical").innerHTML = "";
	datToday = new Date();													// 현재 날자 설정
	
	intThisYear = parseInt(sYear);
	intThisMonth = parseInt(sMonth, 10);
	intThisDay = parseInt(sDay, 10);
	
	if (intThisYear == 0) intThisYear = datToday.getFullYear();				// 값이 없을 경우
	if (intThisMonth == 0) intThisMonth = parseInt(datToday.getMonth())+1;	// 월 값은 실제값 보다 -1 한 값이 돼돌려 진다.
	if (intThisDay == 0) intThisDay = datToday.getDate();
	switch(intThisMonth) {
		case 1:
				intPrevYear = intThisYear -1;
				intPrevMonth = 12;
				intNextYear = intThisYear;
				intNextMonth = 2;
				break;
		case 12:
				intPrevYear = intThisYear;
				intPrevMonth = 11;
				intNextYear = intThisYear + 1;
				intNextMonth = 1;
				break;
		default:
				intPrevYear = intThisYear;
				intPrevMonth = parseInt(intThisMonth) - 1;
				intNextYear = intThisYear;
				intNextMonth = parseInt(intThisMonth) + 1;
				break;
	}

	NowThisYear = datToday.getFullYear();										// 현재 년
	NowThisMonth = datToday.getMonth()+1;										// 현재 월
	NowThisDay = datToday.getDate();											// 현재 일
	
	datFirstDay = new Date(intThisYear, intThisMonth-1, 1);						// 현재 달의 1일로 날자 객체 생성(월은 0부터 11까지의 정수(1월부터 12월))
	intFirstWeekday = datFirstDay.getDay();										// 현재 달 1일의 요일을 구함 (0:일요일, 1:월요일)
	
	intSecondWeekday = intFirstWeekday;
	intThirdWeekday = intFirstWeekday;
	
	datThisDay = new Date(intThisYear, intThisMonth, intThisDay);				// 넘어온 값의 날자 생성
	intThisWeekday = datThisDay.getDay();										// 넘어온 날자의 주 요일

	varThisWeekday = Weekday_name[intThisWeekday];								// 현재 요일 저장
	
	intPrintDay = 1																// 달의 시작 일자
	secondPrintDay = 1
	thirdPrintDay = 1
	
	Stop_Flag = 0
	
	if ((intThisYear % 4)==0) {													// 4년마다 1번이면 (사로나누어 떨어지면)
		if ((intThisYear % 100) == 0)
		{
			if ((intThisYear % 400) == 0)
			{
				Months_day[2] = 29;
			}
		}
		else
		{
			Months_day[2] = 29;
		}
	}
	
	intLastDay = Months_day[intThisMonth];										// 마지막 일자 구함
	Stop_flag = 0
	
	Cal_HTML = "<TABLE style='BORDER:1px solid #6693AA; CELLPADDING=0 CELLSPACING=0 ONMOUSEOVER='doOver(this);' ONMOUSEOUT=doOut(this); STYLE='font-size:8pt;font-family:Tahoma;'>"
			+ "<tr><td colspan=20><table cellspacing=0 cellpadding=0 border=0><tr>"
			+ "<TR><TD align='left' width='100%' colspan='3'><SPAN TITLE='닫기' STYLE=cursor:pointer; onClick='document.getElementById(\"minical\").style.display=\"none\"'>&nbsp;ⓧ&nbsp;</SPAN></TD>";

	if(_isDateClear)
		Cal_HTML += "<TD nowrap=nowrap height='22' valign='middle' align='right' >";

	Cal_HTML += "</TR></table>"
			+ "<TR><TD colspan='10' height='1' bgcolor='#87AACD' width='100%'></TD>"

			+ "<TR ALIGN=CENTER height='30' valign='middle'><TD COLSPAN=7 nowrap=nowrap ALIGN=CENTER><SPAN TITLE='이전달' STYLE='cursor:pointer;' onClick='Show_cal("+intPrevYear+","+intPrevMonth+","+intThisDay+");new Event(event).stop();'><FONT COLOR=Navy>◀</FONT></SPAN>&nbsp;&nbsp;"
			+ "<B STYLE=color:red>"+get_Yearinfo(intThisYear,intThisMonth,intThisDay)+" 년 "+get_Monthinfo(intThisYear,intThisMonth,intThisDay)+" 월 </B>"
			+ " <SPAN TITLE='다음달' STYLE=cursor:pointer; onClick='Show_cal("+intNextYear+","+intNextMonth+","+intThisDay+");new Event(event).stop();'><FONT COLOR=Navy>▶</FONT></SPAN></TD></TR>"
			+ "<TR><TD colspan='10' height='1' bgcolor='#87AACD' width='100%'></TD>"
			//+ "<TR ><TD COLSPAN=10 nowrap=nowrap height='26' valign='middle' align='center' >  <SPAN TITLE='입력된 날짜 지우기' STYLE=cursor:pointer; onClick='doClick();'><FONT COLOR=Blue>&nbsp;입력된 날짜 지우기</FONT></SPAN></TD>"
			+ "</TR>"

			+ "<TR ALIGN=CENTER BGCOLOR=#AACBDD STYLE='color:black;font-weight:bold;' height='20' valign='bottom'><TD>일</TD><TD>월</TD><TD>화</TD><TD>수</TD><TD>목</TD><TD>금</TD><TD>토</TD></TR>";
			
	for (intLoopWeek=1; intLoopWeek < 7; intLoopWeek++)
	{						// 주단위 루프 시작, 최대 6주
		Cal_HTML += "<TR ALIGN=RIGHT BGCOLOR=#F7FBFD>"
		for (intLoopDay=1; intLoopDay <= 7; intLoopDay++)
		{						// 요일단위 루프 시작, 일요일 부터
			if (intThirdWeekday > 0) {											// 첫주 시작일이 1보다 크면
				Cal_HTML += "<TD onClick='doClick(event,this);'>";
				intThirdWeekday--;
			} else {
				if (thirdPrintDay > intLastDay) {								// 입력 날짝 월말보다 크다면
					Cal_HTML += "<TD onClick='doClick(event,this);'>";
				} else {														// 입력날짜가 현재월에 해당 되면
					Cal_HTML += "<TD onClick='doClick(event,this);' title='"+intThisYear+"-"+day2(intThisMonth).toString()+"-"+day2(thirdPrintDay).toString()+"' STYLE=\"cursor:pointer;border:1px solid white;";
					if (intThisYear == NowThisYear && intThisMonth==NowThisMonth && thirdPrintDay==intThisDay) {
						Cal_HTML += "background-color:#30C0FF;";
					}
					
					switch(intLoopDay)
					{
						case 1:													// 일요일이면 빨간 색으로
							Cal_HTML += "color:red;"
							break;
						case 7:
							Cal_HTML += "color:blue;"
							break;
						default:
							Cal_HTML += "color:black;"
							break;
					}
					
					Cal_HTML += "\">"+thirdPrintDay;
					
				}
				thirdPrintDay++;
				
				if (thirdPrintDay > intLastDay) {								// 만약 날짜 값이 월말 값보다 크면 루프문 탈출
					Stop_Flag = 1;
				}
			}
			Cal_HTML += "</TD>";
		}
		Cal_HTML += "</TR>";
		if (Stop_Flag==1) break;
	}
	Cal_HTML += "</TABLE>";

	document.getElementById("minical").innerHTML = Cal_HTML;
}

// 년 정보를 콤보 박스로 표시
function get_Yearinfo(year, month, day)
{
	var min = parseInt(year) - 40;
	var max = parseInt(year) + 10;
	
	min = parseInt(document.getElementById("minical").getAttribute('minYear') );

	var i = new Number();
	var str = new String();
	
	str = "<SELECT onChange='Show_cal(this.value,"+month+","+day+")' onclick='new Event(event).stop();' style='font-size : 8pt; font-family : tahoma'>";
	
	for (i=min; i<=max; i++)
	{
		if (i == parseInt(year))
		{
			str += "<OPTION VALUE="+i+" selected >"+i+"</OPTION>";
		} else {
			str += "<OPTION VALUE="+i+" >"+i+"</OPTION>";
		}
	}
	str += "</SELECT>";
	return str;
}


// 월 정보를 콤보 박스로 표시
function get_Monthinfo(year, month, day)
{
	var i = new Number();
	var str = new String();
	
	str = "<SELECT onChange='Show_cal("+year+",this.value,"+day+");'  onclick='new Event(event).stop();' style='font-size : 8pt; font-family : tahoma'>";
	
	for (i=1; i<=12; i++)
	{
		if (i == parseInt(month))
		{
			str += "<OPTION VALUE="+i+" selected ONMOUSEOVER='doOver(this);'>"+i+"</OPTION>";
		}
		else
		{
			str += "<OPTION VALUE="+i+" ONMOUSEOVER='doOver(this);'>"+i+"</OPTION>";
		}
	}
	
	str += "</SELECT>";
	
	return str;
}

//-->