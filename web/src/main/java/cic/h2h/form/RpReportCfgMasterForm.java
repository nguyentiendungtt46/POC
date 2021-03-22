package cic.h2h.form;

import java.util.ArrayList;

import entity.RpReportCfgMaster;
import frwk.form.ModelForm;
import frwk.form.SearchForm;

public class RpReportCfgMasterForm extends SearchForm<RpReportCfgMaster> {
	
	private String  keyword_code,keyword_name,reportCode;
	ArrayList<RpReportCfgMaster> rpReportCfgMaster = new ArrayList<RpReportCfgMaster>();	
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

	public ArrayList<RpReportCfgMaster> getRpReportCfgMaster() {
		return rpReportCfgMaster;
	}

	public void setRpReportCfgMaster(ArrayList<RpReportCfgMaster> rpReportCfgMaster) {
		this.rpReportCfgMaster = rpReportCfgMaster;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	@Override
	public RpReportCfgMaster getModel() {
		// TODO Auto-generated method stub
		return rpReportCfgMaster.get(0);
	}

}
