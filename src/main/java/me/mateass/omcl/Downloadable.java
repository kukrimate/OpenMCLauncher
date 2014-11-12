package me.mateass.omcl;

import java.io.File;
import java.net.URL;

import me.mateass.omcl.logger.Logger;

public class Downloadable 
{
	private URL url;
	private File localPath;
	private Logger log;
	
	public Downloadable(URL par1url, File par2path) 
	{
		url = par1url;
		localPath = par2path;
		log = new Logger();
	}
	
	public URL getUrl() {
		return url;
	}
	
	public File getPath() {
		return localPath;
	}
	
	public boolean isOnRemote() {
		return Util.checkUrl(url);
	}
	
	public boolean isFile() {
		return localPath.isFile();
	}
	
	public long download() {
		if (localPath.isFile()) {
			log.info("File already exists skiping: " + localPath.getName());
			return 0;
		}
		Long startTime = System.currentTimeMillis();
		Util.downloadFile(url.toString(), localPath);
		Long elapsed = System.currentTimeMillis() - startTime;
		log.info("Download complete: " + localPath.getName());
		return elapsed;
	}
}
