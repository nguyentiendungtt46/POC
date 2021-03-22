package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PARTNER_SERVICE")
public class PartnerService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@JsonIgnore
	@JoinColumn(name = "PARTNER_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private Partner partnerId;
	
	
	@JoinColumn(name = "SERVICE_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.EAGER)
	private ServiceInfo serviceInfoId;
	
	
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.EAGER)
	private CatProduct catProductId;
	@Column(name = "RATE")
	private Long rate;
	@Column(name = "PERIOD")
	private Long period;
	@Column(name = "DISABLE_STATUS")
	private boolean disableStatus;

	public PartnerService() {
		super();
	}

	public PartnerService(String id, Partner partnerId, ServiceInfo serviceInfoId, CatProduct catProductId, Long rate,
			Long period, boolean disableStatus) {
		super();
		this.id = id;
		this.partnerId = partnerId;
		this.serviceInfoId = serviceInfoId;
		this.catProductId = catProductId;
		this.rate = rate;
		this.period = period;
		this.disableStatus = disableStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Partner getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Partner partnerId) {
		this.partnerId = partnerId;
	}

	public ServiceInfo getServiceInfoId() {
		return serviceInfoId;
	}

	public void setServiceInfoId(ServiceInfo serviceInfoId) {
		this.serviceInfoId = serviceInfoId;
	}

	public CatProduct getCatProductId() {
		return catProductId;
	}

	public void setCatProductId(CatProduct catProductId) {
		this.catProductId = catProductId;
	}

	public Long getRate() {
		return rate;
	}

	public void setRate(Long rate) {
		this.rate = rate;
	}

	public Long getPeriod() {
		return period;
	}

	public void setPeriod(Long period) {
		this.period = period;
	}

	public boolean getDisableStatus() {
		return disableStatus;
	}

	public void setDisableStatus(boolean disableStatus) {
		this.disableStatus = disableStatus;
	}

}
