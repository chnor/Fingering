
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
        
        notes.add(new Note(null, 40));
        notes.add(new Note(notes.get(notes.size() - 1), 42));
        notes.add(new Note(notes.get(notes.size() - 1), 44));
        
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
