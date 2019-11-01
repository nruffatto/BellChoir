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
	
	protected double accX;
	protected double accY;
	protected double velX;
	protected double velY;
	public Rectangle rec;
	public Rectangle pastRec;
	
	public boolean isVisible = true;
	
	protected Game game;
	
	protected boolean isInAir;
	protected String[] playerImages = new String[4];
	protected String PackageImage = "Sprites/package.png";
	public Movable(int x, int y) {
		rec = new Rectangle(x, y, HITBOX_WIDTH, (int)(HITBOX_WIDTH * HITBOX_RATIO));//(int)(HITBOX_WIDTH * HITBOX_RATIO)
		accY = 2;
		
		playerImages[0] = "Sprites/mailman1.png";
		playerImages[1] = "Sprites/mailman2.png";
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
		rec.x += velX;
	}
	
	private void moveY() {
		velY += accY;
		rec.y += velY;
	}
	
	private void checkCollisionX() {
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
	
	public boolean isTouching(Movable m) {
		boolean isTouching = false;
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
		Point[] newPoints = {
				new Point(rec.x, rec.y),
				new Point(rec.x + rec.width, rec.y),
				new Point(rec.x + rec.width, rec.y + rec.height / 2),
				new Point(rec.x + rec.width, rec.y + rec.height),
				new Point(rec.x, rec.y+ rec.height),
				new Point(rec.x, rec.y + rec.height / 2),};
		return newPoints;
	}
	
	public Point[] getPastPoints() {
		Point[] newPoints = {
				new Point(pastRec.x, pastRec.y),
				new Point(pastRec.x + pastRec.width, pastRec.y),
				new Point(pastRec.x + pastRec.width, pastRec.y + pastRec.height / 2),
				new Point(pastRec.x + rec.width, pastRec.y + pastRec.height),
				new Point(pastRec.x, pastRec.y+ pastRec.height),
				new Point(pastRec.x, pastRec.y + pastRec.height / 2),};
		return newPoints;
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
}
