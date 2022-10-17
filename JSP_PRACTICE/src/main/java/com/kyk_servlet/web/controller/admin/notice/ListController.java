package com.kyk_servlet.web.controller.admin.notice;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
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

@WebServlet("/admin/board/notice/list")

public class ListController extends HttpServlet {
	// 403 : 보안 오류
	// 404 : url 오류
	// 405 : 메서드 오류
	
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
		List<NoticeView> list = service.getNoticeViewList(field, query, page);
		
		int count = service.getNoticeCount(field, query); // 레코드 개수를 알기 위한 count 선언
		
		
		request.setAttribute("list", list); // 리스트 request 저장소에 담아 view에 전달
		request.setAttribute("count", count);
		
		
		// forward
		request
		.getRequestDispatcher("/WEB-INF/view/admin/board/notice/list.jsp") // forward를 사용하기 위한 Dispatcher로 기준 선정하여
		.forward(request, response); // 현재 사용하는 저장객체 request와 출력 도구 response를 공유한다.
				 
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] openIds = request.getParameterValues("open-id"); // 공개한 id
		String[] delIds = request.getParameterValues("del-id");
		String cmd = request.getParameter("cmd");
		String ids_ = request.getParameter("ids"); // 공개 id를 비교해서 확인하기 위해 받아온 전체 id
		String[] ids = ids_.trim().split(" "); // 받아온 전체 id들 배열로 분리
		
		NoticeService service = new NoticeService();
		
		switch(cmd) { // 찍힌건 모두 전달되지만 value 값의 조건을 두어 해당하는 것만 찍히게 한다.
		case "일괄공개":
			for(String openId : openIds)
				System.out.printf("opened id : %s\n", openId);
			
			List<String> oids = Arrays.asList(openIds); // 배열을 리스트 형태로 바꿀 때 사용
			
			List<String> cids = new ArrayList(Arrays.asList(ids)); // 전체 id로 받아온 뺀 id 리스트 객체로 선언
			cids.removeAll(oids); // 전체 id - 공개 id = 비공개 id
			System.out.println(Arrays.asList(ids));
			System.out.println(oids);
			System.out.println(cids);
			
			// 공개 비공개 설정한 것을 서비스에 보내서 업데이트
			// Transaction 처리 = 둘 다 한번에 실행되어야한다.
			service.pubNoticeAll(oids, cids);
			
			break;
			
		case "일괄삭제":
			
			int[] ids1 = new int[delIds.length];
			for(int i=0; i<delIds.length; i++) 
				ids1[i] = Integer.parseInt(delIds[i]);
			int result = service.deleteNoticeALL(ids1);
			
			break;
		}
		response.sendRedirect("list"); // 지워진 상태에서 리다이렉트를 하여 목록 doGet 요청
	}

}
