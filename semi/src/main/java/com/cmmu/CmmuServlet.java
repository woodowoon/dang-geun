package com.cmmu;

import java.io.File;
import java.io.IOException;
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
@WebServlet("/community/*")
public class CmmuServlet extends MyUploadServlet {
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
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}
		
		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator + "cmmu";
		
		
		if(uri.indexOf("list.do") != -1) {
			cmmuList(req, resp);
		} else if(uri.indexOf("article.do") != -1) {
			cmmuArticle(req, resp);
		} else if(uri.indexOf("write.do") != -1) {
			cmmuWrite(req, resp);
		} else if(uri.indexOf("write_ok.do") != -1) {
			writeSubmit(req, resp);
		} else if(uri.indexOf("delete.do") != -1) {
			cmmuDelete(req, resp);
		} else if(uri.indexOf("update.do") != -1) {
			cmmuUpdate(req, resp);
		} else if(uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("deletePhoto.do") != -1) {
			deletePhoto(req, resp);
		} 
	}
	
	protected void cmmuList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CmmuDAO dao = new CmmuDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			String rName = "";
			int rCode = 0;
			if(info != null) {
				rCode = info.getrCode();
				rName = dao.region(rCode);
				if(info.getuRole() == 1) {
					rCode = 0;
					rName = "관리";
				}
				req.setAttribute("rName", rName);
			}
			
			
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
				URLDecoder.decode(keyword,"utf-8");
			}
			
			
			int dataCount=0;
			if(keyword.length() == 0) {
				dataCount = dao.dataCount(rCode);
			} else {
				dataCount = dao.dataCount(rCode, condition, keyword);
			}
			
			int rows = 10;
			int total_page = util.pageCount(rows, dataCount);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int start = (current_page - 1) * rows + 1;
			int end = current_page * rows;
			
			List<CmmuDTO> list = null;
			if(keyword.length() == 0) {
				list = dao.listCmmu(rCode, start, end);
			} else {
				list = dao.listCmmu(rCode, start, end, condition, keyword);
			}
			
			long gap;
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			int listNum, n=0;
			for(CmmuDTO dto : list) {
				listNum = dataCount - (start + n - 1);
				dto.setListNum(listNum);
				
				Date date = sdf.parse(dto.getReg_date());
				gap = (curDate.getTime() - date.getTime()) / (1000*60*60);
				dto.setGap(gap);
				
				dto.setReg_date(dto.getReg_date().substring(0,10));
				
				n++;
			}
			
			String query = "";
			if(keyword.length() != 0) {
				query = "condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			String listUrl = cp + "/community/list.do";
			String articleUrl = cp + "/community/article.do?page="+current_page;
			if(query.length() != 0) {
				listUrl += "?"+query;
				articleUrl += "&" + query;
			}
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			req.setAttribute("list", list);
			req.setAttribute("page", current_page);
			req.setAttribute("total_page", total_page);
			req.setAttribute("listUrl", listUrl);
			req.setAttribute("articleUrl", articleUrl);
			req.setAttribute("paging", paging);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("condition", condition);
			req.setAttribute("keyword", keyword);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		forward(req, resp, "/WEB-INF/semi/community/list.jsp");
	}
	
	protected void cmmuArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CmmuDAO dao = new CmmuDAO();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			int rCode = info.getrCode();
			if(info.getrCode() == 1) {
				rCode = 0;
			}
			
			if(condition == null) {
				condition = "all";
				keyword = "";
			}
			
			keyword = URLDecoder.decode(keyword,"utf-8");
			
			if(keyword.length() != 0) {
				query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			// 조회수 증가
			dao.updateHitCount(num);
			
			// 게시물 가져오기
			CmmuDTO dto = dao.articleCmmu(num);
			if(dto == null) {
				resp.sendRedirect(cp+"/community/list.do?"+query);
				return;
			}
			
			
			// 이전글 다음글
			CmmuDTO preReadCmmu = dao.preReadCmmu(rCode, num, condition, keyword);
			CmmuDTO nextReadCmmu = dao.nextReadCmmu(rCode, num, condition, keyword);
			
			List<CmmuDTO> listPhoto = dao.listPhoto(num);
			
			// jsp로 전달할 속성
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			req.setAttribute("listPhoto", listPhoto);
			req.setAttribute("preReadCmmu", preReadCmmu);
			req.setAttribute("nextReadCmmu", nextReadCmmu);
			
			forward(req, resp, "/WEB-INF/semi/community/article.jsp");
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/community/list.do?"+query);
	}
	
	protected void cmmuWrite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CmmuDAO dao = new CmmuDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String rName = dao.region(info.getrCode());
		
		req.setAttribute("rName", rName);
		req.setAttribute("mode", "write");
		forward(req, resp, "/WEB-INF/semi/community/write.jsp");
	}
	
	protected void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CmmuDAO dao = new CmmuDAO();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/community/list.do");
			return;
		}
		
		try {
			CmmuDTO dto = new CmmuDTO();
			
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			System.out.println(pathname);
			if(map != null) {
				String[] saveFiles = map.get("saveFilenames");
				dto.setImageFiles(saveFiles);
			}
			
			dao.insertCmmu(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/community/list.do");
	}
	
	
	protected void cmmuDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CmmuDAO dao = new CmmuDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page=" + page;
		/*
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		*/
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			if(condition == null) {
				condition = "all";
				keyword = "";
			}
			
			keyword = URLDecoder.decode(keyword,"utf-8");
			
			if(keyword.length() != 0) {
				query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			CmmuDTO dto = dao.articleCmmu(num);
			
			if(dto == null) {
				resp.sendRedirect(cp + "/community/list.do?" + query);
				return;
			}
			
			/*
			if(!dto.getUserId().equals(info.getUserId()) && !info.getuRole()==1) {
				resp.sendRedirect(cp + "/community/list.do?" + query);
				return;
			}
			*/
			
			List<CmmuDTO> listPhoto = dao.listPhoto(num);
			for(CmmuDTO vo : listPhoto) {
				FileManger.doFiledelete(pathname, vo.getImageFilename());
			}
			dao.deletePhotoFile("all", num);
			
			dao.deleteCmmu(num);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/community/list.do?" + query);
	}
	
	
	protected void cmmuUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CmmuDAO dao = new CmmuDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		try {
			String rName = dao.region(info.getrCode());
			
			
			int num = Integer.parseInt(req.getParameter("num"));
			
			CmmuDTO dto = dao.articleCmmu(num);
			if(dto == null) {
				resp.sendRedirect(cp + "/community/list.do?page=" + page);
				return;
			}
			
			if(!dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp + "/community/list.do?page=" + page);
				return;
			}
			
			List<CmmuDTO> listPhoto = dao.listPhoto(num);
			
			req.setAttribute("rName", rName);
			req.setAttribute("dto", dto);
			req.setAttribute("listPhoto", listPhoto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");
			
			forward(req, resp, "/WEB-INF/semi/community/write.jsp");
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/community/list.do?page=" + page);
	}
	
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CmmuDAO dao = new CmmuDAO();
		String cp = req.getContextPath();
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/community/list.do");
			return;
		}
		String page = req.getParameter("page");
		
		try {
			CmmuDTO dto = new CmmuDTO();
			
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String[] saveFiles = map.get("saveFilenames");
				dto.setImageFiles(saveFiles);
			}
			
			dao.updateCmmu(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/community/list.do?page="+page);
	}
	
	
	protected void deletePhoto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CmmuDAO dao = new CmmuDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			int pNum = Integer.parseInt(req.getParameter("fileNum"));
			
			CmmuDTO dto = dao.articleCmmu(num);
			
			if(dto == null) {
				resp.sendRedirect(cp + "/community/list.do?page=" + page);
				return;
			}
			
			if(!dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp + "/community/list.do?page=" + page);
				return;
			}
			
			CmmuDTO vo = dao.readPhotoFile(pNum);
			if(vo != null) {
				FileManger.doFiledelete(pathname, vo.getImageFilename());
				dao.deletePhotoFile("one", pNum);
			}
			
			resp.sendRedirect(cp + "/community/update.do?num="+num+"&page="+page);
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/community/update.do?page="+page);
	}
	
	
}
