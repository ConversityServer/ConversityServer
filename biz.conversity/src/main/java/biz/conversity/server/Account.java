package biz.conversity.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Account implements Serializable{
	static final long serialVersionUID = 1L;
	private final String userName;
	private final String email;
	private String firstName;
	private String lastName;
	private String major;
	private String gradClass;
	private AccountType accountType;
	private String picture;
	private String backdrop;
	private final Map<String, String> photos;
	
	public Account (String userName, String email, AccountType accountType) {
		this.userName = userName;	// Yes I know userName and email are the same, but
		this.email = email;			// eventually, there might be some hashing to mask userName
		this.accountType = accountType;
		this.photos = new HashMap<>();
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public String getMajor() {
		return this.major;
	}
	
	public String getGradClass() {
		return this.gradClass;
	}
	
	public AccountType getAccountType() {
		return this.accountType;
	}
	
	public String getPicture() {
		return this.picture;
	}
	
	public String getBackdrop() {
		return this.backdrop;
	}
	
	public Map<String, String> getPhotos() {
		return this.photos;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setMajor(String major) {
		this.major = major;
	}
	
	public void setGradClass(String gradClass) {
		this.gradClass = gradClass;
	}
	
	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public void setBackdrop(String backdrop) {
		this.backdrop = backdrop;
	}
	
	@Override
	public String toString() {
		return lastName+", "+firstName+":"+email;
	}
}
