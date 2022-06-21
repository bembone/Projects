package cardutils;

public class PokerHands {

    private PokerHands(){

    }

    public static PokerCombo getPokerCombo(Pile hand){
        if (isThreeOfOne(hand))
            return PokerCombo.THREE_OF_A_KIND;
        if (isFlush(hand))
            return PokerCombo.FLUSH;
        if (isTwoPairs(hand))
            return PokerCombo.TWO_PAIRS;
        if (isPair(hand))
            return PokerCombo.PAIR;
        else return PokerCombo.NONE;
    }

    private static boolean isPair(Pile p){
        for (Suit suit: Suit.values()){
            for (Rank rank: Rank.values()){
                if (p.noOfRank(rank)==2)
                    return true;
            }
        }
        return false;
    }

    private static boolean isTwoPairs(Pile p){
        for (Suit suit: Suit.values()){
            int counter=0;
            for (Rank rank: Rank.values()){
                if (p.noOfRank(rank)==2) {
                    counter++;
                    if (counter==2)
                        return true;
                }
            }
        }
        return false;
    }

    private static boolean isThreeOfOne(Pile p){
        for (Suit suit: Suit.values()){
            for (Rank rank: Rank.values()){
                if (p.noOfRank(rank)==3)
                    return true;
            }
        }
        return false;
    }

    private static boolean isFlush(Pile p){
        for (Suit suit: Suit.values()){
            for (Rank rank: Rank.values()){
                if (p.noOfSuit(suit)==p.getSize())
                    return true;
            }
        }
        return false;
    }

}
