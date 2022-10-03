package com.kyk_servlet.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/doGetPost")
public class DoGetPost extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// GET과 POST 요청 구분법
		// 첫 번째 방법
		if(req.getMethod().equals("GET")) {  // html에서의 선언은 소문자로도 되지만 java 클래스에서는 무조건 대문자로 해야 인식된다!
			System.out.println("GET요청이 왔습니다.");
		}
		else if(req.getMethod().equals("POST")) {
			System.out.println("POST요청이 왔습니다.");
			
		}
		super.service(req, resp); //부모가 가지는 service 호출 즉, 오버라이드를 했지만 본래 service가 호출되는 것이다. 
		                          //즉 GET이 왔으면 GET관련 메서드인 doGet 실행됨 
		                          // service 함수 선언이 없고 doGet/doPost 메서드로 작성 시 super 지움
		
	}
	// 두 번째 방법
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("doGet 메소드가 호출 되었습니다.");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("doPost 메소드가 호출 되었습니다.");
	}
	
}

	
