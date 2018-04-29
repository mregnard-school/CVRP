package module.Solutions;

import module.Node;
import module.Path;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddPathNeighbor extends AbstractNeighbourhood {

    @Override
    protected void initialize() {
        super.initialize();
        selected.setValue(new Path());
    }

    @Override
    protected Map.Entry<Path, Path> neighbourhoodCalculation() {
        System.out.println("coucou");
        Path target = selected.getKey();

        List<Node> nodesStealable = new ArrayList<>(target.getNodes());
        List<Node> newPathNode = new ArrayList<>();
        int rd = random.nextInt(nodesStealable.size());

        Node stolen = nodesStealable.get(rd);
        newPathNode.add(stolen);

        Path newPath = new Path();
        newPath.addAllNodes(nodesStealable);
        newPath.startAndEndingPoint(target.getWarehouse());

        Path targetModified = new Path(target.getMaxCapacity());
        targetModified.addAllNodes(nodesStealable);
        targetModified.startAndEndingPoint(target.getWarehouse());

        return new AbstractMap.SimpleEntry<>(newPath, targetModified);

    }
}
