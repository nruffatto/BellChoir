package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Player extends Movable implements MouseListener, KeyListener{
	
	private int speed = 20;
	private int jumpingSpeed = 40;
	private double crouchScale = 0.5;
	
	private boolean isCrouched = false;

/* }>Key Codes<{
 * get with KeyEvent.VK_<whatever key you want> like VK_A, VK_W, or VK_S
 */
	
	public Player(int x, int y) {
		super(x, y);
		rec.height = 120;
		rec.width = 60;
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
	}
	
	private void unCrouch() {
		int crouchDist = (int)(rec.height / crouchScale - rec.height);
		rec.height += crouchDist;
		rec.y -= crouchDist;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_A) {
				velX = -speed;
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {
				velX = speed;
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			if (isInAir == false) {
				isInAir = true;
				velY = - jumpingSpeed;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			if(!isCrouched) {
				crouch();
				isCrouched = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_A) {
			if(velX < 0) {
				velX = 0;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {
			if(velX > 0) {
				velX = 0;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
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

}
