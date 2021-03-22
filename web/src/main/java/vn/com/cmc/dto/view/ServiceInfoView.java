package vn.com.cmc.dto.view;

import java.util.Date;

public interface ServiceInfoView {
	public String getId();
	public String getServiceName();
	public String getDescription();
	public String getDestinationHost();
	public String getDestinationUri();
	public String getPublishHost();
	public String getPublishUri();
	public String getRequestClass();
	public Long getStatus();
	public String getDestinationOperation();
	public String getPublishOperation();
	public Date getLastRun();
}
