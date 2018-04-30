package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader =new FXMLLoader(getClass().getResource("sample.fxml"));
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
      /*  Path first = new Path();
        Path second = new Path();

        Node n1 = new Node(new Position(0, 0), 0);
        Node n2 = new Node(new Position(0, 1), 1);
        Node n3 = new Node(new Position(1, 0), 2);
        Node n4 = new Node(new Position(1, 1), 3);
        Node n5 = new Node(new Position(2, 1), 4);
        Node n6 = new Node(new Position(2, 2), 5);

        first.addNode(n1);
        first.addNode(n2);
        first.addNode(n3);

        second.addNode(n1);
        second.addNode(n4);
        second.addNode(n5);
        second.addNode(n6);

        System.out.println(first);
        System.out.println(second);
        System.out.println();

        SolutionSwapper swapper = new SolutionSwapper(first, second);
        swapper.swap();*/
    }
}

