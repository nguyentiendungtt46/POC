package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PARTNER_CONNECT_IP")
public class PartnerConnectIp implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	@JsonIgnore
	@JoinColumn(name = "PARTNER_ID", referencedColumnName = "ID", nullable = false, updatable = false)
	@ManyToOne(optional = false)
	private Partner partnerId;
	@Column(name = "IP_ADDRESS")
	private String ipAddress;

	public PartnerConnectIp() {
		super();
	}

	public PartnerConnectIp(String id, Partner partnerId, String ipAddress) {
		super();
		this.id = id;
		this.partnerId = partnerId;
		this.ipAddress = ipAddress;
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
