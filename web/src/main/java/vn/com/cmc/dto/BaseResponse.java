/**
 * 
 */
package vn.com.cmc.dto;

/**
 * @author nvtiep
 *
 *         created: Dec 26, 2019 9:37:24 AM
 */
public class BaseResponse {
	public static final int SUCCESS_STATUS = 200;
	public static final String SUCCESS_CODE = "SUCCESS";
	public static final String SUCCESS_MESSAGE = "Thực hiện thành công!";

	public static final int EXCEPTION_STATUS = 0;
	public static final String EXCEPTION_CODE = "EXCEPTION";
	public static final String EXCEPTION_MESSAGE = "Thực hiện không thành công!";

	private int responseStatus;
	private String responseCode;
	private String message;

	public BaseResponse() {
		super();
	}

	public BaseResponse(int responseStatus, String responseCode, String message) {
		super();
		this.responseStatus = responseStatus;
		this.responseCode = responseCode;
		this.message = message;
	}

	public int getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(int responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
