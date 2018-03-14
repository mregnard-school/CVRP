package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import module.Algorithms.Algorithm;
import module.Algorithms.SimulatedAnnealing;
import module.NodeReader;
import view.Objects.AlgoThreadObj;
import view.Objects.AlgoObserver;

public class Controller {
    AlgoThreadObj algoThreadObj;
    Thread algoThread;
    AlgoObserver algoObserver;

    private void initializeAlgorithm(ActionEvent event, Algorithm algorithm)
    {
        algorithm.initialize(NodeReader.getNodes("data/data01.txt"));
        algoObserver = new AlgoObserver((Canvas)((ComboBox)event.getSource()).getScene().lookup("#mapCanvas"));
        algorithm.addObserver(algoObserver);
        algoThreadObj = new AlgoThreadObj(algorithm);
        algoThread = new Thread(algoThreadObj);
    }

    @FXML
    private void changeAlgorithm(ActionEvent event) {
        System.out.println("Initializing algorithm");
        initializeAlgorithm(event, new SimulatedAnnealing());
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
