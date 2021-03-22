package vn.com.cmc.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the SYS_PARAM database table.
 * 
 */
@Entity
@Table(name="SYS_PARAM")
@NamedQuery(name="SysParam.findAll", query="SELECT s FROM SysParam s")
public class SysParam implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	private String code;

	private String description;

	private String name;

	@Column(name="VALUE")
	private String value;

	public SysParam() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}