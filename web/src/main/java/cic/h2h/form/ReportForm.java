package cic.h2h.form;

import entity.frwk.SysUsers;
import frwk.form.ModelForm;

public class ReportForm extends ModelForm<SysUsers> {

	private String fileName, result;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	@Override
	public SysUsers getModel() {
		return null;
	}

}
