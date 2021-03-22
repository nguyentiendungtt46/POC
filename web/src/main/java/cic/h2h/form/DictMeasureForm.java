package cic.h2h.form;

import entity.DictMeasure;
import frwk.form.SearchForm;

public class DictMeasureForm extends SearchForm<DictMeasure> {

	DictMeasure dictMeasure = new DictMeasure();
	String codeSearch, pathSearch;

	public String getCodeSearch() {
		return codeSearch;
	}

	public void setCodeSearch(String codeSearch) {
		this.codeSearch = codeSearch;
	}

	public String getPathSearch() {
		return pathSearch;
	}

	public void setPathSearch(String pathSearch) {
		this.pathSearch = pathSearch;
	}

	public DictMeasure getDictMeasure() {
		return dictMeasure;
	}

	public void setDictMeasure(DictMeasure dictMeasure) {
		this.dictMeasure = dictMeasure;
	}

	@Override
	public DictMeasure getModel() {
		// TODO Auto-generated method stub
		return dictMeasure;
	}

}
