package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Point;

import javax.imageio.ImageIO;

public class Animation {
	public int frameX = 138;
	public int frameY = 135;
	public Point topCorner = new Point(0,0);
	public int frames;
	public int currentFrame = 0;
	public BufferedImage[] images;
	
	public Animation(String a) {
		String[] splitFile = a.split("_");
		frames = Integer.parseInt(splitFile[1]);
		images = new BufferedImage[frames];
		BufferedImage img;
		try {
			img = ImageIO.read(new File(a));
			for(int i = 0; i < frames; i++) {
				images[i] = img.getSubimage(topCorner.x, topCorner.y, frameX, frameY);
				topCorner.x += frameX;
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(a);
			e.printStackTrace();

		}
	}
	
	public BufferedImage  getImage() {
		return images[currentFrame];
	}
	
	public void nextFrame() {
		currentFrame++;
		if(currentFrame == frames) {
			currentFrame = 0;
		}
	}
}
