package me.mateass.omcl;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class LogoPanel extends JPanel
{

	/**
	 * @author mateass
	 */
	private static final long serialVersionUID = 1L;
    private BufferedImage image;

    public LogoPanel() {
       try {                
          image = ImageIO.read(LauncherForm.class.getResource("/omcl/assets/logo.png"));
       } catch (Exception ex) {
            System.out.println("Missing logo: " + ex.toString());
            System.exit(0);
       }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);            
    }
}
