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

    public boolean canAddNode()
    {
        //verify capacity+
        return true;
    }

    public boolean addNode()
    {
        //verify capacity+
        return false;
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
