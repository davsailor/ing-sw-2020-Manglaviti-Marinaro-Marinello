package it.polimi.ingsw2020.santorini.utils.messages.actions;

public class ActivateGodMessage {
    private boolean wantToActivate;

    public ActivateGodMessage(boolean wantToActivate) {
        this.wantToActivate = wantToActivate;
    }

    public boolean isWantToActivate() {
        return wantToActivate;
    }
}
