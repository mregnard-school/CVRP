package module.Solutions;

import module.Path;
import module.utils.Helpers;
import module.utils.PathSwapper;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class SimpleNeighbor implements NeighbourStrategy{

    Set<Path> paths;
    Set<Solution> neigbhors;
    Solution solution;
    Random rand = Helpers.random;

    Path first;
    Path second;

    @Override
    public Set<Solution> getNeighbourhood(Solution solution) {

        do {
            neigbhors = new HashSet<>();
            paths = solution.getPaths();
            first = selectRandomPath();
            second = selectRandomPath();

            PathSwapper swapper = new PathSwapper(first, second);

            int nbElementToSwap = randomNbElementToSwap();
            nbElementToSwap = 1;
            // FIXME: 25/04/2018 Irindul : Nodes are deleted when nbElementToSwap > 2

            int firstIndex = rand.nextInt(first.getNodes().size() - nbElementToSwap);
            int secondIndex = rand.nextInt(second.getNodes().size() - nbElementToSwap);

            if(first.equals(second)) {
               Path path =  swapper.swapSame(nbElementToSwap, firstIndex, secondIndex);
               /*System.out.println(path);
               System.out.println(first);
               System.out.println();*/
               replacePath(path);
            } else {
                Map.Entry<Path, Path> swapped  = swapper.swap(nbElementToSwap, firstIndex, secondIndex);
                createNewPaths(swapped.getKey(), swapped.getValue());
            }
        } while (!this.solution.isValid());

        neigbhors.add(this.solution);
        return neigbhors;
    }

    private void replacePath(Path path) {
        Set<Path> pathCopy = new HashSet<>(paths);

        pathCopy.remove(first);
        pathCopy.add(path);

        solution = new Solution(pathCopy, this);

    }


    private int randomNbElementToSwap() {
        int size = Math.min(first.getNodes().size(), second.getNodes().size());
        return rand.nextInt(size -1);
    }

    public Path selectRandomPath() {
        int rdIndex = rand.nextInt(paths.size());

        Path selected = paths.stream().skip(rdIndex)
                .findFirst().orElseThrow(IndexOutOfBoundsException::new);

        return selected;
    }

    public void createNewPaths(Path first, Path second) {
        Set<Path> pathCopy = new HashSet<>(paths);
        pathCopy.remove(this.first);
        pathCopy.remove(this.second);
        pathCopy.add(first);
        pathCopy.add(second);
        solution = new Solution(pathCopy, this);
    }
}
