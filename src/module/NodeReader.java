package module;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public abstract class NodeReader
{
    public static List<Node> getNodes(String dataPath)
    {
        List<Node> nodes = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(dataPath))) {
            stream.skip(1).forEach(line -> {
                String[] data = line.split(";");
                nodes.add(new Node(new Position(Integer.parseInt(data[1]), Integer.parseInt(data[2])), Integer.parseInt(data[3])));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nodes;
    }
}
