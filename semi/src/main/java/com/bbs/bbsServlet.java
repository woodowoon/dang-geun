package com.bbs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.SemiServlet;

@WebServlet("/bbs/*")
public class bbsServlet extends SemiServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		
		if(uri.indexOf("list.do") != -1) {
			bbslist(req, resp);
		} else if(uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if(uri.indexOf("write.do") != -1) {
			write(req, resp);
		} 
	}
	
	
	protected void bbslist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = "/WEB-INF/semi/bbs/list.jsp";
		forward(req, resp, path);
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = "/WEB-INF/semi/bbs/article.jsp";
		forward(req, resp, path);
	}
	
	protected void write(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = "/WEB-INF/semi/bbs/write.jsp";
		forward(req, resp, path);
	}
	
}
