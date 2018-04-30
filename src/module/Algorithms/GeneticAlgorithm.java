package module.Algorithms;

import module.Node;
import module.Solutions.Solution;
import module.Solutions.StealNeighbour;
import module.Solutions.SwapNeighbor;
import module.Solutions.SwapSameNeighbourhood;
import module.exceptions.SolutionSwapper;
import module.utils.Helpers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneticAlgorithm extends Algorithm {

    private final int maxStep;
    private final Random random;
    private List<Solution> currentPopulation = new ArrayList<>();
    private double bestSelectionRate;
    private double crossoverRate;
    private double mutationRate;

    public GeneticAlgorithm(int maxStep, List<Node> nodes) {
        this.steps = 0;
        this.maxStep = maxStep;
        this.random = Helpers.random;
        bestSelectionRate = 0.1;
        crossoverRate = 0.5;
        mutationRate = 0.05;
        initialize(nodes);
    }

    public void next() {
        if (hasNext()) {
            steps++;

            List<Solution> newPopulation = new ArrayList<>(selection());
            newPopulation = mutations(newPopulation);

            currentPopulation = newPopulation;

            updateBestSolution();
        }
    }

    public List<Solution> selection() {
        List<Solution> bests = selectBestPercentage(bestSelectionRate);
        List<Solution> selectedForCrossover = selectForCrossover(crossoverRate);

        List<Solution> selected = new ArrayList<>(bests);
        selected.addAll(selectedForCrossover);

        return selected;
    }

    private List<Solution> selectBestPercentage(double percentage) {
        int nb = (int) percentage * currentPopulation.size();
        currentPopulation.sort(Comparator.comparing(Solution::getFitness, Comparator.reverseOrder()));
        return currentPopulation.stream().limit(nb).collect(Collectors.toList());
    }

    private List<Solution> selectForCrossover(double crossoverPercentage) {
        List<Solution> offsprings = new ArrayList<>();
        int nb = (int) crossoverPercentage * currentPopulation.size();
        while (offsprings.size() < nb) {
            Solution firstParent = roulette();
            Solution secondParent = roulette();

            Solution offspring = crossover(firstParent, secondParent);
            offsprings.add(offspring);
        }

        return offsprings;
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
                setNeighborStrategy(solution);
                Solution next = solution.getNextValidSolutions()
                        .stream()
                        .findFirst()
                        .orElseThrow(NoSuchElementException::new);

                return Stream.of(next);
            }

            return Stream.of(solution);
        }).collect(Collectors.toList());

        return population;
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
    }

    private void updateBestSolution() {
        currentPopulation.forEach(solution -> {
            if (solution.getFitness() < bestSolution.getFitness()) {
                bestSolution = solution;
                currentSolution = bestSolution;
            }
        });
    }

    public void next2() {
        if (hasNext()) {
            steps++;

            Solution tmpSolution;
            int failSafe = 0;
            Set<Double> fitnesses = new HashSet<>();
            currentPopulation = new ArrayList<>();

            currentSolution.setNeighbourStrategy(new SwapNeighbor());
            while (currentPopulation.size() < 50 && failSafe < 100) {
                tmpSolution = currentSolution.getNextValidSolutions().iterator().next();
                if (!currentPopulation.contains(tmpSolution) && !fitnesses.contains(tmpSolution.getFitness())) {
                    currentPopulation.add(tmpSolution);
                    fitnesses.add(tmpSolution.getFitness());
                    failSafe = 0;
                } else {
                    failSafe++;
                }
            }

            //System.out.println(currentPopulation.size());

            currentPopulation.sort(Comparator.comparing(Solution::getFitness).reversed());


            /*currentSolution.setNeighbourStrategy(new SwapSameNeighbourhood());
            solutions.addAll(currentSolution.getNextValidSolutions());
            currentSolution.setNeighbourStrategy(new StealNeighbour());
            solutions.addAll(currentSolution.getNextValidSolutions());*/
            //System.out.println("Solutions: " + currentPopulation.size());
            //final double highestFitness = (currentPopulation.stream().max(Comparator.comparing( Solution::getFitness )).get().getFitness()*1.5);

            final double highestFitness = currentPopulation.get(0).getFitness();
            double rouletteSize = currentPopulation.stream().mapToDouble(s -> (highestFitness - s.getFitness())).sum();

            //System.out.println(rouletteSize);


            Set<Solution> firstBestSolutions = new HashSet<>();


            //long start = System.nanoTime();
            while (firstBestSolutions.size() < 10) {
                double randomPick = random.nextInt((int) rouletteSize);
                double fitnessSum = 0;
                for (Solution s : currentPopulation) {
                    if (randomPick >= fitnessSum && randomPick <= fitnessSum + (highestFitness - s.getFitness())) {
                        if (!firstBestSolutions.contains(s)) {
                            firstBestSolutions.add(s);
                        }
                        break;
                    }
                    fitnessSum += highestFitness - s.getFitness();
                }
            }

            //System.out.println("115: " + (System.nanoTime()-start));
            //start = System.nanoTime();


            List<Solution> bestSolutions = firstBestSolutions.stream().collect(Collectors.toList());
            //System.out.println("best solutions: " + bestSolutions.size());
            for (int solOneIndex = 0; solOneIndex < bestSolutions.size(); solOneIndex++) {
                int solTwoIndex;
                do {
                    solTwoIndex = random.nextInt(bestSolutions.size());
                    //System.out.println(solOneIndex  + "!=" + solTwoIndex);
                } while (solOneIndex == solTwoIndex);

                if (bestSolutions.get(solOneIndex) == null) {
                    //System.out.println("soleOneIndex: " + solOneIndex);
                }
                SolutionSwapper sw = new SolutionSwapper(bestSolutions.get(solOneIndex), bestSolutions.get(solTwoIndex));
                sw.swap();
            }

            //System.out.println("138: " + (System.nanoTime()-start));
            //start = System.nanoTime();

            ListIterator<Solution> bestItt = bestSolutions.listIterator();
            List<Solution> newBestSolutions = new ArrayList<>();
            for (Solution s : bestSolutions) {
                if (random.nextInt(100) < 4) {
                    newBestSolutions.add(s);
                } else {
                    s.setNeighbourStrategy(new SwapSameNeighbourhood());
                    newBestSolutions.add(s.getNextValidSolutions().iterator().next());
                }
            }
            bestSolutions = newBestSolutions;
            //System.out.println("156: " + (System.nanoTime()-start));
            //start = System.nanoTime();

            Solution tmpBestSolution = bestSolutions.get(0);
            //System.out.println("bestSolution : " + bestSolution.getFitness());
            for (Solution s : bestSolutions) {
                //System.out.println("\t" + s.getFitness());
                if (s.getFitness() < tmpBestSolution.getFitness()) {
                    tmpBestSolution = s;
                }
            }

            //System.out.println("170: " + (System.nanoTime()-start));
            //start = System.nanoTime();

            //currentSolution = currentSolution.getFitness() < tmpBestSolution.getFitness() ? currentSolution : tmpBestSolution;
            currentSolution = tmpBestSolution;


            bestSolution = bestSolution.getFitness() < currentSolution.getFitness() ? bestSolution : currentSolution;

            currentSolution = bestSolution;


            /*if(currentSolution.getFitness() < bestSolution.getFitness())
            {
                bestSolution = currentSolution;
            }*/


            setChanged();
            notifyObservers();
        }
    }

    @Override
    public boolean hasNext() {
        return steps < maxStep;
    }
}
