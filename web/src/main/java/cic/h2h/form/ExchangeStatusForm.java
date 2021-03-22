package cic.h2h.form;

import dto.ExchangeStatus;
import frwk.form.SearchForm;

public class ExchangeStatusForm extends SearchForm<ExchangeStatus> {

	String fromDate, toDate;
	String partnerId;
	ExchangeStatus exchangeStatus = new ExchangeStatus();

	public ExchangeStatus getExchangeStatus() {
		return exchangeStatus;
	}

	public void setExchangeStatus(ExchangeStatus exchangeStatus) {
		this.exchangeStatus = exchangeStatus;
	}

	@Override
	public ExchangeStatus getModel() {
		// TODO Auto-generated method stub
		return exchangeStatus;
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
