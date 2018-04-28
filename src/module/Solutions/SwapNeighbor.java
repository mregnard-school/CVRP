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
        int size = Math.min(first.getTrimmed().size(), second.getTrimmed().size());
        nbElementToSwap = random.nextInt(size);
        nbElementToSwap = 1;
        // TODO: 28/04/2018 Converge mieux avec 1
    }
}
