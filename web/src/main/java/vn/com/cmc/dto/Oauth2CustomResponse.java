package vn.com.cmc.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Oauth2CustomResponse {
	private String access_token;
	private Integer token_expires_in;
	private String error_code;
	private String error_description;
	private Integer password_expire_in;
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public Integer getToken_expires_in() {
		return token_expires_in;
	}
	public void setToken_expires_in(Integer token_expires_in) {
		this.token_expires_in = token_expires_in;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getError_description() {
		return error_description;
	}
	public void setError_description(String error_description) {
		this.error_description = error_description;
	}
	public Integer getPassword_expire_in() {
		return password_expire_in;
	}
	public void setPassword_expire_in(Integer password_expire_in) {
		this.password_expire_in = password_expire_in;
	}
	
}
