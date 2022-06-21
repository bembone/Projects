//package se.kth.anderslm.sudoku.logic;
package anmy.sudoku.modell;

import java.io.Serializable;
import java.util.Random;

public class SudokuUtilities implements Serializable {

    public enum SudokuLevel {EASY, MEDIUM, HARD}

    public static final int GRID_SIZE = 9;
    public static final int SECTIONS_PER_ROW = 3;
    public static final int SECTION_SIZE = 3;

    /**
     * Create a 3-dimensional matrix with initial values and solution in Sudoku.
     *
     * @param level The level, i.e. the difficulty, of the initial standing.
     * @return A 3-dimensional int matrix.
     * [row][col][0] represents the initial values, zero representing an em empty cell.
     * [row][col][1] represents the solution.
     * @throws IllegalArgumentException if the length of stringRepresentation is not 2*81 characters and
     *                                  for characters other than '0'-'9'.
     */
    // TODO, as part of the assignment: Generate variations of the sudoku matrices by mirroring vertically
    //  and horizontally and by switching set of numbers.
    public static int[][][] generateSudokuMatrix(SudokuLevel level) {
        generateStrings();
        Random rand = new Random();
        int pos = rand.nextInt(easy.length);

        String representationString;
        switch (level) {
            case EASY: representationString = easy[pos]; break;
            case MEDIUM: representationString = medium[pos]; break;
            case HARD: representationString = hard[pos]; break;
            default: representationString = medium[pos];
        }
        return convertStringToIntMatrix(representationString);
    }

    /**
     * Generates string matrix's based on original,
     * the reverse strings are first reversed from the half point represents currentvalues,
     * the second half being correctvalues.
     * Replace string creates a temporary variable to replace 2 pairs of numbers,
     * stores the first replaced one in temp. variable and replaces
     * the second pair with temp. variable.
     */
    private static void generateStrings(){
        String reverse = "";
        for (int i= (easy[0].length()-1)/2;i>=0;i--){
                reverse= reverse + easy[0].charAt(i);
        }
        for (int i= (easy[0].length()-1);i>(easy[0].length()-1)/2;i--){
            reverse= reverse + easy[0].charAt(i);
        }

        easy[1]=reverse;
        reverse = "";
        for (int i= (medium[0].length()-1)/2;i>=0;i--){
            reverse= reverse + medium[0].charAt(i);
        }
        for (int i= (medium[0].length()-1);i>(medium[0].length()-1)/2;i--){
            reverse= reverse + medium[0].charAt(i);
        }
        medium[1]=reverse;
        reverse = "";
        for (int i= (hard[0].length()-1)/2;i>=0;i--){
            reverse= reverse + hard[0].charAt(i);
        }
        for (int i= (hard[0].length()-1);i>(hard[0].length()-1)/2;i--){
            reverse= reverse + hard[0].charAt(i);
        }
        hard[1]=reverse;

        String temp=easy[0];
        temp=temp.replaceAll("1","x");
        temp=temp.replaceAll("2","1");
        temp=temp.replaceAll("x","2");
        easy[2]=temp;

        temp=medium[0];
        temp=temp.replaceAll("1","x");
        temp=temp.replaceAll("2","1");
        temp=temp.replaceAll("x","2");
        medium[2]=temp;

        temp=hard[0];
        temp=temp.replaceAll("1","x");
        temp=temp.replaceAll("2","1");
        temp=temp.replaceAll("x","2");
        hard[2]=temp;

    }

    /**
     * Create a 3-dimensional matrix with initial values and solution in Sudoku.
     *
     * @param stringRepresentation A string of 2*81 characters, 0-9. The first 81 characters represents
     *                             the initial values, '0' representing an empty cell.
     *                             The following 81 characters represents the solution.
     * @return A 3-dimensional int matrix.
     * [row][col][0] represents the initial values, zero representing an em empty cell.
     * [row][col][1] represents the solution.
     * @throws IllegalArgumentException if the length of stringRepresentation is not 2*81 characters and
     *                                  for characters other than '0'-'9'.
     */
    /*package private*/
    static int[][][] convertStringToIntMatrix(String stringRepresentation) {
        if (stringRepresentation.length() != GRID_SIZE * GRID_SIZE * 2)
            throw new IllegalArgumentException("representation length " + stringRepresentation.length());

        int[][][] values = new int[GRID_SIZE][GRID_SIZE][2];
        char[] charRepresentation = stringRepresentation.toCharArray();

        int charIndex = 0;
        // initial values
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                values[row][col][0] =
                        convertCharToSudokuInt(charRepresentation[charIndex++]);
            }
        }

        // solution values
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                values[row][col][1] =
                        convertCharToSudokuInt(charRepresentation[charIndex++]);
            }
        }

        return values;
    }

    private static int convertCharToSudokuInt(char ch) {
        if (ch < '0' || ch > '9') throw new IllegalArgumentException("character " + ch);
        return ch - '0';
    }

    private static final String easy[] =
            {"000914070" +
                    "010000054" +
                    "040002000" +
                    "007569001" +
                    "401000500" +
                    "300100000" +
                    "039000408" +
                    "650800030" +
                    "000403260" +
                    "583914672" +
                    "712386954" +
                    "946752183" +
                    "827569341" +
                    "461238597" +
                    "395147826" +
                    "239675418" +
                    "654821739" +
                    "178493265","",""};

    private static final String medium[] =
            {"300000010" +
                    "000050906" +
                    "050401200" +
                    "030000080" +
                    "002069400" +
                    "000000002" +
                    "900610000" +
                    "200300058" +
                    "100800090" +
                    "324976815" +
                    "718253946" +
                    "659481273" +
                    "536142789" +
                    "872569431" +
                    "491738562" +
                    "985617324" +
                    "267394158" +
                    "143825697","",""};

    private static final String hard[] =
            {"030600000" +
                    "000010070" +
                    "080000000" +
                    "000020000" +
                    "340000800" +
                    "500030094" +
                    "000400000" +
                    "150800200" +
                    "700006050" +
                    "931687542" +
                    "465219378" +
                    "287345916" +
                    "876924135" +
                    "349561827" +
                    "512738694" +
                    "693452781" +
                    "154873269" +
                    "728196453","",""};
}

