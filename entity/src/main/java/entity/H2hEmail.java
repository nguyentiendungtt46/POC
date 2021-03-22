package entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "H2H_EMAIL")
public class H2hEmail implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@Column(name = "EMAIL_TO")
	private String emailTo;

	@Column(name = "EMAIL_SUBJECT")
	private String emailSubject;

	@Column(name = "EMAIL_BODY")
	private String emailBody;

	@Column(name = "SEND_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendTime;

	@Column(name = "STATUS")
	private Short status;

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "CREATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	@Column(name = "TYPE")
	private Short type;

	@Column(name = "TCTD_ID")
	private String tctdId;

	@Transient
	private String partnerCode;
	@Transient
	private String partnerName;

	@Transient
	private String typeStr;
	@Transient
	private String statusStr;

	@Transient
	private String sendTimeStr;

	public H2hEmail() {
		super();
	}

	public H2hEmail(String id) {
		super();
		this.id = id;
	}

	public H2hEmail(String id, String emailTo, String emailSubject, String emailBody, Date sendTime, Short status,
			String message, Date createTime, Short type) {
		super();
		this.id = id;
		this.emailTo = emailTo;
		this.emailSubject = emailSubject;
		this.emailBody = emailBody;
		this.sendTime = sendTime;
		this.status = status;
		this.message = message;
		this.createTime = createTime;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getTctdId() {
		return tctdId;
	}

	public void setTctdId(String tctdId) {
		this.tctdId = tctdId;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getTypeStr() {
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getSendTimeStr() {
		return sendTimeStr;
	}

	public void setSendTimeStr(String sendTimeStr) {
		this.sendTimeStr = sendTimeStr;
	}

}
