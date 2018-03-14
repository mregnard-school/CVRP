package module.Solutions;

import module.Path;

import java.util.Set;

public interface NeighbourStrategy {
    Set<Solution> getNeighbourhood(Solution solution);
}
