package game;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MapEditor extends TimerTask implements MouseListener, KeyListener, ActionListener, MouseWheelListener, MouseMotionListener {

	public static void main(String[] args) {
		MapEditor mE = new MapEditor();
	}
	
	public static final int DEFAULT_BLOCK_SIZE = 64;
	public static final int TIME_STEP = 50;
	public static final int ZOOM_STEP = 5;
	
	private Map map = new Map(12, 12);
	private Stack<Map> mapStack = new Stack<>();
	private Screen screen = new Screen(map);
	private JFrame editorFrame;
	private Container contentPane;
	private JPanel editorPanel;
	private JTextField fileField;
	private JButton openButton;
	private JButton saveButton;
	private JTextField mapWidthField;
	private JTextField mapHeightField;
	private JButton resizeButton;
//	private JLabel mapWidthLabel;
//	private JLabel mapHeightLabel;
	private Timer timer = new Timer();
	
	private int editorPanelWidth = 200;
	
	public Point pos = new Point(0, 0);
	
	private int mouseX;
	private int mouseY;
	
	private boolean ctrlPressed = false; // keyCode is 17
	private boolean altPressed = false;	// keyCode is 18
	private boolean zPressed = false; // keyCode is 90
	private boolean rightClickPressed = false;
	private boolean leftClickPressed = false;
	
	private boolean canInsertBlocks = true;
	
	String currentFileName;
	
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
		contentPane.add(screen, BorderLayout.CENTER);

		
		// Start of editorPanel stuff. As of now, we have to resize the jframe using the mouse for it to appear
		editorPanel = new JPanel();
		editorPanel.setBackground(Color.GRAY);
		
		openButton = new JButton("Open File");
		openButton.addActionListener(this);
		saveButton = new JButton("Save File");
		saveButton.addActionListener(this);
		
		fileField = new JTextField("file_name.txt");
		fileField.addActionListener(this);
		fileField.setBounds(0, 0, 100, 20);
		
		editorPanel.add(fileField, BorderLayout.AFTER_LAST_LINE);
		editorPanel.add(openButton, BorderLayout.AFTER_LAST_LINE);
		editorPanel.add(saveButton, BorderLayout.AFTER_LAST_LINE);
		
		mapWidthField = new JTextField(map.getWidth() + "");
		mapWidthField.setBounds(0, 0, 100, 20);
//		mapWidthField.add(new JLabel("Map Width:"));
		contentPane.add(editorPanel, BorderLayout.SOUTH);
		
		mapHeightField = new JTextField(map.getHeight() + "");
		mapHeightField.setBounds(0, 0, 100, 20);
//		mapHeightField.add(new JLabel("Map Height:"));
		resizeButton = new JButton("Resize Map");
		resizeButton.addActionListener(this);
		
		editorPanel.add(mapWidthField, BorderLayout.AFTER_LAST_LINE);
		editorPanel.add(mapHeightField, BorderLayout.AFTER_LAST_LINE);
		editorPanel.add(resizeButton, BorderLayout.AFTER_LAST_LINE);
		// End of editorPanel Stuff

		editorFrame.addMouseListener(this);
		editorFrame.addMouseMotionListener(this);
		editorFrame.addMouseWheelListener(this);
		editorFrame.addKeyListener(this);
		
		timer.schedule(this, 0, TIME_STEP);
	}
	
	private void storeMapInstance() {
		if(!mapStack.isEmpty()) {
			System.out.println(mapStack.peek().equals(map));
		}
		if(mapStack.isEmpty() || (!mapStack.isEmpty() && !mapStack.peek().equals(map))) {
			System.out.println("storeMapInstance");
			mapStack.push(map.getCopy());
		}
	}
	
	private void undo() {
		System.out.println("undo");
		if(!mapStack.isEmpty()) {
			System.out.println("pop!");
			map = mapStack.pop().getCopy();
			screen.map = map;
		}
	}

	@Override
	public void run() {
		screen.setMousePos(mouseX, mouseY);
		editorFrame.repaint();
//		screen.repaint();
//		editorPanel.repaint();
	}
	
	public void clearMap() {
		storeMapInstance();
		for(int i = 0; i < map.getWidth(); i ++) {
			for(int j = 0; j < map.getHeight(); j ++) {
				map.eraseBlock(i, j);
			}
		}
	}
	
	private void openFile(String fileName) {
		clearMap();
		System.out.println("opening...");
		File f1 = new File(fileName);
		Scanner s1;
		int width;
		int height;
		int xCoord;
		int yCoord;
		String imageFileName;
		int numBlockPropertiesOfFile;
		boolean[] blockProperties = new boolean[Block.NUMBER_OF_PROPERTIES];
		try {
			s1 = new Scanner(f1);
			width = s1.nextInt();
			height = s1.nextInt();
			numBlockPropertiesOfFile = s1.nextInt();
			map.resize(width, height);
			while(s1.hasNextLine() && s1.hasNext()) {
				xCoord = s1.nextInt();
				yCoord = s1.nextInt();
				imageFileName = s1.next();
				for(int i = 0; i < numBlockPropertiesOfFile; i ++) {
					blockProperties[i] = s1.nextBoolean();
				}
				map.insertBlock(xCoord, yCoord, new Block(imageFileName, blockProperties));
			}
			System.out.println("done!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void saveFile(String fileName) {
		System.out.println("saving...");
		int width = map.getWidth();
		int height = map.getHeight();
		int xCoord;
		int yCoord;
		String imageFileName;
		boolean[] blockProperties = new boolean[Block.NUMBER_OF_PROPERTIES];
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(width + " " + height + " " + Block.NUMBER_OF_PROPERTIES);
			writer.newLine();
			for(int i = 0; i < width; i ++) {
				for(int j = 0; j < height; j ++) {
					if(map.getBlock(i, j) != null) {
						writer.write(i + " " + j + " " + map.getBlock(i, j).getImageFileName() + " ");
						for(int k = 0; k < Block.NUMBER_OF_PROPERTIES; k ++) {
							if(map.getBlock(i, j).is(k)){
								writer.write("true ");
							}else {
								writer.write("false ");
							}
						}
						if(i < width - 1 && j < height - 1) {
							writer.newLine();
						}
					}
				}
			}
			writer.flush();
			writer.close();
			System.out.println("done!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	private void insertBlock(int x, int y) {
		storeMapInstance();
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
			storeMapInstance();
			map.insertBlock(x / screen.getBlockSize(), 
					y / screen.getBlockSize(), 
					null);
		}
	}
	
	private void zoom(int zoomAmount) {
		double newBlockSize = screen.getBlockSize() + ZOOM_STEP * zoomAmount;
		double scale = newBlockSize / screen.getBlockSize();
		screen.pos.x =(int) (- ((mouseX - screen.pos.x) * scale - mouseX));
		screen.pos.y =(int) (- ((mouseY - screen.pos.y) * scale - mouseY));
		screen.setBlockSize((int)newBlockSize);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(openButton)) {
			openFile(fileField.getText());
		}else if(e.getSource().equals(saveButton)) {
			saveFile(fileField.getText());
		}else if(e.getSource().equals(resizeButton)) {
			int width = 0, height = 0;
			try {
				width = Integer.parseInt(mapWidthField.getText());
			} catch(NumberFormatException exception) {
				mapWidthField.setText("int please!");
			}
			try {
				height = Integer.parseInt(mapHeightField.getText());
			} catch(NumberFormatException exception) {
				mapHeightField.setText("int please!");
			}
			if(width > 0 && height > 0) {
				map.resize(width, height);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 17) {
			ctrlPressed = true;
//			System.out.println("Ctrl Key Pressed!");
		}else if (e.getKeyCode() == 18) {
			altPressed = true;
		}else if(ctrlPressed && e.getKeyCode() == 90) {
//			System.out.println("Ctrl + z");
			undo();
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
		mouseX = e.getX() - 7;
		mouseY = e.getY() - 30;
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
