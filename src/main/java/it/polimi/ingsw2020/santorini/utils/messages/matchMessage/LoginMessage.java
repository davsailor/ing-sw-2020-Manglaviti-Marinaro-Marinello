package it.polimi.ingsw2020.santorini.utils.messages.matchMessage;

import java.util.Date;

public class LoginMessage {
    private String username;
    private Date birthDate;
    private int numberOfPlayers;

    public LoginMessage(String username, Date birthDate, int numberOfPlayers) {
        this.username = username;
        this.birthDate = birthDate;
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getUsername() {
        return username;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
}
