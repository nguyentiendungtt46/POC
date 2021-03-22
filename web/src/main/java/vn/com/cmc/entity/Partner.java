package vn.com.cmc.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;


/**
 * The persistent class for the PARTNER database table.
 * 
 */
@Entity
@NamedQuery(name="Partner.findAll", query="SELECT p FROM Partner p")
public class Partner implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Expose
	private String id;

	@Column(name="ACC_NUMBER")
	private String accNumber;

	private String address;

	@Column(name="AU_METHOD")
	private String auMethod;

	@Column(name="AUTO_CHANGE_DATE")
	private BigDecimal autoChangeDate;

	@Column(name="BANKPAY_MEM")
	private BigDecimal bankpayMem;

	@Column(name="BHXH_CO_QUAN_ID")
	private String bhxhCoQuanId;

	@Column(name="BIC_CODE")
	private String bicCode;

	private String cif;

	@Expose
	private String code;

	private String defaultcot;

	private String email;

	private String fivedigitcode;

	@Column(name="FOREIGN_BANK")
	private BigDecimal foreignBank;

	@Column(name="GET_CHK_TEL")
	private BigDecimal getChkTel;

	private BigDecimal gmtpls;

	private String intcde;

	@Column(name="IS_RESTART_ADAPTER")
	private BigDecimal isRestartAdapter;

	@Temporal(TemporalType.DATE)
	@Column(name="MODIFY_TIME")
	private Date modifyTime;
	
	@Temporal(TemporalType.DATE)
	@Column(name="VALIDITY_CONTRACT_FROM")
	private Date validityContractFrom;
	
	@Temporal(TemporalType.DATE)
	@Column(name="VALIDITY_CONTRACT_TO")
	private Date validityContractTo;
	
	@Expose
	private String name;

	private String note;

	@Column(name="PARTNER_TYPE")
	private BigDecimal partnerType;

	private BigDecimal paymentafter;

	private String phone;

	private String provincecode;

	@Column(name="RPT_DATA_SOURCE")
	private String rptDataSource;

	@Column(name="SC_INS")
	private BigDecimal scIns;

	@Column(name="SEND_ACK")
	private BigDecimal sendAck;

	@Column(name="SIGN_VERIFY")
	private BigDecimal signVerify;

	@Column(name="SORT_NAME")
	private String sortName;

	private BigDecimal status;

	private String wspass;

	private String wspassonpartner;

	private String wsuser;

	//bi-directional many-to-one association to SysUser
	@OneToMany(mappedBy="partner1")
	@JsonIgnore
	private List<SysUsers> sysUsers1;

	//bi-directional many-to-one association to SysUser
	@OneToMany(mappedBy="partnerCompany")
	@JsonIgnore
	private List<SysUsers> sysUsers2;

	//bi-directional many-to-one association to PartnerConnectIp
	@OneToMany(mappedBy="partner")
	@JsonIgnore
	private List<PartnerConnectIp> partnerConnectIps;
	
	//bi-directional many-to-one association to LogService
	@OneToMany(mappedBy="partner")
	@JsonIgnore
	private List<LogService> logServices;
	

	@Column(name = "NOTIFI_CONTENT")
	private String notifiContent;
	@Column(name = "IS_NOTIFI")
	private Boolean isNotifi;
	

	public String getNotifiContent() {
		return notifiContent;
	}

	public void setNotifiContent(String notifiContent) {
		this.notifiContent = notifiContent;
	}

	public Boolean getIsNotifi() {
		return isNotifi;
	}

	public void setIsNotifi(Boolean isNotifi) {
		this.isNotifi = isNotifi;
	}
	
	public Partner() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccNumber() {
		return this.accNumber;
	}

	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAuMethod() {
		return this.auMethod;
	}

	public void setAuMethod(String auMethod) {
		this.auMethod = auMethod;
	}

	public BigDecimal getAutoChangeDate() {
		return this.autoChangeDate;
	}

	public void setAutoChangeDate(BigDecimal autoChangeDate) {
		this.autoChangeDate = autoChangeDate;
	}

	public BigDecimal getBankpayMem() {
		return this.bankpayMem;
	}

	public void setBankpayMem(BigDecimal bankpayMem) {
		this.bankpayMem = bankpayMem;
	}

	public String getBhxhCoQuanId() {
		return this.bhxhCoQuanId;
	}

	public void setBhxhCoQuanId(String bhxhCoQuanId) {
		this.bhxhCoQuanId = bhxhCoQuanId;
	}

	public String getBicCode() {
		return this.bicCode;
	}

	public void setBicCode(String bicCode) {
		this.bicCode = bicCode;
	}

	public String getCif() {
		return this.cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDefaultcot() {
		return this.defaultcot;
	}

	public void setDefaultcot(String defaultcot) {
		this.defaultcot = defaultcot;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFivedigitcode() {
		return this.fivedigitcode;
	}

	public void setFivedigitcode(String fivedigitcode) {
		this.fivedigitcode = fivedigitcode;
	}

	public BigDecimal getForeignBank() {
		return this.foreignBank;
	}

	public void setForeignBank(BigDecimal foreignBank) {
		this.foreignBank = foreignBank;
	}

	public BigDecimal getGetChkTel() {
		return this.getChkTel;
	}

	public void setGetChkTel(BigDecimal getChkTel) {
		this.getChkTel = getChkTel;
	}

	public BigDecimal getGmtpls() {
		return this.gmtpls;
	}

	public void setGmtpls(BigDecimal gmtpls) {
		this.gmtpls = gmtpls;
	}

	public String getIntcde() {
		return this.intcde;
	}

	public void setIntcde(String intcde) {
		this.intcde = intcde;
	}

	public BigDecimal getIsRestartAdapter() {
		return this.isRestartAdapter;
	}

	public void setIsRestartAdapter(BigDecimal isRestartAdapter) {
		this.isRestartAdapter = isRestartAdapter;
	}

	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public Date getValidityContractFrom() {
		return validityContractFrom;
	}

	public void setValidityContractFrom(Date validityContractFrom) {
		this.validityContractFrom = validityContractFrom;
	}

	public Date getValidityContractTo() {
		return validityContractTo;
	}

	public void setValidityContractTo(Date validityContractTo) {
		this.validityContractTo = validityContractTo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getPartnerType() {
		return this.partnerType;
	}

	public void setPartnerType(BigDecimal partnerType) {
		this.partnerType = partnerType;
	}

	public BigDecimal getPaymentafter() {
		return this.paymentafter;
	}

	public void setPaymentafter(BigDecimal paymentafter) {
		this.paymentafter = paymentafter;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProvincecode() {
		return this.provincecode;
	}

	public void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}

	public String getRptDataSource() {
		return this.rptDataSource;
	}

	public void setRptDataSource(String rptDataSource) {
		this.rptDataSource = rptDataSource;
	}

	public BigDecimal getScIns() {
		return this.scIns;
	}

	public void setScIns(BigDecimal scIns) {
		this.scIns = scIns;
	}

	public BigDecimal getSendAck() {
		return this.sendAck;
	}

	public void setSendAck(BigDecimal sendAck) {
		this.sendAck = sendAck;
	}

	public BigDecimal getSignVerify() {
		return this.signVerify;
	}

	public void setSignVerify(BigDecimal signVerify) {
		this.signVerify = signVerify;
	}

	public String getSortName() {
		return this.sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public BigDecimal getStatus() {
		return this.status;
	}

	public void setStatus(BigDecimal status) {
		this.status = status;
	}

	public String getWspass() {
		return this.wspass;
	}

	public void setWspass(String wspass) {
		this.wspass = wspass;
	}

	public String getWspassonpartner() {
		return this.wspassonpartner;
	}

	public void setWspassonpartner(String wspassonpartner) {
		this.wspassonpartner = wspassonpartner;
	}

	public String getWsuser() {
		return this.wsuser;
	}

	public void setWsuser(String wsuser) {
		this.wsuser = wsuser;
	}

	public List<SysUsers> getSysUsers1() {
		return this.sysUsers1;
	}

	public void setSysUsers1(List<SysUsers> sysUsers1) {
		this.sysUsers1 = sysUsers1;
	}

	public SysUsers addSysUsers1(SysUsers sysUsers1) {
		getSysUsers1().add(sysUsers1);
		sysUsers1.setPartner1(this);

		return sysUsers1;
	}

	public SysUsers removeSysUsers1(SysUsers sysUsers1) {
		getSysUsers1().remove(sysUsers1);
		sysUsers1.setPartner1(null);

		return sysUsers1;
	}

	public List<SysUsers> getSysUsers2() {
		return this.sysUsers2;
	}

	public void setSysUsers2(List<SysUsers> sysUsers2) {
		this.sysUsers2 = sysUsers2;
	}

	public SysUsers addSysUsers2(SysUsers sysUsers2) {
		getSysUsers2().add(sysUsers2);
		sysUsers2.setPartnerCompany(this);

		return sysUsers2;
	}

	public SysUsers removeSysUsers2(SysUsers sysUsers2) {
		getSysUsers2().remove(sysUsers2);
		sysUsers2.setPartnerCompany(null);

		return sysUsers2;
	}

	public List<PartnerConnectIp> getPartnerConnectIps() {
		return partnerConnectIps;
	}

	public void setPartnerConnectIps(List<PartnerConnectIp> partnerConnectIps) {
		this.partnerConnectIps = partnerConnectIps;
	}
	
	public PartnerConnectIp addPartnerConnectIp(PartnerConnectIp partnerConnectIp) {
		getPartnerConnectIps().add(partnerConnectIp);
		partnerConnectIp.setPartner(this);

		return partnerConnectIp;
	}

	public PartnerConnectIp removePartnerConnectIp(PartnerConnectIp partnerConnectIp) {
		getPartnerConnectIps().remove(partnerConnectIp);
		partnerConnectIp.setPartner(null);

		return partnerConnectIp;
	}

	public List<LogService> getLogServices() {
		return logServices;
	}

	public void setLogServices(List<LogService> logServices) {
		this.logServices = logServices;
	}
	
	public LogService addLogServices(LogService logService) {
		getLogServices().add(logService);
		logService.setPartner(this);

		return logService;
	}

	public LogService removeLogServices(LogService logService) {
		getLogServices().remove(logService);
		logService.setPartner(null);

		return logService;
	}
	
	

}