package module.Solutions;

import module.Path;

import java.util.*;

public class PermutationNeighbourhood extends AbstractNeighbourhood {

    private Set<Map.Entry<Path, Path>> newPaths;

    @Override
    protected void initialize() {
        super.initialize();
        solutions = new HashSet<>();
    }

    @Override
    protected boolean valid() {
        return true;
    }

    @Override
    protected void cleanUp() {
        newPaths.forEach(entry -> {
            copy = new HashSet<>(paths);
            cleanPaths(entry.getKey(), entry.getValue());
            solutions.add(new Solution(copy, this));
        });
    }

    @Override
    protected void setNewSolution() {
        //Do nothing
    }

    @Override
    protected Map.Entry<Path, Path> neighbourhoodCalculation() {
        newPaths = swapper.swap();

        return newPaths.stream().findFirst().orElseThrow(NoSuchElementException::new);
    }


}
