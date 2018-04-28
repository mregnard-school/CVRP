package module.Solutions;

import module.Path;
import module.utils.PathSwapper;

import java.util.*;

public class PermutationNeighbourhood extends AbstractNeighbourhood {
    
    private Set<Solution> nextSolutions = new HashSet<>();

    @Override
    public Set<Solution> getNeighbourhood(Solution solution) {
        paths = solution.getPaths();
        Set<Solution> solutions = new HashSet<>();
        selected = selectTwoDifferentPath();
        swapper = new PathSwapper(selected.getKey(), selected.getValue());
        Set<Map.Entry<Path, Path>> newPaths = swapper.swap();
        newPaths.forEach(entry -> {
            copy = new HashSet<>(paths);
            cleanPaths(entry.getKey(), entry.getValue());
            solutions.add(new Solution(copy, this));
        });

        return solutions;
    }

    @Override
    protected Map.Entry<Path, Path> neighbourhoodCalculation() {
       return null;
    }
}
