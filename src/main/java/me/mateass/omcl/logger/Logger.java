package me.mateass.omcl.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	
	private String prefix_info = "[INFO] ";
	private String prefix_warning = "[WARN] ";
	private String prefix_error = "[ERROR] ";
	
	public Logger() {
		
	}
	
	public void info(String input) {
		System.out.println(prefix_info + getDateAndTime() + ": " + input);
	}
	
	public void warning(String input) {
		System.out.println(prefix_warning + getDateAndTime() + ": " + input);
	}
	
	public void error(String input) {
		System.out.println(prefix_error + getDateAndTime() + ": " + input);
	}
	
	private String getDateAndTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
}
