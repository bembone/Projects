package anmy.sudoku.view;

import anmy.sudoku.modell.Filhantering;
import anmy.sudoku.modell.SudokuUtilities;
import anmy.sudoku.modell.SudokuBoard;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class Controller {

    private SudokuBoard board;
    private SudokuView view;

    /**
     * Constructs a new controller with the specified SudokuBoard and SudokuView
     * @param board The SudokuBoard
     * @param view The SudokuView
     */
    public Controller(SudokuBoard board, SudokuView view) {
        this.board = board;
        this.view = view;
    }

    /**
     * Method to place a tile in a position in matrix whenever appropriate mouse actions are used.
     * @param button respresents the value of the tile clicked in form of a char. Made to int by taking - '0'
     * @param x the x coordinate in matrix
     * @param y the y coordinate in matrix
     */
    void handleMouseEvent(char button,int x, int y){
        if (button=='C') {
            board.removeCurrentValue(x, y);
            view.updateTile(x,y);
        }else if (board.getCurrentValue(x,y)==0){
            board.handleButtonToTile(button, x, y);
            view.updateTile(x,y);
        }
    }

    /**
     * Takes the string data from a button and converts it to a char value
     * based on the buttons predefined text value
     * @param button The button pressed
     * @return char value of the button pressed
     */
    char handleButtonPress(Object button){
        return button.toString().charAt(button.toString().length()-2);

    }

    /**
     * Clears all tiles that have been placed by user.
     */
    void handleClearGame(){
        for(int row=0;row< SudokuUtilities.GRID_SIZE;row++){
            for (int col=0;col< SudokuUtilities.GRID_SIZE;col++)
                board.removeCurrentValue(row,col);
        }
    }

    /**
     *
     * @return method returns true if all tiles placed are in correct place
     */
    boolean handleCheckGame(){
        if (board.handleCheckGame()==view.getNumberOfTilesPlaced())
            return true;
        else
            return false;
    }

    /**
     * Program randomly chooses a tile that is empty and places the correct tile in that index.
     * @throws IllegalArgumentException if user presses hint button after puzzle is solved
     */
    void handleHint() throws IllegalArgumentException{

        int count=0;
        for(int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuUtilities.GRID_SIZE; col++) {
                if (board.getCurrentValue(row,col)!=0)
                    count++;
            }
        }

        if (count==81)
            throw new IllegalArgumentException("Board is already solved");

        int hintRow = 0;
        int hintCol = 0;
        int theHint = 0;
        while(theHint==0) {
            int[] hintCords = board.handleHint();
            hintRow = hintCords[0];
            hintCol = hintCords[1];
            if (board.getCurrentValue(hintRow,hintCol)==0) {
                theHint = board.getCorrectValue(hintRow,hintCol);
            }
        }
        board.setCurrentValue(theHint,hintRow,hintCol);
    }

    /**
     * Empties the current board, makes new board.
     * @param newBoard
     */
    void handleRestartGame(SudokuBoard newBoard){
        this.board = newBoard;
    }

    /**
     * Saves the state of the game into a file of your choosing
     */
    void handleSaveGame(){

        FileChooser fileChooser = new FileChooser();
        File file = null;

        fileChooser.setTitle("Save Sudoku File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Sudoku Files", "*.sudoku"));

        try {
            file = fileChooser.showSaveDialog(this.view.getScene().getWindow());
            Filhantering.serializeToFile(this.board,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the data of the file which was saved
     * @return board state of the saved game
     */
    SudokuBoard handleLoadGame(){

        File file = null;
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Sudoku File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Sudoku Files", "*.sudoku"));

        try {
            file = fileChooser.showOpenDialog(this.view.getScene().getWindow());
            this.board = Filhantering.deserializeFromFile(file);
            return this.board;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;

    }



}
