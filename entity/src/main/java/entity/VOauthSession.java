package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "V_OAUTH_SESSION")
public class VOauthSession implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@Column(name = "CODE")
	private String code;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "SO_LUONG_SESSION")
	private String soLuongSession;
	
	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "THOI_DIEM_KET_NOI")
	private Date thoiDiemKetNoi;
	
	@Column(name = "THOI_GIAN_KET_NOI")
	private BigDecimal thoiGianKetNoi;
	
	public VOauthSession() {
		super();
	}

	public VOauthSession(String id, String code, String name, String soLuongSession, String userName,
			Date thoiDiemKetNoi, BigDecimal thoiGianKetNoi) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.soLuongSession = soLuongSession;
		this.userName = userName;
		this.thoiDiemKetNoi = thoiDiemKetNoi;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSoLuongSession() {
		return soLuongSession;
	}

	public void setSoLuongSession(String soLuongSession) {
		this.soLuongSession = soLuongSession;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getThoiDiemKetNoi() {
		return thoiDiemKetNoi;
	}

	public void setThoiDiemKetNoi(Date thoiDiemKetNoi) {
		this.thoiDiemKetNoi = thoiDiemKetNoi;
	}

	public BigDecimal getThoiGianKetNoi() {
		return thoiGianKetNoi;
	}

	public void setThoiGianKetNoi(BigDecimal thoiGianKetNoi) {
		this.thoiGianKetNoi = thoiGianKetNoi;
	}
}
