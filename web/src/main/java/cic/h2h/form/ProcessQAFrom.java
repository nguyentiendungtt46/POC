package cic.h2h.form;

import dto.ProcessQA;
import frwk.form.SearchForm;

public class ProcessQAFrom extends SearchForm<ProcessQA> {

	String fromDate, toDate, partnerId, productCode;

	ProcessQA process = new ProcessQA();

	public ProcessQA getProcess() {
		return process;
	}

	public void setProcess(ProcessQA process) {
		this.process = process;
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

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@Override
	public ProcessQA getModel() {
		// TODO Auto-generated method stub
		return process;
	}

}
