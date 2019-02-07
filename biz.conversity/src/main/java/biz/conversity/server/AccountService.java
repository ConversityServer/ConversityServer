package biz.conversity.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<Object> verifyConnection(
			@RequestParam String userName, 
			@RequestParam String password
			) {
		System.out.println( userName + " attempting log in");
		if (verMap.get(userName) == null || !verMap.get(userName).equals(password)) throw new BadCredentialsException();
		Account account = accounts.get(userName);
		System.out.println( userName + " logged in");
		return new ResponseEntity<>(account, HttpStatus.OK);
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
		if (verMap.get(userName) == null || !verMap.get(userName).equals(password)) throw new BadCredentialsException();
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
			@RequestParam String userName, 
			@RequestParam String password,
			@PathVariable("id") String accountName
			) {
		if (verMap.get(userName) == null || !verMap.get(userName).equals(password)) throw new BadCredentialsException();
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
			@RequestParam String password,
			@RequestBody Account newAccount
			) {
		System.out.println("Account creation started by: "+userName);
		if (accounts.get(userName) != null) throw new AccountAlreadyExistsException();	// This account already exists
		
		verMap.put(userName, password);
		
		AccountType accountType = AccountType.USER;
		if (accounts.isEmpty()) {
			accountType = AccountType.ADMIN;
		}
		newAccount.setAccountType(accountType);
		accounts.put(userName, newAccount);
		
		fileManager.saveData(verMap, "verMap.ser");
		fileManager.saveData(accounts,  "accounts.ser");
		System.out.println(newAccount.getEmail() + " account created");
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
			@RequestParam String userName,
			@RequestParam String password,
			@PathVariable("id") String accountName
			) {
		if (verMap.get(userName) == null || !verMap.get(userName).equals(password)) throw new BadCredentialsException();
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
	
	@RequestMapping(value = "/accounts/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateAccount(
			@PathVariable("id") String userName,
			@RequestParam String password,
			@RequestBody Account account
			){
		if (verMap.get(userName) == null || !verMap.get(userName).equals(password)) throw new BadCredentialsException();
		accounts.put(userName, account);
		fileManager.saveData(accounts, "accounts.ser");
		return new ResponseEntity<>("Account updated", HttpStatus.OK);
	}
	/**
	 * Changes the password on an account.  Only the owner of the account can do this.
	 * @param sessionId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(value = "/accounts/{id}/password", method = RequestMethod.PUT)
	public ResponseEntity<Object> updatePassword(
			@PathVariable("id") String accountName, 
			@RequestParam String oldPassword, 
			@RequestParam String newPassword
			) {
		if (verMap.get(accountName) == null || !verMap.get(accountName).equals(oldPassword)) throw new BadCredentialsException();
		
		verMap.put(accountName, newPassword);
		return new ResponseEntity<>("Password updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/accounts/{account}/photos/{photoName}", method = RequestMethod.PUT)
	public ResponseEntity<Object> addPhoto(
			@PathVariable("account") String accountName,
			@RequestParam String password,
			@PathVariable("photoName") String photoName,
			@RequestBody String photoString
			) {
		if (verMap.get(accountName) == null || !verMap.get(accountName).equals(password)) throw new BadCredentialsException();
		Account account = accounts.get(accountName);
		// Check if a photo with the same name already exists
		if (account.getPhotos().containsKey(photoName)) {
			// If so delete it.  Assume user is updating photo
			account.getPhotos().remove(photoName);
		}
		// Add new photoString to HashMap
		account.getPhotos().put(photoName, photoString);
		
		return new ResponseEntity<>("Photo added", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/accounts/{targetAccount}/photos/{photoName}", method = RequestMethod.GET)
	public ResponseEntity<Object> getPhoto(
			@PathVariable("targetAccount") String targetAccount,
			@PathVariable("photoName") String photoName,
			@RequestParam String accountName,
			@RequestParam String password
			) {
		if (verMap.get(accountName) == null || !verMap.get(accountName).equals(password)) throw new BadCredentialsException();
		Account account = accounts.get(targetAccount);
		// Technically, HashMap.get() would return null anyway if the key doesn't exist, but
		// do it this way in case an exception needs to be thrown later
		String photoString = null;
		if (account.getPhotos().containsKey(photoName)) {
			photoString = account.getPhotos().get(photoName);
		}
		return new ResponseEntity<>(photoString, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/accounts/{account}/photos/{photoName}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deletePhoto(
			@PathVariable("account") String accountName,
			@RequestParam String password,
			@PathVariable("photoName") String photoName
			) {
		if (verMap.get(accountName) == null || !verMap.get(accountName).equals(password)) throw new BadCredentialsException();
		Account account = accounts.get(accountName);
		// Check if a photo with the name exists
		if (account.getPhotos().containsKey(photoName)) {
			// If so delete it.
			account.getPhotos().remove(photoName);
		}
		
		return new ResponseEntity<>("Photo Deleted", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/accounts/{account}/profilePic", method = RequestMethod.PUT)
	public ResponseEntity<Object> changeProfilePic(
			@PathVariable("account") String accountName,
			@RequestParam String password,
			@RequestBody String photoString
			) {
		if (verMap.get(accountName) == null || !verMap.get(accountName).equals(password)) throw new BadCredentialsException();
		Account account = accounts.get(accountName);
		
		account.setPicture(photoString);
		
		return new ResponseEntity<>("Profile Picture updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/accounts/{targetAccount}/profilePic", method = RequestMethod.GET)
	public ResponseEntity<Object> getProfilePic(
			@PathVariable("targetAccount") String targetAccount,
			@RequestParam String accountName,
			@RequestParam String password
			) {
		if (verMap.get(accountName) == null || !verMap.get(accountName).equals(password)) throw new BadCredentialsException();
		Account account = accounts.get(targetAccount);
		String profilePicString = account.getPicture();
		return new ResponseEntity<>(profilePicString, HttpStatus.OK);
	}
}
