package com.kyk_servlet.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/calculator")
public class Calculator extends HttpServlet {

	// GET과 POST 요청 구분
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Cookie[] cookies = req.getCookies(); // 브라우저의 쿠키 블러오기

		String exp = "0";
		if (cookies != null) {
			for (Cookie c : cookies) { // 해당하는 쿠키 값을 찾기 위한 반복

				if (c.getName().equals("exp")) {
					exp = c.getValue();
					break;
				}
			}
		}

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		// req.setCharacterEncoding("UTF-8");

		PrintWriter out = resp.getWriter();
		out.write("<!DOCTYPE html>");
		out.write("<html>");
		out.write("<head>");
		out.write("<meta charset=\"UTF-8\">");
		out.write("<title>Insert title here</title>");
		out.write("<style>");
		out.write("input {");
		out.write("	width: 50px;");
		out.write("	height: 50px;");
		out.write("}");

		out.write(".output {");
		out.write("	height: 50px;");
		out.write("	background: #e9e9e9;");
		out.write("	font-size: 24px;");
		out.write("	font-weight: bold;");
		out.write("	text-align: right;");
		out.write("	padding: 0px 5px;");
		out.write("}");
		out.write("</style>");
		out.write("</head>");
		out.write("<body>");
		out.write("	<form method=\"post\">");
		out.write("		<table>");
		out.write("			<tr>");
		out.printf("				<td class=\"output\" colspan=\"4\">%s</td>", exp);
		out.write("			</tr>");
		out.write("			<tr>");
		out.write("				<td><input type=submit name=\"operator\" value=CE /></td>");
		out.write("				<td><input type=submit name=\"operator\" value=C /></td>");
		out.write("			<td><input type=submit name=\"operator\" value=BS /></td>");
		out.write("				<td><input type=submit name=\"operator\" value=/ /></td>");
		out.write("		</tr>");
		out.write("		<tr>");
		out.write("		<td><input type=submit name=\"value\" value=7 /></td>");
		out.write("		<td><input type=submit name=\"value\" value=8 /></td>");
		out.write("		<td><input type=submit name=\"value\" value=9 /></td>");
		out.write("		<td><input type=submit name=\"operator\" value=* /></td>");
		out.write("		</tr>");
		out.write("	<tr>");
		out.write("		<td><input type=submit name=\"value\" value=4 /></td>");
		out.write("		<td><input type=submit name=\"value\" value=5 /></td>");
		out.write("	<td><input type=submit name=\"value\" value=6 /></td>");
		out.write("	<td><input type=submit name=\"operator\" value=- /></td>");
		out.write("	</tr>");
		out.write("	<tr>");
		out.write("	<td><input type=submit name=\"value\" value=1 /></td>");
		out.write("	<td><input type=submit name=\"value\" value=2 /></td>");
		out.write("	<td><input type=submit name=\"value\" value=3 /></td>");
		out.write("	<td><input type=submit name=\"operator\" value=+ /></td>");
		out.write("	</tr>");
		out.write("<tr>");
		out.write("	<td></td>");
		out.write("	<td><input type=submit name=\"value\" value=0 /></td>");
		out.write("	<td><input type=submit name=\"dot\" value=. /></td>");
		out.write("	<td><input type=submit name=\"operator\" value== /></td>");
		out.write("	</tr>");

		out.write("</table>");

		out.write("</form>");
		out.write("</body>");
		out.write("</html>");

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Cookie[] cookies = req.getCookies(); // 브라우저의 쿠키 블러오기

		String value = req.getParameter("value"); // 확인을 위해 임시변수에 저장
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
		if (operator != null && operator.equals("=")) { // =인 경우는 계산
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn"); // 최신 자바버전은 GraalVM이라고 있고 구버전은
																						// ScriptEngine으로 자바 스크립트 구문
																						// 실행자를 통해 실행
			try {
				exp = String.valueOf(engine.eval(exp));
			} catch (ScriptException e) {
				e.printStackTrace();
			}

		} else if (operator != null && operator.equals("C")) { // C라면 쿠키 삭제
			exp = "";
		} else {
			// =이 아닌 경우 값, 연산자, 점 누적
			exp += (value == null) ? "" : value;
			exp += (operator == null) ? "" : operator;
			exp += (dot == null) ? "" : dot;

		}

		Cookie expCookie = new Cookie("exp", exp);
		if (operator != null && operator.equals("C")) {
			expCookie.setMaxAge(0);
		}
		expCookie.setPath("/calculator"); // 하나로 통일했으므로 자기 url로 설정 -> 다른 url에 쿠키가 전달될 필요가 없어져서 장점이 됨
		resp.addCookie(expCookie);
		resp.sendRedirect("calculator"); // 자기 페이지이므로 자기 페이지 명

	}

}
