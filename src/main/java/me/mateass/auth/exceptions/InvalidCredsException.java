package me.mateass.auth.exceptions;

public class InvalidCredsException extends Exception
{
	/**
	 * @author mateass
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCredsException() {}
	
	public InvalidCredsException(String msg) {
		super(msg);
	}
	
	public InvalidCredsException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public InvalidCredsException(Throwable cause) {
		super(cause);
	}
}
