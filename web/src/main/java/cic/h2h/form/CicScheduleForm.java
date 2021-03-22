package cic.h2h.form;

import entity.CicSchedule;
import frwk.form.SearchForm;

public class CicScheduleForm extends SearchForm<CicSchedule> {

	CicSchedule cicSchedule = new CicSchedule();

	public CicSchedule getCicSchedule() {
		return cicSchedule;
	}

	public void setCicSchedule(CicSchedule cicSchedule) {
		this.cicSchedule = cicSchedule;
	}

	@Override
	public CicSchedule getModel() {
		// TODO Auto-generated method stub
		return cicSchedule;
	}

}
