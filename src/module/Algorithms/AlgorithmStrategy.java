package module.Algorithms;

import module.Node;
import module.Solutions.Solution;

import java.util.List;

public interface AlgorithmStrategy {
    Solution getBestNeighbourhood();

    void next();

    boolean hasNext();

    void initialize(List<Node> nodes);
}
