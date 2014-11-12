package me.mateass.auth.request;

import me.mateass.auth.AuthAgent;

@SuppressWarnings("unused")
public class LoginRequest 
{
	private AuthAgent agent;
	private String username;
	private String password;
	private String clientToken;
	private boolean requestUser = true;
	
	public LoginRequest(AuthAgent par1agent, String par2username, String par3password, String par4clientToken)
	{
		agent = par1agent;
		username = par2username;
		password = par3password;
		clientToken = par4clientToken;
	}
}
