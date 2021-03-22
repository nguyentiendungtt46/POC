package cic.h2h.form;

import entity.RpFileUnstructured;
import frwk.form.SearchForm;

public class RpFileUnstructuredForm extends SearchForm<RpFileUnstructured> {
	private String  keyword_code,keyword_name, fromDate, toDate;
	RpFileUnstructured rpFileUnstructured = new RpFileUnstructured();
	private Boolean cicOwner;
	
	public Boolean getCicOwner() {
		return cicOwner;
	}

	public void setCicOwner(Boolean cicOwner) {
		this.cicOwner = cicOwner;
	}

	public String getKeyword_code() {
		return keyword_code;
	}

	public void setKeyword_code(String keyword_code) {
		this.keyword_code = keyword_code;
	}

	public String getKeyword_name() {
		return keyword_name;
	}

	public void setKeyword_name(String keyword_name) {
		this.keyword_name = keyword_name;
	}

	public RpFileUnstructured getRpFileUnstructured() {
		return rpFileUnstructured;
	}

	public void setRpFileUnstructured(RpFileUnstructured rpFileUnstructured) {
		this.rpFileUnstructured = rpFileUnstructured;
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
	public RpFileUnstructured getModel() {
		return rpFileUnstructured;
	}

}
