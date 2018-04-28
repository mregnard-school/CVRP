package module.Solutions;

import module.Node;
import module.Path;
import module.utils.Helpers;

import java.util.*;

public class StealNeighbour implements NeighbourStrategy {

    private Random random = Helpers.random;
    private List<Path> paths;
    private Path stealer;
    private Path stolen;
    private Solution solution;

    @Override
    public Set<Solution> getNeighbourhood(Solution solution) {
        do {
            paths = new ArrayList<>(solution.getPaths());
            Map.Entry<Path, Path> selected = selectRandomPath();

            stealer = selected.getKey();
            stolen = selected.getValue();

            Map.Entry<Path, Path> modified = steal();
            createNewPaths(modified.getKey(), modified.getValue());
        } while(!this.solution.isValid());

        Set<Solution> solutions = new HashSet<>();
        solutions.add(this.solution);
        return solutions;
    }

    private Map.Entry<Path, Path> selectRandomPath() {
        int first;
        int second;

        do {
            first = random.nextInt(paths.size());
            second = random.nextInt(paths.size());
        } while (first == second);

        return new AbstractMap.SimpleEntry<>(paths.get(first), paths.get(second));
    }

    public void createNewPaths(Path first, Path second) {
        Set<Path> pathCopy = new HashSet<>(paths);
        pathCopy.remove(this.stealer);
        pathCopy.remove(this.stolen);

        if(first.getNodes().size() > 1) {
            pathCopy.add(first);
        }

        if(second.getNodes().size() > 1) {
            pathCopy.add(second);
        }
        solution = new Solution(pathCopy, this);
    }

    public Map.Entry<Path, Path> steal() {

        List<Node> nodesToSteal = new ArrayList<>(stolen.getNodes());

        int rd = random.nextInt(nodesToSteal.size() - 1) + 1;
        Node stoled = nodesToSteal.remove(rd);

        List<Node> nodeStolen = new ArrayList<>(stealer.getNodes());
        rd = random.nextInt(nodeStolen.size() - 1 ) + 1;
        nodeStolen.add(rd, stoled);


        Path first = new Path(stealer.getMaxCapacity());
        first.addAllNodes(nodeStolen);

        Path second = new Path(stolen.getMaxCapacity());
        second.addAllNodes(nodesToSteal);

        return new AbstractMap.SimpleEntry<>(first, second);
    }
}
