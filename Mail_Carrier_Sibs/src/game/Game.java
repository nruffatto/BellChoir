package game;
import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
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
import java.util.Collection;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class Game extends TimerTask implements MouseListener, ActionListener, KeyListener{

	public static void main(String[] args) {
		Game g1 = new Game();
	}
	

	public static final int DEFAULT_BLOCK_SIZE = 64;
	public static final int TIME_STEP = 30;
	public static final int PACKAGE_INDEX = 2;
	
	public JFrame gameFrame;
	public JWindow pauseWindow;
	public Map map = new Map(1, 1);
	
	public String[] mapList = getMaps(); 
	public Screen screen;
	public Movable[] movables = new Movable[10];
	public Player[] players = new Player[2];
	public Package[] packages = new Package[1];
	public Dog[] dogs = new Dog[1];
	public Mailbox[] mailboxes = new Mailbox[1];
	private Container contentPane;
	private Container contentPane1;
	private Container helpPane;
	private Timer timer;
	
	private boolean gameIsReady;
	public int levelNumber = mapList.length;
	public int currentLevelIndex;
	public JButton[] levelButtons = new JButton[levelNumber];
	public JButton[] menuButtons = new JButton[4];
	
	private boolean levelpanel;
	public static final Color LIGHT_BLUE = new Color(51,204,255);
	
	public String menuImage = "Sprites/logo.png";
	public String levelComplete = "Sprites/levelcomplete.png";
	public String[] gameOver = {"Sprites/GameOver1.png","Sprites/GameOver2.png","Sprites/GameOver1.png"};
	public String[] pauseImage = {"Sprites/PauseImage1.png","Sprites/PauseImage2.png","Sprites/PauseImage3.png"};
	public String[] helpImage = {"Sprites/help1.png","Sprites/help2.png"};

	public double score = 0;
	
	public boolean paused = false;
	
	public Font customFont;
	
	public Game() {
		gameIsReady = false;
		gameFrame = new JFrame("Super Mail Carrier Sibs");
		gameFrame.setSize(1280, 720);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon icon = new ImageIcon("mailmanhead.png");
		gameFrame.setIconImage(icon.getImage());
		contentPane = gameFrame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.WHITE);
		gameFrame.setVisible(true);
		
		timer = new Timer();
		timer.schedule(this, 0, TIME_STEP);
		
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/JBLFONT1.ttf")).deriveFont(30f);	
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/JBLFONT1.ttf")));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		LoadMenu();
	}
	
	public void LoadMenu()
	{
		levelpanel = false;
		JPanel panel = new menuImage();
		panel.setDoubleBuffered(true);
		panel.setBackground(LIGHT_BLUE);
		
		
		JButton startbtn = new JButton();
		try {
		    BufferedImage img = ImageIO.read(new File("Sprites/play.png"));
		    BufferedImage img2 = ImageIO.read(new File("Sprites/playhover.png"));
		    startbtn.setIcon(new ImageIcon(img));
		    startbtn.setRolloverIcon(new ImageIcon(img2));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		startbtn.setBorderPainted(false);
		startbtn.setContentAreaFilled(false);
		startbtn.setFocusPainted(false);
		startbtn.setOpaque(false);
		panel.setLayout(null);
		Insets insets = panel.getInsets();
		Dimension size = startbtn.getPreferredSize();
		startbtn.setBounds(800 + insets.right, 100 + insets.top,size.width+50, size.height+25);
		startbtn.addActionListener(this);
		startbtn.setActionCommand("Start");
		panel.add(startbtn);
		
		JButton helpbtn = new JButton();
		try {
		    BufferedImage img = ImageIO.read(new File("Sprites/help.png"));
		    BufferedImage img2 = ImageIO.read(new File("Sprites/helphover.png"));
		    helpbtn.setIcon(new ImageIcon(img));
		    helpbtn.setRolloverIcon(new ImageIcon(img2));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		helpbtn.setBorderPainted(false); 
		helpbtn.setContentAreaFilled(false); 
		helpbtn.setFocusPainted(false); 
		helpbtn.setOpaque(false);
		panel.setLayout(null);
		helpbtn.setBounds(800 + insets.right, 250 + insets.top,size.width+50, size.height+25);
		helpbtn.addActionListener(this);
		helpbtn.setActionCommand("Help");
		
		panel.add(helpbtn);
		
		
		JButton exitbtn = new JButton();
		try {
		    BufferedImage img = ImageIO.read(new File("Sprites/exit.png"));
		    BufferedImage img2 = ImageIO.read(new File("Sprites/exithover.png"));
		    exitbtn.setIcon(new ImageIcon(img));
		    exitbtn.setRolloverIcon(new ImageIcon(img2));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		exitbtn.setBorderPainted(false); 
		exitbtn.setContentAreaFilled(false); 
		exitbtn.setFocusPainted(false); 
		exitbtn.setOpaque(false);
		panel.setLayout(null);
		exitbtn.setBounds(800 + insets.right, 400 + insets.top,size.width+50, size.height+25);
		exitbtn.addActionListener(this);
		exitbtn.setActionCommand("Exit");
		panel.add(exitbtn);
		
		gameFrame.setContentPane(panel);
		gameFrame.setVisible(true);
	}
	
	public void LoadLevelButtons()
	{
		
		contentPane = gameFrame.getContentPane();
		contentPane.setLayout(null);
		contentPane.setBackground(Color.WHITE);
		gameFrame.setVisible(true);
		
		JPanel panel = new JPanel(new GridLayout(4,4,4,4));
		panel.setDoubleBuffered(true);
		panel.setBackground(LIGHT_BLUE);

		for(int i=0 ; i<levelNumber ; i++){
		    JButton btn = new JButton("Level " + String.valueOf(i+1));
		    levelButtons[i] = btn;
		    try {
			    BufferedImage img = ImageIO.read(new File("Sprites/cloud.png"));
			    BufferedImage img2 = ImageIO.read(new File("Sprites/cloudhover.png"));
			    btn.setIcon(new ImageIcon(img));
			    btn.setRolloverIcon(new ImageIcon(img2));
			  } catch (Exception ex) {
			    System.out.println(ex);
			  }
		    //btn.setPreferredSize(new Dimension(40, 40));
		    btn.setHorizontalTextPosition(JButton.CENTER);
		    btn.setVerticalTextPosition(JButton.CENTER);
		    btn.setBorderPainted(false); 
			btn.setContentAreaFilled(false); 
			btn.setFocusPainted(false); 
			btn.setOpaque(false);
		    btn.addActionListener(this);
		    btn.setActionCommand(mapList[i]);
		    btn.setFont(customFont);
		    panel.add(btn);
		}
		
		JButton backbtn = new JButton();
		try {
		    BufferedImage img = ImageIO.read(new File("Sprites/back.png"));
		    BufferedImage img2 = ImageIO.read(new File("Sprites/backhover.png"));
		    backbtn.setIcon(new ImageIcon(img));
		    backbtn.setRolloverIcon(new ImageIcon(img2));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		backbtn.setBorderPainted(false); 
		backbtn.setContentAreaFilled(false); 
		backbtn.setFocusPainted(false); 
		backbtn.setOpaque(false);
		backbtn.addActionListener(this);
		backbtn.setActionCommand("Back");
		menuButtons[2] = backbtn;
		panel.add(backbtn);
		
		gameFrame.setContentPane(panel);
		gameFrame.setVisible(true);
	}

	
	public void LoadHelp()
	{
		
		helpPane = gameFrame.getContentPane();
		helpPane.setLayout(null);
		helpPane.setBackground(Color.WHITE);
		gameFrame.setVisible(true);
		
		JPanel panel = new helpImage();
		panel.setBackground(LIGHT_BLUE);
		
		JButton backbtn = new JButton();
		try {
		    BufferedImage img = ImageIO.read(new File("Sprites/back.png"));
		    BufferedImage img2 = ImageIO.read(new File("Sprites/backhover.png"));
		    backbtn.setIcon(new ImageIcon(img));
		    backbtn.setRolloverIcon(new ImageIcon(img2));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		backbtn.setBorderPainted(false); 
		backbtn.setContentAreaFilled(false); 
		backbtn.setFocusPainted(false); 
		backbtn.setOpaque(false);
		panel.setLayout(null);
		Insets insets = panel.getInsets();
		Dimension size = backbtn.getPreferredSize();
		backbtn.setBounds(800 + insets.right, 250 + insets.top,size.width+50, size.height+25);
		backbtn.addActionListener(this);
		backbtn.setActionCommand("Back");
		menuButtons[2] = backbtn;
		panel.add(backbtn);
		
		JButton nextbtn = new JButton();
		try {
		    BufferedImage img = ImageIO.read(new File("Sprites/next.png"));
		    BufferedImage img2 = ImageIO.read(new File("Sprites/nexthover.png"));
		    nextbtn.setIcon(new ImageIcon(img));
		    nextbtn.setRolloverIcon(new ImageIcon(img2));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		nextbtn.setBorderPainted(false); 
		nextbtn.setContentAreaFilled(false); 
		nextbtn.setFocusPainted(false); 
		nextbtn.setOpaque(false);
		panel.setLayout(null);
		nextbtn.setBounds(800 + insets.right, 380 + insets.top,size.width+50, size.height+25);
		nextbtn.addActionListener(this);
		nextbtn.setActionCommand("Next");
		panel.add(nextbtn);
		
		gameFrame.setContentPane(panel);
		gameFrame.setVisible(true);
	
	}
	
	public void LoadHelp2()
	{
		
		helpPane = gameFrame.getContentPane();
		helpPane.setLayout(null);
		helpPane.setBackground(Color.WHITE);
		gameFrame.setVisible(true);
		
		JPanel panel = new helpImage2();
		panel.setBackground(LIGHT_BLUE);
		
		JButton backbtn = new JButton();
		try {
		    BufferedImage img = ImageIO.read(new File("Sprites/back.png"));
		    BufferedImage img2 = ImageIO.read(new File("Sprites/backhover.png"));
		    backbtn.setIcon(new ImageIcon(img));
		    backbtn.setRolloverIcon(new ImageIcon(img2));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		backbtn.setBorderPainted(false); 
		backbtn.setContentAreaFilled(false); 
		backbtn.setFocusPainted(false); 
		backbtn.setOpaque(false);
		panel.setLayout(null);
		Insets insets = panel.getInsets();
		Dimension size = backbtn.getPreferredSize();
		backbtn.setBounds(800 + insets.right, 250 + insets.top,size.width+50, size.height+25);
		backbtn.addActionListener(this);
		backbtn.setActionCommand("Help");
		panel.add(backbtn);
		
	
		
		gameFrame.setContentPane(panel);
		gameFrame.setVisible(true);
	
	}
	
	private Dimension Dimension(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();
        if (action.equals("Start"))
        {

        	LoadLevelButtons();
        }
        if (action.equals("Help"))
        {
        	LoadHelp();
        }
        if (action.equals("Next"))
        {
        	LoadHelp2();

        }
        if (action.equals("Exit"))
        {
        	System.exit(0);
        }
        
        if (action.equals("Back"))
        {
        	LoadMenu();
        }
        
        if (action.equals("BackMenu"))
        {
        	pauseWindow.dispose();
        	LoadMenu();
        }
        
        if (action.equals("UnPause"))
        {
        	pauseWindow.dispose();
        	unpauseGame();
        }
        
        if (action.equals("NextLevel"))
        {
        	pauseWindow.dispose();
        	StartLevel(mapList[(currentLevelIndex+1) % mapList.length], (currentLevelIndex+1) % mapList.length);
        	
        }
        
        if (action.equals("Restart"))
        {
        	pauseWindow.dispose();
        	StartLevel(mapList[currentLevelIndex],currentLevelIndex);
        	
        }
       
        for(int i=0 ; i<levelNumber ; i++)
        {
        	if (action.equals(mapList[i]))
                {
                	StartLevel(mapList[i],i);
                	for(int j=0 ; j<levelNumber ; j++)
                	{
                		levelButtons[j].setFocusable(false);
                    	levelButtons[j].setVisible(false);
                    }
                }
        }
                
        
    }

	
	public void StartLevel(String level, int index) {
		menuButtons[2].setVisible(false);
		contentPane1 = gameFrame.getContentPane();
		contentPane1.setLayout(new BorderLayout());
		contentPane1.setBackground(Color.WHITE);
		gameFrame.setVisible(true);

		openMap(level);
		
		currentLevelIndex = index;
		
		if(screen != null) {
			contentPane1.remove(screen);
		}
			
		screen  = new Screen(map);
		screen.setLayout(null);
		screen.setBackground(Color.BLUE);
		contentPane1.add(screen, BorderLayout.CENTER);
		screen.addMovables(movables);
		screen.setBlockSize(DEFAULT_BLOCK_SIZE);
		screen.setMapOutline(false);
		screen.setBlockOutline(false);
		screen.setGame(this);
		
		score = 0;
		
		if(map.getSpawnPoint(0) != null) {
			movables[0] = new Player(map.getSpawnPoint(0).x, map.getSpawnPoint(0).y, 0);
		}
		if(map.getSpawnPoint(1) != null) {
			movables[1] = new Player(map.getSpawnPoint(1).x, map.getSpawnPoint(1).y, 1);
		}
		if(map.getSpawnPoint(2) != null) {
			movables[PACKAGE_INDEX] = new Package(map.getSpawnPoint(2).x, map.getSpawnPoint(2).y);
		}
		if(map.getSpawnPoint(3) != null) {
			movables[3] = new Mailbox(map.getSpawnPoint(3).x, map.getSpawnPoint(3).y);
		}
		if(map.getSpawnPoint(4) != null) {
			movables[4] = new Dog(map.getSpawnPoint(4).x, map.getSpawnPoint(4).y, 200);
		}
		
		players[0] = (Player) movables[0];
		players[1] = (Player) movables[1];
		
		packages[0] = (Package)movables[PACKAGE_INDEX];
		
		dogs[0] = (Dog) movables[4];
		
		mailboxes[0] = (Mailbox) movables[3];
		
		screen.clearTargets();
		
		for(int i = 0; i < movables.length; i ++) {
			if(movables[i] != null && !movables[i].isDog() && !movables[i].isMailbox()&& !movables[i].isPackage()) {
				screen.addTarget(movables[i]);
			}
		}
		
		for(int i = 0; i < movables.length; i ++) {
			if(movables[i] != null) {
				movables[i].addGame(this);
			}
		}
		
		contentPane1.requestFocus();
		contentPane1.addKeyListener(this);
		contentPane1.addKeyListener(players[0]);
		contentPane1.addKeyListener(players[1]);
		contentPane1.addMouseListener(packages[0]);
		
		
		
		
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
			score += ((double)TIME_STEP / 1000);
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
	
	public void pauseGame()
	{
		
		
		pauseWindow = new JWindow(gameFrame);
		pauseWindow.setSize(1280/2, 500);
		pauseWindow.setLocationRelativeTo(null);
		
		//pauseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//pauseFrame.setBackground(new Color(213, 134, 145, 123));
		//pauseFrame.setUndecorated(true);
		pauseWindow.setBackground(new Color(0,0,0,123));
		
		contentPane = pauseWindow.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.WHITE);
		gameFrame.setVisible(true);
		
		JPanel panel = new pauseImage();
		panel.setBackground(new Color(0, 0, 0, 0));
		
		JButton contbtn = new JButton();
		try {
		    BufferedImage img = ImageIO.read(new File("Sprites/continue.png"));
		    BufferedImage img2 = ImageIO.read(new File("Sprites/continuehover.png"));
		    contbtn.setIcon(new ImageIcon(img));
		    contbtn.setRolloverIcon(new ImageIcon(img2));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		
		contbtn.setBorderPainted(false); 
		contbtn.setContentAreaFilled(false); 
		contbtn.setFocusPainted(false); 
		contbtn.setOpaque(false);
		panel.setLayout(null);
		Insets insets = panel.getInsets();
		Dimension size = contbtn.getPreferredSize();
		contbtn.setBounds( insets.right, 390 + insets.top,size.width+15, size.height+15);
		contbtn.addActionListener(this);
		contbtn.setActionCommand("UnPause");
		panel.add(contbtn);
		
		JButton menubtn = new JButton();
		try {
		    BufferedImage img = ImageIO.read(new File("Sprites/backtomenu.png"));
		    BufferedImage img2 = ImageIO.read(new File("Sprites/backtomenuhover.png"));
		    menubtn.setIcon(new ImageIcon(img));
		    menubtn.setRolloverIcon(new ImageIcon(img2));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		
		menubtn.setBorderPainted(false); 
		menubtn.setContentAreaFilled(false); 
		menubtn.setFocusPainted(false); 
		menubtn.setOpaque(false);
		panel.setLayout(null);
		menubtn.setBounds(300 + insets.right, 390 + insets.top,size.width+15, size.height+15);
		menubtn.addActionListener(this);
		menubtn.setActionCommand("BackMenu");
		panel.add(menubtn);
		
		pauseWindow.setContentPane(panel);
		pauseWindow.setVisible(true);
		
		gameIsReady = false;
	}
	
	public void levelComplete()
	{
		
		
		pauseWindow = new JWindow(gameFrame);
		pauseWindow.setSize(1280/2, 500);
		pauseWindow.setLocationRelativeTo(null);
		
		//pauseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//pauseFrame.setBackground(new Color(213, 134, 145, 123));
		//pauseFrame.setUndecorated(true);
		pauseWindow.setBackground(new Color(0,0,0,123));
		
		contentPane = pauseWindow.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.WHITE);
		gameFrame.setVisible(true);
		
		JPanel panel = new completedImage();
		panel.setDoubleBuffered(true);
		panel.setBackground(new Color(0, 0, 0, 0));
		
		JButton contbtn = new JButton();
		try {
		    BufferedImage img = ImageIO.read(new File("Sprites/continue.png"));
		    BufferedImage img2 = ImageIO.read(new File("Sprites/continuehover.png"));
		    contbtn.setIcon(new ImageIcon(img));
		    contbtn.setRolloverIcon(new ImageIcon(img2));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		
		contbtn.setBorderPainted(false); 
		contbtn.setContentAreaFilled(false); 
		contbtn.setFocusPainted(false); 
		contbtn.setOpaque(false);
		panel.setLayout(null);
		Insets insets = panel.getInsets();
		Dimension size = contbtn.getPreferredSize();
		contbtn.setBounds( insets.right, 390 + insets.top,size.width+15, size.height+15);
		contbtn.addActionListener(this);
		contbtn.setActionCommand("NextLevel");
		panel.add(contbtn);
		
		JButton menubtn = new JButton();
		try {
		    BufferedImage img = ImageIO.read(new File("Sprites/backtomenu.png"));
		    BufferedImage img2 = ImageIO.read(new File("Sprites/backtomenuhover.png"));
		    menubtn.setIcon(new ImageIcon(img));
		    menubtn.setRolloverIcon(new ImageIcon(img2));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		
		menubtn.setBorderPainted(false); 
		menubtn.setContentAreaFilled(false); 
		menubtn.setFocusPainted(false); 
		menubtn.setOpaque(false);
		panel.setLayout(null);
		menubtn.setBounds(300 + insets.right, 390 + insets.top,size.width+15, size.height+15);
		menubtn.addActionListener(this);
		menubtn.setActionCommand("BackMenu");
		panel.add(menubtn);
		
		pauseWindow.setContentPane(panel);
		pauseWindow.setVisible(true);
		
		gameIsReady = false;
	}
	
	public void gameOver()
	{
		
		
		pauseWindow = new JWindow(gameFrame);
		pauseWindow.setSize(1280/2, 500);
		pauseWindow.setLocationRelativeTo(null);
		
		//pauseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//pauseFrame.setBackground(new Color(213, 134, 145, 123));
		//pauseFrame.setUndecorated(true);
		pauseWindow.setBackground(new Color(0,0,0,123));
		
		contentPane = pauseWindow.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.WHITE);
		gameFrame.setVisible(true);
		
		JPanel panel = new gameOverImage();
		panel.setDoubleBuffered(true);
		panel.setBackground(new Color(0, 0, 0, 0));
		
		JButton restartbtn = new JButton();
		try {
		    BufferedImage img = ImageIO.read(new File("Sprites/restart.png"));
		    BufferedImage img2 = ImageIO.read(new File("Sprites/restarthover.png"));
		    restartbtn.setIcon(new ImageIcon(img));
		    restartbtn.setRolloverIcon(new ImageIcon(img2));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		
		restartbtn.setBorderPainted(false); 
		restartbtn.setContentAreaFilled(false); 
		restartbtn.setFocusPainted(false); 
		restartbtn.setOpaque(false);
		panel.setLayout(null);
		Insets insets = panel.getInsets();
		Dimension size = restartbtn.getPreferredSize();
		restartbtn.setBounds( insets.right, 390 + insets.top,size.width+15, size.height+15);
		restartbtn.addActionListener(this);
		restartbtn.setActionCommand("Restart");
		panel.add(restartbtn);
		
		JButton menubtn = new JButton();
		try {
		    BufferedImage img = ImageIO.read(new File("Sprites/backtomenu.png"));
		    BufferedImage img2 = ImageIO.read(new File("Sprites/backtomenuhover.png"));
		    menubtn.setIcon(new ImageIcon(img));
		    menubtn.setRolloverIcon(new ImageIcon(img2));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		
		menubtn.setBorderPainted(false); 
		menubtn.setContentAreaFilled(false); 
		menubtn.setFocusPainted(false); 
		menubtn.setOpaque(false);
		panel.setLayout(null);
		menubtn.setBounds(300 + insets.right, 390 + insets.top,size.width+15, size.height+15);
		menubtn.addActionListener(this);
		menubtn.setActionCommand("BackMenu");
		panel.add(menubtn);
		
		pauseWindow.setContentPane(panel);
		pauseWindow.setVisible(true);
		
		gameIsReady = false;
	}
	
	
	
	public void unpauseGame()
	{
		gameIsReady = true;
		pauseWindow.setVisible(false);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			if (gameIsReady) {
				pauseGame();
			}
		}
	}
	
	public String[] getMaps() {
		File dir = new File("Maps");
		
		Collection<String> files = new ArrayList<String>();
		
		if(dir.isDirectory()){
	        File[] listFiles = dir.listFiles();
	        for(File file : listFiles){
	        	if(file.isFile()) {
	        		files.add(file.getPath());
	        	}
	        }
		}
		
        return files.toArray(new String[]{});
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		;
	}
	
	private class menuImage extends JPanel {
		@Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        BufferedImage img;
			try {
	        img = ImageIO.read(new File(menuImage));
	        if (img != null)
	            g.drawImage(img, 0, 0,700,700, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	private class pauseImage extends JPanel {
		@Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        BufferedImage img;
			try {
		    int randomNum = ThreadLocalRandom.current().nextInt(0, 3);
	        img = ImageIO.read(new File(pauseImage[randomNum]));
	        if (img != null)
	            g.drawImage(img, 60, 0,500,400, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	private class completedImage extends JPanel {
		@Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        BufferedImage img;
			try {
	        img = ImageIO.read(new File(levelComplete));
	        if (img != null)
	            g.drawImage(img, 60, 0,500,400, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	private class gameOverImage extends JPanel {
		@Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        BufferedImage img;
			try {
			int randomNum = ThreadLocalRandom.current().nextInt(0, 3);
		    img = ImageIO.read(new File(gameOver[randomNum]));
	        if (img != null)
	            g.drawImage(img, 60, 0,500,400, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	private class helpImage extends JPanel {
		
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        BufferedImage img;
			try {
	        img = ImageIO.read(new File(helpImage[0]));
	        if (img != null)
	            g.drawImage(img, 0, 0,770,645, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	private class helpImage2 extends JPanel {
		
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        BufferedImage img;
			try {
	        img = ImageIO.read(new File(helpImage[1]));
	        if (img != null)
	            g.drawImage(img, 0, 0,770,645, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	private class getImage extends JPanel {
		
		public getImage(Graphics g, String spritename)
		{
			paintComponent(g,spritename);
		}
	    public void paintComponent(Graphics g, String sprite) {
	        super.paintComponent(g);
	        BufferedImage img;
			try {
	        img = ImageIO.read(new File(sprite));
	        if (img != null)
	            g.drawImage(img, 0, 0,700,700, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}

}
