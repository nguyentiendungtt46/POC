package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.util.ResourceException;

@Entity
@Table(name = "RP_SUM")
public class RpSum implements Serializable, Cloneable {
	private static final Logger logger = LogManager.getLogger(RpSum.class);
	private static final long serialVersionUID = 1L;
	// Khong de id tu gen de clone giu nguyen id
	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "USER_REPORT")
	private String userReport;
	@Column(name = "REPORT_CODE")
	private String reportCode;
	@Column(name = "FILE_NAME")
	private String fileName;
	@Column(name = "FILE_FORMAT")
	private String fileFormat;
	@Column(name = "FILE_TYPE")
	private String fileType;
	@Column(name = "TCTD_CODE")
	private String tctdCode;

	@Column(name = "REPORT_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date reportDate;

	@Column(name = "REPORTER_NAME")
	private String reporterName;
	@Column(name = "REPORTER_PHONE")
	private String reporterPhone;
	@Column(name = "EQUITY_CAPITAL")
	private Long equityCapital;
	@Column(name = "STATUS")
	private Short status;
	@Column(name = "REDO")
	private Short redo;
	@Column(name = "ERROR_CODE")
	private String errorCode;
	@Column(name = "ERROR_DES")
	private String errorDes;
	@Column(name = "ERROR_PER")
	private BigDecimal errorPer;
	@Column(name = "ATTACHMENT_NAME")
	private String attachmentName;
	@Column(name = "ATTACHMENT_PATH")
	private String attachmentPath;
	@Column(name = "NUM_OF_RECORD")
	private Long numOfRecord;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROCESS_START_TIME")
	private Date processStartTime;
	@Column(name = "TUDONG")
	private Boolean tuDong;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROCESS_END_TIME")
	private Date processEndTime;
	@Column(name = "BAL")
	private Long bal;
	@Column(name = "IVST")
	private Long ivst;
	@Column(name = "AST_")
	private Long ast;
	@Column(name = "PMT_AMT")
	private Long pmtAmt;
	@Column(name = "BOND_AMT")
	private Long bondAmt;
	@Column(name = "ENABLE_RE_EXEC", precision = 1, scale = 0)
	private Boolean enableReExec;
	@Transient
	private String strReportDate;
	@Transient
	private String strBal;
	@Transient
	private String strInst;
	@Transient
	private String strAst;
	@Transient
	private String strPmtAmt;
	@Transient
	private String strBondAmt;

	@Transient
	private byte[] fileContent;
	@Transient
	private byte[] fileAttContent;
	
	@Transient
	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	@Transient
	public byte[] getFileAttContent() {
		return fileAttContent;
	}

	public void setFileAttContent(byte[] fileAttContent) {
		this.fileAttContent = fileAttContent;
	}
	
	public Long getNumOfRecord() {
		return numOfRecord;
	}

	public void setNumOfRecord(Long numOfRecord) {
		this.numOfRecord = numOfRecord;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public BigDecimal getErrorPer() {
		return errorPer;
	}

	public void setErrorPer(BigDecimal errorPer) {
		this.errorPer = errorPer;
	}

	public String getErrorDes() {
		return errorDes;
	}

	public void setErrorDes(String errorDes) {
		this.errorDes = errorDes;
	}

	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	@Column(name = "WAIT_START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date waitStartTime;

	@Column(name = "WAIT_RESOURCE")
	private Boolean waitResource;

	@Column(name = "CREATED_BY")
	private String createBy;

	@Column(name = "MODIFIED_DATE")
	@Temporal(TemporalType.TIMESTAMP)

	private Date modifiedDate;
	@Column(name = "MODIFIED_BY")

	private String modifiedBy;
	@Column(name = "FILE_PATH")
	private String filePath;
	@Column(name = "RETRY")
	private Short retry;

	@Column(name = "FILE_SIZE_ATT")
	private BigDecimal fileSizeAtt;

	@Column(name = "FILE_SIZE")
	private BigDecimal fileSize;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rpSum")
	private Set<RpValidateDetail> rpValidateDetails = new HashSet<RpValidateDetail>(0);
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rpSum")
	private Set<RpSumError> rpSumErrors = new HashSet<RpSumError>(0);

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rpSum")
	private Set<RpDetailK113> rpDetailK113s = new HashSet<RpDetailK113>(0);
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rpSum")
	private Set<RpDetailK31xx> rpDetailK31xxes = new HashSet<RpDetailK31xx>(0);
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rpSum")
	private Set<RpDetailT02ds> rpDetailT02dses = new HashSet<RpDetailT02ds>(0);
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rpSum")
	private Set<RpDetailK102> rpDetailK102s = new HashSet<RpDetailK102>(0);
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rpSum")
	private Set<RpDetailT02g2> rpDetailT02g2s = new HashSet<RpDetailT02g2>(0);
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rpSum")
	private Set<RpDetailT02g1> rpDetailT02g1s = new HashSet<RpDetailT02g1>(0);
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rpSum")
	private Set<RpDetailK5> rpDetailK5s = new HashSet<RpDetailK5>(0);
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rpSum")
	private Set<RpDetailK3213> rpDetailK3213s = new HashSet<RpDetailK3213>(0);
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rpSum")
	private Set<RpDetailK2K2b> rpDetailK2K2bs = new HashSet<RpDetailK2K2b>(0);
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rpSum")
	private Set<RpSumBranch> rpSumBranchs = new HashSet<RpSumBranch>(0);

	@Column(name = "REVERSE_REPORT_ID")
	private String reverseReportId;

	@Column(name = "ZIP_REPORT")
	private Boolean zipReport;

	@Column(name = "ZIP_FILE_NAME")
	private String zipFileName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SEND_TO_M1_TIME")
	private Date sendToM1Time;

	@Column(name = "BRANCH")
	private String branch;
	
	@Transient
	private String createDateStr;

	@Transient
	private String reportDateStr;

	@Transient
	private String statusStr;
	@Transient
	private String fileSizeStr;
	
	public String getReverseReportId() {
		return reverseReportId;
	}

	public void setReverseReportId(String reverseReportId) {
		this.reverseReportId = reverseReportId;
	}

	public RpSum() {
		super();
	}

	public RpSum(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserReport() {
		return userReport;
	}

	public void setUserReport(String userReport) {
		this.userReport = userReport;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getTctdCode() {
		return tctdCode;
	}

	public void setTctdCode(String tctdCode) {
		this.tctdCode = tctdCode;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	public String getReporterPhone() {
		return reporterPhone;
	}

	public void setReporterPhone(String reporterPhone) {
		this.reporterPhone = reporterPhone;
	}

	public Long getEquityCapital() {
		return equityCapital;
	}

	public void setEquityCapital(Long equityCapital) {
		this.equityCapital = equityCapital;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Short getRedo() {
		return redo;
	}

	public void setRedo(Short redo) {
		this.redo = redo;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	@Transient
	private String repoStr;
	@Transient
	private String tuDongStr;

	public String getRepoStr() {
		return repoStr;
	}

	public void setRepoStr(String repoStr) {
		this.repoStr = repoStr;
	}

	public String getTuDongStr() {
		return tuDongStr;
	}

	public void setTuDongStr(String tuDongStr) {
		this.tuDongStr = tuDongStr;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Short getRetry() {
		return retry;
	}

	public void setRetry(Short retry) {
		this.retry = retry;
	}

	public Set<RpValidateDetail> getRpValidateDetails() {
		return this.rpValidateDetails;
	}

	public void setRpValidateDetails(Set<RpValidateDetail> rpValidateDetails) {
		this.rpValidateDetails = rpValidateDetails;
	}

	public Set<RpSumError> getRpSumErrors() {
		return this.rpSumErrors;
	}

	public void setRpSumErrors(Set<RpSumError> rpSumErrors) {
		this.rpSumErrors = rpSumErrors;
	}

	public Set<RpDetailK113> getRpDetailK113s() {
		return this.rpDetailK113s;
	}

	public void setRpDetailK113s(Set<RpDetailK113> rpDetailK113s) {
		this.rpDetailK113s = rpDetailK113s;
	}

	public Set<RpDetailK31xx> getRpDetailK31xxes() {
		return this.rpDetailK31xxes;
	}

	public void setRpDetailK31xxes(Set<RpDetailK31xx> rpDetailK31xxes) {
		this.rpDetailK31xxes = rpDetailK31xxes;
	}

	public Set<RpDetailT02ds> getRpDetailT02dses() {
		return this.rpDetailT02dses;
	}

	public void setRpDetailT02dses(Set<RpDetailT02ds> rpDetailT02dses) {
		this.rpDetailT02dses = rpDetailT02dses;
	}

	public Set<RpDetailK102> getRpDetailK102s() {
		return this.rpDetailK102s;
	}

	public void setRpDetailK102s(Set<RpDetailK102> rpDetailK102s) {
		this.rpDetailK102s = rpDetailK102s;
	}

	public Set<RpDetailT02g2> getRpDetailT02g2s() {
		return this.rpDetailT02g2s;
	}

	public void setRpDetailT02g2s(Set<RpDetailT02g2> rpDetailT02g2s) {
		this.rpDetailT02g2s = rpDetailT02g2s;
	}

	public Set<RpDetailT02g1> getRpDetailT02g1s() {
		return this.rpDetailT02g1s;
	}

	public void setRpDetailT02g1s(Set<RpDetailT02g1> rpDetailT02g1s) {
		this.rpDetailT02g1s = rpDetailT02g1s;
	}

	public Set<RpDetailK5> getRpDetailK5s() {
		return this.rpDetailK5s;
	}

	public void setRpDetailK5s(Set<RpDetailK5> rpDetailK5s) {
		this.rpDetailK5s = rpDetailK5s;
	}

	public Set<RpDetailK3213> getRpDetailK3213s() {
		return this.rpDetailK3213s;
	}

	public void setRpDetailK3213s(Set<RpDetailK3213> rpDetailK3213s) {
		this.rpDetailK3213s = rpDetailK3213s;
	}

	public Set<RpDetailK2K2b> getRpDetailK2K2bs() {
		return this.rpDetailK2K2bs;
	}

	public void setRpDetailK2K2bs(Set<RpDetailK2K2b> rpDetailK2K2bs) {
		this.rpDetailK2K2bs = rpDetailK2K2bs;
	}

	public Set<RpSumBranch> getRpSumBranchs() {
		return this.rpSumBranchs;
	}

	public void setRpSumBranchs(Set<RpSumBranch> rpSumBranchs) {
		this.rpSumBranchs = rpSumBranchs;
	}

	@Override
	public RpSum clone() throws CloneNotSupportedException {
		RpSum clone = (RpSum) super.clone();
		clone.errorCode = null;
		clone.errorDes = null;
		clone.status = Short.valueOf("0");
		clone.retry = Short.valueOf("0");
		clone.errorPer = null;
		clone.setRedo(null);
		clone.setReverseReportId(null);
		clone.setWaitResource(null);
		clone.setWaitStartTime(null);
		clone.setEnableReExec(null);
		clone.setProcessEndTime(null);
		clone.setProcessStartTime(null);
		for (java.lang.reflect.Field f : this.getClass().getDeclaredFields()) {
			if (f.getAnnotationsByType(OneToMany.class).length > 0)
				try {
					f.set(clone, null);
				} catch (Exception e) {
					logger.error(e);
				}
		}
		return clone;

	}

	public Long getBal() {
		return bal;
	}

	public void setBal(Long bal) {
		this.bal = bal;
	}

	public Long getIvst() {
		return ivst;
	}

	public void setIvst(Long ivst) {
		this.ivst = ivst;
	}

	public Long getAst() {
		return ast;
	}

	public void setAst(Long ast) {
		this.ast = ast;
	}

	public Long getPmtAmt() {
		return pmtAmt;
	}

	public void setPmtAmt(Long pmtAmt) {
		this.pmtAmt = pmtAmt;
	}

	public Long getBondAmt() {
		return bondAmt;
	}

	public void setBondAmt(Long bondAmt) {
		this.bondAmt = bondAmt;
	}

	public String getStrReportDate() {
		return strReportDate;
	}

	public void setStrReportDate(String strReportDate) {
		this.strReportDate = strReportDate;
	}

	public String getStrBal() {
		return strBal;
	}

	public void setStrBal(String strBal) {
		this.strBal = strBal;
	}

	public String getStrInst() {
		return strInst;
	}

	public void setStrInst(String strInst) {
		this.strInst = strInst;
	}

	public String getStrAst() {
		return strAst;
	}

	public void setStrAst(String strAst) {
		this.strAst = strAst;
	}

	public String getStrPmtAmt() {
		return strPmtAmt;
	}

	public void setStrPmtAmt(String strPmtAmt) {
		this.strPmtAmt = strPmtAmt;
	}

	public String getStrBondAmt() {
		return strBondAmt;
	}

	public void setStrBondAmt(String strBondAmt) {
		this.strBondAmt = strBondAmt;
	}

	public Date getWaitStartTime() {
		return waitStartTime;
	}

	public void setWaitStartTime(Date waitStartTime) {
		this.waitStartTime = waitStartTime;
	}

	public Boolean getWaitResource() {
		return waitResource;
	}

	public void setWaitResource(Boolean waitResource) {
		this.waitResource = waitResource;
	}

	public BigDecimal getFileSizeAtt() {
		return fileSizeAtt;
	}

	public void setFileSizeAtt(BigDecimal fileSizeAtt) {
		this.fileSizeAtt = fileSizeAtt;
	}

	public BigDecimal getFileSize() {
		return fileSize;
	}

	public void setFileSize(BigDecimal fileSize) {
		this.fileSize = fileSize;
	}

	public Boolean getEnableReExec() {
		return enableReExec;
	}

	public void setEnableReExec(Boolean enableReExec) {
		this.enableReExec = enableReExec;
	}

	public Date getProcessStartTime() {
		return processStartTime;
	}

	public void setProcessStartTime(Date processStartTime) {
		this.processStartTime = processStartTime;
	}

	public Date getProcessEndTime() {
		return processEndTime;
	}

	public void setProcessEndTime(Date processEndTime) {
		this.processEndTime = processEndTime;
	}

	public Boolean getZipReport() {
		return zipReport;
	}

	public void setZipReport(Boolean zipReport) {
		this.zipReport = zipReport;
	}

	public String getZipFileName() {
		return zipFileName;
	}

	public void setZipFileName(String zipFileName) {
		this.zipFileName = zipFileName;
	}

	public Date getSendToM1Time() {
		return sendToM1Time;
	}

	public void setSendToM1Time(Date sendToM1Time) {
		this.sendToM1Time = sendToM1Time;
	}

	private static final String REGEX_ZIP_FILE_NAME = "\\b((\\d{3})|(\\d{8}))((?:(?:(?:0[1-9]|1\\d|2[0-8])(?:0[1-9]|1[0-2])|(?:29|30)(?:0[13-9]|1[0-2])|31(?:0[13578]|1[02]))[1-9]\\d{3}|2902(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)))(\\.)((?!000)\\d{3})(.zip)\\b";
	private static final String REGEX_FILE_NAME_03_2013_TT_NHNN = "\\b(K|P|D)(10|11|20|31|32|33|40|50|2B0)([1-3])([1-3]A?)((\\d{3})|(\\d{8}))((?:(?:(?:0[1-9]|1\\d|2[0-8])(?:0[1-9]|1[0-2])|(?:29|30)(?:0[13-9]|1[0-2])|31(?:0[13578]|1[02]))[1-9]\\d{3}|2902(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)))(\\.)((?!000)\\d{3})(\\.xls(x?))?\\b";
	private static final String REGEX_FILE_NAME_02_2013_TT_NHNN = "\\b(T02)(DS|G1|G2)((\\d{3})|(\\d{8}))((?:(?:(?:0[1-9]|1\\d|2[0-8])(?:0[1-9]|1[0-2])|(?:29|30)(?:0[13-9]|1[0-2])|31(?:0[13578]|1[02]))[1-9]\\d{3}|2902(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)))(\\.)((?!000)\\d{3})(\\.xls(x?))?\\b";

	public Boolean validate() throws ResourceException {
		// File zip
		Pattern p = Pattern.compile(REGEX_ZIP_FILE_NAME);
		Matcher m = p.matcher(fileName);
		if (m.matches()) {
			this.setTctdCode(m.group(1));
			this.setZipReport(Boolean.TRUE);
			try {
				this.setReportDate(new SimpleDateFormat("ddMMyyyy").parse(m.group(4)));
			} catch (ParseException e) {
				throw new ResourceException("RPF_001");
			}
			if (this.reportDate.compareTo(Calendar.getInstance().getTime()) >= 0)
				throw new ResourceException("RPF_004");

			return null;

		}

		// Ten file theo thong tu 03
		p = Pattern.compile(REGEX_FILE_NAME_03_2013_TT_NHNN);
		m = p.matcher(fileName);
		if (m.matches()) {
			this.tctdCode = m.group(5);
			try {
				this.reportDate = new SimpleDateFormat("ddMMyyyy").parse(m.group(8));
			} catch (ParseException e) {
				throw new ResourceException("RPF_001");
			}

		} else {
			// Ten file theo thong tu 02
			p = Pattern.compile(REGEX_FILE_NAME_02_2013_TT_NHNN);
			m = p.matcher(fileName);
			if (m.matches()) {
				this.tctdCode = m.group(3);
				try {
					this.reportDate = new SimpleDateFormat("ddMMyyyy").parse(m.group(6));
				} catch (ParseException e) {
					throw new ResourceException("RPF_001");
				}
			} else
				throw new ResourceException("RPF_001");
		}

		if (this.reportDate.compareTo(Calendar.getInstance().getTime()) >= 0)
			throw new ResourceException("RPF_004");

		return Boolean.TRUE;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public String getReportDateStr() {
		return reportDateStr;
	}

	public void setReportDateStr(String reportDateStr) {
		this.reportDateStr = reportDateStr;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public Boolean getTuDong() {
		return tuDong;
	}

	public void setTuDong(Boolean tuDong) {
		this.tuDong = tuDong;
	}

	public String getFileSizeStr() {
		return fileSizeStr;
	}

	public void setFileSizeStr(String fileSizeStr) {
		this.fileSizeStr = fileSizeStr;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}
	
	
}
