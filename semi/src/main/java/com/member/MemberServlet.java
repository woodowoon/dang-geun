package com.member;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.SemiServlet;

@WebServlet("/member/*")
public class MemberServlet extends SemiServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		
		if(uri.indexOf("login.do") != -1) {
			loginForm(req, resp);
		} else if(uri.indexOf("login_ok.do") != -1) {
			loginSubmit(req, resp);
		} else if(uri.indexOf("logout.do") != -1) {
			logout(req, resp);
		} else if(uri.indexOf("join.do") != -1) {
			joinForm(req, resp);
		} else if(uri.indexOf("join_ok.do") != -1) {
			joinSubmit(req, resp);
		}
	}

	// 로그인
	protected void loginForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = "/WEB-INF/semi/member/login.jsp";
		forward(req, resp, path);
	}
	
	// 로그인 처리
	protected void loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		
		MemberDAO dao = new MemberDAO();
		String cp = req.getContextPath();
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/");
			return;
		}
		
		String userId = req.getParameter("userId");
		String uPwd = req.getParameter("uPwd");
		
		MemberDTO dto = dao.loginMember(userId, uPwd);
		
		if(dto != null) {
			session.setMaxInactiveInterval(60 * 60);
			
			SessionInfo info = new SessionInfo();
			info.setUserId(dto.getUserId());
			info.setuNick(dto.getuNick());
			info.setrCode(dto.getrCode());
			info.setuName(dto.getuName());
			info.setuPwd(dto.getuPwd());
			info.setuRole(dto.getuRole());
			info.setPhotoName(dto.getPhotoName());
			
			session.setAttribute("member", info);
			
			resp.sendRedirect(cp + "/");
			return;
		}
		
		// 로그인 실패시
		String msg = "아이디, 패스워드를 다시 확인해주세요.";
		req.setAttribute("message", msg);
		
		forward(req, resp, "/WEB-INF/semi/member/login.jsp");
	}
	
	// 로그아웃
	protected void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String cp = req.getContextPath();
		
		session.removeAttribute("member");
		
		session.invalidate();
		
		resp.sendRedirect(cp + "/");
	}
	
	protected void joinForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = "/WEB-INF/semi/member/join.jsp";
		forward(req, resp, path);
	}
	
	
	protected void joinSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MemberDAO dao = new MemberDAO();
		String cp = req.getContextPath();
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/");
			return;
		}
		
		String message = "";
		try {
			MemberDTO dto = new MemberDTO();
			
			dto.setUserId(req.getParameter("userId"));
			dto.setuPwd(req.getParameter("uPwd"));
			dto.setuName(req.getParameter("uName"));
			dto.setuNick(req.getParameter("uNick"));
			
			String tel;
			tel = req.getParameter("uTel1") + req.getParameter("uTel2") + req.getParameter("uTel3");
			dto.setuTel(tel);
			
			dto.setPhotoName(req.getParameter("photoName"));
			dto.setrCode(Integer.parseInt(req.getParameter("rCode")));
			
			dao.insertMember(dto);
			resp.sendRedirect(cp + "/");
			return;
			
		} catch (SQLException e) {
			if(e.getErrorCode() == 1) {
				message = "아이디 중복으로 회원가입에 실패했습니다";
			} else if(e.getErrorCode() == 1400) {
				message = "필수사항을 입력하지 않아 회원가입에 실패했습니다";
			} else {
				message = "회원가입에 실패했습니다";
			}
		} catch (Exception e) {
			message = "회원가입에 실패했습니다";
			e.printStackTrace();
		}
		
		req.setAttribute("message", message);
		forward(req, resp, "/WEB-INF/semi/member/join.jsp");
	}
	
}
