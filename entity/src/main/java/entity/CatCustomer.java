package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CAT_CUSTOMER")
public class CatCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    @Column(name = "TEN_KH")
    private String tenKh;
    @Column(name = "MA_KH")
    private String maKh;
    @Column(name = "MA_CIC")
    private String maCic;
    @Column(name = "DIA_CHI")
    private String diaChi;
    @Column(name = "MS_THUE")
    private String msThue;
    @Column(name = "SO_CMT")
    private String soCmt;
    @Column(name = "DKKD")
    private String dkkd;
    @Column(name = "NGON_NGU")
    private String ngonNgu;
    @Column(name = "DIEN_THOAI")
    private String dienThoai;
    @Column(name = "TEN_GIAM_DOC")
    private String tenGiamDoc;
    @Column(name = "NAM_TAI_CHINH")
    private String namTaiChinh;
    @Column(name = "GHI_CHU")
    private String ghiChu;

    public CatCustomer() {
    }

    public CatCustomer(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenKh() {
        return tenKh;
    }

    public void setTenKh(String tenKh) {
        this.tenKh = tenKh;
    }

    public String getMaKh() {
        return maKh;
    }

    public void setMaKh(String maKh) {
        this.maKh = maKh;
    }

    public String getMaCic() {
        return maCic;
    }

    public void setMaCic(String maCic) {
        this.maCic = maCic;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMsThue() {
        return msThue;
    }

    public void setMsThue(String msThue) {
        this.msThue = msThue;
    }

    public String getSoCmt() {
        return soCmt;
    }

    public void setSoCmt(String soCmt) {
        this.soCmt = soCmt;
    }

    public String getDkkd() {
        return dkkd;
    }

    public void setDkkd(String dkkd) {
        this.dkkd = dkkd;
    }

    public String getNgonNgu() {
        return ngonNgu;
    }

    public void setNgonNgu(String ngonNgu) {
        this.ngonNgu = ngonNgu;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }

    public String getTenGiamDoc() {
        return tenGiamDoc;
    }

    public void setTenGiamDoc(String tenGiamDoc) {
        this.tenGiamDoc = tenGiamDoc;
    }

    public String getNamTaiChinh() {
        return namTaiChinh;
    }

    public void setNamTaiChinh(String namTaiChinh) {
        this.namTaiChinh = namTaiChinh;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

}
