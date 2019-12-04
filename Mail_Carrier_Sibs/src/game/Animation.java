package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Point;

import javax.imageio.ImageIO;

public class Animation {
	public Point topCorner = new Point(0,0);
	public int frames;
	public int currentFrame = 0;
	public BufferedImage[] images;
	
	public Animation(String a, int frameX, int frameY) { //Takes Image and splits it into an array of images
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
			System.out.println(a);
			e.printStackTrace();

		}
	}
	
	public BufferedImage getImage() { //get the image of current frame
		return images[currentFrame];
	}
	
	public void nextFrame() { //increments the frame
		currentFrame++;
		if(currentFrame == frames) {
			currentFrame = 0;
		}
	}
}
