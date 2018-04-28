package module.Algorithms;

import module.Solutions.Solution;

import java.util.Observable;

public abstract class Algorithm extends Observable implements AlgorithmStrategy {
    protected Solution bestSolution;
    protected Solution currentSolution;
    protected int steps;

    public Solution getCurrentSolution() {
        return currentSolution;
    }

    public Solution getBestSolution() {
        return bestSolution;
    }

    @Override
    public Solution getBestNeighbourhood() {
        return bestSolution;
    }

    public int getSteps() {
        return steps;
    }
}
