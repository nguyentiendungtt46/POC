package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.util.Formater;
import entity.frwk.SysDictParam;

@Entity
@Table(name = "POC_INTERVIEW_RS")
public class PocInterViewRs implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@JsonIgnore
	@JoinColumn(name = "USERINFO_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private PocUserinfoForm userId;

	@Column(name = "QUESTION_ID")
	private String questionId;

	@Column(name = "RESULT")
	private String result;

	public PocInterViewRs() {
		super();
	}

	public PocInterViewRs(SysDictParam question) {
		this.questionId = question.getId();
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

	public PocUserinfoForm getUserId() {
		return userId;
	}

	public void setUserId(PocUserinfoForm userId) {
		this.userId = userId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
