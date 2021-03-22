package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "INFO_SEARCH_CIC")
public class InfoSearchCic implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    @Column(name = "MATCTD")
    private String matctd;
    @Column(name = "LOAIKH")
    private BigDecimal loaikh;
    @Column(name = "THANHPHO")
    private String thanhpho;
    @Column(name = "DIABAN")
    private String diaban;
    @Column(name = "LOAI_DULIEU")
    private Boolean loaiDulieu;
    @Column(name = "STR_DULIEU")
    private String strDulieu;
    @Column(name = "CO_QH")
    private Boolean coQh;
    @Column(name = "KO_QH")
    private Boolean koQh;
    @Column(name = "CT_QH")
    private Boolean ctQh;
    @Column(name = "SO_THANG_QH")
    private BigInteger soThangQh;
    @Column(name = "CO_TSDB")
    private Boolean coTsdb;
    @Column(name = "KO_TSDB")
    private Boolean koTsdb;
    @Column(name = "TT_XEPHANG")
    private Boolean ttXephang;
    @Column(name = "TT_XEPHANG_STR")
    private String ttXephangStr;
    @Column(name = "TD_CANHAN")
    private Boolean tdCanhan;
    @Column(name = "TD_CANHAN_STR")
    private String tdCanhanStr;
    @Column(name = "DNGHIEP")
    private Boolean dnghiep;
    @Column(name = "DNGHIEP_TU")
    private BigInteger dnghiepTu;
    @Column(name = "DNGHIEP_DEN")
    private BigInteger dnghiepDen;
    @Column(name = "DNGHIEP_SYM")
    private Boolean dnghiepSym;
    @Column(name = "TTSAN")
    private Boolean ttsan;
    @Column(name = "TTSAN_TU")
    private BigInteger ttsanTu;
    @Column(name = "TTSAN_DEN")
    private BigInteger ttsanDen;
    @Column(name = "TTSAN_SYM")
    private Boolean ttsanSym;
    @Column(name = "LNHUAN")
    private Boolean lnhuan;
    @Column(name = "LNHUAN_TU")
    private BigInteger lnhuanTu;
    @Column(name = "LNHUAN_DEN")
    private BigInteger lnhuanDen;
    @Column(name = "LNHUAN_SYM")
    private Boolean lnhuanSym;
    @Column(name = "YEAR_ORAND")
    private Boolean yearOrand;
    @Column(name = "YEAR_COUNT")
    private BigInteger yearCount;
    @Column(name = "NHOMNO1_YES")
    private Boolean nhomno1Yes;
    @Column(name = "NHOMNO1_NO")
    private Boolean nhomno1No;
    @Column(name = "NHOMNO2_YES")
    private Boolean nhomno2Yes;
    @Column(name = "NHOMNO2_NO")
    private Boolean nhomno2No;
    @Column(name = "NHOMNO3_YES")
    private Boolean nhomno3Yes;
    @Column(name = "NHOMNO3_NO")
    private Boolean nhomno3No;
    @Column(name = "NHOMNO4_YES")
    private Boolean nhomno4Yes;
    @Column(name = "NHOMNO4_NO")
    private Boolean nhomno4No;
    @Column(name = "NHOMNO5_YES")
    private Boolean nhomno5Yes;
    @Column(name = "NHOMNO5_NO")
    private Boolean nhomno5No;
    @Column(name = "VAMC_YES")
    private Boolean vamcYes;
    @Column(name = "VAMC_NO")
    private Boolean vamcNo;
    @Column(name = "NOXAU_YES")
    private Boolean noxauYes;
    @Column(name = "NOXAU_NO")
    private Boolean noxauNo;
    @Column(name = "DN_NGOAITE_YES")
    private Boolean dnNgoaiteYes;
    @Column(name = "DN_NGOAITE_NO")
    private Boolean dnNgoaiteNo;
    @Column(name = "DUNO_TU")
    private BigInteger dunoTu;
    @Column(name = "DUNO_DEN")
    private BigInteger dunoDen;
    @Column(name = "DUNO_SYM")
    private Boolean dunoSym;

    public InfoSearchCic() {
    }

    public InfoSearchCic(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatctd() {
        return matctd;
    }

    public void setMatctd(String matctd) {
        this.matctd = matctd;
    }

    public BigDecimal getLoaikh() {
        return loaikh;
    }

    public void setLoaikh(BigDecimal loaikh) {
        this.loaikh = loaikh;
    }

    public String getThanhpho() {
        return thanhpho;
    }

    public void setThanhpho(String thanhpho) {
        this.thanhpho = thanhpho;
    }

    public String getDiaban() {
        return diaban;
    }

    public void setDiaban(String diaban) {
        this.diaban = diaban;
    }

    public Boolean getLoaiDulieu() {
        return loaiDulieu;
    }

    public void setLoaiDulieu(Boolean loaiDulieu) {
        this.loaiDulieu = loaiDulieu;
    }

    public String getStrDulieu() {
        return strDulieu;
    }

    public void setStrDulieu(String strDulieu) {
        this.strDulieu = strDulieu;
    }

    public Boolean getCoQh() {
        return coQh;
    }

    public void setCoQh(Boolean coQh) {
        this.coQh = coQh;
    }

    public Boolean getKoQh() {
        return koQh;
    }

    public void setKoQh(Boolean koQh) {
        this.koQh = koQh;
    }

    public Boolean getCtQh() {
        return ctQh;
    }

    public void setCtQh(Boolean ctQh) {
        this.ctQh = ctQh;
    }

    public BigInteger getSoThangQh() {
        return soThangQh;
    }

    public void setSoThangQh(BigInteger soThangQh) {
        this.soThangQh = soThangQh;
    }

    public Boolean getCoTsdb() {
        return coTsdb;
    }

    public void setCoTsdb(Boolean coTsdb) {
        this.coTsdb = coTsdb;
    }

    public Boolean getKoTsdb() {
        return koTsdb;
    }

    public void setKoTsdb(Boolean koTsdb) {
        this.koTsdb = koTsdb;
    }

    public Boolean getTtXephang() {
        return ttXephang;
    }

    public void setTtXephang(Boolean ttXephang) {
        this.ttXephang = ttXephang;
    }

    public String getTtXephangStr() {
        return ttXephangStr;
    }

    public void setTtXephangStr(String ttXephangStr) {
        this.ttXephangStr = ttXephangStr;
    }

    public Boolean getTdCanhan() {
        return tdCanhan;
    }

    public void setTdCanhan(Boolean tdCanhan) {
        this.tdCanhan = tdCanhan;
    }

    public String getTdCanhanStr() {
        return tdCanhanStr;
    }

    public void setTdCanhanStr(String tdCanhanStr) {
        this.tdCanhanStr = tdCanhanStr;
    }

    public Boolean getDnghiep() {
        return dnghiep;
    }

    public void setDnghiep(Boolean dnghiep) {
        this.dnghiep = dnghiep;
    }

    public BigInteger getDnghiepTu() {
        return dnghiepTu;
    }

    public void setDnghiepTu(BigInteger dnghiepTu) {
        this.dnghiepTu = dnghiepTu;
    }

    public BigInteger getDnghiepDen() {
        return dnghiepDen;
    }

    public void setDnghiepDen(BigInteger dnghiepDen) {
        this.dnghiepDen = dnghiepDen;
    }

    public Boolean getDnghiepSym() {
        return dnghiepSym;
    }

    public void setDnghiepSym(Boolean dnghiepSym) {
        this.dnghiepSym = dnghiepSym;
    }

    public Boolean getTtsan() {
        return ttsan;
    }

    public void setTtsan(Boolean ttsan) {
        this.ttsan = ttsan;
    }

    public BigInteger getTtsanTu() {
        return ttsanTu;
    }

    public void setTtsanTu(BigInteger ttsanTu) {
        this.ttsanTu = ttsanTu;
    }

    public BigInteger getTtsanDen() {
        return ttsanDen;
    }

    public void setTtsanDen(BigInteger ttsanDen) {
        this.ttsanDen = ttsanDen;
    }

    public Boolean getTtsanSym() {
        return ttsanSym;
    }

    public void setTtsanSym(Boolean ttsanSym) {
        this.ttsanSym = ttsanSym;
    }

    public Boolean getLnhuan() {
        return lnhuan;
    }

    public void setLnhuan(Boolean lnhuan) {
        this.lnhuan = lnhuan;
    }

    public BigInteger getLnhuanTu() {
        return lnhuanTu;
    }

    public void setLnhuanTu(BigInteger lnhuanTu) {
        this.lnhuanTu = lnhuanTu;
    }

    public BigInteger getLnhuanDen() {
        return lnhuanDen;
    }

    public void setLnhuanDen(BigInteger lnhuanDen) {
        this.lnhuanDen = lnhuanDen;
    }

    public Boolean getLnhuanSym() {
        return lnhuanSym;
    }

    public void setLnhuanSym(Boolean lnhuanSym) {
        this.lnhuanSym = lnhuanSym;
    }

    public Boolean getYearOrand() {
        return yearOrand;
    }

    public void setYearOrand(Boolean yearOrand) {
        this.yearOrand = yearOrand;
    }

    public BigInteger getYearCount() {
        return yearCount;
    }

    public void setYearCount(BigInteger yearCount) {
        this.yearCount = yearCount;
    }

    public Boolean getNhomno1Yes() {
        return nhomno1Yes;
    }

    public void setNhomno1Yes(Boolean nhomno1Yes) {
        this.nhomno1Yes = nhomno1Yes;
    }

    public Boolean getNhomno1No() {
        return nhomno1No;
    }

    public void setNhomno1No(Boolean nhomno1No) {
        this.nhomno1No = nhomno1No;
    }

    public Boolean getNhomno2Yes() {
        return nhomno2Yes;
    }

    public void setNhomno2Yes(Boolean nhomno2Yes) {
        this.nhomno2Yes = nhomno2Yes;
    }

    public Boolean getNhomno2No() {
        return nhomno2No;
    }

    public void setNhomno2No(Boolean nhomno2No) {
        this.nhomno2No = nhomno2No;
    }

    public Boolean getNhomno3Yes() {
        return nhomno3Yes;
    }

    public void setNhomno3Yes(Boolean nhomno3Yes) {
        this.nhomno3Yes = nhomno3Yes;
    }

    public Boolean getNhomno3No() {
        return nhomno3No;
    }

    public void setNhomno3No(Boolean nhomno3No) {
        this.nhomno3No = nhomno3No;
    }

    public Boolean getNhomno4Yes() {
        return nhomno4Yes;
    }

    public void setNhomno4Yes(Boolean nhomno4Yes) {
        this.nhomno4Yes = nhomno4Yes;
    }

    public Boolean getNhomno4No() {
        return nhomno4No;
    }

    public void setNhomno4No(Boolean nhomno4No) {
        this.nhomno4No = nhomno4No;
    }

    public Boolean getNhomno5Yes() {
        return nhomno5Yes;
    }

    public void setNhomno5Yes(Boolean nhomno5Yes) {
        this.nhomno5Yes = nhomno5Yes;
    }

    public Boolean getNhomno5No() {
        return nhomno5No;
    }

    public void setNhomno5No(Boolean nhomno5No) {
        this.nhomno5No = nhomno5No;
    }

    public Boolean getVamcYes() {
        return vamcYes;
    }

    public void setVamcYes(Boolean vamcYes) {
        this.vamcYes = vamcYes;
    }

    public Boolean getVamcNo() {
        return vamcNo;
    }

    public void setVamcNo(Boolean vamcNo) {
        this.vamcNo = vamcNo;
    }

    public Boolean getNoxauYes() {
        return noxauYes;
    }

    public void setNoxauYes(Boolean noxauYes) {
        this.noxauYes = noxauYes;
    }

    public Boolean getNoxauNo() {
        return noxauNo;
    }

    public void setNoxauNo(Boolean noxauNo) {
        this.noxauNo = noxauNo;
    }

    public Boolean getDnNgoaiteYes() {
        return dnNgoaiteYes;
    }

    public void setDnNgoaiteYes(Boolean dnNgoaiteYes) {
        this.dnNgoaiteYes = dnNgoaiteYes;
    }

    public Boolean getDnNgoaiteNo() {
        return dnNgoaiteNo;
    }

    public void setDnNgoaiteNo(Boolean dnNgoaiteNo) {
        this.dnNgoaiteNo = dnNgoaiteNo;
    }

    public BigInteger getDunoTu() {
        return dunoTu;
    }

    public void setDunoTu(BigInteger dunoTu) {
        this.dunoTu = dunoTu;
    }

    public BigInteger getDunoDen() {
        return dunoDen;
    }

    public void setDunoDen(BigInteger dunoDen) {
        this.dunoDen = dunoDen;
    }

    public Boolean getDunoSym() {
        return dunoSym;
    }

    public void setDunoSym(Boolean dunoSym) {
        this.dunoSym = dunoSym;
    }

}
