package cic.h2h.form;

import entity.LogCheckConnect;
import frwk.form.SearchForm;

public class LogCheckConnectForm extends SearchForm<LogCheckConnect> {
	
	private String parentId;
	private Short status;
	private String ipAddres;
	
	LogCheckConnect logCheckConnect = new LogCheckConnect();
	
	public LogCheckConnect getLogCheckConnect() {
		return logCheckConnect;
	}

	public void setLogCheckConnect(LogCheckConnect logCheckConnect) {
		this.logCheckConnect = logCheckConnect;
	}

	@Override
	public LogCheckConnect getModel() {
		return logCheckConnect;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getIpAddres() {
		return ipAddres;
	}

	public void setIpAddres(String ipAddres) {
		this.ipAddres = ipAddres;
	}
}
