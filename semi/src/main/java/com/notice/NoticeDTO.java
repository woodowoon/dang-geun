package com.notice;

public class NoticeDTO {
	private int listNum;
	private int nNum;
	private String userId;
	private String subject;
	private String content;
	private String reg_date;
	private int hitCount;
	private int notice;
	
	private long gap;
	
	private int fNum;
	private String saveFileName;
	private String originalFileName;
	
	private String[] saveFiles;
	private String[] originalFiles;
	public int getListNum() {
		return listNum;
	}
	public void setListNum(int listNum) {
		this.listNum = listNum;
	}
	public int getnNum() {
		return nNum;
	}
	public void setnNum(int nNum) {
		this.nNum = nNum;
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
	public int getHitCount() {
		return hitCount;
	}
	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}
	public int getNotice() {
		return notice;
	}
	public void setNotice(int notice) {
		this.notice = notice;
	}
	public long getGap() {
		return gap;
	}
	public void setGap(long gap) {
		this.gap = gap;
	}
	public int getfNum() {
		return fNum;
	}
	public void setfNum(int fNum) {
		this.fNum = fNum;
	}
	public String getSaveFileName() {
		return saveFileName;
	}
	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
	}
	public String getOriginalFileName() {
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	public String[] getSaveFiles() {
		return saveFiles;
	}
	public void setSaveFiles(String[] saveFiles) {
		this.saveFiles = saveFiles;
	}
	public String[] getOriginalFiles() {
		return originalFiles;
	}
	public void setOriginalFiles(String[] originalFiles) {
		this.originalFiles = originalFiles;
	}
	
	

}
