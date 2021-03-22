package vn.com.cmc.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the LOG_CHECK_CONNECT database table.
 * 
 */
@Entity
@Table(name="LOG_CHECK_CONNECT")
@NamedQuery(name="LogCheckConnect.findAll", query="SELECT l FROM LogCheckConnect l")
public class LogCheckConnect implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@Column(name="CONNECT_RESULT")
	private Long connectResult;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;

	@Column(name="IP_ADDRESS")
	private String ipAddress;

	@Column(name="PARTNER_ID")
	private String partnerId;
	
	@Column(name="ERROR")
	private String error;

	public LogCheckConnect() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getConnectResult() {
		return this.connectResult;
	}

	public void setConnectResult(Long connectResult) {
		this.connectResult = connectResult;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getPartnerId() {
		return this.partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}