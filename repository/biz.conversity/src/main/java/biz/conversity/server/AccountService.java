package biz.conversity.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountService {
	private static AccountManager accountManager = AccountManager.getInstance();
	private final Map<String, Account> sessions = new HashMap<String, Account>();
	
	@RequestMapping(value = "/connection", method = RequestMethod.GET)
	public ResponseEntity<Object> verifyConnection()
	{
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/account", method = RequestMethod.POST)
	public ResponseEntity<Object> createAccount(
			@RequestParam String userName, 
			@RequestParam String password
			) {
		// User names are based on an email address, but at this point, the userName is
		// encrypted (once implemented).  Dont forget to decrypt the user name before
		// making an email address out of it.
		String email = userName.toLowerCase() + "@uah.edu";
		if (accountManager.createAccount(userName, email, password)) {
			return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Account already exists", HttpStatus.CONFLICT);
	}
	
	@RequestMapping(value = "/account/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> RemoveAccount(
			@PathVariable("id") String sessionId,
			@RequestParam String userName, 
			@RequestParam String password
			) {
		if (accountManager.removeAccount(userName, password)) {
			return new ResponseEntity<>("The Account has been deleted", HttpStatus.OK);
		}
		return new ResponseEntity<>("That Account could not be found", HttpStatus.CONFLICT);
	}
	
	@RequestMapping(value = "/account/{id}", method = RequestMethod.PUT)
	public boolean UpdateAccount(
			@RequestParam String sessionId, 
			@RequestParam String oldPassword, 
			@RequestParam String newPassword
			) {
		Account account = sessions.get(sessionId);
		if (account != null && account.getPassword() == oldPassword) {
			account.setPassword(newPassword);
			return true;
		}
		return false;
	}
	
	@RequestMapping(value = "/session", method = RequestMethod.GET)
	public String LogIn(
			@RequestParam(value="userName") String userName, 
			@RequestParam(value="password") String password
			) {
		Account account = accountManager.validateAccount(userName, password);
		if ( account != null) {
			String sessionId = "" + new Date().getTime();
			sessions.put(sessionId, account);
			System.out.println(account.toString() + " has logged in.");
			return sessionId;
		}
		return null;
	}
	
	@RequestMapping(value = "/session", method = RequestMethod.DELETE)
	public boolean LogOut(
			@RequestParam String sessionId
			) {
		if (sessions.containsKey(sessionId)) {
			System.out.println( sessions.get(sessionId).toString() + " has logged out.");
			sessions.remove(sessionId);
			return true;
		}
		return false;
	}
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	@ResponseBody
	public Profile GetProfile(
			@RequestParam String userName
			) {
		return accountManager.getProfile(userName);
	}
	
	@RequestMapping(value = "/profile", method = RequestMethod.PUT)
	public void PutProfile(
			@RequestParam String sessionId, 
			@RequestParam Profile profile
			) {
		Account account = sessions.get(sessionId);
		if (account != null) {
			account.setProfile(profile);
		}
	}
}
