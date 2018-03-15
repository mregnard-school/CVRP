package module.Solutions;

import module.Path;

import java.util.Set;

public class Solution {
    private Set<Path> paths;
    private long fitness;

    NeighbourStrategy neighbourStrategy;

    public Solution(Set<Path> paths, NeighbourStrategy neighbourStrategy) {
        this.paths = paths;
        this.neighbourStrategy = neighbourStrategy;
        this.fitness = getFitness();
    }

    public long getFitness() {
        return 0;
    }

    public Set<Solution> getNextSolutions() {
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
