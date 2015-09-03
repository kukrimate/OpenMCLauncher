package me.mateass.omcl;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class ImagePanel extends JPanel
{
  private static final long serialVersionUID = 1L;
  private Image img;
  private Image bgImage;
 private boolean image = true;
  
  public ImagePanel()
  {
    setOpaque(true);
    try
    {
      this.bgImage = ImageIO.read(LauncherForm.class.getResource("/omcl/assets/bg.png")).getScaledInstance(32, 32, 16);
    } catch (Exception e) {
     	System.out.println("Missing bg.png: " + e.toString());
     	System.exit(0);
    }
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }
  
  public void setImage(boolean image) {
	  this.image = image;
  }

  public void paintComponent(Graphics paramGraphics)
  {
	 if(image) {
    int i = getWidth() / 2 + 1;
    int j = getHeight() / 2 + 1;
    if ((this.img == null) || (this.img.getWidth(null) != i) || (this.img.getHeight(null) != j)) {
      this.img = createImage(i, j);

      Graphics localGraphics = this.img.getGraphics();
      int m;
      for (int k = 0; k <= i / 32; k++) {
        for (m = 0; m <= j / 32; m++)
          localGraphics.drawImage(this.bgImage, k * 32, m * 32, null);
      }
      if ((localGraphics instanceof Graphics2D)) {
        Graphics2D localGraphics2D = (Graphics2D)localGraphics;
        m = 1;
        localGraphics2D.setPaint(new GradientPaint(new Point2D.Float(0.0F, 0.0F), new Color(553648127, true), new Point2D.Float(0.0F, m), new Color(0, true)));
        localGraphics2D.fillRect(0, 0, i, m);

        m = j;
        localGraphics2D.setPaint(new GradientPaint(new Point2D.Float(0.0F, 0.0F), new Color(0, true), new Point2D.Float(0.0F, m), new Color(1610612736, true)));
        localGraphics2D.fillRect(0, 0, i, m);
      }
      localGraphics.dispose();
    }
    paramGraphics.drawImage(this.img, 0, 0, i * 2, j * 2, null);
	 }
	}
}