

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;

import org.jfugue.MidiParser;

public class Main {
    
    public static Node selectOptimal(List<Node> nodes) {
        int currentOptValue = Integer.MAX_VALUE;
        Node currentOpt = null;
        for (Node node : nodes) {
            if (node.getCost() < currentOptValue) {
                currentOptValue = node.getCost();
                currentOpt = node;
            }
        }
        return currentOpt;
    }
    
    public static void main(String[] args) {
        /*List<ParsedNote> notes = new ArrayList<ParsedNote>();
        
        // Simple test: C scale
        notes.add(new ParsedNote(48, 200));
        notes.add(new ParsedNote(50, 200));
        notes.add(new ParsedNote(52, 200));
        notes.add(new ParsedNote(53, 200));
        notes.add(new ParsedNote(55, 200));
        notes.add(new ParsedNote(57, 200));
        notes.add(new ParsedNote(59, 200));
        notes.add(new ParsedNote(60, 200));
        
        notes.add(new ParsedNote(53, 200));
        notes.add(new ParsedNote(55, 200));
        notes.add(new ParsedNote(57, 200));
        notes.add(new ParsedNote(59, 200));
        notes.add(new ParsedNote(60, 200));
        notes.add(new ParsedNote(62, 200));
        notes.add(new ParsedNote(64, 200));
        notes.add(new ParsedNote(65, 200));*/
        
        /* <USING TEST FILE> */
        
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
		/*for(ParsedNote n : notes) {
			System.out.println(n.getTime() + " " + n.getValue() + " " + n.getDuration());
		}*/
        
        /* </USING TEST FILE> */
        
        List<List<Fingering>> layers = new ArrayList<List<Fingering>>();
        for (ParsedNote note : notes) {
            layers.add(note.getPossibleFingerings());
        }
        
        List<List<Node>> Opt = new ArrayList<List<Node>>();
        for (List<Fingering> fingerings : layers) {
            List<Node> next_layer = new ArrayList<Node>();
            if (Opt.isEmpty()) {
                for (Fingering fingering : fingerings) {
                    next_layer.add(new Node(fingering));
                }
            } else {
                for (Fingering fingering : fingerings) {
                    List<Node> candidates = new ArrayList<Node>();
                    for (Node prev_node : Opt.get(Opt.size()-1)) {
                        candidates.add(new Node(prev_node, fingering));
                    }
                    next_layer.add(selectOptimal(candidates));
                }
            }
            Opt.add(next_layer);
        }
        
        List<Fingering> output = new ArrayList<Fingering>();
        Node current = selectOptimal(Opt.get(Opt.size()-1));
        while (current != null) {
            output.add(current.getFingering());
            current = current.getPrev();
        }
        Collections.reverse(output);
        
        for (Fingering fingering : output) {
            System.out.println(fingering);
        }
        
    }
}
