package cardutils;

import cardutils.Card;
import cardutils.Rank;
import cardutils.Suit;

import java.util.ArrayList;
import java.util.List;

public class Pile {

    private ArrayList<Card> theCards;

    public Pile(){
        theCards = new ArrayList<Card>(5);
    }

    public int getSize(){
        return theCards.size();
    }

    public void clear(){
        theCards.clear();
    }

    public void add(Card c){
        theCards.add(c);
    }

    public void add(List<Card> cards){
        theCards.addAll(cards);
    }

    public Card get(int position){
        return theCards.get(position);
    }

    public List<Card> getCards(){
        return (List<Card>) theCards.clone();
    }

    public Card remove(int position){       // carefull kanske kan skapa problem
        return theCards.remove(position);
    }

    public boolean remove(Card c){
        return theCards.remove(c);
    }

    public boolean contains(Card c){
        return theCards.contains(c);
    }

    public boolean remove(List<Card> cards){
        return theCards.removeAll(cards);
    }

    public int noOfSuit(Suit suit){
        int count=0;
        for (int i=0; i< theCards.size(); i++){
            if (theCards.get(i).getSuit()==suit)
                count++;
        }
        return count;
    }

    public int noOfRank(Rank rank){
        int count=0;
        for (int i=0; i< theCards.size(); i++){
            if (theCards.get(i).getRank()==rank)
                count++;
        }
        return count;
    }

    public String toString(){
        String info="";
        for (int i=0; i< theCards.size(); i++) {
            info += theCards.get(i).toShortString();
            info += " ";
        }
        return info;
    }

}
