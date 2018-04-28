package module.utils;

import module.Node;
import module.Path;

import java.util.*;

public class PathSwapper {

    private Path first;
    private Path second;
    private List<Node> firstNodes;
    private List<Node> secondNodes;
    Set<Map.Entry<Path, Path>> paths;

    public PathSwapper(Path first, Path second) {
        this.first = first;
        this.second = second;
        firstNodes = first.getNodes();
        secondNodes = second.getNodes();
        paths = new HashSet<>();
    }

    public Set<Map.Entry<Path, Path>> swap() {
        int nbMaximumOfSwapsPossible = Math.min(firstNodes.size(), secondNodes.size());
        for (int i = 0; i <= nbMaximumOfSwapsPossible; i++) {
            swapNElements(i);
        }

        return paths;
    }

    public void swapNElements(int nbOfElementsToSwap) {
        for (int i = 1; i <= firstNodes.size() - nbOfElementsToSwap; i++) {
            for (int j = 1; j <= secondNodes.size() - nbOfElementsToSwap; j++) {
                paths.add(swap(i, i + nbOfElementsToSwap, j, j + nbOfElementsToSwap));
            }
        }
    }

    private Map.Entry<Path, Path> swap(int firstIndexToStart, int firstIndexToStop,
                                       int secondIndexToStart, int secondIndexToStop) {

        List<Node> firstNodesCopy = new ArrayList<>(firstNodes);
        List<Node> secondNodesCopy = new ArrayList<>(secondNodes);

        LinkedList<Node> removedFromFirst = new LinkedList<>();
        LinkedList<Node> removedFromSecond = new LinkedList<>();

        for (int i = firstIndexToStart; i < firstIndexToStop; i++) {
            Node removed = firstNodesCopy.remove(firstIndexToStart);
            removedFromFirst.add(removed);
        }

        for (int i = secondIndexToStart; i < secondIndexToStop; i++) {
            Node removed = secondNodesCopy.remove(secondIndexToStart);
            removedFromSecond.add(removed);
        }

        for (int i = firstIndexToStart; i < firstIndexToStop; i++) {
            if (i < firstNodesCopy.size()) {
                firstNodesCopy.add(i, removedFromSecond.removeFirst());
            } else {
                firstNodesCopy.add(removedFromSecond.removeFirst());
            }
        }

        for (int i = secondIndexToStart; i < secondIndexToStop; i++) {
            if (i < secondNodesCopy.size()) {
                secondNodesCopy.add(i, removedFromFirst.removeFirst());
            } else {
                secondNodesCopy.add(removedFromFirst.removeFirst());
            }
        }

        Path first = new Path();
        first.addAllNodes(firstNodesCopy);
        first.startAndEndingPoint(this.first.getWarehouse());


        Path second = new Path();
        second.addAllNodes(secondNodesCopy);
        second.startAndEndingPoint(this.second.getWarehouse());
        return new AbstractMap.SimpleEntry<>(first, second);
    }

    public Path swapSame(int firstIndexToStart, int firstIndexToStop, int secondIndexToStart, int secondIndexToStop) {
        LinkedList<Node> firstSequence = new LinkedList<>();
        LinkedList<Node> secondSequence = new LinkedList<>();

        List<Node> nodeCopy = new ArrayList<>(firstNodes);
        for (int i = firstIndexToStart; i < firstIndexToStop; i++) {
            firstSequence.add(nodeCopy.get(i));
        }

        for (int i = secondIndexToStart; i < secondIndexToStop; i++) {
            secondSequence.add(nodeCopy.get(i));
        }

        for (int i = firstIndexToStart; i < firstIndexToStop; i++) {
            nodeCopy.remove(i);
            nodeCopy.add(i, secondSequence.removeFirst());
        }

        for (int i = secondIndexToStart; i < secondIndexToStop; i++) {
            nodeCopy.remove(i);
            nodeCopy.add(i, firstSequence.removeFirst());
        }

        Path path = new Path();
        path.addAllNodes(nodeCopy);
        path.startAndEndingPoint(first.getWarehouse());
        return path;
    }

    public Map.Entry<Path, Path> swap(int nbElementsToSwap, int firstIndex, int secondIndex) {

        return swap(firstIndex,
                firstIndex + nbElementsToSwap,
                secondIndex,
                secondIndex + nbElementsToSwap);
    }

    public Path swapSame(int nbElementToSwap, int firstIndex, int secondIndex) {
        return swapSame(firstIndex, firstIndex + nbElementToSwap, secondIndex, secondIndex + nbElementToSwap);
    }
}
