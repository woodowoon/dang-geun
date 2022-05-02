package com.notice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class NoticeDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertNotice(NoticeDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;
		
		try {
			sql ="SELECT notice_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			seq = 0;
			if(rs.next()) {
				seq = rs.getInt(1);
			}
			dto.setnNum(seq);
			
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			
			sql = "INSERT INTO notice(nNum, userId, subject, content, notice, reg_date, hitCount ) "
					+ " VALUES (?, ?, ?, ?, ?, SYSDATE, 0)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getnNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getNotice());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getSaveFiles() != null) {
				sql = "INSERT INTO noticeFile(fNum, nNum, saveFileName, originalFileName) "
						+ " VALUES(noticeFile_seq.NEXTVAL, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
			
			for(int i=0; i<dto.getSaveFiles().length; i++) {
				pstmt.setInt(1, dto.getnNum());
				pstmt.setString(2, dto.getSaveFiles()[i]);
				pstmt.setString(3, dto.getOriginalFiles()[i]);
				
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
				} catch (Exception e) {
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
	
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql ="SELECT COUNT(*) FROM notice";
			
			pstmt = conn.prepareStatement(sql);
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
	
	
	public int dataCount(String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) FROM notice n ";
			sql += " JOIN member m ON n.userId = m.userId ";
			if(condition.equals("all")) {
				sql += " WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else if(condition.equals("reg_date")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += "   WHERE TO_CHAR(n.reg_date, 'YYYYMMDD') = ? ";
			} else {
				sql += " WHERE INSTR( " + condition + ", ?) >= 1"; 
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, keyword);
			if(condition.equals("all")) {
				pstmt.setString(2, keyword);
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
	
	
	public List<NoticeDTO> listNotice(int start, int end) {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT * FROM ( " );
			sb.append(" SELECT ROWNUM rnum, tb. * FROM ( ");
			sb.append("   SELECT nNum, n.userId, uNick, subject, content, n.reg_date, hitCount, notice ");
			sb.append("   FROM notice n ");
			sb.append("   JOIN member m ON n.userId = m.userId ");
			sb.append("  ORDER BY nNum DESC ");
			sb.append("  ) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				
				dto.setnNum(rs.getInt("nNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setuNick(rs.getString("uNick"));
				dto.setSubject(rs.getString("subject"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setNotice(rs.getInt("notice"));
				
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
	
	
	public List<NoticeDTO> listNotice(int start, int end, String condition, String keyword) {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT * FROM ( " );
			sb.append(" SELECT ROWNUM rnum, tb. * FROM ( ");
			sb.append("   SELECT nNum, n.userId, uNick, subject, content, n.reg_date, hitCount, notice ");
			sb.append("   FROM notice n ");
			sb.append("   JOIN member m ON n.userId = m.userId ");
			if(condition.equals("all")) {
				sb.append("   WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else if(condition.equals("reg_date")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append("   WHERE TO_CHAR(n.reg_date, 'YYYYMMDD') = ?");
			} else {
				sb.append("   WHERE INSTR(" + condition + ", ?) >= 1");
			}
			sb.append("  ORDER BY nNum DESC ");
			sb.append("  ) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
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
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				
				dto.setnNum(rs.getInt("nNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setuNick(rs.getString("uNick"));
				dto.setSubject(rs.getString("subject"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setNotice(rs.getInt("notice"));
				
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
	
	
	public List<NoticeDTO> listNotice() {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT nNum, n.userId, uNick, subject, content, hitCount, notice, "
					+ "  TO_CHAR(n.reg_date, 'YYYY-MM-DD') reg_date "
					+ " FROM notice n "
					+ " JOIN member m ON n.userId = m.userId "
					+ " WHERE notice = 1 "
					+ " ORDER BY nNum DESC ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				
				dto.setnNum(rs.getInt("nNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setuNick(rs.getString("uNick"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setNotice(rs.getInt("notice"));
				
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
	
	public NoticeDTO readNotice(int nNum) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT nNum, n.userId, uNick, subject, content, n.reg_date, hitCount, notice "
					+ " FROM notice n "
					+ " JOIN member m ON n.userId = m.userId "
					+ " WHERE nNum = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nNum);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new NoticeDTO();
				
				dto.setnNum(rs.getInt("nNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setuNick(rs.getString("uNick"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setNotice(rs.getInt("notice"));
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
	
	public NoticeDTO preReadNotice(int nNum, String condition, String keyword) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if (keyword != null && keyword.length() != 0) {
				sb.append(" SELECT * FROM ( ");
				sb.append("    SELECT nNum, subject ");
				sb.append("    FROM notice n ");
				sb.append("    JOIN member m ON n.userId = m.userId ");
				sb.append("    WHERE ( nNum > ? ) ");
				if (condition.equals("all")) {
					sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(n.reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + condition + ", ?) >= 1 ) ");
				}
				sb.append("     ORDER BY nNum ASC ");
				sb.append(" ) WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, nNum);
				pstmt.setString(2, keyword);
				if (condition.equals("all")) {
					pstmt.setString(3, keyword);
				}
			} else {
				sb.append(" SELECT * FROM ( ");
				sb.append("     SELECT nNum, subject FROM notice ");
				sb.append("     WHERE nNum > ? ");
				sb.append("     ORDER BY nNum ASC ");
				sb.append(" ) WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, nNum);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new NoticeDTO();
				dto.setnNum(rs.getInt("nNum"));
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
	
	
	public NoticeDTO nextReadNotice(int nNum, String condition, String keyword) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (keyword != null && keyword.length() != 0) {
				sb.append(" SELECT * FROM ( ");
				sb.append("    SELECT nNum, subject ");
				sb.append("    FROM notice n ");
				sb.append("    JOIN member m ON n.userId = m.userId ");
				sb.append("    WHERE ( nNum < ? ) ");
				if (condition.equals("all")) {
					sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(n.reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + condition + ", ?) >= 1 ) ");
				}
				sb.append("     ORDER BY nNum DESC ");
				sb.append(" ) WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, nNum);
				pstmt.setString(2, keyword);
				if (condition.equals("all")) {
					pstmt.setString(3, keyword);
				}
			} else {
				sb.append(" SELECT * FROM ( ");
				sb.append("     SELECT nNum, subject FROM notice ");
				sb.append("     WHERE nNum < ? ");
				sb.append("     ORDER BY nNum DESC ");
				sb.append(" ) WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, nNum);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new NoticeDTO();
				dto.setnNum(rs.getInt("nNum"));
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
	
	
	public void updateHitCount(int nNum) throws Exception {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="UPDATE notice SET hitCount=hitCount+1 WHERE nNum=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nNum);
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
	
	public List<NoticeDTO> listNoticeFile(int nNum) {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT fNum, saveFileName, originalFileName FROM noticeFile WHERE nNum = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nNum);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				dto.setfNum(rs.getInt("fNum"));
				dto.setSaveFileName(rs.getString("saveFileName"));
				dto.setOriginalFileName(rs.getString("originalFileName"));
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
	
	
	public NoticeDTO readNoticeFile(int fNum) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql ="SELECT fNum, nNum, saveFileName, originalFileName FROM noticeFile "
					+" WHERE fNum = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, fNum);
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto = new NoticeDTO();
				dto.setfNum(rs.getInt("fNum"));
				dto.setnNum(rs.getInt("nNum"));
				dto.setSaveFileName(rs.getString("saveFileName"));
				dto.setOriginalFileName(rs.getString("originalFileName"));
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
	
	
	public void updateNotice(NoticeDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE notice SET notice=?, subject=?, content=? WHERE nNum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNotice());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setInt(4, dto.getnNum());
			
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			if(dto.getSaveFiles() != null) {
				sql = "INSERT INTO noticeFile(fNum, nNum, saveFileName, originalFileName) "
						+ " VALUES (noticeFile_seq.NEXTVAL, ?, ?, ?)";
				
				pstmt = conn.prepareStatement(sql);
				
				for(int i=0; i<dto.getSaveFiles().length; i++) {
					pstmt.setInt(1, dto.getnNum());
					pstmt.setString(2, dto.getSaveFiles()[i]);
					pstmt.setString(3, dto.getOriginalFiles()[i]);
					
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
	
	public void deleteNoticeFile(String mode, int nNum) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			if(mode.equals("all")) {
				sql = "DELETE FROM noticeFile WHERE nNum=?";
			} else {
				sql = "DELETE FROM noticeFile WHERE fNum=?";
			}
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nNum);
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
	
	
	public void deleteNotice(int nNum) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM notice WHERE nNum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nNum);
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


}
