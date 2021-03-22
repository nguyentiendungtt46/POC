package cic.h2h.form;

import entity.CatParIndex;
import entity.Partner;
import frwk.form.ModelForm;
import frwk.form.SearchForm;

public class CatParIndexForm extends SearchForm<CatParIndex> {
	private String  keyword_code,keyword_name;
	CatParIndex entParIndex = new CatParIndex();
	
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

	public CatParIndex getEntParIndex() {
		return entParIndex;
	}

	public void setEntParIndex(CatParIndex entParIndex) {
		this.entParIndex = entParIndex;
	}

	@Override
	public CatParIndex getModel() {
		
		return entParIndex;
	}
	
}
