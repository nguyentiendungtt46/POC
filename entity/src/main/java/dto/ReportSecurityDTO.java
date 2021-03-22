package dto;

import java.io.Serializable;

import common.util.Formater;

public class ReportSecurityDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String partnerCode;
	private String partnerName;
	private String ipAddress;
	private String service;
	private String product;
	private String frequency;
	private String cycle;
	private String securityCode;
	private String securityName;
	private String securityValue;
	private String type;

	public ReportSecurityDTO() {
		super();
	}

	public ReportSecurityDTO(String partnerCode, String partnername, String ipAddress, String service, String product,
			String frequency, String cycle, String securityCode, String securityName, String securityValue,
			String type) {
		super();
		this.partnerCode = partnerCode;
		this.partnerName = partnername;
		this.ipAddress = ipAddress;
		this.service = service;
		this.product = product;
		this.frequency = frequency;
		if (!Formater.isNull(cycle)) {
			if ("1".equals(cycle)) {
				this.cycle = "Gi\u00E2y";
			} else if ("2".equals(cycle)) {
				this.cycle = "Ph\u00FAt";
			} else if ("3".equals(cycle)) {
				this.cycle = "Gi\u1EDD";
			} else if ("4".equals(cycle)) {
				this.cycle = "Ng\u00E0y";
			} else if ("5".equals(cycle)) {
				this.cycle = "Th\u00E1ng";
			}
			
		}
		
		this.securityCode = securityCode;
		this.securityName = securityName;
		this.securityValue = securityValue;
		this.type = type;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public String getSecurityName() {
		return securityName;
	}

	public void setSecurityName(String securityName) {
		this.securityName = securityName;
	}

	public String getSecurityValue() {
		return securityValue;
	}

	public void setSecurityValue(String securityValue) {
		this.securityValue = securityValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
