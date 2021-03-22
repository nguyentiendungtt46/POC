package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the LOG_CORE_SERVICES_DETAIL database table.
 * 
 */
@Entity
@Table(name = "LOG_CORE_SERVICES_DETAIL")
@NamedQuery(name = "LogCoreServicesDetail.findAll", query = "SELECT l FROM LogCoreServicesDetail l")
public class LogCoreServicesDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	private String id;

	@Column(name = "TYPE")
	private Long type;

	@Lob
	@Column(name = "VALUE")
	private String value;

	// bi-directional many-to-one association to LogCoreService
	@ManyToOne
	@JoinColumn(name = "LOG_CORE_SERVICES_ID")
	private LogCoreService logCoreService;

	public LogCoreServicesDetail() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getType() {
		return this.type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LogCoreService getLogCoreService() {
		return this.logCoreService;
	}

	public void setLogCoreService(LogCoreService logCoreService) {
		this.logCoreService = logCoreService;
	}

}