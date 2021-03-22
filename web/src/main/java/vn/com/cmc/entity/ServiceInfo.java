package vn.com.cmc.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.google.gson.annotations.Expose;


/**
 * The persistent class for the SERVICE_INFO database table.
 * 
 */
@Entity
@Table(name="SERVICE_INFO")
@NamedQuery(name="ServiceInfo.findAll", query="SELECT s FROM ServiceInfo s")
public class ServiceInfo implements Serializable {
	private static final long serialVersionUID = 1L;


	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Expose
	private String id;

	@Column(name="SERVICE_NAME")
	@Expose
	private String serviceName;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="DESTINATION_HOST")
	private String destinationHost;

	@Column(name="DESTINATION_URI")
	private String destinationUri;
	
	@Column(name="DESTINATION_OPERATION")
	private String destinationOperation;

	@Column(name="PUBLISH_HOST")
	private String publishHost;

	@Column(name="PUBLISH_URI")
	private String publishUri;
	
	@Column(name="PUBLISH_OPERATION")
	@Expose
	private String publishOperation;
	
	@Column(name="REQUEST_CLASS")
	private String requestClass;
	
	@Column(name="STATUS")
	private Long status;
	
	@Column(name="APP_TYPE")
	@Expose
	private Long appType;
	
	@Column(name="MAX_SIZE_API")
	@Expose
	private Long maxSizeApi;
	
	//bi-directional many-to-one association to LogService
	@OneToMany(mappedBy="serviceInfo")
	private List<LogService> logServices;

	public ServiceInfo() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDestinationHost() {
		return destinationHost;
	}

	public void setDestinationHost(String destinationHost) {
		this.destinationHost = destinationHost;
	}

	public String getDestinationUri() {
		return destinationUri;
	}

	public void setDestinationUri(String destinationUri) {
		this.destinationUri = destinationUri;
	}

	public String getPublishHost() {
		return publishHost;
	}

	public void setPublishHost(String publishHost) {
		this.publishHost = publishHost;
	}

	public String getPublishUri() {
		return publishUri;
	}

	public void setPublishUri(String publishUri) {
		this.publishUri = publishUri;
	}

	public String getRequestClass() {
		return requestClass;
	}

	public void setRequestClass(String requestClass) {
		this.requestClass = requestClass;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
	
	public Long getAppType() {
		return appType;
	}

	public void setAppType(Long appType) {
		this.appType = appType;
	}
	
	public Long getMaxSizeApi() {
		return maxSizeApi;
	}

	public void setMaxSizeApi(Long maxSizeApi) {
		this.maxSizeApi = maxSizeApi;
	}

	public String getDestinationOperation() {
		return destinationOperation;
	}

	public void setDestinationOperation(String destinationOperation) {
		this.destinationOperation = destinationOperation;
	}

	public String getPublishOperation() {
		return publishOperation;
	}

	public void setPublishOperation(String publishOperation) {
		this.publishOperation = publishOperation;
	}

	public List<LogService> getLogServices() {
		return this.logServices;
	}

	public void setLogServices(List<LogService> logServices) {
		this.logServices = logServices;
	}

	public LogService addLogService(LogService logService) {
		getLogServices().add(logService);
		logService.setServiceInfo(this);

		return logService;
	}

	public LogService removeLogService(LogService logService) {
		getLogServices().remove(logService);
		logService.setServiceInfo(null);

		return logService;
	}

}