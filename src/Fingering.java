
import java.lang.Math;

public class Fingering {
    
    private int note;
    private int string;
    
    /**
     *  Hand position.
     */
    private int position;
    private int finger;
    private int fret;
    private double duration;
    
    public static final int OPEN  = 0;
    public static final int INDEX = 1;
    public static final int LONG  = 2;
    public static final int RING  = 3;
    public static final int PINKY = 4;
    
    /*
     * Apply a penalty inversely proportional to the
     * length of the note.
     * DURATION_PENALTY = 1 applies the whole penalty
     * DURATION_PENALTY = 0 applies no penalty
     */
    private static final double DURATION_PENALTY = 1;
    
    /*Added:*/
    private static final double FRETBOARD_LENGTH = 64.9; //64.77; //cm
    private static final double STRING_SPACING = 0.9; //cm
    // http://liutaiomottola.com/formulae/fret.htm
    private static final double[] FRET_POSITIONS = {0, 0.056126, 0.109101, 0.159104, 0.206291, 0.250847, 0.292893, 0.33258, 0.370039, 0.405396, 0.438769, 0.470268, 0.5, 0.528063, 0.554551, 0.579552, 0.60315, 0.625423, 0.646447, 0.66629, 0.68502, 0.702698, 0.719385, 0.735134, 0.75}; //fractions of fretboard length
    private static final double[][] RELAXED_FINGER_DISTANCES =
    { // --- Index Long Ring Pinky
    	{0.0, 0.0, 0.0, 0.0, 0.0}, // (Not used)
    	{0.0, 0.0, 3.5, 5.7, 7.4}, // To index finger
    	{0.0, 3.5, 0.0, 2.3, 4.9}, // To long finger
    	{0.0, 5.7, 2.3, 0.0, 2.1}, // To ring finger
    	{0.0, 7.4, 4.9, 2.1, 0.0}  // To pinky finger
    }; //cm. Totally non-scientific.
    private static final double[][] CLOSE_FINGER_DISTANCES =
    /*{ // --- Index Long Ring Pinky
       	{0.0, 0.0, 0.0, 0.0, 0.0}, // (Not used)
       	{0.0, 0.0, 1.8, 3.0, 4.4}, // To index finger
       	{0.0, 1.8, 0.0, 1.2, 2.6}, // To long finger
       	{0.0, 3.0, 1.2, 0.0, 1.4}, // To ring finger
       	{0.0, 4.4, 2.6, 1.4, 0.0}  // To pinky finger
    };*/
    { // --- Index Long Ring Pinky
       	{0.0, 0.0, 0.0, 0.0, 0.0}, // (Not used)
       	{0.0, 0.0, 1.8, 3.2, 4.7}, // To index finger
       	{0.0, 1.8, 0.0, 1.4, 2.9}, // To long finger
       	{0.0, 3.2, 1.4, 0.0, 1.5}, // To ring finger
       	{0.0, 4.7, 2.9, 1.5, 0.0}  // To pinky finger
    }; //cm. Totally non-scientific.
    
    /**
     *  Modifier to avoid higher frets (position).
     */
    private static final double FRET_HEIGHT_PENALTY = 0.2;
    /*Added:*/ private static final double NONZERO_POSITION = 0.1;
    
    /**
     *  Modifier to penalize specific fingers.
     */
    private static final double FINGER_PENALTY[] =
    // Open  Index Long  Ring  Pinky
//      {0.90, 0.50, 0.10, 0.20, 1.50};
//      {0.00, 0.10, 0.60, 0.80, 0.30}; //Much harder to use pinky, at least on low frets???
        {0.00, 0.10, 0.40, 0.70, 1.10};
    
    /**
     *  Modifier to penalize specific finger transitions.
     */
    private static final double FINGER_TRANSITION_PENALTY[][] =
    /*{ // Open  Index Long  Ring  Pinky
        {0.00, 0.00, 0.00, 0.00, 0.00}, // To open note
        {0.00, 0.00, 0.80, 0.40, 0.10}, // To index finger
        {0.00, 0.90, 0.00, 1.00, 0.50}, // To long finger
        {0.00, 0.50, 1.10, 0.00, 0.80}, // To ring finger
        {0.00, 0.20, 0.60, 0.90, 0.00}  // To pinky finger
    }; //Change values, consider stretching (e.g ring to pinky = easy with adjacent frets, very hard otherwise)*/
    { // Open  Index Long  Ring  Pinky
        {0.00, 0.00, 0.00, 0.00, 0.00}, // To open note
        {0.00, 0.00, 0.30, 0.40, 0.10}, // To index finger
        {0.00, 0.40, 0.00, 1.00, 0.50}, // To long finger
        {0.00, 0.50, 1.10, 0.00, 0.80}, // To ring finger
        {0.00, 0.20, 0.60, 0.90, 0.00}  // To pinky finger
    };
    private static final double PHYSICAL_STRETCH_PENALTY = 0.25;
    private static final double PHYSICAL_STRETCH_PENALTY_POWER = 1.6;
    private static final double PHYSICAL_CRAMP_PENALTY_POWER = 2.2;
    
    /**
     *  The hand movement penalties are used as multipliers,
     *  i.e. a hand movement over n frets incurs a penalty of
     *  n times the penalty.
     */
    
    // Add constant to each position change???
    private static final double HAND_MOVE_PENALTY = 0.3;
    
    /**
     *  Penalty for hand movements during open notes or pauses.
     */
    private static final double HAND_MOVE_DURING_OPEN_NOTE_PENALTY = 1;
    /**
     *  Penalty for hand movements between notes where the
     *  used finger does not change.
     */
    //private static final double HAND_MOVE_WITH_GUIDE_FINGER_PENALTY = 1.2; //???
    private static final double HAND_MOVE_WITH_GUIDE_FINGER_PENALTY = 1.6;
    /**
     *  Penalty for hand movements between notes where the
     *  used finger changes.
     */
    private static final double HAND_MOVE_WITHOUT_GUIDE_FINGER_PENALTY = 3; // Why such a big difference???
    
    /**
     *  Penalize the displacement from the natural finger position
     *  across the strings, along a single fret. I.e the next finger
     *  on the next string principle.
     *  This penalty is used as a multiplier,
     *  i.e. a displacement over n strings incurs a penalty of
     *  n times the penalty.
     */
    private static final double VERTICAL_STRETCH_PENALTY = 0.5;
    /**
     *  Penalize jumps from one string to another with the same
     *  finger, without any intermediate pause.
     *  This penalty is used as a absolute value, i.e. a movement
     *  across any number of strings will incur the same penalty.
     */
    //private static final double STRING_JUMP_PENALTY = 1.0; // Increase, and multiply by number of strings in jump???
    private static final double STRING_JUMP_PENALTY_STATIC = 1.7;
    private static final double STRING_JUMP_PENALTY_PER_STRING = 0.3;
    /**
     *  Penalize glides without hand movements.
     *  This penalty is used as a absolute value, i.e. a movement
     *  across any number of strings will incur the same penalty.
     */
    //private static final double FINGER_GLIDE_PENALTY = 2.0; //Seems too expensive???
    private static final double FINGER_GLIDE_PENALTY = 1.3;
    
    /**
     *  Penalty for hand stretching
     */
    //private static final double HAND_STRETCH_PENALTY = 0.5; // Increase???
    //private static final double HAND_STRETCH_PENALTY = 0.8;
    private static final double HAND_STRETCH_PENALTY = 0.5; //Augmented by PHYSICAL_STRETCH_PENALTY_...!
    
    public Fingering(int note, int string, int position, int finger, int fret, double duration) {
        this.note = note;
        this.string = string;
        this.position = position;
        this.finger = finger;
        this.fret = fret;
        this.duration = duration;
    }
    
    public double calculateInitiateCost() {
        double result = 0;
        
        if(position != 0 && finger != OPEN) result += NONZERO_POSITION;
        result += FRET_HEIGHT_PENALTY * position;
        result += FINGER_PENALTY[finger];
        
        // The natural finger placement is to have the index finger
        // on the first fret in the hand position, the long finger
        // on the next fret, and so on.
        // Penalize the displacement from natural finger placement
        if (finger != OPEN) {
            // On which fret would we optimally want to place this finger?
            int expected_fret = position + finger - 1;
            result += HAND_STRETCH_PENALTY * Math.abs(expected_fret - fret);
        }
        
        return result;
    }
    
    public double calculateReleaseCost() {
        double result = 0;
        
        // TODO
        
        return result;
    }
    
    public double calculateTransitionCost(Fingering nextFingering) {
        double result = 0;
        
        // Penalize hand movements
        int horDisplacement = Math.abs(position - nextFingering.position);
        if (finger == OPEN || nextFingering.finger == OPEN) {
            result += HAND_MOVE_DURING_OPEN_NOTE_PENALTY * horDisplacement;
        } else if (finger == nextFingering.finger && string == nextFingering.string) {
            result += HAND_MOVE_WITH_GUIDE_FINGER_PENALTY * horDisplacement;
        } else {
            result += HAND_MOVE_WITHOUT_GUIDE_FINGER_PENALTY * horDisplacement;
        }
        
        /*Added:*/
        if(horDisplacement > 0) {
        	result += HAND_MOVE_PENALTY;
        }
        else if(finger != OPEN && nextFingering.finger != OPEN) {
        	double horDistance = (FRET_POSITIONS[fret] - FRET_POSITIONS[nextFingering.fret]) * FRETBOARD_LENGTH;
        	double verDistance = (string - nextFingering.string) * STRING_SPACING; //abs unneeded due to squaring
        	double transDistance = Math.sqrt(horDistance * horDistance + verDistance * verDistance);
        	
        	double extraDistance;
        	if(transDistance > RELAXED_FINGER_DISTANCES[nextFingering.finger][finger]) {
        		extraDistance = transDistance - RELAXED_FINGER_DISTANCES[nextFingering.finger][finger];
        		result += PHYSICAL_STRETCH_PENALTY * Math.pow(extraDistance, PHYSICAL_STRETCH_PENALTY_POWER);
        	}
        	else if(transDistance < CLOSE_FINGER_DISTANCES[nextFingering.finger][finger]) {
        		extraDistance = CLOSE_FINGER_DISTANCES[nextFingering.finger][finger] - transDistance;
        		result += PHYSICAL_STRETCH_PENALTY * Math.pow(extraDistance, PHYSICAL_CRAMP_PENALTY_POWER);
        	}
        }
        
        
        // Penalize finger glides without hand movements
        if (this.position == nextFingering.position
         && this.finger == nextFingering.finger
         && this.fret != nextFingering.fret) {
            result += FINGER_GLIDE_PENALTY;
        }
        
        // Apply penalties for specific finger transitions
        if (string == nextFingering.string) {
            result += FINGER_TRANSITION_PENALTY[finger][nextFingering.finger];
        }
        
        // Principle: Next finger on next string
        // Applies when assigning adjacent notes to the same fret
        // on different strings.
        if (finger != OPEN && nextFingering.fret == this.fret) {
            if (this.finger == INDEX && nextFingering.finger == INDEX) {
                // Index finger - use bar fingering to incur no penalty
            	// What do we actually do with bar???
            } else if (this.finger != nextFingering.finger) {
                int fingerDisplacement = nextFingering.finger - this.finger;
                int expected_string = this.string - fingerDisplacement;
                result += VERTICAL_STRETCH_PENALTY * Math.abs(expected_string - nextFingering.string);
            }
        }
        
        // Jump from one string to another with a single finger
        // or equivalently, a bar with some other finger than
        // the index finger
        if (this.string != nextFingering.string && this.finger == nextFingering.finger /*Added:*/ && !(this.finger == INDEX && nextFingering.fret == this.fret) && this.finger != OPEN) {
            //result += STRING_JUMP_PENALTY;
        	result += STRING_JUMP_PENALTY_STATIC;
        	result += STRING_JUMP_PENALTY_PER_STRING * (Math.abs(this.string - nextFingering.string) - 1);
        } //Check if index finger???
        
        return result;
    }
    
    public double calculateDurationCostModifier() {
        // DURATION_PENALTY = 1 applies the whole penalty
        // DURATION_PENALTY = 0 applies no penalty
        return (1 / Math.pow(getDuration(), DURATION_PENALTY));
    }
    
    public int getString() {
        return string;
    }
    
    public int getFinger() {
        return finger;
    }
    
    public int getPosition() {
        return position;
    }
    
    public double getDuration() {
        return duration;
    }
    
    public int getFret() {
        return fret;
    }
    
    public String toString() {
        return "Fingering: (Note: " + note + ", Position: " + position + ", String: " + string + ", Fret: " + fret + ", Finger: " + finger + ")";
    }
    
}
