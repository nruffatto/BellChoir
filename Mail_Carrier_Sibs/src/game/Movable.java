package game;

import java.awt.Point;
import java.awt.Rectangle;

public class Movable {
	
	
	private double accX;
	private double accY;
	private double velX;
	private double velY;
	public Rectangle rec;
	public Rectangle pastRec;
	
	private Game game;
	
	private boolean isInAir;
	private String imageName;
	public Movable(int x, int y) {
		rec = new Rectangle(x, y, 64, 64);
	}
	
	public Point[] getPoints() {
		Point[] newPoints = {
				new Point(rec.x, rec.y),
				new Point(rec.x + rec.width, rec.y),
				new Point(rec.x + rec.width, rec.y + rec.height),
				new Point(rec.x, rec.y+ rec.height),};
		return newPoints;
	}
	
	public Point[] getPastPoints() {
		Point[] newPoints = {
				new Point(pastRec.x, pastRec.y),
				new Point(pastRec.x + pastRec.width, pastRec.y),
				new Point(pastRec.x + rec.width, pastRec.y + pastRec.height),
				new Point(pastRec.x, pastRec.y+ pastRec.height),};
		return newPoints;
	}
	
	private void updatePastRec() {
		pastRec = new Rectangle(rec.x, rec.y, rec.width, rec.height);
	}
}
