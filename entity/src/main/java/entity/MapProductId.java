package entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MapProductId implements java.io.Serializable {

	private String sysUsersId;
	private String catProductId;
	
	public MapProductId() {
	}
	
	public MapProductId(String sysUsersId, String catProductId) {
		this.sysUsersId = sysUsersId;
		this.catProductId = catProductId;
	}
	
	@Column(name = "SYS_USERS_ID", nullable = false, length = 40)
	public String getSysUsersId() {
		return this.sysUsersId;
	}
	public void setSysUsersId(String sysUsersId) {
		this.sysUsersId = sysUsersId;
	}
	
	@Column(name = "CAT_PRODUCT_ID", nullable = false, length = 40)
	public String getCatProductId() {
		return this.catProductId;
	}
	public void setCatProductId(String catProductId) {
		this.catProductId = catProductId;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MapProductId))
			return false;
		MapProductId castOther = (MapProductId) other;

		return ((this.getSysUsersId() == castOther.getSysUsersId()) || (this.getSysUsersId() != null
				&& castOther.getSysUsersId() != null && this.getSysUsersId().equals(castOther.getSysUsersId())))
				&& ((this.getCatProductId() == castOther.getCatProductId()) || (this.getCatProductId() != null
						&& castOther.getCatProductId() != null && this.getCatProductId().equals(castOther.getCatProductId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getSysUsersId() == null ? 0 : this.getSysUsersId().hashCode());
		result = 37 * result + (getCatProductId() == null ? 0 : this.getCatProductId().hashCode());
		return result;
	}
}	
