package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the LOG_CORE_SERVICES database table.
 * 
 */
@Entity
@Table(name = "LOG_CORE_SERVICES")
@NamedQuery(name = "LogCoreService.findAll", query = "SELECT l FROM LogCoreService l")
public class LogCoreService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	private String id;

	@Column(name = "DATA_VOLUMN")
	private Long dataVolumn;

	@Column(name = "ELAPSED_TIME")
	private Long elapsedTime;

	@Temporal(TemporalType.DATE)
	@Column(name = "END_TIME")
	private Date endTime;

	@Column(name = "INPUT")
	private String input;

	private String ip;

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "OUTPUT")
	private String output;

	@Column(name = "PRODUCT_CODE")
	private String productCode;

	@ManyToOne
	@JoinColumn(name = "SERVICE_CODE")
	private ServiceInfo serviceInfo;

	@Temporal(TemporalType.DATE)
	@Column(name = "START_TIME")
	private Date startTime;

	private Long status;

	@Column(name = "TCTD_CODE")
	private String tctdCode;

	@Column(name = "USER_REQUEST")
	private String userRequest;
	
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

	// bi-directional many-to-one association to LogCoreServicesDetail
	@OneToMany(mappedBy = "logCoreService", cascade=CascadeType.ALL)
	private List<LogCoreServicesDetail> logCoreServicesDetails;

	public LogCoreService() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getDataVolumn() {
		return this.dataVolumn;
	}

	public void setDataVolumn(Long dataVolumn) {
		this.dataVolumn = dataVolumn;
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

	public String getInput() {
		return this.input;
	}

	public void setInput(String input) {
		this.input = input;
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

	public String getOutput() {
		return this.output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
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

	public String getTctdCode() {
		return this.tctdCode;
	}

	public void setTctdCode(String tctdCode) {
		this.tctdCode = tctdCode;
	}

	public String getUserRequest() {
		return this.userRequest;
	}

	public void setUserRequest(String userRequest) {
		this.userRequest = userRequest;
	}

	public List<LogCoreServicesDetail> getLogCoreServicesDetails() {
		return this.logCoreServicesDetails;
	}

	public void setLogCoreServicesDetails(List<LogCoreServicesDetail> logCoreServicesDetails) {
		this.logCoreServicesDetails = logCoreServicesDetails;
	}

	public LogCoreServicesDetail addLogCoreServicesDetail(LogCoreServicesDetail logCoreServicesDetail) {
		getLogCoreServicesDetails().add(logCoreServicesDetail);
		logCoreServicesDetail.setLogCoreService(this);

		return logCoreServicesDetail;
	}

	public LogCoreServicesDetail removeLogCoreServicesDetail(LogCoreServicesDetail logCoreServicesDetail) {
		getLogCoreServicesDetails().remove(logCoreServicesDetail);
		logCoreServicesDetail.setLogCoreService(null);

		return logCoreServicesDetail;
	}

}