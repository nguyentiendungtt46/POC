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

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RP_VALIDATE_DETAIL")
public class RpValidateDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;
	@Column(name = "CREATED_USER")
	private String createdUser;
	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	@Column(name = "UPDATED_USER")
	private String updateUser;
	@Column(name = "UPDATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	@Column(name = "GIATRILOI")
	private String giaTriLoi;
	@Column(name = "MACN")
	private String maCN;
	@Column(name = "TENCN")
	private String tenCN;
	@Column(name = "MAKH")
	private String maKH;
	@Column(name = "TENKH")
	private String tenKH;
	@Column(name = "DONGLOI")
	private String dongLoi;
	@Column(name = "MACHITIEU")
	private String maChiTieu;
	@Column(name = "MALOI")
	private String maLoi;
	@Column(name = "MOTALOI")
	private String moTaLoi;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RP_SUM_BRANCH_ID")
	private RpSumBranch rpSumBranch;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPORT_ID", nullable = false)
	private RpSum rpSum;

//	projList.add(Projections.property("maCN"));
//	projList.add(Projections.property("tenCN"));
//	projList.add(Projections.property("maKH"));
//	projList.add(Projections.property("tenKH"));
//	projList.add(Projections.property("dongLoi"));
//	projList.add(Projections.property("maChiTieu"));
//	projList.add(Projections.property("giaTriLoi"));
//	projList.add(Projections.property("maLoi"));
//	projList.add(Projections.property("moTaLoi"));
	@Formula("'MACN' || nvl(MACN, chr(127)) || 'tenCN' || nvl(tenCN, chr(127)) ||'MAKH' || nvl(MAKH, chr(127)) || 'TENKH' || nvl(TENKH, chr(127)) ||'DONGLOI' || nvl(to_char(DONGLOI), chr(127)) || 'maChiTieu' ||nvl(maChiTieu, chr(127)) || 'GIATRILOI' || nvl(GIATRILOI, chr(127)) || 'MALOI' || nvl(MALOI, chr(127)) || 'MOTALOI' ||nvl(MOTALOI, chr(127))") 
	private String concated;

	public String getConcated() {
		return concated;
	}

	public void setConcated(String concated) {
		this.concated = concated;
	}

	public RpValidateDetail() {
		super();
	}

	public RpValidateDetail(Object[] tuple) {
		this.maCN = (String) tuple[0];
		this.tenCN = (String) tuple[1];
		this.maKH = (String) tuple[2];
		this.tenKH = (String) tuple[3];
		if (tuple[4] != null)
			this.dongLoi = tuple[4].toString();
		else
			this.dongLoi = "";
		this.maChiTieu = (String) tuple[5];
		this.giaTriLoi = (String) tuple[6];
		this.maLoi = (String) tuple[7];
		this.moTaLoi = (String) tuple[8];
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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

	public String getTenCN() {
		return tenCN;
	}

	public void setTenCN(String tenCN) {
		this.tenCN = tenCN;
	}

	public String getMaKH() {
		return maKH;
	}

	public void setMaKH(String maKH) {
		this.maKH = maKH;
	}

	public String getTenKH() {
		return tenKH;
	}

	public void setTenKH(String tenKH) {
		this.tenKH = tenKH;
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

	public RpSumBranch getRpSumBranch() {
		return this.rpSumBranch;
	}

	public void setRpSumBranch(RpSumBranch rpSumBranch) {
		this.rpSumBranch = rpSumBranch;
	}

	public RpSum getRpSum() {
		return this.rpSum;
	}

	public void setRpSum(RpSum rpSum) {
		this.rpSum = rpSum;
	}

}
