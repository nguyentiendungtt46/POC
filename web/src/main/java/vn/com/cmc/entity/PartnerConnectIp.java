package vn.com.cmc.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PARTNER_CONNECT_IP database table.
 * 
 */
@Entity
@Table(name="PARTNER_CONNECT_IP")
@NamedQuery(name="PartnerConnectIp.findAll", query="SELECT p FROM PartnerConnectIp p")
public class PartnerConnectIp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	@Column(name="IP_ADDRESS")
	private String ipAddress;

	//bi-directional many-to-one association to Partner
	@ManyToOne
	@JoinColumn(name="PARTNER_ID")
	private Partner partner;

	public PartnerConnectIp() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

}