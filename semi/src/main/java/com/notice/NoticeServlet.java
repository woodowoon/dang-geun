package com.notice;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@WebServlet("/notice/*")
public class NoticeServlet extends MyUploadServlet{
	private static final long serialVersionUID = 1L;
	
	private String pathname;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if(uri.indexOf("list.do") == -1 && info == null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator + "notice";
		
		if(uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if(uri.indexOf("write.do") != -1) {
			writeForm(req, resp);
		} else if(uri.indexOf("write_ok.do") != -1) {
			writeSubmit(req, resp);
		} else if(uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if(uri.indexOf("update.do") != -1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		} else if(uri.indexOf("deleteFile.do") != -1) {
			deleteFile(req, resp);
		} else if(uri.indexOf("download.do") != -1) {
			download(req, resp);
		}

	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();
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
			if(condition == null) {
				condition = "all";
				keyword = "";
			}
			
			if(req.getMethod().equalsIgnoreCase("GET")) {
				keyword = URLDecoder.decode(keyword, "utf-8");
			}
			
			int rows = 10;
			int dataCount, total_page;
			
			if(keyword.length() == 0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(condition, keyword);
			}
			total_page = myUtil.pageCount(rows, dataCount);
			if(current_page > total_page) {
    			current_page = total_page;
    		}
			
			int start = (current_page - 1) * rows +1;
			int end = current_page * rows;
			
			List<NoticeDTO> list = null;
    		if(keyword.length() == 0) {
    			list = dao.listNotice(start, end);
    		} else {
    			list = dao.listNotice(start, end, condition, keyword);
    		}
    		
    		List<NoticeDTO> listNotice = null;
    		listNotice = dao.listNotice();
    		
    		long gap;
    		Date curDate = new Date();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
    		int listNum, n = 0;
    		for(NoticeDTO dto : list) {
    			listNum = dataCount - (start + n - 1);
    			dto.setListNum(listNum);
    			
    			Date date = sdf.parse(dto.getReg_date());
    			gap = (curDate.getTime() - date.getTime()) / (1000*60*60); // 시간
    			dto.setGap(gap);
    			
    			dto.setReg_date(dto.getReg_date().substring(0,10));
    			
    			n++;
    			
    		}
    		
    			String query = "";
        		String listUrl, articleUrl;
        		
        		listUrl = cp + "/notice/list.do";
        		articleUrl = cp + "/notice/article.do?page=" + current_page;
        		
        		if(keyword.length() != 0) {
        			query = "condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
        			
        			listUrl += "?" + query;
        			articleUrl += "&" + query;
        		}
        		
        		String paging = myUtil.paging(current_page, total_page, listUrl);
        		
        		req.setAttribute("list", list);
        		req.setAttribute("listNotice", listNotice);
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

    	   forward(req, resp, "/WEB-INF/semi/notice/list.jsp");
    	}
    
	
	protected void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "write");
		forward(req, resp, "/WEB-INF/semi/notice/write.jsp");
	}
	
	protected void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/notice/list.do");
			return;
		}
		
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/notice/list.do");
			return;
		}
		
		NoticeDAO dao = new NoticeDAO();
		
		try {
			NoticeDTO dto = new NoticeDTO();
			dto.setUserId(info.getUserId());
			
			dto.setSubject(req.getParameter("subject"));
			if(req.getParameter("notice") != null) {
				dto.setNotice(Integer.parseInt(req.getParameter("notice")));
			}
			dto.setContent(req.getParameter("content"));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String []saveFiles = map.get("saveFilenames");
				String []originalFiles = map.get("originalFilenames");
				
				dto.setSaveFiles(saveFiles);
				dto.setOriginalFiles(originalFiles);
			}
			dao.insertNotice(dto);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/notice/list.do");
		
	}
	
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			int nNum = Integer.parseInt(req.getParameter("nNum"));
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			if(condition == null) {
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			if(keyword.length() != 0) {
				query += "&condition=" + condition + "&keyword=" + URLDecoder.decode(keyword, "utf-8");
			}
			
			dao.updateHitCount(nNum);
			
			NoticeDTO dto = dao.readNotice(nNum);
			if(dto == null) {
				resp.sendRedirect(cp+"/notice/list.do?"+query);
				return;
			}
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			NoticeDTO preReadNotice = dao.preReadNotice(nNum, condition, keyword);
			NoticeDTO nextReadNotice = dao.nextReadNotice(nNum, condition, keyword);
			
			List<NoticeDTO> listFile = dao.listNoticeFile(nNum);
			
			req.setAttribute("dto", dto);
			req.setAttribute("preReadNotice", preReadNotice);
			req.setAttribute("nextReadNotice", nextReadNotice);
			req.setAttribute("listFile", listFile);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			
			forward(req, resp, "/WEB-INF/semi/notice/article.jsp");
			return;
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/notice/list.do?"+query);
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();
		
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/notice/list.do");
			return;
		}
		
		NoticeDAO dao = new NoticeDAO();
		String page = req.getParameter("page");
		
		try {
			int nNum = Integer.parseInt(req.getParameter("nNum"));
			NoticeDTO dto = dao.readNotice(nNum);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/notice/list.do?page="+page);
				return;
			}
			
			List<NoticeDTO> listFile = dao.listNoticeFile(nNum);
			
			req.setAttribute("dto", dto);
			req.setAttribute("listFile", listFile);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");
			
			forward(req, resp, "/WEB-INF/semi/notice/write.jsp");
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/notice/list.do?page="+page);
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();
		
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/notice/list.do");
			return;
		}
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/notice/list.do");
			return;
		}
		
		NoticeDAO dao = new NoticeDAO();
		String page = req.getParameter("page");
		
		try {
			NoticeDTO dto = new NoticeDTO();
			
			dto.setnNum(Integer.parseInt(req.getParameter("nNum")));
			if(req.getParameter("notice") != null) {
				dto.setNotice(Integer.parseInt(req.getParameter("notice")));
			}
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String[] ss = map.get("saveFilenames");
				String[] oo = map.get("originalFilenames");
				
				dto.setSaveFiles(ss);
				dto.setOriginalFiles(oo);
			}
			
			dao.updateNotice(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/notice/list.do?page="+page);
	}
	
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		try {
			int nNum = Integer.parseInt(req.getParameter("nNum"));
		
			dao.deleteNotice(nNum);
			
			resp.sendRedirect(cp+"/notice/list.do?page="+page);
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	protected void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/notice/list.do");
			return;
		}
		
		NoticeDAO dao = new NoticeDAO();
		String page = req.getParameter("page");
		
		try {
			int nNum = Integer.parseInt(req.getParameter("nNum"));
			int fNum = Integer.parseInt(req.getParameter("fNum"));
			
			NoticeDTO dto = dao.readNoticeFile(fNum);
			if(dto != null) {
				FileManger.doFiledelete(pathname, dto.getSaveFileName());
				dao.deleteNoticeFile("one", fNum);
			}
			
			resp.sendRedirect(cp+"/notice/update.do?nNum="+nNum+"&page="+page);
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/notice/list.do?page="+page);
	}
	
	protected void download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();
		boolean b =false;
		
		try {
			int fNum = Integer.parseInt(req.getParameter("fNum"));
			NoticeDTO dto = dao.readNoticeFile(fNum);
			
			if(dto != null) {
				b = FileManger.doFiledownload(dto.getSaveFileName(), dto.getOriginalFileName(), pathname, resp);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(! b) {
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.print("<script>alert('파일 다운로드가 불가능합니다.');history.back();</script>");
		}
	}
	
	
	
	

}
