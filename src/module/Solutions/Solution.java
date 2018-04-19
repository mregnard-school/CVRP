package module.Solutions;

import module.Path;

import java.util.Set;
import java.util.stream.Collectors;

public class Solution {
    private Set<Path> paths;
    private double fitness;
    private boolean valid;

    NeighbourStrategy neighbourStrategy;

    public Solution(Set<Path> paths, NeighbourStrategy neighbourStrategy) {
        this.paths = paths;
        this.neighbourStrategy = neighbourStrategy;
        this.valid = true;
        this.fitness = computeFitness();
    }

    private double computeFitness() {
        double fitness = 0;
        for(Path path : paths){
            double distance = path.getDistance();
            if(distance < 0 ){
                valid = false;
                return  Double.MAX_VALUE;
            } else {
                fitness += distance;
            }
        }
        return fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public Set<Solution> getNextSolutions() {
        return neighbourStrategy.getNeighbourhood(this);
    }

    public Set<Solution> getNextValidSolutions() {
        return getNextSolutions().stream()
                .filter(Solution::isValid)
                .collect(Collectors.toSet());
    }

    public boolean isValid() {
        return valid;
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
