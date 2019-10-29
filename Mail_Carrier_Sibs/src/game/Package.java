package game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Package extends Movable {

	public Package(int x, int y) {
		super(x, y);
	}
	
	@Override
	public Image getImage() {
		File imageFile = new File("Sprites/package.png");
		BufferedImage img;
		try {
			img = ImageIO.read(imageFile);
			return img;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
