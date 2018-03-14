package view.Objects;

import javafx.scene.canvas.Canvas;
import module.Algorithms.Algorithm;
import module.Solutions.Solution;

import java.util.Observable;
import java.util.Observer;

public class AlgoObserver implements Observer {
    Canvas canvas;

    public AlgoObserver(Canvas canvas)
    {
        this.canvas = canvas;
    }



    @Override
    public void update(Observable o, Object arg) {
        Algorithm algo = (Algorithm)o;
        Solution currentSolution = algo.getCurrentSolution();
        //currentSolution.
        //System.out.println("hej");
    }
}
