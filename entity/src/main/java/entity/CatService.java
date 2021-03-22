package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CAT_SERVICE")
public class CatService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;
	
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "DELAY_TIME")
	private Long delayTime;
	
	@Column(name = "DISABLED_STATUS")
	private Short disabledStatus;

	public CatService() {
		super();
	}
	
	public CatService(String id, String code, String name, String description, Long delayTime, Short disabledStatus) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.description = description;
		this.delayTime = delayTime;
		this.disabledStatus = disabledStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Long getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Long delayTime) {
		this.delayTime = delayTime;
	}

	public Short getDisabledStatus() {
		return disabledStatus;
	}

	public void setDisabledStatus(Short disabledStatus) {
		this.disabledStatus = disabledStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
