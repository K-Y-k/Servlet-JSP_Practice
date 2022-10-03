package com.kyk_servlet.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/calc2")
public class Calc2 extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//ServletContext application = req.getServletContext(); // 값을 저장하기 위한 Application 객체 생성
        HttpSession session = req.getSession(); // Session 객체 생성
		Cookie[] cookies = req.getCookies();  // 브라우저의 쿠키 블러오기
        
        
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		// req.setCharacterEncoding("UTF-8");
		
		
		String v_ = req.getParameter("v");  // 확인을 위해 임시변수에 저장
		String operate_ = req.getParameter("operator");
		
		int v = 0; // 값을 입력하지 않았을 때를 위한 초기화
		if (!v_.equals(""))v = Integer.parseInt(v_); // 빈공백 제약
		
		if(operate_.equals("=")) { // 계산
			//int x = (Integer)application.getAttribute("value"); // application 저장소에서 값을 꺼내오는 것
			//int x = (Integer)session.getAttribute("value"); // session 저장소에서 값을 꺼내오는 것
			
			int x = 0;
			for (Cookie c : cookies) { // 해당하는 값을 찾기 위한 반복

				if (c.getName().equals("value")) { 
					x = Integer.parseInt(c.getValue());
					break;
				}
			}
			
			int y = v;
			//String operate = (String)application.getAttribute("operator");
			//String operate = (String)session.getAttribute("operator");

			String operate = "";
			for (Cookie c : cookies) { // 해당하는 값을 찾기 위한 반복

				if (c.getName().equals("operator")) { 
					operate = c.getValue();
					break;
				}
			}
			
			int result = 0;
			
			if(operate.equals("+")) {
				result = x + y;
			}
			else if(operate.equals("-")) {
				result = x - y;
			}
			resp.getWriter().printf("연산의 결과는 %d\n", result);
			
		}
		else { // 계산이 오지 않을 떄는 받아온 데이터 값 저장
//			application.setAttribute("value", v); // 키와 값을 저장소에 담는 것
//			application.setAttribute("operator", operate_);
			
//			session.setAttribute("value", v);  // 키와 값을 저장소에 담는 것
//			session.setAttribute("operator", operate_);
			
			Cookie valueCookie = new Cookie("value", String.valueOf(v));  // 쿠키는 url이 사용할 수 있는 문자형으로만 보내야한다.
			Cookie operateCookie = new Cookie("operator", operate_);
			
			valueCookie.setPath("/calc2"); // 설정한 path의 valueCookie를 가져오라는 뜻, 만약 /notice/이면 notice를 포함하는 하위 경로 요청시 적용
			valueCookie.setMaxAge(24*60*60); // 만료 기간 설정 시x분x초로 표현하면 가독성 향상
			operateCookie.setPath("/calc2"); // 설정한 path의 valueCookie를 가져오라는 뜻, 만약 /notice/이면 notice를 포함하는 하위 경로 요청시 적용
			
			resp.addCookie(valueCookie); // response 헤더에 쿠키를 심어 추가
			resp.addCookie(operateCookie);
			
			resp.sendRedirect("calc2.html"); // 해당 페이지 경로를 우회해서 전송해줌
			
		}
		
	}

}
