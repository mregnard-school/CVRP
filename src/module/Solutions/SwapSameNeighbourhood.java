package module.Solutions;

import module.Path;
import module.utils.Helpers;
import module.utils.PathSwapper;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SwapSameNeighbourhood implements NeighbourStrategy {

    private Random rand = Helpers.random;
    private Path path;
    private Solution solution;
    private Set<Path> paths;

    @Override
    public Set<Solution> getNeighbourhood(Solution solution) {
        paths = solution.getPaths();

        int rd = rand.nextInt(paths.size());

        path = solution.getPaths().stream().skip(rd).findFirst().orElseThrow(IndexOutOfBoundsException::new);

        int nbElementToSwap = 1;

        int firstIndex;
        int secondIndex;

        // TODO: 28/04/2018 Workaround
        if (path.getNodes().size() == 2) {
            firstIndex = 1;
            secondIndex = 1;
        } else {
            firstIndex = rand.nextInt(path.getNodes().size() - nbElementToSwap - 1) + 1;
            secondIndex = rand.nextInt(path.getNodes().size() - nbElementToSwap - 1) + 1;
        }

        PathSwapper pathSwapper = new PathSwapper(path, path);
        pathSwapper.swapSame(nbElementToSwap, firstIndex, secondIndex);
        replacePath(path);

        Set<Solution> solutions = new HashSet<>();
        solutions.add(this.solution);

        return solutions;
    }

    private void replacePath(Path path) {
        Set<Path> pathCopy = new HashSet<>(paths);

        pathCopy.remove(this.path);
        pathCopy.add(path);

        solution = new Solution(pathCopy, this);
    }
}
