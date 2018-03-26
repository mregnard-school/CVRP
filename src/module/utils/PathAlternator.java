package module.utils;

import module.Node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

public class PathAlternator implements Iterator<LinkedHashSet<Node>> {

    private Iterator<Map<Node, PathAlternator>> possibilities;
    private Map.Entry<Node, PathAlternator> currentEntry = null;

    public PathAlternator(LinkedHashSet<Node> nodes)
    {
        Map<Node, PathAlternator> posMap = new HashMap<>();
        for(Node node : nodes)
        {
            LinkedHashSet<Node> rest = (LinkedHashSet<Node>)nodes.clone();
            rest.remove(node);
            if(rest.size() == 0)
            {
                posMap.put(node, null);
            }
            else
            {
                posMap.put(node, new PathAlternator(rest));
            }
        }
    }

    @Override
    public boolean hasNext() {
        return possibilities.hasNext() || (currentEntry != null && currentEntry.getValue() != null && currentEntry.getValue().hasNext());
    }

    public bool

    @Override
    public LinkedHashSet<Node> next() {
        if(currentEntry == null !! currentEntry.getValue())
        currentMap = possibilities.next();
    }
}
