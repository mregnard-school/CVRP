package module.Solutions;

import java.util.HashSet;
import java.util.Set;

public class FirstNeighbourhood implements NeighbourStrategy {

    @Override
    public Set<Solution> getNeighbourhood(Solution solution) {
        Set<Solution> nexSolutions = new HashSet<>();
        nexSolutions.add(solution);
        return nexSolutions;
    }
}
