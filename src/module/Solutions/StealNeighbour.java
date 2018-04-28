package module.Solutions;

import module.Node;
import module.Path;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StealNeighbour extends AbstractNeighbourhood {

    @Override
    protected Map.Entry<Path, Path> neighbourhoodCalculation() {
        Path stealer = selected.getKey();
        Path stolen = selected.getValue();

        List<Node> nodesToSteal = new ArrayList<>(stolen.getNodes());

        int rd = random.nextInt(nodesToSteal.size());
        Node stoled = nodesToSteal.remove(rd);

        List<Node> nodeStolen = new ArrayList<>(stealer.getNodes());
        rd = random.nextInt(nodeStolen.size());
        nodeStolen.add(rd, stoled);

        Path first = new Path(stealer.getMaxCapacity());
        first.addAllNodes(nodeStolen);
        first.startAndEndingPoint(stealer.getWarehouse());

        Path second = new Path(stolen.getMaxCapacity());
        second.addAllNodes(nodesToSteal);
        second.startAndEndingPoint(stolen.getWarehouse());

        return new AbstractMap.SimpleEntry<>(first, second);
    }
}
