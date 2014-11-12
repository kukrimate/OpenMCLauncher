package me.mateass.omcl;

import java.awt.Dimension;
import java.net.URL;

public class Constants {
	public static String windowTitle = "Open MC Launcher v0.1";
	public static Dimension defaultSize = new Dimension(800, 600);
	public static boolean resizable = false;
	public static String blogUrl = "http://mcupdate.tumblr.com/";
	public static String regLink = "http://minecraft.net/register";
	public static URL login_url = Util.urlFromString("https://authserver.mojang.com/authenticate");
	public static URL refresh_url = Util.urlFromString("https://authserver.mojang.com/refresh");
	public static URL resources = Util.urlFromString("http://resources.download.minecraft.net/");
	public static URL libraries = Util.urlFromString("https://libraries.minecraft.net/");
	public static URL jars = Util.urlFromString("https://s3.amazonaws.com/Minecraft.Download/");
	public static URL skin = Util.urlFromString("http://skins.minecraft.net/MinecraftSkins/");
}
