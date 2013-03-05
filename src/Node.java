

import java.util.List;
import java.util.ArrayList;

public class Node {
    
    private Node prev;
    private double cost;
    private Fingering fingering;
    
    public Node(Fingering fingering) {
        this.prev = null;
        //this.cost = fingering.calculateInitiateCost();
        this.cost = 0; // We can start from any fingering without any penalty
        this.fingering = fingering;
    }
    
    public Node(Node prev, Fingering fingering) {
        this.prev = prev;
        
        this.cost = prev.cost;
        this.cost += prev.fingering.calculateReleaseCost();
        this.cost += prev.fingering.calculateTransitionCost(fingering);
        this.cost += fingering.calculateInitiateCost();
        
        this.fingering = fingering;
    }
    
    public Node getPrev() {
        return prev;
    }
    
    public double getCost() {
        return cost;
    }
    
    public Fingering getFingering() {
        return fingering;
    }
    
}
