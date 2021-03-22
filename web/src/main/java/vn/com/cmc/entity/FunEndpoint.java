package vn.com.cmc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "FUN_ENDPOINT")
public class FunEndpoint implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@JoinColumn(name = "ENDPOINT_ID")
	@ManyToOne
	@JsonIgnore
	private QrEndpoint qrEndpoint;

	@JoinColumn(name = "PARAM_ID")
	@ManyToOne
	private SysDictParam dictParam;

	@Column(name = "POSTION")
	private String position;

	@Column(name = "FORMAT_DATA_TYPE")
	private String formatDataType;

	@Column(name = "MAX_LENGTH")
	private String maxLength;

	@Column(name = "REQUIRED")
	private Boolean required;

	@Column(name = "REQUIRED_01")
	private String required01;

	@Column(name = "CHECK_MAPPING")
	private String checkMapping;

	@Column(name = "CHECK_VALUE_DEP")
	private String checkValueDep;

	public FunEndpoint() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public QrEndpoint getQrEndpoint() {
		return qrEndpoint;
	}

	public void setQrEndpoint(QrEndpoint qrEndpoint) {
		this.qrEndpoint = qrEndpoint;
	}

	public SysDictParam getDictParam() {
		return dictParam;
	}

	public void setDictParam(SysDictParam dictParam) {
		this.dictParam = dictParam;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getFormatDataType() {
		return formatDataType;
	}

	public void setFormatDataType(String formatDataType) {
		this.formatDataType = formatDataType;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getRequired01() {
		return required01;
	}

	public void setRequired01(String required01) {
		this.required01 = required01;
	}

	public String getCheckMapping() {
		return checkMapping;
	}

	public void setCheckMapping(String checkMapping) {
		this.checkMapping = checkMapping;
	}

	public String getCheckValueDep() {
		return checkValueDep;
	}

	public void setCheckValueDep(String checkValueDep) {
		this.checkValueDep = checkValueDep;
	}

}
