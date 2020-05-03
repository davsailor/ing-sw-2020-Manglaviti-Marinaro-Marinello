package it.polimi.ingsw2020.santorini.utils.messages.matchMessage;

import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class NextPhaseMessage {
    private String username;
    private PhaseType currentPhase;

    public NextPhaseMessage(String username, PhaseType currentPhase) {
        this.username = username;
        this.currentPhase = currentPhase;
    }

    public String getUsername() {
        return username;
    }

    public PhaseType getCurrentPhase() {
        return currentPhase;
    }
}
