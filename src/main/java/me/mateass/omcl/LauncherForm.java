package me.mateass.omcl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import me.mateass.auth.AuthClient;
import me.mateass.auth.exceptions.InvalidCredsException;
import me.mateass.auth.exceptions.ServerDownException;
import me.mateass.omcl.logger.Logger;
import me.mateass.omcl.skin.Skin;
import me.mateass.omcl.versions.Version;
import me.mateass.omcl.versions.VersionManager;

public class LauncherForm extends ImagePanel {

	/**
	 *  @author mateass
	 */
	private static final long serialVersionUID = 1L;
	private Logger log;
	private AuthClient auth;
	private JComboBox<String> versionSelector;
	private JLabel status;
	private VersionManager vManager;
	
	public LauncherForm() {
		this.setLayout(null);
		this.setBackground(Color.BLUE);
		LogoPanel logo = new LogoPanel();
		logo.setBounds(250, 25, 300, 100);
		logo.setOpaque(false);
		this.add(logo);
		
		log = new Logger();
		
		JPanel login = getLoginPanel();
		login.setBounds(Constants.defaultSize.width / 2 - login.getPreferredSize().width / 2, Constants.defaultSize.height / 2 - login.getPreferredSize().height / 2, login.getPreferredSize().width, login.getPreferredSize().height);
		this.add(login);
		vManager = new VersionManager();
		auth = new AuthClient("", "");
		if(auth.getIsToken() == true) {
		try  {
			auth.doLogin();
		} catch (InvalidCredsException e) {
			log.error(e.getMessage());
			auth = null;
			Util.writeAccessTokenToProps("null");
			return;
		} catch (ServerDownException e) {
			log.error(e.getMessage());
			auth = null;
			Util.writeAccessTokenToProps("null");
			return;
		}
		// Print login info (for Debugging)
		/*	log.info("Successfull login! accessToken: " + auth.getAccessToken());
			log.info("profileId: " + auth.getSelectedProfile().getId());
			log.info("user id: " + auth.getUser().getId());
			log.info("twitch_access_token: " + auth.getUser().getProperties().get(0).getValue());*/
			setLoggedIn();
		} else {
			auth = null;
		}
	}
	
	private JPanel getLoginPanel() {
		JPanel p = new JPanel();
		p.setLayout(null);

		p.setPreferredSize(new Dimension(400, 200));
		p.setBackground(new Color(0,0,0,80));
		
		JLabel usernameLabel = new JLabel("Username: ", 4);
		usernameLabel.setBounds(p.getPreferredSize().width / 2 - 50 - 57 - 20, p.getPreferredSize().height / 2 - 10 - 30, 57, 14);
		usernameLabel.setOpaque(false);
		usernameLabel.setForeground(Color.WHITE);
		
		JLabel passwordLabel = new JLabel("Password: ", 4);
		passwordLabel.setBounds(p.getPreferredSize().width / 2 - 50 - 57 - 20, p.getPreferredSize().height / 2 + 30 - 30, 57, 14);
		passwordLabel.setOpaque(false);
		passwordLabel.setForeground(Color.WHITE);
		
		final JTextField username = new JTextField();
		username.setBounds(p.getPreferredSize().width / 2 - 50 - 57 + 67 - 20, p.getPreferredSize().height / 2 - 10 - 40 + 3, 200, 30);
		username.setOpaque(true);
		
		final JPasswordField password = new JPasswordField();
		password.setBounds(p.getPreferredSize().width / 2 - 50 - 57 + 67 - 20, p.getPreferredSize().height / 2 - 10 + 3, 200, 30);
		password.setOpaque(true);
		
		JButton register = new JButton("Register");
		register.setBounds(100, 140, 100, 30);

		
		JButton login = new JButton("Login");
		login.setBounds(210, 140, 100, 30);
		
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doLogin(username.getText(), new String(password.getPassword()));
			}
		});
		
		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Util.openLink(Util.uriFromString(Constants.regLink));
			}
		});
		
		p.add(usernameLabel);
		p.add(passwordLabel);
		p.add(username);
		p.add(password);
		p.add(register);
		p.add(login);
		
		return p;
	}
	
	private JPanel getVersionSelector() {
		JPanel p = new JPanel();
		p.setLayout(null);
		p.setBorder(BorderFactory.createTitledBorder("Version Selection"));
		JLabel versionLabel = new JLabel("Version:", 0);
		versionLabel.setBounds(10, 25, 50, 10);
		p.add(versionLabel);
		versionSelector = new JComboBox<String>();
		for (String str :  vManager.getVersionList()) {
			versionSelector.addItem(str);
		}
		versionSelector.setBounds(65, 20, 160, 20);
		versionSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vManager.setSelectedVersion((String)versionSelector.getSelectedItem());
			}
		});
		
		p.add(versionSelector);
		JLabel statusLabel = new JLabel("Status:", 0);
		statusLabel.setBounds(10, 50, 50, 10);
		p.add(statusLabel);
		status = new JLabel("Ready to download", 0);
		status.setBounds(38, 45, 160, 20);
		status.setFont(new Font("Arial", Font.BOLD, 12));
		p.add(status);
		return p;
	}
	
	private JPanel getSkinPanel() {
		Skin skin = new Skin(auth.getSelectedProfile().getName());
		JLabel lblimage = new JLabel(new ImageIcon(skin.getSkin().getScaledInstance(160, 320, 0)));
		JPanel p = new JPanel();
		p.add(lblimage, "Center");
		p.setBorder(BorderFactory.createTitledBorder("Skin Viewer"));
		return p;
	}
	
	private JPanel getControlsPanel() {
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createTitledBorder("Buttons"));
		p.setLayout(null);
		JButton play = new JButton("Play");
		play.setBounds(15,25,100,35);
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				launchGame();
			}
		});
		JButton logout = new JButton("Logout");
		logout.setBounds(125,25,100,35);
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				auth.setLoggedOut();
				logOut();
			}
		});
		p.add(play);
		p.add(logout);
		return p;
	}
	
	private void launchGame() {
		Version v = vManager.getSelectedVersion();
		v.initVersion();
		status.setText("Downloading...");
		v.download(status);
		log.info("Strating version: " + v.getVersionId());
		GameLauncher l = new GameLauncher(v, auth.getAccessToken(), Util.uuidToString(auth.getSelectedProfile().getId()), auth.getUser().getProperties(), auth.getSelectedProfile().isLegacy(),auth.getSelectedProfile().getName(), v.getDownloader());
		l.start();
	}
	
	private void logOut() {
		this.removeAll();
		this.setLayout(null);
		this.setBackground(Color.BLUE);
		LogoPanel logo = new LogoPanel();
		logo.setBounds(250, 25, 300, 100);
		logo.setOpaque(false);
		this.add(logo);
		
		log = new Logger();
		
		JPanel login = getLoginPanel();
		login.setBounds(Constants.defaultSize.width / 2 - login.getPreferredSize().width / 2, Constants.defaultSize.height / 2 - login.getPreferredSize().height / 2, login.getPreferredSize().width, login.getPreferredSize().height);
		this.add(login);
		this.setImage(true);
		this.updateUI();
	}
	
	private void setLoggedIn() 
	{
		this.removeAll();
		this.setImage(false);
		JScrollPane news = getNewsPanel();
		news.setBounds(0, 0, Constants.defaultSize.width - 250, Constants.defaultSize.height - 27);
		this.add(news);
		JPanel p = getVersionSelector();
		p.setBounds(Constants.defaultSize.width - 245, 5, 237, 80);
		this.add(p);
		JPanel p2 = getSkinPanel();
		p2.setBounds(Constants.defaultSize.width - 245, 85, 237, 350);
		this.add(p2);
		JPanel p3 = getControlsPanel();
		p3.setBounds(Constants.defaultSize.width - 245, 490, 237, 80);
		this.add(p3);
		this.setOpaque(false);
		this.updateUI();
	}
	
	private void dispError(String error) {
		JOptionPane.showMessageDialog(null, error);
	}
	
	private void doLogin(String username, String password) {
		log.info("Logging In!");
		
		auth = new AuthClient(username, password);
		try {
			auth.doLogin();
		} catch (InvalidCredsException e) {
			log.error(e.getMessage());
			dispError(e.getMessage());
			return;
		} catch (ServerDownException e) {
			log.error(e.getMessage());
			dispError(e.getMessage());
			return;
		}
		
		// Print login info (for Debugging)
		/*	log.info("Successfull login! accessToken: " + auth.getAccessToken());
			log.info("profileId: " + auth.getSelectedProfile().getId());
			log.info("user id: " + auth.getUser().getId());
			log.info("twitch_access_token: " + auth.getUser().getProperties().get(0).getValue());*/
		setLoggedIn();
	}

	private JScrollPane getNewsPanel() {
		JEditorPane jep = new JEditorPane();
		jep.setEditable(false);   
		
		try {
		  jep.setPage(Constants.blogUrl);
		}catch (Exception e) {
		  jep.setContentType("text/html");
		  jep.setText("<html>Could not load</html>");
		} 
		
		jep.addHyperlinkListener(Util.linkListener);

		JScrollPane scrollPane = new JScrollPane(jep);
		return scrollPane;
	}
}
