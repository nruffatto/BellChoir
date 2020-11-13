package src3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import src3.Note;
import src3.NoteLength;

public class Song {
	private ArrayList<BellNote> song = new ArrayList<>();
	private String fileName;
	private File f;
	
	public Song(String fName) {
		fileName  = fName;
		f = new File("Songs/" + fileName);
		Scanner s;
		try {
			s = new Scanner(f);
			validateSong();
			openSong(s);
			s.close();
		}catch(Exception e) {
			if(e.getClass().isInstance(new FileNotFoundException())){
				System.out.println("File not found!");
			}else if (e.getClass().isInstance(new SongFormatException())) {
				System.out.println("Invalid Song format!");
			}
		}
	}
	private void openSong(Scanner s) {
		
		while(true) {
			if(!s.hasNext()) {
				break;
			}
			Note n = new Note(s.next());
			NoteLength nl = new NoteLength(s.next());
			song.add(new BellNote(n, nl));
		}
	}
	private void validateSong() throws SongFormatException, FileNotFoundException {
		Scanner v = new Scanner(f);
		ArrayList<String> lines = new ArrayList<>();
		while(v.hasNextLine()) {
			lines.add(v.nextLine());
		}
		v.close();
		for (String line : lines) {
			v = new Scanner(line.strip());
			String pat1 = "[A-G][0-9][S,F]*\\s*[1-16]*";
			String pat2 = "[A-G][0-9]*\\s*[1-16]*";
			String pat3 = "REST*\\s*[1-16]*";
			if(!line.strip().equals("") && !(v.hasNext(Pattern.compile(pat1)) || v.hasNext(Pattern.compile(pat2)) || v.hasNext(Pattern.compile(pat3)))) {
				v.close();
				throw new SongFormatException();
			}
			v.close();
		}
	}
	public void saveSong() {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter("Songs/" + fileName));
			w.write("Start of song file");
			for (int i = 0; i < song.size(); i++) {
				BellNote bn = song.get(i);
				w.newLine();
				w.write(bn.note.toString() + " " + bn.length.toString());
			}
			w.newLine();
			w.write("end");
			w.flush();
			w.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<BellNote> toList() {
		return song;
	}
}

class SongFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
