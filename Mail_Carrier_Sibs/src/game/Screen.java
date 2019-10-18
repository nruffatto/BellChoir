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
	public static final int cubeStartX = 14;
	public static final int cubeStartY = 28;
	public static final int cubeX = 22;
	public static final int cubeY = 11;
	public static final int cubeWidth = 68 + cubeX;
	public static final int cubeHeight = 68 + cubeY;
	public static final int imageWidth = 120;
	public static final int imageHeight = 98;
	private double widthScale = (double)cubeWidth / (double)(cubeWidth - cubeX);
	private double heightScale = (double)cubeHeight / (double)(cubeHeight - cubeY);
	
	public static final int startingLength = 64;
	private double currentScale = 1;
	
	public Point pos;
	private int len; //block size
	private int pastLen;
	public Map map;
	private Movable[] movables;
	
	private int mouseX = 0;
	private int mouseY = 0;
	
	private boolean mapOutlineOn = true;
	private boolean blockOutlineOn = true;
	
	public Screen(Map m) {
		super();
		pos = new Point();
		len = startingLength;
		pastLen = len;
		map = m;
	}
	
	public void addMovables(Movable[] ms) {
		movables = ms;
	}
	
	public void paintComponent(Graphics g) {
		currentScale = (double) len / startingLength;
		g.setColor(Color.RED);
		for(int i = 0; i < map.getWidth(); i ++) {
			for(int j = map.getHeight()-1; j >= 0 ; j --) {
				if(map.getBlock(i, j) != null) {
//					System.out.println("There's a block in my map!");
					drawBlock(g, map.getBlock(i, j), i, j);
				}
			}
		}
		drawMovables(g);
		if(mapOutlineOn) {
			drawBounds(g);
		}
		if(blockOutlineOn) {
			g.drawRect(((mouseX - pos.x) / len) * len + pos.x, ((mouseY - pos.y) / len) * len + pos.y, len, len);
		} // mouse area
	}
	
	private void drawBlock(Graphics g, Block block, int x, int y) {
		File imageFile = new File(block.getImageFileName());
		BufferedImage img;
		try {
			img = ImageIO.read(imageFile);
//			g.drawImage(img, (int)(x * len - (len * widthScale - len) / 2) + pos.x, (int)(y * len - (len * heightScale - len) / 2) + pos.y, (int)(len * widthScale), (int)(len * heightScale), this);
			g.drawImage(
					img,
					(int)(x * len - (len * widthScale - len) / 2 - cubeStartX * currentScale + pos.x),
					(int)(y * len + (len * heightScale - len) / 2 - cubeStartY * currentScale + pos.y),
					(int)(currentScale * imageWidth),
					(int)(currentScale * imageHeight),
					this);
//			System.out.println("Block Drawn at ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void drawMovables(Graphics g) {
		g.setColor(Color.BLUE);
		for(int i = 0; i < movables.length; i ++) {
			if(movables[i] != null) {
				g.fillRect(
						(int)(movables[i].rec.x * currentScale) + pos.x,
						(int)(movables[i].rec.y * currentScale) + pos.y,
						(int)(movables[i].rec.width * currentScale),
						(int)(movables[i].rec.height * currentScale));
			}
		}
	}
	
	public void drawBounds(Graphics g) {
		g.drawRect(pos.x, pos.y, map.getWidth() * len, map.getHeight() * len);
	}
	
	public void setBlockSize(int s) {
		if(s > MIN_BLOCK_SIZE && s < MAX_BLOCK_SIZE) {
			pastLen = len;
			len = s;
		}
	}
	
	public int getBlockSize() {
		return len;
	}
	
	public int getPastBlockSize() {
		return pastLen;
	}
	
	public int getMouseX() {
		return ((mouseX - pos.x) / len);
	}
	
	public int getMouseY() {
		return ((mouseY - pos.y) / len);
	}
	
	public void setMousePos(int x, int y) {
		mouseX = x;
		mouseY = y;
	}
	
	public void setMapOutline(boolean state) {
		mapOutlineOn = state;
	}
	
	public void setBlockOutline(boolean state) {
		blockOutlineOn = state;
	}
}
