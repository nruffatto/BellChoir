package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animation {
	public int xCoord;
	public int yCoord;
	public int currentState = 0;
	public int scale = 10;
	
	//this is a placeholder
	boolean isFacingLeft;
	
	public void Animate(String a, Graphics g, Movable p)
	{
		String[] splitFile = a.split("_");
		int frames = Integer.parseInt(splitFile[1]);
		currentState=(currentState + 1)%frames;
		BufferedImage img;
		try {
			img = ImageIO.read(new File(a));
			
			if(!isFacingLeft)
			{
			g.drawImage(img, p.rec.x, p.rec.y, p.rec.x + scale, p.rec.y + scale,
					currentState*img.getWidth()/frames, 0, currentState*img.getWidth()/frames + img.getWidth()/frames, img.getHeight(), null);
			}
			else if(isFacingLeft)
			{
				g.drawImage(img, p.rec.x, p.rec.y, p.rec.x + scale, p.rec.y + scale,
						currentState*img.getWidth()/frames + img.getWidth()/frames, 0, currentState*img.getWidth()/frames, img.getHeight(), null);
			}
				
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
