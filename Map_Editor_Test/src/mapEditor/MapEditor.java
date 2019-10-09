package mapEditor;
// Nathan is testing stoff to see how this git buisness works
// Jaden was here
// Nathan is testing staff to see how this git buisness works
// Zoinks, Scoob!
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MapEditor extends TimerTask implements MouseListener, KeyListener, ActionListener, MouseWheelListener, MouseMotionListener {

	public static void main(String[] args) {
		MapEditor mE = new MapEditor();
	}
	
	public static final int DEFAULT_BLOCK_SIZE = 64;
	public static final int TIME_STEP = 50;
	public static final int ZOOM_STEP = 5;
	
	private Map map = new Map(12, 12);
	private Screen screen = new Screen(map);
	private JFrame editorFrame;
	private Container contentPane;
	private JPanel editorPanel;
	private Timer timer = new Timer();
	
	private int editorPanelWidth = 200;
	
	public Point pos = new Point(0, 0);
	
	private int mouseX;
	private int mouseY;
	
	private boolean ctrlPressed = false; // keyCode is 17
	private boolean altPressed = false;	// keyCode is 18
	private boolean rightClickPressed = false;
	private boolean leftClickPressed = false;
	
	private boolean canInsertBlocks = true;
	
	public MapEditor() {
		editorFrame = new JFrame("Map Editor");
		editorFrame.setLocation(pos);
		editorFrame.setSize(map.getWidth() * DEFAULT_BLOCK_SIZE + editorPanelWidth,
				map.getHeight() * DEFAULT_BLOCK_SIZE + 30);
		editorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = editorFrame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.BLACK);
		editorFrame.setVisible(true);
		
//		TestPanel testPanel = new TestPanel();
//		testPanel.setLayout(null);
//		testPanel.setBackground(Color.BLACK);
//		contentPane.add(testPanel);
		
		screen.setLayout(null);
		screen.setBackground(Color.BLACK);
		contentPane.add(screen);
		
//		editorPanel = new JPanel();
//		editorPanel.setBackground(Color.GRAY);
//		contentPane.add(editorPanel, BorderLayout.EAST);

		editorFrame.addMouseListener(this);
		editorFrame.addMouseMotionListener(this);
		editorFrame.addMouseWheelListener(this);
		editorFrame.addKeyListener(this);
		
		timer.schedule(this, 0, TIME_STEP);
	}

	@Override
	public void run() {
		editorFrame.repaint();
//		screen.repaint();
//		editorPanel.repaint();
	}
	
	private void insertBlock(int x, int y) {
		x -= screen.pos.x;
		y -= screen.pos.y;
//		System.out.println(x + " " + y);
		if(x < map.getWidth() * screen.getBlockSize() && x > 0 &&
				y < map.getHeight() * screen.getBlockSize() && y > 0 &&
				canInsertBlocks) {
			map.insertBlock(x / screen.getBlockSize(), 
					y / screen.getBlockSize(), 
					new Block());
//			System.out.println("Block Insertion!");
		}
	}
	
	private void eraseBlock(int x, int y) {
		x -= screen.pos.x;
		y -= screen.pos.y;
		if(x < map.getWidth() * screen.getBlockSize() && x > 0 &&
				y < map.getHeight() * screen.getBlockSize() && y > 0) {
			map.insertBlock(x / screen.getBlockSize(), 
					y / screen.getBlockSize(), 
					null);
		}
	}
	
	private void zoom(int zoomAmount) {
		screen.setBlockSize(screen.getBlockSize() + ZOOM_STEP * zoomAmount);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 17) {
			ctrlPressed = true;
		}
		else if (e.getKeyCode() == 18) {
			altPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == 17) {
			ctrlPressed = false;
		}
		else if (e.getKeyCode() == 18) {
			altPressed = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
//		System.out.println("Mouse dragging!");
		if(leftClickPressed) {
//			System.out.println("Right click dragging!");
			if(ctrlPressed) {
				canInsertBlocks = false;
				screen.pos.x += e.getX() - 7 - mouseX;
				screen.pos.y += e.getY() - 30 - mouseY;
				mouseX = e.getX() - 7;
				mouseY = e.getY() - 30;
			}else {
				insertBlock(e.getX() - 7, e.getY() - 30);
			}
		}
		else if(rightClickPressed) {
			if(ctrlPressed) {
			}else {
				eraseBlock(e.getX() - 7, e.getY() - 30);
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		zoom(- e.getWheelRotation());
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
		mouseX = e.getX() - 7;
		mouseY = e.getY() - 30;
		if(e.getButton() == MouseEvent.BUTTON1) {
			leftClickPressed = true;
//			System.out.println(e.getX() + " " + e.getY() + " Left Press");
		}
		else if(e.getButton() == MouseEvent.BUTTON3) {
			rightClickPressed = true;
//			System.out.println(e.getX() + " " + e.getY() + " Right Press");
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			leftClickPressed = false;
			insertBlock(e.getX() - 7, e.getY() - 30);
//			System.out.println(e.getX() + " " + e.getY() + " Left Release");
		}
		else if(e.getButton() == MouseEvent.BUTTON3) {
			rightClickPressed = false;
			eraseBlock(e.getX() - 7, e.getY() - 30);
//			System.out.println(e.getX() + " " + e.getY() + " Right Release");
		}
		canInsertBlocks = true;
	}
	
	private class TestPanel extends JPanel {
		public void paintComponent(Graphics g) {
			g.setColor(Color.BLUE);
			g.fillRect(128, 128, 64, 64);
		}
	}

}
