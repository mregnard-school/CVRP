package module.Algorithms;

import module.Solutions.Solution;

public interface AlgorithmStrategy {
    Solution getBestNeighbourhood();
    void next();
}
