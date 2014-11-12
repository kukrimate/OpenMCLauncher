package me.mateass.omcl.assets;

import me.mateass.omcl.Downloadable;
import me.mateass.omcl.Util;

public class AssetsIndex extends Downloadable
{
	private String name;
	
	public AssetsIndex(String versionName) {
		super(Util.getAssetsIndexUrlFromName(versionName), Util.getAssetsIndexFileFromName(versionName));
		name = versionName;
	}
	
	public String getName() {
		return name;
	}
}
