package com.sell;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class sellDAO {
	private Connection conn = DBConn.getConnection();
	
	public List<regionDTO> listRegion() {
		List<regionDTO> list = new ArrayList<regionDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT * FROM region";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				regionDTO dto = new regionDTO();
				
				dto.setrCode(rs.getInt("rCode"));
				dto.setrName(rs.getString("rName"));
				
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
	
	public void insertSell(sellDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;
		
		try {
			sql = " SELECT item_seq.NEXTVAL FROM dual";
			
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
			
			sql = " INSERT INTO item (code, userId, rCode, subject, content, price, reg_date, hitCount, status) "
				+ " 	VALUES(?, ?, ?, ?, ?, ?, SYSDATE, 0, 0) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getCode());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getrCode());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			pstmt.setInt(6, dto.getPrice());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getPhotoNames() != null) {
				sql = " INSERT INTO itemPhoto(pNum, code, photoName) "
					+ " VALUES(itemphoto_seq.NEXTVAL, ?, ?) ";
				
				pstmt = conn.prepareStatement(sql);
				
				for(int i=0; i<dto.getPhotoNames().length; i++) {
					pstmt.setInt(1, dto.getCode());
					pstmt.setString(2, dto.getPhotoNames()[i]);
					
					pstmt.executeUpdate();
				}
			}
			
		} catch (SQLException e) {
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
			sql = " SELECT COUNT(*) FROM item "
				+ " WHERE status = 0 ";
			
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
	
	public int dataCount(String rCode, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT COUNT(*) FROM item ";
			if((! rCode.equals("0")) && keyword != "") {
				sql += " WHERE rCode = ? AND INSTR(subject, ?) >= 1 AND status = 0"; 
			} else if(keyword != "") {
				sql += " WHERE INSTR(subject, ?) >= 1 AND status = 0";
			} else if((! rCode.equals("0"))) {
				sql += " WHERE rCode = ? AND status = 0";
			} else {
				sql += " WHERE status = 0";
			}
				
			pstmt = conn.prepareStatement(sql);
			
			if((! rCode.equals("0")) && keyword != "") {
				pstmt.setString(1, rCode);
				pstmt.setString(2, keyword);
			} else if(keyword != "") {
				pstmt.setString(1, keyword);
			} else if((! rCode.equals("0"))) {
				pstmt.setString(1, rCode);
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
	
	public List<sellDTO> listsell(int start, int end) {
		List<sellDTO> list = new ArrayList<sellDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {			
			sb.append(" SELECT * FROM ( ");
			sb.append(" 	SELECT ROWNUM rnum, tb.* FROM( ");
			sb.append("			SELECT i.code, subject, p.photoName, price, reg_date, hitCount ");
			sb.append("			FROM item i LEFT OUTER JOIN ( ");
			sb.append(" 			SELECT tb1.pnum, tb1.Code, tb1.photoName FROM ( ");
			sb.append(" 				SELECT ROW_NUMBER()OVER( ");
			sb.append(" 					PARTITION BY itemPhoto.CODE ");
			sb.append("						ORDER BY itemPhoto.photoName DESC ");
			sb.append(" 				)AS RNUM, itemPhoto.* FROM itemphoto "); 
			sb.append(" 			)tb1 WHERE rnum = 1 "); 
			sb.append(" 		)P ON i.code = p.code "); 
			sb.append("		  	WHERE status = 0 "); 
			sb.append("			ORDER BY code DESC "); 
			sb.append(" 	)tb  WHERE ROWNUM <= ? "); 
			sb.append(" )WHERE rnum >= ? "); 

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				sellDTO dto = new sellDTO();
				
				dto.setCode(rs.getInt("code"));
				dto.setSubject(rs.getString("subject"));
				dto.setPhotoName(rs.getString("photoName"));
				dto.setPrice(rs.getInt("price"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHitCount(rs.getInt("hitCount"));
				
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
	
	public List<sellDTO> listsell(int start, int end, String rCode, String keyword) {
		List<sellDTO> list = new ArrayList<sellDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT * FROM ( ");
			sb.append(" 	SELECT ROWNUM rnum, tb.* FROM( ");
			sb.append("			SELECT i.code, subject, p.photoName, price, reg_date, hitCount ");
			sb.append("			FROM item i LEFT OUTER JOIN ( ");
			sb.append(" 			SELECT tb1.pnum, tb1.Code, tb1.photoName FROM ( ");
			sb.append(" 				SELECT ROW_NUMBER()OVER( ");
			sb.append(" 					PARTITION BY itemPhoto.CODE ");
			sb.append("						ORDER BY itemPhoto.photoName DESC ");
			sb.append(" 				)AS RNUM, itemPhoto.* FROM itemphoto "); 
			sb.append(" 			)tb1 WHERE rnum = 1 "); 
			sb.append(" 		)P ON i.code = p.code "); 
			sb.append("		  	WHERE status = 0 ");
			if((! rCode.equals("0")) && keyword != "") {
				sb.append(" 		AND rCode = ? AND INSTR(subject, ?) >= 1 ");
			} else if(rCode.equals("0")) {
				sb.append("			AND INSTR(subject, ?) >= 1 ");
			} else {
				sb.append("			AND rCode = ? ");
			}
			sb.append("			ORDER BY code DESC "); 
			sb.append(" 	)tb  WHERE ROWNUM <= ? "); 
			sb.append(" )WHERE rnum >= ? "); 
			
			pstmt = conn.prepareStatement(sb.toString());
			
			if((! rCode.equals("0")) && keyword != "") {
				pstmt.setString(1, rCode);
				pstmt.setString(2, keyword);
				pstmt.setInt(3, end);
				pstmt.setInt(4, start);
			} else if(rCode.equals("0")) {
				pstmt.setString(1, keyword);
				pstmt.setInt(2, end);
				pstmt.setInt(3, start);
			} else {
				pstmt.setString(1, rCode);
				pstmt.setInt(2, end);
				pstmt.setInt(3, start);
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				sellDTO dto = new sellDTO();
				
				dto.setCode(rs.getInt("code"));
				dto.setSubject(rs.getString("subject"));
				dto.setPhotoName(rs.getString("photoName"));
				dto.setPrice(rs.getInt("price"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHitCount(rs.getInt("hitCount"));
				
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
	
	public void updateHitCount(int num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " UPDATE item SET hitCount=hitCount+1 WHERE code = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
	}
	
	public sellDTO readSell(int num) {
		sellDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT i.code, i.userId, uNick, i.rCode, r.rName, subject, content, price "
				+ " FROM item i "
				+ " JOIN member m ON i.userId = m.userId "
				+ " JOIN region r ON i.rCode = r.rCode "
				+ " WHERE code = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new sellDTO();
				
				dto.setCode(rs.getInt("code"));
				dto.setUserId(rs.getString("userId"));
				dto.setuNick(rs.getString("uNick"));
				dto.setrCode(rs.getString("rCode"));
				dto.setrName(rs.getString("rName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setPrice(rs.getInt("price"));
			}
			
		} catch (Exception e) {
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
	
	public sellDTO readPhoto(int pNum) {
		sellDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = " SELECT pNum, code, photoName FROM itemPhoto WHERE pNum = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new sellDTO();
				
				dto.setpNum(rs.getInt("pNum"));
				dto.setCode(rs.getInt("code"));
				dto.setPhotoName(rs.getString("photoName"));
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
	
	public List<sellDTO> listPhoto(int num) {
		List<sellDTO> list = new ArrayList<sellDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT pNum, code, photoName "
				+ " FROM itemPhoto "
				+ "	WHERE code = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				sellDTO dto = new sellDTO();
				
				dto.setpNum(rs.getInt("pNum"));
				dto.setpCode(rs.getInt("code"));
				dto.setPhotoName(rs.getString("photoName"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
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

		return list;
	}
	
	public void updateSell(sellDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " UPDATE item SET subject = ?, price = ?, rCode = ?, content = ? WHERE code = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setInt(2, dto.getPrice());
			pstmt.setString(3, dto.getrCode());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getCode());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getPhotoNames() != null) {
				sql = " INSERT INTO itemPhoto(pNum, code, photoName) "
						+ " VALUES(itemphoto_seq.NEXTVAL, ?, ?) ";
					
					pstmt = conn.prepareStatement(sql);
					
					for(int i=0; i<dto.getPhotoNames().length; i++) {
						pstmt.setInt(1, dto.getCode());
						pstmt.setString(2, dto.getPhotoNames()[i]);
						
						pstmt.executeUpdate();
					}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
	}
	
	public void deleteSell(int num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " DELETE FROM item WHERE code = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
					
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	public void deletePhoto(String mode, int num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			if(mode.equals("all")) {
				sql = " DELETE FROM itemPhoto WHERE code = ? ";				
			} else {
				sql = " DELETE FROM itemPhoto WHERE pNum = ? ";
			}
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
	}
	
	public void updateItem(String mode, int num, String id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql = "";
		
		try {
			if(mode.equals("request")) {
				sql = " UPDATE item SET bId = ?, status = 1 ";
			} else if(mode.equals("complete")) {
				sql = " UPDATE item SET bId = ?, status = 2, sell_date = SYSDATE ";
			} else if(mode.equals("cancel")) {
				sql = " UPDATE item SET bId = '', status = 0 ";
			}
			sql += " WHERE code = ?" ;
			
			pstmt = conn.prepareStatement(sql);
			if(mode.equals("cancel")) {
				pstmt.setInt(1, num);
			} else {
				pstmt.setString(1, id);
				pstmt.setInt(2, num);
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
	}
}
