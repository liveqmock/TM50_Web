<%@ page language="java" contentType="application/vnd.ms-excel; charset=EUC-KR"
    pageEncoding="EUC-KR"%><%@ page import="web.common.util.ThunderUtil"%><%@ page import="web.massmail.statistic.service.MassMailStatService"%><%@ page import="web.massmail.write.model.*" %><%@ page import="web.massmail.statistic.control.MassMailStatControlHelper"%><%@ page import="web.massmail.statistic.model.MassMailPersonPreview"%><%@ page import="java.util.Map"%><%@ page import="java.util.HashMap"%><%@ page import="java.util.List"%><% int massmailID = new Integer(request.getParameter("massmailID"));
	int scheduleID = new Integer(request.getParameter("scheduleID"));
	String standard = request.getParameter("standard");

	String type = request.getParameter("type");
	String key = request.getParameter("key");
	int iTotalCnt = new Integer(request.getParameter("iTotalCnt"));
	Map<String, Object> searchMap = new HashMap<String, Object>(); 
	searchMap.put("searchType", "");
	searchMap.put("searchText", "");
	searchMap.put("curPage", 1);
	searchMap.put("iLineCnt", iTotalCnt);
	searchMap.put("iTotalCnt", iTotalCnt);
	searchMap.put("massmailID", massmailID);
	searchMap.put("scheduleID", scheduleID);
	searchMap.put("standard", standard);
	searchMap.put("type", type);
	searchMap.put("key", key);
	
	//엑셀파일 저장 정보
	response.setHeader("Content-Disposition", "attachment; filename=" + standard + ".csv");
	response.setHeader("Content-Description", "JSP Generated Data");
	
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);
	List<MassMailPersonPreview> personPreviewList = service.personPreview(searchMap);
	
	String[] targetIDs = service.getTargetIDs(massmailID);
	List<OnetooneTarget> onetooneTargets = service.selectOnetooneByTargetID(targetIDs, massmailID);
	String[] otos = new String[onetooneTargets.size()];
	for(int i =0; i<onetooneTargets.size(); i++ )
		otos[i] = onetooneTargets.get(i).getOnetooneAlias();
	
	for(int i =0; i<personPreviewList.size(); i++ )
	{
		String oneToOne = personPreviewList.get(i).getOnetooneInfo();
		String temp = "";
		for(int t =0; t<otos.length; t++ )
			temp=temp+ThunderUtil.getOnetooneValueSpace(oneToOne,otos[t])+",";
		personPreviewList.get(i).setOnetooneInfo(temp);
	}
	
	
%>[이메일]<%for(OnetooneTarget onetooneTarget : onetooneTargets){%>,<%=onetooneTarget.getOnetooneName() %><%}if(standard.equals("failcause") || standard.equals("faildomain")){%>,[SMTP코드],[SMTP로그]<%}%>
<%	
	for(MassMailPersonPreview personPreview:personPreviewList){
%><%=personPreview.getEmail()%>,<%if(!personPreview.getOnetooneInfo().equals("")){%><%=personPreview.getOnetooneInfo()%><%}if(standard.equals("failcause") || standard.equals("faildomain")){%>,<%=personPreview.getSmtpCode()%>,<%=web.common.util.StringUtil.replace(personPreview.getSmtpMsg(),",","") %><%}%>
<%}%>