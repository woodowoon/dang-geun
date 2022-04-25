package com.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig(		
		fileSizeThreshold = 1024 * 1024,	
		maxFileSize = 1024 * 1024 * 5,		
		maxRequestSize = 1024 * 1024 * 10
)
public abstract class MyUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	protected void forward(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}

	/**
	 * 단일 파일 업로드
	 * 
	 * @param p			Part 객체
	 * @param pathname	서버에 파일을 저장할 경로
	 * @return			서버에 저장된 파일명, 클라이언트가 업로드한 파일명
	 */
	protected Map<String, String> doFileUpload(Part p, String pathname) throws ServletException, IOException {
		Map<String, String> map = null;

		try {
			File f = new File(pathname);
			if (!f.exists()) { 
				f.mkdirs();
			}

			String originalFilename = getOriginalFilename(p);
			if (originalFilename == null || originalFilename.length() == 0)
				return null;

			String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
			String saveFilename = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance());
			saveFilename += System.nanoTime();
			saveFilename += fileExt;

			String fullpath = pathname + File.separator + saveFilename;
			p.write(fullpath);

			map = new HashMap<>();
			map.put("originalFilename", originalFilename);
			map.put("saveFilename", saveFilename);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * 다중 파일 업로드
	 * 
	 * @param parts		클라이언트가 서버로 전송한 모든 Part 객체
	 * @param pathname	서버에 파일을 저장할 경로
	 * @return			서버에 저장된 파일명, 클라이언트가 올린 파일명
	 */
	protected Map<String, String[]> doFileUpload(Collection<Part> parts, String pathname) throws ServletException, IOException {
		Map<String, String[]> map = null;
		try {
			File f = new File(pathname);
			if (!f.exists()) { 
				f.mkdirs();
			}

			String original, save, ext;
			List<String> listOriginal = new ArrayList<String>();
			List<String> listSave = new ArrayList<String>();

			for (Part p : parts) {
				String contentType = p.getContentType();
				if (contentType != null) { // 파일이면
					original = getOriginalFilename(p);
					if (original == null || original.length() == 0) {
						continue;
					}

					ext = original.substring(original.lastIndexOf("."));
					save = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance());
					save += System.nanoTime();
					save += ext;

					String fullpath = pathname + File.separator + save;
					p.write(fullpath);

					listOriginal.add(original);
					listSave.add(save);
				}
			}

			if (listOriginal.size() != 0) {
				String[] originals = listOriginal.toArray(new String[listOriginal.size()]);
				String[] saves = listSave.toArray(new String[listSave.size()]);

				map = new HashMap<>();

				map.put("originalFilenames", originals);
				map.put("saveFilenames", saves);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	private String getOriginalFilename(Part p) {
		try {
			for (String s : p.getHeader("content-disposition").split(";")) {
				if (s.trim().startsWith("filename")) {
					return s.substring(s.indexOf("=") + 1).trim().replace("\"", "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected abstract void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}

