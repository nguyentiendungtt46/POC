package entity;

import java.io.Serializable;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RP_TYPE")
public class RpType implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "CODE")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static final String ID = "id";
	@Column(name = "FILE_TYPE")
	private String fileType;
	public static final String FILE_TYPE = "fileType";
	@Column(name = "FILE_TYPE_DESC")
	private String fileTypeDesc;
	public static final String FILE_TYPE_DESC = "fileTypeDesc";
	@Column(name = "DATA_TYPE")
	private String dataType;
	public static final String DATA_TYPE = "dataType";
	@Column(name = "CUS_TYPE")
	private short cusType;
	public static final String CUS_TYPE = "cusType";
	@Column(name = "REPORT_TYPE")
	private short reportType;
	public static final String REPORT_TYPE = "reportType";
	@Column(name = "DATA_TYPE_DESC")
	private String dataTypeDesc;
	public static final String DATA_TYPE_DESC = "dataTypeDesc";
	@Column(name = "CUS_TYPE_DESC")
	private String cusTypeDesc;
	public static final String CUS_TYPE_DESC = "cusTypeDesc";
	@Column(name = "REPORT_TYPE_DESC")
	private String reportTypeDesc;
	public static final String REPORT_TYPE_DESC = "reportTypeDesc";
	@Column(name = "TABLE_NAME")
	private String tableName;

	@Transient
	private String status;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "TEMPLATE_CODE")
	private String templateCode;

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public static final String TEMPLATE_CODE = "templateCode";

	@Column(name = "INACTIVE")
	private Boolean inActive;

	public Boolean getInActive() {
		return inActive;
	}

	public void setInActive(Boolean inActive) {
		this.inActive = inActive;
	}

	public static final String INACTIVE = "inActive";

	public RpType() {
	}

	public RpType(String code) {
		this.id = code;
	}

	public RpType(String code, String fileType, String fileTypeDesc, String dataType, short cusType, short reportType) {
		this.id = code;
		this.fileType = fileType;
		this.fileTypeDesc = fileTypeDesc;
		this.dataType = dataType;
		this.cusType = cusType;
		this.reportType = reportType;
	}

	public RpType(Entry<String, String> fileType, Entry<String, String> dataType, Entry<Short, String> cusType,
			Entry<Short, String> reportType) {
		this.id = fileType.getKey() + dataType.getKey() + cusType.getKey() + reportType.getKey();
		this.templateCode = fileType.getKey() + dataType.getKey() + reportType.getKey();
		this.fileTypeDesc = fileType.getValue();
		this.dataTypeDesc = dataType.getValue();
		this.cusTypeDesc = cusType.getValue();
		this.reportTypeDesc = reportType.getValue();
		this.fileType = fileType.getKey();
		this.dataType = dataType.getKey();
		this.cusType = cusType.getKey();
		this.reportType = reportType.getKey();

	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileTypeDesc() {
		return fileTypeDesc;
	}

	public void setFileTypeDesc(String fileTypeDesc) {
		this.fileTypeDesc = fileTypeDesc;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public short getCusType() {
		return cusType;
	}

	public void setCusType(short cusType) {
		this.cusType = cusType;
	}

	public short getReportType() {
		return reportType;
	}

	public void setReportType(short reportType) {
		this.reportType = reportType;
	}

	public String getDataTypeDesc() {
		return dataTypeDesc;
	}

	public void setDataTypeDesc(String dataTypeDesc) {
		this.dataTypeDesc = dataTypeDesc;
	}

	public String getCusTypeDesc() {
		return cusTypeDesc;
	}

	public void setCusTypeDesc(String cusTypeDesc) {
		this.cusTypeDesc = cusTypeDesc;
	}

	public String getReportTypeDesc() {
		return reportTypeDesc;
	}

	public void setReportTypeDesc(String reportTypeDesc) {
		this.reportTypeDesc = reportTypeDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
