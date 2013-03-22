
import java.util.List;
import java.util.ArrayList;

public class ParsedNote implements Comparable<ParsedNote> {
    
    private byte note;
    private double duration;
    private long time;
    
    private static final int MIN_ALLOWED_NOTE = 40;
    private static final int MAX_ALLOWED_NOTE = 64 + 14;
    
    public ParsedNote(long time, byte note, double duration) {
    	this.time = time;
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
        
        // An array of the base notes for each string
        int [] tuning = { 64, 59, 55, 50, 45, 40 };
        int numFrets = 14; //Changed!!!
        
        List<Fingering> result = new ArrayList<Fingering>();
        
        int string = 0;
        for (int baseNote : tuning) {
            string++;
            int fret = note - baseNote;
            // If the note actually exists on this string
            if (note >= baseNote && note <= baseNote + numFrets) {
                if (note == baseNote) {
                    // Open note - hand can be anywhere along the fretboard
                    // Generate a fingering for each of the fretboard positions
                    // This is a simplistic way to account for the hand movement
                    // during the open note
                    // xogede: Can positions be negative???
                    // chnor: No, nor 0
                    for (int position = 1; position <= numFrets; position++) {
                        result.add(new Fingering(note, string, position, 0, fret, duration));
                    }
                } else {
                    // Condition is always true, but if it were left out
                    // you'd probably wonder if it's left out by mistake
                    // or not...
                    if (fret > 0) {
                        // Index finger - always on the first fret in this position
                        int position = fret;
                        result.add(new Fingering(note, string, position, 1, fret, duration));
                    }
                    // chnor: Couldn't the index finger be placed on the
                    //      first fret. Open A major for instance...
                    if (fret - 1 > 0) {
                        // Long finger - always on the second fret in this position
                        int position = fret - 1;
                        result.add(new Fingering(note, string, position, 2, fret, duration));
                    }
                    if (fret - 1 > 0) {
                        // Ring finger - on the second fret in this position
                        int position = fret - 1;
                        result.add(new Fingering(note, string, position, 3, fret, duration));
                    }
                    if (fret - 2 > 0) {
                        // Ring finger - on the third fret in this position
                        int position = fret - 2;
                        result.add(new Fingering(note, string, position, 3, fret, duration));
                    }
                    // xogede: Can the ring finger be on the fourth fret???
                    // chnor: Why not? But it's unlikely to appear since
                    //      the ring finger is avoided
                    if (fret - 3 > 0) {
                        // Ring finger - on the fourth fret in this position
                        int position = fret - 3;
                        result.add(new Fingering(note, string, position, 3, fret, duration));
                    }
                    if (fret - 1 > 0) {
                        // Pinky finger - on the second fret in this position
                        int position = fret - 1;
                        result.add(new Fingering(note, string, position, 4, fret, duration));
                    }
                    // xogede: Seems to happen a little to often?
                    // chnor: Pinky on third happens too often for my
                    //      tastes too, but it's consistent with Falk's
                    //      rule to prefer the pinky over the ring finger
                    if (fret - 2 > 0) {
                        // Pinky finger - on the third fret in this position
                        int position = fret - 2;
                        result.add(new Fingering(note, string, position, 4, fret, duration));
                    }
                    if (fret - 3 > 0) {
                        // Pinky finger - on the fourth fret in this position
                        int position = fret - 3;
                        result.add(new Fingering(note, string, position, 4, fret, duration));
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
    
    public double getDuration() {
        return duration;
    }
    
    public long getTime() {
        return time;
    }
    
    public int compareTo(ParsedNote other) {
		if(this.time - other.time < 0) return -1;
		else if(this.time - other.time > 0) return 1;
		else return 0;
	}
    
}
