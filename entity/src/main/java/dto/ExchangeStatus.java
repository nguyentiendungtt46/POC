package dto;

import java.io.Serializable;

public class ExchangeStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	private String partnerCode;
	private String partnerName;
	private String fromDataTotal;
	private String toDataTotal;
	private String totalRequest;

	public ExchangeStatus() {
		super();
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getFromDataTotal() {
		return fromDataTotal;
	}

	public void setFromDataTotal(String fromDataTotal) {
		this.fromDataTotal = fromDataTotal;
	}

	public String getToDataTotal() {
		return toDataTotal;
	}

	public void setToDataTotal(String toDataTotal) {
		this.toDataTotal = toDataTotal;
	}

	public String getTotalRequest() {
		return totalRequest;
	}

	public void setTotalRequest(String totalRequest) {
		this.totalRequest = totalRequest;
	}

}
