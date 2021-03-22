package entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CAT_AGENCY_STRUCTURE")
public class CatAgencyStructure implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_ID")
	private CatAgencyStructure parent;
	@Column(name = "CODE")
	private String code;
	@Column(name = "NAME")
	private String name;
	@Column(name = "CREATE_DATE")
	private Date createDate;
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	private Set<CatAgencyStructure> catAgencyStructures = new HashSet(0);

	public CatAgencyStructure() {
	}

	public CatAgencyStructure(String id, CatAgencyStructure parent, String code, String name,
			String creator, String modifier, Date createDate, Date modifiedDate, Set<CatAgencyStructure> catAgencyStructures) {
		super();
		this.id = id;
		this.parent = parent;
		this.code = code;
		this.name = name;
		this.createDate = createDate;
		this.modifiedDate = modifiedDate;
		this.catAgencyStructures = catAgencyStructures;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CatAgencyStructure getParent() {
		return parent;
	}

	@Transient
	private String fullPath;

	@Transient
	private String parentId;

//	@Transient
//	public String getFullPath() {
//		if (this.getParent() == null)
//			return this.getCode() + "-" + this.getName();
//		else
//			return this.getParent().getFullPath() + "/" + this.getCode() + "-" + this.getName();
//	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
		this.parent = new CatAgencyStructure(parentId);
	}

	public CatAgencyStructure(String id) {
		super();
		this.id = id;
	}

	public String getFullPath() {
		if (this.parent != null) {
			return this.parent.getCode() + "-" + this.parent.name;
		}
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public void setParent(CatAgencyStructure parent) {
		this.parent = parent;
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


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Set<CatAgencyStructure> getCatAgencyStructures() {
		return catAgencyStructures;
	}

	public void setCatAgencyStructures(Set<CatAgencyStructure> catAgencyStructures) {
		this.catAgencyStructures = catAgencyStructures;
	}

}
