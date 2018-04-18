package view.Objects;

import module.Algorithms.Algorithm;

public class AlgoThreadObj implements Runnable {
    private Algorithm algorithm;

    public AlgoThreadObj(Algorithm algorithm)
    {
        this.algorithm = algorithm;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted() && algorithm.hasNext())
        {
            algorithm.next();
           // System.out.println(algorithm.getBestSolution().getFitness());
        }
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void interrupt(){
        Thread.currentThread().interrupt();
    }
}
