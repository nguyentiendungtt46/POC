package vn.com.cmc.dto;

public class LoggingResponse {

	public static final int SUCCESS_STATUS = 1;
	public static final String SUCCESS_CODE = "SUCCESS";
	public static final String SUCCESS_MESSAGE = "SUCCESS";

	public static final int EXCEPTION_STATUS = -1;
	public static final String EXCEPTION_CODE = "EXCEPTION";
	
	private int responseStatus;
	private String responseCode;
	private String message;
	private Object result;
	
	public LoggingResponse(int responseStatus, String responseCode, String message, Object result) {
		super();
		this.responseStatus = responseStatus;
		this.responseCode = responseCode;
		this.message = message;
		this.result = result;
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

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
