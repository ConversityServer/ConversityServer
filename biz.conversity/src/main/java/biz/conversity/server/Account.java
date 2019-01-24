package biz.conversity.server;

import java.io.Serializable;

public class Account implements Serializable{
	static final long serialVersionUID = 0;
	private String password;
	private Profile profile;
	
	public Account (String email, String password) {
		this.password = password;
		this.profile = new Profile(email);
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public Profile getProfile() {
		return this.profile;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	@Override
	public String toString() {
		return this.profile.toString();
	}
}
