package module.Solutions;

import module.Path;

import java.util.Map;

public class SwapNeighbor extends AbstractNeighbourhood {

    private Path first;
    private Path second;
    private int nbElementToSwap;

    @Override
    protected Map.Entry<Path, Path> neighbourhoodCalculation() {
        first = selected.getKey();
        second = selected.getValue();

        randomNbElementToSwap();
        Map.Entry<Integer, Integer> indexes = selectIndexes(first, second, nbElementToSwap);
        return swapper.swap(nbElementToSwap, indexes.getKey(), indexes.getValue());
    }

    private void randomNbElementToSwap() {
        int rd = random.nextInt(3);
        if (rd < 2) {
            nbElementToSwap = 1;
        } else {
            int size = Math.min(first.getNodes().size(), second.getNodes().size());
            nbElementToSwap = random.nextInt(size);
        }
    }
}
