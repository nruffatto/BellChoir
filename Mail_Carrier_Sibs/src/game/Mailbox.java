package game;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class mailbox extends Movable{

	public mailbox(int x, int y) {
		super(x, y);
		HITBOX_WIDTH = 50;
		HITBOX_RATIO = 127.0 / 60.0;
		IMAGE_SCALE = HITBOX_WIDTH / 60.0;
		startPoint = new Point((int)(38 * IMAGE_SCALE),(int)(7 * IMAGE_SCALE));
		IMAGE_WIDTH = (int)(138 * IMAGE_SCALE);// * IMAGE_SCALE
		IMAGE_HEIGHT = (int)(135 * IMAGE_SCALE);
		rec = new Rectangle(x, y, HITBOX_WIDTH, (int)(HITBOX_WIDTH * HITBOX_RATIO));
	}

	@Override
	public Image getImage() {
		File imageFile = new File("Sprites/mailbox.png");
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
