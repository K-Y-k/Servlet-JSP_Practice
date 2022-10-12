package com.kyk_servlet.web.controller.admin.notice;

import java.util.Date;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kyk_servlet.web.entity.Notice;
import com.kyk_servlet.web.service.NoticeService;

@WebServlet("/admin/board/notice/detail")
public class DetailController extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));

		NoticeService service = new NoticeService();
		Notice notice = service.getNotice(id);  // id를 전달하여 글 가져오기
		request.setAttribute("n", notice);
		
//		// 따로 서비스를 만들지 않고 내부에서 객체화했을 때의 request 저장소에 저장 사용방법
//		Notice notice = new Notice(id, title, writerId, regdate, hit, files, content); // 게터 세터 때문에 오버로드된 생성자와 값을 채우는 순서가 일치해야함
//		request.setAttribute("n", notice);

//		// 객체화하지 않을때의 request 저장소에 저장 사용방법
//		request.setAttribute("title", title);
//		request.setAttribute("writerId", writerId);
//		request.setAttribute("regdate", regdate);
//		request.setAttribute("hit", hit);
//		request.setAttribute("files", files);
//		request.setAttribute("content", content);
		
		
		// forward
		request
		.getRequestDispatcher("/WEB-INF/view/admin/board/notice/detail.jsp") // forward를 사용하기 위한 Dispatcher로 기준 선정하여
		.forward(request, response); // 현재 사용하는 저장객체 request와 출력 도구 response를 공유한다.
		 
		
	}
}
