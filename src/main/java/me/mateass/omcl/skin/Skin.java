package me.mateass.omcl.skin;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import me.mateass.omcl.Constants;
import me.mateass.omcl.Util;
import me.mateass.omcl.logger.Logger;

public class Skin 
{
	private BufferedImage skinImage;
	private BufferedImage head;
	private BufferedImage body;
	private BufferedImage leftArm;
	private BufferedImage rightArm;
	private BufferedImage leftLeg;
	private BufferedImage rightLeg;
	private Logger log;
	
	public Skin(String username)
	{
		log = new Logger();
		BufferedImage image = null;
		try {
		    URL url = new URL(Constants.skin + username + ".png");
		    url = url.openConnection().getURL();
		    image = ImageIO.read(url);
		} catch (Exception e) {
			log.error("Error reading skin: " + e.toString());
		}
		head = image.getSubimage(8, 8, 8, 8);
		body = image.getSubimage(20, 20, 8, 12);
		leftArm = image.getSubimage(44, 20, 4, 12);
		leftLeg = image.getSubimage(4, 20, 4, 12);
		rightArm = Util.flipImage(leftArm);
		rightLeg = Util.flipImage(leftLeg);
		skinImage = new BufferedImage(16, 32, BufferedImage.TYPE_INT_ARGB);
		skinImage.getGraphics().drawImage(head, 4, 0, 8, 8, null);
		skinImage.getGraphics().drawImage(body, 4, 8, 8, 12, null);
		skinImage.getGraphics().drawImage(leftArm, 0, 8, 4, 12, null);
		skinImage.getGraphics().drawImage(rightArm, 12, 8, 4, 12, null);
		skinImage.getGraphics().drawImage(leftLeg, 4, 20, 4, 12, null);
		skinImage.getGraphics().drawImage(rightLeg, 8, 20, 4, 12, null);
	}
	
	public BufferedImage getSkin() {
		return skinImage;
	}
}
