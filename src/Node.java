
import java.util.List;
import java.util.ArrayList;

public class Node {
    
    private Node prev;
    private int cost;
    private Fingering fingering;
    
    public Node(Fingering fingering) {
        this.prev = null;
        this.cost = fingering.calculateInitiateCost();
        this.fingering = fingering;
    }
    
    public Node(Node prev, Fingering fingering) {
        this.prev = prev;
        this.cost = prev.cost;
        this.cost += prev.fingering.calculateReleaseCost();
        this.cost += prev.fingering.calculateTransitionCost(fingering);
        this.cost += fingering.calculateInitiateCost();
        // TODO factor in duration cost
        this.fingering = fingering;
    }
    
    public Node getPrev() {
        return prev;
    }
    
    public int getCost() {
        return cost;
    }
    
    public Fingering getFingering() {
        return fingering;
    }
    
}
