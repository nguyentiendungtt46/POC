package cic.h2h.form;

import frwk.form.SearchForm;
import vn.org.intergration.ws.endpoint.cicqr.VanTinChung;

public class VanTinChungForm extends SearchForm<VanTinChung> {

	VanTinChung request = new VanTinChung();

	public VanTinChung getRequest() {
		return request;
	}

	public void setRequest(VanTinChung request) {
		this.request = request;
	}

	@Override
	public VanTinChung getModel() {
		// TODO Auto-generated method stub
		return request;
	}

}
