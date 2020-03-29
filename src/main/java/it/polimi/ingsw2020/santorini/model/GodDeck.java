package it.polimi.ingsw2020.santorini.model;
import java.lang.reflect.Constructor;
import java.util.Random;

public class GodDeck {
    private GodCard deck[];
    private Board board;
    private final int GOD_NUMBER = GodNameType.values().length;
    private int nextCard;
    private Random random;

    /**
     * method that create the deck of god's card, so that we can extract casually (or chose) the card we'll play
     * @param field the board that refers to this deck
     * @throws Exception (UnexpectedGodException), the god is not available
     */
    public GodDeck(Board field) throws Exception {
        this.board = field;
        this.nextCard = 0;
        this.random = new Random();
        this.deck = new GodCard[GOD_NUMBER];
        try {
            for (GodNameType g : GodNameType.values()) {
                Constructor constructor = g.getName().getClass().getConstructor();
                deck[g.getCode()] = (GodCard) constructor.newInstance();
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
        GodCard temp;
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
     * method that distributes the card on top of the deck
     * @return the card on top of the deck
     * @throws Exception: no more card in deck (EmptyDeckException)
     */
    public GodCard giveGard() throws Exception
    {
        try{
            return deck[nextCard++];
        } catch (Exception e){
            System.out.println("No more gods available!");
            return null;
        }
    }

    /**
     * format the deck into a string
     * @return deck in string version
     */
    public String toString()
    {
        StringBuilder cards = new StringBuilder("Gods: ");
        for(int i = 0; i < GOD_NUMBER; ++i)
            cards.append(deck[i].getName()).append("\n");
        return cards.toString();
    }
}
