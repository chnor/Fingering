

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
        double currentOptValue = Double.POSITIVE_INFINITY;
        Node currentOpt = null;
        for (Node node : nodes) {
            if (node.getCost() < currentOptValue) {
                currentOptValue = node.getCost();
                currentOpt = node;
            }
        }
        return currentOpt;
    }
    
    public static void printTablature(List<Fingering> fingerings) {
        
        List<StringBuilder> strings = new ArrayList<StringBuilder>();
        strings.add(new StringBuilder());
        strings.add(new StringBuilder());
        strings.add(new StringBuilder());
        strings.add(new StringBuilder());
        strings.add(new StringBuilder());
        strings.add(new StringBuilder());
        StringBuilder finger = new StringBuilder();
        StringBuilder position = new StringBuilder();
        
        long minDuration = Integer.MAX_VALUE;
        for (Fingering fingering : fingerings) {
            if (fingering.getDuration() < minDuration) {
                minDuration = fingering.getDuration();
            }
        }
        
        int currentPosition = -1;
        
        int lineWidth = 80;
        for (Fingering fingering : fingerings) {
            if (strings.get(0).length() >= lineWidth) {
                System.out.println(position.substring(0, Math.min(lineWidth, position.length())));
                for (StringBuilder string : strings) {
                    System.out.println(string.substring(0, Math.min(lineWidth, string.length())));
                    string.setLength(0);
                }
                System.out.println(finger.substring(0, Math.min(lineWidth, finger.length())));
                finger.setLength(0);
                System.out.println();
                position.setLength(0);
            }
            
            for (int i = 0; i < 6; i++) {
                if (i == fingering.getString() - 1) {
                    strings.get(i).append(fingering.getFret());
                } else {
                    strings.get(i).append("-");
                }
            }
            finger.append(fingering.getFinger() == 0 ? " " : fingering.getFinger());
            if (fingering.getPosition() == currentPosition) {
                position.append(" ");
            } else {
                position.append(fingering.getPosition());
                currentPosition = fingering.getPosition();
            }
            for (int i = 1; i < fingering.getDuration() / minDuration; i++) {
                for (StringBuilder string : strings) {
                    string.append("-");
                }
                finger.append(" ");
                position.append(" ");
            }
        }
        System.out.println(position.substring(0, Math.min(lineWidth, position.length())));
        for (StringBuilder string : strings) {
            System.out.println(string.substring(0, Math.min(lineWidth, string.length())));
        }
        System.out.println(finger.substring(0, Math.min(lineWidth, finger.length())));
        System.out.println();
    }
    
    public static void main(String[] args) {
        /*List<ParsedNote> notes = new ArrayList<ParsedNote>();
        
        // Simple test: C scale
        notes.add(new ParsedNote(0, 48, 200));
        notes.add(new ParsedNote(0, 50, 200));
        notes.add(new ParsedNote(0, 52, 200));
        notes.add(new ParsedNote(0, 53, 200));
        notes.add(new ParsedNote(0, 55, 200));
        notes.add(new ParsedNote(0, 57, 200));
        notes.add(new ParsedNote(0, 59, 200));
        notes.add(new ParsedNote(0, 60, 200));
        
        notes.add(new ParsedNote(0, 53, 200));
        notes.add(new ParsedNote(0, 55, 200));
        notes.add(new ParsedNote(0, 57, 200));
        notes.add(new ParsedNote(0, 59, 200));
        notes.add(new ParsedNote(0, 60, 200));
        notes.add(new ParsedNote(0, 62, 200));
        notes.add(new ParsedNote(0, 64, 200));
        notes.add(new ParsedNote(0, 65, 200));*/
        
        /* <USING TEST FILE> */
        
        File menuet = new File("C:/Users/Vladimir/Documents/dkand/Bach_Suite_no4_BWV1006a_Menuet1.mid");
        //File menuet = new File("input/Bach_Suite_no4_BWV1006a_Menuet1.mid");
		
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
        
        if (notes.isEmpty()) {
            System.out.println("Failed to load input file.");
        } else {
            
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
            
            //printTablature(output);
            
            System.out.println("Position: " + output.get(0).getPosition());
           // boolean outputString = true;
            for(int i = 0; i < output.size(); i++) {
            	if(i > 0 && output.get(i).getPosition() != output.get(i - 1).getPosition()) {
            		System.out.println();
            		System.out.println("Position: " + output.get(i).getPosition());
            		//outputString = true;
            	}
            	
            	if(output.get(i).getFinger() == Fingering.OPEN)
            		System.out.println("Open string: " + output.get(i).getString());
            	else if(output.get(i).getFret() != output.get(i).getPosition() + output.get(i).getFinger() - 1)
            		System.out.println("Fin: " + output.get(i).getFinger() + ", Str: " + output.get(i).getString() /*+", Duration: " + output.get(i).getDuration() +*/ + ", Fret: " + output.get(i).getFret());
            	else //if(outputString || output.get(i).getString() != output.get(i - 1).getString())
            		System.out.println("Fin: " + output.get(i).getFinger() + ", Str: " + output.get(i).getString() /*+", Duration: " + output.get(i).getDuration() +*/);
            	//else
            	//	System.out.println("Fin: " + output.get(i).getFinger() /*+", Duration: " + output.get(i).getDuration() +*/);
            	
            	//outputString = false;
            }
            
            /*for (Fingering fingering : output) {
                System.out.println(fingering);
            }*/
        }
        
    }
}
