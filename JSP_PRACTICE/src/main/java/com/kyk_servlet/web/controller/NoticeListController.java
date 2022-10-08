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

@WebServlet("/notice/list")

public class NoticeListController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		// notice 객체를 여러개를 담기 위한 리스트 선언
		List<Notice> list = new ArrayList<>();
		
		String sql = "SELECT * FROM NOTICE";
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1"; // 오라클 thin 타입의 드라이버, 데이터베이스 서버 IP, 서비스하는 리스너의 포트번호, 서비스이름 
		String user = "kyk";
		String password = "kim690715";
		
		
		try {
			String driver = "oracle.jdbc.driver.OracleDriver";  
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()) { 
				int id = rs.getInt("ID");
				String title = rs.getString("TITLE");
				String writerId = rs.getString("WRITER_ID");
				Date regdate = rs.getDate("REGDATE");
				int hit = rs.getInt("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");
				
				Notice notice = new Notice(id, title, writerId, regdate, hit, files, content); // 게터 세터 때문에 오버로드된 생성자와 값을 채우는 순서가 일치해야함
				list.add(notice);
			} 
				
			rs.close(); 
			stmt.close(); 
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		
		request.setAttribute("list", list); // 리스트 request 저장소에 담기
		
		// forward
		request
		.getRequestDispatcher("/WEB-INF/view/notice/list.jsp") // forward를 사용하기 위한 Dispatcher로 기준 선정하여
		.forward(request, response); // 현재 사용하는 저장객체 request와 출력 도구 response를 공유한다.
				 
	}

}
