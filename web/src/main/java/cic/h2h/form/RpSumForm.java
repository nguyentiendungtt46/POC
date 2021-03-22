package cic.h2h.form;

import java.util.ArrayList;
import java.util.List;

import entity.RpSum;
import entity.RpValidateDetail;
import frwk.form.ModelForm;
import frwk.form.SearchForm;

public class RpSumForm extends SearchForm<RpSum> {

	private String userReport;
	private String reportCode;
	private String fileName;
	private String matctd;
	private Short status;
	private String formDate;
	private String toDate;
	private String branch;
	
	private Boolean re_do,partnerCancel;
	

	public Boolean getPartnerCancel() {
		return partnerCancel;
	}

	public void setPartnerCancel(Boolean partnerCancel) {
		this.partnerCancel = partnerCancel;
	}

	public Boolean getRe_do() {
		return re_do;
	}

	public void setRe_do(Boolean re_do) {
		this.re_do = re_do;
	}

	private List<RpValidateDetail> validateArrayList = new ArrayList<RpValidateDetail>();

	RpSum rpSum = new RpSum();

	public RpSum getRpSum() {
		return rpSum;
	}

	public void setRpSum(RpSum rpSum) {
		this.rpSum = rpSum;
	}

	@Override
	public RpSum getModel() {
		return rpSum;
	}

	public String getUserReport() {
		return userReport;
	}

	public void setUserReport(String userReport) {
		this.userReport = userReport;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public List<RpValidateDetail> getValidateArrayList() {
		return validateArrayList;
	}

	public void setValidateArrayList(List<RpValidateDetail> validateArrayList) {
		this.validateArrayList = validateArrayList;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMatctd() {
		return matctd;
	}

	public void setMatctd(String matctd) {
		this.matctd = matctd;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getFormDate() {
		return formDate;
	}

	public void setFormDate(String formDate) {
		this.formDate = formDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}
	
}
