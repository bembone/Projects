package anmy.sudoku.view;

import anmy.sudoku.modell.SudokuUtilities;
import anmy.sudoku.modell.SudokuBoard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Optional;

public class SudokuView extends BorderPane {

    private Label[][] numberTiles; // the tiles/squares to show in the ui grid
    private TilePane numberPane;
    private Controller controller;
    private char buttonInput;
    private SudokuBoard model;
    private MenuBar menuBar;

    /**
     * Constructor initialises the board, menubar,  all tiles, the tiles, panes, buttons,
     * and sets input to null.
     */
    public SudokuView() {
        this.model = new SudokuBoard(SudokuUtilities.SudokuLevel.EASY);
        buttonInput='0';
        numberTiles = new Label[SudokuUtilities.GRID_SIZE][SudokuUtilities.GRID_SIZE];
        initNumberTiles();
        numberPane = makeNumberPane();
        controller = new Controller(model, this);

        this.setCenter(numberPane);
        this.setLeft(leftSide());
        this.setRight(rightSide());

        createMenuBar();

    }

    /**
     * Creates a vertical box with the buttons "check" and "hint"
     * @return The vertical box
     */
    private VBox leftSide(){
        VBox v1 = new VBox();
        
        Button check = new Button("Check");
        Button hint = new Button("Hint");
        v1.setAlignment(Pos.CENTER);
        v1.getChildren().add(check);
        v1.getChildren().add(hint);
        v1.setPadding(new Insets(10));
        v1.setSpacing(10);
        this.setLeft(v1);
        hint.addEventHandler(ActionEvent.ACTION, HintHandler);
        check.addEventHandler(ActionEvent.ACTION, CheckGameHandler);
        return v1;
    }

    /**
     * Creates a vertical box with the buttons "1-9" and "C"
     * @return The vertical box
     */
    private VBox rightSide(){
        VBox v2 = new VBox();

        v2.setAlignment(Pos.CENTER);
        Button one = new Button("1");
        one.addEventHandler(ActionEvent.ACTION, buttonHandler);
        Button two = new Button("2");
        two.addEventHandler(ActionEvent.ACTION, buttonHandler);
        Button three = new Button("3");
        three.addEventHandler(ActionEvent.ACTION, buttonHandler);
        Button four = new Button("4");
        four.addEventHandler(ActionEvent.ACTION, buttonHandler);
        Button five = new Button("5");
        five.addEventHandler(ActionEvent.ACTION, buttonHandler);
        Button six = new Button("6");
        six.addEventHandler(ActionEvent.ACTION, buttonHandler);
        Button seven = new Button("7");
        seven.addEventHandler(ActionEvent.ACTION, buttonHandler);
        Button eight = new Button("8");
        eight.addEventHandler(ActionEvent.ACTION, buttonHandler);
        Button nine = new Button("9");
        nine.addEventHandler(ActionEvent.ACTION, buttonHandler);
        Button clear = new Button("C");
        clear.addEventHandler(ActionEvent.ACTION, buttonHandler);
        v2.getChildren().addAll(one,two,three,four,five,six,seven,eight,nine,clear);
        v2.setPadding(new Insets(10));
        v2.setSpacing(1);

        return v2;
    }

    /**
     * creates the view aspect of the tiles, their height and appearance.
     */
    // called by constructor (only)
    private final void initNumberTiles() {
        Font font = Font.font("Monospaced", FontWeight.NORMAL, 20);

        for (int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuUtilities.GRID_SIZE; col++) {
                Label tile = new Label("");
                if (!model.getGameBoardByPos(row,col).isHidden())
                    tile = new Label(Integer.toString(model.getCurrentValue(row,col)));
                tile.setPrefWidth(32);
                tile.setPrefHeight(32);
                tile.setFont(font);
                tile.setAlignment(Pos.CENTER);
                tile.setStyle("-fx-border-color: black; -fx-border-width: 0.5px;"); // css style
                tile.setOnMouseClicked(tileClickHandler); // add your custom event handler

                // add new tile to grid
                numberTiles[row][col] = tile;

            }
        }
    }

    /**
     * Creates the tiles that show on the border pane
     * @return The TilePane
     */
    private final TilePane makeNumberPane() {
        // create the root tile pane
        TilePane root = new TilePane();
        root.setPrefColumns(SudokuUtilities.SECTIONS_PER_ROW);
        root.setPrefRows(SudokuUtilities.SECTIONS_PER_ROW);
        root.setStyle(
                "-fx-border-color: black; -fx-border-width: 1.0px; -fx-background-color: white;");

        // create the 3*3 sections and add the number tiles
        TilePane[][] sections = new TilePane[SudokuUtilities.SECTIONS_PER_ROW][SudokuUtilities.SECTIONS_PER_ROW];
        int i = 0;
        for (int srow = 0; srow < SudokuUtilities.SECTIONS_PER_ROW; srow++) {
            for (int scol = 0; scol < SudokuUtilities.SECTIONS_PER_ROW; scol++) {
                TilePane section = new TilePane();
                section.setPrefColumns(SudokuUtilities.SECTION_SIZE);
                section.setPrefRows(SudokuUtilities.SECTION_SIZE);
                section.setStyle( "-fx-border-color: black; -fx-border-width: 0.5px;");

                // add number tiles to this section
                for (int row = 0; row < SudokuUtilities.SECTION_SIZE; row++) {
                    for (int col = 0; col < SudokuUtilities.SECTION_SIZE; col++) {
                        // calculate which tile and add
                        section.getChildren().add(
                                numberTiles[srow * SudokuUtilities.SECTION_SIZE + row][scol * SudokuUtilities.SECTION_SIZE + col]);
                    }
                }

                // add the section to the root tile pane
                root.getChildren().add(section);
            }
        }

        return root;
    }

    /**
     * Clears the tile on row, cols
     * @param row row of board
     * @param col col of board
     */
    public void clearTile(int row,int col){
        numberTiles[row][col].setText("");
    }

    /**
     * Updates the tile in the view
     * @param row The row
     * @param col The col
     */
    public void updateTile(int row,int col){
        clearTile(row,col);
        if (model.getCurrentValue(row,col)==0){
            numberTiles[row][col].setText("");
        }else {
            numberTiles[row][col].setText(Integer.toString(model.getCurrentValue(row,col)));
        }
    }

    /**
     * Updates all the tiles
     */
    public void updateBoard(){
        for(int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuUtilities.GRID_SIZE; col++) {
                updateTile(row,col);
            }
        }
    }

    /**
     * Checks if the board is full (all tiles have a value) then checks if the board is solved
     * and alerts the user if his solution is the correct one or not
     */
    public void checkIfBoardFilled(){
        int count=0;
        int full = SudokuUtilities.GRID_SIZE*SudokuUtilities.GRID_SIZE;
        for(int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuUtilities.GRID_SIZE; col++) {
                if (model.getCurrentValue(row,col)!=0)
                    count++;
            }
        }
        if (count==full){
            if (model.checkSolvedGame()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("All tiles are correct, puzzle is solved!");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Not solved correctly!");
                alert.showAndWait();
            }
        }
    }

    /**
     * Returns the number of tiles that are currently placed
     * @return The number of tiles
     */
    public int getNumberOfTilesPlaced(){
        int count=0;
        for(int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuUtilities.GRID_SIZE; col++) {
                if (model.getGameBoardByPos(row,col).isHidden()){
                    if (model.getGameBoardByPos(row,col).getCurrentValue()!=0)
                        count++;
                }
            }
        }
        return count;
    }

    /**
     * Sets a value to a tile depending on which button was pressed previously
     */
    private EventHandler<MouseEvent> tileClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            for(int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
                for(int cols = 0; cols < SudokuUtilities.GRID_SIZE; cols++) {
                    if(event.getSource() == numberTiles[row][cols]) {
                        // we got the row and column - now call the appropriate controller method
                        controller.handleMouseEvent(buttonInput,row,cols);
                        checkIfBoardFilled();

                        return;
                    }
                }
            }
        }
    };
    /**
     * gets the source of where the button was pressed
     */
    private EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            buttonInput=controller.handleButtonPress(actionEvent.getSource());


        }
    };

    /**
     * Exits the program
     */
    private EventHandler<ActionEvent> ExitHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            System.exit(0);

        }
    };

    /**
     * Creates a new game with the same difficulty and updates the board
     */
    private EventHandler<ActionEvent> RestartHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            model = new SudokuBoard(model.getDifficulty());
            controller.handleRestartGame(model);
            updateBoard();


        }

    };

    /**
     * Gives the user the opportunity to choose which difficulty to create the next game
     * and updates the board
     */
    private  EventHandler<ActionEvent> LevelHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            ButtonType Easy = new ButtonType("Easy");
            ButtonType Medium = new ButtonType("Medium");
            ButtonType Hard = new ButtonType("Hard");
            alert.getButtonTypes().setAll(Easy,Medium,Hard);

            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Choose the difficulty");
            Optional<ButtonType> choice = alert.showAndWait();
            if (choice.get() == Easy){
                model = new SudokuBoard(SudokuUtilities.SudokuLevel.EASY);
                controller.handleRestartGame(model);
                updateBoard();
            } else if (choice.get() == Medium){
                model = new SudokuBoard(SudokuUtilities.SudokuLevel.MEDIUM);
                controller.handleRestartGame(model);
                updateBoard();
            } else if (choice.get() == Hard){
                model = new SudokuBoard(SudokuUtilities.SudokuLevel.HARD);
                controller.handleRestartGame(model);
                updateBoard();
            }
        }
    };

    /**
     * Clears the values that have been palced on the board
     * then updates the board
     */
    private EventHandler<ActionEvent> ClearHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            controller.handleClearGame();
            updateBoard();
        }
    };

    /**
     * When the "check" button is pressed, alert user if all currently placed
     * tiles are correctly placed or not
     */
    private EventHandler<ActionEvent> CheckGameHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (controller.handleCheckGame()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("All tiles are currently in their right position!");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("One or more tiles are incorrect!");
                alert.showAndWait();
            }
        }
    };
    /**
     * Show brief rules of the game in the form of an alert.
     */
    private EventHandler<ActionEvent> RulesHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Sudoku Rules");
            alert.setContentText("Every square has to contain a single number\n" +
                    "Only the numbers from 1 through to 9 can be used\n" +
                    "Each 3×3 box can only contain each number from 1 to 9 once\n" +
                    "Each vertical column can only contain each number from 1 to 9 once\n" +
                    "Each horizontal row can only contain each number from 1 to 9 once\n\n\n" +
                    "https://masteringsudoku.com/sudoku-rules-beginners/");
            alert.showAndWait();
        }
    };

    /**
     * Generates a hint on the board by showing a correct value of a tile with no value yet
     * then updates the board and checks if its filled
     */
    private EventHandler<ActionEvent> HintHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            controller.handleHint();
            updateBoard();
            checkIfBoardFilled();
        }
    };
    /**
     * Saves the current data on the board in the form of a .sudoku file in a location of your choosing
     */
    private EventHandler<ActionEvent> SaveHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            controller.handleSaveGame();
        }
    };
    /**
     * Opens a file chooser so user can open any saved gamestate.
     */
    private EventHandler<ActionEvent> LoadHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            model = controller.handleLoadGame();
            updateBoard();

        }
    };

    /**
     * Creates the menu bar and all its contents
     */
    public void createMenuBar(){
        Menu fileMenu = new Menu("File");
        MenuItem loadGameItem = new MenuItem("Load Game");
        loadGameItem.addEventHandler(ActionEvent.ACTION,LoadHandler);
        MenuItem saveGameItem = new MenuItem("Save Game");
        saveGameItem.addEventHandler(ActionEvent.ACTION,SaveHandler);

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addEventHandler(ActionEvent.ACTION, ExitHandler);
        fileMenu.getItems().addAll(loadGameItem,saveGameItem,exitItem);

        Menu GameMenu = new Menu("Game");
        MenuItem RestartGame = new MenuItem("Restart");
        RestartGame.addEventHandler(ActionEvent.ACTION, RestartHandler);
        MenuItem GameLevel = new MenuItem("Choose Level");
        GameLevel.addEventHandler(ActionEvent.ACTION, LevelHandler);
        GameMenu.getItems().addAll(RestartGame,GameLevel);

        Menu HelpMenu = new Menu("Help");
        MenuItem clear = new MenuItem("Clear");
        clear.addEventHandler(ActionEvent.ACTION, ClearHandler);
        MenuItem checkGame = new MenuItem("Check game");
        checkGame.addEventHandler(ActionEvent.ACTION,CheckGameHandler);
        MenuItem GamesRules = new MenuItem("Rules");
        GamesRules.addEventHandler(ActionEvent.ACTION,RulesHandler);
        HelpMenu.getItems().addAll(clear,checkGame,GamesRules);


        menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,GameMenu,HelpMenu);
    }

    /**
     * Returns the menu bar
     * @return The menubar
     */
    public MenuBar getMenuBar(){
        return this.menuBar;
    }


}
