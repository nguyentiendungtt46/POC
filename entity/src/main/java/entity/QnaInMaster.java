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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "QNA_IN_MASTER")
public class QnaInMaster implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;
	@Column(name = "LOAISP")
	private String loaisp;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "THOIGIANYC")
	private Date thoigianyc;
	@Column(name = "USERID")
	private String userid;
	@Column(name = "STATUS")
	private Short status;
	@Lob
	@Column(name = "CONTENT")
	private String content;
	@Lob
	@Column(name = "MO_TA_LOI")
	private String moTaLoi;
	@Column(name = "MA_LOI")
	private String maLoi;
	@Column(name = "RE_TRY_TIME")
	private Short retryTime;
	@Column(name = "MANH")
	private String manh;
	@Column(name = "MA_SO_PHIEU")
	private String maSoPhieu;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "qnaInMsId")
	private Set<QnaInDetail> qnaInDetails = new HashSet<QnaInDetail>(0);
	@Transient
	private ArrayList<QnaInDetail> qnaInDetailArrayList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "qnaInMasterId")
	private Set<QnaOutMaster> qnaOutMasters = new HashSet<QnaOutMaster>(0);;
	@Transient
	private ArrayList<QnaOutMaster> qnaOutMasterArrayList;

//	@JoinColumn(name = "MATOCHUCTINDUNG", referencedColumnName = "CODE")
//	@ManyToOne(optional = false)
//	private Partner matochuctindung;

	@Column(name = "num_Of_Error_Cus")
	private Long numOfErrorCus;
	@Column(name = "CUS_ERROR_DESCRIPTION")
	@Lob
	private String cusErrorDescription;

	@Column(name = "TONG_SO_PHIEU")
	private Long tongSoPhieu;

	@Transient
	private String strStatus;

	public Long getTongSoPhieu() {
		return tongSoPhieu;
	}

	public void setTongSoPhieu(Long tongSoPhieu) {
		this.tongSoPhieu = tongSoPhieu;
	}

	public QnaInMaster() {
	}

	public QnaInMaster(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoaisp() {
		return loaisp;
	}

	public void setLoaisp(String loaisp) {
		this.loaisp = loaisp;
	}

	public Date getThoigianyc() {
		return thoigianyc;
	}

	public void setThoigianyc(Date thoigianyc) {
		this.thoigianyc = thoigianyc;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getManh() {
		return manh;
	}

	public void setManh(String manh) {
		this.manh = manh;
	}

//	public Partner getMatochuctindung() {
//		return matochuctindung;
//	}
//
//	public void setMatochuctindung(Partner matochuctindung) {
//		this.matochuctindung = matochuctindung;
//	}

	public Set<QnaInDetail> getQnaInDetails() {
		return qnaInDetails;
	}

	public void setQnaInDetails(Set<QnaInDetail> qnaInDetails) {
		this.qnaInDetails = qnaInDetails;
		this.qnaInDetailArrayList = new ArrayList<QnaInDetail>(qnaInDetails);
	}

	public ArrayList<QnaInDetail> getQnaInDetailArrayList() {
		return qnaInDetailArrayList;
	}

	public void setQnaInDetailArrayList(ArrayList<QnaInDetail> qnaInDetailArrayList) {
		this.qnaInDetailArrayList = qnaInDetailArrayList;
		this.qnaInDetails.addAll(qnaInDetailArrayList);
	}

	public Set<QnaOutMaster> getQnaOutMasters() {
		return qnaOutMasters;
	}

	public void setQnaOutMasters(Set<QnaOutMaster> qnaOutMasters) {
		this.qnaOutMasters = qnaOutMasters;
		this.qnaOutMasterArrayList = new ArrayList<QnaOutMaster>(qnaOutMasters);
	}

	public ArrayList<QnaOutMaster> getQnaOutMasterArrayList() {
		return qnaOutMasterArrayList;
	}

	public void setQnaOutMasterArrayList(ArrayList<QnaOutMaster> qnaOutMasterArrayList) {
		this.qnaOutMasterArrayList = qnaOutMasterArrayList;
		this.qnaOutMasters.addAll(qnaOutMasterArrayList);
	}

	@Transient
	private Long daTra;
	@Transient
	private Long daCoKetQua;
	@Transient
	private Long chuaCoKetQua;

	public Long getChuaCoKetQua() {
		return chuaCoKetQua;
	}

	public void setChuaCoKetQua(Long chuaCoKetQua) {
		this.chuaCoKetQua = chuaCoKetQua;
	}

	@Transient
	public Long getDaTra() {
		return daTra;
	}

	public void setDaTra(Long daTra) {
		this.daTra = daTra;
	}

	@Transient
	public Long getDaCoKetQua() {
		return daCoKetQua;
	}

	public void setDaCoKetQua(Long daCoKetQua) {
		this.daCoKetQua = daCoKetQua;
	}

	public String getMoTaLoi() {
		return moTaLoi;
	}

	public void setMoTaLoi(String moTaLoi) {
		this.moTaLoi = moTaLoi;
	}

	public String getMaLoi() {
		return maLoi;
	}

	public void setMaLoi(String maLoi) {
		this.maLoi = maLoi;
	}

	public Short getRetryTime() {
		return retryTime;
	}

	public void setRetryTime(Short retryTime) {
		this.retryTime = retryTime;
	}

	public Long getNumOfErrorCus() {
		return numOfErrorCus;
	}

	public void setNumOfErrorCus(Long numOfErrorCus) {
		this.numOfErrorCus = numOfErrorCus;
	}

	public String getCusErrorDescription() {
		return cusErrorDescription;
	}

	public void setCusErrorDescription(String cusErrorDescription) {
		this.cusErrorDescription = cusErrorDescription;
	}

	public String getStrStatus() {
		return strStatus;
	}

	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

	public String getMaSoPhieu() {
		return maSoPhieu;
	}

	public void setMaSoPhieu(String maSoPhieu) {
		this.maSoPhieu = maSoPhieu;
	}

	
}	
