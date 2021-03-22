package cic.h2h.form;

import entity.InfoSearchCic;
import frwk.form.SearchForm;

public class InfoSearchCicForm extends SearchForm<InfoSearchCic> {
	private String  fromDate, toDate, maTCTD, maSP, userRequest, loaiKh;
	InfoSearchCic infoSearchCic = new InfoSearchCic();

	
	public String getLoaiKh() {
		return loaiKh;
	}

	public void setLoaiKh(String loaiKh) {
		this.loaiKh = loaiKh;
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

	public String getMaTCTD() {
		return maTCTD;
	}

	public void setMaTCTD(String maTCTD) {
		this.maTCTD = maTCTD;
	}

	public String getMaSP() {
		return maSP;
	}

	public void setMaSP(String maSP) {
		this.maSP = maSP;
	}

	public String getUserRequest() {
		return userRequest;
	}
	
	public InfoSearchCic getInfoSearchCic() {
		return infoSearchCic;
	}

	public void setInfoSearchCic(InfoSearchCic infoSearchCic) {
		this.infoSearchCic = infoSearchCic;
	}

	public void setUserRequest(String userRequest) {
		this.userRequest = userRequest;
	}

	@Override
	public InfoSearchCic getModel() {
		// TODO Auto-generated method stub
		return infoSearchCic;
	}
}
