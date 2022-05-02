package com.cmmu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class CmmuDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertCmmu(CmmuDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		int seq;
		
		try {
			sql = "SELECT comm_seq.NEXTVAL FROM dual";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			seq = 0;
			
			if(rs.next()) {
				seq = rs.getInt(1);
			}
			
			dto.setNum(seq);
			
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			
			sql = "INSERT INTO community(cNum, userId, subject, content, reg_date, hitCount) "
				+ " VALUES (?, ?, ?, ?, SYSDATE, 0) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getImageFiles() != null) {
				sql = "INSERT INTO commPhoto(pNum, cNum, photoName) "
					+ " VALUES(commPhoto_seq.NEXTVAL,?,?) ";
				
				pstmt = conn.prepareStatement(sql);
				
				for(int i=0; i<dto.getImageFiles().length; i++) {
					pstmt.setInt(1, dto.getNum());
					pstmt.setString(2, dto.getImageFiles()[i]);
					
					pstmt.executeUpdate();
				}
			}
			
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
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
	}

	
	public String region(int num) {
		String rName = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT rName FROM region WHERE rCode=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				rName = rs.getString(1);
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
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return rName;
	}
	
	
	public int dataCount(int rCode) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			if(rCode != 0) {
				sql = "SELECT NVL(COUNT(*),0) FROM community c "
					+ " JOIN member m ON c.userId = m.userId "
					+ " WHERE rCode=?";
			} else {
				sql = "SELECT NVL(COUNT(*),0) FROM community";
			}
			
			
			pstmt = conn.prepareStatement(sql);
			
			if(rCode != 0) pstmt.setInt(1, rCode);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
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
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	
	public int dataCount(int rCode, String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if(rCode != 0) {
				sb.append(" SELECT NVL(COUNT(*),0) FROM community c ");
				sb.append(" 	JOIN member m ON c.userId = m.userId ");
				if(condition.equals("all")) {
					sb.append(" WHERE (INSTR(subject, ?)>=1 OR INSTR(content, ?)>=1) ");
				} else if(condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\.|\\/)", "");
					sb.append(" WHERE TO_CHAR(c.reg_date,'YYYYMMDD') = ? ");
				} else {
					sb.append(" WHERE INSTR("+condition+", ?) >= 1 ");
				}
				sb.append(" AND rCode=?");
				
			} else {
				sb.append(" SELECT NVL(COUNT(*),0) FROM community c ");
				sb.append(" 	JOIN member m ON c.userId = m.userId ");
				if(condition.equals("all")) {
					sb.append(" WHERE INSTR(subject, ?)>=1 OR INSTR(content, ?)>=1 ");
				} else if(condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\.|\\/)", "");
					sb.append(" WHERE TO_CHAR(c.reg_date,'YYYYMMDD') = ? ");
				} else {
					sb.append(" WHERE INSTR("+condition+", ?) >= 1 ");
				}
			}
			

			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, keyword);
			
			if(condition.equals("all") && rCode != 0) {
				pstmt.setString(2, keyword);
				pstmt.setInt(3, rCode);
			} else if(condition.equals("all") && rCode == 0) {
				pstmt.setString(2, keyword);
			} else {
				if(rCode != 0) {
					pstmt.setInt(2, rCode);
				}
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
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
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}


	public List<CmmuDTO> listCmmu(int rCode, int start, int end){
		List<CmmuDTO> list = new ArrayList<CmmuDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT * FROM ( ");
			sb.append(" 	SELECT ROWNUM rnum, tb.* FROM( ");
			sb.append(" 		SELECT cNum, c.userId, uNick, subject, content, ");
			sb.append("				c.reg_date, hitCount, rName ");
			sb.append(" 		FROM community c");
			sb.append("			JOIN member m ON m.userId = c.userId ");
			sb.append("			JOIN region r ON m.rCode = r.rCode ");
			if(rCode != 0) {
				sb.append(" 	WHERE m.rCode = ? ");
			}
			sb.append(" 		ORDER BY cNum DESC ");
			sb.append(" 	)tb WHERE ROWNUM <= ? ");
			sb.append(" ) WHERE rnum >= ? ");

				
			pstmt = conn.prepareStatement(sb.toString());
			
			if(rCode != 0) {
				pstmt.setInt(1, rCode);
				pstmt.setInt(2, end);
				pstmt.setInt(3, start);
			} else {
				pstmt.setInt(1, end);
				pstmt.setInt(2, start);
			}
			
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CmmuDTO dto = new CmmuDTO();
				
				dto.setNum(rs.getInt("cNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setuNick(rs.getString("uNick"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setrName(rs.getString("rName"));
				
				list.add(dto);
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
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return list;
	}
	
	
	public List<CmmuDTO> listCmmu(int rCode, int start, int end, String condition, String keyword){
		List<CmmuDTO> list = new ArrayList<CmmuDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT * FROM ( ");
			sb.append(" 	SELECT ROWNUM rnum, tb.* FROM( ");
			sb.append(" 		SELECT cNum, c.userId, uNick, subject, content, ");
			sb.append("				c.reg_date, hitCount, rName ");
			sb.append(" 		FROM community c");
			sb.append("			JOIN member m ON m.userId = c.userId ");
			sb.append("			JOIN region r ON m.rCode = r.rCode ");
			if(condition.equals("all")) {
				sb.append(" 	WHERE (INSTR(subject,?)>=1 OR INSTR(content,?)>=1) ");
			} else if(condition.equals("reg_date")) {
				keyword = keyword.replaceAll("(\\-|\\.|\\/)", "");
				sb.append(" WHERE TO_CHAR(c.reg_date,'YYYYMMDD') = ? ");
			} else {
				sb.append(" WHERE INSTR("+condition+", ?) >= 1 ");
			}
			if(rCode != 0) {
				sb.append(" AND m.rCode = ? ");
			}
			sb.append(" 		ORDER BY cNum DESC ");
			sb.append(" 	)tb WHERE ROWNUM <= ? ");
			sb.append(" ) WHERE rnum >= ? ");

				
			pstmt = conn.prepareStatement(sb.toString());
			
			if(rCode != 0) {
				if(condition.equals("all")) {
					pstmt.setString(1, keyword);
					pstmt.setString(2, keyword);
					pstmt.setInt(3, rCode);
					pstmt.setInt(4, end);
					pstmt.setInt(5, start);
				} else {
					pstmt.setString(1, keyword);
					pstmt.setInt(2, rCode);
					pstmt.setInt(3, end);
					pstmt.setInt(4, start);
				}
			} else {
				if(condition.equals("all")) {
					pstmt.setString(1, keyword);
					pstmt.setString(2, keyword);
					pstmt.setInt(3, end);
					pstmt.setInt(4, start);
				} else {
					pstmt.setString(1, keyword);
					pstmt.setInt(2, end);
					pstmt.setInt(3, start);
				}
			}
			
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CmmuDTO dto = new CmmuDTO();
				
				dto.setNum(rs.getInt("cNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setuNick(rs.getString("uNick"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setrName(rs.getString("rName"));
				
				list.add(dto);
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
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return list;
	}
	
	
	public CmmuDTO articleCmmu(int num) {
		CmmuDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT c.cNum, c.userId, subject, uNick, rCode, content, c.reg_date, hitCount, p.photoName "
				+ " FROM community c "
				+ " JOIN member m ON c.userId = m.userId "
				+ " LEFT OUTER JOIN commPhoto p ON c.cNum = p.cNum "
				+ " WHERE c.cNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new CmmuDTO();
				
				dto.setNum(rs.getInt("cNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setSubject(rs.getString("subject"));
				dto.setuNick(rs.getString("uNick"));
				dto.setrName(region(rs.getInt("rCode")));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setImageFilename(rs.getString("photoName"));
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
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return dto;
	}
	
	
	public List<CmmuDTO> listPhoto(int num) {
		List<CmmuDTO> list = new ArrayList<CmmuDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT pNum, cNum, photoName FROM commPhoto WHERE cNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CmmuDTO dto = new CmmuDTO();
				
				dto.setFileNum(rs.getInt("pNum"));
				dto.setNum(rs.getInt("cNum"));
				dto.setImageFilename(rs.getString("photoName"));
				
				list.add(dto);
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
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return list;
	}
	
	
	public CmmuDTO readPhotoFile(int photoNum) {
		CmmuDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT pNum, cNum, photoName FROM commPhoto WHERE pNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, photoNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new CmmuDTO();
				
				dto.setFileNum(rs.getInt("pNum"));
				dto.setNum(rs.getInt("cNum"));
				dto.setImageFilename(rs.getString("photoName"));
				
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
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return dto;
	}
	
	
	public CmmuDTO preReadCmmu(int rCode, int num, String condition, String keyword) {
		CmmuDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if(keyword != null && keyword.length() != 0) {
				sb.append("SELECT * FROM ( ");
				sb.append(" 	SELECT cNum, subject ");
				sb.append("		FROM community c ");
				sb.append(" 	JOIN member m ON m.userId=c.userId ");
				sb.append(" 	WHERE (cNum > ?) ");
				if(condition.equals("all")) {
					sb.append(" AND (INSTR(subject,?)>=1 OR INSTR(content,?)>=1) ");
				} else if(condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\.|\\/)", "");
					sb.append(" AND TO_CHAR(c.reg_date,'YYYYMMDD') = ? ");
				} else {
					sb.append(" AND INSTR("+condition+", ?) >= 1 ");
				}
				if(rCode != 0) {
					sb.append(" AND m.rCode = ? ");
				}
				sb.append(" 	ORDER BY cNum ASC ");
				sb.append(") WHERE ROWNUM = 1 ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, num);
				pstmt.setString(2, keyword);
				
				if(rCode != 0) {
					if(condition.equals("all")) {
						pstmt.setString(3, keyword);
						pstmt.setInt(4, rCode);
					} else {
						pstmt.setInt(3, rCode);
					}
				} else {
					if(condition.equals("all")) {
						pstmt.setString(3, keyword);
					}
				}
				
			} else {
				sb.append("SELECT * FROM ( ");
				sb.append(" 	SELECT cNum, subject ");
				sb.append("		FROM community c");
				sb.append(" 	JOIN member m ON m.userId=c.userId ");
				sb.append(" 	WHERE (cNum > ?) ");
				if(rCode != 0) {
					sb.append(" AND m.rCode = ? ");
				}
				sb.append(" 	ORDER BY cNum ASC ");
				sb.append(") WHERE ROWNUM = 1 ");
				
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, num);
				if(rCode != 0) {
					pstmt.setInt(2, rCode);
				}
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new CmmuDTO();
				
				dto.setNum(rs.getInt("cNum"));
				dto.setSubject(rs.getString("subject"));
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
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return dto;
	}
	
	
	public CmmuDTO nextReadCmmu(int rCode, int num, String condition, String keyword) {
		CmmuDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if(keyword != null && keyword.length() != 0) {
				sb.append("SELECT * FROM ( ");
				sb.append(" 	SELECT cNum, subject ");
				sb.append("		FROM community c ");
				sb.append(" 	JOIN member m ON m.userId=c.userId ");
				sb.append(" 	WHERE (cNum < ?) ");
				if(condition.equals("all")) {
					sb.append(" AND (INSTR(subject,?)>=1 OR INSTR(content,?)>=1) ");
				} else if(condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\.|\\/)", "");
					sb.append(" AND TO_CHAR(c.reg_date,'YYYYMMDD') = ? ");
				} else {
					sb.append(" AND INSTR("+condition+", ?) >= 1 ");
				}
				if(rCode != 0) {
					sb.append(" AND m.rCode = ? ");
				}
				sb.append(" 	ORDER BY cNum DESC ");
				sb.append(") WHERE ROWNUM = 1 ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, num);
				pstmt.setString(2, keyword);
				
				if(rCode != 0) {
					if(condition.equals("all")) {
						pstmt.setString(3, keyword);
						pstmt.setInt(4, rCode);
					} else {
						pstmt.setInt(3, rCode);
					}
				} else {
					if(condition.equals("all")) {
						pstmt.setString(3, keyword);
					}
				}
				
			} else {
				sb.append("SELECT * FROM ( ");
				sb.append(" 	SELECT cNum, subject ");
				sb.append("		FROM community c ");
				sb.append(" 	JOIN member m ON m.userId=c.userId ");
				sb.append(" 	WHERE cNum < ? ");
				if(rCode != 0) {
					sb.append(" AND m.rCode = ? ");
				}
				sb.append(" 	ORDER BY cNum DESC ");
				sb.append(") WHERE ROWNUM = 1 ");
				
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, num);
				if(rCode != 0) {
					pstmt.setInt(2, rCode);
				}
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new CmmuDTO();
				
				dto.setNum(rs.getInt("cNum"));
				dto.setSubject(rs.getString("subject"));
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
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return dto;
	}
	
	
	public void updateHitCount(int num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE community SET hitCount = hitCount+1 WHERE cNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
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
	
	
	public void deleteCmmu(int num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM community WHERE cNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
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
	
	
	public void deletePhotoFile(String mode, int num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			if(mode.equals("all")) {
				sql = "DELETE FROM commPhoto WHERE cNum = ? ";
			} else {
				sql = "DELETE FROM commPhoto WHERE pNum = ? ";
			}
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
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
	
	
	
	public void updateCmmu(CmmuDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE community SET subject=?, content=? WHERE cNum=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getNum());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getImageFiles() != null) {
				sql = "INSERT INTO commPhoto (pNum, cNum, photoName) "
					+ " VALUES (commPhoto_seq.NEXTVAL, ?, ?) ";
				
				pstmt = conn.prepareStatement(sql);
				
				for(int i =0; i<dto.getImageFiles().length; i++) {
					pstmt.setInt(1, dto.getNum());
					pstmt.setString(2, dto.getImageFiles()[i]);
					
					pstmt.executeUpdate();
				}
			}
			
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
	
}
