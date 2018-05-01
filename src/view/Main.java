package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        primaryStage.setTitle("Annealing Algo");
        primaryStage.setScene(new Scene(root, 940, 768));

        String css = this.getClass().getResource("../view/style.css").toExternalForm();
        primaryStage.getScene().getStylesheets().add(css);

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        controller.interrupt();
    }

    public static void main(String[] args) {

        launch(args);
    }
}

