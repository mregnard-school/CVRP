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

    public List<Node> getNodesWithWarehouse() {
        return nodes;
    }

    public List<Node> getNodes() {
        if (nodes.isEmpty()) {
            return new ArrayList<>(nodes);
        }
        return nodes.subList(1, nodes.size() - 1);
    }

    public boolean canAddNode(Node node) {
        return currentCapacity + node.getCapacity() < maxCapacity;
    }

    public boolean addNode(Node node) {
        if (!canAddNode(node)) {
            return false;
        }
        nodes.add(node);
        computeCapacity();
        return true;
    }

    public void addAllNodes(Collection<Node> nodes) {
        this.nodes.addAll(nodes);
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

    public void startAndEndingPoint(Node warehouse) {
        if (!nodes.contains(warehouse)) {
            nodes.add(0, warehouse);
            nodes.add(warehouse);
        }
        recompute();
    }

    public Node getWarehouse() {
        return nodes.get(0);
    }

    @Override
    public String toString() {
        String string = "(";
        string += nodes.stream()
                .map(Node::toString)
                .collect(joining(";"));
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
        return this.getNodesWithWarehouse().equals(other.getNodesWithWarehouse());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNodesWithWarehouse());
    }
}
