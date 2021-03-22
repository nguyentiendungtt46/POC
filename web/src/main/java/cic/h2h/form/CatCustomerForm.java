package cic.h2h.form;

import entity.CatCustomer;
import frwk.form.SearchForm;

public class CatCustomerForm extends SearchForm<CatCustomer> {
	private String  keyword_code,keyword_name;
	CatCustomer catCustomer = new CatCustomer();
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
	public CatCustomer getCatCustomer() {
		return catCustomer;
	}
	public void setCatCustomer(CatCustomer catCustomer) {
		this.catCustomer = catCustomer;
	}
	@Override
	public CatCustomer getModel() {
		// TODO Auto-generated method stub
		return catCustomer;
	}
	
	
}
