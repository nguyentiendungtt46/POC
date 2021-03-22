package entity;

import java.io.Serializable;

public class ServiceProductClient implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String productId;
	private String serviceId;

	public ServiceProductClient() {
		super();
	}

	public ServiceProductClient(String id, String productId, String serviceId) {
		super();
		this.id = id;
		this.productId = productId;
		this.serviceId = serviceId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
