
import java.util.List;
import java.util.ArrayList;

public class Note {
    
    private Note prev;
    private Note next;
    private int note;
    
    public Note(Note prev, int note) {
        this.prev = prev;
        if (prev != null) prev.next = this;
        this.note = note;
    }
    
    public List<Fingering> getPossibleFingerings() {
        List<Fingering> result = new ArrayList<Fingering>();
        
        // Absolute bogus
        result.add(new Fingering(note, 1, 5, 2));
        result.add(new Fingering(note, 2, 5, 2));
        result.add(new Fingering(note, 3, 5, 2));
        
        return result;
    }
    
}
