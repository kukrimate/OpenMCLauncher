package me.mateass.omcl.versions;

import me.mateass.omcl.Downloadable;
import me.mateass.omcl.Util;

public class Library extends Downloadable
{	
	private boolean isNative = false;
	
	public Library(String par1name, boolean par2isNative) 
	{
		super(Util.getUrlFromLibraryName(par1name, par2isNative), Util.getFileFromLibraryName(par1name, par2isNative));
		isNative = par2isNative;
	}
	
	public boolean getIsNative() {
		return isNative;
	}
}
