package com.mypage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.cmmu.CmmuDTO;
import com.sell.sellDTO;
import com.util.DBConn;

public class MyPageDAO {
	private Connection conn = DBConn.getConnection();
	
	//판매 중인 상품 찾기
	public sellDTO readSell(int sNum) {
		sellDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
		} catch (Exception e) {
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
		
		return dto;
	}
	
	//판매중인 아이템 리스트
	public List<sellDTO> listSell(String userId) {
		List<sellDTO> list = new ArrayList<sellDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "";
			
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				sellDTO dto = new sellDTO();
				dto.setSubject(rs.getString("subject"));
				dto.setPrice(rs.getInt("price"));
				dto.setReg_date(rs.getString("reg_date"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
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
	
	//판매 완료된 상품 찾기
	public sellDTO readSold(int sNum) {
		sellDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
		} catch (Exception e) {
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
		return dto;
	}
	
	//판매 완료된 상품 리스트
	public List<sellDTO> listSold(String userId) {
		List<sellDTO> list = new ArrayList<sellDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
		} catch (Exception e) {
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
	
	//커뮤니티 글 찾기
	public CmmuDTO readCmmu(int sNum) {
		CmmuDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
		} catch (Exception e) {
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
		
		return dto;
	}
	
	//커뮤니티에 쓴 글 리스트
	public List<CmmuDTO> listCmmu(int sNum) {
		List<CmmuDTO> list = new ArrayList<CmmuDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
		} catch (Exception e) {
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
	
	//프로필사진 등록
	
	//판매중인 상품 갯수
	//판매완료 상품 갯수
	//커뮤니티 작성글 갯수
}
