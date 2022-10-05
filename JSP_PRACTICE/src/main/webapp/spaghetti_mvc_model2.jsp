<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- MVC model2  --> 

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<%
	pageContext.setAttribute("r", "hello"); // 페이지내에서 이 저장소의 데이터(hello)를 사용할 수 있다.
%>
<body>
    <!-- Scope한정사로 명칭이 같은 저장소들 구분가능 -->
   	${requestScope.r} 입니다.                         <br><!-- <%=request.getAttribute("r")%>을 EL 표기법으로 간단히 할 수 있게 됨 -->
    ${name[0]}                                      <br><!-- 배열일 때  -->
	${n.title}                                      <br><!-- Map일 때  -->
	${empty pageScope.r?'값이 비어있다':pageScope.r}    <br><!-- page 저장소 활용  empty는변수 == null || 변수 == ''와 같다. -->
    ${param.n lt 3}                                 <br><!-- 파라미터는 입력 값이 들어와야 출력됨 -->
    ${header.accept}                                <br><!-- header의 accept 정보 가져옴 -->
</body>
</html>