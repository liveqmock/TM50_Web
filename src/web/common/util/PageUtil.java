package web.common.util;


import javax.servlet.http.HttpServletRequest;


/**
 * <p>Title: JSP 페이지 관련 유틸리티</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: BIXON technology</p>
 *
 * @author 김남규
 * @version 1.0
 */
public class PageUtil {
	
	private static String firstImgURL = "/images/bt_first.gif"; 
	private static String forwardImgURL = "/images/bt_prev1.gif"; 
	private static String backwardImgURL = "/images/bt_next1.gif"; 
	private static String lastImgURL = "/images/bt_last.gif"; 
	
	public static String dividePageForm(int pageNo, int countPerPage, int totalCount, HttpServletRequest request, String linkUrl) {
		int endPage = endPage(pageNo, countPerPage, totalCount);
		
		
		StringBuffer sb = new StringBuffer();
		
		for (int i=0; i < endPage; i++) {
			sb.append("<a href=\"");
			sb.append(request.getContextPath() + linkUrl);
			sb.append("pageNo=");
			sb.append(i+1);
			sb.append("&countPerPage=");
			sb.append(countPerPage);
			sb.append("\">");
			sb.append(i+1);
			sb.append("</a>");
			if( i < (endPage-1) ){
				sb.append("&nbsp");
			}
		}
		
		return sb.toString();
	}
	public static String dividePageJavascript(int pageNo, int countPerPage, int totalCount, HttpServletRequest request, String javascriptFunction){ 
		
		//Default 10으로 세팅 해서 Paging Util 을 생성 한다. 
		return dividePageJavascript(pageNo, countPerPage, totalCount, request, javascriptFunction,10);
		
	}
	
	public static String dividePageJavascript(int pageNo, int countPerPage, int totalCount, HttpServletRequest request, String javascriptFunction, int pagingCnt){
		
		
		StringBuffer result = new StringBuffer(); 
		StringBuffer sb = new StringBuffer();
		
		int endpage = endPage(pageNo,countPerPage,totalCount); 

		//페이징 시 처음 들어가는 페이지 
		int firstPageOfPaging = (pageNo%pagingCnt)>0 ? ((pageNo/pagingCnt)*pagingCnt)+1 : (((pageNo/pagingCnt)-1)*(pagingCnt))+1; 
		//페이징 시 마지막에 들어 가는 페이지 
		int endPageOfPaging = (firstPageOfPaging+pagingCnt) > endpage ? firstPageOfPaging + ((endpage%pagingCnt)-1) : (firstPageOfPaging + pagingCnt-1) ; 
		
		for (int i=firstPageOfPaging ;i<endPageOfPaging+1; i++) {
			
			if(i==pageNo){
				sb.append("<FONT color=red><STRONG>"+i+"<STRONG></font>");
			}else{ 
				sb.append("<a href=\"javascript:" + javascriptFunction + "(" +i+ ")\" />");
				sb.append(i);
				sb.append("</a>");
			}
			sb.append("&nbsp");
		}
		
		if(endpage>=1){ 
							
			result.append("<a href=\"javascript:" + javascriptFunction + "(1)\" />");
			result.append("<img src='"+firstImgURL+"' alt='처음으로' align='absmiddle' />");
			result.append("</a>&nbsp;");
			
			if((firstPageOfPaging-pagingCnt) >0) result.append("<a href=\"javascript:" + javascriptFunction + "(" + (firstPageOfPaging-pagingCnt) +")\" />");
			result.append("<img src='"+forwardImgURL+"' alt='"+pagingCnt+"페이지 앞으로' align='absmiddle' />");
			if((firstPageOfPaging-pagingCnt) >0) result.append("</a>");
			result.append("&nbsp;");
			
			result.append(sb.toString()); 
			
			if((endPageOfPaging+pagingCnt) <= endpage) result.append("<a href=\"javascript:" + javascriptFunction + "("+ (firstPageOfPaging+pagingCnt)+ ")\" />");
			result.append("<img src='"+backwardImgURL+"' alt='"+pagingCnt+"페이지 뒤로'  align='absmiddle'/>");
			if((endPageOfPaging+pagingCnt) <= endpage)result.append("</a>&nbsp;");
			
			
			result.append("<a href=\"javascript:" + javascriptFunction + "("+endpage+")\" alt='마지막 페이지로' />");
			result.append("<img src='"+lastImgURL+"' alt='끝으로'  align='absmiddle'/>");
			result.append("</a>");
		}
		
		return result.toString();
	}
	
	public static int endPage(int currentPage, int countPerPage, int totalNo) {

		int extra = totalNo % countPerPage;

        if ( extra > 0 ){           
            return (totalNo - extra )/countPerPage + 1;
        } else {
            return totalNo/countPerPage;
        }
    }
}