/**
 * 
 */
package dto;

import java.io.Serializable;

/**
 * @author nvtiep
 *
 *         created: Sep 8, 2020 2:13:35 PM
 */
public class ReportDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serviceId;
	private String serviceName;
	private String serviceStatus;
	private String partnerCode;
	private String partnerName;
	private String userRequest;
	private String ipAddress;
	private String startTime;
	private String violateCode;
	private String violateDesc;

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

	public String getUserRequest() {
		return userRequest;
	}

	public void setUserRequest(String userRequest) {
		this.userRequest = userRequest;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getViolateCode() {
		return violateCode;
	}

	public void setViolateCode(String violateCode) {
		this.violateCode = violateCode;
	}

	public String getViolateDesc() {
		return violateDesc;
	}

	public void setViolateDesc(String violateDesc) {
		this.violateDesc = violateDesc;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

}
