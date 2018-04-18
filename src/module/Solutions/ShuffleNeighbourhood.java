package module.Solutions;

import module.Node;
import module.Path;
import module.utils.Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ShuffleNeighbourhood implements NeighbourStrategy {

    private Path selectedPath;
    private Set<Path> solutions;
    Random rand;

    @Override
    public Set<Solution> getNeighbourhood(Solution solution) {
        Set<Path> paths = solution.getPaths();
        rand = Helpers.random;

        int skipping = rand.nextInt(paths.size());
        selectedPath = paths.stream().skip(skipping)
                .findFirst().orElseThrow(IndexOutOfBoundsException::new);

        shuffleTwoElements();
        return null;
    }

    private void shuffleTwoElements() {
        List<Node> nodes = selectedPath.getNodes();
        int firstIndex;
        int secondIndex;
        do {
             firstIndex = rand.nextInt(nodes.size());
             secondIndex = rand.nextInt(nodes.size());
        } while (firstIndex == secondIndex);

       shuffleTwoElementsWithIndex(nodes, firstIndex, secondIndex);
    }

    private List<Node> shuffleTwoElementsWithIndex(List<Node> nodes, int firstIndex, int secondIndex) {
        List<Node> nodesCopy = new ArrayList<>(nodes);
        Node tmp = nodesCopy.get(firstIndex);
        nodesCopy.set(firstIndex, nodesCopy.get(secondIndex));
        nodesCopy.set(secondIndex, tmp);
        return nodesCopy;
    }
}
