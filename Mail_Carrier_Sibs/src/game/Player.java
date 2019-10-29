package game;

import java.awt.Image;
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
	
	private boolean isCrouched = false;
	
	private String[] crouchImages = {"Sprites/mailmancrouch1.png","Sprites/mailmancrouch2.png"};
	
	
	
/* }>Key Codes<{
 * get with KeyEvent.VK_<whatever key you want> like VK_A, VK_W, or VK_S
 */
	
	public Player(int x, int y, int playerNumber) {
		super(x, y);
		this.playerNumber = playerNumber;
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
	
	@Override
	public void keyPressed(KeyEvent e) {
		getControls();
		if(e.getKeyCode() == left) {
				velX = -speed;
		}
		if(e.getKeyCode() == right) {
				velX = speed;
		}
		if(e.getKeyCode() == up) {
			
			if (!isInAir) {
				isInAir = true;
				velY = - jumpingSpeed;
			}
		}
		if(e.getKeyCode() == down) {
			if(!isCrouched) {
				crouch();
				isCrouched = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		getControls();
		if(e.getKeyCode() == left) {
			if(velX < 0) {
				velX = 0;
			}
		}
		if(e.getKeyCode() == right) {
			if(velX > 0) {
				velX = 0;
			}
		}
		if(e.getKeyCode() == down) {
				unCrouch();
				isCrouched = false;
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
