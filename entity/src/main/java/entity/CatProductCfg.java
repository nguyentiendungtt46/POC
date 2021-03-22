package entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CAT_PRODUCT_CFG")
public class CatProductCfg implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    @JoinColumn(name = "INDEX_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CatParIndex indexId;
    @JsonIgnore
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CatProduct productId;
    
    public CatProductCfg() {
    	super();
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CatParIndex getIndexId() {
		return indexId;
	}

	public void setIndexId(CatParIndex indexId) {
		this.indexId = indexId;
	}

	public CatProduct getProductId() {
		return productId;
	}

	public void setProductId(CatProduct productId) {
		this.productId = productId;
	}
    
    
}
