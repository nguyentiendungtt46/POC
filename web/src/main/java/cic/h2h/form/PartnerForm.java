package cic.h2h.form;

import entity.Partner;
import frwk.form.ModelForm;
import frwk.form.SearchForm;

public class PartnerForm extends SearchForm<Partner> {
	private String  keyword_code,keyword_name;
	private String formDate;
	private String toDate;
	Partner partner = new Partner();
	
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

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	@Override
	public Partner getModel() {
		
		return partner;
	}

	public String getFormDate() {
		return formDate;
	}

	public void setFormDate(String formDate) {
		this.formDate = formDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

}
