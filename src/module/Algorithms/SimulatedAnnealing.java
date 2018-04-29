package module.Algorithms;

import module.Node;
import module.Path;
import module.Solutions.Solution;
import module.Solutions.StealNeighbour;
import module.Solutions.SwapNeighbor;
import module.Solutions.SwapSameNeighbourhood;
import module.utils.Helpers;

import java.util.*;

public class SimulatedAnnealing extends Algorithm {

    private final int maxStep;
    private final Random random;
    private double currentTemperature;
    private final double mu;


    private final static int MAX_CAPACITY = 100;

    public SimulatedAnnealing(int maxStep, List<Node> paths) {
        initialize(paths);
        steps = 0;
        this.maxStep = maxStep;
        random = Helpers.random;
        mu = 0.99999999;
        initializeTemperature();
    }

    @Override
    public void initialize(List<Node> nodes) {
        Set<Path> paths = new HashSet<>();
        Node warehouse = nodes.get(0);
        nodes.stream().skip(1).forEach(node -> {
            Path path = new Path(MAX_CAPACITY);
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

    private void initializeTemperature() {
        //Gives 100 for 30 nodes and 500 for 60 nodes
        currentTemperature = currentSolution.getPaths().size() * 10 / 3;
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
                System.out.println("accepted");
                currentSolution = nextSolution;
                if (currentSolution.getFitness() < bestSolution.getFitness()) {
                    bestSolution = currentSolution;
                }
            }

            System.out.println("Temp : " + temperature);
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

    public int getSteps() {
        return steps;
    }
}
