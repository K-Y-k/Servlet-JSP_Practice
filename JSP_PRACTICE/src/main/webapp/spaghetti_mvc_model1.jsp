<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- 스파게티 코드를 MVC model1화  --> 
<%
	int num = 0;
	String num_ = request.getParameter("n");
	if(num_ != null && !num_.equals(""))
		num = Integer.parseInt(num_);
	
	String result;
	if (num % 2 == 0) 
		result = "짝수";

   else
	   result = "짝수";

%>




<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
    <%=result %>입니다.
   
</body>
</html>