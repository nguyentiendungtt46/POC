package cic.h2h.form;

import entity.LogCoreService;
import frwk.form.SearchForm;

public class LogCoreServicesForm extends SearchForm<LogCoreService> {
	private String fromDate, toDate;
	private String serviceId, username, status;
	LogCoreService logCoreService = new LogCoreService();
	
	public LogCoreService getLogCoreService() {
		return logCoreService;
	}

	public void setLogCoreService(LogCoreService logCoreService) {
		this.logCoreService = logCoreService;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public LogCoreService getModel() {
		return logCoreService;
	}
}
