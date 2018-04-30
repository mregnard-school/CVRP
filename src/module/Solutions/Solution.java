package module.Solutions;

import module.Path;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution {
    private Set<Path> paths;
    private double fitness;
    private boolean valid;
    private double nbNodes;
    NeighbourStrategy neighbourStrategy;

    public Solution(Set<Path> paths, NeighbourStrategy neighbourStrategy) {
        this.paths = paths;
        this.neighbourStrategy = neighbourStrategy;
        this.valid = true;
        this.fitness = computeFitness();
        nbNodes = paths.stream().mapToInt(path -> path.getNodes().size()).sum();
    }

    public Solution(Solution solution) {
        this.neighbourStrategy = solution.getNeighbourStrategy();
        this.valid = true;
        this.paths = new HashSet<>();
        solution.paths.forEach(path -> this.paths.add(new Path(path)));
        this.fitness = computeFitness();
    }

    private double computeFitness() {
        double fitness = 0;
        for(Path path : paths){
            if(path.hasExceeded()){
                valid = false;
                return Double.MAX_VALUE;
            } else {
                double distance = path.getDistance();
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

    public void setPaths(Set<Path> paths) {
        this.paths = paths;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(paths);
    }

    @Override
    public boolean equals(Object obj) {
        if( obj == null) {
            return false;
        }
        if (obj instanceof Solution) {
            Solution other = (Solution) obj;
            return this.paths.equals(other.paths);
        }

        return false;
    }


}
