package module.Algorithms;

import module.Node;
import module.Path;
import module.Solutions.Solution;
import module.Solutions.SwapNeighbor;

import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

public abstract class Algorithm extends Observable implements AlgorithmStrategy {
    protected Solution bestSolution;
    protected Solution currentSolution;
    protected int steps;
    protected int maxStep;
    protected int MAX_CAPACITY = 100;

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


    @Override
    public void initialize(List<Node> nodes) {
        Set<Path> paths = new HashSet<>();
        Node warehouse = nodes.get(0);
        nodes.stream().skip(1).forEach(node -> {
            Path path = new Path();
            path.addNode(warehouse);
            path.addNode(node);
            paths.add(path);
        });

        paths.forEach(path -> {
            path.addNode(warehouse);
            path.recompute();
        });
        currentSolution = new Solution(paths, new SwapNeighbor());
        bestSolution = currentSolution;
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

    public int getMAX_CAPACITY() {
        return MAX_CAPACITY;
    }

    public void setMAX_CAPACITY(int MAX_CAPACITY) {
        this.MAX_CAPACITY = MAX_CAPACITY;
    }

    public void forceGFXpdate()
    {
        setChanged();
        notifyObservers();
    }
}
