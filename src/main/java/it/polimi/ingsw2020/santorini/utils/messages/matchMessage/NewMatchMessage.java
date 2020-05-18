package it.polimi.ingsw2020.santorini.utils.messages.matchMessage;

import java.util.Date;

public class NewMatchMessage {
    private boolean wantNewMatch;
    private int selectedMatch;
    private Date birthDate;

    public NewMatchMessage(boolean wantNewMatch, int selectedMatch, Date birthDate) {
        this.wantNewMatch = wantNewMatch;
        this.selectedMatch = selectedMatch;
        this.birthDate = birthDate;
    }

    public boolean isWantNewMatch() {
        return wantNewMatch;
    }

    public int getSelectedMatch() {
        return selectedMatch;
    }

    public Date getBirthDate() {
        return birthDate;
    }
}
