package entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Table(name = "RP_FILE_UNSTRUCTURED")
public class RpFileUnstructured implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name = "ID")
	private String id;
	@Column(name = "FILE_NAME")
	private String fileName;
	@Column(name = "BRANCH")
	private String branch;
	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "LAST_DOWNLOAD_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastDownloadTime;
	@Column(name = "FILE_SIZE")
	private BigDecimal fileSize;
	@Column(name = "TYPE")
	private BigDecimal type;

	@Transient
	private String createDateStr;

	@Transient
	private String lastDownloadTimeStr;

	@Transient
	private String partnerName;
	
	@Transient
	private String fileSizeStr;

	public RpFileUnstructured() {
	}

	public RpFileUnstructured(String id) {
		this.id = id;
	}

	public RpFileUnstructured(String id, String fileName, String branch, Date createdDate, String createdBy) {
		this.id = id;
		this.fileName = fileName;
		this.branch = branch;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
	}

	public RpFileUnstructured(String fileName, String branch, Date createdDate, String createdBy, BigDecimal fileSize,
			BigDecimal type) {
		super();
		this.fileName = fileName;
		this.branch = branch;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.fileSize = fileSize;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastDownloadTime() {
		return lastDownloadTime;
	}

	public void setLastDownloadTime(Date lastDownloadTime) {
		this.lastDownloadTime = lastDownloadTime;
	}

	public BigDecimal getFileSize() {
		return fileSize;
	}

	public void setFileSize(BigDecimal fileSize) {
		this.fileSize = fileSize;
	}

	public BigDecimal getType() {
		return type;
	}

	public void setType(BigDecimal type) {
		this.type = type;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public String getLastDownloadTimeStr() {
		return lastDownloadTimeStr;
	}

	public void setLastDownloadTimeStr(String lastDownloadTimeStr) {
		this.lastDownloadTimeStr = lastDownloadTimeStr;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getFileSizeStr() {
		return fileSizeStr;
	}

	public void setFileSizeStr(String fileSizeStr) {
		this.fileSizeStr = fileSizeStr;
	}
	
}
