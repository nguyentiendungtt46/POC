package cic.h2h.form;

import entity.VOauthSession;
import frwk.form.SearchForm;

public class VOauthSessionForm extends SearchForm<VOauthSession> {
	private String  userName;
	VOauthSession vOauthSession  = new VOauthSession();
	
	@Override
	public VOauthSession getModel() {
		
		return vOauthSession;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public VOauthSession getvOauthSession() {
		return vOauthSession;
	}

	public void setvOauthSession(VOauthSession vOauthSession) {
		this.vOauthSession = vOauthSession;
	}
	
} 
