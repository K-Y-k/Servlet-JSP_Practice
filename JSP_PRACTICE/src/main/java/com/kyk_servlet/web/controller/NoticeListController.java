package com.kyk_servlet.web.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kyk_servlet.web.entity.Notice;
import com.kyk_servlet.web.service.NoticeService;

@WebServlet("/notice/list")

public class NoticeListController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		// notice 객체를 여러개를 담기 위한 리스트 선언
		NoticeService service = new NoticeService(); // NoticeService에서 가져옴
		List<Notice> list = service.getNoticeList();
		
		
		
		
		request.setAttribute("list", list); // 리스트 request 저장소에 담기
		
		// forward
		request
		.getRequestDispatcher("/WEB-INF/view/notice/list.jsp") // forward를 사용하기 위한 Dispatcher로 기준 선정하여
		.forward(request, response); // 현재 사용하는 저장객체 request와 출력 도구 response를 공유한다.
				 
	}

}
