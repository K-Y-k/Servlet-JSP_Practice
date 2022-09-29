package com.kyk_servlet.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class Test extends HttpServlet {
	
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		
		PrintWriter out = resp.getWriter();
		
		// 수동적으로 지정한 반복
		for(int i=0; i<7; i++) {  
			out.println((i+1)+": 안녕! <br/>");
		}
		
		// 사용자에게 값을 받아와서 능동적으로 지정한 반복 GET과 QueryString 활용
		String cnt_ = req.getParameter("cnt");  // 확인을 위해 임시변수에 저장
		
		int cnt = Integer.parseInt(cnt_);      // 기본값 선언
		
		if(cnt_ !=null && !cnt_.equals("")) {  // null또는 빈문자열이 아닌 경우 (null은 String이 아니기에 null먼저 확인해야되서 순서 지켜야함)
			 cnt = Integer.parseInt(cnt_);     // 쿼리 정상 진행
		}
		
		for(int i=0; i<cnt; i++) {
			out.println((i+1)+": 안녕! <br/>");
		}
		
	}

}
