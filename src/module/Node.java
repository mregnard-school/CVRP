package module;

public class Node {
    Position position;
    private int capacity;

    public Node(Position position, int capacity) {
        this.position = position;
        this.capacity = capacity;
    }


    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
