package me.mateass.auth;

import java.net.URL;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.mateass.auth.exceptions.InvalidCredsException;
import me.mateass.auth.exceptions.ServerDownException;
import me.mateass.auth.request.LoginRequest;
import me.mateass.auth.request.RefreshRequest;
import me.mateass.auth.response.LoginResponse;
import me.mateass.auth.response.RefreshResponse;
import me.mateass.auth.response.Response;
import me.mateass.omcl.Constants;
import me.mateass.omcl.logger.Logger;
import me.mateass.omcl.Util;

public class AuthClient extends AuthExec
{
	private Gson gson;
	private Logger log;
	private AuthAgent agent = new AuthAgent("Minecraft", 1);
	private String username;
	private String password;
	private String accessToken;
	private String clientToken;
	private McProfile selectedProfile;
	private User user;
	private boolean isToken = false;
	
	public AuthClient(String par1username, String par2password) 
	{
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(UUID.class, new IDTypeAdapter());
		gson = builder.create();
		log = new Logger();
		log.info("AuthClient Init");
		username = par1username;
		password = par2password;
		clientToken = Util.readClientTokenFromProps();
		accessToken = Util.readAccessTokenFromProps().equals("null") ? null : Util.readAccessTokenFromProps();
		if(accessToken != null) {
			isToken = true;
		}
	}
		
	public boolean getIsToken() {
		return isToken;
	}
	
	public void doLogin() throws InvalidCredsException, ServerDownException {
		if(isToken) {
			loginWithToken();
		} else if(username != null && username != "" && password != null && password != "") {
			loginWithUsername();
		} else {
			try {
				throw new InvalidCredsException("Invalid Username or Password!");
			} catch (InvalidCredsException e) {
				log.error("Exception logging in: " + e.toString());
			}
			return;
		}
	}

	private void loginWithUsername() throws ServerDownException, InvalidCredsException {
		log.info("Logging in with username!");
		LoginRequest request = new LoginRequest(agent, username, password, clientToken);
		LoginResponse response = null;
		try {
			response = sendRequest(Constants.login_url, request, LoginResponse.class);
			if (response.getErrorValues().get("error") != null) {
				throw new InvalidCredsException(response.getErrorValues().get("errorMessage"));
			} else {
				accessToken = response.getAccessToken();
				Util.writeAccessTokenToProps(accessToken);
				isToken = true;
				selectedProfile = response.getSelectedProfile();
				user = response.getUser();
			}
		} catch(Exception e) {
			if(e instanceof InvalidCredsException) {
				throw new InvalidCredsException(response.getErrorValues().get("errorMessage"));
			}
			throw new ServerDownException("Servers are down right now! Check Your Connection!");
		}
	}
	
	private void loginWithToken() throws InvalidCredsException, ServerDownException {
		log.info("Logging in with token!");
		RefreshRequest request = new RefreshRequest(clientToken, accessToken);
		//System.out.println(gson.toJson(request));
		RefreshResponse response = null;
		try {
			response = sendRequest(Constants.refresh_url, request, RefreshResponse.class);
			if(response.getErrorValues().get("error") != null) {
				throw new InvalidCredsException(response.getErrorValues().get("errorMessage"));
			} else {
				accessToken = response.getAccessToken();
				Util.writeAccessTokenToProps(accessToken);
				isToken = true;
				selectedProfile = response.getSelectedProfile();
				user = response.getUser();
			} 
		} catch(Exception e) {
			if(e instanceof InvalidCredsException) {
				throw new InvalidCredsException(response.getErrorValues().get("errorMessage"));
			}
			throw new ServerDownException("Servers are down right now! Check Your Connection!");
		}
	}
	
	public McProfile getSelectedProfile() {
		return selectedProfile;
	}
	
	public User getUser() {
		return user;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	private <T extends Response> T sendRequest(URL url, Object input, Class<T> classOfT) throws Exception
	{
		String result = sendPost(url, gson.toJson(input), "application/json");
		//System.out.println(result);
		T res = this.gson.fromJson(result, classOfT); 
		if (res == null) {
			return null;
		}
		
		return res;
		
	}

	public void setLoggedOut() {
		Util.writeAccessTokenToProps("null");
		this.user = null;
		this.selectedProfile = null;
		this.accessToken = null;
		this.isToken = false;
		this.username = null;
		this.password = null;
	}

}