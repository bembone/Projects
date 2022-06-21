import psmodel.PsLogic;

import java.util.Scanner;

public class PsUserInterface {
    private Scanner scan;
    private PsLogic game;
    private int Highscore;

    public PsUserInterface() {
        scan = new Scanner(System.in);
        game = new PsLogic();
        Highscore = 0;
    }

    // main loop
    public void run() {
        int answer;

        do {
            printMenu();
            answer = scan.nextInt();
            switch(answer) {
                case 1  :	gameLoop(); break;
                case 2  :	printHighscore(); break;
                case 3  :	System.out.println("Bye, bye!"); break;
                default: 	System.out.println("Unknown command");
            }

        } while(answer != 3);
    }

    public void gameLoop(){
        game.initNewGame();

        do{
            System.out.println(game.toString());
            System.out.println("Which pile will you put the card " + game.pickNextCard() + "? " + "(1-5)");
            game.addCardToPile(scan.nextInt()-1);
        }while (!game.isGameOver());
        System.out.println(game.toString());
        System.out.println("Game done, you got " + game.getPoints() + " points!");
        Highscore= game.getPoints();
    }

    public void printHighscore() {
        System.out.println("Your Highscore is: " + Highscore);
    }

    public void printMenu() {
        System.out.println("-----Menu-----");
        System.out.println("1 - Start a new game");
        System.out.println("2 - Highscore");
        System.out.println("3 - Exit");
        System.out.println("--------------");
    }

    public static void main(String[] args) {
        PsUserInterface menu = new PsUserInterface();
        menu.run();
    }
}