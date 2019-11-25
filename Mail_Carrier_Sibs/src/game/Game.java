package game;
import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
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
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends TimerTask implements MouseListener, ActionListener, KeyListener{

	public static void main(String[] args) {
		Game g1 = new Game();
	}
	

	public static final int DEFAULT_BLOCK_SIZE = 64;
	public static final int TIME_STEP = 30;
	public static final int PACKAGE_INDEX = 2;
	
	public JFrame gameFrame;
	public JFrame pauseFrame;
	public Map map = new Map(1, 1);
	private String[] mapList = {"tunnel.txt","m2.txt", "mappy.txt"};
	public Screen screen;
	public Movable[] movables = new Movable[10];
	public Player[] players = new Player[2];
	public Package[] packages = new Package[1];
	public Dog[] dogs = new Dog[1];
	public Mailbox[] mailboxes = new Mailbox[1];
	private Container contentPane;
	private Container contentPane1;
	private Timer timer;
	
	private boolean gameIsReady;
	public int LevelNumber = mapList.length;
	public int currentLevelIndex;
	public JButton[] LevelButtons = new JButton[LevelNumber];
	public JButton[] MenuButtons = new JButton[4];
	
	private boolean levelpanel;
	public static final Color LIGHT_BLUE = new Color(51,204,255);
	
	public String MenuImage = "Sprites/logo.png";
	public String LevelComplete = "Sprites/levelcomplete.png";
	public String[] PauseImage = {"Sprites/PauseImage1.png","Sprites/PauseImage2.png","Sprites/PauseImage3.png"};
	
	public double score = 0;
	
	public boolean paused = false;
	
	public Font customFont;
	
	public Game() {
		gameIsReady = false;
		gameFrame = new JFrame("Super Mail Carrier Sibs");
		gameFrame.setSize(1280, 720);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		JPanel panel = new MenuImage();
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
		MenuButtons[0] = startbtn;
		panel.add(startbtn);
		
		
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
		exitbtn.setBounds(800 + insets.right, 300 + insets.top,size.width+50, size.height+25);
		exitbtn.addActionListener(this);
		exitbtn.setActionCommand("Exit");
		MenuButtons[1] = exitbtn;
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
		panel.setPreferredSize(Dimension (30,30));
		panel.setBackground(LIGHT_BLUE);

		for(int i=0 ; i<LevelNumber ; i++){
		    JButton btn = new JButton("Level " + String.valueOf(i+1));
		    LevelButtons[i] = btn;
		    //btn.setPreferredSize(new Dimension(40, 40));
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
		//panel.setLayout(null);
		Insets insets = panel.getInsets();
		Dimension size = backbtn.getPreferredSize();
		backbtn.setBounds(800 + insets.right, 300 + insets.top,size.width+50, size.height+25);
		backbtn.addActionListener(this);
		backbtn.setActionCommand("Back");
		MenuButtons[1] = backbtn;
		//panel.add(backbtn);
		
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
        	
        	MenuButtons[0].setVisible(false);
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
        	pauseFrame.setVisible(false);
        	LoadMenu();
        }
        
        if (action.equals("UnPause"))
        {
        	unpauseGame();
        }
        
        if (action.equals("NextLevel"))
        {
        	StartLevel(mapList[currentLevelIndex+1],currentLevelIndex+1);
        	
        }
       
        for(int i=0 ; i<LevelNumber ; i++)
        {
        	if (action.equals(mapList[i]))
                {
                	StartLevel(mapList[i],i);
                	for(int j=0 ; j<LevelNumber ; j++)
                	{
                		LevelButtons[j].setFocusable(false);
                    	LevelButtons[j].setVisible(false);
                    }
                }
        }
                
        
    }

	
	public void StartLevel(String level, int index) {
		
		contentPane1 = gameFrame.getContentPane();
		contentPane1.setLayout(new BorderLayout());
		contentPane1.setBackground(Color.WHITE);
		gameFrame.setVisible(true);
	
		openMap(level);
		
		currentLevelIndex = index;
		
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
		}
		score += ((double)TIME_STEP / 1000);
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
		
		
		pauseFrame = new JFrame("Super Mail Carrier Sibs");
		pauseFrame.setSize(1280/2, 500);
		pauseFrame.setLocationRelativeTo(null);
		
		pauseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//pauseFrame.setBackground(new Color(213, 134, 145, 123));
		pauseFrame.setUndecorated(true);
		pauseFrame.setBackground(new Color(0,0,0,123));
		
		contentPane = pauseFrame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.WHITE);
		gameFrame.setVisible(true);
		
		JPanel panel = new PauseImage();
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
		contbtn.setBounds(25 + insets.right, 400 + insets.top,size.width+10, size.height+10);
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
		menubtn.setBounds(390 + insets.right, 390 + insets.top,size.width+10, size.height+10);
		menubtn.addActionListener(this);
		menubtn.setActionCommand("BackMenu");
		panel.add(menubtn);
		
		pauseFrame.setContentPane(panel);
		pauseFrame.setVisible(true);
		
		gameIsReady = false;
	}
	
	public void LevelComplete()
	{
		
		
		pauseFrame = new JFrame("Super Mail Carrier Sibs");
		pauseFrame.setSize(1280/2, 500);
		pauseFrame.setLocationRelativeTo(null);
		
		pauseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//pauseFrame.setBackground(new Color(213, 134, 145, 123));
		pauseFrame.setUndecorated(true);
		pauseFrame.setBackground(new Color(0,0,0,123));
		
		contentPane = pauseFrame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.WHITE);
		gameFrame.setVisible(true);
		
		JPanel panel = new CompletedImage();
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
		contbtn.setBounds(25 + insets.right, 400 + insets.top,size.width+10, size.height+10);
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
		menubtn.setBounds(390 + insets.right, 390 + insets.top,size.width+10, size.height+10);
		menubtn.addActionListener(this);
		menubtn.setActionCommand("BackMenu");
		panel.add(menubtn);
		
		pauseFrame.setContentPane(panel);
		pauseFrame.setVisible(true);
		
		gameIsReady = false;
	}
	
	
	
	public void unpauseGame()
	{
		gameIsReady = true;
		pauseFrame.setVisible(false);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			pauseGame();

		}
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
	
	private class MenuImage extends JPanel {
		@Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        BufferedImage img;
			try {
	        img = ImageIO.read(new File(MenuImage));
	        if (img != null)
	            g.drawImage(img, 0, 0,700,700, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	private class PauseImage extends JPanel {
		@Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        BufferedImage img;
			try {
		    int randomNum = ThreadLocalRandom.current().nextInt(0, 3);
	        img = ImageIO.read(new File(PauseImage[randomNum]));
	        if (img != null)
	            g.drawImage(img, 60, 0,500,400, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	private class CompletedImage extends JPanel {
		@Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        BufferedImage img;
			try {
	        img = ImageIO.read(new File(LevelComplete));
	        if (img != null)
	            g.drawImage(img, 60, 0,500,400, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}

}
