package me.mateass.omcl.versions;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.mateass.omcl.Constants;
import me.mateass.omcl.Util;

public class VersionManager
{
	private ArrayList<Version> versions;
	private int selectedVersion;
	private Gson gson;
	
	public VersionManager() 
	{
		gson = new Gson();
		fillVersions();
	}
	
	public Version getSelectedVersion() {
		return versions.get(selectedVersion);
	}
	
	public void setSelectedVersion(String version) {
		for (Version v : versions) {
			if(v.getVersionId().equals(version)) {
				selectedVersion = versions.indexOf(v);
				break;
			}
		}
	}
	
	public ArrayList<String> getVersionList() {
		ArrayList<String> str = new ArrayList<String>();
		for(Version ver : versions) {
			str.add(ver.getVersionId());
		}
		return str;
	}
	
	private void fillVersions() {
		versions = new ArrayList<Version>();
		String jsonString = Util.getUrlConetents(Util.urlFromString(Constants.jars.toString() + "versions" + "/" + "versions.json"));
		JsonObject json = gson.fromJson(jsonString, JsonObject.class);
		JsonArray versionsArray = json.get("versions").getAsJsonArray();
		for (JsonElement version : versionsArray) {
			JsonObject o = version.getAsJsonObject();
			versions.add(new Version(o.get("id").getAsString()));
		}
		selectedVersion = 0;
	}
}
