package cic.h2h.form;

import frwk.form.SearchForm;
import vn.org.intergration.ws.endpoint.cicqr.TimKiemKH;

public class TimKiemKHForm extends SearchForm<TimKiemKH> {

	TimKiemKH timkiem = new TimKiemKH();

	public TimKiemKH getTimkiem() {
		return timkiem;
	}

	public void setTimkiem(TimKiemKH timkiem) {
		this.timkiem = timkiem;
	}

	@Override
	public TimKiemKH getModel() {
		// TODO Auto-generated method stub
		return timkiem;
	}

}
