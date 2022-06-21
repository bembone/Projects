package anmy.sudoku;

import anmy.sudoku.view.SudokuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
        //huvudklass h1 = new huvudklass();
        //System.out.println(h1.toString());

    }

    @Override
    public void start(Stage stage) throws Exception {

        SudokuView view = new SudokuView();
        MenuBar menuBar = view.getMenuBar();
        VBox root = new VBox(menuBar,view);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);

        stage.setTitle("Sudoku");
        stage.show();

    }
}