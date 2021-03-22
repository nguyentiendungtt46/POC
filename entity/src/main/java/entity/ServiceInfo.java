package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SERVICE_INFO")
public class ServiceInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@Column(name = "SERVICE_NAME")
	private String serviceName;

	@Column(name = "CODE")
	private String code;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "DESTINATION_HOST")
	private String destinationHost;

	@Column(name = "DESTINATION_URI")
	private String destinationUri;

	@Column(name = "DESTINATION_OPERATION")
	private String destinationOperation;

	@Column(name = "PUBLISH_HOST")
	private String publishHost;

	@Column(name = "PUBLISH_URI")
	private String publishUri;

	@Column(name = "PUBLISH_OPERATION")
	private String publishOperation;

	@Column(name = "REQUEST_CLASS")
	private String requestClass;

	@Column(name = "STATUS")
	private Boolean status;

	@Column(name = "APP_TYPE")
	private Short appType;

	@Column(name = "TYPE")
	private Short type;

	@Column(name = "MAX_SIZE_API")
	private BigDecimal maxSizeApi;

	@Column(name = "TIME_TO_LIVE")
	private BigDecimal timeToLive;

	@Column(name = "FREQUENCY")
	private BigDecimal frequency;
	
	@Column(name="Order_num")
	private Short order;
	
	@Column(name = "CALL_BY_JOB")
	private Boolean callByJob;
	@Transient
	private String typeName;

	public BigDecimal getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(BigDecimal timeToLive) {
		this.timeToLive = timeToLive;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "serviceInfoId")
	private Set<ServiceProduct> serviceProducts = new HashSet<ServiceProduct>(0);

	@Transient
	private ArrayList<ServiceProduct> serviceProductArrayList;

	@Transient
	private ArrayList<ServiceProductClient> serviceProductArrayListClient;

	@Transient
	private List<CatProduct> catProducts;

	public ServiceInfo() {
		super();
	}

	public ServiceInfo(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
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

	public String getDestinationOperation() {
		return destinationOperation;
	}

	public void setDestinationOperation(String destinationOperation) {
		this.destinationOperation = destinationOperation;
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

	public String getPublishOperation() {
		return publishOperation;
	}

	public void setPublishOperation(String publishOperation) {
		this.publishOperation = publishOperation;
	}

	public String getRequestClass() {
		return requestClass;
	}

	public void setRequestClass(String requestClass) {
		this.requestClass = requestClass;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Set<ServiceProduct> getServiceProducts() {
		return serviceProducts;
	}

	public void setServiceProducts(Set<ServiceProduct> serviceProducts) {
		this.serviceProducts = serviceProducts;
		this.serviceProductArrayList.addAll(serviceProducts);
	}

	public ArrayList<ServiceProduct> getServiceProductArrayList() {
		return serviceProductArrayList;
	}

	public void setServiceProductArrayList(ArrayList<ServiceProduct> serviceProductArrayList) {
		this.serviceProductArrayList = serviceProductArrayList;
		this.serviceProducts.addAll(serviceProductArrayList);
	}

	public List<CatProduct> getCatProducts() {
		return catProducts;
	}

	public void setCatProducts(List<CatProduct> catProducts) {
		this.catProducts = catProducts;
	}

	public ArrayList<ServiceProductClient> getServiceProductArrayListClient() {
		return serviceProductArrayListClient;
	}

	public void setServiceProductArrayListClient(ArrayList<ServiceProductClient> serviceProductArrayListClient) {
		this.serviceProductArrayListClient = serviceProductArrayListClient;
	}

	public Short getAppType() {
		return appType;
	}

	public void setAppType(Short appType) {
		this.appType = appType;
	}

	public BigDecimal getMaxSizeApi() {
		return maxSizeApi;
	}

	public void setMaxSizeApi(BigDecimal maxSizeApi) {
		this.maxSizeApi = maxSizeApi;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getFrequency() {
		return frequency;
	}

	public void setFrequency(BigDecimal frequency) {
		this.frequency = frequency;
	}

	public Short getOrder() {
		return order;
	}

	public void setOrder(Short order) {
		this.order = order;
	}

	public Boolean getCallByJob() {
		return callByJob;
	}

	public void setCallByJob(Boolean callByJob) {
		this.callByJob = callByJob;
	}

	
}
