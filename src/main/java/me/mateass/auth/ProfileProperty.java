package me.mateass.auth;

public class ProfileProperty 
{
	private final String name;
	private final String value;
	
	public ProfileProperty(String par1name, String par2value) {
		name = par1name;
		value = par2value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
}
