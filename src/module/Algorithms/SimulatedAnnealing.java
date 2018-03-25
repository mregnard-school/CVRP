package module.Algorithms;

import module.Node;
import module.Path;
import module.Solutions.FirstNeighbourhood;
import module.Solutions.Solution;
import module.utils.Helpers;

import java.util.*;

public class SimulatedAnnealing extends Algorithm {

    private int steps;
    private final int maxStep;
    private final Random random;
    private final double initialAcceptance;
    private final double maxAcceptance;
    private double maxTemperature;
    private double currentTemperature;
    private final double mu;

    private final static int MAX_CAPACITY = 100;

    public SimulatedAnnealing(int maxStep, double initialAcceptance, double maxAcceptance, List<Node> paths) {
        //Create new Solution
        initialize(paths);
        steps = 0; //First iteration
        this.maxStep = maxStep;
        random = Helpers.random;
        this.initialAcceptance = initialAcceptance;
        this.maxAcceptance = maxAcceptance;
        mu = 0.95;
        initializeTemperature();
    }

    private void initializeTemperature() {
        Set<Solution> neighbours = currentSolution.getNextSolutions();
        double delta = Long.MIN_VALUE;
        for (Solution solution : neighbours) {
            double delta_temp = solution.getFitness() - currentSolution.getFitness();
            if (delta_temp > delta) {
                delta = delta_temp;
            }
        }
        //Delta now holds the maximum difference between the neighbourhood

        double initialTemperature = (delta * (-1)) / Math.log(initialAcceptance);
        maxTemperature = Math.log(
                (delta * (-1)) / (initialTemperature * Math.log(maxAcceptance))
        ) / Math.log(mu);

        currentTemperature = initialTemperature;
    }

    private double temparature() {
        if (steps % maxTemperature == 0) {
            currentTemperature = currentTemperature * mu;
        }
        return currentTemperature;
    }

    private Solution pickRandom(Set<Solution> solutions) {
        Iterator<Solution> iterator = solutions.iterator();
        int index = random.nextInt(solutions.size());

        for (int i = 0; i < index; i++) {
            iterator.next();
        }
        return iterator.next();
    }

    private double acceptanceProbability(Solution first, Solution second, double temperature) {
        double firstFitness = first.getFitness();
        double secondFitness = second.getFitness();

        if (firstFitness < secondFitness) {
            return 1;
        }

        return Math.exp(-(secondFitness - firstFitness) / temperature);
    }

    @Override
    public void next() {
        if(hasNext()){
            steps++;
            double temperature = temparature();
            Set<Solution> neighbors = currentSolution.getNextSolutions();
            Solution nextSolution = pickRandom(neighbors);
            double probability = random.nextDouble();
            if (acceptanceProbability(currentSolution, nextSolution, temperature) >= probability) {
                currentSolution = nextSolution;
                if (currentSolution.getFitness() < bestSolution.getFitness()) {
                    bestSolution = currentSolution;
                }
            }

            System.out.println(bestSolution.getFitness());

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
        Set<Path> paths = new HashSet<>();
        Node centralNode = nodes.get(0);

        Path currentPath = new Path(MAX_CAPACITY);
        currentPath.addNode(centralNode);

        for (Node node : nodes.subList(1, nodes.size()-1)) { // We skip the first node, as it's the central
            if (!currentPath.canAddNode(node)) {
                currentPath.recompute();
                paths.add(currentPath);
                currentPath = new Path(MAX_CAPACITY);
                currentPath.addNode(centralNode);
                currentPath.addNode(node);
            } else {
                currentPath.addNode(node);
            }
        }
        currentSolution = new Solution(paths, new FirstNeighbourhood());
        bestSolution = currentSolution;
        System.out.println("Initial solution : " + bestSolution.getFitness());
    }
}
