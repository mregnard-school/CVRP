package view.Objects;

import module.Algorithms.Algorithm;

public class AlgoThreadObj implements Runnable {

    private Algorithm algorithm;
    private boolean run;

    public AlgoThreadObj(Algorithm algorithm) {
        this.algorithm = algorithm;
        run = true;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && algorithm.hasNext()) {
            while (run && !Thread.currentThread().isInterrupted()) {
                algorithm.next();
                // System.out.println(algorithm.getBestSolution().getFitness());
            }
        }
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void interrupt() {
        Thread.currentThread().interrupt();
    }

    public void toggle() {
        run = !run;
    }

    public boolean isRunning() {
        return run;
    }
}
