/**
 * 
 */
package vn.com.cmc.dto;

/**
 * @author nvtiep
 *
 *         created: Dec 26, 2019 9:35:40 AM
 */
public class Oauth2ExceptionModel {
	private String error;
	private String error_description;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

}
