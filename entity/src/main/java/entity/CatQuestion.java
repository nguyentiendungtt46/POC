package entity;

/**
*
* @author nvhuy
*/
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CAT_QUESTION")
public class CatQuestion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID")
    private String id;
    @Column(name = "MA_CAU_HOI")
    private String maCauHoi;
    @Column(name = "CAU_HOI")
    private String cauHoi;
    @Column(name = "DIEM_YEU")
    private Short diemYeu;
    @Column(name = "DIEM_TRUNGBINH")
    private Short diemTrungbinh;
    @Column(name = "DIEM_TOT")
    private Short diemTot;

    public CatQuestion() {
    }

    public CatQuestion(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCauHoi() {
        return cauHoi;
    }

    public void setCauHoi(String cauHoi) {
        this.cauHoi = cauHoi;
    }

    public Short getDiemYeu() {
        return diemYeu;
    }

    public void setDiemYeu(Short diemYeu) {
        this.diemYeu = diemYeu;
    }

    public Short getDiemTrungbinh() {
        return diemTrungbinh;
    }

    public void setDiemTrungbinh(Short diemTrungbinh) {
        this.diemTrungbinh = diemTrungbinh;
    }

    public Short getDiemTot() {
        return diemTot;
    }

    public void setDiemTot(Short diemTot) {
        this.diemTot = diemTot;
    }

	public String getMaCauHoi() {
		return maCauHoi;
	}

	public void setMaCauHoi(String maCauHoi) {
		this.maCauHoi = maCauHoi;
	}
    
}
