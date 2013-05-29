<%@ page contentType="application/dat; charset=euc-kr"%><%
    response.addHeader("Content-Disposition","attachment;filename=sample.csv");
	out.println("[이메일],[고객명],[고객번호],[휴대폰]");
	out.println("yhlim@andwise.com,임박사,9793,010-0000-2826");
	out.println("egpark@andwise.com,박박사,9794,010-0000-8249");
	out.println("khkim@andwise.com,김박사,9795,010-0000-6587");
%>