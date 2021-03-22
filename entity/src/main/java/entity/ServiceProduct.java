package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SERVICE_PRODUCT")
public class ServiceProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@JsonIgnore
	@JoinColumn(name = "SERVICE_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private ServiceInfo serviceInfoId;

	@JsonIgnore
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.EAGER)
	private CatProduct catProductId;

	public ServiceProduct() {
		super();
	}

	public ServiceProduct(String id, ServiceInfo serviceInfoId, CatProduct catProductId) {
		super();
		this.id = id;
		this.serviceInfoId = serviceInfoId;
		this.catProductId = catProductId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

}
