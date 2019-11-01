package game;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Package extends Movable {
	
//	public static final int HITBOX_WIDTH = 69;
//	public static final double HITBOX_RATIO = 56.0 / 69.0;
//	public static final double IMAGE_SCALE = HITBOX_WIDTH / 60.0;
//	public Point startPoint = new Point((int)(0),(int)(0));
//	public static final int IMAGE_WIDTH = (int)(69 * IMAGE_SCALE);// * IMAGE_SCALE
//	public static final int IMAGE_HEIGHT = (int)(56 * IMAGE_SCALE);
	
	public Movable holder;
	private Movable lastHolder;

	public Package(int x, int y) {
		super(x, y);
		HITBOX_WIDTH = 69;
		HITBOX_RATIO = 56.0 / 69.0;
		IMAGE_SCALE = HITBOX_WIDTH / 60.0;
		startPoint = new Point((int)(0),(int)(0));
		IMAGE_WIDTH = (int)(69 * IMAGE_SCALE);// * IMAGE_SCALE
		IMAGE_HEIGHT = (int)(56 * IMAGE_SCALE);
		rec = new Rectangle(x, y, HITBOX_WIDTH, (int)(HITBOX_WIDTH * HITBOX_RATIO));
	}
	
	public void setHolder(Movable m) {
		if(m != lastHolder) {
			holder = m;
			lastHolder = m;
		}
	}
	
	public void removeHolder() {
		holder = null;
	}
	
	public void getThrown(int x, int y) {
		velX = x;
		velY = y;
		removeHolder();
	}
	
	@Override
	public void update() {
		if(holder != null) {
			rec.setLocation(holder.rec.x, holder.rec.y);
		}else {
			super.update();
		}
	}
	
	@Override
	protected void checkCollisionY() {
		isInAir = true;
		Point[] points = getPoints();
		Point[] pastPoints = getPastPoints();
		for(int i = 0; i < points.length; i ++) {
			if(game.map.contains(new Point(points[i].x / Screen.startingLength, points[i].y / Screen.startingLength))) {
				if(game.map.getBlock(points[i].x / Screen.startingLength, points[i].y / Screen.startingLength) != null) {
					lastHolder = null;
					if(points[i].y > pastPoints[i].y) {//points[i].y > pastPoints[i].y
						rec.y -= points[i].y % Screen.startingLength + 1;
						isInAir = false;
//						System.out.println(points[i].y % Screen.startingLength + 1);
					}else if(points[i].y < pastPoints[i].y){//if(points[i].y < pastPoints[i].y)
						rec.y += Screen.startingLength - points[i].y % Screen.startingLength + 1;
//						System.out.println("2");
					}else {
//						System.out.println("same");
					}
					velY = 0;
					break;
				}
			}
		}
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
