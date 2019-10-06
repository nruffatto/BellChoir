package mapEditor;

import java.awt.Point;

public class Map {
	
	private Block[][] map;
	private int wid;
	private int hei;
	
	public Map(int width, int height) {
		map = new Block[height][width];
		wid = width;
		hei = height;
	}
	
	public void resize(int width, int height) {
		Block[][] newMap = new Block[height][width];
		for(int i = 0; i < wid; i ++) {
			for(int j = 0; j < hei; j ++) {
				newMap[j][i] = map[j][i];
			}
		}
		map = newMap;
		wid = width;
		hei = height;
	}
	
	public Block getBlock(int x, int y) {
		return map[y][x];
	}
	
	public void insertBlock(int x, int y, Block block) {
		map[y][x] = block;
//		System.out.println("Map has new block at " + x + " " + y);
	}
	
	public int getWidth() {
		return wid;
	}
	
	public int getHeight() {
		return hei;
	}
}
