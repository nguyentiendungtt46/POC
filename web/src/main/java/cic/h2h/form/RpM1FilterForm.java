package cic.h2h.form;

import entity.RpM1Filter;
import frwk.form.ModelForm;
import frwk.form.SearchForm;

public class RpM1FilterForm extends SearchForm<RpM1Filter> {

	private String reportCode;
	
	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	
	RpM1Filter rpM1Filter = new RpM1Filter();
	
	public RpM1Filter getRpM1Filter() {
		return rpM1Filter;
	}

	public void setRpM1Filter(RpM1Filter rpM1Filter) {
		this.rpM1Filter = rpM1Filter;
	}

	@Override
	public RpM1Filter getModel() {
		return rpM1Filter;
	}
}
