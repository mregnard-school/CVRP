package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import module.Algorithms.Algorithm;
import module.Algorithms.SimulatedAnnealing;
import module.Node;
import module.NodeReader;
import module.exceptions.ComparingException;
import view.Objects.AlgoObserver;
import view.Objects.AlgoThreadObj;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    private AlgoThreadObj algoThreadObj;
    private Thread algoThread;
    private AlgoObserver algoObserver;
    private boolean started;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ComboBox algoDropdown;
    @FXML
    private ComboBox datasetDropdown;
    @FXML
    private Canvas mapCanvas;
    @FXML
    private Button playButton;
    @FXML
    private Button stepButton;
    @FXML
    private Button stopButton;
    @FXML
    private TextField maxIterationsInput;

    public void initialize() {
        algoDropdown.getItems().addAll(
                "Simulated Annealing"
        );

        datasetDropdown.getItems().addAll(
                "data01.txt",
                "data02.txt",
                "data03.txt",
                "data04.txt",
                "data05.txt"
        );

        maxIterationsInput.textProperty().addListener((obs, oldText, newText) -> {
            /*Pattern p = Pattern.compile("\\d");
            Matcher m = p.matcher(newText);
            if(!m.matches())
            {
                maxIterationsInput.setText(oldText);
            }
            System.out.println("Text changed from "+oldText+" to "+newText);*/
            // ...
        });



        algoDropdown.setValue(algoDropdown.getItems().get(0));
        datasetDropdown.setValue(datasetDropdown.getItems().get(0));
        algoDropdown.fireEvent(new ActionEvent());
    }

    private void initializeAlgorithm(Algorithm algorithm, List<Node> nodes) {
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
            algoThreadObj = new AlgoThreadObj(algorithm, algoObserver);
            algoThread = new Thread(algoThreadObj);
            algoObserver.display(algorithm);
        } catch (ComparingException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void reset() {
        System.out.println("Initializing algorithm");
        algoDropdown.setDisable(false);
        datasetDropdown.setDisable(false);
        List<Node> nodes = NodeReader.getNodes("data/" + datasetDropdown.getValue());
        initializeAlgorithm(new SimulatedAnnealing(100000, nodes), nodes);
        started = false;
    }

    @FXML
    private void setMaxIterations() {
        System.out.println("Setting max iterations");
    }

    @FXML
    private void playButtonClick(ActionEvent event) {
        if(algoThread != null){
            algoDropdown.setDisable(true);
            datasetDropdown.setDisable(true);
            stepButton.setDisable(true);
            stopButton.setDisable(true);
            if(!started){
                algoThread.start();
                started = true;
                playButton.setText("Pause");
            } else {
                algoThreadObj.toggle();
                String text = algoThreadObj.isRunning() ? "Pause" : "Play";
                playButton.setText(text);
                stepButton.setDisable(false);
                stopButton.setDisable(false);
            }
        }
    }

    @FXML
    private void stepButtonClick(ActionEvent event) {
        if(algoThreadObj != null ){
            algoObserver.setDisplayNextStep(true);
            algoThreadObj.getAlgorithm().next();
        }
    }

    @FXML
    private void stopButtonClick(ActionEvent event) {
        algoDropdown.setDisable(false);
        datasetDropdown.setDisable(false);
        if(algoThreadObj != null ){
            algoDropdown.fireEvent(new ActionEvent());
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
