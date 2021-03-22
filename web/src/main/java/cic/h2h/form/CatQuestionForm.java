package cic.h2h.form;

import entity.CatQuestion;
import frwk.form.SearchForm;

public class CatQuestionForm extends SearchForm<CatQuestion> {
	private String  keyword_code,keyword_name;
	CatQuestion catQuestion = new CatQuestion();
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
	public CatQuestion getCatQuestion() {
		return catQuestion;
	}
	public void setCatQuestion(CatQuestion catQuestion) {
		this.catQuestion = catQuestion;
	}
	@Override
	public CatQuestion getModel() {
		// TODO Auto-generated method stub
		return catQuestion;
	}
	
}
