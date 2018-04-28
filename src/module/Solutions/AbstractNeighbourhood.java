package module.Solutions;

import module.Path;
import module.utils.Helpers;
import module.utils.PathSwapper;

import java.util.*;

public abstract class AbstractNeighbourhood implements NeighbourStrategy {
    private Random random = Helpers.random;
    protected Set<Path> paths;
    protected PathSwapper swapper;
    protected Map.Entry<Path, Path> selected;
    protected Map.Entry<Path, Path> modified;
    private Set<Path> copy;
    private Set<Solution> solutions;

    @Override
    public Set<Solution> getNeighbourhood(Solution solution) {
        paths = solution.getPaths();
        selected = selectTwoDifferentPath();
        swapper = new PathSwapper(selected.getKey(), selected.getValue());

       modified = neighbourhoodCalculation();

        cleanPaths(modified.getKey(), modified.getValue());
        setNewSolution();

        return solutions;
    }

    private Map.Entry<Path, Path> selectTwoDifferentPath() {
        Path first;
        Path second;
        do {
            first = selectRandomPath();
            second = selectRandomPath();
        } while (first.equals(second));

        return new AbstractMap.SimpleEntry<>(first, second);
    }

    protected abstract Map.Entry<Path, Path> neighbourhoodCalculation();

    private void cleanPaths(Path key, Path value) {
        copy = new HashSet<>(paths);
        cleanPath(selected.getKey(), key);
        cleanPath(selected.getValue(), value);
    }

    private void cleanPath(Path pathToRemove, Path pathToAdd) {
        paths.remove(pathToRemove);
        paths.add(pathToAdd);
    }
    
    private void setNewSolution() {
        solutions = new HashSet<>();
        Solution solution = new Solution(copy, this);
        solutions.add(solution);
    }

    private Path selectRandomPath() {
        int rdIndex = random.nextInt(paths.size());

        return paths.stream()
                .skip(rdIndex)
                .findFirst()
                .orElseThrow(IndexOutOfBoundsException::new);
    }

}
