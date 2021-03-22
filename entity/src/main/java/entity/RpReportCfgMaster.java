package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "RP_REPORT_CFG_MASTER")
public class RpReportCfgMaster implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID")
    private String id;
    @Column(name = "PROPERTY_CODE")
    private String propertyCode;
    @Column(name = "PROPERTY_DES")
    private String propertyDes;
    @Column(name = "DATA_TYPE")
    private String dataType;
    @Column(name = "POSITION")
    private Short position;
    @Column(name = "CREATED_USER")
    private String createdUser;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "UPDATED_USER")
    private String updatedUser;
    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Column(name = "PROPERTY_LENGTH")
    private Integer propertyLength;
    @Column(name = "EXCEL_POSITION")
    private Short positionExcel;
	/*
	 * @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportCfgId") private
	 * Collection<RpReportCfgDetail> rpReportCfgDetailCollection;
	 */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportCfgId")
	private Set<RpReportCfgDetail> reportCfgDetails = new HashSet<RpReportCfgDetail>(0);
    @Transient
    @JsonIgnore
	private ArrayList<RpReportCfgDetail> reportCfgDetailArrayList;
	/*
	 * @JoinColumn(name = "REPORT_CODE", referencedColumnName = "CODE")
	 * 
	 * @ManyToOne private RpTypeMapping reportCode;
	 */
    @Column(name = "REPORT_CODE")
    private String reportCode;

    public RpReportCfgMaster() {
    }

    public RpReportCfgMaster(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPropertyCode() {
        return propertyCode;
    }

    public void setPropertyCode(String propertyCode) {
        this.propertyCode = propertyCode;
    }

    public String getPropertyDes() {
        return propertyDes;
    }

    public void setPropertyDes(String propertyDes) {
        this.propertyDes = propertyDes;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Short getPosition() {
        return position;
    }

    public void setPosition(Short position) {
        this.position = position;
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

    public Integer getPropertyLength() {
        return propertyLength;
    }

    public void setPropertyLength(Integer propertyLength) {
        this.propertyLength = propertyLength;
    }

	/*
	 * public RpTypeMapping getReportCode() { return reportCode; }
	 * 
	 * public void setReportCode(RpTypeMapping reportCode) { this.reportCode =
	 * reportCode; }
	 */
    

	public Set<RpReportCfgDetail> getReportCfgDetails() {
		return reportCfgDetails;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public void setReportCfgDetails(Set<RpReportCfgDetail> reportCfgDetails) {
		this.reportCfgDetails = reportCfgDetails;
		this.reportCfgDetailArrayList = new ArrayList<RpReportCfgDetail>(reportCfgDetails);
	}

	public ArrayList<RpReportCfgDetail> getReportCfgDetailArrayList() {
		return reportCfgDetailArrayList;
	}

	public void setReportCfgDetailArrayList(ArrayList<RpReportCfgDetail> reportCfgDetailArrayList) {
		this.reportCfgDetailArrayList = reportCfgDetailArrayList;
		this.reportCfgDetails.addAll(reportCfgDetailArrayList);
	}

	public Short getPositionExcel() {
		return positionExcel;
	}

	public void setPositionExcel(Short positionExcel) {
		this.positionExcel = positionExcel;
	}
    
}	
