package module.Algorithms;

import module.Node;
import module.Path;
import module.Solutions.FirstNeighbourhood;
import module.Solutions.Solution;
import module.utils.Seeder;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimulatedAnnealing extends Algorithm {

    private int steps;
    private final int maxStep;
    private final Random random;
    private final double initialAcceptance;
    private final double maxAcceptance;
    private double maxTemperature;
    private double currentTemperature;
    private double mu;


    public SimulatedAnnealing(int maxStep, double initialAcceptance, double maxAcceptance, List<Node> paths) {
        //Create new Solution
        initialize(paths);
        steps = 0; //First iteration
        this.maxStep = maxStep;
        random = new Random(Seeder.SEED);
        this.initialAcceptance = initialAcceptance;
        this.maxAcceptance = maxAcceptance;
        mu = 0.95;
        initializeTemperature();
    }

    private void initializeTemperature() {
        Set<Solution> neighbours = currentSolution.getNextSolutions();
        long delta = Long.MIN_VALUE;
        for (Solution solution : neighbours) {
            long delta_temp = solution.getFitness() - currentSolution.getFitness();
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
        if (steps % maxTemperature == 0){
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

    @Override
    public Solution getBestNeighbourhood() {
        return null;
    }

    private double acceptanceProbability(Solution first, Solution second, double temperature) {
        long firstFitness = first.getFitness();
        long secondFitness = second.getFitness();

        if (firstFitness > secondFitness) {
            return 1;
        }

        return Math.exp(-(secondFitness - firstFitness) / temperature);
    }

    @Override
    public void next() {
        steps++;
        double temperature = temparature();
        Set<Solution> neighbors = currentSolution.getNextSolutions();
        Solution nextSolution = pickRandom(neighbors);
        double probability = random.nextDouble();
        if (acceptanceProbability(currentSolution, nextSolution, temperature) >= probability) {
            currentSolution = nextSolution;
            if (currentSolution.getFitness() > bestSolution.getFitness()) {
                bestSolution = currentSolution;
            }
        }

        setChanged();
        notifyObservers();
    }

    @Override
    public boolean hasNext() {
        return steps < maxStep;
    }

    @Override
    public void initialize(List<Node> nodes) {
        int maxCapacity = 100;
        Set<Path> paths = new HashSet<>();
        Path currentPath = new Path(maxCapacity);
        for(Node node : nodes) {
            if (!currentPath.canAddNode(node)) {
                paths.add(currentPath);
                currentPath = new Path(maxCapacity);
                currentPath.addNode(node);
            }
            else
            {
                currentPath.addNode(node);
            }

        }
        currentSolution = new Solution(paths, new FirstNeighbourhood());
        bestSolution = currentSolution;
    }
}
