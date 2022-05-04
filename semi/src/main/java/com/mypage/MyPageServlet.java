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
import com.share.shareDTO;
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
		MypageShareDAO sdao = new MypageShareDAO();
		
		MyUtil util = new MyUtil();
		
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
			
			
			// 나눔중
			String sharePage = req.getParameter("page");
			int shareCurrentPage = 1;
			
			if(sharePage != null) {
				shareCurrentPage = Integer.parseInt(sharePage);
			}
			
			int shareCount = sdao.dataCount("share", info.getUserId());
			int shareTotal = util.pageCount(rows, shareCount);
			if(shareCurrentPage > shareTotal) {
				shareCurrentPage = shareTotal;
			}
			
			int shareStart = (shareCurrentPage - 1) * rows + 1;
			int shareEnd = shareCurrentPage * rows;
			
			List<shareDTO> sharelist = null;
			sharelist = sdao.listShare(info.getUserId(), shareStart, shareEnd);
			
			String shareQuery = "sharePage=" + sharePage + "&shareCurrentPage=" + shareCurrentPage;
			
			String shareListUrl = cp + "/mypage/list.do";
			String shareArticleUrl = cp + "/share/article.do";
			if(shareQuery.length() != 0) {
				shareListUrl += "?" + shareQuery;
			}
			
			String sharePaging = util.paging(shareStart, shareTotal, shareListUrl);
			
			req.setAttribute("shareQuery", shareQuery);
			req.setAttribute("shareCount", shareCount);
			req.setAttribute("shareTotal", shareTotal);
			req.setAttribute("shareCurrentPage", shareCurrentPage);
			req.setAttribute("sharePage", sharePage);
			req.setAttribute("sharelist", sharelist);
			req.setAttribute("shareArticleUrl", shareArticleUrl);
			req.setAttribute("sharePaging", sharePaging);
			
			// 나눔완료
			String shareoutPage = req.getParameter("page");
			
			int shareoutCurrentPage = 1;
			
			if(shareoutPage != null) {
				shareoutCurrentPage = Integer.parseInt(shareoutPage);
			}
			
			int shareoutCount = sdao.dataCount("shareEnd", info.getUserId());
			int shareoutTotal = util.pageCount(rows, shareoutCount);
			if(shareoutCurrentPage > shareoutTotal) {
				shareoutCurrentPage = shareoutTotal;
			}
			
			int shareoutStart = (shareoutCurrentPage - 1) * rows + 1;
			int shareoutEnd = shareoutCurrentPage * rows;
			
			List<shareDTO> shareoutList = null;
			shareoutList = sdao.listShareEnd(info.getUserId(), shareoutStart, shareoutEnd);
			
			String shareoutQuery = "shareoutPage=" + shareoutPage + "&shareoutCurrentPage=" + shareoutCurrentPage;
			
			String shareoutListUrl = cp + "/mypage/list.do";
			if(shareoutQuery.length() != 0) {
				shareoutListUrl += "?" + shareoutQuery;
			}
			
			String shareoutPaging = util.paging(shareoutCurrentPage, shareoutTotal, shareoutListUrl);
			
			req.setAttribute("shareoutPage", shareoutPage);
			req.setAttribute("shareoutCurrentPage", shareoutCurrentPage);
			req.setAttribute("shareoutCount", shareoutCount);
			req.setAttribute("shareoutTotal", shareoutTotal);
			req.setAttribute("shareoutList", shareoutList);
			req.setAttribute("shareoutListUrl", shareoutListUrl);
			req.setAttribute("shareoutQuery", shareoutQuery);
			req.setAttribute("shareoutPaging", shareoutPaging);
			
			forward(req, resp, "/WEB-INF/semi/mypage/main.jsp");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	protected void upload(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
}
