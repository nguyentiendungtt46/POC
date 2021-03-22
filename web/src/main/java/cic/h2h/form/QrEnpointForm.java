package cic.h2h.form;

import entity.QrEndpoint;
import frwk.form.SearchForm;

public class QrEnpointForm extends SearchForm<QrEndpoint> {

	QrEndpoint endpoint = new QrEndpoint();
	private String code, endpointSearch;

	public QrEndpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(QrEndpoint endpoint) {
		this.endpoint = endpoint;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEndpointSearch() {
		return endpointSearch;
	}

	public void setEndpointSearch(String endpointSearch) {
		this.endpointSearch = endpointSearch;
	}

	@Override
	public QrEndpoint getModel() {
		// TODO Auto-generated method stub
		return endpoint;
	}

}
