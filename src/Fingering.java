

import java.lang.Math;

public class Fingering {
    
    private int note;
    private int string;
    private int position;
    private int finger;
    
    public Fingering(int note, int string, int position, int finger) {
        this.note = note;
        this.string = string;
        this.position = position;
        this.finger = finger;
    }
    
    public int calculateInitiateCost() {
        int result = 0;
        
        // TODO
        
        return result;
    }
    
    public int calculateReleaseCost() {
        int result = 0;
        
        // TODO
        
        return result;
    }
    
    public int calculateTransitionCost(Fingering nextFingering) {
        int result = 0;
        
        
        // TODO
        
        result += java.lang.Math.abs(this.string - nextFingering.string);
        
        return result;
    }
    
    public String toString() {
        return "Fingering: (" + note + ", " + string + ")";
    }
    
}
