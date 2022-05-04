package com.mypage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.share.shareDTO;
import com.util.DBConn;

public class MypageShareDAO {
	private Connection conn = DBConn.getConnection();
	
	// 지역 리스트
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
	
	// 데이터 개수
	public int dataCount(String mode, String userId) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			if(mode.equals("share")) {
				sql = " SELECT COUNT(*) FROM shareTable WHERE status <> 2 AND userId = ?";
			} else if(mode.equals("shareEnd")) {
				sql = " SELECT COUNT(*) FROM shareTable WHERE status = 2 AND userId = ?";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
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
	
	// 나눔중인 아이템 리스트
	public List<shareDTO> listShare(String userId, int start, int end) {
		List<shareDTO> list = new ArrayList<shareDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT * FROM ( ");
			sb.append(" 	SELECT ROWNUM rnum, tb.* FROM( ");
			sb.append("			SELECT s.code, subject, p.photoName, reg_date, status ");
			sb.append("			FROM shareTable s LEFT OUTER JOIN ( ");
			sb.append(" 			SELECT tb1.pnum, tb1.Code, tb1.photoName FROM ( ");
			sb.append(" 				SELECT ROW_NUMBER()OVER( ");
			sb.append(" 					PARTITION BY sharePhoto.CODE ");
			sb.append("						ORDER BY sharePhoto.photoName DESC ");
			sb.append(" 				)AS RNUM, sharePhoto.* FROM sharePhoto "); 
			sb.append(" 			)tb1 WHERE rnum = 1 "); 
			sb.append(" 		)P ON s.code = p.code "); 
			sb.append("		  	WHERE status <> 2 AND userId = ? "); 
			sb.append("			ORDER BY code DESC "); 
			sb.append(" 	)tb  WHERE ROWNUM <= ? "); 
			sb.append(" )WHERE rnum >= ? "); 
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				shareDTO dto = new shareDTO();
				
				dto.setCode(rs.getInt("code"));
				dto.setSubject(rs.getString("subject"));
				dto.setPhotoName(rs.getString("photoName"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setStatus(rs.getInt("status"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!= null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!= null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}
	
	
	// 나눔완료 아이템 리스트
	public List<shareDTO> listShareEnd(String userId, int start, int end) {
		List<shareDTO> list = new ArrayList<shareDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT * FROM ( ");
			sb.append(" 	SELECT ROWNUM rnum, tb.* FROM( ");
			sb.append("			SELECT s.code, subject, p.photoName, s.reg_date, status ");
			sb.append("			FROM shareTable s LEFT OUTER JOIN ( ");
			sb.append(" 			SELECT tb1.pnum, tb1.Code, tb1.photoName FROM ( ");
			sb.append(" 				SELECT ROW_NUMBER()OVER( ");
			sb.append(" 					PARTITION BY sharePhoto.CODE ");
			sb.append("						ORDER BY sharePhoto.photoName DESC ");
			sb.append(" 				)AS RNUM, sharePhoto.* FROM sharePhoto "); 
			sb.append(" 			)tb1 WHERE rnum = 1 "); 
			sb.append(" 		)P ON s.code = p.code "); 
			sb.append("		  	WHERE status = 2 AND userId = ? "); 
			sb.append("			ORDER BY code DESC "); 
			sb.append(" 	)tb  WHERE ROWNUM <= ? "); 
			sb.append(" )WHERE rnum >= ? "); 
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				shareDTO dto = new shareDTO();
				
				dto.setCode(rs.getInt("code"));
				dto.setSubject(rs.getString("subject"));
				dto.setPhotoName(rs.getString("photoName"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setStatus(rs.getInt("status"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!= null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!= null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}
	
}
