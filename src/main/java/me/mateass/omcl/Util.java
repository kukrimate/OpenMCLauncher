package me.mateass.omcl;

import java.awt.Desktop;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.List;
import java.util.UUID;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import me.mateass.omcl.logger.Logger;
import me.mateass.omcl.versions.Library;
import me.mateass.omcl.versions.Version;

public class Util 
{
	private static String dirName = "minecraftt";
	private static Logger log = new Logger();
	public static  HyperlinkListener linkListener = new HyperlinkListener() {
		    public void hyperlinkUpdate(HyperlinkEvent linkEvent) {
		      if (linkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
		        try {
		          Util.openLink(linkEvent.getURL().toURI());
		        } catch (Exception localException) {
		          localException.printStackTrace();
		        }
		    }
		  };
	
	public static void setLookAndFeel() {
		try {
			OS os = getOs();
			if(os == OS.WIN) {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} else if(os == OS.MAC) {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} else if(os == OS.LINUX) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			} else {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
		} catch(Exception e) {
			System.out.println("Exception happend during getting look and feel! Fallback mode! Exception: " + e.toString());
		}
	}

	public static String getCpSeparator() {
		if (getOs() == OS.WIN)
			return ";";
		return ":";
	}
	
	public static File getMcDirectory() 
	{
		String userHome = System.getProperty("user.home");
		String appData = System.getenv("APPDATA");
		File mcDir = new File(userHome + File.separator + "." + dirName + File.separator);
		OS os = getOs();
		
		if(os == OS.WIN) {
			mcDir = new File(appData + File.separator + "." + dirName + File.separator);
		} else if(os == OS.MAC) {
			mcDir = new File(userHome + File.separator + "Library" + File.separator + "Application Support" + File.separator + dirName + File.separator);
		} else if(os == OS.LINUX) {
			mcDir = new File(userHome + File.separator + "." + dirName + File.separator);
		} else {
			mcDir = new File(userHome + File.separator + "." + dirName + File.separator);
		}
		
		if(!mcDir.exists()) {
			mcDir.mkdirs();
		}
		
		return mcDir;
	}
	
	public static OS getOs() {
		String osName = System.getProperty("os.name").toLowerCase();
		
		if(osName.contains("win")) {
			return OS.WIN;
		} else if(osName.contains("mac")) {
			return OS.MAC;
		} else if(osName.contains("linux")) {
			return OS.LINUX;
		} else if(osName.contains("sun")) {
			return OS.SOLARIS;
		} else {
			return OS.UNKNOWN;
		}
	}
	
	public static void downloadFile(String url, File path) {
		File folder = new File(path.getParentFile() + File.separator);
		if(!folder.isDirectory()) {
			folder.mkdirs();
		}
		try {
			FileOutputStream fos = new FileOutputStream(path);
			BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
		    byte data[] = new byte[1024];
		    int count;
		    while((count = in.read(data,0,1024)) != -1)
		    {
		        fos.write(data, 0, count);
		    }
		    
		    fos.close();
		} catch(Exception e) {
			System.out.println("Exception downloading file: " + e.toString());
		}
	}
	
	public static void extractZip(File file, File path) {
		byte[] buffer = new byte[1024];
		 
	     try{
	    	 
	    	File folder = path;
	    	if(!folder.exists()){
	    		folder.mkdir();
	    	}
	 
	    	ZipInputStream zis = 
	    		new ZipInputStream(new FileInputStream(file));
	    	
	    	
	    	ZipEntry ze = zis.getNextEntry();
	    	
	 
	    	while(ze!=null){
	    		
		    	if(!ze.isDirectory())
		    	{
		    	   String fileName = ze.getName();
		           File newFile = new File(folder + File.separator + fileName);
		 		 
		            new File(newFile.getParent()).mkdirs();
		 
		            FileOutputStream fos = new FileOutputStream(newFile);             
		 
		            int len;
		            while ((len = zis.read(buffer)) > 0) {
		       		fos.write(buffer, 0, len);
		            }
		 
		            fos.close();   
		            ze = zis.getNextEntry();
		    	} else {
		    		File dir = new File(folder + File.separator + ze.getName() + File.separator);
		    		dir.mkdirs();
		    		ze = zis.getNextEntry();
		    	}
	    	}
	 
	        zis.closeEntry();
	    	zis.close();
	  
	    }catch(Exception e){
	    	System.out.println("Exception unziping: " + e.toString());
	    }
	}
	
	public static void openLink(URI uri) {
		try {
			 Desktop d = Desktop.getDesktop();
			 d.browse(uri);
		} catch(Exception e) {
			System.out.println("Exception opening link: " + e.toString());
		}
	}
	
	public static URL urlFromString(String url) {
		try {
			return new URL(url);
		} catch(Exception e) {
			System.out.println("URL converting exception: " + url);
			return null;
		}
	}
	
	public static URI uriFromString(String url) {
		try {
			return new URI(url);
		} catch(Exception e) {
			System.out.println("URI converting exception: " + url);
			return null;
		}
	}
	
	public static UUID genToken() {
		return UUID.randomUUID();
	}
	
	public static void writeLinesToFile(File file, String[] lines) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(file.toString(), "UTF-8");
			for (String line : lines) {
				writer.println(line);
			}
			writer.close();
		} catch (Exception e) {
			log.error("Error writing lines: " + e.toString());
		}
	}
	
	public static String[] readLinesFromFile(File file) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(file.toString()), Charset.forName("UTF-8"));
			return lines.toArray(new String[lines.size()]);
		} catch (Exception e) {
			log.error("Error reading lines: " + e.toString());
			return null;
		}
	}
	
	public static void createLauncherPropsFile() {
		File launcherProps = new File(getMcDirectory(), "properties");
		if(launcherProps.exists()) {
			return;
		}
		try {
			launcherProps.createNewFile();
		} catch (Exception e) {;
			log.error("Error creating props file: " + e.toString());
			System.exit(0);
		}
		String[] lines = {uuidToString(genToken()), "null"};
		writeLinesToFile(launcherProps, lines);
	}
	
	public static void writeAccessTokenToProps(String accessToken) {
		File launcherProps = new File(getMcDirectory(), "properties");
		if(!launcherProps.exists()) {
			createLauncherPropsFile();
		}
		String[] oldFile = readLinesFromFile(launcherProps);
		String clientToken = oldFile[0];
		String[] lines = {clientToken, accessToken};
		writeLinesToFile(launcherProps, lines);
	}
	
	public static String readAccessTokenFromProps() {
		File launcherProps = new File(getMcDirectory(), "properties");
		if(!launcherProps.exists()) {
			createLauncherPropsFile();
			return null;
		}
		String[] lines = readLinesFromFile(launcherProps);
		return lines[1];
	}
	
	public static String readClientTokenFromProps() {
		File launcherProps = new File(getMcDirectory(), "properties");
		if(!launcherProps.exists()) {
			createLauncherPropsFile();
		}
		String[] lines = readLinesFromFile(launcherProps);
		return lines[0];
	}
	
	public static String uuidToString(UUID uuid) {
		return uuid.toString().replaceAll("-", "");
	}
	
	public static boolean checkUrl(URL url) {
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("HEAD");
			return (conn.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static URL getUrlFromLibraryName(String par1name, boolean par2isNative) 
	{
		String[] split = par1name.split(":");
		String onlyPath = split[0];
		String name = split[1];
		String version = split[2];
		
		if(onlyPath.contains(".")) {
			onlyPath = onlyPath.replace(".", "/");
		}
		
		if (!par2isNative) {
			String filename = name + "-" + version + ".jar";
			return Util.urlFromString(Constants.libraries + onlyPath + "/" + name + "/" + version + "/" + filename);
		} else {
			String nativeName;
			Util.OS os = Util.getOs();
			if (os == Util.OS.WIN) {
				nativeName = "natives-windows";
				if(name.contains("twitch")) {
					String arch = System.getProperty("os.arch");
					if(arch.equals("x86")) {
						nativeName = nativeName + "-32";
					} else {
						nativeName = nativeName + "-64";
					}
				}
			} else if(os == Util.OS.MAC) {
				nativeName = "natives-osx";
			} else {
				nativeName = "natives-linux";
			}
			String filename = name + "-" + version + "-" + nativeName + ".jar";
			return Util.urlFromString(Constants.libraries + onlyPath + "/" + name + "/" + version + "/" + filename);
		}
	}
	
	public static File getFileFromLibraryName(String par1name, boolean par2isNative) 
	{
		String[] split = par1name.split(":");
		String onlyPath = split[0];
		String name = split[1];
		String version = split[2];
		
		if(onlyPath.contains(".")) {
			onlyPath = onlyPath.replace(".", File.separator);
		}
		
		if (!par2isNative) {
			String filename = name + "-" + version + ".jar";
			return new File(Util.getMcDirectory() + File.separator + "libraries" + File.separator + onlyPath + File.separator + name + File.separator + version + File.separator + filename);
		} else {
			String nativeName;
			Util.OS os = Util.getOs();
			if (os == Util.OS.WIN) {
				nativeName = "natives-windows";
				if(name.contains("twitch")) {
					String arch = System.getProperty("os.arch");
					if(arch.equals("x86")) {
						nativeName = nativeName + "-32";
					} else {
						nativeName = nativeName + "-64";
					}
				}
			} else if(os == Util.OS.MAC) {
				nativeName = "natives-osx";
			} else {
				nativeName = "natives-linux";
			}
			String filename = name + "-" + version + "-" + nativeName + ".jar";
			return new File(Util.getMcDirectory() + File.separator + "libraries" + File.separator + onlyPath + File.separator + name + File.separator + version + File.separator + filename);
		}
	}
	
	public static URL getUrlFromVersionName(String par1versionName, String par2ext) {
		return urlFromString(Constants.jars + "versions" + "/" + par1versionName + "/" + par1versionName + "." + par2ext);
	}
	
	public static File getFileFromVersionName(String par1versionName, String par2ext) {
		return new File(Util.getMcDirectory() + File.separator + "versions" + File.separator + par1versionName + File.separator + par1versionName + "." + par2ext);
	}
	
	public static String getUrlConetents(URL url) {

		try {	
	        BufferedReader in = new BufferedReader(
	        new InputStreamReader(url.openStream()));
	
	        String fullText = "";
	        
	        String inputLine;
	        while ((inputLine = in.readLine()) != null)
	        {
	        	fullText += inputLine;
	        }
	        
	        in.close();
	        
	        return fullText;
	        
		} catch(Exception e) { return  "exception: " + e.toString();}
	}
	
	public static URL getAssetsIndexUrlFromName(String versionName) {
		return urlFromString(Constants.jars + "indexes" + "/" + versionName + ".json");
	}
	
	public static File getAssetsIndexFileFromName(String versionName) {
		return new File(getMcDirectory() + File.separator + "assets" + File.separator + "indexes" + File.separator + versionName + ".json");
	}
	
	public static URL getAssetObjectUrlFromName(String object) {
		return urlFromString(Constants.resources + object.substring(0, 2) + "/" + object);
	}
	
	public static File getAssetObjectFileFromName(String object) {
		return new File(getMcDirectory() + File.separator + "assets" + File.separator + "objects" + File.separator + object.substring(0, 2) + File.separator + object);
	}
	
	public static BufferedImage flipImage(BufferedImage src) {
		 BufferedImage bufferedImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
			    AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			    tx.translate(-bufferedImage.getWidth(null), 0);
			    AffineTransformOp op = new AffineTransformOp(tx,
			        AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			    bufferedImage = op.filter(src, null);
			    return bufferedImage;
	}
	
	public static String getMainClassFromJson(String jsonString) {
		Gson gson = new Gson();
		JsonObject json = gson.fromJson(jsonString, JsonObject.class);
		return json.get("mainClass").getAsString();
	}
	
	public static String getArgsFromJson(String jsonString) {
		Gson gson = new Gson();
		JsonObject json = gson.fromJson(jsonString, JsonObject.class);
		return json.get("minecraftArguments").getAsString();
	}
	
	public static String arrayToString(String[] in) {
		String o = "";
		for(String str : in) {
			o += str;
		}
		return o;
	}
	
	public static String extractAndGetNatives(Version version) {
		String dirName = version.getVersionId() + "-natives-" + System.nanoTime();
		File nativesDir = new File(getMcDirectory() + File.separator + "versions" + File.separator + version.getVersionId() + File.separator + dirName + File.separator);
		nativesDir.mkdirs();
		System.out.println(nativesDir.toString());
		
		for(Library lib : version.getLibraries()) {
			if (lib.getIsNative()) {
				extractZip(lib.getPath(), nativesDir);
			}
		}
		
		return dirName;
	}
	
	public static void cleanAllNatives(Version ver, String dirName) {
		File nativesDir = new File(getMcDirectory() + File.separator + "versions" + File.separator + ver.getVersionId() + File.separator + dirName + File.separator);
		deleteFolder(nativesDir);
	}
	
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { 
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
		
	public enum OS {
		WIN,
		MAC,
		LINUX,
		SOLARIS,
		UNKNOWN;
	}
}
