package module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class NodeReader {
    public static List<Node> getNodes(String dataPath) {
        final List<Node> nodes = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(dataPath))) {
            stream.skip(1).forEach(line -> {
                String[] data = line.split(";");
                Position pos = new Position(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
                nodes.add(new Node(pos, Integer.parseInt(data[3])));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nodes;
    }
}
