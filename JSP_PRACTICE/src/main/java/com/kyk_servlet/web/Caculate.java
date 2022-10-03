package com.kyk_servlet.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/calculate")
public class Caculate extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		// req.setCharacterEncoding("UTF-8");
		
		
		String x_ = req.getParameter("x");  // 확인을 위해 임시변수에 저장
		String y_ = req.getParameter("y");
		String operate_ =req.getParameter("operater");
		
		int result = 0;
		
		int x = 0;
		int y = 0;
		
		if (!x_.equals(""))x = Integer.parseInt(x_);
		if (!y_.equals(""))y = Integer.parseInt(y_);
		
		if (operate_.equals("덧셈")) {
			result = x + y;
		}
		else if (operate_.equals("뺄셈")) {
			 result = x - y;
		}
		
//		// 배열로 저장할 시의 연산
//		String[] num_ = req.getParameterValues("num");
//		
//		if (operate_.equals("덧셈")) {
//			for (int i = 0; i < num_.length; i++) {
//				int num = Integer.parseInt(num_[i]);
//				result += num;
//			}
//		}
//
//		else if (operate_.equals("뺄셈")) {
//			for (int i = 0; i < num_.length; i++) {
//				int num = Integer.parseInt(num_[i]);
//				if(num == Integer.parseInt(num_[0])){
//						result += num;
//				}
//				else {
//					result -= num;
//				}
//			}
//		}
		
		resp.getWriter().printf("연산의 결과는 %d\n", result);
	}

}
