package me.mateass.auth;

import java.util.ArrayList;
import java.util.UUID;

public class User 
{
	private UUID id;
	private ArrayList<ProfileProperty> properties;
	
	public User(UUID par1id, ArrayList<ProfileProperty> par2props) {
		id = par1id;
		properties = par2props;
	}
	
	public UUID getId() {
		return id;
	}
	
	public ArrayList<ProfileProperty> getProperties() {
		if (properties != null) {
			return properties;
		} else  {
			return new ArrayList<ProfileProperty>();	
		}
	}
}
