package module.Algorithms;

import module.Node;
import module.Solutions.Solution;
import module.Solutions.StealNeighbour;
import module.Solutions.SwapNeighbor;
import module.Solutions.SwapSameNeighbourhood;
import module.utils.Helpers;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SimulatedAnnealing extends Algorithm {
    private final Random random;
    private double currentTemperature;
    private double mu;

    public SimulatedAnnealing(int maxStep, List<Node> paths) {
        super(maxStep);
        initialize(paths);
        steps = 0;
        random = Helpers.random;
        mu = 0.99999999;
        initializeTemperature();
    }

    private void initializeTemperature() {
        currentTemperature = 10;
    }

    @Override
    public void next() {
        if (hasNext()) {
            steps++;
            double temperature = temparature();
            setNeighbourhoodStrategy();
            Set<Solution> neighbors = currentSolution.getNextValidSolutions();
            Solution nextSolution = pickRandom(neighbors);

            double probability = random.nextDouble();

            double acceptance = acceptanceProbability(currentSolution, nextSolution, temperature);
            if (acceptance >= probability) {
                currentSolution = nextSolution;
                if (currentSolution.getFitness() < bestSolution.getFitness()) {
                    bestSolution = currentSolution;
                }
            }

            setChanged();
            notifyObservers();
        }
    }

    @Override
    public boolean hasNext() {
        return steps < maxStep && currentTemperature > Math.pow(10, -5);
    }

    private double temparature() {
        currentTemperature = currentTemperature * mu;
        return currentTemperature;
    }

    private void setNeighbourhoodStrategy() {
        int rd = random.nextInt(3);

        switch (rd) {
            case 0:
                currentSolution.setNeighbourStrategy(new SwapNeighbor());
                break;
            case 1:
                currentSolution.setNeighbourStrategy(new SwapSameNeighbourhood());
                break;
            case 2:
                currentSolution.setNeighbourStrategy(new StealNeighbour());
                break;
            default:
                break;
        }
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

        if (secondFitness < firstFitness) {
            return 1;
        }

        return Math.exp(-(secondFitness - firstFitness) / temperature);
    }

    public double getMu() {
        return mu;
    }

    public void setMu(double mu) {
        this.mu = mu;
    }

    public int getSteps() {
        return steps;
    }

    public double getCurrentTemperature()
    {
        return this.currentTemperature;
    }
    public void setCurrentTemperature(double temperature)
    {
        this.currentTemperature = temperature;
    }
}
