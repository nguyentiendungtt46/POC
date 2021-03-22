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
import javax.persistence.FetchType;
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
@Table(name = "QNA_IN_OUT_DETAIL")
public class QnaInDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;
	@Column(name = "MSPHIEU")
	private String msphieu;
	@Column(name = "TENKH")
	private String tenkh;
	@Column(name = "DIACHI")
	private String diachi;
	@Column(name = "MSTHUE")
	private String msthue;
	@Column(name = "SOCMT")
	private String socmt;
	@Column(name = "MANH")
	private String manh;

//	@JoinColumn(name = "MATCTD", referencedColumnName = "CODE")
//	@ManyToOne(optional = false)
//	private Partner matctd;

	@Column(name = "USERID")
	private String nguoihoi;
	@Column(name = "LOAIKH")
	private BigDecimal loaikh;
	@Column(name = "DKKD")
	private String dkkd;
	@Column(name = "LOAIGTK")
	private String loaigtk;
	@Column(name = "SOGTK")
	private String sogtk;
	@Column(name = "MAKH")
	private String makh;
	@Column(name = "MACIC")
	private String macic;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NGAYTAO")
	private Date ngaytao;
	@Column(name = "NGAYTRALOI")
	private Date ngaytraloi;
	@Column(name = "GHICHU")
	private String ghichu;

	@Column(name = "THOIDIEMTRADOITAC")
	private Date thoidiemtraloidoitac;

	@Column(name = "NOIDUNGTRALOI")
	private String noidungtraloi;

	@Column(name = "NOIDUNGCAUHOI")
	private String noidungcauhoi;
	@Column(name = "TUDONG")
	private Boolean tudong;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "THOIDIEMGUIVAOCORECIC")
	private Date thoiDiemGuiVaoCoreCIC;
	@JoinColumn(name = "QNA_IN_MS_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JsonIgnore
	private QnaInMaster qnaInMsId;

	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private CatProduct serviceProduct;
	
	
	@Column(name = "BRANCH")
	private String branch;
	
	@Column(name = "ERROR")
	private Boolean error;

	@Column(name = "STATUS")
	private Short status;

	@Transient
	private String statusStr;

	@Transient
	private String thoidiemtraloidoitacStr;

	@Transient
	private String thoiDiemGuiVaoCoreCICStr;

	@Transient
	private String tudongStr;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "qnaInDetailId")
	private Set<QnaInDetailEr> qnaInDetailErs = new HashSet<QnaInDetailEr>(0);
	@Transient
	private ArrayList<QnaInDetailEr> qnaInDetailErArrayList;
	
	public QnaInDetail() {
	}

	public QnaInDetail(String id) {
		this.id = id;
	}

	public QnaInDetail(String id, String msphieu, String tenkh, String diachi, String socmt, String manh) {
		this.id = id;
		this.msphieu = msphieu;
		this.tenkh = tenkh;
		this.diachi = diachi;
		this.socmt = socmt;
		this.manh = manh;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsphieu() {
		return msphieu;
	}

	public void setMsphieu(String msphieu) {
		this.msphieu = msphieu;
	}

	public String getTenkh() {
		return tenkh;
	}

	public void setTenkh(String tenkh) {
		this.tenkh = tenkh;
	}

	public String getDiachi() {
		return diachi;
	}

	public void setDiachi(String diachi) {
		this.diachi = diachi;
	}

	public String getMsthue() {
		return msthue;
	}

	public void setMsthue(String msthue) {
		this.msthue = msthue;
	}

	public String getSocmt() {
		return socmt;
	}

	public void setSocmt(String socmt) {
		this.socmt = socmt;
	}

	public String getManh() {
		return manh;
	}

	public void setManh(String manh) {
		this.manh = manh;
	}

//	public Partner getMatctd() {
//		return matctd;
//	}
//
//	public void setMatctd(Partner matctd) {
//		this.matctd = matctd;
//	}

	public String getNguoihoi() {
		return nguoihoi;
	}

	public void setNguoihoi(String nguoihoi) {
		this.nguoihoi = nguoihoi;
	}

	public BigDecimal getLoaikh() {
		return loaikh;
	}

	public void setLoaikh(BigDecimal loaikh) {
		this.loaikh = loaikh;
	}

	public String getDkkd() {
		return dkkd;
	}

	public void setDkkd(String dkkd) {
		this.dkkd = dkkd;
	}

	public String getLoaigtk() {
		return loaigtk;
	}

	public void setLoaigtk(String loaigtk) {
		this.loaigtk = loaigtk;
	}

	public String getSogtk() {
		return sogtk;
	}

	public void setSogtk(String sogtk) {
		this.sogtk = sogtk;
	}

	public String getMakh() {
		return makh;
	}

	public void setMakh(String makh) {
		this.makh = makh;
	}

	public String getMacic() {
		return macic;
	}

	public void setMacic(String macic) {
		this.macic = macic;
	}

	public Date getNgaytao() {
		return ngaytao;
	}

	public void setNgaytao(Date ngaytao) {
		this.ngaytao = ngaytao;
	}

	public Date getNgaytraloi() {
		return ngaytraloi;
	}

	public void setNgaytraloi(Date ngaytraloi) {
		this.ngaytraloi = ngaytraloi;
	}

	public String getGhichu() {
		return ghichu;
	}

	public void setGhichu(String ghichu) {
		this.ghichu = ghichu;
	}

	public QnaInMaster getQnaInMsId() {
		return qnaInMsId;
	}

	public void setQnaInMsId(QnaInMaster qnaInMsId) {
		this.qnaInMsId = qnaInMsId;
	}

	public Date getThoidiemtraloidoitac() {
		return thoidiemtraloidoitac;
	}

	public void setThoidiemtraloidoitac(Date thoidiemtraloidoitac) {
		this.thoidiemtraloidoitac = thoidiemtraloidoitac;
	}

	public String getNoidungtraloi() {
		return noidungtraloi;
	}

	public void setNoidungtraloi(String noidungtraloi) {
		this.noidungtraloi = noidungtraloi;
	}

	public String getNoidungcauhoi() {
		return noidungcauhoi;
	}

	public void setNoidungcauhoi(String noidungcauhoi) {
		this.noidungcauhoi = noidungcauhoi;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Boolean getTudong() {
		return tudong;
	}

	public void setTudong(Boolean tudong) {
		this.tudong = tudong;
	}

	public Date getThoiDiemGuiVaoCoreCIC() {
		return thoiDiemGuiVaoCoreCIC;
	}

	public void setThoiDiemGuiVaoCoreCIC(Date thoiDiemGuiVaoCoreCIC) {
		this.thoiDiemGuiVaoCoreCIC = thoiDiemGuiVaoCoreCIC;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getThoidiemtraloidoitacStr() {
		return thoidiemtraloidoitacStr;
	}

	public void setThoidiemtraloidoitacStr(String thoidiemtraloidoitacStr) {
		this.thoidiemtraloidoitacStr = thoidiemtraloidoitacStr;
	}

	public String getTudongStr() {
		return tudongStr;
	}

	public void setTudongStr(String tudongStr) {
		this.tudongStr = tudongStr;
	}

	public String getThoiDiemGuiVaoCoreCICStr() {
		return thoiDiemGuiVaoCoreCICStr;
	}

	public void setThoiDiemGuiVaoCoreCICStr(String thoiDiemGuiVaoCoreCICStr) {
		this.thoiDiemGuiVaoCoreCICStr = thoiDiemGuiVaoCoreCICStr;
	}

	public CatProduct getServiceProduct() {
		return serviceProduct;
	}

	public void setServiceProduct(CatProduct serviceProduct) {
		this.serviceProduct = serviceProduct;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}
	
	public Set<QnaInDetailEr> getQnaInDetailErs() {
		return qnaInDetailErs;
	}

	public void setQnaInDetailErs(Set<QnaInDetailEr> qnaInDetailErs) {
		this.qnaInDetailErs = qnaInDetailErs;
	}

	public ArrayList<QnaInDetailEr> getQnaInDetailErArrayList() {
		return qnaInDetailErArrayList;
	}

	public void setQnaInDetailErArrayList(ArrayList<QnaInDetailEr> qnaInDetailErArrayList) {
		this.qnaInDetailErArrayList = qnaInDetailErArrayList;
	}

}