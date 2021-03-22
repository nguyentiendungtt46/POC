package entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RP_TYPE")
public class RpTypeMapping implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "CODE")
    private String code;
    @Column(name = "FILE_TYPE")
    private String fileType;
    @Column(name = "FILE_TYPE_DESC")
    private String fileTypeDesc;
    @Column(name = "CUS_TYPE")
    private BigDecimal cusType;
    @Column(name = "REPORT_TYPE")
    private BigDecimal reportType;
    @Column(name = "DATA_TYPE_DESC")
    private String dataTypeDesc;
    @Column(name = "CUS_TYPE_DESC")
    private String cusTypeDesc;
    @Column(name = "REPORT_TYPE_DESC")
    private String reportTypeDesc;
    @Column(name = "INACTIVE")
    private Short inactive;
    @Column(name = "TEMPLATE_CODE")
    private String templateCode;
    @Column(name = "DATA_TYPE")
    private String dataType;
	/*
	 * @OneToMany(mappedBy = "reportCode") private Collection<RpReportCfgMaster>
	 * rpReportCfgMasterCollection;
	 */

    public RpTypeMapping() {
    }

    public RpTypeMapping(String code) {
        this.code = code;
    }

    public RpTypeMapping(String code, String fileType, String fileTypeDesc, BigDecimal cusType, BigDecimal reportType) {
        this.code = code;
        this.fileType = fileType;
        this.fileTypeDesc = fileTypeDesc;
        this.cusType = cusType;
        this.reportType = reportType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public BigDecimal getCusType() {
        return cusType;
    }

    public void setCusType(BigDecimal cusType) {
        this.cusType = cusType;
    }

    public BigDecimal getReportType() {
        return reportType;
    }

    public void setReportType(BigDecimal reportType) {
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

    public Short getInactive() {
        return inactive;
    }

    public void setInactive(Short inactive) {
        this.inactive = inactive;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

}
