package com.sell;

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
import com.util.MyUploadServlet;
import com.util.FileManger;
import com.util.MyUtil;

@MultipartConfig
@WebServlet("/sell/*")
public class sellServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;
	
	private String pathname;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri = req.getRequestURI();
		//String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		
		// 파일을 저장할 경로
		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator + "notice";		
		
		if(uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if(uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if(uri.indexOf("write.do") != -1) {
			writeForm(req, resp);
		} else if(uri.indexOf("write_ok.do") != -1) {
			writeSubmit(req, resp);
		} else if(uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if(uri.indexOf("update.do") != -1) {
			updateForm(req,resp);
		} else if(uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("deletePhoto.do") != -1) {
			deletePhoto(req, resp);
		} else if(uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		} else if(uri.indexOf("sellRequest.do") != -1) {
			purchaseRequest(req, resp);
		}
	}
	
	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sellDAO dao = new sellDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			List<regionDTO> regionList = null;
			regionList = dao.listRegion();
			
			
			String page = req.getParameter("page");
			int current_page = 1;
			
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
		
			
			String rCode = req.getParameter("rCode");	
			String keyword = req.getParameter("keyword");
			
			if(rCode == null) {
				rCode = info == null ? "0" : Integer.toString(info.getrCode());
			}
			
			if(keyword == null) {
				keyword = "";
			}
			
			if(req.getMethod().equalsIgnoreCase("GET")) {
				keyword = URLDecoder.decode(keyword, "utf-8");
			}
			
			int rows = 10;
			int dataCount, total_page;
			
			if(keyword.length() == 0 && rCode.equals("0")) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(rCode, keyword);
			}
			
			total_page = util.pageCount(rows, dataCount);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			
			int start = (current_page - 1) * rows + 1;
			int end = current_page * rows;
						
			List<sellDTO> list = null;
			if(keyword.length() == 0 && rCode.equals("0")) {
				list = dao.listsell(start, end);
			} else {
				list = dao.listsell(start, end, rCode, keyword);
			}
			
			int listNum, n = 0;
			for(sellDTO dto : list) {
				listNum = dataCount - (start + n - 1);
				dto.setListNum(listNum);
				
				n++;
			}
			
			String query = "rCode=" + rCode;
			if(keyword.length() != 0) {
				query += "&keyword="+URLEncoder.encode(keyword, "utf-8");	
			}
			
			String listUrl = cp+"/sell/list.do";
			String articleUrl = cp+"/sell/article.do?page="+current_page;
			if(query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}
//				System.out.println(listUrl);
//				System.out.println(articleUrl);
//				System.out.println(query);
			
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("rCode", rCode);
			req.setAttribute("regionList", regionList); // 지역 List
			req.setAttribute("list", list); // 게시글 list
			req.setAttribute("page", page); // 페이지
			req.setAttribute("current_page", current_page); // 현재 페이지
			req.setAttribute("total_page", total_page); // 전체 페이지
			req.setAttribute("paging", paging); // 페이징
			req.setAttribute("rCode", rCode); // 선택된 지역코드
			req.setAttribute("keyword", keyword); // 검색값
			req.setAttribute("articleUrl", articleUrl); // 게시글 상세url
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		forward(req, resp, "/WEB-INF/semi/sell/list.jsp");
	}
	
	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sellDAO dao = new sellDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			
			int rCode = info == null ? 0 : info.getrCode();
			
			if(req.getParameter("rCode") != null) {
				rCode = Integer.parseInt(req.getParameter("rCode"));
			}
			
			String keyword = req.getParameter("keyword");
			if(keyword == null) {
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			
			//if(keyword.length() != 0) {
				query += "&rCode="+rCode+"&keyword="+URLEncoder.encode(keyword, "utf-8");
			//}
			dao.updateHitCount(num);
			
			sellDTO dto = dao.readSell(num);
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			List<sellDTO> listPhoto = dao.listPhoto(num);
			
			req.setAttribute("dto", dto);
			req.setAttribute("listPhoto", listPhoto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			
			forward(req, resp, "/WEB-INF/semi/sell/article.jsp");
			return;	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+ "/sell/list.do?" + query);
	}

	private void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sellDAO dao = new sellDAO();
		String path = "/WEB-INF/semi/sell/write.jsp";
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		if(info == null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		try {
			List<regionDTO> region = null;
			region = dao.listRegion();
			
			req.setAttribute("mode", "write");
			req.setAttribute("region", region);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		forward(req, resp, path);
	}
	
	private void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+ "/sell/list.do");
			return;
		}
		
		// 회원만 글쓰기 가능
		if(info.getUserId() == null) {
			resp.sendRedirect(cp+"/sell/list.do");
			return;
		}
		
		sellDAO dao = new sellDAO();
		try {
			sellDTO dto = new sellDTO();
			dto.setUserId(info.getUserId());
			dto.setrCode(req.getParameter("rCode"));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setPrice(Integer.parseInt(req.getParameter("price")));
			
			// 상품이미지
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String[] photoNames = map.get("saveFilenames");
				dto.setPhotoNames(photoNames);
			}
			
			dao.insertSell(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/sell/list.do");
	}
	
	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sellDAO dao = new sellDAO();
		String cp = req.getContextPath();
		String page = "1";
		if(req.getParameter("page") != null) {
			page = req.getParameter("page");
		}
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			sellDTO dto = dao.readSell(num);
			
			if(dto == null) {
				resp.sendRedirect(cp + "/sell/list.do?page=" +page);
				return;
			}
			
			List<sellDTO> listPhoto = dao.listPhoto(num);
			List<regionDTO> region = null;
			region = dao.listRegion();
			
			req.setAttribute("dto", dto);
			req.setAttribute("listPhoto", listPhoto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");
			req.setAttribute("region", region);
			
			forward(req, resp, "/WEB-INF/semi/sell/write.jsp");
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		resp.sendRedirect(cp + "/sell/list.do?page=" + page);
	}
	
	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sellDAO dao = new sellDAO();
		
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/sell/list.do");
			return;
		}
		
		String page =req.getParameter("page");
		
		try {
			sellDTO dto = new sellDTO();
			dto.setCode(Integer.parseInt(req.getParameter("code")));
			dto.setrCode(req.getParameter("rCode"));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setPrice(Integer.parseInt(req.getParameter("price")));
			
			// 상품이미지
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String[] photoNames = map.get("saveFilenames");
				dto.setPhotoNames(photoNames);
			}
			dao.updateSell(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/sell/list.do?page=" + page);
	}
	
	private void deletePhoto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sellDAO dao = new sellDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			int pNum = Integer.parseInt(req.getParameter("pNum"));
			// System.out.println(pNum);
			
			sellDTO dto = dao.readSell(num);
			
			if(dto == null) {
				resp.sendRedirect(cp + "/sell/list.do?page=" + page);
				return;
			}
			
			if(! info.getUserId().equals(dto.getUserId())) {
				resp.sendRedirect(cp + "/sell/list.do?page=" + page);
			}
			
			sellDTO vo = dao.readPhoto(pNum);
			
			if(vo != null) {
				FileManger.doFiledelete(pathname, vo.getPhotoName());
			
				dao.deletePhoto("one", pNum);
				
			}
			resp.sendRedirect(cp+ "/sell/update.do?num=" + num + "&page=" + page);
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/sell/list.do?page=" + page);
	}
	
	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
		sellDAO dao = new sellDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();
		
		if(info == null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		try {
			//System.out.println(req.getParameter("num"));
			int num = Integer.parseInt(req.getParameter("num"));
			
			dao.deletePhoto("all", num);
			dao.deleteSell(num);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/sell/list.do");
	}
	
	private void purchaseRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sellDAO dao = new sellDAO();
		String cp = req.getContextPath();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		if(info == null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		String page = req.getParameter("page");
		String flag = req.getParameter("flag");
		String rCode= req.getParameter("rCode");
		
		String id = info.getUserId();
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			if(dao.readSell(num).getStatus() != 0 || dao.readSell(num) == null) {
				resp.sendRedirect(cp +"/sell/list.do&page="+page+"&rCode="+rCode);
				return;
			}
			
			dao.updateItem(flag, num, id);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(flag.equals("request")) {
			resp.sendRedirect(cp+"/sell/list.do?page=" + page + "&rCode=" +rCode);
		} else {
			System.out.println(flag);
			resp.sendRedirect(cp+"/mypage/list.do");
			
		}
	}
}

