package cic.h2h.form;

import entity.QnaOutFile;
import frwk.form.ModelForm;
import frwk.form.SearchForm;

public class QnaOutFileForm extends SearchForm<QnaOutFile> {
	
	private String outFileName;
	private String inFileName;
	private String ngaytraloitu;
	private String ngaytraloiden;
	private String matctd;
	private Boolean hasResult;
	public Boolean getHasResult() {
		return hasResult;
	}

	public void setHasResult(Boolean hasResult) {
		this.hasResult = hasResult;
	}

	QnaOutFile qnaOutFile = new QnaOutFile();

	public QnaOutFile getQnaOutFile() {
		return qnaOutFile;
	}

	public void setQnaOutFile(QnaOutFile qnaOutFile) {
		this.qnaOutFile = qnaOutFile;
	}

	@Override
	public QnaOutFile getModel() {
		return qnaOutFile;
	}

	public String getOutFileName() {
		return outFileName;
	}

	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}

	public String getInFileName() {
		return inFileName;
	}

	public void setInFileName(String inFileName) {
		this.inFileName = inFileName;
	}

	public String getNgaytraloitu() {
		return ngaytraloitu;
	}

	public void setNgaytraloitu(String ngaytraloitu) {
		this.ngaytraloitu = ngaytraloitu;
	}

	public String getNgaytraloiden() {
		return ngaytraloiden;
	}

	public void setNgaytraloiden(String ngaytraloiden) {
		this.ngaytraloiden = ngaytraloiden;
	}

	public String getMatctd() {
		return matctd;
	}

	public void setMatctd(String matctd) {
		this.matctd = matctd;
	}
	public Short status;
	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}
}
