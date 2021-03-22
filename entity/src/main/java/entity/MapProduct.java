package entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import entity.frwk.SysUsers;

@Entity
@Table(name = "MAP_PRODUCT")
public class MapProduct implements java.io.Serializable {

	private MapProductId id;
	private CatProduct catProductId;
	private SysUsers sysUsersId;
	
	public MapProduct() {
	}
	
	public MapProduct(CatProduct catProductId, SysUsers sysUsersId) {
		this.catProductId = catProductId;
		this.sysUsersId = sysUsersId;
		this.id = new MapProductId(sysUsersId.getId(), catProductId == null ? null : catProductId.getId());
	}

	public MapProduct(MapProductId id, CatProduct catProductId, SysUsers sysUsersId) {
		this.id = id;
		this.catProductId = catProductId;
		this.sysUsersId = sysUsersId;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "sysUsersId", column = @Column(name = "SYS_USERS_ID", nullable = false, length = 40)),
			@AttributeOverride(name = "catProductId", column = @Column(name = "CAT_PRODUCT_ID", nullable = false, length = 40)) })
	public MapProductId getId() {
		return id;
	}

	public void setId(MapProductId id) {
		this.id = id;
	}

	@JoinColumn(name = "CAT_PRODUCT_ID", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	public CatProduct getCatProductId() {
		return catProductId;
	}

	public void setCatProductId(CatProduct catProductId) {
		this.catProductId = catProductId;
	}

	@JoinColumn(name = "SYS_USERS_ID", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	public SysUsers getSysUsersId() {
		return sysUsersId;
	}

	public void setSysUsersId(SysUsers sysUsersId) {
		this.sysUsersId = sysUsersId;
	}
}
