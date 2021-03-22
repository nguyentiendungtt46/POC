package cic.h2h.form;

import common.util.Formater;
import entity.CatProduct;
import frwk.form.ModelForm;
import frwk.form.SearchForm;

public class CatProductForm extends SearchForm<CatProduct> {
	private String  keyword_code,keyword_name,server_name;
	private String lstIdxId;
	public String getLstIdxId() {
		return lstIdxId;
	}
	public void setLstIdxId(String lstIdxId) {
		this.lstIdxId = lstIdxId;
		if(!Formater.isNull(this.lstIdxId))
			this.arrIdxId = this.lstIdxId.split(",");
		else this.arrIdxId = new String[] {};
	}
	private String[] arrIdxId;
	public String[] getArrIdxId() {
		return arrIdxId;
	}
	private CatProduct catProduct = new CatProduct();
	public String getKeyword_code() {
		return keyword_code;
	}
	public void setKeyword_code(String keyword_code) {
		this.keyword_code = keyword_code;
	}
	public String getKeyword_name() {
		return keyword_name;
	}
	public void setKeyword_name(String keyword_name) {
		this.keyword_name = keyword_name;
	}
	public CatProduct getCatProduct() {
		return catProduct;
	}
	public void setCatProduct(CatProduct catProduct) {
		this.catProduct = catProduct;
	}
	public String getServer_name() {
		return server_name;
	}
	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}
	@Override
	public CatProduct getModel() {
		
		return catProduct;
	}
	
}
