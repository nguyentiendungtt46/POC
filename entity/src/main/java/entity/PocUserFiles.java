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

@Entity
@Table(name = "POC_USER_FILES")
public class PocUserFiles implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	
	@JoinColumn(name = "USERINFO_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private PocUserinfoForm userId;
	
	@JoinColumn(name = "FILE_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private PocFile fileId;

	public PocUserFiles() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PocUserinfoForm getUserId() {
		return userId;
	}

	public void setUserId(PocUserinfoForm userId) {
		this.userId = userId;
	}

	public PocFile getFileId() {
		return fileId;
	}

	public void setFileId(PocFile fileId) {
		this.fileId = fileId;
	}

}
