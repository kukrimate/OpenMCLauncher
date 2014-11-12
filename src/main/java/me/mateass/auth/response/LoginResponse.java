package me.mateass.auth.response;

import me.mateass.auth.McProfile;
import me.mateass.auth.User;

public class LoginResponse extends Response 
{
	private String accessToken;
	private String clientToken;
	private McProfile selectedProfile;
	private McProfile[] availableProfiles;
	private User user;
	  
	public LoginResponse(String par1accessToken, String par2clientToken, McProfile par3selectedProfile, McProfile[] par4availableProfiles, User par5user) {
		accessToken = par1accessToken;
		clientToken = par2clientToken;
		selectedProfile = par3selectedProfile;
		availableProfiles = par4availableProfiles;
		user = par5user;
	}
	
	 public String getAccessToken()
	  {
	    return this.accessToken;
	  }
	  
	  public String getClientToken()
	  {
	    return this.clientToken;
	  }
	  
	  public McProfile[] getAvailableProfiles()
	  {
	    return this.availableProfiles;
	  }
	  
	  public McProfile getSelectedProfile()
	  {
	    return this.selectedProfile;
	  }
	  
	  public User getUser()
	  {
	    return this.user;
	  }
}
