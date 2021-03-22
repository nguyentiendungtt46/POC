package cic.h2h.form;

import frwk.form.SearchForm;
import entity.AppFlow;

public class AppFlowForm extends SearchForm<AppFlow> {

	private String scode, sname;
	private String type;

	public String getScode() {
		return scode;
	}

	public void setScode(String scode) {
		this.scode = scode;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	private AppFlow appFlow = new AppFlow();

	public AppFlow getAppFlow() {
		return appFlow;
	}

	public void setAppFlow(AppFlow AppFlow) {
		this.appFlow = AppFlow;
	}

	@Override
	public AppFlow getModel() {
		return appFlow;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
