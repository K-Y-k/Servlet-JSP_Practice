package com.kyk_servlet.web.controller.admin.notice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.kyk_servlet.web.entity.Notice;
import com.kyk_servlet.web.service.NoticeService;

@MultipartConfig( // 파일업로드를 위한 서블릿 설정 이노테이션
		//location = "", // 파일용량이 크면 서버 메모리가 부족하기에 일정량이 넘으면 디스크에 임시저장한다는 뜻 기본 값은 설정안함
		fileSizeThreshold = 1024*1024,   // 이 속성은 디스크 임시저장의 최소 기준값 설정
		maxFileSize = 1024*1024*50,       // 파일이 너무 크면 서비스 마비로 파일 하나의 최대용량 제한
		maxRequestSize = 1024*1024*50*5   // 파일 전체요청 용량 제한
)
@WebServlet("/admin/board/notice/reg")
public class RegController extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// forward
		request
		.getRequestDispatcher("/WEB-INF/view/admin/board/notice/reg.jsp") // forward를 사용하기 위한 Dispatcher로 기준 선정하여
		.forward(request, response); // 현재 사용하는 저장객체 request와 출력 도구 response를 공유한다.

	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		System.out.println("title : "+title);
		
		String content = request.getParameter("content");
		String isOpen = request.getParameter("open");
		
		Collection<Part> parts = request.getParts(); // 여러 파일을 받기 위한 Collection 객체와 getParts()
		StringBuilder builder = new StringBuilder(); // 데이터베이스에 저장하기 위한 StringBuilder 생성
		
		for(Part p : parts) { // 반복문을 통해 여러 파일 담아오기
			if(!p.getName().equals("file")) continue; // 이름이 파일이 아니면 패스
			
			// 이름이 파일이 맞으면
			Part filePart = p;  // 1.Part 자료형으로 저장
			String fileName = filePart.getSubmittedFileName(); // 파일명 가져오기
			builder.append(fileName);
			builder.append(",");
			
			InputStream fis = filePart.getInputStream(); // 2.Part를 바이너리형으로 받음
			
			// "/upload/" -> "c:upload\"이러한 절대경로로 변경해야한다.
			String realPath = request.getServletContext().getRealPath("/upload"); // 3.동적으로 절대경로로 변환
			System.out.println(realPath);
			
			String filePath = realPath + File.separator + fileName;  // 저장할 경로 설정 \같은 경로 구분은 File.separator이 대신해줌
			FileOutputStream fos = new FileOutputStream(filePath); // 출력하기 위한 버퍼 생성
			
			
			// 여러 문자는 스캐너 단일 문자는 read
			byte[] buf = new byte[1024]; // 시간이 오래 걸리지 않게 바이트 단위 설정
			int size = 0;  // 들어온 데이터 바이트의 개수
			while((size = fis.read(buf)) != -1) // 반복문으로 1바이트 단위로 가져옴
				fos.write(buf, 0, size); // 바이트의 크기가 꼭 1024까지가 아니기에 0번째~size번째까지 구간설정하여 읽어옴
			
			fos.close();
			fis.close();	
		}
		
		builder.delete(builder.length()-1, builder.length()); // ,를 빼주기 위한 작업 길어-1부터 길어-1까지로 
		
		boolean pub = false;
		if(isOpen != null) // 값이 전달 되었으면 pub 변환
			pub = true;
		
		// Notice 객체 통해 원하는 입력을 한다.
		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setPub(pub);
		notice.setWriterId("kyk123");
		notice.setFiles(builder.toString()); // builder의 값을 문자열로
		
		NoticeService service = new NoticeService(); 
		service.insertNotice(notice);
		
		response.sendRedirect("list"); // 새로운 페이지 요청
		                               // ("/admin/board/notice/reg") -> ("/admin/board/notice/list")로 요청됨
		
//		request.setCharacterEncoding("UTF-8");
//		response.setContentType("text/html; charset=UTF-8");
//		
//		PrintWriter out = response.getWriter();
//		out.printf("title : %s <br>", title);
//		out.printf("content : %s <br>", content);
//		out.printf("open : %s <br>", isOpen);
		
	}
}
