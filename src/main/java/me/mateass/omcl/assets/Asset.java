package me.mateass.omcl.assets;

import me.mateass.omcl.Downloadable;
import me.mateass.omcl.Util;

public class Asset extends Downloadable
{
	public Asset(String objectName) {
		super(Util.getAssetObjectUrlFromName(objectName), Util.getAssetObjectFileFromName(objectName));
	}
}
