package view.Objects;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import module.Algorithms.Algorithm;
import module.Solutions.Solution;
import module.utils.Helpers;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class AlgoObserver implements Observer {
    private Canvas canvas;
    private double multiplier; //To make all the dots fir in the biggest screen as possible
    private double offsetX;
    private double offsetY;

    public AlgoObserver(Canvas canvas, double multiplier, double offsetX, double offsetY)
    {
        this.canvas = canvas;
        this.multiplier = multiplier;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }


    public void displaySolution(Solution solution){
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //Clearing previous canvas
        gc.clearRect(0, 0, gc.getCanvas().getHeight(), gc.getCanvas().getWidth());
        gc.setLineWidth(1);

        final LinkedList<Color> colors = new LinkedList<>();
        final Random random = Helpers.random;
        //Pool of colors for each path
        int nbPaths = solution.getPaths().size();
        for (int i = 0; i < nbPaths; i++) {
            final int red = random.nextInt(255);
            final int blue = random.nextInt(255);
            final int green = random.nextInt(255);
            colors.add(Color.rgb(red, green, blue));
        }

        solution.getPaths().forEach(path -> {
            Color c = colors.poll();
            gc.setFill(c);
            gc.setStroke(c);
            // Setting the dots
            path.getNodes().forEach(node -> {
                gc.fillOval(node.getPosition().getX()*multiplier - offsetX,
                        node.getPosition().getY()*multiplier - offsetY,
                        multiplier,
                        multiplier
                );
            });

            // Setting the lines between the dots
            gc.beginPath();
            path.getNodes().stream().forEach(node -> {
                gc.lineTo(node.getPosition().getX()*multiplier - offsetX + (multiplier/2),
                        node.getPosition().getY()*multiplier - offsetY + (multiplier/2)
                );
            });
            gc.stroke();
            gc.closePath();
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        Algorithm algo = (Algorithm)o;
        Solution currentSolution = algo.getCurrentSolution();

        displaySolution(currentSolution);
    }
}
