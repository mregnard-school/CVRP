package module.utils;

import module.Node;
import module.Path;

import java.util.*;

public class PathSwapper {

    private List<Node> firstNodes;
    private List<Node> secondNodes;
    Set<Map.Entry<Path, Path>> paths;

    public PathSwapper(Path first, Path second) {
        firstNodes = first.getNodes();
        secondNodes = second.getNodes();
        paths = new HashSet<>();
    }

    public Set<Map.Entry<Path, Path>> swap() {
        int nbMaximumOfSwapsPossible = Math.min(firstNodes.size(), secondNodes.size()) - 1;
        for (int i = 1; i <= nbMaximumOfSwapsPossible; i++) {
            swapNElements(i);
        }

        return paths;
    }

    private void swapNElements(int nbOfElementsToSwap) {
        for (int i = 1; i <= firstNodes.size() - nbOfElementsToSwap; i++) {
            for (int j = 1; j <= secondNodes.size() - nbOfElementsToSwap; j++) {
               paths.add(swap(i, i + nbOfElementsToSwap, j, j + nbOfElementsToSwap));
            }
        }
    }

    public Map.Entry<Path, Path> swap(int firstIndexToStart, int firstIndexToStop,
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
            firstNodesCopy.add(i, removedFromSecond.removeFirst());
        }

        for (int i = secondIndexToStart; i < secondIndexToStop; i++) {
            secondNodesCopy.add(i, removedFromFirst.removeFirst());
        }

        Path first = new Path();
        first.addAllNodes(firstNodesCopy);

        Path second = new Path();
        second.addAllNodes(secondNodesCopy);

        return new AbstractMap.SimpleEntry<>(first, second);
    }
}
