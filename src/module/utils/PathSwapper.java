package module.utils;

import module.Node;
import module.Path;

import java.util.*;
import java.util.stream.Collectors;

public class PathSwapper {

    private LinkedHashSet<Node> firstNodes;
    private LinkedHashSet<Node> secondNodes;
    private int nbNodesInFirst;
    private int nbNodesInSecond;
    private int nbSwap;

    Set<Map.Entry<Path, Path>> paths;
    Set<Node> toSwapFromFirst;
    Set<Node> toSwapFromSecond;

    public PathSwapper(Path first, Path second) {
        firstNodes = first.getNodes();
        secondNodes = second.getNodes();
        nbNodesInFirst = firstNodes.size();
        nbNodesInSecond = secondNodes.size();
        paths = new HashSet<>();
    }

    public Set<Map.Entry<Path, Path>> swapPath() {
        determineNumberOfSwaps();

        for (int i = 1; i <= nbNodesInFirst - nbSwap; i++) {
            for (int j = 1; j <= nbNodesInSecond - nbSwap; j++) {
                LinkedHashSet<Node> firstCopy = new LinkedHashSet<>(firstNodes);
                LinkedHashSet<Node> secondCopy = new LinkedHashSet<>(secondNodes);

               setOfNodesToSwapFromBoth(i, j);

                //Swapping !
                firstCopy.removeAll(toSwapFromFirst);
                firstCopy.addAll(toSwapFromSecond);
                secondCopy.removeAll(toSwapFromSecond);
                secondCopy.addAll(toSwapFromFirst);

                Path firstPath = new Path();
                Path secondPath = new Path();
                firstPath.addAllNodes(firstCopy);
                secondPath.addAllNodes(secondCopy);

                paths.add(new AbstractMap.SimpleEntry<>(firstPath, secondPath));
            }
        }
        return paths;
    }

    public void determineNumberOfSwaps() {
        nbSwap = Math.min(nbNodesInFirst, nbNodesInSecond) - 2;
    }

    public void setOfNodesToSwapFromBoth(int i, int j) {
        setOfNodesToSwapFromFirst(i);
        setOfNodesToSwapFromSecond(j);
    }

    public void setOfNodesToSwapFromFirst(int nbOfElementToSkip) {
       toSwapFromFirst = firstNodes.stream()
                .skip(nbOfElementToSkip)
                .limit(nbSwap)
                .collect(Collectors.toSet());
    }

    public void setOfNodesToSwapFromSecond(int nbOfElementToSkip) {
       toSwapFromSecond = secondNodes.stream()
                .skip(nbOfElementToSkip)
                .limit(nbSwap)
                .collect(Collectors.toSet());
    }
}
