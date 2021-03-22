package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "QNA_IN_FILE")
public class QnaInFile implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;
	@JsonIgnore
	@JoinColumn(name = "MATOCHUCTINDUNG", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private Partner tctd;
	@Column(name = "DUONGDANFILECAUHOI")
	private String duongdanfilecauhoi;
	@Column(name="DESCRIPTION")
	private String descripton;
	private Date ngaybatdau;
	@Column(name = "CHUKY")
	private BigDecimal chuky;
	@Column(name = "TENFILE")
	private String tenfile;
	@Column(name = "MASP")
	private String masp;
	@Column(name = "USERHOITIN")
	private String userHoiTin;
	@Column(name = "NGAYTRALOICUOICUNG")
	@Temporal(TemporalType.TIMESTAMP)
	private Date ngayTraLoiCuoiCung;

	@Column(name = "NGAYHOI")
	@Temporal(TemporalType.TIMESTAMP)
	private Date ngayHoi;

	@Column(name = "STATUS")
	private Boolean status;
	
	@Column(name="FILE_SIZE")
	private String fileSize;
	
	@Column(name = "BRANCH")
	private String branch;
	
	@Transient
	private String ngayHoiStr;

	@Transient
	private String ngayTraLoiCuoiCungStr;

	@Transient
	private String statusStr;

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Date getNgayHoi() {
		return ngayHoi;
	}

	public void setNgayHoi(Date ngayHoi) {
		this.ngayHoi = ngayHoi;
	}

	public String getUserHoiTin() {
		return userHoiTin;
	}

	public void setUserHoiTin(String userHoiTin) {
		this.userHoiTin = userHoiTin;
	}

	public Date getNgayTraLoiCuoiCung() {
		return ngayTraLoiCuoiCung;
	}

	public void setNgayTraLoiCuoiCung(Date ngayTraLoiCuoiCung) {
		this.ngayTraLoiCuoiCung = ngayTraLoiCuoiCung;
	}

	public String getMasp() {
		return masp;
	}

	public void setMasp(String masp) {
		this.masp = masp;
	}

	@Transient
	private ArrayList<QnaOutFile> qnaOutFileArrayList;

	public QnaInFile() {
		super();
	}

	public QnaInFile(String id, Partner tctd, String duongdanfilecauhoi, Date ngaybatdau, BigDecimal chuky,
			String tenfile) {
		super();
		this.id = id;
		this.tctd = tctd;
		this.duongdanfilecauhoi = duongdanfilecauhoi;
		this.ngaybatdau = ngaybatdau;
		this.chuky = chuky;
		this.tenfile = tenfile;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Partner getTctd() {
		return tctd;
	}

	public void setTctd(Partner tctd) {
		this.tctd = tctd;
	}

	public String getDuongdanfilecauhoi() {
		return duongdanfilecauhoi;
	}

	public void setDuongdanfilecauhoi(String duongdanfilecauhoi) {
		this.duongdanfilecauhoi = duongdanfilecauhoi;
	}

	public Date getNgaybatdau() {
		return ngaybatdau;
	}

	public void setNgaybatdau(Date ngaybatdau) {
		this.ngaybatdau = ngaybatdau;
	}

	public BigDecimal getChuky() {
		return chuky;
	}

	public void setChuky(BigDecimal chuky) {
		this.chuky = chuky;
	}

	public String getTenfile() {
		return tenfile;
	}

	public void setTenfile(String tenfile) {
		this.tenfile = tenfile;
	}

	public String getNgayHoiStr() {
		return ngayHoiStr;
	}

	public void setNgayHoiStr(String ngayHoiStr) {
		this.ngayHoiStr = ngayHoiStr;
	}

	public String getNgayTraLoiCuoiCungStr() {
		return ngayTraLoiCuoiCungStr;
	}

	public void setNgayTraLoiCuoiCungStr(String ngayTraLoiCuoiCungStr) {
		this.ngayTraLoiCuoiCungStr = ngayTraLoiCuoiCungStr;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getDescripton() {
		return descripton;
	}

	public void setDescripton(String descripton) {
		this.descripton = descripton;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}
	
}
