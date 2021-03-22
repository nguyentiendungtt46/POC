package vn.com.cmc.entity;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;


/**
 * The persistent class for the OAUTH_ACCESS_TOKEN database table.
 * 
 */
@Entity
@Table(name="OAUTH_ACCESS_TOKEN")
@NamedQuery(name="OauthAccessToken.findAll", query="SELECT o FROM OauthAccessToken o")
public class OauthAccessToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="AUTHENTICATION_ID")
	@Expose
	private String authenticationId;

	@Lob
	@Expose
	private byte[] authentication;

	@Column(name="CLIENT_ID")
	private String clientId;

	@Column(name="REFRESH_TOKEN")
	private String refreshToken;

	@Lob
	private byte[] token;

	@Column(name="TOKEN_ID")
	private String tokenId;

	@Column(name="USER_NAME")
	@Expose
	private String userName;
	
	@Transient
	@Expose
	private String fullName;
	
	@Transient
	@Expose
	private int expiresIn;
	
	@Transient
	@Expose
	private String codeTCTD;
	
	@Transient
	@Expose
	private String nameTCTD;

	public OauthAccessToken() {
	}
	
//	public OauthAccessToken(String authenticationId, byte[] token, String userName, String fullName, String codeTCTD, String nameTCTD) {
//		super();
//		this.authenticationId = authenticationId;
//		this.token = token;
//		this.userName = userName;
//		this.fullName = fullName;
//		this.codeTCTD = codeTCTD;
//		this.nameTCTD = nameTCTD;
//		if(token != null) {
//			this.expiresIn = deserializeToken(token).getExpiresIn();
//		}
//	}

	public String getAuthenticationId() {
		return this.authenticationId;
	}

	public void setAuthenticationId(String authenticationId) {
		this.authenticationId = authenticationId;
	}

	public byte[] getAuthentication() {
		return this.authentication;
	}

	public void setAuthentication(byte[] authentication) {
		this.authentication = authentication;
	}

	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public byte[] getToken() {
		return this.token;
	}
//
//	public void setToken(byte[] token) {
//		if(token != null) {
//			this.expiresIn = deserializeToken(token).getExpiresIn();
//		}
//		this.token = token;
//	}

	public String getTokenId() {
		return this.tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCodeTCTD() {
		return codeTCTD;
	}

	public void setCodeTCTD(String codeTCTD) {
		this.codeTCTD = codeTCTD;
	}

	public String getNameTCTD() {
		return nameTCTD;
	}

	public void setNameTCTD(String nameTCTD) {
		this.nameTCTD = nameTCTD;
	}
	
	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

//	private DefaultOAuth2AccessToken deserializeToken(byte[] tokenBytes) {
//        try {
//            ByteArrayInputStream bis = new ByteArrayInputStream(tokenBytes);
//            ObjectInput in = new ObjectInputStream(bis);
//            DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) in.readObject();
//            return token;
//        } catch (Exception e) {
//            // ignore
//        }
//
//        return null;
//    }
	
}