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

public class Player extends Movable implements KeyListener{
	public static final int NUMBER_OF_STATS = 2;
	public static final int SPEED_INDEX = 0;
	public static final int JUMP_INDEX = 1;
	public static final int NORMAL_INDEX = 0;
	public static final int HAS_PACKAGE_INDEX = 1;
	private int currentState = NORMAL_INDEX;
	private int[][] stats = new int[2][NUMBER_OF_STATS];
	
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
	private boolean isRunning = false;
	private boolean isCrouched = false;
	private boolean isJumping = false;
	public boolean isFacingLeft;
	public boolean hasPackage;

	private String[] crouchImages = {"Sprites/mailmancrouch1.png","Sprites/mailmancrouch2.png"};
	private String[] packageImages = {"Sprites/packagerun1.png","Sprites/packagerun2.png"};
	
	protected Animation animation = new Animation("Sprites/mailmanrun_8_.png");
	
	
	
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

		stats[NORMAL_INDEX][SPEED_INDEX] = 10;
		stats[HAS_PACKAGE_INDEX][SPEED_INDEX] = 7;
		stats[NORMAL_INDEX][JUMP_INDEX] = 25;
		stats[HAS_PACKAGE_INDEX][JUMP_INDEX] = 20;
	}
	
	public void getControls() {
		switch (playerNumber) {
			case 0:
				left = KeyEvent.VK_A;
				right = KeyEvent.VK_D;
				up = KeyEvent.VK_W;
				down = KeyEvent.VK_S;
				break;
			case 1: 
				left = KeyEvent.VK_LEFT;
				right = KeyEvent.VK_RIGHT;
				up = KeyEvent.VK_UP;
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
		if(hasPackage) {
			currentState = HAS_PACKAGE_INDEX;
		}else {
			currentState = NORMAL_INDEX;
		}
		speed = stats[currentState][SPEED_INDEX];
		jumpingSpeed = stats[currentState][JUMP_INDEX];
		if(isJumping && !isInAir) {
			isInAir = true;
			velY = - jumpingSpeed;
		}
		if(crouchKeyPressed && !isCrouched) {
			if(!hasPackage) {
				crouch();
				isCrouched = true;
			}
		}else if(!crouchKeyPressed && isCrouched && !checkCollisionCrouch()) {
			unCrouch();
			isCrouched = false;
		}
		if(game.packages[0].holder == null && !isCrouched) {
			hasPackage = false;
			if(isTouching(game.packages[0])) {
				game.packages[0].setHolder(this);
				hasPackage = true;
			}
		}
		if(leftKeyPressed && !rightKeyPressed) {
			velX = -speed;
		}else if(!leftKeyPressed && rightKeyPressed) {
			velX = speed;
		}else {
			velX = 0;
		}
		if(isRunning) {
			animation.currentFrame++;
			if(animation.currentFrame == animation.frames) {
				animation.currentFrame = 0;
			}
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
				isFacingLeft = true;
		}
		if(e.getKeyCode() == right) {
				rightKeyPressed = true;
				isRunning = true;
				isFacingLeft = false;
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
			isRunning = false;
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
	public Image getImage() {
		File imageFile;
		if(isCrouched) {
			imageFile = new File(crouchImages[playerNumber]);
		}else if(game.packages[0].holder == this){
			imageFile = new File(packageImages[playerNumber]);
		}else{
			imageFile = new File(playerImages[playerNumber]);
		}
		BufferedImage img;
		if (isRunning) {
			return img = animation.images[animation.currentFrame];
		}
		else {
			try {
				img = ImageIO.read(imageFile);
				return img;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
