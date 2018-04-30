package view;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import module.Algorithms.Algorithm;
import module.Algorithms.SimulatedAnnealing;
import module.Algorithms.GeneticAlgorithm;
import module.Node;
import module.NodeReader;
import module.exceptions.ComparingException;
import module.utils.Helpers;
import view.Objects.AlgoObserver;
import view.Objects.AlgoThreadObj;
import view.Objects.NumberTextField;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
    private VBox detailedSettings;
    @FXML
    private TextField maxIterationsInput;

    private List<VBox> settingsFields;

    public void initialize() {
        algoDropdown.getItems().addAll(
                Helpers.algorithms.keySet()
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
        //mapCanvas.prefHeight(750);
        ///mapCanvas.prefWidth(750);
        ///mapCanvas.setWidth(750);
        //mapCanvas.setHeight(750);

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

        if(algoDropdown.getValue().equals("Simulated Annealing"))
        {
            initializeAlgorithm(new SimulatedAnnealing(1000000, nodes), nodes);

        }
        else if(algoDropdown.getValue().equals("Genetic Algorithm"))
        {
            initializeAlgorithm(new GeneticAlgorithm(10000000, nodes), nodes);
        }

        detailedSettings.getChildren().clear();

        settingsFields = new ArrayList<>();
        ObservableList<NumberTextField> oList = FXCollections.observableArrayList(tf -> new Observable[]{tf.textProperty()});
        oList.addListener((ListChangeListener.Change<? extends NumberTextField> c) -> {
            while (c.next()) {
                if (c.wasUpdated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i) {
                        Helpers.makeChangeToAlgorithm(algoThreadObj.getAlgorithm(), c.getList().get(i).label, c.getList().get(i).getText());
                        //System.out.println("Updated index: " + i + ", new value: " + c.getList().get(i).getText());
                    }
                }
            }
        });
        for(Map.Entry<String,String> setting : Helpers.algoSettings.get(algoDropdown.getValue()).entrySet())
        {
            VBox vbox = new VBox();
                Label label = new Label(setting.getKey());
                vbox.getChildren().add(label);
                NumberTextField value = new NumberTextField(setting.getValue());
                    value.label = setting.getKey();
                    oList.add(value);
                vbox.getChildren().add(value);
            detailedSettings.getChildren().add(vbox);
            settingsFields.add(vbox);
        }


        started = false;
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
