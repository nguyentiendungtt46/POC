package entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "QNA_OUT_FILE")
public class QnaOutFile implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	private String id;
	@Column(name = "NGAYTRALOICUOICUNG")
	private Date thoidiemtrachotctd;
	@Column(name = "TENFILE")
	private String tenfile;
	@Column(name = "MATOCHUCTINDUNG")
	private String maToChucTinDung;
	@Column(name = "USERLAYKETQUA")
	private String userLayKetQua;
	@Column(name = "NGAYCOKETQUA")
	private Date ngayCoKetQua;

	@Transient
	private String ngayCoKetQuaStr;
	@Transient
	private String thoidiemtrachotctdStr;

	public Date getNgayCoKetQua() {
		return ngayCoKetQua;
	}

	public void setNgayCoKetQua(Date ngayCoKetQua) {
		this.ngayCoKetQua = ngayCoKetQua;
	}

	public String getUserLayKetQua() {
		return userLayKetQua;
	}

	public void setUserLayKetQua(String userLayKetQua) {
		this.userLayKetQua = userLayKetQua;
	}

	public String getMaToChucTinDung() {
		return maToChucTinDung;
	}

	public void setMaToChucTinDung(String maToChucTinDung) {
		this.maToChucTinDung = maToChucTinDung;
	}

	public QnaOutFile() {
		super();
	}

	public QnaOutFile(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getThoidiemtrachotctd() {
		return thoidiemtrachotctd;
	}

	public void setThoidiemtrachotctd(Date thoidiemtrachotctd) {
		this.thoidiemtrachotctd = thoidiemtrachotctd;
	}

	public String getTenfile() {
		return tenfile;
	}

	public void setTenfile(String tenfile) {
		this.tenfile = tenfile;
	}

	public String getNgayCoKetQuaStr() {
		return ngayCoKetQuaStr;
	}

	public void setNgayCoKetQuaStr(String ngayCoKetQuaStr) {
		this.ngayCoKetQuaStr = ngayCoKetQuaStr;
	}

	public String getThoidiemtrachotctdStr() {
		return thoidiemtrachotctdStr;
	}

	public void setThoidiemtrachotctdStr(String thoidiemtrachotctdStr) {
		this.thoidiemtrachotctdStr = thoidiemtrachotctdStr;
	}

}
