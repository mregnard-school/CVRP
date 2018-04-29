package module.Algorithms;

import module.Node;
import module.Path;
import module.Solutions.*;
import module.exceptions.SolutionSwapper;
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

            List<Solution> currentSolutions = new ArrayList<>();

            currentSolution.setNeighbourStrategy(new SwapNeighbor());
            Solution tmpSolution;
            int failSafe = 0;
            while(currentSolutions.size() < 100 && failSafe < 100)
            {
                tmpSolution = currentSolution.getNextValidSolutions().iterator().next();
                if(currentSolutions.add(tmpSolution))
                {
                    currentSolutions.add(tmpSolution);
                    failSafe = 0;
                }
                else
                {
                    failSafe++;
                }
            }


            /*currentSolution.setNeighbourStrategy(new SwapSameNeighbourhood());
            solutions.addAll(currentSolution.getNextValidSolutions());
            currentSolution.setNeighbourStrategy(new StealNeighbour());
            solutions.addAll(currentSolution.getNextValidSolutions());*/
            //System.out.println("Solutions: " + currentSolutions.size());
            final double highestFitness = (currentSolutions.stream().max(Comparator.comparing( Solution::getFitness )).get().getFitness()*1.5);
            double rouletteSize = currentSolutions.stream().mapToDouble(s -> (highestFitness - s.getFitness())).sum();

            //System.out.println(rouletteSize);


            List<Solution> bestSolutions = new ArrayList<>();

            failSafe = 0;
            while(bestSolutions.size() < 10 && failSafe < 100)
            {
                double randomPick = random.nextInt((int)rouletteSize);
                int fitnessSum = 0;
                tmpSolution = null;
                for(Solution s : currentSolutions)
                {
                    if(randomPick >= fitnessSum && randomPick <= fitnessSum+(highestFitness-s.getFitness()))
                    {
                        tmpSolution = s;
                        break;
                    }
                    fitnessSum+=highestFitness-s.getFitness();
                }
                if(!bestSolutions.contains(tmpSolution))
                {
                    if(tmpSolution == null)
                    {
                        
                    }
                    bestSolutions.add(tmpSolution);
                    failSafe = 0;
                }
                else
                {
                    //System.out.println("damn");
                    failSafe++;
                }
            }

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
                    System.out.println("soleOneIndex: " + solOneIndex);
                }
                SolutionSwapper sw = new SolutionSwapper(bestSolutions.get(solOneIndex), bestSolutions.get(solTwoIndex));
                sw.swap();
            }

            Solution tmpBestSolution = bestSolutions.get(0);
            for(Solution s : bestSolutions)
            {
                if(s.getFitness() < tmpBestSolution.getFitness())
                {
                    tmpBestSolution = s;
                }
            }

            bestSolution = bestSolution.getFitness() < tmpBestSolution.getFitness() ? bestSolution : tmpBestSolution;




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
        //System.out.println(steps + "<" + maxStep);
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
