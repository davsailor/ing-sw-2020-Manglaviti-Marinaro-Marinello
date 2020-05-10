package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.model.gods.*;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GodCardTest {
    private GodCard tester;

    @Before
    public void setUp() {
        tester = new GodCard();
    }

    @Test
    public void testGetName() {
        tester = new Apollo();
        assertEquals("Apollo", tester.getName());
    }

    @Test
    public void testGetMaxPlayersNumber() {
        tester = new Apollo();
        assertEquals(3, tester.getMaxPlayersNumber());
    }

    @Test
    public void testGetTimingName() {
        tester = new Apollo();
        assertEquals("Your Move", tester.getTimingName());
    }

    @Test
    public void testGetTiming() {
        tester = new Apollo();
        assertEquals(PhaseType.STANDBY_PHASE_1, tester.getTiming());
    }

    @Test
    public void testIsMandatory() {
        tester = new Apollo();
        assertFalse(tester.isMandatory());
    }

    @Test
    public void testIsNeedParameters(){
        tester = new Apollo();
        assertTrue(tester.isNeedParameters());
    }

    @Test
    public void invokeGod() {
        //TODO: quando saranno implementati i poteri degli dei: da fare per ogni divinità
    }

    @Test
    public void testToStringEffect() {
        GodDeck deck = new GodDeck();
        tester = deck.giveCard();
        assertEquals(Apollo.toStringEffect(tester), tester.toStringEffect());
        tester = deck.giveCard();
        assertEquals(Artemis.toStringEffect(tester), tester.toStringEffect());
        tester = deck.giveCard();
        assertEquals(Athena.toStringEffect(tester), tester.toStringEffect());
        tester = deck.giveCard();
        assertEquals(Atlas.toStringEffect(tester), tester.toStringEffect());
        tester = deck.giveCard();
        assertEquals(Demeter.toStringEffect(tester), tester.toStringEffect());
        tester = deck.giveCard();
        assertEquals(Hephaestus.toStringEffect(tester), tester.toStringEffect());
        tester = deck.giveCard();
        assertEquals(Minotaur.toStringEffect(tester), tester.toStringEffect());
        tester = deck.giveCard();
        assertEquals(Pan.toStringEffect(tester), tester.toStringEffect());
        tester = deck.giveCard();
        assertEquals(Prometheus.toStringEffect(tester), tester.toStringEffect());
        tester = deck.giveCard();
        assertEquals(Ares.toStringEffect(tester), tester.toStringEffect());
        tester = deck.giveCard();
        assertEquals(Hestia.toStringEffect(tester), tester.toStringEffect());
        tester = deck.giveCard();
        assertEquals(Persephone.toStringEffect(tester), tester.toStringEffect());
        tester = deck.giveCard();
        assertEquals(Poseidon.toStringEffect(tester), tester.toStringEffect());
        tester = deck.giveCard();
        assertEquals(Zeus.toStringEffect(tester), tester.toStringEffect());
    }

    @Test
    public void testToStringEffect_noExistentGod_noEffectToPrint(){
        GodCard tester = new GodCard();
        assertEquals("no selected god", tester.toStringEffect());
    }
}