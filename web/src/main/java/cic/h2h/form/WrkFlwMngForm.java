package cic.h2h.form;

import entity.WrkFlwMng;
import frwk.form.SearchForm;

public class WrkFlwMngForm extends SearchForm<WrkFlwMng> {

	WrkFlwMng wrkFlwMng = new WrkFlwMng();

	public WrkFlwMng getWrkFlwMng() {
		return wrkFlwMng;
	}

	public void setWrkFlwMng(WrkFlwMng wrkFlwMng) {
		this.wrkFlwMng = wrkFlwMng;
	}

	@Override
	public WrkFlwMng getModel() {
		// TODO Auto-generated method stub
		return wrkFlwMng;
	}

}
