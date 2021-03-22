package cic.h2h.form;

import dto.ReportSecurityDTO;
import frwk.form.SearchForm;

public class ReportSecurityDTOForm extends SearchForm<ReportSecurityDTO> {

	ReportSecurityDTO reportSscurity = new ReportSecurityDTO();

	private String job_name, serviceInfos, ipAddress, type, codeSecurity;

	public ReportSecurityDTO getReportSscurity() {
		return reportSscurity;
	}

	public void setReportSscurity(ReportSecurityDTO reportSscurity) {
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

	@Override
	public ReportSecurityDTO getModel() {
		// TODO Auto-generated method stub
		return reportSscurity;
	}

}
