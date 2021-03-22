package cic.h2h.form;

import entity.QnaInFile;
import frwk.form.ModelForm;
import frwk.form.SearchForm;

public class QnaInFileForm extends SearchForm<QnaInFile> {
	private String  keyword_code,keyword_name;
	private String fromDate, toDate;
	QnaInFile qnaInFile = new QnaInFile();
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
	public QnaInFile getQnaInFile() {
		return qnaInFile;
	}
	public void setQnaInFile(QnaInFile qnaInFile) {
		this.qnaInFile = qnaInFile;
	}
	@Override
	public QnaInFile getModel() {
		
		return qnaInFile;
	}
	
	private Short status;
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	
} 
