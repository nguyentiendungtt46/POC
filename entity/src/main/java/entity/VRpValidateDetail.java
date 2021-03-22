package entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "V_RP_VALIDATE_DETAIL")
public class VRpValidateDetail implements Serializable {
	//t.macn, t.makh, t.dongloi, t.machitieu, t.giatriloi, t.maloi, t.motaloi from RP_VALIDATE_DETAIL t;
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "GIATRILOI")
	private String giaTriLoi;
	@Column(name = "MACN")
	private String maCN;
	@Column(name = "MAKH")
	private String maKH;
	@Column(name = "DONGLOI")
	private String dongLoi;
	@Column(name = "MACHITIEU")
	private String maChiTieu;
	@Column(name = "MALOI")
	private String maLoi;
	@Column(name = "MOTALOI")
	private String moTaLoi;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPORT_ID", nullable = false)
	private RpSum rpSum;
	public RpSum getRpSum() {
		return rpSum;
	}
	public void setRpSum(RpSum rpSum) {
		this.rpSum = rpSum;
	}
	public String getGiaTriLoi() {
		return giaTriLoi;
	}
	public void setGiaTriLoi(String giaTriLoi) {
		this.giaTriLoi = giaTriLoi;
	}
	public String getMaCN() {
		return maCN;
	}
	public void setMaCN(String maCN) {
		this.maCN = maCN;
	}
	public String getMaKH() {
		return maKH;
	}
	public void setMaKH(String maKH) {
		this.maKH = maKH;
	}
	public String getDongLoi() {
		return dongLoi;
	}
	public void setDongLoi(String dongLoi) {
		this.dongLoi = dongLoi;
	}
	public String getMaChiTieu() {
		return maChiTieu;
	}
	public void setMaChiTieu(String maChiTieu) {
		this.maChiTieu = maChiTieu;
	}
	public String getMaLoi() {
		return maLoi;
	}
	public void setMaLoi(String maLoi) {
		this.maLoi = maLoi;
	}
	public String getMoTaLoi() {
		return moTaLoi;
	}
	public void setMoTaLoi(String moTaLoi) {
		this.moTaLoi = moTaLoi;
	}
	
}
