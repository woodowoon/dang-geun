package com.share;

public class shareDTO {
	private int listNum;
	
	private int code;
	private String userId;
	private String bId;
	private String uNick;
	private int rCode; // 지역 코드
	private String rCode_name; // 지역 이름
	private String subject;
	private String content;
	private String reg_date;
	private String share_date; // 나눔완료한 날짜
	private int hitCount;
	private int status; // 나눔중:0, 나눔신청:1, 나눔완료:2
	
	private int pNum;
	private String photoName;
	private String[] photoFiles;
	
	public int getListNum() {
		return listNum;
	}
	public void setListNum(int listNum) {
		this.listNum = listNum;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getbId() {
		return bId;
	}
	public void setbId(String bId) {
		this.bId = bId;
	}
	public String getuNick() {
		return uNick;
	}
	public void setuNick(String uNick) {
		this.uNick = uNick;
	}
	public int getrCode() {
		return rCode;
	}
	public void setrCode(int rCode) {
		this.rCode = rCode;
	}
	public String getrCode_name() {
		return rCode_name;
	}
	public void setrCode_name(String rCode_name) {
		this.rCode_name = rCode_name;
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
	public String getShare_date() {
		return share_date;
	}
	public void setShare_date(String share_date) {
		this.share_date = share_date;
	}
	public int getHitCount() {
		return hitCount;
	}
	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getpNum() {
		return pNum;
	}
	public void setpNum(int pNum) {
		this.pNum = pNum;
	}
	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	public String[] getPhotoFiles() {
		return photoFiles;
	}
	public void setPhotoFiles(String[] photoFiles) {
		this.photoFiles = photoFiles;
	}
	
	
}
