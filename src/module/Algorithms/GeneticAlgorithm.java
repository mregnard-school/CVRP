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
    private final Random random;
    private int populationSize;
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
        this.random = Helpers.random;
        bestSelectionRate = 0.1;
        crossoverRate = 0.9;
        randomRate = 1 - crossoverRate - bestSelectionRate;
        mutationRate = 0.4;
        populationSize = 10;
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
        List<Solution> bests = new ArrayList<>();
        int nb = (int) (bestSelectionRate * currentPopulation.size());
        currentPopulation.sort(Comparator.comparing(Solution::getFitness));
        currentPopulation.stream()
                .limit(nb)
                .forEach(best -> bests.add(new Solution(best)));
        return bests;
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
        double total = currentPopulation.stream().mapToDouble(Solution::getFitness).sum();
        double totalDividende = total * (currentPopulation.size() - 1);

        TreeMap<Double, Solution> probabilities = new TreeMap<>();

        currentPopulation.forEach(solution -> {
            double proba = (total - solution.getFitness()) / totalDividende;
            probabilities.put(proba, solution);
        });

        double rd = random.nextDouble();

        Solution solution = Helpers.cumulativeSum(probabilities.entrySet().stream()).flatMap(entry -> {
            double proba = entry.getKey();
            if (rd < proba) {
                return Stream.of(entry.getValue());
            }
            return Stream.empty();
        }).findFirst().orElse(probabilities.lastEntry().getValue());

        return solution;
    }

    private Solution crossover(Solution firstParent, Solution secondParent) {
        if (firstParent.equals(secondParent)) {
            return crossoverOnSame(firstParent);
        } else {
            return crossOverWithTwoDifferent(firstParent, secondParent); // TODO: 30/04/2018 Crossover
        }
    }

    private Solution crossoverOnSame(Solution firstParent) {
        firstParent.setNeighbourStrategy(new SwapNeighbor());
        Solution offspring = firstParent.getNextValidSolutions()
                .stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        return new Solution(offspring);
    }

    private Solution crossOverWithTwoDifferent(Solution firstParent, Solution secondParent) {
        Solution cloned = new Solution(secondParent);
        Set<Path> paths = new HashSet<>(cloned.getPaths());
        int firstIndex = random.nextInt(firstParent.getPaths().size());

        Path first = new Path(firstParent.getPaths().stream().skip(firstIndex).findFirst().orElseThrow(NoSuchElementException::new));

        List<Node> copy = new ArrayList<>(first.getNodes());

        for (Node node : copy) {
            for (int i = 0; i < paths.size(); i++) {
                Path currentPath = paths.stream().skip(i).findFirst().orElseThrow(NoSuchElementException::new);

                if (currentPath.contains(node)) {
                    currentPath.getNodes().remove(node);
                    break;
                }
            }
        }

        paths.forEach(Path::recompute);
        Node warehouse = first.getWarehouse();
        Path newPath = new Path();
        newPath.addNode(warehouse);
        first.getNodes().forEach(newPath::addNode);
        newPath.addNode(warehouse);
        paths.add(newPath);

        Set<Path> nonEmpty = new HashSet<>();
        for (Path path : paths) {
            if (path.getNodes().size() > 0) {
                nonEmpty.add(path);
            }
        }

        Solution offspring = new Solution(nonEmpty, new SwapNeighbor());
        return offspring;
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

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public double getBestSelectionRate() {
        return bestSelectionRate;
    }

    public void setBestSelectionRate(double bestSelectionRate) {
        this.bestSelectionRate = bestSelectionRate;
        randomRate = 1 - crossoverRate - bestSelectionRate;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
        randomRate = 1 - crossoverRate - bestSelectionRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }
}
