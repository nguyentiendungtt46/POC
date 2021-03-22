package cic.h2h.form;

import entity.LogServiceTransient;
import frwk.form.SearchForm;

public class LogServicesForm extends SearchForm<LogServiceTransient> {
	private String fromDate, toDate;
	private String serviceId, prodId, partnerId, type;
	private LogServiceTransient logCoreService = new LogServiceTransient();

	public LogServiceTransient getLogCoreService() {
		return logCoreService;
	}

	public void setLogCoreService(LogServiceTransient logCoreService) {
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

	@Override
	public LogServiceTransient getModel() {
		return logCoreService;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
