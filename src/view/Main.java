package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Annealing Algo");
        primaryStage.setScene(new Scene(root, 1024, 768));

        String css = this.getClass().getResource("../view/button.css").toExternalForm();
        primaryStage.getScene().getStylesheets().add(css);

        primaryStage.show();
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

        second.addNode(n4);
        second.addNode(n5);
        second.addNode(n6);

        System.out.println(first);
        System.out.println(second);

        System.out.println("Swapping :");

        Set<Map.Entry<Path, Path>> paths = PathSwapper.swapPath(first, second);
        paths.forEach(entry -> {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            System.out.println();
        });*/
    }
}

