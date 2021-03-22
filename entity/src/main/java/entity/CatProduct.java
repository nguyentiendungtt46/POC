package entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.util.FormatNumber;

@Entity
@Table(name = "CAT_PRODUCT")
public class CatProduct implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;
	@Column(name = "CODE")
	private String code;
	@Column(name = "NAME")
	private String name;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "STATUS")
	private Boolean status;
	@Column(name = "CALL_BY_JOB")
	private Boolean callByJob;
	@Transient
	private String statusStr;
	@Column(name = "CREATOR")
	private String creator;
	@Column(name = "APPROVE")
	private String approve;
	@Column(name = "CREATE_DATE")
	private Date createDate;
	@Column(name = "APPROVE_DATE")
	private Date approveDate;
	@Column(name = "FIX_PRODUCT", precision = 1, scale = 0)
	private Boolean fixProduct;
	@Column(name = "SERVICE_ID")
	private String serviceId;

	@Column(name = "CODE_QA")
	private String codeQA;

	@Column(name = "FREQUENCY")
	private BigDecimal frequency;

	@Transient
	private ServiceInfo serviceInfo;
	
	@Column(name = "TYPE_PRODUCT")
	private Short typeProduct;
	@Column(name="Order_num")
	private Short order;
	public Boolean getFixProduct() {
		return fixProduct;
	}

	public void setFixProduct(Boolean fixProduct) {
		this.fixProduct = fixProduct;
	}

	@JsonIgnore
	@JoinColumn(name = "PARTNER", referencedColumnName = "ID")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private Partner partnerId;

	@Transient
	private String strIndex;
	@Transient
	private String strFixProduct;

	@Column(name = "MAX_SIZE_API")
	private BigDecimal maxSizeApi;

	@Column(name = "TIME_TO_LIVE")
	private BigDecimal timeToLive;

	public CatProduct() {
	}

	public CatProduct(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Short getTypeProduct() {
		return typeProduct;
	}

	public void setTypeProduct(Short typeProduct) {
		this.typeProduct = typeProduct;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getApprove() {
		return approve;
	}

	public void setApprove(String approve) {
		this.approve = approve;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	public Partner getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Partner partnerId) {
		this.partnerId = partnerId;
	}

	@Column(name = "PRICE")
	private BigDecimal price;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
		this.priceStr = FormatNumber.num2Str(price);
	}

	@Transient
	private String priceStr;

	public String getPriceStr() {
		return priceStr;
	}

	public void setPriceStr(String priceStr) throws Exception {
		this.priceStr = priceStr;
		this.price = FormatNumber.str2num(priceStr);
	}

	public String getStrIndex() {
		return strIndex;
	}

	public void setStrIndex(String strIndex) {
		this.strIndex = strIndex;
	}

//	public List<MapProduct> getMapProduct() {
//		return mapProduct;
//	}
//
//	public void setMapProduct(List<MapProduct> mapProduct) {
//		this.mapProduct = mapProduct;
//	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
		this.serviceInfo = new ServiceInfo(serviceId);
	}


	public String getStrFixProduct() {
		return strFixProduct;
	}

	public void setStrFixProduct(String strFixProduct) {
		this.strFixProduct = strFixProduct;
	}

	public String getCodeQA() {
		return codeQA;
	}

	public void setCodeQA(String codeQA) {
		this.codeQA = codeQA;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public BigDecimal getMaxSizeApi() {
		return maxSizeApi;
	}

	public void setMaxSizeApi(BigDecimal maxSizeApi) {
		this.maxSizeApi = maxSizeApi;
	}

	public BigDecimal getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(BigDecimal timeToLive) {
		this.timeToLive = timeToLive;
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
