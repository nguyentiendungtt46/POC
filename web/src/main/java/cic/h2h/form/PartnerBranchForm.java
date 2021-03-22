package cic.h2h.form;

import entity.PartnerBranch;
import frwk.form.SearchForm;

public class PartnerBranchForm extends SearchForm<PartnerBranch> {

	PartnerBranch partnerBranch = new PartnerBranch();

	private String codeSearch, nameSearch, partnerId;

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getCodeSearch() {
		return codeSearch;
	}

	public void setCodeSearch(String codeSearch) {
		this.codeSearch = codeSearch;
	}

	public String getNameSearch() {
		return nameSearch;
	}

	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	public PartnerBranch getPartnerBranch() {
		return partnerBranch;
	}

	public void setPartnerBranch(PartnerBranch partnerBranch) {
		this.partnerBranch = partnerBranch;
	}


	@Override
	public PartnerBranch getModel() {
		return partnerBranch;
	}

}
