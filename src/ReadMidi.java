import java.io.*;
import java.util.*;

import javax.sound.midi.*;
import org.jfugue.*;


public class ReadMidi {
	//private static ArrayList<JFMidiEvent> midiEvents = new ArrayList<JFMidiEvent>();
	
	public static int noteNameToNumber(String noteName) {
		
		return 0;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File menuet = new File("C:/Users/Vladimir/Documents/dkand/Bach_Suite_no4_BWV1006a_Menuet1.mid");
		
		MidiParser parser = new MidiParser();
		//MusicStringRenderer renderer = new MusicStringRenderer();
		//parser.addParserListener(renderer);
		MidiParserListener listener = new MidiParserListener();
		parser.addParserListener(listener);
		
		try {
			parser.parse(MidiSystem.getSequence(menuet));
		}
		catch(InvalidMidiDataException e) {}
		catch(IOException e) {}
		
		ArrayList<ParsedNote> notes = listener.getNotes();
		Collections.sort(notes);
		for(ParsedNote n : notes) {
			System.out.println(n.time + " " + n.note + " " + n.duration);
		}
		
		//listener.printCounters();
		
		/*String[] tokens = renderer.getPattern().getTokens();
		String[] split;
		
		int currentVoice = 0;
		int currentTime = 0;
		for(int i = 0; i < tokens.length; i++) {
			System.out.println(tokens[i]);
			/*if(tokens[i].startsWith("V")) {
				currentVoice = Integer.parseInt(tokens[i].substring(1));
			}
			else if(tokens[i].startsWith("@")) {
				currentTime = Integer.parseInt(tokens[i].substring(1));
			}
			else if(tokens[i].startsWith("T")) {
				midiEvents.add(new JFTempoEvent(currentTime, currentVoice, Integer.parseInt(tokens[i].substring(1))));
			}
			else {
				split = tokens[i].split("/");
				midiEvents.add(new JFNoteEvent(currentTime, currentVoice, noteNameToNumber(split[0]), Double.parseDouble(split[1])));
			}* /
		}*/
	}

}
