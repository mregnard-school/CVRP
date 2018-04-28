package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    private AlgoThreadObj algoThreadObj;
    private Thread algoThread;
    private AlgoObserver algoObserver;
    private boolean started;

    @FXML
    private Button playButton;


    private void initializeAlgorithm(ActionEvent event, Algorithm algorithm, List<Node> nodes) {
        Canvas mapCanvas = (Canvas)((ComboBox)event.getSource()).getScene().lookup("#mapCanvas");
        mapCanvas.prefHeight(750);
        mapCanvas.prefWidth(750);
        mapCanvas.setWidth(750);
        mapCanvas.setHeight(750);

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

            double widthMultiplier = (mapCanvas.getWidth()-40) / (maxWidth-minWidth);
            double heightMultiplier = (mapCanvas.getHeight()-140) / (maxHeight-minHeight);
            double smallestMultiplier = Math.min(widthMultiplier, heightMultiplier);

            algoObserver = new AlgoObserver(mapCanvas,
                    smallestMultiplier,
                    minWidth*smallestMultiplier,
                    minHeight*smallestMultiplier-100
            );

            algorithm.addObserver(algoObserver);
            algoThreadObj = new AlgoThreadObj(algorithm);
            algoThread = new Thread(algoThreadObj);
            algoObserver.display(algorithm);
        } catch (ComparingException e){
            e.printStackTrace();
        }

    }

    @FXML
    private void changeAlgorithm(ActionEvent event) {
        System.out.println("Initializing algorithm");
        List<Node> nodes = NodeReader.getNodes("data/data01.txt");
        initializeAlgorithm(event, new SimulatedAnnealing(100000, nodes), nodes);
    }

    @FXML
    private void playButtonClick(ActionEvent event) {
        if(algoThread != null){
            if(!started){
                algoThread.start();
                started = true;
                playButton.setText("Pause");
            } else {
                algoThreadObj.toggle();
                String text = algoThreadObj.isRunning() ? "Pause" : "Play";
                playButton.setText(text);
            }
        }
    }

    @FXML
    private void stepButtonClick(ActionEvent event) {
        if(algoThreadObj != null ){
            algoThreadObj.getAlgorithm().next();
        }
    }

    @FXML
    private void showBestSolutionClick(ActionEvent event) {
        algoObserver.setShowBestSolution(((CheckBox)event.getSource()).isSelected());
        if (!algoThreadObj.isRunning()) {
           algoObserver.display(algoThreadObj.getAlgorithm());
        }
    }

    public void interrupt() {
        if(algoThreadObj != null) {
            algoThreadObj.interrupt();
        }
        if(algoThread != null) {
            algoThread.interrupt();
        }
    }

}
