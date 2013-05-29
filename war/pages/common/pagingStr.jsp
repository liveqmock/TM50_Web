<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>   
<%
int iPageNum = Form( request.getParameter("curPage"), "0");
int iLineCnt = Form( request.getParameter("iLineCnt"), "0");
int iTotalCnt = Form( request.getParameter("iTotalCnt"), "0");
int iPageCnt = Form( request.getParameter("iPageCnt"), "0");
String id = request.getParameter("id");

if(iTotalCnt > 0) {
	out.print( getPaggingStr(id, iPageNum, iLineCnt, iTotalCnt, iPageCnt) );
} else {
	out.print( "<!--데이터가 없습니다.-->  " );
}
%>
<script language="javascript">

	if(!$('<%=id%>').goTopage) {

		$('<%=id%>').goTopage = function( page ) {

			$('<%=id%>').searchForm.curPage.value = page;
			$('<%=id%>').list( true );
		}
		$('<%=id%>').goToblock = function( block ) {

			$('<%=id%>').searchForm.curPage.value = <%=iPageCnt%>*(block-1)+1;
			$('<%=id%>').list( true );
		}
	}

</script>
<%!

public int Form( String var, String def ) {

	if(var == null) {
		return 0;
	}

	return Integer.parseInt(  var );

}

// 입력 받은 값에 대한 페이징 결과 String를 반환한다.
// 현재페이지, 전체 페이지, 현재 블럭, 전체 블럭, 블럭당 페이지수.
// 현재 페이지, 게시물 수, 전체 게시물수, 블럭당 페이지수.
public	String	getPaggingStr(String id , int nowPage, int lineCount, int totalCount, int pagePerBlock) {

	int totalPage   = (new Double(Math.ceil((double)totalCount / lineCount))).intValue();       //  전체 페이지.
	int nowBlock    = (new Double(Math.ceil((double)nowPage/pagePerBlock))).intValue();         //  현재 블럭.
	int totalBlock  = (new Double(Math.ceil((double)totalPage / pagePerBlock))).intValue();     //  전체 블럭.

	if(totalPage!=0 && nowPage>totalPage) nowPage = totalPage ;


	StringBuffer	pageStr			=	new StringBuffer();
	String			strVal			=	"";
	int				i_nowBlock	=	nowBlock - 1;
	int				i_nowBlock2	=	nowBlock + 1;
	int				i_linkValue	=	0;

	if(totalPage !=0){

		// 이전 블럭 표시
		// 처음 링크용.
		pageStr.append("<a href=\"javascript:$('"+ id +"').goToblock('1')\"><img src=\"images/btn_start.gif\"/></a>&nbsp;&nbsp;\n");

		if (nowBlock > 1) {

			pageStr.append("<a href=\"javascript:$('"+ id +"').goToblock('"+ i_nowBlock +"')\">");
			pageStr.append("<img src='images/btn_pre.gif' align=\"absmiddle\"/></a>&nbsp;&nbsp;");

		} else {

			pageStr.append("<img src='images/btn_pre.gif' align=\"absmiddle\"/>&nbsp;&nbsp;");
		}

		// page Link 표시
		for(int j = 0; j < pagePerBlock; j++) {

			i_linkValue	=	(nowBlock * pagePerBlock - pagePerBlock) + j + 1;

			// 링크 시작.
			pageStr.append("<a href=\"javascript:$('"+ id +"').goTopage("+ i_linkValue +")\"'>");

			// 현재 페이지의 링크일 경우 색상을 준다.(열기)
			if(i_linkValue == nowPage) {
				pageStr.append("<span style='color:#555555'><b>");
			}


			// 링크의 표기.
			pageStr.append("[&nbsp;" + i_linkValue + "&nbsp;]");

			// 현재 페이지의 링크일 경우 색상을 준다.(닫음)
			if(i_linkValue == nowPage) {
				pageStr.append("</b></span>");
			}

			// 링크 끝.
			pageStr.append("</a>&nbsp;");

			// 페이징의 반복이 끝났다면 종료.
			if(i_linkValue == totalPage) {
				break;
			}
		}	// for(int j = 0; j < pagePerBlock; j++) {

		// 다음 블럭 표시
		if(totalBlock > nowBlock ) {
			pageStr.append("&nbsp;&nbsp;<a href=\"javascript:$('"+ id +"').goToblock('"+ i_nowBlock2 +"')\"><img src='images/btn_next.gif' align=\"absmiddle\"/></a>");
		} else {
			pageStr.append("&nbsp;&nbsp;<img src='images/btn_next.gif' align=\"absmiddle\"/>");
		}			
			
		pageStr.append("&nbsp;&nbsp;<a href=\"javascript:$('"+ id +"').goTopage('" + totalPage + "')\"><img src=\"images/btn_end.gif\"/></a>\n");
		
		pageStr.append("&nbsp;&nbsp;&nbsp;&nbsp;<b>( Total : "+StringUtil.formatPrice(totalCount)+" )</b>");
	}

	strVal	=	pageStr.toString();
	return strVal;
}

%>