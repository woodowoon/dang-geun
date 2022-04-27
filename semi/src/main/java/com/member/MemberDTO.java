package com.member;

public class MemberDTO {
	private String userId;
	private String uName;
	private String uPwd;
	private String uNick;
	private String uTel, uTel1, uTel2, uTel3;
	private String reg_date;
	private String photoName;
	private int rCode;
	
	private int enabled;

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

	public String getuPwd() {
		return uPwd;
	}

	public void setuPwd(String uPwd) {
		this.uPwd = uPwd;
	}

	public String getuNick() {
		return uNick;
	}

	public void setuNick(String uNick) {
		this.uNick = uNick;
	}

	public String getuTel() {
		return uTel;
	}

	public void setuTel(String uTel) {
		this.uTel = uTel;
	}

	public String getuTel1() {
		return uTel1;
	}

	public void setuTel1(String uTel1) {
		this.uTel1 = uTel1;
	}

	public String getuTel2() {
		return uTel2;
	}

	public void setuTel2(String uTel2) {
		this.uTel2 = uTel2;
	}

	public String getuTel3() {
		return uTel3;
	}

	public void setuTel3(String uTel3) {
		this.uTel3 = uTel3;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public int getrCode() {
		return rCode;
	}

	public void setrCode(int rCode) {
		this.rCode = rCode;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	
	
}
