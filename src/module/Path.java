package module;

import java.util.LinkedHashSet;

public class Path {
    LinkedHashSet<Node> nodes;
    int maxCapacity;
    int currentCapacity;

    public Path(int maxCapacity)
    {
        this.nodes = new LinkedHashSet<>();
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
    }

    public LinkedHashSet<Node> getNodes() {
        return nodes;
    }

    public boolean canAddNode(Node node) { return currentCapacity + node.getCapacity() < maxCapacity; }

    public boolean addNode(Node node)
    {
        if(!canAddNode(node)) {return false;}
        nodes.add(node);
        currentCapacity+= node.getCapacity();
        return true;
    }

    public boolean replaceNode()
    {
        //verify capacity+
        return true;
    }

    public boolean removeNode()
    {
        //verify capacity+
        return true;
    }
}
