package cic.h2h.form;

import entity.QnaInMaster;
import frwk.form.ModelForm;
import frwk.form.SearchForm;

public class QnaInMasterForm extends SearchForm<QnaInMaster> {

	private String malo;
	private String loaisp;
	private String matctd;
	private String tentctd;
	private String ngayhoitintu;
	private String ngayhoitinden;
	private String maSoPhieu;
	private Boolean hasResult;
	private Boolean hasRealTimeError;
	public Boolean getHasRealTimeError() {
		return hasRealTimeError;
	}

	public void setHasRealTimeError(Boolean hasRealTimeError) {
		this.hasRealTimeError = hasRealTimeError;
	}

	private String username;

	public Boolean getHasResult() {
		return hasResult;
	}

	public void setHasResult(Boolean hasResult) {
		this.hasResult = hasResult;
	}

	public String getMaSoPhieu() {
		return maSoPhieu;
	}

	public void setMaSoPhieu(String maSoPhieu) {
		this.maSoPhieu = maSoPhieu;
	}

	QnaInMaster qnaInMaster = new QnaInMaster();

	public QnaInMaster getQnaInMaster() {
		return qnaInMaster;
	}

	public void setQnaInMaster(QnaInMaster qnaInMaster) {
		this.qnaInMaster = qnaInMaster;
	}

	@Override
	public QnaInMaster getModel() {
		return qnaInMaster;
	}

	public String getMalo() {
		return malo;
	}

	public void setMalo(String malo) {
		this.malo = malo;
	}

	public String getLoaisp() {
		return loaisp;
	}

	public void setLoaisp(String loaisp) {
		this.loaisp = loaisp;
	}

	public String getMatctd() {
		return matctd;
	}

	public void setMatctd(String matctd) {
		this.matctd = matctd;
	}

	public String getTentctd() {
		return tentctd;
	}

	public void setTentctd(String tentctd) {
		this.tentctd = tentctd;
	}

	public String getNgayhoitintu() {
		return ngayhoitintu;
	}

	public void setNgayhoitintu(String ngayhoitintu) {
		this.ngayhoitintu = ngayhoitintu;
	}

	public String getNgayhoitinden() {
		return ngayhoitinden;
	}

	public void setNgayhoitinden(String ngayhoitinden) {
		this.ngayhoitinden = ngayhoitinden;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
