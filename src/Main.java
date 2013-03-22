

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
        
        double minDuration = Double.POSITIVE_INFINITY;
        double maxDuration = Double.NEGATIVE_INFINITY;
        for (Fingering fingering : fingerings) {
            if (fingering.getDuration() < minDuration) {
                minDuration = fingering.getDuration();
            } else if (fingering.getDuration() > maxDuration) {
                maxDuration = fingering.getDuration();
            }
        }
        
        int pos_per_measure = (int)Math.round(maxDuration/minDuration);
        //pos_per_measure = 16;
        int measures_per_line = 4;
        int measureCount = 0;
        int currentPosition = -1;
        int posInMeasure = 0;
        
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
                //posInMeasure = 0;
            }
            
            for (int i = 0; i < 6; i++) {
                if (i == fingering.getString() - 1) {
                    strings.get(i).append(fingering.getFret());
                } else {
                    strings.get(i).append("-");
                }
            }
            posInMeasure++;
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
                posInMeasure++;
                
                /* XXX Measure boundary can occur here too */
                /*
                if (posInMeasure >= pos_per_measure) {
                    if (measureCount >= measures_per_line) {
                        System.out.println(position.substring(0, Math.min(lineWidth, position.length())));
                        for (StringBuilder string : strings) {
                            System.out.println(string.substring(0, Math.min(lineWidth, string.length())));
                            string.setLength(0);
                        }
                        System.out.println(finger.substring(0, Math.min(lineWidth, finger.length())));
                        finger.setLength(0);
                        System.out.println();
                        position.setLength(0);
                        measureCount = 0;
                        //posInMeasure = 0;
                    } else {
                        for (int j = 0; j < 6; j++) {
                            strings.get(j).append("|");
                        }
                        finger.append(" ");
                        position.append(" ");
                        measureCount++;
                    }
                    posInMeasure = 0;
                }
                */
            }
            
            /* Have we crossed a measure boundary */
            /*
            if (posInMeasure >= pos_per_measure) {
                if (measureCount >= measures_per_line) {
                    System.out.println(position.substring(0, Math.min(lineWidth, position.length())));
                    for (StringBuilder string : strings) {
                        System.out.println(string.substring(0, Math.min(lineWidth, string.length())));
                        string.setLength(0);
                    }
                    System.out.println(finger.substring(0, Math.min(lineWidth, finger.length())));
                    finger.setLength(0);
                    System.out.println();
                    position.setLength(0);
                    measureCount = 0;
                    //posInMeasure = 0;
                } else {
                    for (int i = 0; i < 6; i++) {
                        strings.get(i).append("|");
                    }
                    finger.append(" ");
                    position.append(" ");
                    measureCount++;
                }
                posInMeasure = 0;
            }
            */
        }
        
        System.out.println(position.substring(0, Math.min(lineWidth, position.length())));
        for (StringBuilder string : strings) {
            System.out.println(string.substring(0, Math.min(lineWidth, string.length())));
        }
        System.out.println(finger.substring(0, Math.min(lineWidth, finger.length())));
        System.out.println();
        
        /*
        System.out.println(position.substring(0, Math.min(lineWidth, position.length())));
        for (StringBuilder string : strings) {
            System.out.println(string.substring(0, Math.min(lineWidth, string.length())));
        }
        System.out.println(finger.substring(0, Math.min(lineWidth, finger.length())));
        System.out.println();
        */
    }
    
    public static void main(String[] args) {
        
        if (args.length < 1) {
            System.out.println("No input file specified");
            return;
        }
        
        //File menuet = new File("C:/Users/Vladimir/Documents/dkand/Bach_Suite_no4_BWV1006a_Menuet1.mid");
        File menuet = new File(args[0]);
		
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
        
        if (notes.isEmpty()) {
            System.out.println("Failed to load input file.");
        } else {
            
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
            
            /*
	        for(ParsedNote n : notes) {
		        System.out.println(n.getTime() + " " + n.getValue() + " " + n.getDuration());
	        }
	        */
	        
            printTablature(output);
            
            /*
             * chnor: Is this obsolete?
             */
            if (false) {
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
            }
        }
        
    }
}
