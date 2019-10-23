package game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Movable {
	
	
	protected double accX;
	protected double accY;
	protected double velX;
	protected double velY;
	public Rectangle rec;
	public Rectangle pastRec;
	
	private Game game;
	
	protected boolean isInAir;
	private String imageName;
	public Movable(int x, int y) {
		rec = new Rectangle(x, y, 64, 64);
		accY = 5;
		
		imageName = "mailman1.png";
		updatePastRec();
	}
	
	public void addGame(Game g) {
		game = g;
	}
	
	public void update() {
		moveX();
		checkCollisionX();
		updatePastRec();
		moveY();
		checkCollisionY();
		updatePastRec();
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
						break;
					}else {
						rec.x += Screen.startingLength - points[i].x % Screen.startingLength + 1;
						break;
					}
				}
			}
		}
	}
	
	private void checkCollisionY() {
		isInAir = true;
		Point[] points = getPoints();
		Point[] pastPoints = getPastPoints();
		for(int i = 0; i < points.length; i ++) {
			if(game.map.contains(new Point(points[i].x / Screen.startingLength, points[i].y / Screen.startingLength))) {
				if(game.map.getBlock(points[i].x / Screen.startingLength, points[i].y / Screen.startingLength) != null) {
					velY = 0;
					if(points[i].y > pastPoints[i].y) {
						rec.y -= points[i].y % Screen.startingLength + 1;
						isInAir = false;
						break;
					}else if(points[i].y < pastPoints[i].y) {
						rec.y += Screen.startingLength - points[i].y % Screen.startingLength + 1;
						break;
					}
				}
			}
		}
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
		pastRec = new Rectangle(rec.x, rec.y, rec.width, rec.height);
	}
}
