package cic.h2h.form;

import entity.H2hEmail;
import frwk.form.SearchForm;

public class H2hEmailForm extends SearchForm<H2hEmail> {

	Short type;
	Short status;
	String formDate;
	String toDate;
	String email;
	
	String subject;
	String parentId;
	String contentEmail;
	
	
	H2hEmail h2hEmail = new H2hEmail();
	
	@Override
	public H2hEmail getModel() {
		return h2hEmail;
	}

	public H2hEmail getH2hEmail() {
		return h2hEmail;
	}

	public void setH2hEmail(H2hEmail h2hEmail) {
		this.h2hEmail = h2hEmail;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getFormDate() {
		return formDate;
	}

	public void setFormDate(String formDate) {
		this.formDate = formDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getContentEmail() {
		return contentEmail;
	}

	public void setContentEmail(String contentEmail) {
		this.contentEmail = contentEmail;
	}
}
