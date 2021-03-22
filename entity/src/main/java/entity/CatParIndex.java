package entity;

import java.math.BigDecimal;
import java.text.ParseException;
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

import common.util.FormatNumber;

@Entity
@Table(name = "CAT_PAR_INDEX")
public class CatParIndex implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_ID")
	private CatParIndex parent;
	@Column(name = "CODE")
	private String code;
	@Column(name = "NAME")
	private String name;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "PRICE")
	private BigDecimal price;

	@Transient
	private String priceStr;
	/*
	 * @Column(name = "CREATOR") private String creator;
	 * 
	 * @Column(name = "MODIFIER") private String modifier;
	 */
	@Column(name = "CREATE_DATE")
	private Date createDate;
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	private Set<CatParIndex> entParIndexs = new HashSet(0);

	public CatParIndex() {
	}

	public CatParIndex(String id, CatParIndex parent, String code, String name, String description, BigDecimal price,
			String creator, String modifier, Date createDate, Date modifiedDate, Set<CatParIndex> entParIndexs) {
		super();
		this.id = id;
		this.parent = parent;
		this.code = code;
		this.name = name;
		this.description = description;
		this.price = price;
		/*
		 * this.creator = creator; this.modifier = modifier;
		 */
		this.createDate = createDate;
		this.modifiedDate = modifiedDate;
		this.entParIndexs = entParIndexs;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CatParIndex getParent() {
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
		this.parent = new CatParIndex(parentId);
	}

	public CatParIndex(String id) {
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

	public void setParent(CatParIndex parent) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
		this.priceStr = FormatNumber.num2Str(price);
	}

	/*
	 * public String getCreator() { return creator; }
	 * 
	 * public void setCreator(String creator) { this.creator = creator; }
	 * 
	 * public String getModifier() { return modifier; }
	 * 
	 * public void setModifier(String modifier) { this.modifier = modifier; }
	 */

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

	public Set<CatParIndex> getEntParIndexs() {
		return entParIndexs;
	}

	public void setEntParIndexs(Set<CatParIndex> entParIndexs) {
		this.entParIndexs = entParIndexs;
	}

	public String getPriceStr() {
		if (this.price != null)
			return FormatNumber.num2Str(this.price );
		else return priceStr;
	}

	public void setPriceStr(String priceStr) throws ParseException {
		this.priceStr = priceStr;
		this.price = FormatNumber.str2num(priceStr);
	}

}
