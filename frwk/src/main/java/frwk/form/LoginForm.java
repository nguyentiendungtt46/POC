package frwk.form;

import java.io.Serializable;

import entity.frwk.SysUsers;

public class LoginForm extends ModelForm<SysUsers> implements Serializable {
	 private String user, pass, rsa, RSACode;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getRsa() {
		return rsa;
	}

	public void setRsa(String rsa) {
		this.rsa = rsa;
	}

	public String getRSACode() {
		return RSACode;
	}

	public void setRSACode(String rSACode) {
		RSACode = rSACode;
	}
	private SysUsers su;
	public SysUsers getSu() {
		return su;
	}

	public void setSu(SysUsers su) {
		this.su = su;
	}

	@Override
	public SysUsers getModel() {
		return su;
	}
	 
}
