package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.util.DBConn;


public class MemberDAO {
	private Connection conn = DBConn.getConnection();
	
	public MemberDTO loginMember(String userId, String uPwd) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append(" SELECT userId, uPwd, reg_date, uNick, rCode, uRole, uName, uTel, photoName  FROM member ");
			sb.append(" WHERE userId = ? AND uPwd = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, userId);
			pstmt.setString(2, uPwd);

			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setUserId(rs.getString("userId"));
				dto.setuPwd(rs.getString("uPwd"));
				dto.setuNick(rs.getString("uNick"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setrCode(rs.getInt("rCode"));
				dto.setuRole(rs.getInt("uRole"));
				dto.setuName(rs.getString("uName"));
				dto.setPhotoName(rs.getString("photoName"));
				dto.setuTel(rs.getString("uTel"));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return dto;
	}
	
	
	public void insertMember(MemberDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO member(userId,uName,uPwd,uRole,uTel,uNick,reg_date,photoName,rCode) "
				+ " VALUES (?,?,?,?,?,?,SYSDATE,?,?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getuName());
			pstmt.setString(3, dto.getuPwd());
			pstmt.setInt(4, dto.getuRole());
			pstmt.setString(5, dto.getuTel());
			pstmt.setString(6, dto.getuNick());
			pstmt.setString(7, dto.getPhotoName());
			pstmt.setInt(8, dto.getrCode());
			
			pstmt.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (Exception e2) {
			}
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
		}
	}
	
	
	public MemberDTO readMember(String userId) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT userId,uName,uPwd,uRole,uTel,uNick,reg_date,photoName,rCode ");
			sb.append(" FROM member ");
			sb.append(" WHERE userId=? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setUserId(rs.getString("userId"));
				dto.setuName(rs.getString("uName"));
				dto.setuPwd(rs.getString("uPwd"));
				dto.setuRole(rs.getInt("uRole"));
				dto.setuTel(rs.getString("uTel"));
				dto.setuNick(rs.getString("uNick"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setPhotoName(rs.getString("photoName"));
				dto.setrCode(rs.getInt("rCode"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return dto;
	}
	
	
}
