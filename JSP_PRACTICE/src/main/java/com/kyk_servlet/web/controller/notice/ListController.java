package com.kyk_servlet.web.controller.notice;

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
import com.kyk_servlet.web.entity.NoticeView;
import com.kyk_servlet.web.service.NoticeService;

@WebServlet("/notice/list")

public class ListController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// list?f=title&q=~ 이런 형태로 전송된다
		
		String field_ = request.getParameter("f"); 
		String query_ = request.getParameter("q"); 
		String page_  = request.getParameter("p"); // int형은 null을 받을 수 없으므로 null을 받기위한 String으로 선언

		String field = "title"; // 전달 안됐을 때의 기본 값
		if(field_ != null && !field_.equals("")) // 사용자에게 전달된 값이 있을 경우
			field = field_;
		
		String query = ""; // 전달 안됐을 때의 기본 값
		if(query_ != null && !query_.equals("")) // 사용자에게 전달된 값이 있을 경우
			query = query_;
		
		int page = 1; // 전달 안됐을 때의 기본 값
		if(page_ != null && !page_.equals("")) // 사용자에게 전달된 값이 있을 경우
			page = Integer.parseInt(page_);
		
		
		// notice 객체를 여러개를 담기 위한 리스트 선언
		NoticeService service = new NoticeService(); // NoticeService에서 가져옴
		List<NoticeView> list = service.getNoticePubViewList(field, query, page);
		
		int count = service.getNoticeCount(field, query); // 레코드 개수를 알기 위한 count 선언
		
		
		request.setAttribute("list", list); // 리스트 request 저장소에 담아 view에 전달
		request.setAttribute("count", count);
		
		
		// forward
		request
		.getRequestDispatcher("/WEB-INF/view/notice/list.jsp") // forward를 사용하기 위한 Dispatcher로 기준 선정하여
		.forward(request, response); // 현재 사용하는 저장객체 request와 출력 도구 response를 공유한다.
				 
	}

}
