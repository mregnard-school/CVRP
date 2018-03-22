package module;

import java.util.Collection;
import java.util.LinkedHashSet;

import static java.util.stream.Collectors.joining;

public class Path {
    LinkedHashSet<Node> nodes;
    int maxCapacity;
    int currentCapacity;

    public Path(){
        this(100);
    }

    public Path(int maxCapacity) {
        this.nodes = new LinkedHashSet<>();
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
    }

    public LinkedHashSet<Node> getNodes() {
        return nodes;
    }

    public boolean canAddNode(Node node) {
        return currentCapacity + node.getCapacity() < maxCapacity;
    }

    public boolean addNode(Node node) {
        if (!canAddNode(node)) {
            return false;
        }
        nodes.add(node);
        currentCapacity += node.getCapacity();
        return true;
    }
    
    public void addAllNodes(Collection<Node> nodes){
        this.nodes.addAll(nodes);
        // TODO: 22/03/2018 Recalculate capacity + distance
        // TODO: 22/03/2018 If capacity >  MAX_CAPACITY, fitness = -1.
    }

    public boolean replaceNode() {
        //verify capacity+
        return true;
    }

    public boolean removeNode() {
        //verify capacity+
        return true;
    }

    @Override
    public String toString() {
        String string = "(";
        string += nodes.stream()
                .map(Node::toString)
                .collect(joining(","));
        string += ")";
        return string;
    }
}
