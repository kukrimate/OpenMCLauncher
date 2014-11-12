package me.mateass.auth;

import java.util.ArrayList;
import java.util.UUID;

public class McProfile 
{
	private UUID id;
	private String name;
	private ArrayList<ProfileProperty> properties;
	private boolean legacy;
	
	public McProfile(UUID par1id, String par2name) {
		id = par1id;
		name = par2name;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void fillProperties(ArrayList<ProfileProperty> props) {
		properties = props;
	}
	
	public ArrayList<ProfileProperty> getProperties() {
		return properties;
	}
	
	public boolean isLegacy() {
		return legacy;
	}
}
