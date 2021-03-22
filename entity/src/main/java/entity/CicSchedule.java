package entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CIC_SCHEDULE")
public class CicSchedule implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@Column(name = "SERVICE")
	private String service;
	@Column(name = "SAN_PHAM")
	private String product;
	@Column(name = "FREQUENCY")
	private String frequency;

	@Column(name = "STATUS")
	private Boolean status;
	@Column(name = "TIME_TO_LIVE")
	private BigDecimal timeToLive;
	@Column(name = "MAX_SIZE_API")
	private BigDecimal maxSizeApi;

	@Column(name = "pr_code")
	private String prCode;

	@Column(name = "sv_code")
	private String svCode;

	public CicSchedule() {
		super();
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public BigDecimal getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(BigDecimal timeToLive) {
		this.timeToLive = timeToLive;
	}

	public BigDecimal getMaxSizeApi() {
		return maxSizeApi;
	}

	public void setMaxSizeApi(BigDecimal maxSizeApi) {
		this.maxSizeApi = maxSizeApi;
	}

	public String getPrCode() {
		return prCode;
	}

	public void setPrCode(String prCode) {
		this.prCode = prCode;
	}

	public String getSvCode() {
		return svCode;
	}

	public void setSvCode(String svCode) {
		this.svCode = svCode;
	}

}
