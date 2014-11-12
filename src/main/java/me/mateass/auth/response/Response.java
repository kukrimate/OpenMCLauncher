package me.mateass.auth.response;

import java.util.HashMap;

public class Response 
{
	private String error;
	private String errorMessage;
	private String cause;
		
	public HashMap<String, String> getErrorValues() {
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("error", error);
		values.put("errorMessage", errorMessage);
		values.put("cause", cause);
		return values;
	}
}
