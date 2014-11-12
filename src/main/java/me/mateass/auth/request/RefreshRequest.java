package me.mateass.auth.request;

@SuppressWarnings("unused")
public class RefreshRequest 
{
	private String clientToken;
	private String accessToken;
	private boolean requestUser = true;
	
	public RefreshRequest(String par1clientToken, String par2accessToken) {
		clientToken = par1clientToken;
		accessToken = par2accessToken;
	}
}
