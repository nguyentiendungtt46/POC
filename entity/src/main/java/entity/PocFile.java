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

import common.util.Formater;

@Entity
@Table(name = "POC_FILE")
public class PocFile implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@JsonIgnore
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private PocUserinfoForm userId;

	@Column(name = "FILE_ID")
	private String fileId;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "FILE_PATH")
	private String filePath;

	@Column(name = "FILE_TYPE")
	private String fileType;

	public PocFile() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (Formater.isNull(id))
			this.id = null;
		else
			this.id = id;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public PocUserinfoForm getUserId() {
		return userId;
	}

	public void setUserId(PocUserinfoForm userId) {
		this.userId = userId;
	}

}
