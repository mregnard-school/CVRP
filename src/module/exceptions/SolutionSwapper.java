package module.exceptions;

import module.Node;
import module.Path;
import module.Solutions.Solution;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SolutionSwapper {
    private Solution first;
    private Solution second;

    public SolutionSwapper(Solution first, Solution second)
    {
        this.first = first;
        this.second = second;
    }

    public boolean swap()
    {
        List<Path> firstWorst = first.getPaths().stream().sorted(Comparator.comparing(Path::getDistance)).collect(Collectors.toList());
        List<Path> secondBest = second.getPaths().stream().sorted(Comparator.comparing(Path::getDistance)).collect(Collectors.toList());

        int index;
        for(Path firstP : firstWorst)
        {
            int cut = firstP.getNodes().size()/2;
            while(cut >= 2)
            {
                int offset = 0;
                while(firstP.getNodes().size()>offset+cut)
                {
                    for(Path secondP : secondBest)
                    {
                        index = secondP.indexOfPath(firstP.getNodes().subList(offset, offset+cut));
                        if(index != -1)
                        {
                            //System.out.println("I just found something");
                            return true;
                        }
                    }
                    offset++;
                }
                cut--;
            }

        }
        return false;




    }

    private void printPath(Path p)
    {
        for(Node n : p.getNodes())
        {
            System.out.println("(" + n.getPosition().getX() + ")");
        }
    }

    public Solution getFirst() {
        return first;
    }

    public Solution getSecond() {
        return second;
    }
}
