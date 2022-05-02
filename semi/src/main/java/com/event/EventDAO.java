package com.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class EventDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertEvent(EventDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;
		
		try {
			//시퀀스 값 가져오기
			sql = " SELECT event_seq.NEXTVAL FROM dual ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			seq = 0;
			if(rs.next()) {
				seq = rs.getInt(1);
			}
			dto.seteNum(seq);
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			
			sql = "INSERT INTO event(eNum, userId, subject, content, start_date, end_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.geteNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getStartDate());
			pstmt.setString(6, dto.getEndDate());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getSavePhotoes() != null) {
				sql = " INSERT INTO eventPhoto (pNum, eNum, photoname)"
					+ " VALUES (event_seq.NEXTVAL, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				
				for(int i=0; i<dto.getSavePhotoes().length; i++) {
					pstmt.setInt(1, dto.geteNum());
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
			sql = "SELECT COUNT(*) FROM event";
			
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
	
	public List<EventDTO> listEvent(int start, int end){
		List<EventDTO> list = new ArrayList<EventDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT * FROM ( ");
			sb.append("		SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("			SELECT eNum, uNick, subject, content, TO_CHAR(start_date, 'YYYY-MM-DD') start_date, TO_CHAR(end_date, 'YYYY-MM-DD') end_date");
			sb.append("			FROM event e ");
			sb.append("			JOIN member m ON e.userId = m.userId ");
			sb.append("			ORDER BY eNum DESC ");
			sb.append("		) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EventDTO dto = new EventDTO();
				dto.seteNum(rs.getInt("eNum"));
				dto.setuNick(rs.getString("uNick"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setStartDate(rs.getString("start_date"));
				dto.setEndDate(rs.getString("end_date"));
				
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
	
	public EventDTO readEvent(int eNum) {
		EventDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT eNum, m.uNick, subject, content, TO_CHAR(start_date, 'YYYY-MM-DD') start_date, TO_CHAR(end_date, 'YYYY-MM-DD') end_date "
				+ "FROM event e "
				+ "JOIN member m ON e.userId = m.userId "
				+ "WHERE eNum = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, eNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new EventDTO();
				
				dto.seteNum(rs.getInt("eNum"));
				dto.setuNick(rs.getString("uNick"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setStartDate(rs.getString("start_date"));
				dto.setEndDate(rs.getString("end_date"));
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
	
	public void updateEvent(EventDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		
		
		try {
			sql = " UPDATE event SET subject = ?, content = ?, start_date = ?, end_date = ? WHERE eNum = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getStartDate());
			pstmt.setString(4, dto.getEndDate());
			pstmt.setInt(5, dto.geteNum());
			
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
					
			if(dto.getSavePhotoes() != null) {
				sql = " INSERT INTO eventPhoto(pNum, eNum, photoName) "
					+ " VALUES (eventPhoto_seq.NEXTVAL, ? , ? )";
				pstmt = conn.prepareStatement(sql);
				
				for(int i = 0 ; i<dto.getSavePhotoes().length; i++) {
					
				pstmt.setInt(1, dto.geteNum());
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
	
	public void deleteEvent(int eNum) {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " DELETE FROM event WHERE eNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eNum);
			
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
	
	public List<EventDTO> listEventPhoto(int eNum){
		List<EventDTO> list = new ArrayList<EventDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT eNum, pNum, photoName FROM eventPhoto "
				+ " WHERE eNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eNum);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EventDTO dto = new EventDTO();
				
				dto.seteNum(rs.getInt("eNum"));
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
	
	public EventDTO readEventPhoto(int pNum) {
		EventDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT pNum, eNum, photoName FROM eventPhoto "
				+ " WHERE pNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new EventDTO();
				
				dto.setpNum(rs.getInt("pNum"));
				dto.seteNum(rs.getInt("eNum"));
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
	
	public void deleteEventPhoto(int pNum) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " DELETE FROM eventPhoto WHERE pNum = ? ";
			
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
	
} 
