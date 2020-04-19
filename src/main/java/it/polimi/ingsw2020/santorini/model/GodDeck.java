package it.polimi.ingsw2020.santorini.model;
import it.polimi.ingsw2020.santorini.exceptions.EmptyDeckException;
import it.polimi.ingsw2020.santorini.exceptions.UnexpectedGodException;
import it.polimi.ingsw2020.santorini.utils.GodNameType;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class GodDeck {
    private int[] deck;
    private final int GOD_NUMBER = GodNameType.values().length;
    private int nextCard;
    private Random random;

    /**
     * method that create the deck of god's card, so that we can extract casually (or chose) the card we'll play
     */
    public GodDeck(){
        this.nextCard = 0;
        this.random = new Random();
        this.deck = new int[GOD_NUMBER];
        try {
            for (GodNameType g : GodNameType.values()) {
                deck[g.getCode()] = g.getCode();
            }
        } catch (Exception e) {
            System.out.println("Unexpected God!");
        }
    }

    /**
     * method that will randomize the order of cards into the array
     */
    public void shuffleDeck()
    {
        int j;
        int temp;
        this.nextCard = 0;
        for(int i = 0; i < GOD_NUMBER; ++i)
        {
            j = random.nextInt(GOD_NUMBER);
            temp    = deck[i];
            deck[i] = deck[j];
            deck[j] = temp;
        }
    }

    /**
     * assigns a card to the player
     * @return the top deck god card
     */
    public GodCard giveGard()
    {
        try{
            if(nextCard == GOD_NUMBER) throw new EmptyDeckException();
            for(GodNameType g : GodNameType.values()) {
                if(g.getCode() == deck[nextCard]) {
                    ++nextCard;
                    return (GodCard) g.getConstructor().newInstance();
                }
            }
            throw new UnexpectedGodException();
        } catch (EmptyDeckException e){
            System.out.println("No more gods available!");
        }
        catch (UnexpectedGodException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            System.out.println("Your god does not exist!");
        }
        return null;
    }

    /**
     * format the deck into a string
     * @return deck in string version
     */
    public String toString()
        {
            StringBuilder cards = new StringBuilder("Gods: ");
            for (int i = 0; i < GOD_NUMBER; ++i){
                for (GodNameType g : GodNameType.values()) {
                    try {
                        cards.append(g.getName()).append("\n");
                    } catch (UnexpectedGodException e) {
                        System.out.println("Your god does not exist!");
                    }
                }
            }
        return cards.toString();
    }
}
