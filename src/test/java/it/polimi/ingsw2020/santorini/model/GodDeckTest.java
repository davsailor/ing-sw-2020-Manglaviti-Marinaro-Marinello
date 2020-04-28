package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.exceptions.EmptyDeckException;
import org.junit.Test;

import static org.junit.Assert.*;

public class GodDeckTest {

    @Test
    public void testShuffleDeck() {
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
                assertFalse("error occurred in deck creation", false );
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
                assertFalse("shuffle modifies cards into the deck",false);
        }
    }

    @Test
    public void testGiveGard_firstApollo_giveApollo() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(0,0);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card.getName().equals("Apollo") && deck.getNextCard() == 1);
    }

    @Test
    public void testGiveGard_firstAres_giveAres() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(9,0);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card.getName().equals("Ares") && deck.getNextCard() == 1);
    }

    @Test
    public void testGiveGard_firstArtemis_giveArtemis() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(1,0);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card.getName().equals("Artemis") && deck.getNextCard() == 1);
    }

    @Test
    public void testGiveGard_firstAthena_giveAthena() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(2,0);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card.getName().equals("Athena") && deck.getNextCard() == 1);
    }

    @Test
    public void testGiveGard_firstAtlas_giveAtlas() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(3,0);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card.getName().equals("Atlas") && deck.getNextCard() == 1);
    }

    @Test
    public void testGiveGard_firstDemeter_giveDemeter() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(4,0);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card.getName().equals("Demeter") && deck.getNextCard() == 1);
    }

    @Test
    public void testGiveGard_firstHephaestus_giveHephaestus() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(5,0);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card.getName().equals("Hephaestus") && deck.getNextCard() == 1);
    }

    @Test
    public void testGiveGard_firstMinotaur_giveMinotaur() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(6,0);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card.getName().equals("Minotaur") && deck.getNextCard() == 1);
    }

    @Test
    public void testGiveGard_firstPan_givePan() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(7,0);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card.getName().equals("Pan") && deck.getNextCard() == 1);
    }

    @Test
    public void testGiveGard_firstPrometheus_givePrometheus() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(8,0);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card.getName().equals("Prometheus") && deck.getNextCard() == 1);
    }

    @Test
    public void testGiveGard_firstHestia_giveHestia() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(10,0);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card.getName().equals("Hestia") && deck.getNextCard() == 1);
    }

    @Test
    public void testGiveGard_firstPersephone_givePersephone() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(11,0);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card.getName().equals("Persephone") && deck.getNextCard() == 1);
    }

    @Test
    public void testGiveGard_firstPoseidon_givePoseidon() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(12,0);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card.getName().equals("Poseidon") && deck.getNextCard() == 1);
    }

    @Test
    public void testGiveGard_firstZeus_giveZeus() {
        GodDeck deck = new GodDeck();
        deck.setValueToIndex(13,0);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card.getName().equals("Zeus") && deck.getNextCard() == 1);
    }

    @Test
    public void testGiveCard_noMoreCard_giveNull(){
        GodDeck deck = new GodDeck();
        deck.setNextCard(14);
        GodCard card = deck.giveCard();
        assertTrue("Test passato!",card == null && deck.getNextCard() == 14);
    }
}
