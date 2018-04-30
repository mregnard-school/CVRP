package module.Algorithms;

import module.Node;
import module.Solutions.Solution;

import java.util.List;
import java.util.Observable;

public abstract class Algorithm extends Observable implements AlgorithmStrategy {
    protected Solution bestSolution;
    protected Solution currentSolution;
    protected int steps;
    protected int maxStep;

    public Algorithm(int maxStep)
    {
        this.maxStep = maxStep;
    }

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

    public void setMaxStep(int maxStep)
    {
        this.maxStep = maxStep;
    }

    public int getMaxStep()
    {
        return this.maxStep;
    }

    public boolean hasNext() {
        return steps < maxStep;
    }
}
