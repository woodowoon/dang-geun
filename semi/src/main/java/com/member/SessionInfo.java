package com.member;

public class SessionInfo {
	private String userId;
	private String uName;
	private String uNick;
	
	private int urole;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public int getUrole() {
		return urole;
	}
	public void setUrole(int urole) {
		this.urole = urole;
	}
	public String getuNick() {
		return uNick;
	}
	public void setuNick(String uNick) {
		this.uNick = uNick;
	}
	
	
}
