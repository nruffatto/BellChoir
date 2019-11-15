package game;

import java.awt.Point;
import java.util.ArrayList;

public class Map {
	
	private Block[][] map;
	private ArrayList<Point> spawnPoints = new ArrayList<>();
	private int wid; //width
	private int hei; //height
	
	public Map(int width, int height) {
		map = new Block[height][width];
		wid = width;
		hei = height;
		spawnPoints.add(null);spawnPoints.add(null);spawnPoints.add(null);spawnPoints.add(null);spawnPoints.add(null);
		spawnPoints.add(null);spawnPoints.add(null);spawnPoints.add(null);spawnPoints.add(null);spawnPoints.add(null);
	}
	
	public void clear() {
		for(int i = 0; i < getWidth(); i ++) {
			for(int j = 0; j < getHeight(); j ++) {
				eraseBlock(i, j);
			}
		}
	}
	
	public void resize(int width, int height) {
		Block[][] newMap = new Block[height][width];
		for(int i = 0; i < Math.min(wid, width); i ++) {
			for(int j = 0; j < Math.min(hei, height); j ++) {
				newMap[j][i] = map[j][i];
			}
		}
		map = newMap;
		wid = width;
		hei = height;
	}
	
	public Map getCopy() {
		Map copy = new Map(wid, hei);

		for(int i = 0; i < wid; i ++) {
			for(int j = 0; j < hei; j ++) {
				copy.insertBlock(i, j, this.getBlock(i, j));
			}
		}
		return copy;
	}
	
	public Block getBlock(int x, int y) {
		return map[y][x];
	}
	
	public void insertBlock(int x, int y, Block block) {
		map[y][x] = block;
//		System.out.println("Map has new block at " + x + " " + y);
	}
	
	public void eraseBlock(int x, int y) {
		map[y][x] = null;
	}
	
	public void insertSpawnPoint(int x, int y, int i) {
		spawnPoints.remove(i);
		spawnPoints.add(i, new Point(x, y));
	}
	
	public Point getSpawnPoint(int i) {
		return spawnPoints.get(i);
	}
	
	public int getSpawnPointsSize() {
		return spawnPoints.size();
	}
	
	public int getWidth() {
		return wid;
	}
	
	public int getHeight() {
		return hei;
	}
	
	public boolean contains(Point p) {
		return p.x >= 0 && p.x < wid && p.y >= 0 && p.y < hei;
	}
	
	public boolean equals(Map comp) {
//		System.out.println("comparing maps!");
		boolean equal = true;
		if(wid != comp.getWidth() || hei != comp.getHeight()) {
			return false;
		}
		for(int i = 0; i < wid; i ++) {
			for(int j = 0; j < hei; j ++) {
				if(!(this.getBlock(i, j) == null && comp.getBlock(i, j) == null)) {
					if((this.getBlock(i, j) != null && comp.getBlock(i, j) == null) ||  
							(this.getBlock(i, j) == null && comp.getBlock(i, j) != null)) {
//						System.out.println("X1");
//						insertBlock(i, j, new Block("cobble.png"));
						return false;
					}else if(!this.getBlock(i, j).equals(comp.getBlock(i, j))) {
//						System.out.println("X2");
						return false;
					}
				}
			}
		}
		return true;
	}
	public String toString() {
		String mapString = "";
		for(int j = 0; j < hei; j ++) {
			for(int i = 0; i < wid; i ++) {
				if(getBlock(i, j) != null) {
					mapString += "X ";
				}else {
					mapString += "_ ";
				}
			}
			mapString += "\n";
		}
		return mapString;
	}
}
