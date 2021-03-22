package vn.com.cmc.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the SYS_USERS database table.
 * 
 */
@Entity
@Table(name="SYS_USERS")
@NamedQuery(name="SysUsers.findAll", query="SELECT s FROM SysUsers s")
public class SysUsers implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	private Long active;

	private Long assignrsa;

	@Temporal(TemporalType.DATE)
	@Column(name="AUDIT_DATE")
	private Date auditDate;

	@Column(name="CELL_PHONE")
	private String cellPhone;

	@Column(name="COMPANY_NAME")
	private String companyName;

	@Column(name="COMPANY_NAME_EN")
	private String companyNameEn;

	@Column(name="CQ_BHXH")
	private Long cqBhxh;

	private Long createpin;

	private Long creatersa;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_ISSUE")
	private Date dateIssue;

	private String dayltd;

	@Column(name="DEPARTMENT_NAME")
	private String departmentName;

	@Column(name="DEPARTMENT_NAME_EN")
	private String departmentNameEn;

	private String email;

	@Temporal(TemporalType.DATE)
	@Column(name="END_DATE")
	private Date endDate;

	private String frdayltd;

	private String frtransltd;

	private String gender;

	private String identification;

	@Column(name="IDENTITY_PAPER")
	private String identityPaper;

	@Column(name="JOB_ID")
	private String jobId;

	@Column(name="JOB_NAME")
	private String jobName;

	@Column(name="JOB_NAME_EN")
	private String jobNameEn;

	private String name;

	@Column(name="NAME_EN")
	private String nameEn;

	private String nationality;

	private String password;

	@Column(name="PATH_CREATE")
	private String pathCreate;

	@Column(name="PATH_OWNER")
	private String pathOwner;

	private String phone;

	private String pin;

	@Column(name="PLACE_ISSUE")
	private String placeIssue;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PWD_DATE")
	private Date pwdDate;

	@Column(name="SEC_DIVICE_CODE")
	private String secDiviceCode;

	@Temporal(TemporalType.DATE)
	@Column(name="START_DATE")
	private Date startDate;

	private String transltd;

	private String username;

	private Long usersa;

	//bi-directional many-to-one association to Partner
	@ManyToOne
	@JoinColumn(name="DEPARTMENT_ID")
	private Partner partner1;

	//bi-directional many-to-one association to Partner
	@ManyToOne
	@JoinColumn(name="COMPANY_ID")
	private Partner partnerCompany;
	
	@Column(name="PASSWORD_STATUS")
	private Long passwordStatus;
	
	@Column(name="LOGIN_ERROR_TIMES")
	private Long loginErrorTimes;
	
	@Column(name="ENABLE_SERVICE")
	private Long enableService;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BLOCK_TIME")
	private Date blockTime;
	
	@Transient
	private String accessToken;
	
	@Transient
	private String refreshToken;

	public SysUsers() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getActive() {
		return this.active;
	}

	public void setActive(Long active) {
		this.active = active;
	}

	public Long getAssignrsa() {
		return this.assignrsa;
	}

	public void setAssignrsa(Long assignrsa) {
		this.assignrsa = assignrsa;
	}

	public Date getAuditDate() {
		return this.auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getCellPhone() {
		return this.cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyNameEn() {
		return this.companyNameEn;
	}

	public void setCompanyNameEn(String companyNameEn) {
		this.companyNameEn = companyNameEn;
	}

	public Long getCqBhxh() {
		return this.cqBhxh;
	}

	public void setCqBhxh(Long cqBhxh) {
		this.cqBhxh = cqBhxh;
	}

	public Long getCreatepin() {
		return this.createpin;
	}

	public void setCreatepin(Long createpin) {
		this.createpin = createpin;
	}

	public Long getCreatersa() {
		return this.creatersa;
	}

	public void setCreatersa(Long creatersa) {
		this.creatersa = creatersa;
	}

	public Date getDateIssue() {
		return this.dateIssue;
	}

	public void setDateIssue(Date dateIssue) {
		this.dateIssue = dateIssue;
	}

	public String getDayltd() {
		return this.dayltd;
	}

	public void setDayltd(String dayltd) {
		this.dayltd = dayltd;
	}

	public String getDepartmentName() {
		return this.departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentNameEn() {
		return this.departmentNameEn;
	}

	public void setDepartmentNameEn(String departmentNameEn) {
		this.departmentNameEn = departmentNameEn;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getFrdayltd() {
		return this.frdayltd;
	}

	public void setFrdayltd(String frdayltd) {
		this.frdayltd = frdayltd;
	}

	public String getFrtransltd() {
		return this.frtransltd;
	}

	public void setFrtransltd(String frtransltd) {
		this.frtransltd = frtransltd;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIdentification() {
		return this.identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getIdentityPaper() {
		return this.identityPaper;
	}

	public void setIdentityPaper(String identityPaper) {
		this.identityPaper = identityPaper;
	}

	public String getJobId() {
		return this.jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return this.jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobNameEn() {
		return this.jobNameEn;
	}

	public void setJobNameEn(String jobNameEn) {
		this.jobNameEn = jobNameEn;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNationality() {
		return this.nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPathCreate() {
		return this.pathCreate;
	}

	public void setPathCreate(String pathCreate) {
		this.pathCreate = pathCreate;
	}

	public String getPathOwner() {
		return this.pathOwner;
	}

	public void setPathOwner(String pathOwner) {
		this.pathOwner = pathOwner;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPin() {
		return this.pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getPlaceIssue() {
		return this.placeIssue;
	}

	public void setPlaceIssue(String placeIssue) {
		this.placeIssue = placeIssue;
	}

	public Date getPwdDate() {
		return this.pwdDate;
	}

	public void setPwdDate(Date pwdDate) {
		this.pwdDate = pwdDate;
	}

	public String getSecDiviceCode() {
		return this.secDiviceCode;
	}

	public void setSecDiviceCode(String secDiviceCode) {
		this.secDiviceCode = secDiviceCode;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getTransltd() {
		return this.transltd;
	}

	public void setTransltd(String transltd) {
		this.transltd = transltd;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getUsersa() {
		return this.usersa;
	}

	public void setUsersa(Long usersa) {
		this.usersa = usersa;
	}

	public Partner getPartner1() {
		return this.partner1;
	}

	public void setPartner1(Partner partner1) {
		this.partner1 = partner1;
	}

	public Partner getPartnerCompany() {
		return this.partnerCompany;
	}

	public void setPartnerCompany(Partner partnerCompany) {
		this.partnerCompany = partnerCompany;
	}

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

	public Long getPasswordStatus() {
		return passwordStatus;
	}

	public void setPasswordStatus(Long passwordStatus) {
		this.passwordStatus = passwordStatus;
	}

	public Long getLoginErrorTimes() {
		return loginErrorTimes;
	}

	public void setLoginErrorTimes(Long loginErrorTimes) {
		this.loginErrorTimes = loginErrorTimes;
	}

	public Long getEnableService() {
		return enableService;
	}

	public void setEnableService(Long enableService) {
		this.enableService = enableService;
	}

	public Date getBlockTime() {
		return blockTime;
	}

	public void setBlockTime(Date blockTime) {
		this.blockTime = blockTime;
	}
	
}