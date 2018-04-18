package module.utils;

import module.Node;
import module.Path;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PathSwapper {

    public static void swap(Path first,
                            Path second,
                            int firstIndexToStart,
                            int firstIndexToStop,
                            int secondIndexToStart,
                            int secondIndexToStop) {

        List<Node> firstNodes = new ArrayList<>(first.getNodes());
        List<Node> secondNodes = new ArrayList<>(second.getNodes());

        LinkedList<Node> removedFromFirst = new LinkedList<>();
        LinkedList<Node> removedFromSecond = new LinkedList<>();
        for (int i = firstIndexToStart; i < firstIndexToStop; i++) {
            Node removed = firstNodes.remove(firstIndexToStart);
            removedFromFirst.add(removed);
        }

        for (int i = secondIndexToStart; i < secondIndexToStop; i++) {
            Node removed = secondNodes.remove(secondIndexToStart);
            removedFromSecond.add(removed);
        }

        for (int i = firstIndexToStart; i < firstIndexToStop; i++) {
            firstNodes.add(i, removedFromSecond.removeFirst());
        }

        for (int i = secondIndexToStart; i < secondIndexToStop; i++) {
            secondNodes.add(i, removedFromFirst.removeFirst());
        }

        Path p = new Path();
        p.addAllNodes(firstNodes);

        Path p1 = new Path();
        p1.addAllNodes(secondNodes);
        System.out.println(p);
        System.out.println(p1);
        System.out.println();
    }

    public static void swap2(Path first, Path second) {
        int nbMaximmumOfSwapsPossible = Math.min(first.getNodes().size(), second.getNodes().size()) - 1;
        for (int i = 1; i <= nbMaximmumOfSwapsPossible; i++) {
            swapNElements(i, first, second);
        }
    }

    private static void swapNElements(int nbOfElementsToSwap, Path first, Path second) {
        for (int i = 1; i <= first.getNodes().size() - nbOfElementsToSwap; i++) {
            for (int j = 1; j <= second.getNodes().size() - nbOfElementsToSwap; j++) {
                swap(first, second, i, i + nbOfElementsToSwap, j, j + nbOfElementsToSwap);
            }
        }
    }


}
