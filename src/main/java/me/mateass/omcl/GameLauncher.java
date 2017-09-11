package me.mateass.omcl;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;

import me.mateass.auth.ProfileProperty;
import me.mateass.omcl.logger.Logger;
import me.mateass.omcl.versions.Library;
import me.mateass.omcl.versions.Version;

public class GameLauncher extends Thread 
{
	private Version version;
	private String accessToken;
	private String profileId;
	private ArrayList<ProfileProperty> profileProperties;
	private Process game;
	private boolean isLegacy;
	private String username;
	private Logger log;
	private Thread downloader;
	
	public GameLauncher(Version par1version, String par2accessToken, String par3profileId, ArrayList<ProfileProperty> par4profileProperties, boolean par5isLegacy, String par6username, Thread par7downloader) {
		version = par1version;
		accessToken = par2accessToken;
		profileId = par3profileId;
		profileProperties = par4profileProperties;
		isLegacy = par5isLegacy;
		username = par6username;
		downloader = par7downloader;
		log = new Logger();
	}
	
	@Override
	public void run() {
		try {
			downloader.join();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		String nativesDir = Util.extractAndGetNatives(version);
		ArrayList<String> args = new ArrayList<String>();
		args.add("java");
		args.add("-Xmx1G");
		args.add("-Djava.library.path=" + Util.getMcDirectory() + File.separator + "versions" + File.separator + version.getVersionId() + File.separator + nativesDir + File.separator);
		args.add("-cp");
		String libraries = "";
		for (Library lib : version.getLibraries()) {
			libraries += lib.getPath().toString() + Util.getCpSeparator();
		}
		libraries += version.getJar().getPath().toString();
		args.add(libraries);
		String mainClass = Util.getMainClassFromJson(Util.arrayToString(Util.readLinesFromFile(version.getJson().getPath())));
		args.add(mainClass);
		
		String clientArgs = Util.getArgsFromJson(Util.arrayToString(Util.readLinesFromFile(version.getJson().getPath())));
		String userType = isLegacy == true ? "LEGACY" : "MOJANG";
		String mcDir = Util.getMcDirectory().toString();
		String assetsDir = new File(mcDir + File.separator + "assets" + File.separator).toString();
		
		String strProps = "";
		
		strProps = buildLegacyProperties(profileProperties);
		
		clientArgs = replaceArgs(clientArgs, username, version.getVersionId(), version.getIndex().getName(), profileId, accessToken, strProps, userType);
		String[] args2 = clientArgs.split("\\s+");
		for(String str : args2) {
			if(str.equals("mcdir")) {
				args.add(mcDir);
			} else if(str.equals("assetsdir")) {
				args.add(assetsDir);
			} else {
				args.add(str);
			}
		}
		
		String test = "";
		for (String arg : args) {
			test += arg + ' ';
		}
		
		System.out.println(test);
		
		ProcessBuilder builder = new ProcessBuilder(args);
		
		builder.directory(Util.getMcDirectory());
		
		builder.redirectOutput(Redirect.INHERIT);
		builder.redirectError(Redirect.INHERIT);
		
		try {	
			game = builder.start();
			game.waitFor();
			log.info("Game exited with exit code: " + game.exitValue());
			Util.cleanAllNatives(version, nativesDir);
		} catch (Exception e) {
			System.out.println("Exception starting game: " + e.toString());
		}
	}
	
	public String replaceArgs(String in, String username, String version, String assetIndex, String uuid, String accessToken, String userProperties, String userType) {
		String o = in;
		o = o.replace("$", "");
		o = o.replace("{", "");
		o = o.replace("}", "");
		o = o.replaceAll("auth_player_name", username);
		o = o.replaceAll("version_name", version);
		o = o.replaceAll("game_directory", "mcdir");
		o = o.replaceAll("assets_root", "assetsdir");
		o = o.replaceAll("assets_index_name", assetIndex);
		o = o.replaceAll("auth_uuid", uuid);
		o = o.replaceAll("auth_access_token", accessToken);
		o = o.replaceAll("user_properties", userProperties);
		o = o.replaceAll("user_type", userType);
		o = o.replaceAll("auth_session", uuid);
		
		return o;
	}
	
	public String buildLegacyProperties(ArrayList<ProfileProperty> in) {
		ProfileProperty twitchToken;
		try {
			twitchToken  = in.get(0);
			return "{\"twitch_access_token\":[\"" + twitchToken.getValue() +"\"]}";
		} catch(Exception e) {
			return "{}";
		}

	}
	
	public Process getGameProcess() {
		return game;
	}	
}
