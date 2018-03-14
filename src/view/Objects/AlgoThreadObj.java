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
        System.out.println(Thread.currentThread().isInterrupted());
        while(!Thread.currentThread().isInterrupted() && algorithm.hasNext())
        {
            algorithm.next();
        }
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }
}
