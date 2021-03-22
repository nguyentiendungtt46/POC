package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "APP_FLOW")
public class AppFlow {	
    private String id;
	private Short status;
	private Short subStatus;
	private String description;
	private String name;
	private String action;
	private String code;
	private Boolean first;
	private Boolean last;
	private Boolean owner;
	private Boolean right;
	private WrkFlwMng wrkFlwMng;
	@Column(name = "STATUS")
	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}
	@Column(name = "SUB_STATUS")
	public Short getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(Short subStatus) {
		this.subStatus = subStatus;
	}
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "ACTION")
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	@Transient
	public String getCode() {
		String statusCode = String.valueOf(this.status);
		if(this.subStatus!=null)
			statusCode += "." + String.valueOf(this.subStatus);
		this.code = statusCode;
		return this.code;
	}
	@Transient
	public Boolean getFirst() {
		return first;
	}

	public void setFirst(Boolean first) {
		this.first = first;
	}
	@Transient
	public Boolean getLast() {
		return last;
	}

	public void setLast(Boolean last) {
		this.last = last;
	}

	public boolean equalStatus(AppFlow appFlow1) {
		if (!appFlow1.getStatus().equals(this.getStatus()))
			return false;
		if (this.getSubStatus() == null) {
			if (appFlow1.getSubStatus() != null && appFlow1.getSubStatus() != 0)
				return false;
			return true;
		}
		return this.getSubStatus().equals(appFlow1.getSubStatus());
	}

	@Transient
	public Boolean getOwner() {
		return owner;
	}
	
	public void setOwner(Boolean owner) {
		this.owner = owner;
	}
	@Transient
	public Boolean getRight() {
		return right;
	}

	public void setRight(Boolean right) {
		this.right = right;
	}
	@ManyToOne
	@JoinColumn(name="WRK_FLW_MNG_ID")
	public WrkFlwMng getWrkFlwMng() {
		return wrkFlwMng;
	}

	public void setWrkFlwMng(WrkFlwMng wrkFlwMng) {
		this.wrkFlwMng = wrkFlwMng;
	}
	
}
