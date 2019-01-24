package biz.conversity.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import biz.conversity.server.exceptions.AccountAlreadyExistsException;
import biz.conversity.server.exceptions.AccountNotFoundException;
import biz.conversity.server.exceptions.BadCredentialsException;
import biz.conversity.server.exceptions.UnauthorizedRequestException;

@RestController
public class AccountService {
	private static FileManager fileManager = FileManager.getInstance();
	private static Map<String, String> verMap;
	private static Map<String, Account> accounts;
	
	/**
	 * Constructor
	 */
	public AccountService() {
		verMap = (HashMap<String, String>)fileManager.loadData("verMap.ser");
		if (verMap == null) {
			verMap = new HashMap<String, String>();
			fileManager.saveData(verMap, "verMap.ser");
		}
		
		accounts = (HashMap<String, Account>)fileManager.loadData("accounts.ser");
		if (accounts == null) {
			accounts = new HashMap<String, Account>();
			fileManager.saveData(accounts, "accounts.ser");
		}
	}
	
	/**
	 * Method to check for connection to server
	 * @return true
	 */
	@RequestMapping(value = "/connection", method = RequestMethod.GET)
	public ResponseEntity<Object> verifyConnection()
	{
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	/**
	 * Requests the entire list of users.  Requires Admin account.
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllAccounts(
			@RequestParam String userName, 
			@RequestParam String password
			) {
		if (verMap.get(userName) != password) throw new BadCredentialsException();
		Account requester = accounts.get(userName);
		if (requester.getAccountType() != AccountType.ADMIN) throw new UnauthorizedRequestException();
		return new ResponseEntity<>(accounts.values(), HttpStatus.OK);
	}

	/**
	 * Requests a specified user account
	 * @param accountName
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getAccount(
			@PathVariable("id") String accountName,
			@RequestParam String userName, 
			@RequestParam String password
			) {
		if (verMap.get(userName) != password) throw new BadCredentialsException();
		Account account = accounts.get(accountName);
		if (account == null) throw new AccountNotFoundException();
		return new ResponseEntity<>(account, HttpStatus.OK);
	}
	
	/**
	 * Creates a new Account
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/accounts", method = RequestMethod.POST)
	public ResponseEntity<Object> createAccount(
			@RequestParam String userName, 
			@RequestParam String password
			) {
		// User names and email address are the same at this point, but the userName is
		// encrypted (once implemented).  Dont forget to decrypt the user name before
		// making an email address out of it.
		if (accounts.get(userName) != null) throw new AccountAlreadyExistsException();	// This account already exists
		
		String email = userName.toLowerCase();
		verMap.put(userName, password);
		
		AccountType accountType = AccountType.USER;
		if (accounts.isEmpty()) {
			accountType = AccountType.ADMIN;
		}
		Account newAccount = new Account(userName, email, accountType);
		accounts.put(userName, newAccount);
		
		fileManager.saveData(verMap, "verMap.ser");
		fileManager.saveData(accounts,  "accounts.ser");
			
		return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
	}
	
	/**
	 * Deletes an account.  The requester must be the same as the account, or an Admin
	 * @param accountName
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/accounts/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> RemoveAccount(
			@PathVariable("id") String accountName,
			@RequestParam String userName,
			@RequestParam String password
			) {
		if (verMap.get(userName) != password) throw new BadCredentialsException();
		Account requester = accounts.get(userName);
		if (
				requester != accounts.get(accountName) || 
				requester.getAccountType() != AccountType.ADMIN
				) throw new UnauthorizedRequestException();
		
		verMap.remove(accountName);
		accounts.remove(accountName);
		fileManager.saveData(accounts, "accounts.ser");
		return new ResponseEntity<>("The Account has been deleted", HttpStatus.OK);
	}
	
	/**
	 * Changes the password on an account.  Only the owner of the account can do this.
	 * @param sessionId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(value = "/accounts/{id}/password", method = RequestMethod.PUT)
	public ResponseEntity<Object> UpdatePassword(
			@PathVariable("id") String accountName, 
			@RequestParam String oldPassword, 
			@RequestParam String newPassword
			) {
		if (verMap.get(accountName) != oldPassword) throw new BadCredentialsException();
		
		verMap.put(accountName, newPassword);
		return new ResponseEntity<>("Password updated", HttpStatus.OK);
	}
}
