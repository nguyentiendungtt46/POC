package cic.utils;


public class ExchgFtpContext {
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
	 * Thu muc fpt tep phi cau truc tctd
	 */
	private String ftpExchgInFld;
	/**
	 * Thu muc fpt tep phi cau truc cic
	 */
	private String ftpExchgOutFld;
	
	
	public String getFtpExchgInFld() {
		return ftpExchgInFld;
	}
	public void setFtpExchgInFld(String ftpExchgInFld) {
		this.ftpExchgInFld = ftpExchgInFld;
	}
	public String getFtpExchgOutFld() {
		return ftpExchgOutFld;
	}
	public void setFtpExchgOutFld(String ftpExchgOutFld) {
		this.ftpExchgOutFld = ftpExchgOutFld;
	}
	
}
