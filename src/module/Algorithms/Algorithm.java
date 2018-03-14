package module.Algorithms;

import module.Solutions.Solution;

import java.util.Observable;

public abstract class Algorithm extends Observable implements AlgorithmStrategy {
    protected int currentIterationNumber;
    protected int maxIterations = 100;
    protected Solution bestSolution;
    protected Solution currentSolution;

    public Solution getCurrentSolution()
    {
        return currentSolution;
    }

}
