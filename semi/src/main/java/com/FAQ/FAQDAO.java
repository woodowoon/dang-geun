package com.FAQ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class FAQDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertFAQ(FAQDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;
		
		try {
			//시퀀스 값 가져오기
			sql = " SELECT FAQ_seq.NEXTVAL FROM dual ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			seq = 0;
			if(rs.next()) {
				seq = rs.getInt(1);
			}
			dto.setfNum(seq);
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			
			sql = " INSERT INTO FAQ (fNum, userId, subject, content, reg_date, category) "
				+ " VALUES (?, ?, ?, ?, SYSDATE, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getfNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getCategory());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getSavePhotoes() != null) {
				sql = " INSERT INTO FAQPhoto (pNum, fNum, photoname )"
					+ " VALUES (faq_seq.NEXTVAL, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				
				for(int i=0; i<dto.getSavePhotoes().length; i++) {
					pstmt.setInt(1, dto.getfNum());
					pstmt.setString(2, dto.getSavePhotoes()[i]);
					
					pstmt.executeUpdate();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
	}
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT COUNT(*) FROM FAQ ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		
		return result;
	}
	
	public int dataCount(String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT COUNT(*) FROM FAQ ");
			
			if(condition.equals("1") && keyword.length() != 0) {
				sb.append(" WHERE category = 1 AND INSTR(subject, ? )  >= 1 ");
			} else if (condition.equals("2") && keyword.length() != 0) {
				sb.append(" WHERE category = 2 AND INSTR(subject, ? ) >= 1  ");
			} else if (condition.equals("3") && keyword.length() != 0) {
				sb.append(" WHERE category = 3 AND INSTR(subject, ? ) >= 1  ");
			} else if (condition.equals("0") && keyword.length() != 0) {
				sb.append(" WHERE INSTR(subject, ? ) >= 1 ");
			} else if (condition.equals("1")){
				sb.append(" WHERE category = 1 " ); 
			} else if (condition.equals("2")){
				sb.append(" WHERE category = 2 " ); 
			} else if (condition.equals("3")){
				sb.append(" WHERE category = 3 " ); 
			}
			pstmt = conn.prepareStatement(sb.toString());
			
			if(keyword.length()!= 0) {
			pstmt.setString(1, keyword);
			}
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	public List<FAQDTO> listFAQ(int start, int end){
		List<FAQDTO> list = new ArrayList<FAQDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT * FROM ( ");
			sb.append(" 	SELECT ROWNUM rnum, tb.* from( ");
			sb.append(" 		SELECT fNum, uNick, subject, content, TO_CHAR(f.reg_date, 'YYYY-MM-DD') reg_date , category ");
			sb.append(" 		FROM FAQ f ");
			sb.append(" 		JOIN member m ON f.userId = m.userId ");
			sb.append(" 		ORDER BY fNum DESC ");
			sb.append(" 	)tb WHERE ROWNUM <= ? ");
			sb.append(" ) WHERE rnum >= ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				FAQDTO dto = new FAQDTO();
				dto.setfNum(rs.getInt("fNum"));
				dto.setuNick(rs.getString("uNick"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setCategory(rs.getInt("category"));
				
				list.add(dto);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return list;
	}
	
	public List<FAQDTO> listFAQ(int start, int end, String condition, String keyword){
		List<FAQDTO> list = new ArrayList<FAQDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT * FROM ( ");
			sb.append(" 	SELECT ROWNUM rnum, tb.* from( ");
			sb.append(" 		SELECT fNum, uNick, subject, content, TO_CHAR(f.reg_date, 'YYYY-MM-DD') reg_date , category ");
			sb.append(" 		FROM FAQ f ");
			sb.append(" 		JOIN member m ON f.userId = m.userId ");
			if(condition.equals("1") && keyword.length() != 0) {
				sb.append(" 	WHERE category = 1 AND INSTR(subject, ? )  >= 1 ");
			} else if(condition.equals("2") && keyword.length() != 0) {
				sb.append(" 	WHERE category = 2 AND INSTR(subject, ? )  >= 1 ");
			} else if(condition.equals("3") && keyword.length() != 0) {
				sb.append(" 	WHERE category = 3 AND INSTR(subject, ? ) >= 1 ");
			} else if(condition.equals("0") && keyword.length() != 0){
				sb.append(" 	WHERE INSTR(subject, ? ) >= 1 ");
			} else if(condition.equals("1") && keyword.length() == 0){
				sb.append(" 	WHERE category = 1 ");
			} else if(condition.equals("2") && keyword.length() == 0){
				sb.append(" 	WHERE category = 2 ");
			} else if(condition.equals("3") && keyword.length() == 0){
				sb.append(" 	WHERE category = 3 ");
			}
			sb.append(" 		ORDER BY fNum DESC  ");
			sb.append(" 	)tb WHERE ROWNUM <= ? ");
			sb.append(" ) WHERE rnum >= ? ");
			
			
			pstmt = conn.prepareStatement(sb.toString());
			
			if(keyword.length() != 0) {
				pstmt.setString(1, keyword);
				pstmt.setInt(2, end);
				pstmt.setInt(3, start);
			} else {
				pstmt.setInt(1, end);
				pstmt.setInt(2, start);
			}
			
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				FAQDTO dto = new FAQDTO();
				dto.setfNum(rs.getInt("fNum"));
				dto.setuNick(rs.getString("uNick"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setCategory(rs.getInt("category"));
				
				list.add(dto);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return list;
	}
	
	public FAQDTO readFAQ(int fNum) {
		FAQDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT fNum, m.uNick, subject, content, category, "
				+ "		TO_CHAR(f.reg_date, 'YYYY-MM-DD') reg_date "
				+ " FROM FAQ f "
				+ " JOIN member m ON f.userid = m.userId "
				+ " WHERE fNum = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, fNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new FAQDTO();
				
				dto.setfNum(rs.getInt("fNum"));
				dto.setCategory(rs.getInt("category"));
				dto.setuNick(rs.getString("uNick"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return dto;
	}
	
	public void updateFAQ(FAQDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		
		
		try {
			sql = " UPDATE FAQ SET subject = ?, content = ?, category = ? WHERE fNum = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getCategory());
			pstmt.setInt(4, dto.getfNum());
			
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
					
			if(dto.getSavePhotoes() != null) {
				sql = " INSERT INTO FAQPhoto(pNum, fNum, photoName) "
					+ " VALUES (FAQPhoto_seq.NEXTVAL, ? , ? )";
				pstmt = conn.prepareStatement(sql);
				
				for(int i = 0 ; i<dto.getSavePhotoes().length; i++) {
					
				pstmt.setInt(1, dto.getfNum());
				pstmt.setString(2, dto.getSavePhotoes()[i]);
				
				pstmt.executeUpdate();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		
	}
	
	public void deleteFAQ(int fNum) {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " DELETE FROM FAQ WHERE fNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, fNum);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
	}
	
	public List<FAQDTO> listFAQPhoto(int fNum){
		List<FAQDTO> list = new ArrayList<FAQDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT fNum, pNum, photoName FROM FAQPHOTO "
				+ " WHERE fNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, fNum);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				FAQDTO dto = new FAQDTO();
				
				dto.setfNum(rs.getInt("fNum"));
				dto.setpNum(rs.getInt("pNum"));
				dto.setSavePhotoname(rs.getString("photoName"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}
	
	public FAQDTO readFAQPhoto(int pNum) {
		FAQDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT pNum, fNum, photoName FROM FAQPhoto "
				+ " WHERE pNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new FAQDTO();
				
				dto.setpNum(rs.getInt("pNum"));
				dto.setfNum(rs.getInt("fNum"));
				dto.setSavePhotoname(rs.getString("photoName"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return dto;
	}
	
	public void deleteFAQPhoto(int pNum) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " DELETE FROM FAQPhoto WHERE pNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pNum);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
	}
	
	public FAQDTO preReadFAQ(int fNum, String condition, String keyword) {
		FAQDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if(keyword.length()==0 && condition.equals("0")) {
				sb.append(" SELECT * FROM ( ");
				sb.append("     SELECT fNum, subject FROM FAQ ");
				sb.append("     WHERE fNum > ? ");
				sb.append("     ORDER BY fNum ASC ");
				sb.append(" ) WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, fNum);
			} else {
				sb.append(" SELECT * FROM ( ");
				sb.append("    SELECT fNum, subject ");
				sb.append("    FROM FAQ f ");
				sb.append("    JOIN member m ON f.userId = m.userId ");
				sb.append("    WHERE ( fNum > ? ) ");
				if(condition.equals("1") && keyword.length() != 0) {
					sb.append(" 	AND category = 1 AND INSTR(subject, ? )  >= 1 ");
				} else if(condition.equals("2") && keyword.length() != 0) {
					sb.append(" 	AND category = 2 AND INSTR(subject, ? )  >= 1 ");
				} else if(condition.equals("3") && keyword.length() != 0) {
					sb.append(" 	AND category = 3 AND INSTR(subject, ? ) >= 1 ");
				} else if(condition.equals("0") && keyword.length() != 0){
					sb.append(" 	AND INSTR(subject, ? ) >= 1 ");
				} else if(condition.equals("1") && keyword.length() == 0){
					sb.append(" 	AND category = 1 ");
				} else if(condition.equals("2") && keyword.length() == 0){
					sb.append(" 	AND category = 2 ");
				} else if(condition.equals("3") && keyword.length() == 0){
					sb.append(" 	AND category = 3 ");
				}
				sb.append("     ORDER BY fnum ASC ");
				sb.append(" ) WHERE ROWNUM = 1 ");
				pstmt = conn.prepareStatement(sb.toString());

				
				pstmt.setInt(1, fNum);
				if (keyword.length() != 0) {
					pstmt.setString(2, keyword);
				}
			
			}
				rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new FAQDTO();
				dto.setfNum(rs.getInt("fNum"));
				dto.setSubject(rs.getString("subject"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
 
		return dto;
	}

	// 다음글
	public FAQDTO nextReadFAQ(int fNum, String condition, String keyword) {
		FAQDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if(keyword.length()==0 && condition.equals("0")) {
				sb.append(" SELECT * FROM ( ");
				sb.append("     SELECT fNum, subject FROM FAQ ");
				sb.append("     WHERE fnum < ? ");
				sb.append("     ORDER BY fNum DESC ");
				sb.append(" ) WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, fNum);
			}else {
			
				sb.append(" SELECT * FROM ( ");
				sb.append("    SELECT fNum, subject ");
				sb.append("    FROM FAQ f ");
				sb.append("    JOIN member m ON f.userId = m.userId ");
				sb.append("    WHERE ( fNum < ? ) ");
				if(condition.equals("1") && keyword.length() != 0) {
					sb.append(" 	AND category = 1 AND INSTR(subject, ? )  >= 1 ");
				} else if(condition.equals("2") && keyword.length() != 0) {
					sb.append(" 	AND category = 2 AND INSTR(subject, ? )  >= 1 ");
				} else if(condition.equals("3") && keyword.length() != 0) {
					sb.append(" 	AND category = 3 AND INSTR(subject, ? ) >= 1 ");
				} else if(condition.equals("0") && keyword.length() != 0){
					sb.append(" 	AND INSTR(subject, ? ) >= 1 ");
				} else if(condition.equals("1") && keyword.length() == 0){
					sb.append(" 	AND category = 1 ");
				} else if(condition.equals("2") && keyword.length() == 0){
					sb.append(" 	AND category = 2 ");
				} else if(condition.equals("3") && keyword.length() == 0){
					sb.append(" 	AND category = 3 ");
				}
				sb.append("     ORDER BY fnum DESC ");
				sb.append(" ) WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, fNum);
				if (keyword.length() != 0) {
					pstmt.setString(2, keyword);
				}
			} 

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new FAQDTO();
				dto.setfNum(rs.getInt("fNum"));
				dto.setSubject(rs.getString("subject"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return dto;
	}

	
} 
