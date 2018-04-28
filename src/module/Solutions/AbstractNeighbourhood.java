package module.Solutions;

import module.Path;
import module.utils.Helpers;
import module.utils.PathSwapper;

import java.util.*;

public abstract class AbstractNeighbourhood implements NeighbourStrategy {
    protected Random random = Helpers.random;
    protected Solution solution;
    protected Set<Path> paths;
    protected PathSwapper swapper;
    protected Map.Entry<Path, Path> selected;
    protected Map.Entry<Path, Path> modified;
    protected Set<Path> copy;
    protected Set<Solution> solutions;

    @Override
    public Set<Solution> getNeighbourhood(Solution solution) {
        paths = solution.getPaths();
        do {
           calculateNextSolutions();
        } while (notValid());

        return solutions;
    }

    public void calculateNextSolutions() {
        initialize();

        modified = neighbourhoodCalculation();

        cleanUp();
        setNewSolution();
    }

    protected void initialize() {
        selected = selectTwoDifferentPath();
        swapper = new PathSwapper(selected.getKey(), selected.getValue());
    }

    protected Map.Entry<Path, Path> selectTwoDifferentPath() {
        Path first;
        Path second;
        do {
            first = selectRandomPath();
            second = selectRandomPath();
        } while (first.equals(second));

        return new AbstractMap.SimpleEntry<>(first, second);
    }

    protected Path selectRandomPath() {
        int rdIndex = random.nextInt(paths.size());

        return paths.stream()
                .skip(rdIndex)
                .findFirst()
                .orElseThrow(IndexOutOfBoundsException::new);
    }

    protected abstract Map.Entry<Path, Path> neighbourhoodCalculation();

    protected void cleanUp() {
        copy = new HashSet<>(paths);
        cleanPaths(modified.getKey(), modified.getValue());
    }

    protected void cleanPaths(Path key, Path value) {
        cleanPath(selected.getKey(), key);
        cleanPath(selected.getValue(), value);
    }

    protected void cleanPath(Path pathToRemove, Path pathToAdd) {
        copy.remove(pathToRemove);
        if (pathToAdd.getTrimmed().size() > 0) {
            copy.add(pathToAdd);
        }
    }

    protected void setNewSolution() {
        solutions = new HashSet<>();
        solution = new Solution(copy, this);
        solutions.add(solution);
    }

    private boolean notValid() {
        return !valid();
    }

    protected boolean valid() {
       return this.solution.isValid();
    }

    protected Map.Entry<Integer, Integer> selectIndexes(Path first, Path second, int nbElementToSwap) {
        int firstIndex = 1;
        int secondIndex = 1;

        // TODO: 28/04/2018 Workaround (remove first and last from path before sending it)
        if (first.getNodes().size() > 2 && second.getNodes().size() > 2) {
            firstIndex = random.nextInt(first.getNodes().size() - nbElementToSwap - 1) + 1;
            secondIndex = random.nextInt(second.getNodes().size() - nbElementToSwap - 1) + 1;
        }

        return  new AbstractMap.SimpleEntry<>(firstIndex, secondIndex);
    }

}
