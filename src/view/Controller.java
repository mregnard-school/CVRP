package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import module.Algorithms.Algorithm;
import module.Algorithms.SimulatedAnnealing;
import module.Node;
import module.NodeReader;
import module.exceptions.ComparingException;
import view.Objects.AlgoObserver;
import view.Objects.AlgoThreadObj;

import java.util.Comparator;
import java.util.List;

public class Controller {
    AlgoThreadObj algoThreadObj;
    Thread algoThread;
    AlgoObserver algoObserver;

    private void initializeAlgorithm(ActionEvent event, Algorithm algorithm, List<Node> nodes)
    {
        Canvas mapCanvas = (Canvas)((ComboBox)event.getSource()).getScene().lookup("#mapCanvas");
        mapCanvas.prefHeight(600);
        mapCanvas.prefWidth(600);
        mapCanvas.setWidth(600);
        mapCanvas.setHeight(600);

        try{
            int maxWidth = nodes.stream()
                    .max(Comparator.comparingInt(n -> n.getPosition().getX()))
                    .orElseThrow(ComparingException::new)
                    .getPosition()
                    .getX();

            int maxHeight = nodes.stream()
                    .max(Comparator.comparingInt(n -> n.getPosition().getY()))
                    .orElseThrow(ComparingException::new)
                    .getPosition()
                    .getY();

            int minWidth = nodes.stream()
                    .min(Comparator.comparingInt(n -> n.getPosition().getX()))
                    .orElseThrow(ComparingException::new)
                    .getPosition()
                    .getX();

            int minHeight = nodes.stream()
                    .min(Comparator.comparingInt(n -> n.getPosition().getY()))
                    .orElseThrow(ComparingException::new)
                    .getPosition()
                    .getY();



            System.out.println(minWidth + "<" + maxWidth);

            double widthMultiplier = (mapCanvas.getWidth()-40) / (maxWidth-minWidth);
            double heightMultiplier = (mapCanvas.getHeight()-40) / (maxHeight-minHeight);
            double smallestMultiplier = Math.min(widthMultiplier, heightMultiplier);

            algoObserver = new AlgoObserver(mapCanvas,
                    smallestMultiplier,
                    minWidth*smallestMultiplier,
                    minHeight*smallestMultiplier
            );

            algorithm.addObserver(algoObserver);
            algoThreadObj = new AlgoThreadObj(algorithm);
            algoThread = new Thread(algoThreadObj);
        } catch (ComparingException e){
            e.printStackTrace();
        }

    }

    @FXML
    private void changeAlgorithm(ActionEvent event) {
        System.out.println("Initializing algorithm");
        List<Node> nodes = NodeReader.getNodes("data/data01.txt");
        initializeAlgorithm(event, new SimulatedAnnealing(10000, 0.8, 0.01, nodes), nodes);
    }

    @FXML
    private void playButtonClick(ActionEvent event) {
        // Button was clicked, do something...
        algoThread.start();
    }

    @FXML
    private void stepButtonClick(ActionEvent event) {
        // Button was clicked, do something...
        algoThreadObj.getAlgorithm().next();
    }
    /*Platform.runLater(new Runnable() {
        @Override public void run() {
            labelConnection.setText("Connecting...");
        }
    });*/

}
