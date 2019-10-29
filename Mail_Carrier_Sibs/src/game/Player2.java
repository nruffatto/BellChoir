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

public class Player2 extends Movable implements MouseListener, KeyListener{
	
	private int speed = 10;
	private int jumpingSpeed = 25;
	private double crouchScale = 0.5;
	
	private boolean isCrouched = false;
	
	private String crouchImageName = "Sprites/mailmancrouch2.png";
	
	
	
/* }>Key Codes<{
 * get with KeyEvent.VK_<whatever key you want> like VK_A, VK_W, or VK_S
 */
	
	public Player2(int x, int y) {
		super(x, y);
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
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				velX = -speed;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				velX = speed;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			
			if (!isInAir) {
				isInAir = true;
				velY = - jumpingSpeed;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(!isCrouched) {
				crouch();
				isCrouched = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(velX < 0) {
				velX = 0;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(velX > 0) {
				velX = 0;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
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
			imageFile = new File(crouchImageName);
		}else {
			imageFile = new File(playerImages[1]);
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
