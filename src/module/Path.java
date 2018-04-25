package module;

import java.util.*;

import static java.util.stream.Collectors.joining;

public class Path {
    List<Node> nodes;
    int maxCapacity;
    int currentCapacity;
    double distance;
    boolean exceeded;

    public Path() {
        this(100);
    }

    public Path(int maxCapacity) {
        this.nodes = new ArrayList<>();
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
        exceeded = false;
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

    public void recompute() {
        computeCapacity();
        computeDistance();
    }

    private void computeCapacity() {
        exceeded = false;
        currentCapacity = nodes.stream()
                .mapToInt(Node::getCapacity)
                .sum();

        if (currentCapacity > maxCapacity) {
            exceeded = true;
        }
    }

    private void computeDistance() {
        distance = -1;
        if (!hasExceeded()) {
            Iterator<Node> iterator = nodes.iterator();
            Node current = iterator.next();
            distance = 0;

            while (iterator.hasNext()) {
                Node next = iterator.next();
                distance += current.getPosition().getDistanceFrom(next.getPosition());
                current = next;
            }
        }
    }

    public double getDistance() {
        return this.distance;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public boolean hasExceeded() {
        return exceeded;
    }

    @Override
    public String toString() {
        String string = "(";
        string += nodes.stream()
                .map(Node::toString)
                .collect(joining(" ; "));
        string += ")";
        return string;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Path)) {
            return false;
        }

        Path other = (Path) obj;
        return this.getNodes().equals(other.getNodes());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNodes());
    }
}
