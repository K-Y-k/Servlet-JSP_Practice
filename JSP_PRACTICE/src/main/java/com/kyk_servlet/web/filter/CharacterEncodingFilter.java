package com.kyk_servlet.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req
			, ServletResponse resp
			, FilterChain chain)
			throws IOException, ServletException {
		
		req.setCharacterEncoding("UTF-8");
		//System.out.println("chain 실행 전에 해야할 설정들"); 
		chain.doFilter(req, resp); // 흐름을 넘겨 다음 필터 또는 서블릿 실행
		//System.out.println("chain 실행 후에 해야할 것들"); 

	}

}
