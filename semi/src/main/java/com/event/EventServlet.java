package com.event;

import java.io.File;
import java.io.IOException;
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
@WebServlet("/event/*")
public class EventServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;
	
	private String pathname;
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		
		HttpSession session = req.getSession();
		
		//파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		pathname = root+"uploads"+File.separator+"event";
		
		if(uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if (uri.indexOf("article.do")!= -1) {
			article(req, resp);
		} else if (uri.indexOf("write.do")!= -1) {
			writeForm(req, resp);
		} else if (uri.indexOf("write_ok.do")!= -1) {
			writeSubmit(req, resp);
		} else if (uri.indexOf("update.do")!= -1) {
			updateForm(req, resp);
		} else if (uri.indexOf("update_ok.do")!= -1) {
			updateSubmit(req, resp);
		} else if (uri.indexOf("delete.do")!= -1) {
			delete(req, resp);
		} else if (uri.indexOf("deletePhoto.do")!= -1) {
			deletePhoto(req, resp);
		}
		
		
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		EventDAO dao = new EventDAO();
		MyUtil myUtil = new MyUtil();
		
		String cp = req.getContextPath();
		try {
			
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			
			int dataCount;
			dataCount = dao.dataCount();

			
			int rows = 10;
			int total_page = myUtil.pageCount(rows, dataCount);
			if( current_page > total_page) {
				current_page = total_page;
			}
						
			int start = (current_page -1 ) * rows + 1;
			int end = current_page * rows;
			
			List<EventDTO> list = null;
			
			list = dao.listEvent(start, end);
			
			int listNum, n = 0;
    		for(EventDTO dto : list) {
    			listNum = dataCount - (start + n - 1);
    			dto.setListNum(listNum);
    			n++;
    		}
			
			String listUrl, articleUrl;
			
			listUrl = cp + "/event/list.do";
			articleUrl = cp + "/event/article.do?page="+current_page;
									
			String paging = myUtil.paging(current_page, total_page, listUrl);
			
			req.setAttribute("list", list);
			req.setAttribute("page", current_page);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("total_page", total_page);
			req.setAttribute("paging", paging);
			req.setAttribute("articleUrl", articleUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		String path = "/WEB-INF/semi/event/list.jsp";
		forward(req, resp, path);
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		EventDAO dao = new EventDAO();
		String cp = req.getContextPath();
		MyUtil myUtil = new MyUtil();
		
		String page = req.getParameter("page");
		String query = "page="+page;
		try {
			int eNum = Integer.parseInt(req.getParameter("eNum"));
			
			EventDTO dto = dao.readEvent(eNum);
			if(dto == null) {
				resp.sendRedirect(cp+"/event/list.do?"+query);
				return;
			}
			
			dto.setContent(myUtil.htmlSymbols(dto.getContent()));
						
			List<EventDTO> listPhoto = dao.listEventPhoto(eNum);
			
			
			req.setAttribute("dto", dto);
			req.setAttribute("listPhoto", listPhoto);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			
			String path = "/WEB-INF/semi/event/article.jsp";
			forward(req, resp, path);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/event/list.do?"+query);
		
		
	}
	
	protected void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setAttribute("mode", "write");
		String path = "/WEB-INF/semi/event/write.jsp";
		forward(req, resp, path);
	}
	
	protected void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if(info == null ) {
			resp.sendRedirect(cp+"/event/list.do");
			return;
		}
		
		if(info.getuRole() != 1) {
			resp.sendRedirect(cp+"/event/list.do");
			return;
		}
		
		EventDAO dao = new EventDAO();
		try {
			EventDTO dto = new EventDTO();
			
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setStartDate(req.getParameter("startDate"));
			dto.setEndDate(req.getParameter("endDate"));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String [] savePhotoes = map.get("saveFilenames");
				
				dto.setSavePhotoes(savePhotoes);
			}
			
			dao.insertEvent(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/event/list.do");
	}
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();
		
		if(info == null ) {
			resp.sendRedirect(cp+"/event/list.do");
			return;
		}
		if(info.getuRole() != 1) {
			resp.sendRedirect(cp+"/event/list.do");
			return;
		}
		
		EventDAO dao = new EventDAO();
		String page = req.getParameter("page");

		try {
			int eNum = Integer.parseInt(req.getParameter("eNum"));
			EventDTO dto = dao.readEvent(eNum);
			if(dto == null) {
				resp.sendRedirect(cp+"/event/list.do?page="+page);
				return;
			}
			
			List<EventDTO> listPhoto = dao.listEventPhoto(eNum);
			
			req.setAttribute("dto", dto);
			req.setAttribute("listPhoto", listPhoto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");
			
			forward(req, resp, "/WEB-INF/semi/event/write.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/event/list.do?page="+page);
		
	}
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();
		
		if(info == null ) {
			resp.sendRedirect(cp+"/event/list.do");
			return;
		}
		if(info.getuRole() != 1) {
			resp.sendRedirect(cp+"/event/list.do");
			return;
		}
		
		EventDAO dao = new EventDAO();
		String page = req.getParameter("page");
		
		try {
			EventDTO dto = new EventDTO();
			
			dto.seteNum(Integer.parseInt(req.getParameter("eNum")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setStartDate(req.getParameter("startDate"));
			dto.setEndDate(req.getParameter("endDate"));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String [] savePhotoes = map.get("saveFilenames");
				dto.setSavePhotoes(savePhotoes);
			}
				
			dao.updateEvent(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/event/list.do?page="+page);
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		if(info == null ) {
			resp.sendRedirect(cp+"/event/list.do");
			return;
		}
		if(info.getuRole() != 1) {
			resp.sendRedirect(cp+"/event/list.do?");
			return;
		}
		
		EventDAO dao = new EventDAO();
		
		try {
			int eNum = Integer.parseInt(req.getParameter("eNum"));
			EventDTO dto = dao.readEvent(eNum);
			if(dto != null) {
				dao.deleteEvent(eNum);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/event/list.do?page="+page);
	}
	
	protected void deletePhoto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		if(info == null ) {
			resp.sendRedirect(cp+"/event/list.do");
			return;
		}
		
		if( info.getuRole() != 1) {
			resp.sendRedirect(cp+"/event/list.do");
			return;
		}
		
		EventDAO dao = new EventDAO();
		
		String page = req.getParameter("page");
		try {
			int eNum = Integer.parseInt(req.getParameter("eNum"));
			int pNum = Integer.parseInt(req.getParameter("pNum"));
			
			EventDTO dto = dao.readEvent(eNum);
			if(dto != null) {
				FileManger.doFiledelete(pathname, dto.getSavePhotoname());
				
				dao.deleteEventPhoto(pNum);
			}
			
			resp.sendRedirect(cp+"/event/update.do?page="+page+"&eNum="+eNum);
			return;
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/event/list.do?page="+page);
	}

}
