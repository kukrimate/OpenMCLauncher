package me.mateass.auth.exceptions;

public class ServerDownException extends Exception 
{
	/**
	 * @author mateass
	 */
	private static final long serialVersionUID = 1L;

	public ServerDownException() {}
	
	public ServerDownException(String msg) {
		super(msg);
	}
	
	public ServerDownException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ServerDownException(Throwable cause) {
		super(cause);
	}
}
