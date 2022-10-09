package com.kyk_servlet.web.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kyk_servlet.web.entity.Notice;

// 서블릿을 업무 서비스로 따로 분리
public class NoticeService {
	// 함수명이 같으면(오버로드 관계) 기능이 같고 코드가 유사하다. 중복과 수정의 어려움이 있기에 하나만 구현하고 재호출하는 식으로 해야 코드관리가 편해진다.
	public List<Notice> getNoticeList() { 

		return getNoticeList("title", "", 1); // 재호출

	}

	public List<Notice> getNoticeList(int page) {

		return getNoticeList("title", "", page); // 재호출
	}

	public List<Notice> getNoticeList(String field/*TITLE, WRIRER_ID*/, String query/*A*/, int page) { // 목록을 얻기위한 함수

		// notice 객체를 여러개를 담기 위한 리스트 선언
		List<Notice> list = new ArrayList<>();
		
		String sql = "SELECT * FROM ("
				+ "    SELECT ROWNUM NUM, N.* "
				+ "    FROM (SELECT * FROM NOTICE WHERE "+ field + " LIKE ? ORDER BY REGDATE DESC) N "
				+ ") "
				+ "WHERE NUM BETWEEN ? AND ?"; // 1, 11, 21, 31 -> 등차수열 an = a1+(n-1)*10 -> an = 1+(page-1)*10
		                                        // 10, 20, 30, 40 -> page*10 
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1"; // 오라클 thin 타입의 드라이버, 데이터베이스 서버 IP, 서비스하는 리스너의 포트번호,
																// 서비스이름
		String user = "kyk";
		String password = "kim690715";

		try {
			String driver = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement pstmt = conn.prepareStatement(sql); // 미리 sql 준비
			
			pstmt.setString(1, "%"+query+"%");
			pstmt.setInt(2, 1+(page-1)*10);
			pstmt.setInt(3, page*10);
			
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				String title = rs.getString("TITLE");
				String writerId = rs.getString("WRITER_ID");
				Date regdate = rs.getDate("REGDATE");
				int hit = rs.getInt("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");

				Notice notice = new Notice(id, title, writerId, regdate, hit, files, content); // 게터 세터 때문에 오버로드된 생성자와
																								// 값을 채우는 순서가 일치해야함
				list.add(notice);
			}

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

		return list;
	}
	
	public int getNoticeCount() { // 카운트를 얻기위한 함수
		
		return getNoticeCount("title","");
	}
	
	public int getNoticeCount(String field, String query) { // 목록을 가져왔을 때 페이징 되지 않은 레코드 개수 확인 메서드
		
		int count = 0;
		
		String sql = "SELECT COUNT(ID) COUNT FROM ("
				+ "    SELECT ROWNUM NUM, N.* "
				+ "    FROM (SELECT * FROM NOTICE WHERE \"+ field + \" LIKE ? ORDER BY REGDATE DESC) N "
				+ ") ";
				
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1"; // 오라클 thin 타입의 드라이버, 데이터베이스 서버 IP, 서비스하는 리스너의 포트번호,
																// 서비스이름
		String user = "kyk";
		String password = "kim690715";

		
		try {
			String driver = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement pstmt = conn.prepareStatement(sql); // 미리 sql 준비
			
			pstmt.setString(1, "%"+query+"%");
			
			ResultSet rs = pstmt.executeQuery();

			count = rs.getInt("COUNT");
			
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
		
		return count;
	}
	
	public Notice getNotice(int id) { // id 하나만 해당하는 게시글 가져오기
		Notice notice = null;
		
		String sql = "SELECT * FROM NOTICE WHERE ID=?";

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1"; // 오라클 thin 타입의 드라이버, 데이터베이스 서버 IP, 서비스하는 리스너의 포트번호,
																// 서비스이름
		String user = "kyk";
		String password = "kim690715";

		try {
			String driver = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement pstmt = conn.prepareStatement(sql); // 미리 sql 준비
			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) { // 가져올 것이 있으면 읽어온다.
				int nid = rs.getInt("ID");
				String title = rs.getString("TITLE");
				String writerId = rs.getString("WRITER_ID");
				Date regdate = rs.getDate("REGDATE");
				int hit = rs.getInt("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");

				notice = new Notice(id, title, writerId, regdate, hit, files, content); // 게터 세터 때문에 오버로드된 생성자와
				// 값을 채우는 순서가 일치해야함
			
			}

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

		return notice;
	}
	
	public Notice getNextNotice(int id) { // 다음글을 알기 위한 함수(현재 게시글 id를 통해)
		Notice notice = null;
		
		String sql = "SELECT * FROM NOTICE "
				+ "WHERE ID = ( "
				+ "    SELECT ID FROM NOTICE "
				+ "    WHERE REGDATE > (SELECT REGDATE FROM NOTICE WHERE ID=?) "
				+ "    AND ROWNUM = 1 "
				+ ")";
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1"; // 오라클 thin 타입의 드라이버, 데이터베이스 서버 IP, 서비스하는 리스너의 포트번호,
																// 서비스이름
		String user = "kyk";
		String password = "kim690715";
		
		try {
			String driver = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement pstmt = conn.prepareStatement(sql); // 미리 sql 준비
			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) { // 가져올 것이 있으면 읽어온다.
				int nid = rs.getInt("ID");
				String title = rs.getString("TITLE");
				String writerId = rs.getString("WRITER_ID");
				Date regdate = rs.getDate("REGDATE");
				int hit = rs.getInt("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");

				notice = new Notice(id, title, writerId, regdate, hit, files, content); // 게터 세터 때문에 오버로드된 생성자와
				// 값을 채우는 순서가 일치해야함
			
			}

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

		
		return notice;
	}
	
	public Notice getPrevNotice(int id) {// 이전글을 알기 위한 함수(현재 게시글 id를 통해)
		Notice notice = null;
		
		String sql = "SELECT ID FROM (SELECT * FROM NOTICE ORDER BY REGDATE DESC) "
				+ "WHERE REGDATE < (SELECT REGDATE FROM NOTICE WHERE ID=?) "
				+ "AND ROWNUM = 1";
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1"; // 오라클 thin 타입의 드라이버, 데이터베이스 서버 IP, 서비스하는 리스너의 포트번호,
																// 서비스이름
		String user = "kyk";
		String password = "kim690715";
		
		try {
			String driver = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement pstmt = conn.prepareStatement(sql); // 미리 sql 준비
			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) { // 가져올 것이 있으면 읽어온다.
				int nid = rs.getInt("ID");
				String title = rs.getString("TITLE");
				String writerId = rs.getString("WRITER_ID");
				Date regdate = rs.getDate("REGDATE");
				int hit = rs.getInt("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");

				notice = new Notice(id, title, writerId, regdate, hit, files, content); // 게터 세터 때문에 오버로드된 생성자와
				// 값을 채우는 순서가 일치해야함
			
			}

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

		return notice;
	}
}
