package com.kyk_servlet.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/calc3")
public class Calc3 extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//ServletContext application = req.getServletContext(); // 값을 저장하기 위한 Application 객체 생성
        //HttpSession session = req.getSession(); // Session 객체 생성
		Cookie[] cookies = req.getCookies();  // 브라우저의 쿠키 블러오기
        
		String value = req.getParameter("value");  // 확인을 위해 임시변수에 저장
		String operator = req.getParameter("operator");
		String dot = req.getParameter("dot");
		
		String exp = "";
		if (cookies != null) {
			for (Cookie c : cookies) { // 해당하는 쿠키 값을 찾기 위한 반복

				if (c.getName().equals("exp")) {
					exp = c.getValue();
					break;
				}
			}
		}
		if(operator != null && operator.equals("=")) { // =인 경우는 계산
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn"); // 최신 자바버전은 GraalVM이라고 있고 구버전은 ScriptEngine으로 자바 스크립트 구문 실행자를 통해 실행 
			try {
				exp = String.valueOf(engine.eval(exp));
			} catch (ScriptException e) {
				e.printStackTrace();
			}
			
		}
		else if(operator != null && operator.equals("C")) { // C라면 쿠키 삭제
			exp = "";
		}
		else {
			// =이 아닌 경우 값, 연산자, 점 누적
			exp += (value == null)? "":value;  
			exp += (operator == null)? "":operator;
			exp += (dot == null)? "":dot;
		
		}
		
		Cookie expCookie = new Cookie("exp", exp);
		if(operator != null && operator.equals("C")) {
			expCookie.setMaxAge(0);
		}
		resp.addCookie(expCookie);
		resp.sendRedirect("calcpage"); // 해당 페이지 경로를 우회해서 전송해줌 (자바에서 자바로 넘어갈 때는 경로가 필요 없으므로 명칭만)
		
	}

}
