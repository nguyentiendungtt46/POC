package cic.h2h.form;

import dto.ProcessRp;
import frwk.form.SearchForm;

public class ProcessRpFrom extends SearchForm<ProcessRp> {

	ProcessRp processRp = new ProcessRp();
	private String fromDate, toDate, partnerId;

	public ProcessRp getProcessRp() {
		return processRp;
	}

	public void setProcessRp(ProcessRp processRp) {
		this.processRp = processRp;
	}

	@Override
	public ProcessRp getModel() {
		// TODO Auto-generated method stub
		return processRp;
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

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

}
