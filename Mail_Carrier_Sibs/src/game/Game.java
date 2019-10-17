package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class Game extends TimerTask implements MouseListener, ActionListener, KeyListener{

	public static void main(String[] args) {
		Game g1 = new Game();
	}
	
	public static final int DEFAULT_BLOCK_SIZE = 64;
	public static final int TIME_STEP = 50;
	
	private JFrame gameFrame;
	private Map map = new Map(1, 1);
	private String[] mapList = {"file_name.txt"};
	private Screen screen;
	private Container contentPane;
	private Timer timer = new Timer();
	
	private boolean gameIsReady;
	
	
	public Game() {
		gameIsReady = false;
		gameFrame = new JFrame("Map game");
		gameFrame.setLocation(0, 0);
		gameFrame.setSize(800,
				800 + 30);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = gameFrame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.WHITE);
		gameFrame.setVisible(true);
		
		openMap(mapList[0]);
		
		screen  = new Screen(map);
		screen.setLayout(null);
		screen.setBackground(Color.BLUE);
		contentPane.add(screen, BorderLayout.CENTER);
		
		timer.schedule(this, 0, TIME_STEP);
		gameIsReady = true;
	}

	@Override
	public void run() {
		gameFrame.repaint();
	}
	
	private void openMap(String fileName) {
		map.clear();
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
