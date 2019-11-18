package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Movable { // 38, 6, 69, 129   image: 138, 135
	
	public int HITBOX_WIDTH = 50;
	public double HITBOX_RATIO = 127.0 / 60.0;
	public double IMAGE_SCALE = HITBOX_WIDTH / 60.0;
	public Point startPoint = new Point((int)(38 * IMAGE_SCALE),(int)(7 * IMAGE_SCALE));
	public int IMAGE_WIDTH = (int)(138 * IMAGE_SCALE);// * IMAGE_SCALE
	public int IMAGE_HEIGHT = (int)(135 * IMAGE_SCALE);
	
	public boolean isVisible = true;
	
	protected double accX;
	protected double accY;
	protected double velX;
	protected double velY;
	private int maxVelX = 100;
	private int maxVelY = 100;
	public Rectangle rec;
	public Rectangle pastRec;
	
	protected Game game;
	
	protected boolean isInAir = false;
	protected boolean wasInAir = false;
	protected boolean isFacingLeft = false;
	protected String PackageImage = "Sprites/package.png";
	public boolean hasPackage = false;
	
	public Movable(int x, int y) {
		rec = new Rectangle(x, y, HITBOX_WIDTH, (int)(HITBOX_WIDTH * HITBOX_RATIO));//(int)(HITBOX_WIDTH * HITBOX_RATIO)
		accY = 2;
		updatePastRec();
	}
	
	public void addGame(Game g) {
		game = g;
	}
	
	public void update() {
		updatePastRec();
		moveX();
		checkCollisionX();
		updatePastRec();
		moveY();
		checkCollisionY();
	}
	
	private void moveX() {
		velX += accX;
		if(velX > maxVelX) {
			velX = maxVelX;
		}else if(velX < -maxVelX) {
			velX = -maxVelX;
		}
		rec.x += velX;
	}
	
	private void moveY() {
		velY += accY;
		if(velY > maxVelY) {
			velY = maxVelY;
		}else if(velY < -maxVelY) {
			velY = -maxVelY;
		}
		rec.y += velY;
	}
	
	protected void checkCollisionX() {
		Point[] points = getPoints();
		Point[] pastPoints = getPastPoints();
		for(int i = 0; i < points.length; i ++) {
			if(game.map.contains(new Point(points[i].x / Screen.startingLength, points[i].y / Screen.startingLength))) {
				if(game.map.getBlock(points[i].x / Screen.startingLength, points[i].y / Screen.startingLength) != null) {
					if(points[i].x > pastPoints[i].x) {
						rec.x -= points[i].x % Screen.startingLength + 1;
//						System.out.println("X1");
					}else if(points[i].x < pastPoints[i].x){
						rec.x += Screen.startingLength - points[i].x % Screen.startingLength + 7;
//						System.out.println("X2");
					}
					velX = 0;
					break;
				}
			}
		}
	}
	
	protected void checkCollisionY() {
		isInAir = true;
		Point[] points = getPoints();
		Point[] pastPoints = getPastPoints();
		for(int i = 0; i < points.length; i ++) {
			if(game.map.contains(new Point(points[i].x / Screen.startingLength, points[i].y / Screen.startingLength))) {
				if(game.map.getBlock(points[i].x / Screen.startingLength, points[i].y / Screen.startingLength) != null) {
					if(points[i].y > pastPoints[i].y) {//points[i].y > pastPoints[i].y
						rec.y -= points[i].y % Screen.startingLength + 1;
						isInAir = false;
					}else if(points[i].y < pastPoints[i].y){//if(points[i].y < pastPoints[i].y)
						rec.y += Screen.startingLength - points[i].y % Screen.startingLength + 1;
//						System.out.println("2");
					}else {
//						System.out.println("same");
					}
					velY = 0;
					velX = 0;
					break;
				}
			}
		}
	}
	
	public boolean isTouching(Movable m) {
		boolean isTouching = false;
		if(m == null) {
			return false;
		}
		Point[] points1 = getPoints();
		Point[] points2 = m.getPoints();
		for(int i = 0; i < points1.length; i ++) {
			if(m.rec.contains(points1[i])){
				isTouching = true;
				break;
			}
		}
		for(int i = 0; i < points1.length; i ++) {
			if(m.rec.contains(points1[i])){
				isTouching = true;
				break;
			}
		}
		return isTouching;
	}
	
	public Point[] getPoints() {
		return getP(rec);
	}
	
	private Point[] getP(Rectangle r) {
		int n = 3;
		Point[] newPoints = new Point[n * 4];
		int counter = 0;
		for(int i = 0; i < n; i ++) {
			newPoints[counter] = new Point(r.x + i * (r.width / n), r.y);
			counter ++;
			newPoints[counter] = new Point(r.x + r.width, r.y + i * (r.height / n));
			counter ++;
			newPoints[counter] = new Point(r.x + r.width - i * (r.width / n), r.y + r.height);
			counter ++;
			newPoints[counter] = new Point(r.x, r.y + r.height - i * (r.height / n));
			counter ++;
		}
//		Point[] newPoints = {
//				new Point(r.x, r.y),
//				new Point(r.x + r.width, r.y),
//				new Point(r.x + r.width, r.y + r.height),
//				new Point(r.x, r.y+ r.height),
//				new Point(r.x, r.y + r.height / 2),
//				new Point(r.x + r.width, r.y + r.height / 2),
//				new Point(r.x + r.width / 2, r.y + r.height),
//				new Point(r.x + r.width / 2, r.y),};
		return newPoints;
	}
	
	public Point[] getPastPoints() {
		return getP(pastRec);
	}
	
	private void updatePastRec() {
		pastRec = (Rectangle) rec.clone();
//		pastRec = new Rectangle(rec.x, rec.y, rec.width, rec.height);
	}
	
	public Image getImage() {
		File imageFile = new File("Sprites/error.png");
		BufferedImage img;
		try {
			img = ImageIO.read(imageFile);
			return img;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isPlayer() {
		return false;
	}
	
	public boolean isPackage() {
		return false;
	}
	
	public boolean isDog() {
		return false;
	}
	
	public boolean isMailbox() {
		return false;
	}
}
