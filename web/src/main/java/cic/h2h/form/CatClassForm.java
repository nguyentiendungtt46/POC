package cic.h2h.form;

import entity.CatClass;
import frwk.form.SearchForm;

public class CatClassForm extends SearchForm<CatClass> {
	private String  keyword_code,keyword_name;
	CatClass catClass = new CatClass();
	
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

	public CatClass getCatClass() {
		return catClass;
	}

	public void setCatClass(CatClass catClass) {
		this.catClass = catClass;
	}

	@Override
	public CatClass getModel() {
		// TODO Auto-generated method stub
		return catClass;
	}

}
