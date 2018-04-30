package module.Algorithms;

import module.Node;
import module.Path;
import module.Solutions.Solution;
import module.Solutions.StealNeighbour;
import module.Solutions.SwapNeighbor;
import module.Solutions.SwapSameNeighbourhood;
import module.utils.Helpers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneticAlgorithm extends Algorithm {

    private final int maxStep;
    private final Random random;
    private final int populationSize;
    private List<Solution> currentPopulation;
    private List<Node> nodes;
    private double bestSelectionRate;
    private double crossoverRate;
    private double mutationRate;
    private double randomRate;

    public GeneticAlgorithm(int maxStep, List<Node> nodes) {
        super(maxStep);
        this.nodes = nodes;
        this.steps = 0;
        this.maxStep = maxStep;
        this.random = Helpers.random;
        bestSelectionRate = 0.2;
        crossoverRate = 0.8;
        randomRate = 1 - crossoverRate - bestSelectionRate;
        mutationRate = 0.5;
        populationSize = 150;
        currentPopulation = new ArrayList<>();
        initialize(nodes);
    }

    @Override
    public void initialize(List<Node> nodes) {
        Set<Solution> solutions = new HashSet<>();
        while (solutions.size() < populationSize) {
            solutions.add(createRandomSolution());
        }

        currentPopulation = new ArrayList<>(solutions);
        currentSolution = currentPopulation.get(0);
        bestSolution = currentSolution;
        updateBestSolution();
    }

    public Solution createRandomSolution() {
        List<Node> copy = new ArrayList<>(nodes);
        Node warehouse = nodes.get(0);
        copy.remove(warehouse);
        Collections.shuffle(copy, random);
        copy.add(0, warehouse);

        Set<Path> paths = new HashSet<>();
        Path currentPath = new Path();
        currentPath.addNode(warehouse);

        Iterator<Node> iterator = copy.iterator();
        iterator.next(); //warehouse

        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (!currentPath.canAddNode(node)) {
                paths.add(currentPath);
                currentPath = new Path();
                currentPath.addNode(warehouse);
            }
            currentPath.addNode(node);
        }

        paths.add(currentPath);

        paths.parallelStream().forEach(path -> {
            path.addNode(warehouse);
            path.recompute();
        });


        return new Solution(paths, new SwapNeighbor());

    }

    public void next() {
        if (hasNext()) {
            steps++;

            List<Solution> newPopulation = new ArrayList<>(selection());
            newPopulation = mutations(newPopulation);
            currentPopulation = newPopulation;
            updateBestSolution();

            setChanged();
            notifyObservers();
        }
    }

    public List<Solution> selection() {
        List<Solution> bests = selectBestPercentage();
        List<Solution> selectedForCrossover = selectForCrossover();
        List<Solution> randoms = selectRandom();

        List<Solution> selected = new ArrayList<>(bests);
        selected.addAll(selectedForCrossover);
        selected.addAll(randoms);

        return selected;
    }

    private List<Solution> selectBestPercentage() {
        int nb = (int) (bestSelectionRate * currentPopulation.size());
        currentPopulation.sort(Comparator.comparing(Solution::getFitness, Comparator.reverseOrder()));
        return currentPopulation.stream().limit(nb).collect(Collectors.toList());
    }

    private List<Solution> selectForCrossover() {
        List<Solution> offsprings = new ArrayList<>();
        int nb = (int) (crossoverRate * currentPopulation.size());
        while (offsprings.size() < nb) {
            Solution firstParent = roulette();
            Solution secondParent = roulette();

            Solution offspring = crossover(firstParent, secondParent);
            offsprings.add(offspring);
        }

        return offsprings;
    }

    private List<Solution> selectRandom() {
        List<Solution> randoms = new ArrayList<>();
        int nb = (int) (randomRate * currentPopulation.size());
        while (randoms.size() < nb) {
            randoms.add(createRandomSolution());
        }

        return randoms;
    }

    private Solution roulette() {
        List<Double> cummulatedDesc = new ArrayList<>();
        currentPopulation.sort(Comparator.comparing(Solution::getFitness, Comparator.reverseOrder()));

        Iterator<Solution> iterator = currentPopulation.iterator();
        cummulatedDesc.add(iterator.next().getFitness());
        while (iterator.hasNext()) {
            double fitness = iterator.next().getFitness();
            cummulatedDesc.add(fitness + cummulatedDesc.get(cummulatedDesc.size() - 1));
        }

        int maximumCumulated = cummulatedDesc.get(cummulatedDesc.size() - 1).intValue();

        int rand = random.nextInt(maximumCumulated);
        iterator = currentPopulation.iterator();
        for (Double value : cummulatedDesc) {
            Solution solution = iterator.next();
            if (rand <= value) {
                return solution;
            }
        }

        //Should not be called
        return null;
    }

    private Solution crossover(Solution firstParent, Solution secondParent) {
        if (firstParent.equals(secondParent)) {
            firstParent.setNeighbourStrategy(new SwapNeighbor());
            Solution offspring = firstParent.getNextValidSolutions()
                    .stream()
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);

            return offspring;
        } else {
            return firstParent; // TODO: 30/04/2018 Crossover
        }
    }

    private List<Solution> mutations(List<Solution> population) {

        population = population.stream().flatMap(solution -> {
            double rd = random.nextDouble();
            if (rd < mutationRate) {
                Solution mutated = mutate(solution);
                return Stream.of(mutated);
            }
            return Stream.of(solution);
        }).collect(Collectors.toList());

        return population;
    }

    private Solution mutate(Solution solution) {
        setNeighborStrategy(solution);
        Solution solution1 = solution.getNextValidSolutions()
                .stream()
                .findFirst()
                .orElse(null);
        return solution1;
    }

    private void setNeighborStrategy(Solution solution) {
        int rd = random.nextInt(3);

        switch (rd) {
            case 0:
                solution.setNeighbourStrategy(new SwapNeighbor());
                break;
            case 1:
                solution.setNeighbourStrategy(new SwapSameNeighbourhood());
                break;
            case 2:
                solution.setNeighbourStrategy(new StealNeighbour());
                break;
            default:
                break;
        }

        // TODO: 30/04/2018 Put in Algorithm to avoid copy paster code
    }

    private void updateBestSolution() {
        currentPopulation.forEach(solution -> {
            if (solution.getFitness() < bestSolution.getFitness()) {
                bestSolution = solution;
                currentSolution = bestSolution;
            }
        });
    }

    @Override
    public boolean hasNext() {
        return steps < maxStep;
    }
}
