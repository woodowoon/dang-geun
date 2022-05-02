package com.FAQ;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.FileManger;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@MultipartConfig
@WebServlet("/FAQ/*")
public class FAQServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;
	
	private String pathname;
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		
		HttpSession session = req.getSession();
		
		//파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		pathname = root+"uploads"+File.separator+"FAQ";
		
		if(uri.indexOf("list.do") != -1) {
			FAQList(req, resp);
		} else if (uri.indexOf("article.do")!= -1) {
			FAQArticle(req, resp);
		} else if (uri.indexOf("write.do")!= -1) {
			FAQWriteForm(req, resp);
		} else if (uri.indexOf("write_ok.do")!= -1) {
			FAQWriteSubmit(req, resp);
		} else if (uri.indexOf("update.do")!= -1) {
			FAQUpdateForm(req, resp);
		} else if (uri.indexOf("update_ok.do")!= -1) {
			FAQUpdateSubmit(req, resp);
		} else if (uri.indexOf("delete.do")!= -1) {
			FAQDelete(req, resp);
		} else if (uri.indexOf("deletePhoto.do")!= -1) {
			FAQDeletePhoto(req, resp);
		}
		
		
	}
	
	protected void FAQList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		FAQDAO dao = new FAQDAO();
		MyUtil myUtil = new MyUtil();
		
		String cp = req.getContextPath();
		try {
			
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if (condition == null && keyword == null) {
				condition = "0";
				keyword = "";
			} else if (keyword == null) {
				keyword = "";
			}
			
			//GET방식으로 키워드 받아온 경우 디코드
			if(req.getMethod().equalsIgnoreCase("GET")) {
				keyword = URLDecoder.decode(keyword,"utf-8");
			}
			
			
			int dataCount;
			if(condition.equals("0") && keyword.length()==0 ) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(condition, keyword);
			}
			
			int rows = 10;
			int total_page = myUtil.pageCount(rows, dataCount);
			if( current_page > total_page) {
				current_page = total_page;
			}
						
			int start = (current_page -1 ) * rows + 1;
			int end = current_page * rows;
			
			List<FAQDTO> list = null;
			
			
			if(condition.equals("0") && keyword.length() == 0) {
				list = dao.listFAQ(start, end);
			} else {
				list = dao.listFAQ(start, end, condition, keyword);
			}
			
			int listNum, n = 0;
    		for(FAQDTO dto : list) {
    			listNum = dataCount - (start + n - 1);
    			dto.setListNum(listNum);
    			n++;
    		}
			
			String query = "";
			String listUrl, articleUrl;
			
			listUrl = cp + "/FAQ/list.do";
			articleUrl = cp + "/FAQ/article.do?page="+current_page;
			
			if (keyword.length() != 0) {
				query = "condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
				listUrl += "?"+query;
				articleUrl += "&"+query;
			} else {
				query = "condition="+condition;
				listUrl += "?"+query;
				articleUrl += "&"+query;
			}
			
			
			String paging = myUtil.paging(current_page, total_page, listUrl);
			
			req.setAttribute("list", list);
			req.setAttribute("page", current_page);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("total_page", total_page);
			req.setAttribute("paging", paging);
			req.setAttribute("condition", condition);
			req.setAttribute("keyword", keyword);
			req.setAttribute("articleUrl", articleUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		String path = "/WEB-INF/semi/FAQ/list.jsp";
		forward(req, resp, path);
	}
	
	protected void FAQArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		FAQDAO dao = new FAQDAO();
		String cp = req.getContextPath();
		MyUtil myUtil = new MyUtil();
		
		String page = req.getParameter("page");
		String query = "page="+page;
		try {
			int fNum = Integer.parseInt(req.getParameter("fNum"));
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			if(condition == null && keyword == null) {
				condition = "0";
				keyword = "";
			} else if (keyword == null) {
				keyword = "";
			}
			
			keyword = URLDecoder.decode(keyword, "utf-8");
			if( keyword.length() != 0) {
				query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			} else {
				query += "&condition="+condition;
			}
			
			FAQDTO dto = dao.readFAQ(fNum);
			if(dto == null) {
				resp.sendRedirect(cp+"/FAQ/list.do?"+query);
				return;
			}
			
			dto.setContent(myUtil.htmlSymbols(dto.getContent()));
			

			FAQDTO preReadFAQ = dao.preReadFAQ(fNum, condition, keyword);
			FAQDTO nextReadFAQ = dao.nextReadFAQ(fNum, condition, keyword);
			
			
			List<FAQDTO> listPhoto = dao.listFAQPhoto(fNum);
			
			
		 	
			
			req.setAttribute("dto", dto);
			req.setAttribute("preReadFAQ", preReadFAQ);
			req.setAttribute("nextReadFAQ", nextReadFAQ);
			req.setAttribute("listPhoto", listPhoto);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			
			String path = "/WEB-INF/semi/FAQ/article.jsp";
			forward(req, resp, path);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/FAQ/list.do?"+query);
		
		
	}
	protected void FAQWriteForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setAttribute("mode", "write");
		String path = "/WEB-INF/semi/FAQ/write.jsp";
		forward(req, resp, path);
	}
	protected void FAQWriteSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//게시글 저장
		
		String cp = req.getContextPath();
		
		//로그인정보
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if(info == null ) {
			resp.sendRedirect(cp+"/FAQ/list.do");
			return;
		}
		
		if(info.getuRole() != 1) {
			resp.sendRedirect(cp+"/FAQ/list.do");
			return;
		}
		//GET방식으로 접근시 redirect
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/FAQ/list.do");
			return;
		}
		
		
		FAQDAO dao = new FAQDAO();
		try {
			FAQDTO dto = new FAQDTO();
			
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setCategory(Integer.parseInt(req.getParameter("category")));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String [] savePhotoes = map.get("saveFilenames");
				
				dto.setSavePhotoes(savePhotoes);
			}
			
			dao.insertFAQ(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/FAQ/list.do");
	}
	protected void FAQUpdateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();
		
		if(info == null ) {
			resp.sendRedirect(cp+"/FAQ/list.do");
			return;
		}
		if(info.getuRole() != 1) {
			resp.sendRedirect(cp+"/FAQ/list.do");
			return;
		}
		
		FAQDAO dao = new FAQDAO();
		String page = req.getParameter("page");

		try {
			int fNum = Integer.parseInt(req.getParameter("fNum"));
			FAQDTO dto = dao.readFAQ(fNum);
			if(dto == null) {
				resp.sendRedirect(cp+"/FAQ/list.do?page="+page);
				return;
			}
			
			List<FAQDTO> listPhoto = dao.listFAQPhoto(fNum);
			
			req.setAttribute("dto", dto);
			req.setAttribute("listPhoto", listPhoto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");
			
			forward(req, resp, "/WEB-INF/semi/FAQ/write.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/FAQ/list.do?page="+page);
		
	}
	protected void FAQUpdateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();
		
		if(info == null ) {
			resp.sendRedirect(cp+"/FAQ/list.do");
			return;
		}
		if(info.getuRole() != 1) {
			resp.sendRedirect(cp+"/FAQ/list.do");
			return;
		}
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/FAQ/list.do");
			return;
		}
		
		FAQDAO dao = new FAQDAO();
		String page = req.getParameter("page");
		
		try {
			FAQDTO dto = new FAQDTO();
			
			dto.setfNum(Integer.parseInt(req.getParameter("fNum")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setCategory(Integer.parseInt(req.getParameter("category")));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String [] savePhotoes = map.get("saveFilenames");
				dto.setSavePhotoes(savePhotoes);
			}
				
			
			
			dao.updateFAQ(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/FAQ/list.do?page="+page);
	}
	
	protected void FAQDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		if(info == null ) {
			resp.sendRedirect(cp+"/FAQ/list.do");
			return;
		}
		if(info.getuRole() != 1) {
			resp.sendRedirect(cp+"/FAQ/list.do?");
			return;
		}
		
		FAQDAO dao = new FAQDAO();
		
		try {
			int fNum = Integer.parseInt(req.getParameter("fNum"));
			FAQDTO dto = dao.readFAQ(fNum);
			if(dto != null) {
				dao.deleteFAQ(fNum);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/FAQ/list.do?page="+page);
	}
	
	protected void FAQDeletePhoto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//글 수정에서 파일 삭제
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		if(info == null ) {
			resp.sendRedirect(cp+"/FAQ/list.do");
			return;
		}
		
		if( info.getuRole() != 1) {
			resp.sendRedirect(cp+"/FAQ/list.do");
			return;
		}
		
		FAQDAO dao = new FAQDAO();
		
		String page = req.getParameter("page");
		try {
			int fNum = Integer.parseInt(req.getParameter("fNum"));
			int pNum = Integer.parseInt(req.getParameter("pNum"));
			
			FAQDTO dto = dao.readFAQ(fNum);
			if(dto != null) {
				FileManger.doFiledelete(pathname, dto.getSavePhotoname());
				
				dao.deleteFAQPhoto(pNum);
			}
			
			resp.sendRedirect(cp+"/FAQ/update.do?page="+page+"&fNum="+fNum);
			return;
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/FAQ/list.do?page="+page);
	}

}
