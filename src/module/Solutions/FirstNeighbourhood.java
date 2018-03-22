package module.Solutions;

import module.Path;
import module.utils.Helpers;
import module.utils.PathSwapper;

import java.util.*;

public class FirstNeighbourhood implements NeighbourStrategy {

    @Override
    public Set<Solution> getNeighbourhood(Solution solution) {
        Random rand = Helpers.random;
        Set<Path> paths = solution.getPaths();
        int firstIndex;
        int secondIndex;
        do {
            secondIndex = rand.nextInt(paths.size());
            firstIndex = rand.nextInt(paths.size());
        } while (firstIndex == secondIndex);

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

        Path first = optionalFirst.orElseThrow(NoSuchElementException::new);
        Path second = optionalSecond.orElseThrow(NoSuchElementException::new);

        Set<Map.Entry<Path, Path>> newPaths = PathSwapper.swapPath(first, second);
        Set<Solution> nextSolutions = new HashSet<>();
        newPaths.forEach(entry -> {
            Set<Path> pathCopy = new HashSet<>(paths);
            pathCopy.remove(first);
            pathCopy.remove(second);
            pathCopy.add(entry.getKey());
            pathCopy.add(entry.getValue());
            Solution newSolution = new Solution(pathCopy, this);
            nextSolutions.add(newSolution);
        });

        return nextSolutions;
    }
}
