package vn.com.cmc.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PARTNER_CONNECT_ACCOUNT database table.
 * 
 */
@Entity
@Table(name="PARTNER_CONNECT_ACCOUNT")
@NamedQuery(name="PartnerConnectAccount.findAll", query="SELECT p FROM PartnerConnectAccount p")
public class PartnerConnectAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="PARTNER_ID")
	private String partnerId;

	private String password;

	private String username;
	
	@Transient
	private String accessToken;
	@Transient
	private String refreshToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public PartnerConnectAccount() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartnerId() {
		return this.partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}