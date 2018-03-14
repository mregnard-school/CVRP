package module.Solutions;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import module.Path;

import java.util.Set;

public class Solution {
    private Set<Path> paths;
    private long fitness;

    NeighbourStrategy neighbourStrategy;

    public Solution(Set<Path> paths)
    {
        this.paths = paths;
        this.fitness = getFitness();
    }

    public long getFitness()
    {
        return 0;
    }

    public Set<Solution> getNextSolutions()
    {
        return neighbourStrategy.getNeighbourhood(this);
    }

    public NeighbourStrategy getNeighbourStrategy() {
        return neighbourStrategy;
    }

    public void setNeighbourStrategy(NeighbourStrategy neighbourStrategy) {
        this.neighbourStrategy = neighbourStrategy;
    }

    public Set<Path> getPaths() {
        return paths;
    }
}
