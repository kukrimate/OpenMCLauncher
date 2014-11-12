package me.mateass.omcl;

import javax.swing.JFrame;

public class LauncherWindow extends JFrame {

	/**
	 * 	@author mateass
	 */
	private static final long serialVersionUID = 1L;
	public static LauncherWindow frame;
	public LauncherForm lForm = new LauncherForm();
	
	public LauncherWindow() {
		super(Constants.windowTitle);
		this.setPreferredSize(Constants.defaultSize);
		this.setResizable(Constants.resizable);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		lForm.setPreferredSize(Constants.defaultSize);
		this.pack();
		this.add(lForm);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		Util.createLauncherPropsFile();
		Util.setLookAndFeel();
		frame = new LauncherWindow();
	}
}
