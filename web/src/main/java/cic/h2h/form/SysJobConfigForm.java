package cic.h2h.form;

import entity.SysJobConfig;
import frwk.form.SearchForm;

public class SysJobConfigForm extends SearchForm<SysJobConfig> {
	private String code;
	private String name;
	SysJobConfig sysJobConfig = new SysJobConfig();
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SysJobConfig getSysJobConfig() {
		return sysJobConfig;
	}
	public void setSysJobConfig(SysJobConfig sysJobConfig) {
		this.sysJobConfig = sysJobConfig;
	}
	@Override
	public SysJobConfig getModel() {
		// TODO Auto-generated method stub
		return sysJobConfig;
	}
	
}
