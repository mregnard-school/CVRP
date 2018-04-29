package view.Objects;

import module.Algorithms.Algorithm;

public class AlgoThreadObj implements Runnable {

    private Algorithm algorithm;
    private AlgoObserver algoObserver;
    private boolean run;

    public AlgoThreadObj(Algorithm algorithm, AlgoObserver algoObserver) {
        this.algorithm = algorithm;
        this.algoObserver = algoObserver;
        run = true;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && algorithm.hasNext()) {
            while (run && !Thread.currentThread().isInterrupted()  && algorithm.hasNext() ) {
                algorithm.next();
            }
        }
        algorithm.next();
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void interrupt() {
        Thread.currentThread().interrupt();
    }

    public void toggle() {
        algoObserver.setDisplayNextStep(true);
        run = !run;
    }

    public boolean isRunning() {
        return run;
    }
}
