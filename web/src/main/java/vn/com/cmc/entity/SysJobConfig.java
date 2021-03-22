package vn.com.cmc.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the SYS_JOB_CONFIG database table.
 * 
 */
@Entity
@Table(name="SYS_JOB_CONFIG")
@NamedQuery(name="SysJobConfig.findAll", query="SELECT s FROM SysJobConfig s")
public class SysJobConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	private String id;

	private Long active;

	@Column(name="FIX_DELAY")
	private Long fixDelay;

	@Column(name="JOB_CODE")
	private String jobCode;

	@Column(name="JOB_NAME")
	private String jobName;

	@Column(name="JOB_ORDER")
	private Long jobOrder;

	public SysJobConfig() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getActive() {
		return this.active;
	}

	public void setActive(Long active) {
		this.active = active;
	}

	public Long getFixDelay() {
		return this.fixDelay;
	}

	public void setFixDelay(Long fixDelay) {
		this.fixDelay = fixDelay;
	}

	public String getJobCode() {
		return this.jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getJobName() {
		return this.jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Long getJobOrder() {
		return this.jobOrder;
	}

	public void setJobOrder(Long jobOrder) {
		this.jobOrder = jobOrder;
	}

}