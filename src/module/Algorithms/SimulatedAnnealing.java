package module.Algorithms;

import module.Node;
import module.Path;
import module.Solutions.*;
import module.utils.Helpers;

import java.util.*;

public class SimulatedAnnealing extends Algorithm {

    private final int maxStep;
    private final Random random;
    private final double initialAcceptance;
    private final double maxAcceptance;
    private double maxTemperature;
    private double currentTemperature;
    private final double mu;


    private final static int MAX_CAPACITY = 100;

    public SimulatedAnnealing(int maxStep, double initialAcceptance, double maxAcceptance, List<Node> paths) {
        initializeAPathByNode(paths);
        steps = 0; //First iteration
        this.maxStep = maxStep;
        random = Helpers.random;
        this.initialAcceptance = initialAcceptance;
        this.maxAcceptance = maxAcceptance;
        mu = 0.9995;
        initializeTemperature();
        System.out.println(temparature());
    }

    @Override
    public void initialize(List<Node> nodes) {
        Set<Path> paths = new HashSet<>();
        Node centralNode = nodes.get(0);

        Path currentPath = new Path(MAX_CAPACITY);
        currentPath.addNode(centralNode);

        for (Node node : nodes.subList(1, nodes.size())) { // We skip the first node, as it's the central
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
        // TODO: 25/04/2018 Add centralNode at the end of each path
        currentSolution = new Solution(paths, new StealNeighbour());
        bestSolution = currentSolution;
    }

    private void initializeAPathByNode(List<Node> nodes) {

        Set<Path> paths = new HashSet<>();
        Node warehouse = nodes.get(0);
        nodes.stream().skip(1).forEach(node -> {
            Path path = new Path(MAX_CAPACITY);
            path.addNode(warehouse);
            path.addNode(node);
            paths.add(path);
        });

        paths.forEach(Path::recompute);
        currentSolution = new Solution(paths, new SwapNeighbor());
        bestSolution = currentSolution;
    }

    private void initializeTemperature() {
        double delta = calculateDelta();
        delta = 120;
        double initialTemperature = (delta * (-1)) / Math.log(initialAcceptance);
        maxTemperature = Math.log(
                (delta * (-1)) / (initialTemperature * Math.log(maxAcceptance))
        ) / Math.log(mu);

        currentTemperature = initialTemperature;
        currentTemperature = 10;
        System.out.println("Delta : " + delta);
        System.out.println("Initial temp : " + initialTemperature);

    }

    private double calculateDelta() {
        currentSolution.setNeighbourStrategy(new PermutationNeighbourhood());
        Set<Solution> neighbours = currentSolution.getNextValidSolutions();
        currentSolution.setNeighbourStrategy(new StealNeighbour());

        return neighbours.stream()
                .mapToDouble(solution -> solution.getFitness() - currentSolution.getFitness())
                .map(Math::abs)
                .max()
                .orElse(Double.POSITIVE_INFINITY);
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
            if (acceptanceProbability(currentSolution, nextSolution, temperature) >= probability) {
                currentSolution = nextSolution;
                if (currentSolution.getFitness() < bestSolution.getFitness()) {
                    bestSolution = currentSolution;
                }
            }

            setChanged();
            notifyObservers();
        }
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

    @Override
    public boolean hasNext() {
        return steps < maxStep;
    }

    private double temparature() {
        currentTemperature = currentTemperature * mu;
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

        if (secondFitness < firstFitness) {
            return 1;
        }

        return Math.exp(-(secondFitness - firstFitness) / temperature);
    }

    public int getSteps() {
        return steps;
    }
}
