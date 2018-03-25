package module;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import static java.util.stream.Collectors.joining;

public class Path {
    LinkedHashSet<Node> nodes;
    int maxCapacity;
    int currentCapacity;
    double distance;

    public Path() {
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

    public void addAllNodes(Collection<Node> nodes) {
        this.nodes.addAll(nodes);
        currentCapacity = computeCapacity();
        distance = computeDistance();
    }

    private int computeCapacity() {
        return nodes.stream()
                .mapToInt(Node::getCapacity)
                .sum();
    }

    private double computeDistance() {
        if(currentCapacity > maxCapacity){
            return -1;
        }
        //Reordering to compute easily
        reorder();
        Iterator<Node> iterator = nodes.iterator();
        Node current = iterator.next();
        double distance = 0;

        while (iterator.hasNext()) {
            Node next = iterator.next();
            distance += current.getPosition().getDistanceFrom(next.getPosition());
            current = next;
        }
        return distance;
    }

    public double getDistance(){
        return this.distance;
    }

    public void reorder() {
        //Wrapper to easily change method if needed
        reorderGreedy();
    }

    private void reorderGreedy() {
        LinkedHashSet<Node> greeded = new LinkedHashSet<>();

        for (Iterator<Node> iterator = greedyIterator(); iterator.hasNext(); ) {
            greeded.add(iterator.next());
        }

        nodes = greeded;
    }


    Iterator<Node> greedyIterator() {
        return new GreedyIterator(this, nodes.iterator().next());
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
