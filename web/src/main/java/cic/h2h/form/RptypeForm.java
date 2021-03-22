package cic.h2h.form;

import java.util.Map;

import entity.RpType;
import frwk.form.ModelForm;
import frwk.form.SearchForm;

public class RptypeForm extends SearchForm<RpType> {
	private RpType rpType = new RpType();
	private String templateCode;

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public RpType getRpType() {
		return rpType;
	}

	public void setRpType(RpType rpType) {
		this.rpType = rpType;
	}

	@Override
	public RpType getModel() {
		return rpType;
	}

	private String fileType;
	private String dataType;
	private Short cusType;
	private Short reportType;

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Short getCusType() {
		return cusType;
	}

	public void setCusType(Short cusType) {
		this.cusType = cusType;
	}

	public Short getReportType() {
		return reportType;
	}

	public void setReportType(Short reportType) {
		this.reportType = reportType;
	}

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private Boolean active;

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
