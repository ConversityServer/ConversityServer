package biz.conversity.server;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

public class AccountManager {
	private static AccountManager instance;
	private final Map<String, Account> accounts;
	
	@SuppressWarnings("unchecked")
	private AccountManager() {
		File accountFile = new File("accounts.ser");
		if (accountFile.exists()) {
			System.out.println("Loading Account List...");
			accounts = (HashMap<String, Account>)FileManager.GetInstance().LoadData("accounts.ser");
		} 
		else {
			System.out.println("Creating new Account List...");
			accounts = new HashMap<String, Account>();
		}
	}
	
	public static AccountManager getInstance() {
		if (instance == null) {
			instance = new AccountManager();
		}
		return instance;
	}
	
	public boolean createAccount(String userName, String email, String password){
		if (accounts.containsKey(userName)) {
			return false;
		}
		System.out.println("Creating New Account..");
		Account newAccount = new Account(email, password);
		accounts.put(userName, newAccount);
		FileManager.GetInstance().SaveData(accounts, "accounts.ser");
		return true;
	}
	
	public boolean removeAccount(String userName, String password) {
		Account targetAccount = accounts.get(userName);
		if (targetAccount.getPassword() == password) {
			accounts.remove(userName);
			return true;
		}
		return false;		
	}
	
	public Account validateAccount(String userName, String password) {
		
		Account targetAccount = accounts.get(userName);
		if (targetAccount != null && targetAccount.getPassword().compareTo(password)==0) {
			return targetAccount;
		}
		return null;
	}
	
	public Profile getProfile(String userName) {
		Account account = accounts.get(userName);
		if (account != null) {
			return account.getProfile();
		}
		return null;
	}
}
