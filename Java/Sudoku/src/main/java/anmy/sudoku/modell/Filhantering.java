package anmy.sudoku.modell;

import java.io.*;

public class Filhantering {

    /**
     * Saves the current game to a file of your choosing /or new file
     * @param model The current game
     * @param file The file to save to
     * @throws IOException if failed to save
     */
    public static void serializeToFile(SudokuBoard model, File file) throws IOException{

        ObjectOutputStream out = null;

        try{
            out = new ObjectOutputStream(new FileOutputStream(file.getAbsolutePath()));
            out.writeObject(model);
        }
        finally {
            try{
                if (out != null)
                    out.close();
            } catch (Exception e){}
        }

    }

    /**
     * Loads a game from a .sudoku file of your choosing
     * @return The loaded game
     * @param file The file to load from
     * @throws IOException if failed to load
     */
    public static SudokuBoard deserializeFromFile(File file) throws IOException{

        ObjectInputStream in = null;

        try {

            in = new ObjectInputStream(new FileInputStream(file.getAbsolutePath()));
            SudokuBoard model = (SudokuBoard) in.readObject();
            return model;
        } catch (FileNotFoundException e){
            System.out.println("FileNotFound");
        } catch (EOFException e){
            System.out.println("End Of File");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            try{
                if(in!=null) in.close();
            } catch (Exception e) {}
        }

        return null;
    }

}


