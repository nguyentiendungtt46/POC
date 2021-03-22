package cic.h2h.form;

import entity.DefinedError;
import frwk.form.SearchForm;

public class DefinedErrorForm extends SearchForm<DefinedError> {
	DefinedError errorPopup = new DefinedError();

	public DefinedError getErrorPopup() {
		return errorPopup;
	}

	public void setErrorPopup(DefinedError errorPopup) {
		this.errorPopup = errorPopup;
	}
	
	@Override
	public DefinedError getModel() {
		return errorPopup;
	}
}
