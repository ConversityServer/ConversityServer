package biz.conversity.server;

import java.io.Serializable;

public class Account implements Serializable{
	static final long serialVersionUID = 1L;
	private final String userName;
	private final String email;
	private String firstName;
	private String lastName;
	private String major;
	private String gradClass;
	private AccountType accountType;
	//private BufferedImage profilePicture;
	//private BufferedImage backdropPicture;
	//private final List<BufferedImage> pictures;
	
	public Account (String userName, String email, AccountType accountType) {
		this.userName = userName;	// Yes I know userName and email are the same, but
		this.email = email;			// eventually, there might be some hashing to mask userName
		this.accountType = accountType;
	}
	
	public String getUserName() {
		return this.userName;
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
	
	@Override
	public String toString() {
		return lastName+firstName+":"+email;
	}
}
