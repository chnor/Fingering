import java.io.*;
import java.util.*;

import javax.sound.midi.*;
import org.jfugue.*;


public class ReadMidi {

	public static int noteNameToNumber(String noteName) {
		
		return 0;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File menuet = new File("C:/Users/Vladimir/Documents/dkand/Bach_Suite_no4_BWV1006a_Menuet1.mid");
		
		MidiParser parser = new MidiParser();
		//MusicStringRenderer renderer = new MusicStringRenderer();
		//parser.addParserListener(renderer);
		JFParserListener listener = new JFParserListener();
		parser.addParserListener(listener);
		
		try {
			parser.parse(MidiSystem.getSequence(menuet));
		}
		catch(InvalidMidiDataException e) {}
		catch(IOException e) {}
		
		ArrayList<JFMidiEvent> midiEvents = listener.getEvents();
		Collections.sort(midiEvents);
		for(JFMidiEvent e : midiEvents) {
			System.out.print(e.voice + " " + e.time + " ");
			if(e instanceof JFNoteEvent) {
				System.out.println(((JFNoteEvent) e).note + " " + ((JFNoteEvent) e).duration);
			}
			else if(e instanceof JFTempoEvent) {
				System.out.println(((JFTempoEvent) e).tempo);
			}
		}
		
		//listener.printCounters();
		
		/*String[] tokens = renderer.getPattern().getTokens();
		String[] split;
		
		int currentVoice = 0;
		int currentTime = 0;
		for(int i = 0; i < tokens.length; i++) {
			System.out.println(tokens[i]);
			if(tokens[i].startsWith("V")) {
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
				//midiEvents.add(new JFNoteEvent(currentTime, currentVoice, noteNameToNumber(split[0]), Double.parseDouble(split[1])));
			}
		}*/
	}

}
