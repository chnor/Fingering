
import java.util.List;
import java.util.ArrayList;

public class Note {
    
    private int note;
    private long duration;
    
    private static final int MIN_ALLOWED_NOTE = 40;
    private static final int MAX_ALLOWED_NOTE = 64 + 12;
    
    public Note(int note, long duration) {
        this.note = note;
        this.duration = duration;
        if(note < MIN_ALLOWED_NOTE) {
            throw new IllegalArgumentException("Note is not available on common fretboards: " + note);
        }
        if(note > MAX_ALLOWED_NOTE) {
            throw new IllegalArgumentException("Note is not available on common fretboards: " + note);
        }
    }
    
    public List<Fingering> getPossibleFingerings() {
        
        int [] tuning = { 64, 59, 55, 50, 45, 40 };
        int [] availableFingers = { 1, 2, 3, 4 };
        int numFrets = 12;
        
        List<Fingering> result = new ArrayList<Fingering>();
        
        int string = 0;
        for (int baseNote : tuning) {
            string++;
            int fret = note - baseNote;
            if (note >= baseNote && note <= baseNote + numFrets) {
                if (note == baseNote) {
                    // Open note
                    for (int position = 1; position <= numFrets; position++) {
                        result.add(new Fingering(note, string, position, 0, duration));
                    }
                } else {
                    for (int finger : availableFingers) {
                        int position = fret - finger;
                        result.add(new Fingering(note, string, position, finger, duration));
                    }
                }
            }
        }
        
        if(result.isEmpty()) {
            throw new IllegalArgumentException("Note is not available on the specified fretboard: " + note);
        }
        return result;
    }
    
    public int getValue() {
        return note;
    }
    
    public long getDuration() {
        return duration;
    }
    
}
