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
import com.kyk_servlet.web.entity.NoticeView;

// 서블릿을 업무 서비스로 따로 분리
public class NoticeService {
	public int removeNoticeAll(int[] ids){
	
		return 0;
	}
	
	// 배열로만 전달할 것인지 vs 사용자 편의에 따라 쉬운 자료형 받아오게 할 것인지 코딩은 더 많아지지만 후자가 더 좋음 (오버로드)
	public int pubNoticeAll(int[] oids, int[] cids){
		
		// 타입 변환 작업
		List<String> oidsList = new ArrayList<>();
		for(int i=0; i<oids.length; i++)
			oidsList.add(String.valueOf(oids[i]));
		
		List<String> cidsList = new ArrayList<>();
		for(int i=0; i<cids.length; i++)
			cidsList.add(String.valueOf(cids[i]));
		
		return pubNoticeAll(oidsList, cidsList); // 오버로드한 것이므로 각 타입으로만 변환해서 재호출
	}
	public int pubNoticeAll(List<String> oids, List<String> cids){
		
		// 타입 변환 작업
		String oidsCSV =String.join(",", oids);
		String cidsCSV =String.join(",", cids);
		
		return pubNoticeAll(oidsCSV, cidsCSV); // 오버로드한 것이므로 각 타입으로만 변환해서 재호출
	}
	public int pubNoticeAll(String oidsCSV, String cidsCSV){ // "23 24 25"
		
		int result = 0;
		
		// 둘 다 실행되어야하므로 선택적으로 실행하는 것이 아니기에 sql문이 2개가 필요
		String sqlOpen = String.format("UPDATE NOTICE SET PUB=1 WHERE ID IN (%s)", oidsCSV);  // String format을 활용한 방법
		String sqlClose = "UPDATE NOTICE SET PUB=0 WHERE ID IN ("+cidsCSV+")";
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1"; // 오라클 thin 타입의 드라이버, 데이터베이스 서버 IP, 서비스하는 리스너의 포트번호,
																// 서비스이름
		String user = "kyk";
		String password = "kim690715";

		try {
			// 연결은 한번이면 되지만
			String driver = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			
			// sql문이 2개이기에 2개를 실행하기 위한 준비물
			Statement stmtOpen = conn.createStatement(); // 미리 sql 준비
			int resultOpen = stmtOpen.executeUpdate(sqlOpen); // executeUpdate는 insert update delete일 때 사용

			Statement stmtClose = conn.createStatement(); // 미리 sql 준비
			int resultClose = stmtClose.executeUpdate(sqlClose); // executeUpdate는 insert update delete일 때 사용

			result = resultOpen + resultClose;
			
			stmtOpen.close();
			stmtClose.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
		
	}
	
	public int insertNotice(Notice notice) { // 글 등록 함수
		int result = 0;
		
		String sql = "INSERT INTO NOTICE(TITLE, CONTENT, WRITER_ID, PUB, FILES) VALUES(?,?,?,?,?)";
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1"; // 오라클 thin 타입의 드라이버, 데이터베이스 서버 IP, 서비스하는 리스너의 포트번호,
																// 서비스이름
		String user = "kyk";
		String password = "kim690715";
		
		try {
			String driver = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement pstmt = conn.prepareStatement(sql); // 미리 sql 준비
			
			pstmt.setString(1, notice.getTitle());
			pstmt.setString(2, notice.getContent());
			pstmt.setString(3, notice.getWriterId());
			pstmt.setBoolean(4, notice.getPub());
			pstmt.setString(5, notice.getFiles());
			
			result = pstmt.executeUpdate(); // executeUpdate는 insert update delete일 때 사용

			pstmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	public int deleteNotice(int id) {
		return 0;
	}
	
	public int updateNotice(Notice notice) {
		return 0;
	}
	
	public List<Notice> getNoticeNewestList(){
		return null;
	}
	
	
	// 함수명이 같으면(오버로드 관계) 기능이 같고 코드가 유사하다. 중복과 수정의 어려움이 있기에 하나만 구현하고 재호출하는 식으로 해야 코드관리가 편해진다.
	public List<NoticeView> getNoticeViewList() { 

		return getNoticeViewList("title", "", 1); // 재호출

	}

	public List<NoticeView> getNoticeViewList(int page) {

		return getNoticeViewList("title", "", page); // 재호출
	}

	public List<NoticeView> getNoticeViewList(String field, String query, int page) {  // 게시글 전체 보는 함수
		// NoticeView 객체를 여러개를 담기 위한 리스트 선언
		List<NoticeView> list = new ArrayList<>();
				
		String sql = "SELECT * FROM ("
					+ "    SELECT ROWNUM NUM, N.* "
			     	+ "    FROM (SELECT * FROM NOTICE_VIEW WHERE "+ field + " LIKE ? ORDER BY REGDATE DESC) N "
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
				//String content = rs.getString("CONTENT");
				int cmtCount = rs.getInt("CMT_COUNT");
				boolean pub = rs.getBoolean("PUB");
						
				NoticeView notice = new NoticeView(id, title, writerId, regdate, hit, files, pub, cmtCount); // 게터 세터 때문에 오버로드된 생성자와
																										// 값을 채우는 순서가 일치해야함
				list.add(notice);
			}

			rs.close();
			pstmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<NoticeView> getNoticePubViewList(String field, String query, int page) { // 공개된 게시글만 보는 함수
		// NoticeView 객체를 여러개를 담기 위한 리스트 선언
		List<NoticeView> list = new ArrayList<>();
				
		String sql = "SELECT * FROM ("
					+ "    SELECT ROWNUM NUM, N.* "
			     	+ "    FROM (SELECT * FROM NOTICE_VIEW WHERE "+ field + " LIKE ? ORDER BY REGDATE DESC) N "
					+ ") "
					+ "WHERE PUB=1 AND NUM BETWEEN ? AND ?"; // 1, 11, 21, 31 -> 등차수열 an = a1+(n-1)*10 -> an = 1+(page-1)*10
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
				//String content = rs.getString("CONTENT");
				int cmtCount = rs.getInt("CMT_COUNT");
				boolean pub = rs.getBoolean("PUB");
						
				NoticeView notice = new NoticeView(id, title, writerId, regdate, hit, files, pub, cmtCount); // 게터 세터 때문에 오버로드된 생성자와
																										// 값을 채우는 순서가 일치해야함
				list.add(notice);
			}

			rs.close();
			pstmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
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
				+ "    FROM (SELECT * FROM NOTICE WHERE "+ field + " LIKE ? ORDER BY REGDATE DESC) N "
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
			
			if(rs.next())
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
				boolean pub = rs.getBoolean("PUB");

				notice = new Notice(id, title, writerId, regdate, hit, files, content, pub); // 게터 세터 때문에 오버로드된 생성자와
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
				boolean pub = rs.getBoolean("PUB");

				notice = new Notice(id, title, writerId, regdate, hit, files, content, pub); // 게터 세터 때문에 오버로드된 생성자와
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
				boolean pub = rs.getBoolean("PUB");

				notice = new Notice(id, title, writerId, regdate, hit, files, content, pub); // 게터 세터 때문에 오버로드된 생성자와
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
	
	public int deleteNoticeALL(int[] ids) {

		int result = 0;
		
		String params = "";
		
		for(int i=0; i<ids.length; i++) { // 1,2,3 같은 문자열 형태로 만들기 위한 반복
			params += ids[i];
			
			if(i <= ids.length-1) // 마지막 끝이 같기 전까지 반복
				params +=",";
		}
		String sql = "DELETE NOTICE WHERE ID IN (" + params + ")";
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1"; // 오라클 thin 타입의 드라이버, 데이터베이스 서버 IP, 서비스하는 리스너의 포트번호,
																// 서비스이름
		String user = "kyk";
		String password = "kim690715";
		
		try {
			String driver = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement(); // 미리 sql 준비
			
			result = stmt.executeUpdate(sql); // executeUpdate는 insert update delete일 때 사용

			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
}
