package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Player extends Movable implements MouseListener, KeyListener{
	
	private int speed = 15;
	private int jumpingSpeed = 40;

/* }>Key Codes<{
 * get with KeyEvent.VK_<whatever key you want> like VK_A, VK_W, or VK_S
 */
	
	public Player(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void update() {
		updateProperties();
		super.update();
	}
	
	public void updateProperties() {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_A) {
				velX = -speed;
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {
				velX = speed;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			velY = - jumpingSpeed;
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
