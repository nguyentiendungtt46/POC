package cic.h2h.form;

import entity.ServiceInfo;
import frwk.form.SearchForm;

public class ServiceInfoForm extends SearchForm<ServiceInfo> {

	private String code;
	private String name;
	private String status;
	private Short type;
	private String apiServerName;
	private String apiPublishName;

	public String getApiServerName() {
		return apiServerName;
	}

	public void setApiServerName(String apiServerName) {
		this.apiServerName = apiServerName;
	}

	public String getApiPublishName() {
		return apiPublishName;
	}

	public void setApiPublishName(String apiPublishName) {
		this.apiPublishName = apiPublishName;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private ServiceInfo serviceInfo;

	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	@Override
	public ServiceInfo getModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
