<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
    String cnt_ = request.getParameter("cnt"); // 확인을 위해 임시변수에 저장 
		
	int cnt = 10;   // 기본값 선언
		
	if(cnt_ !=null && !cnt_.equals("")) {   // null또는 빈문자열이 아닌 경우 (null은 String이 아니기에 null먼저 확인해야되서 순서 지켜야함)
		cnt = Integer.parseInt(cnt_);    // 쿼리 정상 진행  
	}
		
%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<% for(int i=0; i<cnt; i++) { %>
	안녕 Servlet!!
	<br>
	<% } %>
</body>
</html>