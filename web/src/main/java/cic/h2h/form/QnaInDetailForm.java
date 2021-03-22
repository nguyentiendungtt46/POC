package cic.h2h.form;

import entity.QnaInDetail;
import frwk.form.SearchForm;

public class QnaInDetailForm extends SearchForm<QnaInDetail> {
	private String branch;
	private String malo;
	private String matctd;
	private String ngaytaotu;
	private String ngaytaoden;
	private String maphieu;
	private String qnaInMsId;
	private String macic;
	private String makh;
	private String tenkh;
	private String socmnd;
	private String masothue;
	private Boolean hasResult, cicErr;
	private String partnerId;
	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public Boolean getCicErr() {
		return cicErr;
	}

	public void setCicErr(Boolean cicErr) {
		this.cicErr = cicErr;
	}

	public Boolean getHasResult() {
		return hasResult;
	}

	public void setHasResult(Boolean hasResult) {
		this.hasResult = hasResult;
	}
	
	QnaInDetail qnaInDetail = new QnaInDetail();

	public QnaInDetail getQnaInDetail() {
		return qnaInDetail;
	}

	public void setQnaInDetail(QnaInDetail qnaInDetail) {
		this.qnaInDetail = qnaInDetail;
	}

	@Override
	public QnaInDetail getModel() {
		return qnaInDetail;
	}

	public String getMalo() {
		return malo;
	}

	public void setMalo(String malo) {
		this.malo = malo;
	}

	public String getMatctd() {
		return matctd;
	}

	public void setMatctd(String matctd) {
		this.matctd = matctd;
	}

	public String getMaphieu() {
		return maphieu;
	}

	public void setMaphieu(String maphieu) {
		this.maphieu = maphieu;
	}

	public String getQnaInMsId() {
		return qnaInMsId;
	}

	public void setQnaInMsId(String qnaInMsId) {
		this.qnaInMsId = qnaInMsId;
	}
	public String getMacic() {
		return macic;
	}

	public void setMacic(String macic) {
		this.macic = macic;
	}

	public String getMakh() {
		return makh;
	}

	public void setMakh(String makh) {
		this.makh = makh;
	}

	public String getTenkh() {
		return tenkh;
	}

	public void setTenkh(String tenkh) {
		this.tenkh = tenkh;
	}

	public String getSocmnd() {
		return socmnd;
	}

	public void setSocmnd(String socmnd) {
		this.socmnd = socmnd;
	}

	public String getMasothue() {
		return masothue;
	}

	public void setMasothue(String masothue) {
		this.masothue = masothue;
	}

	public String getNgaytaotu() {
		return ngaytaotu;
	}

	public void setNgaytaotu(String ngaytaotu) {
		this.ngaytaotu = ngaytaotu;
	}

	public String getNgaytaoden() {
		return ngaytaoden;
	}

	public void setNgaytaoden(String ngaytaoden) {
		this.ngaytaoden = ngaytaoden;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}
	
	
	
}
