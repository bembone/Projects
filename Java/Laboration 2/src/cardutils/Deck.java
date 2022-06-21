package cardutils;

import cardutils.Card;
import cardutils.Rank;
import cardutils.Suit;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private ArrayList<Card> theCards;

    public Deck(){
        theCards = new ArrayList<Card>(52);
    }

    public int getSize(){
        return theCards.size();
    }

    public Card dealCard(){
        Card topCard=theCards.get(theCards.size()-1);
        theCards.remove(theCards.size()-1);
        return topCard;
    }

    public void shuffleCards(){
        Collections.shuffle(theCards);
    }

    public void fill(){
        theCards.clear();
        for (Suit c : Suit.values()){
            for (Rank r : Rank.values()){
                Card card = new Card(r,c);
                theCards.add(card);
            }
        }

    }


    public String toString(){
        String info="";
        for (int i=0; i< theCards.size(); i++)
            info += theCards.get(i) + "\n";
        return info;
    }
}
