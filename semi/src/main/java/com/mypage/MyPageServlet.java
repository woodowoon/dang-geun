package com.mypage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cmmu.CmmuDTO;
import com.member.SessionInfo;
import com.util.MyUploadServlet;

@MultipartConfig
@WebServlet("/mypage/*")
public class MyPageServlet extends MyUploadServlet{
	private static final long serialVersionUID = 1L;
	String pathname;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		if(info == null && req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}
		
		//파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		pathname = root+"uploads"+File.separator+"FAQ";
		
		if(uri.indexOf("list.do") != -1) {
			main(req, resp);
		} else if(uri.indexOf("list.do") != -1) {
			upload(req, resp);
		}
	}
	
	protected void main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MyPageDAO dao = new MyPageDAO();
		List<CmmuDTO> cmmuList = null;
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			
			// 커뮤니티
			String articleUrl = cp + "/community/article.do?page=1";
			
			int rCode = info.getrCode();
			String rName = dao.region(rCode);
			String userId = info.getUserId();
			cmmuList = dao.listCmmu(userId);
			
			int cmmuCount = dao.countCmmu(userId);
			
			req.setAttribute("rName", rName);
			req.setAttribute("cmmuCount", cmmuCount);
			req.setAttribute("cmmuList", cmmuList);
			req.setAttribute("articleUrl", articleUrl);
			
			forward(req, resp, "/WEB-INF/semi/mypage/main.jsp");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	protected void upload(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
}
