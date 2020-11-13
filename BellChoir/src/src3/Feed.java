package src3;

import javax.sound.sampled.SourceDataLine;

public class Feed implements Runnable{
    public static final int SAMPLE_RATE = 48 * 1024; // ~48KHz
    public static final int MEASURE_LENGTH_SEC = 1;
	
	private SourceDataLine line;
	private float[] fs = new float[MEASURE_LENGTH_SEC * SAMPLE_RATE];
	
	private int numVoices = 0;
	
	public Feed(SourceDataLine sdl) {
		line = sdl;
	}
	
	public void add(FeedSample feedSample) {
		numVoices ++;
	}

	@Override
	public void run() {
		
	}
}

class FeedSample {
	
	private float[] s;
	private int maxVal;
	
	public FeedSample(float[] sample, int maximum) {
		maxVal = maximum;
		s = sample;
	}
}
