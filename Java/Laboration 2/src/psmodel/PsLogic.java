package psmodel;

import cardutils.*;

import java.util.ArrayList;
import java.util.List;

public class PsLogic implements IPsLogic {
    private Card nextCard;
    private Deck deck;
    private Pile[] piles;

    public PsLogic(){
        deck = new Deck();
        piles = new Pile[5];
        for (int i=0; i < piles.length; i++)
            piles[i] = new Pile();
    }

    public int getCardCount(){
        return 52-deck.getSize();
    }

    public void initNewGame(){
        deck.fill();
        deck.shuffleCards();
        for (int i=0; i < piles.length; i++)
            piles[i].clear();
    }

    public Card pickNextCard() throws IllegalStateException {
        if (nextCard!=null)
            throw new IllegalStateException("Error, have not dealt the previous card!");
        nextCard= deck.dealCard();
        return nextCard;
    }

    public void addCardToPile(int n) throws IllegalStateException {
        if (nextCard==null)
            throw new IllegalStateException("Error, have not picked up a card from the deck!");
        if (piles[n].getSize()==5)
            throw new IllegalStateException("This pile is full!");
        else if (piles[n].getSize()<5) {
            piles[n].add(nextCard);
            nextCard = null;
        }
    }

    public List<Pile> getPiles() {
        ArrayList<Pile> thePiles = new ArrayList<Pile>(piles.length);
        for (int i=0; i< piles.length; i++)
            thePiles.add(piles[i]);
        return thePiles;
    }

    public boolean isGameOver() {
        if (52-deck.getSize()>=25)
            return true;
        return false;
    }

    public int getPoints() {
        int points=0;
        for (Pile pile : piles) {
            if (PokerHands.getPokerCombo(pile) == PokerCombo.THREE_OF_A_KIND)
                points+=6;
            if (PokerHands.getPokerCombo(pile) == PokerCombo.FLUSH)
                points+=5;
            if (PokerHands.getPokerCombo(pile) == PokerCombo.TWO_PAIRS)
                points+=3;
            if (PokerHands.getPokerCombo(pile) == PokerCombo.PAIR)
                points+=1;
            if (PokerHands.getPokerCombo(pile) == PokerCombo.NONE)
                points+=0;
        }
        return points;
    }

    public String toString(){
        String info="";
        //info += deck.toString();          // Testing
        //info += "\n";
        for (int i=1; i< piles.length+1; i++) {
            info += "Pile " + i + "     (" + piles[i-1].getSize() + " cards)";
            if (piles[i-1].getSize()==5)
                info += " (FULL!!!)";
            info += "\n";
            info += piles[i-1].toString();
            info += "\n";
        }
        return info;
    }

}
