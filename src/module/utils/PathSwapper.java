package module.utils;

import module.Node;
import module.Path;

import java.util.*;
import java.util.stream.Collectors;

public class PathSwapper {

    public static Set<Map.Entry<Path, Path>> swapPath(Path first, Path second){
        LinkedHashSet<Node> firstNodes = first.getNodes();
        LinkedHashSet<Node> secondNodes = second.getNodes();
        int nbNodesFirst = firstNodes.size();
        int nbNodesSecond = secondNodes.size();


        Set<Map.Entry<Path, Path>> paths = new HashSet<>();
        //Number of swaps is determined by taking the minimum -1.
        int nbSwap = Math.min(nbNodesFirst, nbNodesSecond) - 2;

        for (int i = 1; i <= firstNodes.size()-nbSwap; i++) {
            for (int j = 1; j <= secondNodes.size()-nbSwap; j++) {
                LinkedHashSet<Node> firstCopy = new LinkedHashSet<>(firstNodes);
                LinkedHashSet<Node> secondCopy = new LinkedHashSet<>(secondNodes);

                Set<Node> toSwapFromFirst = firstNodes.stream()
                        .skip(i)
                        .limit(nbSwap)
                        .collect(Collectors.toSet());

                Set<Node> toSwapFromSecond = secondNodes.stream()
                        .skip(j)
                        .limit(nbSwap)
                        .collect(Collectors.toSet());

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
}
