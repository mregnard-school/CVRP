package view.Objects;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import module.Algorithms.Algorithm;
import module.Solutions.Solution;

import java.util.Observable;
import java.util.Observer;

public class AlgoObserver implements Observer {
    Canvas canvas;
    double multiplier;
    double offsetX;
    double offsetY;

    public AlgoObserver(Canvas canvas, double multiplier, double offsetX, double offsetY)
    {
        this.canvas = canvas;
        this.multiplier = multiplier;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }



    @Override
    public void update(Observable o, Object arg) {
        Algorithm algo = (Algorithm)o;
        Solution currentSolution = algo.getCurrentSolution();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        System.out.println(multiplier);
        currentSolution.getPaths().stream().forEach(path -> {
            path.getNodes().stream().forEach(node -> {
                gc.fillOval(node.getPosition().getX()*multiplier - offsetX, node.getPosition().getY()*multiplier - offsetY, multiplier, multiplier);
            });

            gc.beginPath();
            path.getNodes().stream().skip(1).forEach(node -> {
                gc.lineTo(node.getPosition().getX()*multiplier - offsetX + (multiplier/2), node.getPosition().getY()*multiplier - offsetY + (multiplier/2));
            });
            gc.stroke();
            gc.closePath();
        });
    }
}
