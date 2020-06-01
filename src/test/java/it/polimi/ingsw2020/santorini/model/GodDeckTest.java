package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.exceptions.UnexpectedGodException;
import it.polimi.ingsw2020.santorini.model.gods.Chronus;
import org.junit.Test;

import static org.junit.Assert.*;

public class GodDeckTest {

    @Test
    public void testGetDeck(){
        GodDeck godDeck = new GodDeck(2);
        int[] godDeck1;
        godDeck1 = godDeck.getDeck();
        for (int i = 0; i < godDeck1.length; i++){
            assertEquals(godDeck1[i], godDeck.getDeck()[i]);
        }
    }
    @Test
    public void setNextSelection() {
        GodDeck godDeck = new GodDeck(2);
        GodCard tester;
        godDeck.setNextSelection(1);
        assertEquals(1, godDeck.getNextSelection());
    }

    @Test
    public void GetNextSelection(){
        GodDeck godDeck = new GodDeck(2);
        GodCard tester;
        assertEquals(0, godDeck.getNextSelection());
    }


    @Test
    public void extract() {
        GodDeck godDeck = new GodDeck(2);
        assertEquals(0, godDeck.getNextCard());
        godDeck.extract(0);
        assertEquals(1, godDeck.getNextCard());
        godDeck.extract(1);
        assertEquals(2, godDeck.getNextCard());

    }

    @Test
    public void testIsExtracted() {
        GodDeck godDeck = new GodDeck(3);
        godDeck.extract(0);
        assertTrue(godDeck.isExtracted(0));
        assertTrue(godDeck.isExtracted(11));
    }

    @Test
    public void testEnumeration_GiveAllCards() throws UnexpectedGodException {
        GodDeck godDeck = new GodDeck(2);
        GodCard tester;
        for(GodFactotum g: GodFactotum.values()){
            tester = g.summon();
            assertEquals(tester.getName(), g.getName());
        }
    }

    @Test
    public void testGiveCard() throws UnexpectedGodException {
        GodDeck godDeck = new GodDeck(2);
        GodCard tester;
        godDeck.extract(11);
        tester = godDeck.giveCard(11);
        assertEquals(tester.getName(), Chronus.class.getSimpleName());
    }
}