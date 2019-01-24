package biz.conversity.server;

public class InvalidAccountException extends RuntimeException{
	static final long serialVersionUID = 1;
	public InvalidAccountException(String message) {
		super(message);
	}
}
