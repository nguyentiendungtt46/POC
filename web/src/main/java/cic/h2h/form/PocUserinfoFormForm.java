package cic.h2h.form;

import java.io.File;

import entity.PocUserinfoForm;
import frwk.form.SearchForm;

public class PocUserinfoFormForm extends SearchForm<PocUserinfoForm> {
	private PocUserinfoForm erqInf = new PocUserinfoForm();
	private String name, cardId;
	private String status, templateId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public PocUserinfoForm getErqInf() {
		return erqInf;
	}

	public void setErqInf(PocUserinfoForm erqInf) {
		this.erqInf = erqInf;
	}

	@Override
	public PocUserinfoForm getModel() {
		return erqInf;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

}
