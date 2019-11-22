package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Screen extends JPanel{
	//Map Rendering
	public static final int RENDER_WIDTH = 20;
	public static final int RENDER_HEIGHT = 20;
	
	//Zoom Levels
	public static final int MAX_BLOCK_SIZE = 64; 
	public static final int MIN_BLOCK_SIZE = 32;

//	public static final int MIN_X = 0;
//	public static final int MIN_Y = 0;
//	public static final int MAX_X = 0;
//	public static final int MAX_Y = 0;
	
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
	public double currentScale = 1;
	
	public Point pos;
	private int len; //block size
	private int pastLen;
	public Map map;
	public Game game;
	private Movable[] movables;
	private ArrayList<Movable> targets = new ArrayList<>();
	
	private int mouseX = 0;
	private int mouseY = 0;
	
	private boolean mapOutlineOn = true;
	private boolean blockOutlineOn = true;
	private boolean hitBoxesOn = false;
	private boolean imagesOn = true;
	
	public int maxX, minX, maxY, minY;
	
	private BufferedImage img;
	
	public Screen(Map m) {
		super();
		pos = new Point();
		len = startingLength;
		pastLen = len;
		map = m;

		File imageFile = new File("Sprites/sky.png");
		try {
			img = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setGame(Game g) {
		game = g;
	}
	
	public void addMovables(Movable[] ms) {
		movables = ms;
	}
	
	public void paintComponent(Graphics g) {
		target();
		currentScale = (double) len / startingLength;

		g.drawImage(img, (int)(pos.x * (currentScale / 2)), //
				(int)(pos.y * (currentScale / 2)),
				(int)(3200 * currentScale),//
				(int)(1200 * currentScale), this);

		int avX = (minX + maxX) / 2;
		int avY = (minY + maxY) / 2;
		for(int i = avX / 64 - RENDER_WIDTH; i < avX / 64 + RENDER_WIDTH; i ++) {
			for(int j = avY / 64 + RENDER_HEIGHT; j > avY / 64 - RENDER_HEIGHT; j --) {
				if(i >= 0 && i < map.getWidth() && 
						j >= 0 && j < map.getHeight() &&
						map.getBlock(i, j) != null) {
					drawBlock(g, map.getBlock(i, j), i, j);
				}
			}
		}
		if(movables != null) {
			drawMovables(g);
		}
		if(mapOutlineOn) {
			drawBounds(g);
		}
		if(blockOutlineOn) {
			g.drawRect(((mouseX - pos.x) / len) * len + pos.x, ((mouseY - pos.y) / len) * len + pos.y, len, len);
		} // mouse area
		g.setColor(new Color(0, 0, 0, 75));
		g.fillRect((int)(game.gameFrame.getWidth() / 2) - 10, 0, 100, 40);
		g.setColor(Color.white);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g.drawString(String.format("%2d:%02d", (int) game.score/60, (int) game.score%60), (int)(game.gameFrame.getWidth() / 2), 30);
	}
	
	public void addTarget(Movable m) {
		targets.add(m);
	}
	
	private void target() {
		int currentX, currentY;
		currentX = (int)targets.get(0).rec.getCenterX();
		currentY = (int)targets.get(0).rec.getCenterY();
		maxX = currentX; minX = currentX; maxY = currentY; minY = currentY;
		for(int i = 0; i < targets.size(); i ++) {
			currentX = (int)targets.get(i).rec.getCenterX();
			currentY = (int)targets.get(i).rec.getCenterY();
			if(currentX > maxX) {
				maxX = currentX;
			}else if(currentX < minX) {
				minX = currentX;
			}
			if(currentY > maxY) {
				maxY = currentY;
			}else if(currentY < minY) {
				minY = currentY;
			}
		}

		double scaleX = (double)(game.gameFrame.getWidth() * 7) / (double)((maxX - minX) * 8);
		double scaleY = (double)(game.gameFrame.getHeight() * 7) / (double)((maxY - minY) * 8);
		setScale(Math.min(scaleX, scaleY));
		setLocation(-(maxX + minX) / 2 * currentScale + game.gameFrame.getWidth() / 2, 
				-(maxY + minY) / 2 * currentScale + game.gameFrame.getHeight() / 2);
//		System.out.println((double)(game.gameFrame.getWidth() * 7) / (double)((maxX - minX) * 8));
	}
	
	private void drawBlock(Graphics g, Block block, int x, int y) {
		if(!block.getImageFileName().equals("none")) {
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
	}
	
	public void setLocation(double x, double y) {
		if(x > 0) {
			pos.x = 0;
		}else if((x - game.gameFrame.getWidth()) < -map.getWidth() * len) {
			pos.x = -map.getWidth() * len + game.gameFrame.getWidth();
		}else {
			pos.x = (int) x;
		}
		if(y > 0) {
			pos.y = 0;
		}else if((y - game.gameFrame.getHeight() - 30) < -map.getHeight() * len) {
			pos.y = -map.getHeight() * len + game.gameFrame.getHeight() - 30;
		}else {
			pos.y = (int) y;
		}
		if(pos.x > 0) {
			pos.x = 0;
		}
		if(pos.y > 0) {
			pos.y = 0;
		}
	}
	
	private void drawMovables(Graphics g) {
		g.setColor(Color.BLUE);
		for(int i = 0; i < movables.length; i ++) {
			if(movables[i] != null) {
				Image img = movables[i].getImage();
				if(imagesOn) {
					if(movables[i].isVisible) {
						if(!movables[i].isFacingLeft) {
							g.drawImage(
									img,
									(int)((movables[i].rec.x - movables[i].startPoint.x) * currentScale + pos.x),
									(int)((movables[i].rec.y - movables[i].startPoint.y) * currentScale + pos.y),
									(int)(movables[i].IMAGE_WIDTH * currentScale),
									(int)(movables[i].IMAGE_HEIGHT * currentScale),
									this);
						} else {
							g.drawImage(
									img,
									(int)(((movables[i].rec.x - movables[i].startPoint.x) * currentScale + pos.x)+(movables[i].IMAGE_WIDTH * currentScale)),
									(int)((movables[i].rec.y - movables[i].startPoint.y) * currentScale + pos.y),
									(int)-(movables[i].IMAGE_WIDTH * currentScale),
									(int)(movables[i].IMAGE_HEIGHT * currentScale),
									this);
						}
					}	
				}
				if(hitBoxesOn) {
					g.setColor(Color.BLUE);
					g.drawRect(
							(int)(movables[i].rec.x * currentScale) + pos.x,
							(int)(movables[i].rec.y * currentScale) + pos.y,
							(int)(movables[i].rec.width * currentScale),
							(int)(movables[i].rec.height * currentScale));
					g.setColor(Color.YELLOW);
					g.drawRect(
							(int)(movables[i].pastRec.x * currentScale) + pos.x,
							(int)(movables[i].pastRec.y * currentScale) + pos.y,
							(int)(movables[i].pastRec.width * currentScale),
							(int)(movables[i].pastRec.height * currentScale));
				}
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
	
	public void setScale(double s) {
		setBlockSize((int)((double)startingLength * s));
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
