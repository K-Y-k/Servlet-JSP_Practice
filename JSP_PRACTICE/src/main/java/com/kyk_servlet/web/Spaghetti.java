package com.kyk_servlet.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/mvc_model2")
public class Spaghetti extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int num = 0;
		String num_ = req.getParameter("n");
		if(num_ != null && !num_.equals(""))
			num = Integer.parseInt(num_);
		
		String result;
		if (num % 2 == 0) 
			result = "짝수";

	   else
		   result = "홀수";
		
		req.setAttribute("r", result);
		
		String[] name = {"yk", "dra"};
		req.setAttribute("name", name);
		
		Map<String, Object> notice = new HashMap<String, Object>();
		notice.put("id", 1);
		notice.put("title", "안녕하세요");
		
		req.setAttribute("n", notice);
		
		//redirect : 현재 작업과 관련없이 새로운 요청을 하는 것
		//forward : 현재 작업한 내용을 이어감
		RequestDispatcher dispatcher = req.getRequestDispatcher("spaghetti_mvc_model2.jsp");
		dispatcher.forward(req, resp); // 현재 작업한 내용을 여기에 담겨있다면 그 내용이 위 jsp 서블릿으로 이어져서 요청을 진행
		
	}
}
