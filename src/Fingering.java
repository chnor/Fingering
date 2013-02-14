

import java.lang.Math;

public class Fingering {
    
    private int note;
    private int string;
    /**
     *  Hand position.
     */
    private int position;
    private int finger;
    private long duration;
    
    /**
     *  Modifier to avoid higher frets.
     */
    private static final double FRET_HEIGHT_PENALTY = 0.2;
    
    
    /**
     *  Penalty for hand movements during open notes or pauses.
     */
    private static final double HAND_MOVE_DURING_OPEN_NOTE_PENALTY = 1;
    private static final double HAND_MOVE_WITH_GUIDE_FINGER_PENALTY = 1.2;
    private static final double HAND_MOVE_WITHOUT_GUIDE_FINGER_PENALTY = 3;
    
    private static final double VERTICAL_MOVEMENT_PENALTY = 0.5;
    
    public Fingering(int note, int string, int position, int finger, long duration) {
        this.note = note;
        this.string = string;
        this.position = position;
        this.finger = finger;
        this.duration = duration;
    }
    
    public int calculateInitiateCost() {
        int result = 0;
        
        result += FRET_HEIGHT_PENALTY * position;
        
        return result;
    }
    
    public int calculateReleaseCost() {
        int result = 0;
        
        // TODO
        
        return result;
    }
    
    public int calculateTransitionCost(Fingering nextFingering) {
        int result = 0;
        
        int horDisplacement = java.lang.Math.abs(position - nextFingering.position);
        int vertDisplacement = java.lang.Math.abs(this.string - nextFingering.string);
        
        if (finger == 0 || nextFingering.finger == 0) {
            result += HAND_MOVE_DURING_OPEN_NOTE_PENALTY * horDisplacement;
        } else if (finger == nextFingering.finger && string == nextFingering.string) {
            result += HAND_MOVE_WITH_GUIDE_FINGER_PENALTY * horDisplacement;
        } else {
            result += HAND_MOVE_WITHOUT_GUIDE_FINGER_PENALTY * horDisplacement;
        }
        result += VERTICAL_MOVEMENT_PENALTY * vertDisplacement;
        
        return result;
    }
    
    public String toString() {
        return "Fingering: (" + note + ", " + string + ", " + position + ", " + finger + ")";
    }
    
}
