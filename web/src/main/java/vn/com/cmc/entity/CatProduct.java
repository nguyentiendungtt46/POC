package vn.com.cmc.entity;

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
	private Short status;
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

	@Column(name = "DISABLE")
	private boolean disable;

	@Column(name = "CODE_QA")
	private String codeQA;

	@Column(name = "FREQUENCY")
	private BigDecimal frequency;

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

//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
//	private Set<CatProductCfg> catProductCfgs = new HashSet<CatProductCfg>(0);
//	@Transient
//	private ArrayList<CatProductCfg> catProductCfgArrayList;

	@Transient
	private String strIndex;
	@Transient
	private String strDisable;
	@Transient
	private String strFixProduct;
//	@JsonIgnore
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "catProductId")
//	private List<MapProduct> mapProduct = new ArrayList<MapProduct>();

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
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
//
//	public Set<CatProductCfg> getCatProductCfgs() {
//		return catProductCfgs;
//	}
//
//	public void setCatProductCfgs(Set<CatProductCfg> catProductCfgs) {
//		this.catProductCfgs = catProductCfgs;
//		this.catProductCfgArrayList = new ArrayList<CatProductCfg>(catProductCfgs);
//	}
//
//	public ArrayList<CatProductCfg> getCatProductCfgArrayList() {
//		return catProductCfgArrayList;
//	}
//
//	public void setCatProductCfgArrayList(ArrayList<CatProductCfg> catProductCfgArrayList) {
//		this.catProductCfgArrayList = catProductCfgArrayList;
//		this.catProductCfgs.addAll(catProductCfgArrayList);
//	}

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


	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public String getStrDisable() {
		return strDisable;
	}

	public void setStrDisable(String strDisable) {
		this.strDisable = strDisable;
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

}
