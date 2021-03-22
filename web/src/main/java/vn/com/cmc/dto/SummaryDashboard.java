package vn.com.cmc.dto;

public class SummaryDashboard {
	
	private Integer countAvailableToken;
	private Integer totalService;
	private Integer upService;
	private Integer downService;
	
	public SummaryDashboard(Integer countAvailableToken, Integer totalService, Integer upService, Integer downService) {
		super();
		if(countAvailableToken == null) countAvailableToken = 0;
		if(totalService == null) totalService = 0;
		if(upService == null) upService = 0;
		if(downService == null) downService = 0;
		
		this.countAvailableToken = countAvailableToken;
		this.totalService = totalService;
		this.upService = upService;
		this.downService = downService;
	}
	
	
	public Integer getCountAvailableToken() {
		return countAvailableToken;
	}

	public void setCountAvailableToken(Integer countAvailableToken) {
		this.countAvailableToken = countAvailableToken;
	}

	public Integer getTotalService() {
		return totalService;
	}
	public void setTotalService(Integer totalService) {
		this.totalService = totalService;
	}
	public Integer getUpService() {
		return upService;
	}
	public void setUpService(Integer upService) {
		this.upService = upService;
	}
	public Integer getDownService() {
		return downService;
	}
	public void setDownService(Integer downService) {
		this.downService = downService;
	}
	
}
