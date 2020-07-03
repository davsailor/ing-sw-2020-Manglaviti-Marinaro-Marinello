package it.polimi.ingsw2020.santorini.model;
import it.polimi.ingsw2020.santorini.exceptions.UnexpectedGodException;

public class GodDeck {
    private int[] deck;
    private final int GOD_NUMBER = GodFactotum.values().length;
    private int nextCard;
    private int nextSelection;
    private int numberOfPlayers;

    /**
     * method that create the deck of god's card, so that we can extract casually (or chose) the card we'll play
     * @param numberOfPlayers the number of players of the match. If it is 2, Chronus is permitted, otherwise Chronus is forbidden
     */
    public GodDeck(int numberOfPlayers){
        this.nextCard = 0;
        this.nextSelection = 0;
        if(numberOfPlayers == 2)
            this.deck = new int[GOD_NUMBER];
        else
            this.deck = new int[GOD_NUMBER - 1];
        this.numberOfPlayers = numberOfPlayers;
        try {
            int i = 0;
            for (GodFactotum g : GodFactotum.values())
                if(numberOfPlayers <= g.getNumberOfPlayers()) {
                    deck[i] = g.getCode();
                    ++i;
                }
        } catch (Exception e) {
            System.out.println("Unexpected God!");
        }
    }

    public int getNextSelection() {
        return nextSelection;
    }

    public void setNextSelection(int nextSelection) {
        this.nextSelection = nextSelection;
    }

    /**
     * creates a shadow copy of the deck
     * @return the shadow copy of the deck
     */
    public int[] getDeck() {
        int[] deckCpy = new int[deck.length];
        for(int i = 0; i < deck.length; ++i)
            deckCpy[i] = deck[i];
        return deckCpy;
    }

    /**
     * getter of nextCard
     * @return the index of the next card
     */
    public int getNextCard() {
        return nextCard;
    }

    /**
     * creates and assigns a card to the player
     * @param code the code of the card
     * @return the card corresponding to the code
     */
    public GodCard giveCard(int code) {
        try{
            if(code >= GOD_NUMBER || code < 0) throw new UnexpectedGodException();
            for(GodFactotum g : GodFactotum.values())
                if(isExtracted(code) && g.getCode() == code)
                    return g.summon();
            throw new UnexpectedGodException();
        } catch (UnexpectedGodException e) {
            System.out.println("Your god does not exist!");
        }
        return null;
    }

    /**
     * extract the selected card and places it at the top of the deck
     * @param code the code of the god to extract
     */
    public void extract(int code){
        if(code < 0 || code >= GOD_NUMBER || isExtracted(code)) return;
        int temp;
        for(int i = nextCard; i < deck.length; ++i){
            if(deck[i] == code){
                temp = deck[i];
                if (i - nextCard >= 0) System.arraycopy(deck, nextCard, deck, nextCard + 1, i - nextCard);
                deck[nextCard] = temp;
                ++nextCard;
                break;
            }
        }
    }

    /**
     * method that will determine if a certain card has been extracted from the deck
     * @param code the code of the god we are looking for
     * @return true if the god has been extracted, false otherwise
     */
    public boolean isExtracted(int code){
        if(code == 11 && numberOfPlayers == 3) return true;
        for(int i = 0; i < nextCard; ++i)
            if(deck[i] == code) return true;
        return false;
    }

    /**
     * format the deck into a string
     * @return deck in string version
     */
    public String toString() {
        StringBuilder cards = new StringBuilder("Gods: \n");
        for (GodFactotum g : GodFactotum.values()) {
            try {
                if(numberOfPlayers <= g.getNumberOfPlayers() && !isExtracted(g.getCode()))
                    cards.append("Insert ").append(g.getCode()).append(":\t").append(g.getName()).append("\n");
            } catch (UnexpectedGodException e) {
                System.out.println("Your god does not exist!");
            }
        }
        return cards.toString();
    }
}
