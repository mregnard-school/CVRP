package module.Algorithms;

import module.Node;
import module.Path;
import module.Solutions.*;
import module.exceptions.SolutionSwapper;
import module.utils.Helpers;

import java.util.*;
import java.util.stream.Collectors;

public class GeneticAlgorithm extends Algorithm {

    private final static int MAX_CAPACITY = 100;
    private final Random random;
    private List<Solution> currentSolutions = new ArrayList<>();

    public GeneticAlgorithm(int maxStep, List<Node> nodes) {
        super(maxStep);
        this.steps = 0;
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

            Solution tmpSolution;
            int failSafe = 0;
            Set<Double> fitnesses = new HashSet<>();
            currentSolutions = new ArrayList<>();

            currentSolution.setNeighbourStrategy(new SwapNeighbor());
            while(currentSolutions.size() < 50 && failSafe < 100)
            {
                tmpSolution = currentSolution.getNextValidSolutions().iterator().next();
                if(!currentSolutions.contains(tmpSolution) && !fitnesses.contains(tmpSolution.getFitness()))
                {
                    currentSolutions.add(tmpSolution);
                    fitnesses.add(tmpSolution.getFitness());
                    failSafe = 0;
                }
                else
                {
                    failSafe++;
                }
            }

            //System.out.println(currentSolutions.size());

            currentSolutions.sort(Comparator.comparing(Solution::getFitness).reversed());


            /*currentSolution.setNeighbourStrategy(new SwapSameNeighbourhood());
            solutions.addAll(currentSolution.getNextValidSolutions());
            currentSolution.setNeighbourStrategy(new StealNeighbour());
            solutions.addAll(currentSolution.getNextValidSolutions());*/
            //System.out.println("Solutions: " + currentSolutions.size());
            //final double highestFitness = (currentSolutions.stream().max(Comparator.comparing( Solution::getFitness )).get().getFitness()*1.5);

            final double highestFitness = currentSolutions.get(0).getFitness();
            double rouletteSize = currentSolutions.stream().mapToDouble(s -> (highestFitness - s.getFitness())).sum();

            //System.out.println(rouletteSize);


            Set<Solution> firstBestSolutions = new HashSet<>();


            //long start = System.nanoTime();
            while(firstBestSolutions.size() < 10)
            {
                double randomPick = random.nextInt((int)rouletteSize);
                double fitnessSum = 0;
                for(Solution s : currentSolutions)
                {
                    if(randomPick >= fitnessSum && randomPick <= fitnessSum+(highestFitness-s.getFitness()))
                    {
                        if(!firstBestSolutions.contains(s))
                        {
                            firstBestSolutions.add(s);
                        }
                        break;
                    }
                    fitnessSum+=highestFitness-s.getFitness();
                }
            }

            //System.out.println("115: " + (System.nanoTime()-start));
            //start = System.nanoTime();


            List<Solution> bestSolutions = firstBestSolutions.stream().collect(Collectors.toList());
            //System.out.println("best solutions: " + bestSolutions.size());
            for(int solOneIndex=0; solOneIndex<bestSolutions.size(); solOneIndex++)
            {
                int solTwoIndex;
                do {
                    solTwoIndex = random.nextInt(bestSolutions.size());
                    //System.out.println(solOneIndex  + "!=" + solTwoIndex);
                } while(solOneIndex == solTwoIndex);

                if(bestSolutions.get(solOneIndex) == null)
                {
                    //System.out.println("soleOneIndex: " + solOneIndex);
                }
                SolutionSwapper sw = new SolutionSwapper(bestSolutions.get(solOneIndex), bestSolutions.get(solTwoIndex));
                sw.swap();
            }

            //System.out.println("138: " + (System.nanoTime()-start));
            //start = System.nanoTime();

            ListIterator<Solution> bestItt = bestSolutions.listIterator();
            List<Solution> newBestSolutions = new ArrayList<>();
            for(Solution s : bestSolutions)
            {
                if(random.nextInt(100) < 4)
                {
                    newBestSolutions.add(s);
                }
                else
                {
                    s.setNeighbourStrategy(new SwapSameNeighbourhood());
                    newBestSolutions.add(s.getNextValidSolutions().iterator().next());
                }
            }
            bestSolutions = newBestSolutions;
            //System.out.println("156: " + (System.nanoTime()-start));
            //start = System.nanoTime();

            Solution tmpBestSolution = bestSolutions.get(0);
            //System.out.println("bestSolution : " + bestSolution.getFitness());
            for(Solution s : bestSolutions)
            {
                //System.out.println("\t" + s.getFitness());
                if(s.getFitness() < tmpBestSolution.getFitness())
                {
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
