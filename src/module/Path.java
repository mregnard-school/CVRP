package module;

import java.util.*;

import static java.util.stream.Collectors.joining;

public class Path {
    List<Node> nodes;
    int maxCapacity;
    int currentCapacity;
    double distance;

    public Path() {
        this(100);
    }

    public Path(int maxCapacity) {
        this.nodes = new ArrayList<>();
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
    }

    public List<Node> getNodes() {
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
        recompute();
    }

    public void recompute(){
        computeCapacity();
        computeDistance();
    }

    private void computeCapacity() {
        currentCapacity =  nodes.stream()
                .mapToInt(Node::getCapacity)
                .sum();
    }

    private void computeDistance() {
        if(currentCapacity > maxCapacity){
            distance = -1;
        }

        Iterator<Node> iterator = nodes.iterator();
        Node current = iterator.next();
        distance = 0;

        while (iterator.hasNext()) {
            Node next = iterator.next();
            distance += current.getPosition().getDistanceFrom(next.getPosition());
            current = next;
        }
    }

    public double getDistance(){
        return this.distance;
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

    public int getMaxCapacity()
    {
        return maxCapacity;
    }

    public int getCurrentCapacity()
    {
        return currentCapacity;
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
