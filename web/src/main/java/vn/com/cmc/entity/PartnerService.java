package vn.com.cmc.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the PARTNER_SERVICE database table.
 * 
 */
@Entity
@Table(name="PARTNER_SERVICE")
@NamedQuery(name="PartnerService.findAll", query="SELECT p FROM PartnerService p")
public class PartnerService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	@Column(name="DISABLE_STATUS")
	private BigDecimal disableStatus;

	@Column(name="PARTNER_ID")
	private String partnerId;

	private BigDecimal period;

	@Column(name="PRODUCT_ID")
	private String productId;

	private BigDecimal rate;

	@Column(name="SERVICE_ID")
	private String serviceId;

	@Column(name="MAX_OF_DAY")
	private BigDecimal maxOfDay;
	
	public PartnerService() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getDisableStatus() {
		return this.disableStatus;
	}

	public void setDisableStatus(BigDecimal disableStatus) {
		this.disableStatus = disableStatus;
	}

	public String getPartnerId() {
		return this.partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public BigDecimal getPeriod() {
		return this.period;
	}

	public void setPeriod(BigDecimal period) {
		this.period = period;
	}

	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public BigDecimal getRate() {
		return this.rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public BigDecimal getMaxOfDay() {
		return maxOfDay;
	}

	public void setMaxOfDay(BigDecimal maxOfDay) {
		this.maxOfDay = maxOfDay;
	}

}