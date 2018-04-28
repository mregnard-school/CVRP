package module.Solutions;

import module.Path;
import module.utils.PathSwapper;

import java.util.AbstractMap;
import java.util.Map;

public class SwapSameNeighbourhood extends AbstractNeighbourhood {

    Path path;
    int nbElementToSwap;

    @Override
    protected Map.Entry<Path, Path> neighbourhoodCalculation() {
        path = selected.getKey();
        nbElementToSwap = 1;

        Map.Entry<Integer, Integer> indexes = selectIndexes(path, path, nbElementToSwap);

        PathSwapper pathSwapper = new PathSwapper(path, path);
        Path modified = pathSwapper.swapSame(nbElementToSwap, indexes.getKey(), indexes.getValue());

        return new AbstractMap.SimpleEntry<>(modified, modified);
    }

    @Override
    protected void cleanPaths(Path key, Path value) {
        cleanPath(selected.getKey(), key);
    }

}
