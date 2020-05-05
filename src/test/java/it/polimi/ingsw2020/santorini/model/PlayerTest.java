package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.model.gods.Apollo;
import it.polimi.ingsw2020.santorini.utils.Color;
import it.polimi.ingsw2020.santorini.utils.PlayerStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;

public class PlayerTest {

    private Date date;
    private Player player;

    @Before
    public void setup(){
        date = new Date(1998, 8, 3);
        player = new Player("davsailor" , date);
    }

    @Test
    public void testGetNickname() {
        assertEquals("davsailor", player.getNickname());
    }

    @Test
    public void testSetNickname() {
        player.setNickname("lukeMari");
        assertEquals("lukeMari", player.getNickname());
    }

    @Test
    public void testGetBirthDate() {
        assertEquals(date, player.getBirthDate());
    }

    @Test
    public void testSetBirthDate() {
        Date date = new Date(1998,8,27);
        player.setBirthDate(date);
        assertEquals(date, player.getBirthDate());
    }

    @Test
    public void testGetColor() {
        player.setColor(Color.GREEN);
        assertEquals(Color.GREEN, player.getColor());
    }

    @Test
    public void testSetColor() {
        player.setColor(Color.GREEN);
        assertEquals(Color.GREEN, player.getColor());
    }

    @Test
    public void testGetStatus() {
        assertEquals(PlayerStatus.WAITING, player.getStatus());
    }

    @Test
    public void testSetStatus() {
        player.setStatus(PlayerStatus.READY);
        assertEquals(PlayerStatus.READY, player.getStatus());
        player.setStatus(PlayerStatus.DISCONNECTED);
        assertEquals(PlayerStatus.DISCONNECTED, player.getStatus());
        player.setStatus(PlayerStatus.PLAYING);
        assertEquals(PlayerStatus.PLAYING, player.getStatus());
    }

    @Test
    public void testGetDivinePower() {
        assertNull(player.getDivinePower());
        player.setDivinePower(new Apollo());
        assertTrue(player.getDivinePower() instanceof Apollo);
    }

    @Test
    public void testSetDivinePower() {
        player.setDivinePower(new Apollo());
        assertTrue(player.getDivinePower() instanceof Apollo);
    }

    @Test
    public void testGetAndSetBuilderF() {
        player.setBuilderF(new Builder(player,'\u2640', new Board(new GodDeck()),null));
        assertNotNull(player.getBuilderF());
        assertEquals('\u2640', player.getBuilderF().getGender());
        assertEquals(3, player.getBuilderF().getBuildPosX());
        assertEquals(3, player.getBuilderF().getBuildPosY());
    }

    @Test
    public void testGetAndSetBuilderM() {
        player.setBuilderM(new Builder(player,'\u2642', new Board(new GodDeck()),null));
        assertNotNull(player.getBuilderM());
        assertEquals('\u2642', player.getBuilderM().getGender());
        assertEquals(3, player.getBuilderM().getBuildPosX());
        assertEquals(3, player.getBuilderM().getBuildPosY());
    }

    @Test
    public void testSetAndGetRiseActions() {
        assertTrue(player.getRiseActions());
        player.setRiseActions(false);
        assertFalse(player.getRiseActions());
    }

    @Test
    public void testSetAndGetMoveActions() {
        assertTrue(player.getMoveActions());
        player.setMoveActions(false);
        assertFalse(player.getMoveActions());
    }

    @Test
    public void testSetAndGetBuildActions() {
        assertTrue(player.getBuildActions());
        player.setBuildActions(false);
        assertFalse(player.getBuildActions());
    }

    @Test
    public void testSetAndGetPlayingBuilder() {
        assertNull(player.getPlayingBuilder());
        player.setBuilderF(new Builder(player, 'M',null, null));
        player.setPlayingBuilder(player.getBuilderF());
        assertEquals(player.getBuilderF(), player.getPlayingBuilder());
    }
}