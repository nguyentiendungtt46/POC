package cic.h2h.form;

import entity.LogCoreServicesDetail;
import frwk.form.SearchForm;

public class LogCoreServicesDetailForm extends SearchForm<LogCoreServicesDetail> {
	private String logCoreId;
	LogCoreServicesDetail logCoreServicesDetail = new LogCoreServicesDetail();
	
	public LogCoreServicesDetail getLogCoreServicesDetail() {
		return logCoreServicesDetail;
	}

	public void setLogCoreServicesDetail(LogCoreServicesDetail logCoreServicesDetail) {
		this.logCoreServicesDetail = logCoreServicesDetail;
	}

	public String getLogCoreId() {
		return logCoreId;
	}

	public void setLogCoreId(String logCoreId) {
		this.logCoreId = logCoreId;
	}

	@Override
	public LogCoreServicesDetail getModel() {
		return logCoreServicesDetail;
	}
}
