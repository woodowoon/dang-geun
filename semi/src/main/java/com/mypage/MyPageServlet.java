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
import com.sell.sellDTO;
import com.util.MyUploadServlet;
import com.util.MyUtil;

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
		MyUtil util = new MyUtil();
		List<CmmuDTO> cmmuList = null;
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			// 커뮤니티
			String articleUrl = cp + "/community/article.do?page=1";
			String listUrl = cp+"/community/article.do";

			int rCode = info.getrCode();
			String rName = dao.region(rCode);
			String userId = info.getUserId();
			cmmuList = dao.listCmmu(userId);
			
			int cmmuCount = dao.countCmmu(userId);
			
			req.setAttribute("rName", rName);
			req.setAttribute("cmmuCount", cmmuCount);
			req.setAttribute("cmmuList", cmmuList);
			req.setAttribute("articleUrl", articleUrl);
			
			// 판매 중, 거래 중 상품
			String sellPage = req.getParameter("page"); 
			int sellCurrentPage = 1; // 판매중의 현재페이지

			if(sellPage != null) {
				sellCurrentPage = Integer.parseInt(sellPage);
			}
			
			int rows = 5; // 한 페이지에 나오는 열 개수
			int sellCount = dao.dataCount("sell", info.getUserId());
			int sellTotal = util.pageCount(rows, sellCount);
			if(sellCurrentPage > sellTotal) {
				sellCurrentPage = sellTotal;
			}
			
			int sellStart = (sellCurrentPage - 1) * rows + 1;
			int sellEnd = sellCurrentPage * rows;
			
			List<sellDTO> sellList = null;
			sellList = dao.listSell("sell", info.getUserId(), sellStart, sellEnd);
			
			String SellQuery = "sellPage="+sellPage+"&sellCurrentPage="+sellCurrentPage ;
			
			String sellListUrl = cp + "/mypage/list.do";
			String sellArticleUrl = cp + "/sell/article.do";
			if(SellQuery.length() != 0) {
				//sellArticleUrl += "?" + SellQuery;
				sellListUrl += "?" + SellQuery;
			}
		
			String sellPaging =  util.paging(sellCurrentPage, sellTotal, sellListUrl);
			
			req.setAttribute("SellQuery", SellQuery);
			req.setAttribute("sellCount", sellCount);
			req.setAttribute("sellList", sellList);
			req.setAttribute("sellPage", sellPage);
			req.setAttribute("sellCurrentPage", sellCurrentPage);
			req.setAttribute("sellTotal", sellTotal);
			req.setAttribute("sellPaging", sellPaging);
			req.setAttribute("sellArticleUrl", sellArticleUrl);
			
			
			
			// 판매 완료 상품			
			String soldPage = req.getParameter("page"); // 판매 완료 페이지
			
			int soldCurrentPage = 1;
			
			if(soldPage != null) {
				soldCurrentPage = Integer.parseInt(soldPage);
			}
			
			int soldCount = dao.dataCount("sold", info.getUserId());
			int soldTotal = util.pageCount(rows, soldCount);
			if(soldCurrentPage > soldTotal) {
				soldCurrentPage = soldTotal;
			}
			
			int soldStart = (soldCurrentPage - 1) * rows + 1;
			int soldEnd = soldCurrentPage * rows;
			
			List<sellDTO> soldList = null;
			soldList = dao.listSell("sold", info.getUserId(), soldStart, soldEnd);
			
			String SoldQuery = "soldPage="+soldPage+"&soldCurrentPage="+soldCurrentPage ;
			
			String soldListUrl = cp + "/mypage/list.do";
			if(SoldQuery.length() != 0) {
				soldListUrl += "?" + SellQuery;
			}
			
			String soldPaging =  util.paging(soldCurrentPage, soldTotal, soldListUrl);
			
			req.setAttribute("SoldQuery", SoldQuery);
			req.setAttribute("soldCount", soldCount);
			req.setAttribute("soldList", soldList);
			req.setAttribute("soldPage", soldPage);
			req.setAttribute("soldCurrentPage", soldCurrentPage);
			req.setAttribute("soldTotal", soldTotal);
			req.setAttribute("soldPaging", soldPaging);
			req.setAttribute("itemCount", dao.dataCount("all", info.getUserId()));
			
			forward(req, resp, "/WEB-INF/semi/mypage/main.jsp");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	protected void upload(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
}
