package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.model.gods.*;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import org.junit.Assert;
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
    public void testCanActivate(){
        assertFalse(tester.canActivate(null));
    }

    @Test
    public void testToStringEffect_noExistentGod_noEffectToPrint(){
        GodCard tester = new GodCard();
        assertEquals("no selected god", tester.toStringEffect());
    }

    @Test
    public void testToStringApollo(){
        GodCard godCard = new Apollo();
        assertEquals("Your Move" + ": Your Worker may move into an opponent Worker’s space\n" +
                "(using normal movement rules) and force their Worker to the space yours\n" +
                "just vacated (swapping their positions).",godCard.toStringEffect());
    }

    @Test
    public void testToStringAres(){
        GodCard godCard = new Ares();
        assertEquals("End of Your Turn" + ": You may remove an unoccupied block\n" +
        "(not dome) neighboring your unmoved Worker",godCard.toStringEffect());
    }

    @Test
    public void testToStringArtemis(){
        GodCard godCard = new Artemis();
        assertEquals("Your Move" + ": Your Worker may move one additional time,\n" +
                "but not back to the space it started on.",godCard.toStringEffect());
    }

    @Test
    public void testToStringAthena(){
        GodCard godCard = new Athena();
        assertEquals("Opponents' Turn" + ": If one of your Workers moved up on your last turn, \n" +
                "opponent Workers cannot move up this turn.",godCard.toStringEffect());
    }

    @Test
    public void testToStringAtlas(){
        GodCard godCard = new Atlas();
        assertEquals("Your Build" + ": Your Worker may build a dome\n" +
                "at any level including the ground.",godCard.toStringEffect());
    }

    @Test
    public void testToStringChronus(){
        GodCard godCard = new Chronus();
        assertEquals("Win Condition" + ": You also win\n" +
                "when there are at least five\n" +
                "Complete Towers on the board..",godCard.toStringEffect());
    }

    @Test
    public void testToStringDemeter(){
        GodCard godCard = new Demeter();
        assertEquals("Your Build" + ": Your Worker may build one additional time,\n" +
                "but not on the same space.",godCard.toStringEffect());
    }

    @Test
    public void testToStringHephaestus(){
        GodCard godCard = new Hephaestus();
        assertEquals("Your Build" + ": Your Worker may build one additional block\n" +
                "(not dome) on top of your first block.",godCard.toStringEffect());
    }

    @Test
    public void testToStringHestia(){
        GodCard godCard = new Hestia();
        assertEquals("Your Build" + ": Your Worker may build one additional time.\n" +
                "The additional build cannot be on a perimeter space.",godCard.toStringEffect());
    }

    @Test
    public void testToStringMinotaur(){
        GodCard godCard = new Minotaur();
        assertEquals("Your Move" + ": Your Worker may move into an opponent Worker’s\n" +
                "space (using normal movement rules), if the next space in the same direction is\n" +
                "unoccupied. Their Worker is forced into that space (regardless of its level).",godCard.toStringEffect());
    }

    @Test
    public void testToStringPan(){
        GodCard godCard = new Pan();
        assertEquals("Win Condition" + ": You also win if your Worker moves down two or more levels.",godCard.toStringEffect());
    }

    @Test
    public void testToStringPoseidon(){
        GodCard godCard = new Poseidon();
        assertEquals("End of Your Turn" + ": If your unmoved Worker is on the ground level,\n" +
                "it may build up to three times in neighboring spaces.",godCard.toStringEffect());
    }

    @Test
    public void testToStringPrometheus(){
        GodCard godCard = new Prometheus();
        assertEquals("Your Turn" + ": If your Worker does not move up,\n" +
                "it may build both before and after moving.",godCard.toStringEffect());
    }

    @Test
    public void testToStringZeus(){
        GodCard godCard = new Zeus();
        assertEquals("Your Build" + ": Your Worker may build under itself in its current\n" +
                "space, forcing it up one level. You do not win by forcing yourself up to the third level.",godCard.toStringEffect());
    }
}