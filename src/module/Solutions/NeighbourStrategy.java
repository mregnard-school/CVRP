package module.Solutions;

import java.util.Set;

public interface NeighbourStrategy {
    Set<Solution> getNeighbourhood(Solution solution);
}
