package src;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Tone {
	private static BellNote current_note;
	
	private static ArrayList<Thread> taskList = new ArrayList<>();
	
	private static final List<BellNote> song = Stream.of(
            new BellNote(Note.A5, NoteLength.QUARTER),
            new BellNote(Note.F4, NoteLength.QUARTER),
            new BellNote(Note.E4, NoteLength.QUARTER),
            new BellNote(Note.A5, NoteLength.QUARTER),
            new BellNote(Note.D4, NoteLength.QUARTER),
            new BellNote(Note.G4, NoteLength.QUARTER),
            new BellNote(Note.E4, NoteLength.QUARTER),
            new BellNote(Note.A4, NoteLength.QUARTER),
            new BellNote(Note.REST, NoteLength.QUARTER),
            new BellNote(Note.A5, NoteLength.QUARTER),
            new BellNote(Note.F4, NoteLength.QUARTER),
            new BellNote(Note.C4, NoteLength.QUARTER),
            new BellNote(Note.D4, NoteLength.QUARTER),
            new BellNote(Note.G4, NoteLength.QUARTER),
            new BellNote(Note.E4, NoteLength.QUARTER),
            new BellNote(Note.A4, NoteLength.QUARTER),
            new BellNote(Note.A5, NoteLength.QUARTER),
            new BellNote(Note.F4, NoteLength.QUARTER),
            new BellNote(Note.E4, NoteLength.QUARTER),
            new BellNote(Note.A5, NoteLength.QUARTER),
            new BellNote(Note.D4, NoteLength.QUARTER),
            new BellNote(Note.G4, NoteLength.QUARTER),
            new BellNote(Note.E4, NoteLength.QUARTER),
            new BellNote(Note.A4, NoteLength.QUARTER),
            new BellNote(Note.REST, NoteLength.QUARTER),
            new BellNote(Note.A5, NoteLength.QUARTER),
            new BellNote(Note.F4, NoteLength.QUARTER),
            new BellNote(Note.C4, NoteLength.QUARTER),
            new BellNote(Note.D4, NoteLength.QUARTER),
            new BellNote(Note.G4, NoteLength.QUARTER),
            new BellNote(Note.E4, NoteLength.QUARTER),
            new BellNote(Note.A4, NoteLength.QUARTER),
            
            new BellNote(Note.REST, NoteLength.HALF)
        ).collect(Collectors.toList());
	
 
    private final AudioFormat af;

    Tone(AudioFormat af) {
        this.af = af;
    }

    void playSong(List<BellNote> song) throws LineUnavailableException {
        try (final SourceDataLine line = AudioSystem.getSourceDataLine(af)) {
        	for (BellNote bn: song) {
        		taskList.add(new Thread(new Task(bn.note, line)));
        	}
        	current_note = song.get(0);
        	for (Thread t: taskList) {
        		t.start();
        	}
            line.open();
            line.start();

            for (BellNote bn: song) {
                playNote(line, bn);
                System.out.println(bn.note+" "+bn.length);
            }
            line.drain();
        }
    }

    private void playNote(SourceDataLine line, BellNote bn) {
    	current_note = bn;
//        final int ms = Math.min(bn.length.timeMs(), Note.MEASURE_LENGTH_SEC * 1000);
//        final int actualLength = Note.SAMPLE_RATE * ms / 1000;
//        line.write(bn.note.sample(), 0, actualLength);
    }
    
    public static void main(String[] args) throws Exception {
        final AudioFormat af =
            new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, false);
        Tone t = new Tone(af);
        t.playSong(song);
    }
    
    
	private class Task implements Runnable{
		
		Note note;
		SourceDataLine line;
		public Task(Note n, SourceDataLine l) {
			note = n;
			line = l;
		}

		@Override
		public void run() {	
			Note cNote = current_note.note;
			while(cNote.equals(this.note)) {
				System.out.print(Thread.currentThread().getName() + " plays ");
				playNote(current_note.length);
			}
		}
		
		public void playNote(NoteLength length) {
	        final int ms = Math.min(length.timeMs(), Note.MEASURE_LENGTH_SEC * 1000);
	        final int actualLength = Note.SAMPLE_RATE * ms / 1000;
	        line.write(note.sample(), 0, actualLength);
		}
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

enum NoteLength {
    WHOLE(1.0f),
    HALF(0.5f),
    QUARTER(0.25f),
    EIGHTH(0.125f);

    private final int timeMs;

    private NoteLength(float length) {
        timeMs = (int)(length * Note.MEASURE_LENGTH_SEC * 1000);
    }

    public int timeMs() {
        return timeMs;
    }
}

enum Note {
    // REST Must be the first 'Note'
    REST,
    A4,
    A4S,
    B4,
    C4,
    C4S,
    D4,
    D4S,
    E4,
    F4,
    F4S,
    G4,
    G4S,
    A5;

    public static final int SAMPLE_RATE = 48 * 1024; // ~48KHz
    public static final int MEASURE_LENGTH_SEC = 1;

    // Circumference of a circle divided by # of samples
    private static final double step_alpha = (2.0 * Math.PI) / SAMPLE_RATE;

    private final double FREQUENCY_A_HZ = 440.0;
    private final double MAX_VOLUME = 127.0;

    private final byte[] sinSample = new byte[MEASURE_LENGTH_SEC * SAMPLE_RATE];

    private Note() {
        int n = this.ordinal();
        if (n > 0) {
            // Calculate the frequency!
            final double halfStepUpFromA0 = n - 1;
            final double exp0 = halfStepUpFromA0 / 12.0;
            final double freq0 = FREQUENCY_A_HZ * Math.pow(2.0, exp0);

            // Create sinusoidal data sample for the desired frequency
            final double sinStep0 = freq0 * step_alpha;
            double sampStep;
            for (int i = 0; i < sinSample.length; i++) {
            	sampStep = (Math.sin(i * sinStep0) * (-MAX_VOLUME / (sinSample.length * 0.5))*(i - sinSample.length * 0.5));
            	if (sampStep > MAX_VOLUME) {
            		sampStep = MAX_VOLUME;
            	}else if(sampStep < -MAX_VOLUME) {
            		sampStep = -MAX_VOLUME;
            	}
        		sinSample[i] = (byte)(sampStep);
            }
        }
    }

    public byte[] sample() {
        return sinSample;
    }
}