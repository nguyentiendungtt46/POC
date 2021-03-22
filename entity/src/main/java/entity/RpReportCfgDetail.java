package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.util.Formater;

@Entity
@Table(name = "RP_REPORT_CFG_DETAIL")
public class RpReportCfgDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	@Column(name = "CONFIG_KEY")
	private String configKey;
	@Column(name = "CONFIG_VALUE")
	private String configValue;
	@Column(name = "ERR_CODE")
	private String errCode;
	@Column(name = "ERR_DESC")
	private String errDesc;
	@Column(name = "CREATED_USER")
	@JsonIgnore
	private String createdUser;
	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date createdDate;
	@Column(name = "UPDATED_USER")
	@JsonIgnore
	private String updatedUser;
	@Column(name = "UPDATED_DATE")
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	@Column(name = "STATUS")
	private BigDecimal status;
	@JoinColumn(name = "REPORT_CFG_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	@JsonIgnore
	private RpReportCfgMaster reportCfgId;
	@Transient
	private Boolean process;

	public RpReportCfgDetail() {
		super();
	}

	public RpReportCfgDetail(String id) {
		this.id = id;
	}

	public RpReportCfgDetail(String id, String configKey, String errCode) {
		this.id = id;
		this.configKey = configKey;
		this.errCode = errCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrDesc() {
		return errDesc;
	}

	public void setErrDesc(String errDesc) {
		this.errDesc = errDesc;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public BigDecimal getStatus() {
		return status;
	}

	public void setStatus(BigDecimal status) {
		this.status = status;
	}

	public RpReportCfgMaster getReportCfgId() {
		return reportCfgId;
	}

	public void setReportCfgId(RpReportCfgMaster reportCfgId) {
		this.reportCfgId = reportCfgId;
	}

	public Boolean getProcess() {
		return process;
	}

	public void setProcess(Boolean process) {
		this.process = process;
	}

	public boolean equals(RpReportCfgDetail other) {
		if (Formater.isNull(this.getConfigKey())) {
			if (!Formater.isNull(other.getConfigKey()))
				return false;
		}
		else if (!this.getConfigKey().equals(other.getConfigKey()))
			return false;

		if (Formater.isNull(this.getConfigValue())) {
			if (!Formater.isNull(other.getConfigValue()))
				return false;
		} else if (!this.getConfigValue().equals(other.getConfigValue()))
			return false;

		if (Formater.isNull(this.getErrCode())) {
			if (!Formater.isNull(other.getErrCode()))
				return false;
		} else if (!this.getErrCode().equals(other.getErrCode()))
			return false;

		if (Formater.isNull(this.getErrDesc())) {
			if (!Formater.isNull(other.getErrDesc()))
				return false;
		} else if (!this.getErrDesc().equals(other.getErrDesc()))
			return false;

		return true;
	}
}
