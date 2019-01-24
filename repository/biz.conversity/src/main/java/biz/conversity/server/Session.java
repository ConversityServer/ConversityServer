package biz.conversity.server;

import java.util.Date;

public class Session {
	private final long sessionId;
	private final Account account;
	
	public Session(Account account) {
		this.sessionId = new Date().getTime();
		this.account = account;
	}
	
	public Account GetAccount() {
		return account;
	}
	
	public long GetSessionId() {
		return sessionId;
	}
}
