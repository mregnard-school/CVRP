package module.Solutions;

import module.Path;
import module.utils.PathSwapper;

import java.util.AbstractMap;
import java.util.Map;

public class SwapSameNeighbourhood extends AbstractNeighbourhood {

    Path path;
    int firstIndex;
    int secondIndex;
    int nbElementToSwap;

    @Override
    protected Map.Entry<Path, Path> neighbourhoodCalculation() {
        path = selected.getKey();
        nbElementToSwap = 1;
        selectIndexes();

        PathSwapper pathSwapper = new PathSwapper(path, path);
        Path modified = pathSwapper.swapSame(nbElementToSwap, firstIndex, secondIndex);

        return new AbstractMap.SimpleEntry<>(modified, modified);
    }


    @Override
    protected void cleanPaths(Path key, Path value) {
        cleanPath(selected.getKey(), key);
    }

    private void selectIndexes() {
        firstIndex = 1;
        secondIndex = 1;

        // TODO: 28/04/2018 Workaround (remove first and last from path before sending it)
        if (path.getNodes().size() > 2) {
            firstIndex = random.nextInt(path.getNodes().size() - nbElementToSwap - 1) + 1;
            secondIndex = random.nextInt(path.getNodes().size() - nbElementToSwap - 1) + 1;
        }
    }

}
