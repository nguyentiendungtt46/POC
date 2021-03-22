package cic.h2h.form;

import entity.QnaFilePro;
import frwk.form.SearchForm;

public class QnaFileProForm extends SearchForm<QnaFilePro> {

	String name;
	String code;
	String parentId;
	

	QnaFilePro qnaFilePro = new QnaFilePro();
	
	public QnaFilePro getQnaFilePro() {
		return qnaFilePro;
	}

	public void setQnaFilePro(QnaFilePro qnaFilePro) {
		this.qnaFilePro = qnaFilePro;
	}

	@Override
	public QnaFilePro getModel() {
		return qnaFilePro;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}
