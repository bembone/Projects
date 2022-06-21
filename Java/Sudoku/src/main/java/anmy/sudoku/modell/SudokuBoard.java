package anmy.sudoku.modell;

import java.io.Serializable;
import java.util.Random;

/**
 * Represents a sudoku board
 * The board consists of a two-dimensional array of the class squareData
 */
public class SudokuBoard implements Serializable {

    public static final int ROWS = 9;
    public static final int COLS = 9;

    private squareData gameBoard[][] = new squareData[ROWS][COLS];
    private SudokuUtilities.SudokuLevel theDifficulty;

    /**
     * Constructs a new SudokuBoard with the specified enum SudokuLevel difficulty
     * @param difficulty The difficulty from which to construct the board from
     */
    public SudokuBoard(SudokuUtilities.SudokuLevel difficulty){
        int theBoard[][][] = SudokuUtilities.generateSudokuMatrix(difficulty);
        for (int i=0;i<ROWS;i++){
            for (int j=0;j<COLS;j++){
                squareData theData = new squareData(theBoard[i][j][1],false, theBoard[i][j][0]);
                this.gameBoard[i][j]=theData;

                if (theBoard[i][j][0]==0){
                    gameBoard[i][j].setHidden(true);
                }
            }
        }
        theDifficulty=difficulty;
    }

    /**
     * Returns the data of a specified square in the board
     * @param x The X posiiton
     * @param y The Y posiiton
     * @return squareData in the [x][y] position
     */
    public squareData getGameBoardByPos(int x, int y) {
        return gameBoard[x][y];
    }

    /**
     * Places a specified value into a specific square where hidden is defiened as true
     * @param button The value to place
     * @param x The X position
     * @param y The Y position
     */
    public void handleButtonToTile(char button,int x, int y){
        if (gameBoard[x][y].isHidden()) {
            gameBoard[x][y].setCurrentValue(button - '0');
        }
    }

    /**
     * Returns the current value of a specified square
     * @param x The X position
     * @param y The Y position
     */
    public int getCurrentValue(int x, int y){
        return this.gameBoard[x][y].getCurrentValue();
    }

    /**
     * Returns the correct value of a specified square
     * @param x The X position
     * @param y The Y position
     */
    public int getCorrectValue(int x, int y){
        return this.gameBoard[x][y].getCorrectValue();
    }

    /**
     * Removes a specified value from a specific square where hidden is defiened as true
     * @param x The X position
     * @param y The Y position
     */
    public void removeCurrentValue(int x, int y){
        if (gameBoard[x][y].isHidden()) {
            gameBoard[x][y].removeCurrentValue();
        }
    }

    /**
     * Sets the current value of a square if it is empty
     * @param value The value to set
     * @param x The X position
     * @param y The Y position
     */
    public void setCurrentValue(int value,int x, int y){
        if (gameBoard[x][y].getCurrentValue()==0) {
            gameBoard[x][y].setCurrentValue(value);
        }
    }

    /**
     * Checks if all the squares of the board are have the correct value
     * @return boolean true if all the squares are correct
     */
    public boolean checkSolvedGame(){
        int count = 0;
        for (int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuUtilities.GRID_SIZE; col++) {
                if (gameBoard[row][col].getCurrentValue()==gameBoard[row][col].getCorrectValue()){
                    count++;
                }
            }
        }
        if (count==(SudokuUtilities.GRID_SIZE*SudokuUtilities.GRID_SIZE))
            return true;
        return false;
    }

    /**
     * Checks how many of the "hidden" squares have the correct value assigned
     * @return The amount of correct squares
     */
    public int handleCheckGame(){
        int count=0;
        for (int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuUtilities.GRID_SIZE; col++) {
                if (gameBoard[row][col].isHidden()){
                    if (gameBoard[row][col].getCurrentValue() == gameBoard[row][col].getCorrectValue()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Randomizes a position of the board to give hint to
     * @return The position
     */
    public int[] handleHint(){

        Random cords = new Random();
        int row = cords.nextInt(SudokuUtilities.GRID_SIZE);
        int col = cords.nextInt(SudokuUtilities.GRID_SIZE);
        int theCords[] = new int[2];
        theCords[0]=row;
        theCords[1]=col;

        return theCords;
    }

    /**
     * Retruns the difficulty of the board
     * @return theDifficulty
     */
    public SudokuUtilities.SudokuLevel getDifficulty(){
        return theDifficulty;
    }

    /**
     * Returns a string representation of how the board looks
     *
     */
    @Override
    public String toString() {
        String info="";
        for (int i=0;i<ROWS;i++){
            for (int j=0;j<COLS;j++){
                info+=gameBoard[i][j].getCurrentValue() + " ";
            }
            info+="\n";
        }
        return info;
    }
}
