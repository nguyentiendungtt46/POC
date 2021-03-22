package cic.utils;

public class QnAFtpContext {

	/**
	 * Thong tin fpt H2H
	 */
	private FtpInf ftpInf;
	public FtpInf getFtpInf() {
		return ftpInf;
	}
	public void setFtpInf(FtpInf ftpInf) {
		this.ftpInf = ftpInf;
	}
	
	/**
	 * Thu muc fpt hoi tin theo tep
	 */
	private String ftpQnaInFld;
	/**
	 * Thu muc fpt tra loi tin theo tep
	 */
	private String ftpQnaOutFld;
	/**
	 * Thu muc fpt tra loi tin dinh ky
	 */
	private String ftpQnaFxFld;
	
	
	public String getFtpQnaInFld() {
		return ftpQnaInFld;
	}
	public void setFtpQnaInFld(String ftpQnaInFld) {
		this.ftpQnaInFld = ftpQnaInFld;
	}
	public String getFtpQnaOutFld() {
		return ftpQnaOutFld;
	}
	public void setFtpQnaOutFld(String ftpQnaOutFld) {
		this.ftpQnaOutFld = ftpQnaOutFld;
	}
	public String getFtpQnaFxFld() {
		return ftpQnaFxFld;
	}
	public void setFtpQnaFxFld(String ftpQnaFxFld) {
		this.ftpQnaFxFld = ftpQnaFxFld;
	}
}
