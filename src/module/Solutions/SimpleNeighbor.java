package module.Solutions;

import module.Path;

import java.util.Map;

public class SimpleNeighbor extends AbstractNeighbourhood {

    private Path first;
    private Path second;
    private int nbElementToSwap;
    int firstIndex;
    int secondIndex;

    @Override
    protected Map.Entry<Path, Path> neighbourhoodCalculation() {
        first = selected.getKey();
        second = selected.getValue();

        randomNbElementToSwap();
        selectIndexes();

        return swapper.swap(nbElementToSwap, firstIndex, secondIndex);
    }

    private void randomNbElementToSwap() {
        int size = Math.min(first.getNodes().size(), second.getNodes().size());
        nbElementToSwap = random.nextInt(size - 1);
        nbElementToSwap = 1;
        // FIXME: 25/04/2018 Irindul : Nodes are deleted when nbElementToSwap > 2
    }

    private void selectIndexes() {
        firstIndex = 1;
        secondIndex = 1;

        // TODO: 28/04/2018 Workaround (remove first and last from path before sending it)
        if (first.getNodes().size() > 2 && second.getNodes().size() > 2) {
            firstIndex = random.nextInt(first.getNodes().size() - nbElementToSwap - 1) + 1;
            secondIndex = random.nextInt(second.getNodes().size() - nbElementToSwap - 1) + 1;
        }
    }
}
