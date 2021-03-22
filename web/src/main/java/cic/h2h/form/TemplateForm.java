package cic.h2h.form;

import java.util.List;

import entity.Templates;
import frwk.form.SearchForm;

public class TemplateForm extends SearchForm<Templates> {
	Templates template = new Templates();
	String stempNm, productId, templateType;
	List<String> lstTemplateType;
	Boolean hasMultiInstance, forAll;

	public Templates getTemplate() {
		return template;
	}

	public void setTemplate(Templates template) {
		this.template = template;
	}

	public String getStempNm() {
		return stempNm;
	}

	public void setStempNm(String stempNm) {
		this.stempNm = stempNm;
	}

	public List<String> getLstTemplateType() {
		return lstTemplateType;
	}

	public void setLstTemplateType(List<String> lstTemplateType) {
		this.lstTemplateType = lstTemplateType;
	}

	public Boolean getHasMultiInstance() {
		return hasMultiInstance;
	}

	public void setHasMultiInstance(Boolean hasMultiInstance) {
		this.hasMultiInstance = hasMultiInstance;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public Boolean getForAll() {
		return forAll;
	}

	public void setForAll(Boolean forAll) {
		this.forAll = forAll;
	}

	@Override
	public Templates getModel() {
		// TODO Auto-generated method stub
		return template;
	}

}
