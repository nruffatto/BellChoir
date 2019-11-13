package game;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Dog extends Movable{
	
	private int speed = 11;
	private int jumpingSpeed = 25;
	private int vision = 7;

	public Dog(int x, int y) {
		super(x, y);
		HITBOX_WIDTH = 150;
		HITBOX_RATIO = 2.2 / 3.0;
		IMAGE_SCALE = (double) HITBOX_WIDTH / 150;
		startPoint = new Point((int)(38 * IMAGE_SCALE),(int)(7 * IMAGE_SCALE));
		IMAGE_WIDTH = (int)(209 * IMAGE_SCALE);// * IMAGE_SCALE
		IMAGE_HEIGHT = (int)(122 * IMAGE_SCALE);
		rec = new Rectangle(x, y, HITBOX_WIDTH, (int)(HITBOX_WIDTH * HITBOX_RATIO));
	}

	@Override
	public void update() {
		updateProperties();
		super.update();
	}

	private void updateProperties() {
		if (inRange(game.players[0]) && game.players[0].isCrouched){
			move(game.players[0]);
		}else if (inRange(game.players[1]) && game.players[1].isCrouched) {
			move(game.players[1]);
		}else if (inRange(game.packages[0])) {
			move(game.packages[0]);
		}else {
			velX = 0;
		}	
	}
	
	private void move(Movable m) {
		double distanceX = m.rec.getCenterX() - this.rec.getCenterX();
		if (distanceX > 0) {
			velX = speed;
		}else if (distanceX < 0) {
			velX = -speed;
		}else {
			velX = 0;
		}
	}
	
	private boolean inRange(Movable m) {
		double distanceX = Math.abs(m.rec.getCenterX() - this.rec.getCenterX());
		if(distanceX <= vision * game.screen.startingLength) {
			return true;
		} else {
			return false;
		}
		
	}
	
	@Override
	protected void checkCollisionX() {
		Point[] points = getPoints();
		Point[] pastPoints = getPastPoints();
		for(int i = 0; i < points.length; i ++) {
			if(game.map.contains(new Point(points[i].x / Screen.startingLength, points[i].y / Screen.startingLength))) {
				if(game.map.getBlock(points[i].x / Screen.startingLength, points[i].y / Screen.startingLength) != null) {
					if(points[i].x > pastPoints[i].x) {
						rec.x -= points[i].x % Screen.startingLength + 1;
						if(!isInAir) {
							velY = -jumpingSpeed;
						}
//						System.out.println("X1");
					}else if(points[i].x < pastPoints[i].x){
						rec.x += Screen.startingLength - points[i].x % Screen.startingLength + 7;
						if(!isInAir) {
							velY = -jumpingSpeed;
						}
//						System.out.println("X2");
					}
					velX = 0;
					break;
				}
			}
		}
	}
	
	@Override
	public Image getImage() {
		File imageFile = new File("Sprites/dog.png");
		BufferedImage img;
		try {
			img = ImageIO.read(imageFile);
			return img;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean isDog() {
		return true;
	}
}
