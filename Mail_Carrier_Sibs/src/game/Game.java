package game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends TimerTask implements MouseListener, ActionListener, KeyListener{

	public static void main(String[] args) {
		Game g1 = new Game();
	}
	

	public static final int DEFAULT_BLOCK_SIZE = 64;
	public static final int TIME_STEP = 5;
	public static final int PACKAGE_INDEX = 2;
	
	public JFrame gameFrame;
	public Map map = new Map(1, 1);
	private String[] mapList = {"file1.txt"};
	public Screen screen;
	public Movable[] movables = new Movable[10];
	public Player[] players = new Player[2];
	public Package[] packages = new Package[1];
	public Dog[] dogs = new Dog[1];
	public Mailbox[] mailboxes = new Mailbox[1];
	private Container contentPane;
	private Timer timer = new Timer();
	
	private boolean gameIsReady;
	
	
	public Game() {
		gameIsReady = false;
		gameFrame = new JFrame("Map game");
		gameFrame.setSize(1280, 720);
		gameFrame.setLocationRelativeTo(null);
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
		screen.addMovables(movables);
		screen.setBlockSize(DEFAULT_BLOCK_SIZE);
		screen.setMapOutline(false);
		screen.setBlockOutline(false);
		screen.setGame(this);
		
		movables[0] = new Player(map.getSpawnPoint(0).x, map.getSpawnPoint(0).y, 0);
		movables[1] = new Player(map.getSpawnPoint(1).x, map.getSpawnPoint(1).y, 1);
		movables[PACKAGE_INDEX] = new Package(map.getSpawnPoint(2).x, map.getSpawnPoint(2).y);
		movables[3] = new Dog(1000,500); 
		movables[4] = new Mailbox(1000,500);
		
		players[0] = (Player) movables[0];
		players[1] = (Player) movables[1];
		
		packages[0] = (Package)movables[PACKAGE_INDEX];
		
		dogs[0] = (Dog) movables[3];
		
		mailboxes[0] = (Mailbox) movables[4];
		
		for(int i = 0; i < movables.length; i ++) {
			if(movables[i] != null && !movables[i].isDog() && !movables[i].isMailbox()) {
				screen.addTarget(movables[i]);
			}
		}
		
		for(int i = 0; i < movables.length; i ++) {
			if(movables[i] != null) {
				movables[i].addGame(this);
			}
		}
		
		gameFrame.addKeyListener(players[0]);
		gameFrame.addKeyListener(players[1]);
		gameFrame.addMouseListener(packages[0]);
		
		timer.schedule(this, 0, TIME_STEP);
		gameIsReady = true;
		gameFrame.repaint();
	}

	@Override
	public void run() {
		if(gameIsReady) {
			for(int i = 0; i < movables.length; i ++) {
				if(movables[i] != null) {
					movables[i].update();
					if(movables[i].rec.y > 10000) {
						movables[i].rec.y = 0;
					}
				}
			}
			gameFrame.revalidate();
			gameFrame.repaint();
		}
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
			boolean done = false;
			int counter = 0;
			while(!done) {
				String next = s1.next();
				if(next.equals("done")) {
					done = true;
				}else {
					map.insertSpawnPoint(Integer.parseInt(next), s1.nextInt(), counter);
				}
				counter ++;
			}
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
