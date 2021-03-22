package entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.util.Formater;
import entity.frwk.SysUsers;

@Entity
@Table(name = "PARTNER")
public class Partner implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;
	@Column(name = "CODE")
	private String code;
	@Column(name = "NAME")
	private String name;
	@Column(name = "STATUS")
	private Short status;
	@Column(name = "MODIFY_TIME")
	private Date modifyTime;
	@Column(name = "ADDRESS")
	private String address;
	@Column(name = "PHONE")
	private String phone;
	@Column(name = "PAYMENTAFTER")
	private Short paymentafter;
	@Column(name = "ACC_NUMBER")
	private String accNumber;
	@Column(name = "PARTNER_TYPE")
	private Short partnerType;
	@Column(name = "PROVINCECODE")
	private String provincecode;
	@Column(name = "INTCDE")
	private String intcde;
	@Column(name = "SORT_NAME")
	private String sortName;
	@Column(name = "BIC_CODE")
	private String bicCode;
	@Column(name = "AU_METHOD")
	private String auMethod;
	@Column(name = "BANKPAY_MEM")
	private Short bankpayMem;
	@Column(name = "CIF")
	private String cif;
	@Column(name = "WSUSER")
	private String wsuser;
	@Column(name = "WSPASS")
	private String wspass;
	@Column(name = "WSPASSONPARTNER")
	private String wspassonpartner;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "GMTPLS")
	private BigDecimal gmtpls;
	@Column(name = "DEFAULTCOT")
	private String defaultcot;
	@Column(name = "AUTO_CHANGE_DATE")
	private Short autoChangeDate;
	@Column(name = "GET_CHK_TEL")
	private Short getChkTel;
	@Column(name = "SEND_ACK")
	private Short sendAck;
	@Column(name = "FIVEDIGITCODE")
	private String fivedigitcode;
	@Column(name = "IS_RESTART_ADAPTER")
	private Short isRestartAdapter;
	@Column(name = "FOREIGN_BANK")
	private Integer foreignBank;
	@Column(name = "SC_INS")
	private Short scIns;
	@Column(name = "BHXH_CO_QUAN_ID")
	private String bhxhCoQuanId;
	@Column(name = "RPT_DATA_SOURCE")
	private String rptDataSource;
	@Column(name = "SIGN_VERIFY")
	private Short signVerify;
	@Column(name = "EMAIL")
	private String email;
	@Column(name = "NOTE")
	private String note;
	@Column(name = "VALIDITY_CONTRACT_FROM")
	private Date validityContractFrom;
	@Column(name = "VALIDITY_CONTRACT_TO")
	private Date validityContractTo;

	@Column(name = "SEND_EMAIL", precision = 1, scale = 0)
	private boolean sendEmail;
	
	@Column(name = "REASON_UNCONNETT")
	private String reasonUnconnett;
	
	@Column(name = "MAX_USER")
	private BigDecimal maxUser;

	@Transient
	private String validityContractFromStr;
	@Transient
	private String validityContractToStr;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "partnerId")
	private Set<PartnerConnectIp> partnerConnectIps = new HashSet<PartnerConnectIp>(0);
	@Transient
	private ArrayList<PartnerConnectIp> partnerConnectIpArrayList;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "partnerId")
	private Set<PartnerService> partnerServices = new HashSet<PartnerService>(0);
	@Transient
	private ArrayList<PartnerService> partnerServicesArrayList;

	public String getReasonUnconnett() {
		return reasonUnconnett;
	}

	public void setReasonUnconnett(String reasonUnconnett) {
		this.reasonUnconnett = reasonUnconnett;
	}

	@Transient
	private List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyId")
	private Set<SysUsers> sysUsers = new HashSet<SysUsers>(0);
	@Transient
	private ArrayList<SysUsers> sysUsersArrayList;

	public Partner() {
		super();
	}

	public Partner(String id, String code, String name, Short status, Date modifyTime, String address, String phone,
			Short paymentafter, String accNumber, Short partnerType, String provincecode, String intcde,
			String sortName, String bicCode, String auMethod, Short bankpayMem, String cif, String wsuser,
			String wspass, String wspassonpartner, BigDecimal gmtpls, String defaultcot, Short autoChangeDate,
			Short getChkTel, Short sendAck, String fivedigitcode, Short isRestartAdapter, Integer foreignBank,
			Short scIns, String bhxhCoQuanId, String rptDataSource, Short signVerify, String email, String note,
			Date validityContractFrom, Date validityContractTo, boolean sendEmail, String reasonUnconnett,
			String validityContractFromStr, String validityContractToStr, Set<PartnerConnectIp> partnerConnectIps,
			ArrayList<PartnerConnectIp> partnerConnectIpArrayList, Set<PartnerService> partnerServices,
			ArrayList<PartnerService> partnerServicesArrayList, List<ServiceInfo> serviceInfos, Set<SysUsers> sysUsers,
			ArrayList<SysUsers> sysUsersArrayList) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.status = status;
		this.modifyTime = modifyTime;
		this.address = address;
		this.phone = phone;
		this.paymentafter = paymentafter;
		this.accNumber = accNumber;
		this.partnerType = partnerType;
		this.provincecode = provincecode;
		this.intcde = intcde;
		this.sortName = sortName;
		this.bicCode = bicCode;
		this.auMethod = auMethod;
		this.bankpayMem = bankpayMem;
		this.cif = cif;
		this.wsuser = wsuser;
		this.wspass = wspass;
		this.wspassonpartner = wspassonpartner;
		this.gmtpls = gmtpls;
		this.defaultcot = defaultcot;
		this.autoChangeDate = autoChangeDate;
		this.getChkTel = getChkTel;
		this.sendAck = sendAck;
		this.fivedigitcode = fivedigitcode;
		this.isRestartAdapter = isRestartAdapter;
		this.foreignBank = foreignBank;
		this.scIns = scIns;
		this.bhxhCoQuanId = bhxhCoQuanId;
		this.rptDataSource = rptDataSource;
		this.signVerify = signVerify;
		this.email = email;
		this.note = note;
		this.validityContractFrom = validityContractFrom;
		this.validityContractTo = validityContractTo;
		this.sendEmail = sendEmail;
		this.reasonUnconnett = reasonUnconnett;
		this.validityContractFromStr = validityContractFromStr;
		this.validityContractToStr = validityContractToStr;
		this.partnerConnectIps = partnerConnectIps;
		this.partnerConnectIpArrayList = partnerConnectIpArrayList;
		this.partnerServices = partnerServices;
		this.partnerServicesArrayList = partnerServicesArrayList;
		this.serviceInfos = serviceInfos;
		this.sysUsers = sysUsers;
		this.sysUsersArrayList = sysUsersArrayList;
	}

	public Partner(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Short getPaymentafter() {
		return paymentafter;
	}

	public void setPaymentafter(Short paymentafter) {
		this.paymentafter = paymentafter;
	}

	public String getAccNumber() {
		return accNumber;
	}

	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}

	public Short getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(Short partnerType) {
		this.partnerType = partnerType;
	}

	public String getProvincecode() {
		return provincecode;
	}

	public void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}

	public String getIntcde() {
		return intcde;
	}

	public void setIntcde(String intcde) {
		this.intcde = intcde;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getBicCode() {
		return bicCode;
	}

	public void setBicCode(String bicCode) {
		this.bicCode = bicCode;
	}

	public String getAuMethod() {
		return auMethod;
	}

	public void setAuMethod(String auMethod) {
		this.auMethod = auMethod;
	}

	public Short getBankpayMem() {
		return bankpayMem;
	}

	public void setBankpayMem(Short bankpayMem) {
		this.bankpayMem = bankpayMem;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getWsuser() {
		return wsuser;
	}

	public void setWsuser(String wsuser) {
		this.wsuser = wsuser;
	}

	public String getWspass() {
		return wspass;
	}

	public void setWspass(String wspass) {
		this.wspass = wspass;
	}

	public String getWspassonpartner() {
		return wspassonpartner;
	}

	public void setWspassonpartner(String wspassonpartner) {
		this.wspassonpartner = wspassonpartner;
	}

	public BigDecimal getGmtpls() {
		return gmtpls;
	}

	public void setGmtpls(BigDecimal gmtpls) {
		this.gmtpls = gmtpls;
	}

	public String getDefaultcot() {
		return defaultcot;
	}

	public void setDefaultcot(String defaultcot) {
		this.defaultcot = defaultcot;
	}

	public Short getAutoChangeDate() {
		return autoChangeDate;
	}

	public void setAutoChangeDate(Short autoChangeDate) {
		this.autoChangeDate = autoChangeDate;
	}

	public Short getGetChkTel() {
		return getChkTel;
	}

	public void setGetChkTel(Short getChkTel) {
		this.getChkTel = getChkTel;
	}

	public Short getSendAck() {
		return sendAck;
	}

	public void setSendAck(Short sendAck) {
		this.sendAck = sendAck;
	}

	public String getFivedigitcode() {
		return fivedigitcode;
	}

	public void setFivedigitcode(String fivedigitcode) {
		this.fivedigitcode = fivedigitcode;
	}

	public Short getIsRestartAdapter() {
		return isRestartAdapter;
	}

	public void setIsRestartAdapter(Short isRestartAdapter) {
		this.isRestartAdapter = isRestartAdapter;
	}

	public Integer getForeignBank() {
		return foreignBank;
	}

	public void setForeignBank(Integer foreignBank) {
		this.foreignBank = foreignBank;
	}

	public Short getScIns() {
		return scIns;
	}

	public void setScIns(Short scIns) {
		this.scIns = scIns;
	}

	public String getBhxhCoQuanId() {
		return bhxhCoQuanId;
	}

	public void setBhxhCoQuanId(String bhxhCoQuanId) {
		this.bhxhCoQuanId = bhxhCoQuanId;
	}

	public String getRptDataSource() {
		return rptDataSource;
	}

	public void setRptDataSource(String rptDataSource) {
		this.rptDataSource = rptDataSource;
	}

	public Short getSignVerify() {
		return signVerify;
	}

	public void setSignVerify(Short signVerify) {
		this.signVerify = signVerify;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "partnerId", cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<PartnerConnectIp> getPartnerConnectIps() {
		return partnerConnectIps;
	}

	public void setPartnerConnectIps(Set<PartnerConnectIp> partnerConnectIps) {
		this.partnerConnectIps = partnerConnectIps;
		this.partnerConnectIpArrayList = new ArrayList<PartnerConnectIp>(partnerConnectIps);
	}

	public ArrayList<PartnerConnectIp> getPartnerConnectIpArrayList() {
		return partnerConnectIpArrayList;
	}

	public void setPartnerConnectIpArrayList(ArrayList<PartnerConnectIp> partnerConnectIpArrayList) {
		this.partnerConnectIpArrayList = partnerConnectIpArrayList;
		this.partnerConnectIps.addAll(partnerConnectIpArrayList);
	}

	public Set<PartnerService> getPartnerServices() {
		return partnerServices;
	}

	public void setPartnerServices(Set<PartnerService> partnerServices) {
		this.partnerServices = partnerServices;
		this.partnerServicesArrayList.addAll(partnerServices);
	}

	public ArrayList<PartnerService> getPartnerServicesArrayList() {
		return partnerServicesArrayList;
	}

	public void setPartnerServicesArrayList(ArrayList<PartnerService> partnerServicesArrayList) {
		this.partnerServicesArrayList = partnerServicesArrayList;
		this.partnerServices.addAll(partnerServicesArrayList);
	}

	public List<ServiceInfo> getServiceInfos() {
		return serviceInfos;
	}

	public void setServiceInfos(List<ServiceInfo> serviceInfos) {
		this.serviceInfos = serviceInfos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "companyId")
	public Set<SysUsers> getSysUsers() {
		return sysUsers;
	}

	public void setSysUsers(Set<SysUsers> sysUsers) {
		this.sysUsers = sysUsers;
		this.sysUsersArrayList = new ArrayList<SysUsers>(sysUsers);
	}

	public ArrayList<SysUsers> getSysUsersArrayList() {
		return sysUsersArrayList;
	}

	public void setSysUsersArrayList(ArrayList<SysUsers> sysUsersArrayList) {
		this.sysUsersArrayList = sysUsersArrayList;
		this.sysUsers.addAll(sysUsersArrayList);
	}

	public Date getValidityContractFrom() {
		this.validityContractFromStr = Formater.date2str(this.validityContractFrom);
		return validityContractFrom;
	}

	public void setValidityContractFrom(Date validityContractFrom) {
		this.validityContractFrom = validityContractFrom;
	}

	public Date getValidityContractTo() {
		this.validityContractToStr = Formater.date2str(this.validityContractTo);
		return validityContractTo;
	}

	public void setValidityContractTo(Date validityContractTo) {
		this.validityContractTo = validityContractTo;
	}

	public String getValidityContractFromStr() {
		return validityContractFromStr;
	}

	public void setValidityContractFromStr(String validityContractFromStr) throws Exception {
		this.validityContractFromStr = validityContractFromStr;
		this.validityContractFrom = Formater.str2date(this.validityContractFromStr);
	}

	public String getValidityContractToStr() {
		return validityContractToStr;
	}

	public void setValidityContractToStr(String validityContractToStr) throws Exception {
		this.validityContractToStr = validityContractToStr;
		this.validityContractTo = Formater.str2date(this.validityContractToStr);
	}

	public boolean isSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public BigDecimal getMaxUser() {
		return maxUser;
	}

	public void setMaxUser(BigDecimal maxUser) {
		this.maxUser = maxUser;
	}
	
}
