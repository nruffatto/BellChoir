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
	private int dogState;
	
	private static final int SIT_INDEX = 0;
	private static final int RUN_INDEX = 1;
	private static final int DEFAULT_INDEX = 2;
	
	private Animation[] dogImages = {
			new Animation("Sprites/dogsit_8_.png",209,134),
			new Animation("Sprites/dogrun_7_.png",209,134),
			new Animation("Sprites/dog_1_.png",209,122)
	};

	public Dog(int x, int y, int size) {
		super(x, y);
		HITBOX_WIDTH = size;
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
		if (inRange(game.packages[0]) && game.packages[0].holder != null) { 
			// So that the dog goes after the player when he has the package
			move(game.packages[0]);
		}else if (inRange(game.players[0]) && game.players[0].isCrouched){
			move(game.players[0]);
		}else if (inRange(game.players[1]) && game.players[1].isCrouched) {
			move(game.players[1]);
		}else if (inRange(game.packages[0])) {
			move(game.packages[0]);
		}else {
			dogState = SIT_INDEX;
			velX = 0;
		}	
		
		switch(dogState) {
			case 0: dogImages[SIT_INDEX].nextFrame(); break;
			case 1: dogImages[RUN_INDEX].nextFrame(); break;
			default: break;
		}
	}
	
	private void move(Movable m) {
		double distanceX = m.rec.getCenterX() - this.rec.getCenterX();
		dogState = RUN_INDEX;
		if (distanceX > 5) {
			isFacingLeft = false;
			velX = speed;
		}else if (distanceX < -5) {
			isFacingLeft = true;
			velX = -speed;
		}else {
			velX = 0;
			dogState = SIT_INDEX;
		}
	}
	
	private boolean inRange(Movable m) {
		double distanceX = Math.abs(m.rec.getCenterX() - this.rec.getCenterX());
		double distanceY = Math.abs(m.rec.getCenterY() - this.rec.getCenterY());
		if(distanceY <= vision * game.screen.startingLength && distanceX <= vision * game.screen.startingLength) {
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
		switch(dogState) {
		case 0: return dogImages[SIT_INDEX].getImage();
		case 1: return dogImages[RUN_INDEX].getImage();
		default: return dogImages[DEFAULT_INDEX].getImage();
	}
	}
	
	@Override
	public boolean isDog() {
		return true;
	}
}
