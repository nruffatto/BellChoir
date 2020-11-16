package src3;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Tone3 {

	public static ArrayList<Note> noteList = new ArrayList<>();
	
	public static ArrayList<Note> notesUsed = new ArrayList<>();
    // Mary had a little lamb
	private static final ArrayList<BellNote> song = new Song("The_Shire.txt").toList();
 
    private final AudioFormat af;

    Tone3(AudioFormat af) {
        this.af = af;
        for (BellNote bn : song) {
        	noteList.add(bn.note);
        }
        for(Note n : noteList) { // Fills notesUsed with unique notes
        	boolean noteExists = false;
        	for(Note m : notesUsed) {
        		if(n.name().equals(m.name())) {
        			noteExists = true;
        		}
        	}
        	if (!noteExists) {
        		notesUsed.add(n);
        	}
        }
    }

    void playSong(Player[] players) throws LineUnavailableException {
        try (final SourceDataLine line = AudioSystem.getSourceDataLine(af)) {
            line.open();
            line.start();



            for (BellNote bn : song) {
//                System.out.println("(main:) "+Thread.currentThread()+" heading into giveTurn function");
                for (Player p : players) {
                	if (p.myNote.name().equals(bn.note.name())) {
                        p.giveTurn(bn.length, line); //run a function of player p
                	}
                }
            }
            line.drain();
        }
    }

//    private void playNote(SourceDataLine line, BellNote bn) {
//        final int ms = Math.min(bn.length.timeMs(), Note.MEASURE_LENGTH_SEC * 1000);
//        final int actualLength = Note.SAMPLE_RATE * ms / 1000;
//        line.write(bn.note.sample(), 0, actualLength);
//        line.write(Note.REST.sample(), 0, 50);
//    }

    public static void main(String[] args) {  
        // Create all the players, and give each a turn
    	final AudioFormat af = new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, false);
  		Tone3 t = new Tone3(af);
    	
        final int numPlayers = notesUsed.size();

//        System.out.println("This thread: "+Thread.currentThread()+" (name,priority,nameGroup)");              	
//        System.out.println(" will create Players (each of which creates its own thread)");              	
        Player[] players = new Player[numPlayers];
        for (int i = 0; i < notesUsed.size(); i++) {
            players[i] = new Player(notesUsed.get(i));
        }
        
        try {
			t.playSong(players);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
        
//        System.out.println("(main:) "+Thread.currentThread()+" will stop the Players ");              	
        for (Player p : players) {
            p.stopPlayer();
        }
//        System.out.println(Thread.currentThread()+" will kill the Players ");              	
        for (Player p : players) {
            p.killPlayer();
        }
    }
//    public static void main(String[] args) throws Exception {
//        final AudioFormat af =
//            new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, false);
//        Tone3 t = new Tone3(af);
//        t.playSong(song);
//    }
}

class Player implements Runnable {

    public final Note myNote;
    private final Thread t;
    private NoteLength currentLength;
    private boolean myTurn = false;
    private int turnCount;
    public SourceDataLine sdl;

    Player(Note n) {
        this.myNote = n;
        turnCount = 1;
        t = new Thread(this);
        t.start();
    }

    public void stopPlayer() {
        t.interrupt();
    }
    
	public void killPlayer() {
	     try {
	         t.join();
//	         System.out.println("Player "+myNote.name() + " is dead.");
	     } catch (InterruptedException e) {
//	         System.err.println("Interrupted while trying to kill Player "+myNote.name());
	     }
	 }

    public void giveTurn(NoteLength nl, SourceDataLine l) { // usually, the main thread runs this to set private data myTurn for this player thread
    	this.currentLength = nl;
    	this.sdl = l;
        synchronized (this) {
            if (myTurn) {
//                throw new IllegalStateException("Attempt to give a turn to a player who's hasn't completed the current turn");
            }
//            System.out.println("(giveTurn:) "+Thread.currentThread()+" is setting Player " + myNote.name() + " to take a turn");              	
            myTurn = true;            	
            // I have set this players's myTurn so now tell it to go (or eventually, go)
            notify();  
            if (myTurn) { // if player thread is not done yet, 
            			  // this thread (probably main) should wait
                try {
//                    System.out.println("(giveTurn:) Now "+Thread.currentThread()+ " is waiting.");              	
                    wait();
                } catch (InterruptedException exc) {
//                	System.out.println("(giveTurn:) Interrupted while waiting for "+myNote.name()+" to finish turn.");
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
//                        System.out.println("(run:) "+Thread.currentThread()+" for player "+ myNote.name() + " is waiting.");              	
                        wait();
                    }
                    // My turn!
                    doTurn();

                    // Done, finished turn and now wake up one waiting thread
                    myTurn = false;
                    notify();
                } catch (InterruptedException exc) {
//                	System.out.println("(run:) Interrupted "+myNote.name());
                	break;
                }
            }
        }
    }

    private void doTurn() {
        System.out.println(Thread.currentThread().getName() + " plays " + myNote.name());
        final int ms = Math.min(currentLength.timeMs(), Note.MEASURE_LENGTH_SEC * 1000);
        final int actualLength = Note.SAMPLE_RATE * ms / 1000;
        sdl.write(myNote.sample(), 0, actualLength);
    }
}

class BellNote {
    final Note note;
    final NoteLength length;

    BellNote(Note note, NoteLength length) {
        this.note = note;
        this.length = length;
    }
}

class NoteLength {
//    WHOLE(1.0f),
//    HALF(0.5f),
//    QUARTER(0.25f),
//    EIGHTH(0.125f),
//    nth16(0.0625f);
	private final double speed = 1.1;

    private final int timeMs;
    
    double length;
    
    public NoteLength(String s) {
    	double d;
    	try {
    		d = 1.0 / Double.parseDouble(s);
    	}catch (NumberFormatException nfe) {
    		d = 0;
    	}
    	length = d;
        timeMs = (int)(length * Note.MEASURE_LENGTH_SEC * 1000 * (1.0/speed));
    }

    public int timeMs() {
        return timeMs;
    }
    
    public double getValue() {
    	return length;
    }
    
    public String toString() {
    	return length + "";
    }
}

class Note {
	private String[] notes = {"A", "AS", "B", "C", "CS", "D", "DS", "E", "F", "FS", "G", "GS"};
	
	private String[] noteKeys = {
			"AF", "A", "AS", 
			"BF", "B", "BS", 
			"CF", "C", "CS", 
			"DF", "D", "DS", 
			"EF", "E", "ES", 
			"FF", "F", "FS", 
			"GF", "G", "GS", 
			"REST"
			};
	
	private ArrayList<String> noteKeysList = new ArrayList<>();
	
	private int[] noteValues = {
			-1, 0, 1, 
			1, 2, 3,
			2, 3, 4,
			4, 5, 6,
			6, 7, 8,
			7, 8, 9,
			9, 10, 11,
			50
			};
	
//	private TreeMap<String, Integer> map = new TreeMap<>();
	
    public static final int SAMPLE_RATE = 48 * 1024; // ~48KHz
    public static final int MEASURE_LENGTH_SEC = 1;

    // Circumference of a circle divided by # of samples
    private static final double step_alpha = (2.0 * Math.PI) / SAMPLE_RATE;

    private final double FREQUENCY_A_HZ = 440;
    private final double MAX_VOLUME = 127.0;

    private final byte[] sinSample = new byte[MEASURE_LENGTH_SEC * SAMPLE_RATE];
    
    private int n;
    
    private int magnitude = 0;

    public void NoteInt(int noteNum) {
    	n = noteNum;
    	if(n > -57 && n < 48) {
	        // Calculate the frequency!
	        final double freq = FREQUENCY_A_HZ * Math.pow(2.0, (n) / 12.0);
	        final double freq2 = FREQUENCY_A_HZ * Math.pow(2.0, (n - 12) / 12.0);
	        final double freq3 = FREQUENCY_A_HZ * Math.pow(2.0, (n + 12) / 12.0);
	
	        // Create sinusoidal data sample for the desired frequency
	        final double sinStep = freq * step_alpha;
	        final double sinStep2 = freq2 * step_alpha;
	        final double sinStep3 = freq3 * step_alpha;
	        for (int i = 0; i < sinSample.length; i++) {
	            sinSample[i] = (byte)((
	            		Math.sin(i * sinStep)
//	            		+ Math.sin(i * sinStep2)
//	            		+ Math.sin(i * sinStep3)
	            		) * MAX_VOLUME  * (1.0/3.0)
//	            		* Math.pow(2, -0.001 * i) // short xylo
	            		* Math.pow(2, -0.00025 * i) // long xylo
//	            		* (1 - Math.pow(2, -0.001 * i)) // short organ
//	            		* (1 - Math.pow(2, -0.0001 * i)) // long organ
	            		);
	        }
    	}
    }
    
    public Note(String noteString) {
    	try {
    		NoteInt(Integer.parseInt(noteString));
    		magnitude = n / 12 + 4;
    	}catch (NumberFormatException nfe){
        	fillKeys();
        	if (noteString.strip().equals("REST")){
        		n = 50;
        	} else {
            	String letter = "";
            	noteString = noteString.toUpperCase();
            	for (int i = 0; i < noteString.length(); i++) {
            		char c = noteString.charAt(i);
            		if ((int)c >= 65 && (int)c <= 90) {
            			letter += c;
            		}else if ((int)c >= 48 && (int)c <= 57) {
            			magnitude = Integer.parseInt(c + "");
            		}
            	}
            	n = 12 * (magnitude - 4) + noteValues[noteKeysList.indexOf(letter)];
            	NoteInt(n);
        	}
    	}
    }
    
    private void fillKeys() {
    	for (String nk : noteKeys) {
    		noteKeysList.add(nk);
    	}
    }

	public byte[] sample() {
        return sinSample;
    }
    
    public String name() {
    	int index = n % 12;
    	if (index<0) {
    		index = (144 - index) % 12;
    	} else if(index == 50) {
    		return "REST";
    	}
    	String name = notes[index].charAt(0)+"";
    	name += (magnitude) + "";
    	if (notes[index].length() == 2) {
    		name += notes[index].charAt(1)+"";
    	}
    	return name;
    }
    
    public String toString() {
    	return n + "";
    }
}