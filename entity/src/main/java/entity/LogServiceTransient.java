package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the LOG_SERVICES database table.
 * 
 */
@Entity
@Table(name = "LOG_SERVICES")
public class LogServiceTransient implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")

	private String id;

	@Column(name = "ELAPSED_TIME")

	private Long elapsedTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME")

	private Date endTime;

	private String ip;

	@Column(name = "MESSAGE")

	private String message;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME")

	private Date startTime;

	private Long status;

	@Column(name = "TYPE")
	private Short type;
	
	@Formula("dbms_lob.compare(stackTrace, empty_clob() )")
	private Boolean isStackTrace;

	// bi-directional many-to-one association to ServiceInfo
	@ManyToOne
	@JoinColumn(name = "TCTD_CODE")

	private Partner partner;

	@Column(name = "USER_REQUEST")

	private String userRequest;

	@Column(name = "PRODUCT_CODE")

	private String productCode;

	@Column(name = "RESPONSE_CODE")

	private String responseCode;

	@Column(name = "REQUEST_VOL")

	private Long requestVol;

	@Column(name = "RESPONSE_VOL")

	private Long responseVol;

	@Lob
	@Column(name = "REQUEST_CONTENT")

	private String requestContent;

	@Lob
	@Column(name = "RESPONSE_CONTENT")

	private String responseContent;

	@Transient

	private Date fromDate;

	@Transient
	private Date toDate;

	@Transient
	private int typeQueryTime;

	@Transient
	private String stackTrace;

	@Column(name = "CAUSE")
	private String cause;

	// bi-directional many-to-one association to ServiceInfo
	@ManyToOne
	@JoinColumn(name = "SERVICE_CODE")
	private ServiceInfo serviceInfo;

	@Transient
	private BigDecimal elapsedTimeReport;
	@Transient
	private BigDecimal dataVolumnReport;

	public BigDecimal getElapsedTimeReport() {
		return elapsedTimeReport;
	}

	public void setElapsedTimeReport(BigDecimal elapsedTimeReport) {
		this.elapsedTimeReport = elapsedTimeReport;
	}

	public BigDecimal getDataVolumnReport() {
		return dataVolumnReport;
	}

	public void setDataVolumnReport(BigDecimal dataVolumnReport) {
		this.dataVolumnReport = dataVolumnReport;
	}

	public LogServiceTransient(ServiceInfo serviceInfo, Partner partner, String ip, String userRequest, Date startTime,
			Date endTime, Long elapsedTime, Long status, String message, String requestContent) {
		super();
		this.serviceInfo = serviceInfo;
		this.partner = partner;
		this.ip = ip;
		this.userRequest = userRequest;
		this.startTime = startTime;
		this.endTime = endTime;
		this.elapsedTime = elapsedTime;
		this.status = status;
		this.message = message;
		this.requestContent = requestContent;
	}

	public LogServiceTransient() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getElapsedTime() {
		return this.elapsedTime;
	}

	public void setElapsedTime(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public String getUserRequest() {
		return this.userRequest;
	}

	public void setUserRequest(String userRequest) {
		this.userRequest = userRequest;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public int getTypeQueryTime() {
		return typeQueryTime;
	}

	public void setTypeQueryTime(int typeQueryTime) {
		this.typeQueryTime = typeQueryTime;
	}

	public ServiceInfo getServiceInfo() {
		return this.serviceInfo;
	}

	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

	public Long getRequestVol() {
		return requestVol;
	}

	public void setRequestVol(Long requestVol) {
		this.requestVol = requestVol;
	}

	public Long getResponseVol() {
		return responseVol;
	}

	public void setResponseVol(Long responseVol) {
		this.responseVol = responseVol;
	}

	@Transient
	private String tctdCode;

	public String getTctdCode() {
		return tctdCode;
	}

	public void setTctdCode(String tctdCode) {
		this.tctdCode = tctdCode;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public Boolean getIsStackTrace() {
		return isStackTrace;
	}

	public void setIsStackTrace(Boolean isStackTrace) {
		this.isStackTrace = isStackTrace;
	}

}