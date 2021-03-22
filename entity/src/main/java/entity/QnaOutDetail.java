package entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "QNA_OUT_DETAIL")
public class QnaOutDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
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
    @Lob
    @Column(name = "NOIDUNG")
    private String noidung;
    @Column(name = "THOIDIEMNHANTRALOI")
    private Date thoidiemnhantraloi;
    @Column(name = "THOIDIEMTRACHOTCTD")
    private Date thoidiemtrachotctd;
    @Column(name = "NGAYTRALOI")
    private Date ngaytraloi;
    @Column(name = "GHICHU")
    private String ghichu;
    @Column(name = "FO1")
    private String fo1;
    @Column(name = "FO2")
    private String fo2;
    @Column(name = "FO3")
    private String fo3;
    @Column(name = "MAKH")
    private String makh;
    @Column(name = "MACIC")
    private String macic;
    @JoinColumn(name = "PARTNER_BRANCH", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Partner partnerBranch;
    @JoinColumn(name = "QNA_OUT_MS_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private QnaOutMaster qnaOutMsId;

    public QnaOutDetail() {
    }

    public QnaOutDetail(String id) {
        this.id = id;
    }

    public QnaOutDetail(String id, String msphieu, String tenkh, String diachi, String socmt, String noidung) {
        this.id = id;
        this.msphieu = msphieu;
        this.tenkh = tenkh;
        this.diachi = diachi;
        this.socmt = socmt;
        this.noidung = noidung;
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

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public Date getThoidiemnhantraloi() {
        return thoidiemnhantraloi;
    }

    public void setThoidiemnhantraloi(Date thoidiemnhantraloi) {
        this.thoidiemnhantraloi = thoidiemnhantraloi;
    }

    public Date getThoidiemtrachotctd() {
        return thoidiemtrachotctd;
    }

    public void setThoidiemtrachotctd(Date thoidiemtrachotctd) {
        this.thoidiemtrachotctd = thoidiemtrachotctd;
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

    public String getFo1() {
        return fo1;
    }

    public void setFo1(String fo1) {
        this.fo1 = fo1;
    }

    public String getFo2() {
        return fo2;
    }

    public void setFo2(String fo2) {
        this.fo2 = fo2;
    }

    public String getFo3() {
        return fo3;
    }

    public void setFo3(String fo3) {
        this.fo3 = fo3;
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

    public Partner getPartnerBranch() {
        return partnerBranch;
    }

    public void setPartnerBranch(Partner partnerBranch) {
        this.partnerBranch = partnerBranch;
    }

    public QnaOutMaster getQnaOutMsId() {
        return qnaOutMsId;
    }

    public void setQnaOutMsId(QnaOutMaster qnaOutMsId) {
        this.qnaOutMsId = qnaOutMsId;
    }

}
