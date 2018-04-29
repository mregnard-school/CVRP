package module.Algorithms;

import module.Node;
import module.Path;
import module.Solutions.*;
import module.utils.Helpers;

import java.util.*;
import java.util.stream.Collectors;

public class SimulatedGenetic extends Algorithm {

    private final int maxStep;
    private final static int MAX_CAPACITY = 100;
    private final Random random;

    public SimulatedGenetic(int maxStep, List<Node> nodes) {
        this.steps = 0;
        this.maxStep = maxStep;
        this.random = Helpers.random;
        initializeAPathByNode(nodes);
    }


    private void initializeAPathByNode(List<Node> nodes) {

        Set<Path> paths = new HashSet<>();
        Node warehouse = nodes.get(0);
        Path tmpPath = null;
        for(Node node : nodes.stream().skip(1).collect(Collectors.toList()))
        {
            if(tmpPath == null || tmpPath.getCurrentCapacity()+node.getCapacity()>tmpPath.getMaxCapacity())
            {
                tmpPath = new Path(MAX_CAPACITY);
                tmpPath.addNode(warehouse);
                paths.add(tmpPath);
            }
            tmpPath.addNode(node);
        }

        paths.forEach(path -> {
            path.addNode(warehouse);
            path.recompute();
        });
        currentSolution = new Solution(paths, new SwapNeighbor());
        bestSolution = currentSolution;
    }

    @Override
    public void next() {
        if (hasNext()) {
            steps++;

            Set<Solution> solutions = currentSolution.getNextValidSolutions();
            int rouletteSize = 0;
            solutions.
            if(currentSolution.getFitness() < bestSolution.getFitness())
            {
                bestSolution = currentSolution;
            }


            setChanged();
            notifyObservers();
        }
    }

    @Override
    public boolean hasNext() {
        return steps < maxStep;
    }

    @Override
    public void initialize(List<Node> nodes) {

    }

    private Solution pickRandom(Set<Solution> solutions) {
        Iterator<Solution> iterator = solutions.iterator();
        int index = random.nextInt(solutions.size());

        for (int i = 0; i < index; i++) {
            iterator.next();
        }
        return iterator.next();
    }
}
