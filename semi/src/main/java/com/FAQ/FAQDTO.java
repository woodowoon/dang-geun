package com.FAQ;

public class FAQDTO {
	private int listNum;
	private int fNum;
	private String userId;
	private String uNick;
	private String subject;
	private String content;
	private String reg_date;
	private int category;
	
	private int pNum;
	private String savePhotoname;
	private String [] savePhotoes;

	
	public int getListNum() {
		return listNum;
	}
	public void setListNum(int listNum) {
		this.listNum = listNum;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	
	public String getuNick() {
		return uNick;
	}
	public void setuNick(String uNick) {
		this.uNick = uNick;
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
	public int getfNum() {
		return fNum;
	}
	public void setfNum(int fNum) {
		this.fNum = fNum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	
}
