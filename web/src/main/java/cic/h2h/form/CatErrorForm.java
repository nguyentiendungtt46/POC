package cic.h2h.form;

import entity.CatError;
import frwk.form.SearchForm;

public class CatErrorForm extends SearchForm<CatError> {
	private String  keyword_code,keyword_name;
	CatError catError = new CatError();
	
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

	public CatError getCatError() {
		return catError;
	}

	public void setCatError(CatError catError) {
		this.catError = catError;
	}

	@Override
	public CatError getModel() {
		// TODO Auto-generated method stub
		return catError;
	}
}
