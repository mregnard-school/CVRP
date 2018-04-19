package view.Objects;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import module.Algorithms.Algorithm;
import module.Node;
import module.Path;
import module.Solutions.Solution;
import module.utils.Helpers;

import java.util.*;

public class AlgoObserver implements Observer {
    private Canvas canvas;
    private double multiplier; //To make all the dots fir in the biggest screen as possible
    private double offsetX;
    private double offsetY;
    private boolean showBestSolution;
    private List<Color> colors;

    public AlgoObserver(Canvas canvas, double multiplier, double offsetX, double offsetY) {
        this.canvas = canvas;
        this.multiplier = multiplier;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.showBestSolution = false;

        colors = new ArrayList<>();

        generateColors(10);
    }

    private void generateColors(int amount)
    {
        //Pool of colors for each path
        final Random random = Helpers.random;

        while(colors.size() < amount)
        {
            for (int i = colors.size(); i < amount; i++) {
                int red;
                int blue;
                int green;
                do {
                    red = random.nextInt(255);
                    blue = random.nextInt(255);
                    green = random.nextInt(255);
                } while (red == 255 && blue == 255 && green == 255);
                colors.add(Color.rgb(red, green, blue));
            }

            ListIterator<Color> itt = colors.listIterator();
            Color prevColor = itt.next();
            while(itt.hasNext())
            {
                Color currentColor = itt.next();
                double redDiff = Math.abs(currentColor.getRed() - prevColor.getRed());
                double greenDiff = Math.abs(currentColor.getGreen() - prevColor.getGreen());
                double blueDiff = Math.abs(currentColor.getBlue() - prevColor.getBlue());
                if(redDiff + greenDiff + blueDiff < 0.5)
                {
                    itt.remove();
                }
            }
        }


    }

    public void drawPathInfo(Solution solution)
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int infoOffsetY = 10;
        Iterator<Color> colorIterator = colors.iterator();
        double oldLineWidth = gc.getLineWidth();
        for(Path path : solution.getPaths())
        {
            Color c = colorIterator.next();
            gc.setFill(c);
            gc.setStroke(c);
            gc.setLineWidth(4);
            gc.strokeLine(350, infoOffsetY, 400, infoOffsetY);
            double distance = path.getDistance();
            String text = "Distance: " + (distance == -1 ? "Not possible" : (Math.round(distance*10000.0)/10000.0));
            text+= ", Charge: " + path.getCurrentCapacity() + "<" + path.getMaxCapacity();
            gc.fillText(text, 410, infoOffsetY+4);
            infoOffsetY+=20;
        }
        gc.setLineWidth(oldLineWidth);
    }

    public void drawSolution(Solution solution) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //Clearing previous canvas
        gc.clearRect(0, 0, gc.getCanvas().getHeight(), gc.getCanvas().getWidth());
        gc.setLineWidth(1);

        // If we haven't generated enough colors
        if(colors.size() < solution.getPaths().size())
        {
            generateColors(solution.getPaths().size());
        }

        Iterator<Color> colorIterator = colors.iterator();

        drawPathInfo(solution);
        solution.getPaths().forEach(path -> {
            Color c = colorIterator.next();
            gc.setFill(c);
            gc.setStroke(c);




            List<Node> nodes = new ArrayList<>();
            nodes.addAll(path.getNodes());
            // Setting the dots
            nodes.forEach(node -> {
                gc.fillOval(node.getPosition().getX() * multiplier - offsetX,
                        node.getPosition().getY() * multiplier - offsetY,
                        multiplier,
                        multiplier
                );
            });

            // Setting the lines between the dots
            // Just to have the last not from the foreach below
            gc.beginPath();
            nodes.forEach(node -> {
                gc.lineTo(node.getPosition().getX() * multiplier - offsetX + (multiplier / 2),
                        node.getPosition().getY() * multiplier - offsetY + (multiplier / 2)
                );
            });
            // Link first dot to last dot
            gc.lineTo(nodes.get(0).getPosition().getX() * multiplier - offsetX + (multiplier / 2),
                    nodes.get(0).getPosition().getY() * multiplier - offsetY + (multiplier / 2)
            );
            gc.stroke();
            gc.closePath();
        });
    }

    private void drawAlgorithmInfo(Algorithm algorithm)
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.YELLOWGREEN);
        double fitness = algorithm.getCurrentSolution().getFitness();
        gc.fillText("Current solution: " + (fitness == Double.MAX_VALUE ? "Not possible" : fitness), 20, 20);
        gc.setFill(Color.GREEN);
        gc.fillText("Best solution: " + algorithm.getBestSolution().getFitness(), 20, 40);
    }

    @Override
    public void update(Observable o, Object arg) {
        Algorithm algo = (Algorithm) o;
        Solution solutionToDraw = showBestSolution ? algo.getBestSolution() : algo.getCurrentSolution();
        drawSolution(solutionToDraw);
        drawAlgorithmInfo(algo);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isShowBestSolution() {
        return showBestSolution;
    }

    public void setShowBestSolution(boolean showBestSolution) {
        this.showBestSolution = showBestSolution;
    }
}
