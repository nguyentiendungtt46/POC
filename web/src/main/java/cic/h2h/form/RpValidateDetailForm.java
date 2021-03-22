package cic.h2h.form;

import entity.RpValidateDetail;
import frwk.form.ModelForm;
import frwk.form.SearchForm;

public class RpValidateDetailForm extends SearchForm<RpValidateDetail> {

	RpValidateDetail validateDetail = new RpValidateDetail();

	private String reportId, errorCode, customerCode, maCT, fileName;

	public RpValidateDetail getValidateDetail() {
		return validateDetail;
	}

	public void setValidateDetail(RpValidateDetail validateDetail) {
		this.validateDetail = validateDetail;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getMaCT() {
		return maCT;
	}

	public void setMaCT(String maCT) {
		this.maCT = maCT;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public RpValidateDetail getModel() {
		// TODO Auto-generated method stub
		return validateDetail;
	}

}
