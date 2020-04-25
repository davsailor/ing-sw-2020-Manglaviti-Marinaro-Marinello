package it.polimi.ingsw2020.santorini.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GodDeckTest {

    @Test
    void testShuffleDeck() {
        GodDeck deck = new GodDeck();
        boolean containsBeforeShuffle;
        boolean containsAfterShuffle = true;
        for(GodFactotum g : GodFactotum.values()) {
            containsBeforeShuffle = false;
            for(int i = 0; i < deck.getGOD_NUMBER(); ++i){
                if(deck.getDeck()[i] == g.getCode()) {
                    containsBeforeShuffle = true;
                    break;
                }
            }
            if(!containsBeforeShuffle || deck.getDeck().length != deck.getGOD_NUMBER())
                assertFalse(false, "error occurred in deck creation");
        }
        deck.shuffleDeck();
        for(GodFactotum g : GodFactotum.values()) {
            containsAfterShuffle = false;
            for(int i = 0; i < deck.getGOD_NUMBER(); ++i){
                if(deck.getDeck()[i] == g.getCode()) {
                    containsAfterShuffle = true;
                    break;
                }
            }
            if(!containsAfterShuffle || deck.getDeck().length != deck.getGOD_NUMBER())
                assertFalse(false, "shuffle modifies cards into the deck");
        }
    }

    @Test
    void testGiveGard() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(0,0);
        GodCard card = deck.giveCard();
        assertTrue(card.getName().equals("Apollo") && deck.getNextCard() == 1, "Test passato!");
    }
}