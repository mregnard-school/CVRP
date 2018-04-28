package module.Solutions;

import module.Path;
import module.utils.Helpers;
import module.utils.PathSwapper;

import java.util.*;

public class PermutationNeighbourhood implements NeighbourStrategy {

    private int firstIndex;
    private int secondIndex;
    private Random rand = Helpers.random;

    private Set<Path> paths;

    private Path first;
    private Path second;

    private Set<Solution> nextSolutions = new HashSet<>();

    @Override
    public Set<Solution> getNeighbourhood(Solution solution) {
        paths = solution.getPaths();

        selectPaths();

        PathSwapper swapper = new PathSwapper(first, second);
        Set<Map.Entry<Path, Path>> newPaths = swapper.swap();
        newPaths.forEach(entry -> createNewPaths(entry.getKey(), entry.getValue()));

        return nextSolutions;
    }

    public void selectPaths(){
        calculateIndexes();
        Iterator<Path> iterator = paths.iterator();
        int i = 0;
        Optional<Path> optionalFirst = Optional.empty();
        Optional<Path> optionalSecond = Optional.empty();

        while(iterator.hasNext()){
            Path path = iterator.next();
            if(i == firstIndex){
                optionalFirst = Optional.of(path);
            } else if (i == secondIndex){
                optionalSecond = Optional.of(path);
            }
            i++;
        }

        first = optionalFirst.orElseThrow(NoSuchElementException::new);
        second = optionalSecond.orElseThrow(NoSuchElementException::new);
    }

    public void calculateIndexes() {
        do {
            secondIndex = rand.nextInt(paths.size());
            firstIndex = rand.nextInt(paths.size());
        } while (firstIndex == secondIndex);
    }

    public void createNewPaths(Path first, Path second) {
        Set<Path> pathCopy = new HashSet<>(paths);
        pathCopy.remove(this.first);
        pathCopy.remove(this.second);
        pathCopy.add(first);
        pathCopy.add(second);
        Solution newSolution = new Solution(pathCopy, this);
        nextSolutions.add(newSolution);
    }
}
