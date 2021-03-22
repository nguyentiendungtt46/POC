package vn.com.cmc.dto;

public class ServiceDetailSummaryDashboard {
	private Long countRequest;
	private Long avgRequest;
	private Long slowestRequest;
	private Long fastestRequest;
	
	
	public ServiceDetailSummaryDashboard(Long countRequest, Long avgRequest, Long slowestRequest, Long fastestRequest) {
		super();
		if(avgRequest == null) avgRequest = (long) 0;
		if(slowestRequest == null) slowestRequest = (long) 0;
		if(fastestRequest == null) fastestRequest = (long) 0;
		
		this.countRequest = countRequest;
		this.avgRequest = avgRequest;
		this.slowestRequest = slowestRequest;
		this.fastestRequest = fastestRequest;
	}
	
	public Long getCountRequest() {
		return countRequest;
	}
	public void setCountRequest(Long countRequest) {
		this.countRequest = countRequest;
	}
	public Long getAvgRequest() {
		return avgRequest;
	}
	public void setAvgRequest(Long avgRequest) {
		if(avgRequest == null) avgRequest = (long) 0;
		this.avgRequest = avgRequest;
	}
	public Long getSlowestRequest() {
		return slowestRequest;
	}
	public void setSlowestRequest(Long slowestRequest) {
		if(slowestRequest == null) slowestRequest = (long) 0;
		this.slowestRequest = slowestRequest;
	}
	public Long getFastestRequest() {
		return fastestRequest;
	}
	public void setFastestRequest(Long fastestRequest) {
		if(fastestRequest == null) fastestRequest = (long) 0;
		this.fastestRequest = fastestRequest;
	}
	
	
}
