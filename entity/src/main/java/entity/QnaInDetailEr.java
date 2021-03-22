package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jdk.nashorn.internal.ir.annotations.Ignore;

@Entity
@Table(name = "QNA_IN_OUT_DETAIL_ER")
public class QnaInDetailEr implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "ID")
    private String id;
    @Column(name = "MALOI")
    private String maloi;
    @Column(name = "MOTALOI")
    private String motaloi;
    @JoinColumn(name = "QNA_IN_OUT_DETAIL_ID", referencedColumnName = "ID")
    @JsonIgnore
    @ManyToOne
    private QnaInDetail qnaInDetailId;

    public QnaInDetailEr() {
    }

    public QnaInDetailEr(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaloi() {
        return maloi;
    }

    public void setMaloi(String maloi) {
        this.maloi = maloi;
    }

    public String getMotaloi() {
        return motaloi;
    }

    public void setMotaloi(String motaloi) {
        this.motaloi = motaloi;
    }

	public QnaInDetail getQnaInDetailId() {
		return qnaInDetailId;
	}

	public void setQnaInDetailId(QnaInDetail qnaInDetailId) {
		this.qnaInDetailId = qnaInDetailId;
	}

    
}
