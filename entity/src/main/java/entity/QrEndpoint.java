package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import entity.frwk.SysDictParam;

@Entity
@Table(name = "QR_ENDPOINT", schema = "CIC")
public class QrEndpoint implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@Column(name = "PRODUCT_CODE")
	private String productCode;

	@Column(name = "ENDPOINT")
	private String endpoint;

	@Column(name = "FUN_NAME")
	private String funName;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "qrEndpoint")
	private List<FunEndpoint> funEndpoints = new ArrayList<FunEndpoint>();

	@javax.persistence.Transient
	private List<SysDictParam> dictParams;

	public QrEndpoint() {
		super();
	}

	public List<SysDictParam> getDictParams() {
		return dictParams;
	}

	public void setDictParams(List<SysDictParam> dictParams) {
		this.dictParams = dictParams;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getFunName() {
		return funName;
	}

	public void setFunName(String funName) {
		this.funName = funName;
	}

	public List<FunEndpoint> getFunEndpoints() {
		return funEndpoints;
	}

	public void setFunEndpoints(List<FunEndpoint> funEndpoints) {
		this.funEndpoints = funEndpoints;
	}

}
