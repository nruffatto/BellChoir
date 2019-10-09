package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Screen extends JPanel{
	
	
	//Zoom Levels
	public static final int MAX_BLOCK_SIZE = 512; 
	public static final int MIN_BLOCK_SIZE = 8;
	
	//information for how to draw the block. depends on image.
	public static final int cubeX = 11;
	public static final int cubeY = 22;
	public static final int cubeWidth = 50;
	public static final int cubeHeight = 60;
	private double widthScale = (double)cubeWidth / (double)(cubeWidth - cubeX);
	private double heightScale = (double)cubeHeight / (double)(cubeHeight - cubeY);
	
	public Point pos;
	private int len; //block size
	private Map map;
	private Movable[] movables;
	
	public Screen(Map m) {
		super();
		pos = new Point();
		len = 64;
		map = m;
	}
	
	public void addMovables(Movable[] ms) {
		movables = ms;
	}
	
	public void paintComponent(Graphics g) {
		for(int i = 0; i < map.getWidth(); i ++) {
			for(int j = map.getHeight()-1; j >= 0 ; j --) {
				if(map.getBlock(i, j) != null) {
//					System.out.println("There's a block in my map!");
					drawBlock(g, map.getBlock(i, j), i, j);
				}
			}
		}
		drawBounds(g);
	}
	
	private void drawBlock(Graphics g, Block block, int x, int y) {
		File imageFile = new File(block.getImageFileName());
		BufferedImage img;
		try {
			img = ImageIO.read(imageFile);
			g.drawImage(img, (int)(x * len - (len * widthScale - len) / 2) + pos.x, (int)(y * len - (len * heightScale - len) / 2) + pos.y, (int)(len * widthScale), (int)(len * heightScale), this);
//			System.out.println("Block Drawn at ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void drawBounds(Graphics g) {
		g.drawRect(pos.x, pos.y, map.getWidth() * len, map.getHeight() * len);
	}
	
	public void setBlockSize(int s) {
		if(s > MIN_BLOCK_SIZE && s < MAX_BLOCK_SIZE) {
			len = s;
		}
	}
	
	public int getBlockSize() {
		return len;
	}
}
