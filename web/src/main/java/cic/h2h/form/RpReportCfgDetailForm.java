package cic.h2h.form;

import java.util.ArrayList;
import java.util.List;

import entity.RpReportCfgDetail;
import entity.frwk.SysDictType;
import frwk.form.ModelForm;
import frwk.form.SearchForm;

public class RpReportCfgDetailForm extends SearchForm<RpReportCfgDetail> {
	private String keyword_code, keyword_name, reportCode, partnerId;
	ArrayList<RpReportCfgDetail> rpReportCfgDetails = new ArrayList<RpReportCfgDetail>();

	public String getKeyword_code() {
		return keyword_code;
	}

	private List<SysDictType> dictTypes = new ArrayList<SysDictType>();

	public void setKeyword_code(String keyword_code) {
		this.keyword_code = keyword_code;
	}

	public String getKeyword_name() {
		return keyword_name;
	}

	public void setKeyword_name(String keyword_name) {
		this.keyword_name = keyword_name;
	}

	public ArrayList<RpReportCfgDetail> getRpReportCfgDetails() {
		return rpReportCfgDetails;
	}

	public void setRpReportCfgDetails(ArrayList<RpReportCfgDetail> rpReportCfgDetails) {
		this.rpReportCfgDetails = rpReportCfgDetails;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public List<SysDictType> getDictTypes() {
		return dictTypes;
	}

	public void setDictTypes(List<SysDictType> dictTypes) {
		this.dictTypes = dictTypes;
	}

	@Override
	public RpReportCfgDetail getModel() {
		// TODO Auto-generated method stub
		return rpReportCfgDetails.get(0);
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

}
