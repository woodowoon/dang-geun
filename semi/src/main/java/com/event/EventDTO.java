package com.event;

public class EventDTO {
	private int listNum;
	private int eNum;
	private String userId;
	private String uNick;
	private String subject;
	private String content;
	private String startDate;
	private String endDate;
	
	private int pNum;
	private String savePhotoname;
	private String [] savePhotoes;
	
	public int getListNum() {
		return listNum;
	}
	public void setListNum(int listNum) {
		this.listNum = listNum;
	}
	public int geteNum() {
		return eNum;
	}
	public void seteNum(int eNum) {
		this.eNum = eNum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getuNick() {
		return uNick;
	}
	public void setuNick(String uNick) {
		this.uNick = uNick;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getpNum() {
		return pNum;
	}
	public void setpNum(int pNum) {
		this.pNum = pNum;
	}
	public String getSavePhotoname() {
		return savePhotoname;
	}
	public void setSavePhotoname(String savePhotoname) {
		this.savePhotoname = savePhotoname;
	}
	public String[] getSavePhotoes() {
		return savePhotoes;
	}
	public void setSavePhotoes(String[] savePhotoes) {
		this.savePhotoes = savePhotoes;
	}

}
