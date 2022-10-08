package com.kyk_servlet.web.controller;

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

@WebServlet("/notice/detail")
public class NoticeDetailController extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));

		String driver = "oracle.jdbc.driver.OracleDriver";
		String sql = "SELECT * FROM NOTICE WHERE ID = ?";

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1"; // 오라클 thin 타입의 드라이버, 데이터베이스 서버 IP, 서비스하는 리스너의 포트번호, 서비스이름 
		String user = "kyk";
		String password = "kim690715";

		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement pstmt = conn.prepareStatement(sql);  // ?를 미리 채울 수 있는 PreparedStatement로 괄호 안에 sql 넣기
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery(); // prepared에서 sql을 넣었기에 여기 괄호에는 sql 안들어감
			rs.next(); // 서버의 다음 레코드 하나를 가져온다. 

			String title = rs.getString("TITLE");
			String writerId = rs.getString("WRITER_ID");
			Date regdate = rs.getDate("REGDATE");
			int hit = rs.getInt("HIT");
			String files = rs.getString("FILES");
			String content = rs.getString("CONTENT");
			
			
			// 객체화했을 때의 request 저장소에 저장 사용방법
			Notice notice = new Notice(id, title, writerId, regdate, hit, files, content); // 게터 세터 때문에 오버로드된 생성자와 값을 채우는 순서가 일치해야함
			request.setAttribute("n", notice);

//			// 객체화하지 않을때의 request 저장소에 저장 사용방법
//			request.setAttribute("title", title);
//			request.setAttribute("writerId", writerId);
//			request.setAttribute("regdate", regdate);
//			request.setAttribute("hit", hit);
//			request.setAttribute("files", files);
//			request.setAttribute("content", content);

			rs.close();
			pstmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		// forward
		request
		.getRequestDispatcher("/WEB-INF/view/notice/detail.jsp") // forward를 사용하기 위한 Dispatcher로 기준 선정하여
		.forward(request, response); // 현재 사용하는 저장객체 request와 출력 도구 response를 공유한다.
		 
		
	}
}
