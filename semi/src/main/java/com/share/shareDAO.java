package com.share;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;


public class shareDAO {
	private Connection conn = DBConn.getConnection();
	
	// 지역
	public List<shareDTO> listRegion() {
		List<shareDTO> list = new ArrayList<shareDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT * FROM region";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				shareDTO dto = new shareDTO();
				
				dto.setrCode(rs.getInt("rCode"));
				dto.setrCode_name(rs.getString("rName"));
				
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
	
	
	// 나눔글 등록
	public void insertShare(shareDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;
		
		try {
			sql = "SELECT share_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			seq = 0;
			
			if(rs.next()) {
				seq = rs.getInt(1);
			}
			
			dto.setCode(seq);
			
			rs.close();
			pstmt.close();
			
			rs = null;
			pstmt = null;
			
			sql = " INSERT INTO shareTable(code, userId, rCode, subject, content, reg_date, hitCount, status) "
								+ " VALUES (?, ?, ?, ?, ?, SYSDATE, 0, 0) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getCode());
			pstmt.setString(2, dto.getUserId());
			pstmt.setInt(3, dto.getrCode());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			
			pstmt.executeQuery();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getPhotoFiles() != null) {
				sql = " INSERT INTO sharePhoto (pNum, code, photoName) "
					+ " VALUES (sharePhoto_seq.NEXTVAL, ?, ?) ";
				pstmt = conn.prepareStatement(sql);
				
				for(int i = 0; i < dto.getPhotoFiles().length; i++) {
					pstmt.setInt(1, dto.getCode());
					pstmt.setString(2, dto.getPhotoFiles()[i]);
					
					pstmt.executeQuery();
				}
			}
			
			
		} catch (SQLException e) {
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
	}
	
	// 데이터 개수
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = " SELECT COUNT(*) FROM shareTable ";
			pstmt = conn.prepareStatement(sql);
			 
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (SQLException e) {
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
	
	// 검색 데이터 개수
	public int dataCount(String rCode, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT COUNT(*) FROM shareTable WHERE ") ;
			
			
			 if(  rCode.equals("0") ) { // keyword만 있는 경우
				 sb.append("  (INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 )");
				 
			 } else if( keyword.length() == 0 ) { // 지역만 만 검색
				 sb.append("  rCode = ? ");
			 } else { // 지역 과 keyword 검색
				 sb.append("   rCode = ? AND ");
				sb.append("     (INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 )");
			 }
			
			
			pstmt = conn.prepareStatement(sb.toString());
			
			if( rCode.equals("0") ) {
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
			} else if ( keyword.length() == 0  ) {
				pstmt.setInt(1, Integer.parseInt(rCode));
			} else {
				pstmt.setInt(1, Integer.parseInt(rCode));
				pstmt.setString(2, keyword);
				pstmt.setString(3, keyword);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (SQLException e) {
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
	
	// 리스트
	public List<shareDTO> listShare(int start, int end) {
		List<shareDTO> list = new ArrayList<shareDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT * FROM ( ");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("          SELECT code, subject, m.uNick ,hitCount, ");
			sb.append("              TO_CHAR(s.reg_date, 'YYYY-MM-DD') reg_date, s.rCode, rName ");
			sb.append("          FROM shareTable s ");
			sb.append("          JOIN member m ON s.userId = m.userId ");
			sb.append("          JOIN region r ON s.rCode = r.rCode ");
			sb.append("          ORDER BY code DESC ");
			sb.append("    ) tb WHERE ROWNUM <= ? ");
			sb.append(" ) WHERE rnum >= ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				shareDTO dto = new shareDTO();
				
				dto.setCode(rs.getInt("code"));
				dto.setuNick(rs.getString("uNick"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setrCode(rs.getInt("rCode"));
				dto.setrCode_name(rs.getString("rName"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
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
	
	// 검색 리스트
	public List<shareDTO> listShare(int start, int end, String rCode, String keyword) {
		List<shareDTO> list = new ArrayList<shareDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT * FROM ( ");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("          SELECT code, subject, m.uNick ,hitCount, ");
			sb.append("              TO_CHAR(s.reg_date, 'YYYY-MM-DD') reg_date, s.rCode, rName ");
			sb.append("          FROM shareTable s ");
			sb.append("          JOIN member m ON s.userId = m.userId   ");
			sb.append("          JOIN region r ON s.rCode = r.rCode WHERE ");
			if(  rCode.equals("0") ) { // keyword만 있는 경우
				 sb.append("  (INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 )");
				 
			 } else if( keyword.length() == 0 ) { // 지역만 만 검색
				 sb.append("  s.rCode = ? ");
			 } else { // 지역 과 keyword 검색
				 sb.append("   s.rCode = ? AND ");
				 sb.append("     (INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 )");
			 }
			sb.append("          ORDER BY code DESC ");
			sb.append("    ) tb WHERE ROWNUM <= ? ");
			sb.append(" ) WHERE rnum >= ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			if(  rCode.equals("0") ) {
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
				pstmt.setInt(3, end);
				pstmt.setInt(4, start);
			} else if( keyword.length() == 0 ) {
				pstmt.setInt(1, Integer.parseInt(rCode));
				pstmt.setInt(2, end);
				pstmt.setInt(3, start);
			} else {
				pstmt.setInt(1, Integer.parseInt(rCode));
				pstmt.setString(2, keyword);
				pstmt.setString(3, keyword);
				pstmt.setInt(4, end);
				pstmt.setInt(5, start);
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				shareDTO dto = new shareDTO();
				
				dto.setCode(rs.getInt("code"));
				dto.setuNick(rs.getString("uNick"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setrCode(rs.getInt("rCode"));
				dto.setrCode_name(rs.getString("rName"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
		return list;
	}
	
	// 조회수
	public void updateHitCount(int code) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "UPDATE shareTable SET hitCount = hitCount+1 WHERE code = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, code);
			
			pstmt.executeQuery();
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
					
				}
			}
		}
	}
	
	// 글보기
	public shareDTO readShare(int code) {
		shareDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT code, m.uNick, r.rCode, s.userId, subject, content, s.reg_date, rName, hitCount, bId  "
				+ " FROM shareTable s "
				+ " JOIN member m ON s.userId = m.userId "
				+ " JOIN region r ON s.rCode = r.rCode WHERE "
				+ " code = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, code);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new shareDTO();
				
				dto.setCode(rs.getInt("code"));
				dto.setuNick(rs.getString("uNick"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setrCode_name(rs.getString("rName"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setUserId(rs.getString("userId"));
				dto.setrCode(rs.getInt("rCode"));
				dto.setbId(rs.getString("bId"));
			}
			
		} catch (SQLException e) {
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
	
	// 사진 확인
	public List<shareDTO> listShareFile(int code) {
		List<shareDTO> list = new ArrayList<shareDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT pNum, code, photoname FROM  sharePhoto WHERE code = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, code);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				shareDTO dto = new shareDTO();
				
				dto.setpNum(rs.getInt("pNum"));
				dto.setCode(rs.getInt("code"));
				dto.setPhotoName(rs.getString("photoname"));
				
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
	
	public shareDTO readSharePhoteFile(int pNum) {
		shareDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT pNum, code, photoname FROM  sharePhoto WHERE pNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, pNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new shareDTO();
				
				dto.setpNum(rs.getInt("pNum"));
				dto.setCode(rs.getInt("code"));
				dto.setPhotoName(rs.getString("photoname"));
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
	
	// 수정
	public void updateShare(shareDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " UPDATE shareTable SET subject = ?, content = ?, rCode = ? WHERE code = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getrCode());
			pstmt.setInt(4, dto.getCode());
			
			pstmt.executeQuery();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getPhotoFiles() != null) {
				sql = " INSERT INTO sharePhoto(pNum, code, photoname) VALUES "
					+ " (sharePhoto_seq.NEXTVAL, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				
				for (int i = 0; i<dto.getPhotoFiles().length; i++) {
					pstmt.setInt(1, dto.getCode());
					pstmt.setString(2, dto.getPhotoFiles()[i]);
					
					pstmt.executeQuery();
				}
			}
			
		} catch (SQLException e) {
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
	
	// 삭제
	public void deleteShare(int code) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {		
			sql = " DELETE FROM shareTable WHERE code = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, code);
			
			pstmt.executeQuery();
			
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
	
	// 사진 삭제
	public void deletePhotoFile(String mode, int code) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			if(mode.equals("all")) {
				sql = " DELETE FROM sharePhoto WHERE code = ? ";
			} else {
				sql = " DELETE FROM sharePhoto WHERE pNum = ? ";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, code);
			
			pstmt.executeQuery();
			
		} catch (SQLException e) {
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
	
	// 나눔 신청
	public void shareApp(shareDTO dto, int status) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " UPDATE shareTable SET bId = ?, status = ?, share_date = ? WHERE code = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			if(status == 0) {
				pstmt.setString(1, null);
				pstmt.setInt(2, status);
				pstmt.setString(3, null);
				pstmt.setInt(4, dto.getCode());
			} else if(status == 1) {
				pstmt.setString(1, dto.getbId());
				pstmt.setInt(2, status);
				pstmt.setString(3, null);
				pstmt.setInt(4, dto.getCode());
			} else if(status == 2) {
				pstmt.setString(1, dto.getbId());
				pstmt.setInt(2, status);
				pstmt.setString(3, "SYSDATE");
				pstmt.setInt(4, dto.getCode());
			}
			
			
			
			rs = pstmt.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					
				}
			} if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					
				}
			}
		}
	}
	
	public int readShareApp(int code) {
		shareDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT status  "
				+ " FROM shareTable WHERE "
				+ " code = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, code);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new shareDTO();
				
				dto.setStatus(rs.getInt("status"));
			}
			
		} catch (SQLException e) {
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
		
		return dto.getStatus();
	}
	
	
}
