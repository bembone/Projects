module anmy.sudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens anmy.sudoku to javafx.fxml;
    exports anmy.sudoku;
    exports anmy.sudoku.view;
    opens anmy.sudoku.view to javafx.fxml;
    exports anmy.sudoku.modell;
    opens anmy.sudoku.modell to javafx.fxml;


}