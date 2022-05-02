package com.share;

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
@WebServlet("/share/*")
public class shareServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;

	private String pathname;
	
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		if(info == null) {
			forward(req, resp, "/WEB-INF/semi/member/login.jsp");
			return;			
		}
		
		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator + "share";
		
		if(uri.indexOf("list.do") != -1) {
			sharelist(req, resp);
		} else if(uri.indexOf("article.do") != -1) {
			sharearticle(req, resp);
		} else if(uri.indexOf("write.do") != -1) {
			shareWriteForm(req, resp);
		} else if(uri.indexOf("write_ok.do") != -1) {
			shareWriteSubmit(req, resp);
		} else if(uri.indexOf("update.do") != -1) {
			shareUpdateForm(req, resp);
		} else if(uri.indexOf("update_ok.do") != -1) {
			shareUpdateSubmit(req, resp);
		} else if(uri.indexOf("delete.do") != -1) {
			shareDelete(req, resp);
		} else if(uri.indexOf("deleteFile") != -1) {
			deleteFile(req, resp);
		}
	}
	
	// 리스트
	protected void sharelist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		shareDAO dao = new shareDAO();
		MyUtil util = new MyUtil();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		try {
			List<shareDTO> regionList = null;
			regionList = dao.listRegion();
			
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			String rCode = req.getParameter("rCode");
			String keyword = req.getParameter("keyword");
			
			if(rCode == null) {
				rCode = Integer.toString(info.getrCode());
			}
			if(keyword == null) {
				keyword = "";
			}
			
			if(req.getMethod().equalsIgnoreCase("GET")) {
				keyword = URLDecoder.decode(keyword, "utf-8");
			}
			
			int dataCount;
			if(rCode.equals("0") && keyword.length() == 0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(rCode, keyword);
			}
			
			int rows = 10;
			int total_page = util.pageCount(rows, dataCount);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int start = (current_page - 1) * rows + 1;
			int end = current_page * rows;
			
			List<shareDTO> list = null;
			if(rCode.equals("0") && keyword.length() == 0) {
				list = dao.listShare(start, end);
			} else {
				list = dao.listShare(start, end, rCode, keyword);
			}
			
			int listNum, n = 0;
			for(shareDTO dto:list) {
				listNum = dataCount - (start + n -1);
				dto.setListNum(listNum);
				n++;
			}
			
			// rCode 는 계속 쫒아다니게끔
			String query =  "rCode=" + rCode;
			if(keyword.length() != 0) {
				query += "&keyword=" + URLEncoder.encode(keyword, "utf-8");
			}
			
			String listUrl = cp + "/share/list.do";
			String articleUrl = cp + "/share/article.do?page=" + current_page;
			if(query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			req.setAttribute("list", list);
			req.setAttribute("page", page);
			req.setAttribute("current_page", current_page);
			req.setAttribute("total_page", total_page);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("articleUrl", articleUrl);
			req.setAttribute("paging", paging);
			req.setAttribute("rCode", rCode);
			req.setAttribute("keyword", keyword);
			req.setAttribute("regionList", regionList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String path = "/WEB-INF/semi/share/list.jsp";
		forward(req, resp, path);
	}
	
	// 글보기
	protected void sharearticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		shareDAO dao = new shareDAO();
		
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		
		try {
			int code = Integer.parseInt(req.getParameter("code"));
			String rCode = req.getParameter("rCode");
			String keyword = req.getParameter("keyword");
			

			if(rCode == null) {
				rCode = "0";
			}
			if(keyword == null) {
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			
			if(rCode.equals("0") || keyword.length() != 0) {
				query += "&rCode=" + rCode + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");
			}
			
			// 조회수
			dao.updateHitCount(code);
			
			shareDTO dto = dao.readShare(code);
			if(dto == null) {
				resp.sendRedirect(cp + "/share/list.do?" + query);
				return;
			}
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			List<shareDTO> listFile = dao.listShareFile(code);
			
			req.setAttribute("dto", dto);
			req.setAttribute("listFile", listFile);
			req.setAttribute("page", page);
			
			forward(req, resp, "/WEB-INF/semi/share/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/share/list.do?page=" + page);
		
	}
	
	// 글쓰기
	protected void shareWriteForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		shareDAO dao = new shareDAO();
		req.setAttribute("mode", "write");
		List<shareDTO> regionList = null;
		regionList = dao.listRegion();
		
		req.setAttribute("regionList", regionList);
		
		forward(req, resp, "/WEB-INF/semi/share/write.jsp");
	}
	
	// 저장
	protected void shareWriteSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		shareDAO dao = new shareDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();
		
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/share/list.do");
			return;
		}
		
		try {
			shareDTO dto = new shareDTO();
			
			List<shareDTO> regionList = null;
			regionList = dao.listRegion();
			
			dto.setUserId(info.getUserId());
			dto.setrCode(Integer.parseInt(req.getParameter("rCode")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));

			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if (map != null) {
				String[] saveFiles = map.get("saveFilenames");
				dto.setPhotoFiles(saveFiles);
			}
			
			req.setAttribute("regionList", regionList);
			
			dao.insertShare(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/share/list.do");
		
	}
	
	// 수정 폼
	protected void shareUpdateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		shareDAO dao = new shareDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		
		try {
			int code = Integer.parseInt(req.getParameter("code"));
			shareDTO dto = dao.readShare(code);
			
			List<shareDTO> regionList = null;
			regionList = dao.listRegion();
			
			if(dto == null) {
				resp.sendRedirect(cp + "/share/list.do?page=" + page);
				return;
			}
			
			if(!dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp + "/share/list.do?page=" + page);
				return;
			}
			
			List<shareDTO> listFile = dao.listShareFile(code);
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("listFile", listFile);
			req.setAttribute("regionList", regionList);
			
			req.setAttribute("mode", "update");
			
			forward(req, resp, "/WEB-INF/semi/share/write.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/share/list.do?page=" + page);
	}
	
	// 수정완료
	protected void shareUpdateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		shareDAO dao = new shareDAO();
		
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/share/list.do");
			return;
		}
		
		String page = req.getParameter("page");
		
		try {
			shareDTO dto = new shareDTO();
			dto.setCode(Integer.parseInt(req.getParameter("code")));
			dto.setSubject(req.getParameter("subject"));
			dto.setrCode(Integer.parseInt(req.getParameter("rCode")));
			dto.setContent(req.getParameter("content"));
			
			Map<String , String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String[] saveFiles = map.get("saveFilenames");
				dto.setPhotoFiles(saveFiles);
			}
			
			dao.updateShare(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/share/list.do?page=" + page);
	}
	
	// 수정완료-파일삭제
	protected void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		shareDAO dao = new shareDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		try {
			int code = Integer.parseInt(req.getParameter("code"));
			int pNum = Integer.parseInt(req.getParameter("pNum"));
			
			shareDTO dto = dao.readShare(code);
			if(dto == null) {
				resp.sendRedirect(cp + "/share/list.do?page=" + page);
				return;
			}
			
			if(!info.getUserId().equals(dto.getUserId())) {
				resp.sendRedirect(cp + "/share/list.do?page=" + page);
				return;
			}
			
			shareDTO vo = dao.readSharePhoteFile(pNum);
			if(vo != null) {
				FileManger.doFiledelete(pathname, vo.getPhotoName());
				
				dao.deletePhotoFile("one", pNum);
			}
			
			resp.sendRedirect(cp + "/share/update.do?code=" + code + "&page=" + page);
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/share/list.do?page=" + page);
	}
	
	// 삭제
	protected void shareDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		shareDAO dao = new shareDAO();
		String cp = req.getContextPath();
			
		String page = req.getParameter("page");
		
		try {
			int code = Integer.parseInt(req.getParameter("code"));
			
			shareDTO dto = dao.readShare(code);
			if(dto == null) {
				resp.sendRedirect(cp + "/share/list.do?page=" + page);
				return;
			}
			
			List<shareDTO> listFile = dao.listShareFile(code);
			for(shareDTO vo : listFile) {
				FileManger.doFiledelete(pathname, vo.getPhotoName());
			}
			
			dao.deletePhotoFile("all", code);
			
			dao.deleteShare(code);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/share/list.do?page=" + page);
		
	}
}
