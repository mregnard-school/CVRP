package module;


import java.util.*;

public class GreedyIterator implements Iterator<Node> {

    private LinkedHashSet<Node> nodes;

    private Set<Node> visited;

    //Might not be necessary to check
    private Queue<Node> toVisit;

    public GreedyIterator(Path path, Node start) {
        nodes = new LinkedHashSet<>(path.getNodes());
        visited = new HashSet<>();
        toVisit = new LinkedList<>();

        visited.add(start);
        toVisit.add(start);
        nodes.removeAll(visited);
    }

    @Override
    public boolean hasNext() {
        return !toVisit.isEmpty();
    }

    @Override
    public Node next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Node next = toVisit.poll();
        visited.add(next);
        nodes.removeAll(visited);
        if (!nodes.isEmpty()) {
            toVisit.add(next.getClosest(nodes));
        }


        return next;
    }
}
