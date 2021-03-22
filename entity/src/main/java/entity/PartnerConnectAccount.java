package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import entity.frwk.SysUsers;

@Entity
@Table(name = "PARTNER_CONNECT_ACCOUNT")
public class PartnerConnectAccount implements java.io.Serializable {
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

	@JoinColumn(name = "SYSUSER_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
    private SysUsers sysUserId;
    
    
	public PartnerConnectAccount() {
		super();
	}


	public PartnerConnectAccount(String id, Partner partnerId, SysUsers sysUserId) {
		this.id = id;
		this.partnerId = partnerId;
		this.sysUserId = sysUserId;
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

	public SysUsers getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(SysUsers sysUserId) {
		this.sysUserId = sysUserId;
	}
}
