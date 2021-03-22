package entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SYS_JOB_CONFIG")
public class SysJobConfig implements Serializable {
	private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    @Column(name = "JOB_CODE")
    private String jobCode;
    @Column(name = "JOB_NAME")
    private String jobName;
    @Column(name = "ACTIVE")
    private Boolean active;
    @Column(name = "FIX_DELAY")
    private BigDecimal fixDelay;
    @Column(name = "JOB_ORDER")
    private BigDecimal jobOrder;
    @Column(name = "PERIOD")
    private BigDecimal period;
    @Column(name = "RATE")
    private BigDecimal rate;
    
    public SysJobConfig() {
    }

    public SysJobConfig(String id) {
        this.id = id;
    }

    public SysJobConfig(String id, String jobCode) {
        this.id = id;
        this.jobCode = jobCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BigDecimal getFixDelay() {
        return fixDelay;
    }

    public void setFixDelay(BigDecimal fixDelay) {
        this.fixDelay = fixDelay;
    }

    public BigDecimal getJobOrder() {
        return jobOrder;
    }

    public void setJobOrder(BigDecimal jobOrder) {
        this.jobOrder = jobOrder;
    }

	public BigDecimal getPeriod() {
		return period;
	}

	public void setPeriod(BigDecimal period) {
		this.period = period;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
    
}
