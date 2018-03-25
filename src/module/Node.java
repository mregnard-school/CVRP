package module;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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

    public Node getClosest(Set<Node> nodes){
        double distance = Double.MAX_VALUE;
        Optional<Node> closestOptional = Optional.empty();
        for(Node node : nodes){
            double currentDistance = this.getPosition().getDistanceFrom(node.getPosition());
            if(currentDistance <= distance){
                distance = currentDistance;
                closestOptional = Optional.of(node);
            }
        }

        return closestOptional.orElseThrow(NoSuchElementException::new);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(this == obj){
            return true;
        }
        if(obj instanceof Node){
            Node other = (Node) obj;
            return this.getPosition().equals(other.getPosition()) && this.getCapacity() == other.getCapacity();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPosition(), this.getCapacity());
    }

    @Override
    public String toString() {
        return  "[" + position.getX() + ", " + position.getY() + ", " + capacity + "]";
    }
}
