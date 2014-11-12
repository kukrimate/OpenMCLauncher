package me.mateass.omcl.versions;

import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.JLabel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.mateass.omcl.Downloadable;
import me.mateass.omcl.Util;
import me.mateass.omcl.assets.Asset;
import me.mateass.omcl.assets.AssetsIndex;

public class Version extends ArrayList<Downloadable> implements Runnable
{
	/**
	 * @author mateass
	 */
	private static final long serialVersionUID = 1L;
	private String versionName;
	private ArrayList<Library> libraries;
	private ArrayList<Asset> assets;
	private AssetsIndex index;
	private Gson gson;
	private Thread downloader;
	private JLabel status;
	
	public Version(String par1versionName) {
		versionName = par1versionName;
		gson = new Gson();
		this.add(new Downloadable(Util.getUrlFromVersionName(versionName, "json"), Util.getFileFromVersionName(versionName, "json")));
		this.add(new Downloadable(Util.getUrlFromVersionName(versionName, "jar"), Util.getFileFromVersionName(versionName, "jar")));
	}
	
	public void run() {
		getJson().download();
		getJar().download();
		index.download();
		downloadLibraries();
		downloadAssets();
		status.setText("Game running..");
	}
	
	public AssetsIndex getIndex() {
		return index;
	}
	
	public void initVersion() 
	{
		fillLibraries();
		index = new AssetsIndex(getAssetsIndexName());
		fillAssets();
	}
	
	public Downloadable getJson() {
		return this.get(0);
	}
	
	public Downloadable getJar() {
		return this.get(1);
	}
	
	public ArrayList<Library> getLibraries() {
		return libraries;
	}
	
	private void fillLibraries() {
		libraries = new ArrayList<Library>();
		String jsonString;
		if (!getJson().isFile()) {
			jsonString = Util.getUrlConetents(getJson().getUrl());
		} else {
			jsonString = Util.arrayToString(Util.readLinesFromFile(getJson().getPath()));
		}
		JsonObject json = gson.fromJson(jsonString, JsonObject.class);
		JsonArray jsonlibraries = json.get("libraries").getAsJsonArray();
		for (JsonElement library : jsonlibraries) {
			boolean isNative = false;
			if(library.getAsJsonObject().get("natives") != null) {
				isNative = true;
			}
			
			//rule check
			
			boolean isAdd = true;
			
			if(library.getAsJsonObject().get("rules") != null) {
				JsonArray rules = library.getAsJsonObject().get("rules").getAsJsonArray();
				for(JsonElement rule : rules) {
					JsonObject o = rule.getAsJsonObject();
					
					String currentOs = "";
					if(Util.getOs().equals(Util.OS.WIN)) {
						currentOs = "windows";
					} else if(Util.getOs().equals(Util.OS.MAC)) {
						currentOs = "osx";
					} else {
						currentOs = "linux";
					}
					
					if (o.get("action").getAsString().equals("disallow") && o.get("os").getAsJsonObject().get("name").getAsString().equals(currentOs)) {
						isAdd = false;
					}
					
					if (o.get("action").getAsString().equals("allow") && o.get("os") != null && !o.get("os").getAsJsonObject().get("name").getAsString().equals(currentOs)) {
						isAdd = false;
					}
				}
			}
			
			//end rule check
			
			if(isAdd)
			{
				libraries.add(new Library(library.getAsJsonObject().get("name").getAsString(), isNative));
			}
		}
	}
	
	private void downloadLibraries() {
		for(Library lib : libraries) {
			lib.download();
		}
	}
	
	private String getAssetsIndexName() {
		String jsonString = Util.getUrlConetents(getJson().getUrl());
		JsonObject json = gson.fromJson(jsonString, JsonObject.class);
		if(json.get("assets") != null) {
			return json.get("assets").getAsString();
		}
		return "legacy";
	}
	
	private void fillAssets() {
		assets = new ArrayList<Asset>();
		String jsonString;
		if (!index.isFile()) {
			jsonString = Util.getUrlConetents(index.getUrl());
		} else {
			jsonString = Util.arrayToString(Util.readLinesFromFile(index.getPath()));
		}
		JsonObject json = gson.fromJson(jsonString, JsonObject.class);
		JsonObject objects = json.get("objects").getAsJsonObject();
		for (Entry<String, JsonElement> entry : objects.entrySet()) {
			JsonObject asset = entry.getValue().getAsJsonObject();
			String hash = asset.get("hash").getAsString();
			assets.add(new Asset(hash));
		}
	}
	
	private void downloadAssets() {
		for(Asset asset : assets) {
			asset.download();
		}
	}
	
	public void download(JLabel par1status) {
		status = par1status;
		downloader = new Thread(this);
		downloader.start();
	}
	
	public Thread getDownloader() {
		return downloader;
	}
	
	public String getVersionId() {
		return versionName;
	}

}
