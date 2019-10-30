package game;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player extends Movable implements MouseListener, KeyListener{
	
	private int speed = 10;
	private int jumpingSpeed = 25;
	private double crouchScale = 0.5;
	
	private int left;
	private int right;
	private int up;
	private int down;
	
	protected int playerNumber;
	
	private boolean leftKeyPressed = false;
	private boolean rightKeyPressed = false;
	
	private boolean crouchKeyPressed = false;
	private boolean isCrouched = false;
	private boolean isJumping = false;
	
	private String[] crouchImages = {"Sprites/mailmancrouch1.png","Sprites/mailmancrouch2.png"};
	
	
	
/* }>Key Codes<{
 * get with KeyEvent.VK_<whatever key you want> like VK_A, VK_W, or VK_S
 */
	
	public Player(int x, int y, int playerNumber) {
		super(x, y);
		HITBOX_WIDTH = 50;
		HITBOX_RATIO = 127.0 / 60.0;
		IMAGE_SCALE = HITBOX_WIDTH / 60.0;
		startPoint = new Point((int)(38 * IMAGE_SCALE),(int)(7 * IMAGE_SCALE));
		IMAGE_WIDTH = (int)(138 * IMAGE_SCALE);// * IMAGE_SCALE
		IMAGE_HEIGHT = (int)(135 * IMAGE_SCALE);
		rec = new Rectangle(x, y, HITBOX_WIDTH, (int)(HITBOX_WIDTH * HITBOX_RATIO));
		this.playerNumber = playerNumber;
	}
	
	public void getControls() {
		switch (playerNumber) {
			case 0:
				left = KeyEvent.VK_A;
				right = KeyEvent.VK_D;
				up = KeyEvent.VK_W;
				down = KeyEvent.VK_SPACE;
				break;
			case 1: 
				left = KeyEvent.VK_NUMPAD4;
				right = KeyEvent.VK_NUMPAD6;
				up = KeyEvent.VK_NUMPAD8;
				down = KeyEvent.VK_DOWN;
				break;
			default: break;
		}
	}
	
	@Override
	public void update() {
		updateProperties();
		super.update();
	}
	
	public void updateProperties() {
		if(isJumping && !isInAir) {
			isInAir = true;
			velY = - jumpingSpeed;
		}
		if(crouchKeyPressed && !isCrouched) {
			crouch();
			isCrouched = true;
		}else if(!crouchKeyPressed && isCrouched && !checkCollisionCrouch()) {
			unCrouch();
			isCrouched = false;
		}
		if(isTouching(game.movables[game.PACKAGE_INDEX])) {
			game.packages[0].setHolder(this);
		}
		if(leftKeyPressed && !rightKeyPressed) {
			velX = -speed;
		}else if(!leftKeyPressed && rightKeyPressed) {
			velX = speed;
		}else {
			velX = 0;
		}
	}
	
	private void crouch() {
		int crouchDist = (int)(rec.height - rec.height * crouchScale);
		rec.height -= crouchDist;
		rec.y += crouchDist;
		startPoint.y += crouchDist;
	}
	
	private void unCrouch() {
		int crouchDist = (int)(rec.height / crouchScale - rec.height);
		rec.height += crouchDist;
		rec.y -= crouchDist;
		startPoint.y -= crouchDist;
	}
	
	private boolean checkCollisionCrouch() {
		boolean collision = false;
		unCrouch();
		Point[] points = getPoints();
		Point[] pastPoints = getPastPoints();
		for(int i = 0; i < points.length; i ++) {
			if(game.map.contains(new Point(points[i].x / Screen.startingLength, points[i].y / Screen.startingLength))) {
				if(game.map.getBlock(points[i].x / Screen.startingLength, points[i].y / Screen.startingLength) != null) {
					collision = true;
					break;
				}
			}
		}
		crouch();
		return collision;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		getControls();
		if(e.getKeyCode() == left) {
				leftKeyPressed = true;
		}
		if(e.getKeyCode() == right) {
				rightKeyPressed = true;
		}
		if(e.getKeyCode() == up) {
			isJumping = true;
		}
		if(e.getKeyCode() == down) {
			crouchKeyPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		getControls();
		if(e.getKeyCode() == left) {
			leftKeyPressed = false;
		}
		if(e.getKeyCode() == right) {
			rightKeyPressed = false;
		}
		if(e.getKeyCode() == up) {
			isJumping = false;
		}
		if(e.getKeyCode() == down) {
			crouchKeyPressed = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
	@Override
	public Image getImage() {
		File imageFile;
		if(isCrouched) {
			imageFile = new File(crouchImages[playerNumber]);
		}else {
			imageFile = new File(playerImages[playerNumber]);
		}
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
