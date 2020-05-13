package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.model.gods.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class GodCard {
    /**
     * getter of class parameters, setter not needed since thy are considered final
     */
    protected String name;
    protected int maxPlayersNumber;
    protected String timingName;
    protected PhaseType timing;
    protected boolean mandatory;
    protected boolean needParameters;

    public GodCard(){}

    public String getName() {
        return name;
    }

    public int getMaxPlayersNumber() {
        return maxPlayersNumber;
    }

    public String getTimingName() { return timingName; }

    public PhaseType getTiming() {
        return timing;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isNeedParameters() { return needParameters; }

    /**
     * the function that express the power of the god. Each God will have its power
     * @param match is the board of the game you are playing
     * @param turnManager
     */
    public void invokeGod(Match match, Message message, TurnLogic turnManager) throws EndMatchException {}

    public boolean canActivate(Match match){
        /*try {
            switch (this.getName()) {
                case "Apollo":
                    return Apollo.canActivateApollo(match);
                case "Ares":
                    return Ares.canActivateAres(match);
                case "Artemis":
                    return Artemis.canActivateArtemis(match);
                case "Athena":
                    return Athena.canActivateAthena(match);
                case "Atlas":
                    return Atlas.canActivateAtlas(match);
                case "Demeter":
                    return Demeter.canActivateDemeter(match);
                case "Hephaestus":
                    return Hephaestus.canActivateHephaestus(match);
                case "Hestia":
                    return Hestia.canActivateHestia(match);
                case "Minotaur":
                    return Minotaur.canActivateMinotaur(match);
                case "Pan":
                    return Pan.canActivatePan(match);
                case "Persephone":
                    return Persephone.canActivatePersephone(match);
                case "Poseidon":
                    return Poseidon.canActivatePoseidon(match);
                case "Prometheus":
                    return Prometheus.canActivatePrometheus(match);
                case "Zeus":
                    return Zeus.canActivateZeus(match);
            }
        } catch(NullPointerException ignored){}*/
        return false;
    }

    /**
     * the function that explains the power of god
     * @return string format of god's name and its power. Each God will have its own
     */
    public String toStringEffect(){
        try {
            switch (this.getName()) {
                case "Apollo":
                    return Apollo.toStringEffect(this);
                case "Ares":
                    return Ares.toStringEffect(this);
                case "Artemis":
                    return Artemis.toStringEffect(this);
                case "Athena":
                    return Athena.toStringEffect(this);
                case "Atlas":
                    return Atlas.toStringEffect(this);
                case "Demeter":
                    return Demeter.toStringEffect(this);
                case "Hephaestus":
                    return Hephaestus.toStringEffect(this);
                case "Hestia":
                    return Hestia.toStringEffect(this);
                case "Minotaur":
                    return Minotaur.toStringEffect(this);
                case "Pan":
                    return Pan.toStringEffect(this);
                case "Persephone":
                    return Persephone.toStringEffect(this);
                case "Poseidon":
                    return Poseidon.toStringEffect(this);
                case "Prometheus":
                    return Prometheus.toStringEffect(this);
                case "Zeus":
                    return Zeus.toStringEffect(this);
            }
        } catch(NullPointerException ignored){}
        return "no selected god";
    }
}
