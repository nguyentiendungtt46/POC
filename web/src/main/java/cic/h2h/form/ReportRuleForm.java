package cic.h2h.form;

import entity.RpRule;
import frwk.form.ModelForm;
import frwk.form.SearchForm;

public class ReportRuleForm extends SearchForm<RpRule> {

	private String code, name;
	private RpRule rpRule = new RpRule();

	public RpRule getRpRule() {
		return rpRule;
	}

	public void setRpRule(RpRule rpRule) {
		this.rpRule = rpRule;
	}

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

	@Override
	public RpRule getModel() {
		return rpRule;
	}

}
