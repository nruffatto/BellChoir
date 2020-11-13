package src2;
public class Player implements Runnable {
    private static final int NUM_TURNS = 3;
    enum PlayerName {
       A,B,C,D;
    }
    public static void main(String[] args) {  
        // Create all the players, and give each a turn
        final int numPlayers = PlayerName.values().length;

        System.out.println("This thread: "+Thread.currentThread()+" (name,priority,nameGroup)");              	
        System.out.println(" will create Players (each of which creates its own thread)");              	
        Player[] players = new Player[numPlayers];
        for (PlayerName pn : PlayerName.values()) {
            players[pn.ordinal()] = new Player(pn);
        }

        for (int i = 1; i <= NUM_TURNS; i++) {
            System.out.println("(main:) "+Thread.currentThread()+" will tell Players to take turn #"+i);              	
            for (Player p : players) {
                System.out.println("(main:) "+Thread.currentThread()+" heading into giveTurn function");              	
                p.giveTurn(); //run a function of player p
            }
        }
        System.out.println("(main:) "+Thread.currentThread()+" will stop the Players ");              	
        for (Player p : players) {
            p.stopPlayer();
        }
        System.out.println(Thread.currentThread()+" will kill the Players ");              	
        for (Player p : players) {
            p.killPlayer();
        }
    }

    private final PlayerName myName;
    private final Thread t;
    private boolean myTurn = false;
    private int turnCount;

    Player(PlayerName myName) {
        this.myName = myName;
        turnCount = 1;
        t = new Thread(this, myName.name());
        t.start();
    }

    public void stopPlayer() {
        t.interrupt();
    }
    
	public void killPlayer() {
	     try {
	         t.join();
	         System.out.println("Player "+myName.name() + " is dead.");
	     } catch (InterruptedException e) {
	         System.err.println("Interrupted while trying to kill Player "+myName.name());
	     }
	 }

    public void giveTurn() { // usually, the main thread runs this to set private data myTurn for this player thread
    	 
        synchronized (this) {
            if (myTurn) {
                throw new IllegalStateException("Attempt to give a turn to a player who's hasn't completed the current turn");
            }
            System.out.println("(giveTurn:) "+Thread.currentThread()+" is setting Player " + myName.name() + " to take a turn");              	
            myTurn = true;            	
            // I have set this players's myTurn so now tell it to go (or eventually, go)
            notify();  
            if (myTurn) { // if player thread is not done yet, 
            			  // this thread (probably main) should wait
                try {
                    System.out.println("(giveTurn:) Now "+Thread.currentThread()+ " is waiting.");              	
                    wait();
                } catch (InterruptedException exc) {
                	System.out.println("(giveTurn:) Interrupted while waiting for "+myName.name()+" to finish turn.");
                }
                //eventually will be notified and can finish and return
            }
        }
    }

    public void run() {
        synchronized (this) {
        	while (true) { // go until interrrupted
        		try {
                    // Wait for my turn to begin   
        			while (!myTurn) {
                        System.out.println("(run:) "+Thread.currentThread()+" for player "+ myName.name() + " is waiting.");              	
                        wait();
                    }
                    // My turn!
                    doTurn();
                    turnCount++;

                    // Done, finished turn and now wake up one waiting thread
                    myTurn = false;
                    notify();
                } catch (InterruptedException exc) {
                	System.out.println("(run:) Interrupted "+myName.name());
                	break;
                }
            }
        }
    }

    private void doTurn() {
        System.out.println("(doTurn:) Player[" + myName.name() + "] taking turn " + turnCount);
    }
}