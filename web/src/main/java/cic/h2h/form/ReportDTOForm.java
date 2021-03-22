package cic.h2h.form;

import dto.ReportDTO;
import dto.ReportSecurityDTO;
import frwk.form.SearchForm;

public class ReportDTOForm extends SearchForm<ReportDTO> {

	ReportDTO reportSscurity = new ReportDTO();

	private String job_name, serviceInfos, ipAddress, type, codeSecurity, fromDate, toDate, branchCode, violateCode;

	public ReportDTO getReportSscurity() {
		return reportSscurity;
	}

	public void setReportSscurity(ReportDTO reportSscurity) {
		this.reportSscurity = reportSscurity;
	}

	public String getJob_name() {
		return job_name;
	}

	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}

	public String getServiceInfos() {
		return serviceInfos;
	}

	public void setServiceInfos(String serviceInfos) {
		this.serviceInfos = serviceInfos;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCodeSecurity() {
		return codeSecurity;
	}

	public void setCodeSecurity(String codeSecurity) {
		this.codeSecurity = codeSecurity;
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

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getViolateCode() {
		return violateCode;
	}

	public void setViolateCode(String violateCode) {
		this.violateCode = violateCode;
	}

	@Override
	public ReportDTO getModel() {
		// TODO Auto-generated method stub
		return reportSscurity;
	}

}
