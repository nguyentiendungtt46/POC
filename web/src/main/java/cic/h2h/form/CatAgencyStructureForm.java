package cic.h2h.form;

import entity.CatAgencyStructure;
import frwk.form.SearchForm;

public class CatAgencyStructureForm  extends SearchForm<CatAgencyStructure> {
	private String  keyword_code,keyword_name;
	CatAgencyStructure catAgencyStructure = new CatAgencyStructure();
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
	public CatAgencyStructure getCatAgencyStructure() {
		return catAgencyStructure;
	}
	public void setCatAgencyStructure(CatAgencyStructure catAgencyStructure) {
		this.catAgencyStructure = catAgencyStructure;
	}
	@Override
	public CatAgencyStructure getModel() {
		// TODO Auto-generated method stub
		return catAgencyStructure;
	}
	
}
