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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "QNA_OUT_MASTER")
public class QnaOutMaster implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "MASP")
    private String masp;
    @Column(name = "LOAISP")
    private String loaisp;
    @Column(name = "THOIDIEMTRALOI")
    private Date thoidiemtraloi;
    @Column(name = "USERID")
    private String userid;
    @Column(name = "DIENTHOAI")
    private String dienthoai;
    @Column(name = "MABANTIN")
    private String mabantin;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "DELEVER_TIME")
    private Date deleverTime;
    @Lob
    @Column(name = "CONTENT")
    private String content;
    @JoinColumn(name = "MATOCHUCTINDUNG", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Partner matochuctindung;
    
    @JoinColumn(name = "QNA_IN_MASTER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private QnaInMaster qnaInMasterId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "qnaOutMsId")
    private Set<QnaOutDetail> qnaOutDetails = new HashSet<QnaOutDetail>(0);
    @Transient
	private ArrayList<QnaOutDetail> qnaOutDetailArrayList;
    public QnaOutMaster() {
    }

    public QnaOutMaster(String id) {
        this.id = id;
    }

    public QnaOutMaster(String id, String masp, Date thoidiemtraloi) {
        this.id = id;
        this.masp = masp;
        this.thoidiemtraloi = thoidiemtraloi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getLoaisp() {
        return loaisp;
    }

    public void setLoaisp(String loaisp) {
        this.loaisp = loaisp;
    }

    public Date getThoidiemtraloi() {
        return thoidiemtraloi;
    }

    public void setThoidiemtraloi(Date thoidiemtraloi) {
        this.thoidiemtraloi = thoidiemtraloi;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDienthoai() {
        return dienthoai;
    }

    public void setDienthoai(String dienthoai) {
        this.dienthoai = dienthoai;
    }

    public String getMabantin() {
        return mabantin;
    }

    public void setMabantin(String mabantin) {
        this.mabantin = mabantin;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Date getDeleverTime() {
        return deleverTime;
    }

    public void setDeleverTime(Date deleverTime) {
        this.deleverTime = deleverTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Partner getMatochuctindung() {
        return matochuctindung;
    }

    public void setMatochuctindung(Partner matochuctindung) {
        this.matochuctindung = matochuctindung;
    }

    public QnaInMaster getQnaInMasterId() {
        return qnaInMasterId;
    }

    public void setQnaInMasterId(QnaInMaster qnaInMasterId) {
        this.qnaInMasterId = qnaInMasterId;
    }

	public Set<QnaOutDetail> getQnaOutDetails() {
		return qnaOutDetails;
	}

	public void setQnaOutDetails(Set<QnaOutDetail> qnaOutDetails) {
		this.qnaOutDetails = qnaOutDetails;
		this.qnaOutDetailArrayList = new ArrayList<QnaOutDetail>(qnaOutDetails);
	}

	public ArrayList<QnaOutDetail> getQnaOutDetailArrayList() {
		return qnaOutDetailArrayList;
	}

	public void setQnaOutDetailArrayList(ArrayList<QnaOutDetail> qnaOutDetailArrayList) {
		this.qnaOutDetailArrayList = qnaOutDetailArrayList;
		this.qnaOutDetails.addAll(qnaOutDetailArrayList);
	}
    
    
}
