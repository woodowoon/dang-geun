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
			
			sb.append(" SELECT userId, uPwd, reg_date, uNick, rCode FROM member ");
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
}
