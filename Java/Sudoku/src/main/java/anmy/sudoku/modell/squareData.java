package anmy.sudoku.modell;

import java.io.Serializable;

/**
 *This class contains the data of each tile. The tiles correct value corresponding to the solved matrix, the value given to the tile by the user and a hidden
 * boolean to describe its visibility. For example if the current value of a tile in the generated matrix is "0", then the tile should initialise as an empty tile.
 * Constructor in this class creates an object when called and sets all the correct values from the solved array and the user inputs to the current inputs.
 *
 */
public class squareData implements Serializable{  // ta bort extends TilePane,
    // måste göra denna till TilePane på ett annat sätt, kanske via controller

    private int currentValue;
    private final int correctValue;
    private boolean hidden;

    /**
     * Constructs a new squareData with the specified correct value, if it is hidden and current value
     * @param theValue The correct value
     * @param Hidden boolean hidden
     * @param currentValue The current value
     */
    public squareData(int theValue, boolean Hidden, int currentValue){
        this.correctValue = theValue;
        this.hidden = Hidden;
        this.currentValue = currentValue;
    }

    /**
     * Used when comparing a input from user to the correct corresponding tile.
     * @return the correct value in a specified index sudoku matrix.
     */
    public int getCorrectValue() {
        return correctValue;
    }

    /**
     * Describes the tile, should the tile be empty or filled from start?
     * @return hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Sets the hidden value of the square
     * @param hidden boolean
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Returns the current value
     * @return currentValue
     */
    public int getCurrentValue() {
        return currentValue;
    }

    /**
     * Sets the current value of the square
     * @param currentValue The current value
     */
    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    /**
     * Removes a value from specified tile sets it to 0 in case of clear button.
     */
    public void removeCurrentValue(){
        this.currentValue=0;
    }

    /**
     * Returns a string representation of squareData
     *
     */
    @Override
    public String toString() {
        return "squareData{" +
                "correctValue=" + correctValue +
                ", hidden=" + hidden +
                ", currentValue=" + currentValue +
                '}';
    }
}
