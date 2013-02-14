

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

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
        List<Note> notes = new ArrayList<Note>();
        
        // Simple test: C scale
        notes.add(new Note(48, 200));
        notes.add(new Note(50, 200));
        notes.add(new Note(52, 200));
        notes.add(new Note(53, 200));
        notes.add(new Note(55, 200));
        notes.add(new Note(57, 200));
        notes.add(new Note(59, 200));
        notes.add(new Note(60, 200));
        
        notes.add(new Note(53, 200));
        notes.add(new Note(55, 200));
        notes.add(new Note(57, 200));
        notes.add(new Note(59, 200));
        notes.add(new Note(60, 200));
        notes.add(new Note(62, 200));
        notes.add(new Note(64, 200));
        notes.add(new Note(65, 200));
        
        List<List<Fingering>> layers = new ArrayList<List<Fingering>>();
        for (Note note : notes) {
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
