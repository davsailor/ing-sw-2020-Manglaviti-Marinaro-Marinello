package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.controller.TurnLogic;
import it.polimi.ingsw2020.santorini.exceptions.EndMatchException;
import it.polimi.ingsw2020.santorini.model.gods.*;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;

public class GodCard {

    protected String name;                  // name of god
    protected int maxPlayersNumber;         // max players number you can play with using this card
    protected String timingName;            // name of the timing (only for text effect purpose)
    protected PhaseType timing;             // phase in which you can activate its power
    protected boolean mandatory;            // if the power MUST activate or CAN activate
    protected boolean needParameters;       // if the god needs some information to activate

    /*
     * constructor for serialization and deserialization with Gson
     */
    public GodCard(){}

    /*
     * getter of class parameters, setter not needed since thy are considered final
     */
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
     * the function that will modify the match and its normal phases following the instructions of god
     * @param match the match playing
     * @param message this message refers to possible parameters requested to players. Each overridden method can
     *                deserialize correctly this message. Apollo will understand its own param message (ApolloParamMessage)
     *                but will not understand other gods parameters messages
     * @param turnManager since some gods modify phases of the match, we need a reference to turnManager to correctly
     *                    modify the turn
     * @throws EndMatchException some gods (such as Pan) can make you win the game
     */
    public void invokeGod(Match match, Message message, TurnLogic turnManager) throws EndMatchException {}

    /**
     * this function evaluate the match state and tells if the god's power can be used
     * in the superclass is constantly false, because it's the state in which no god is selected
     * @param match is the current match that has to be evaluate
     * @return true if the god can be activated, false otherwise
     */
    public boolean canActivate(Match match){
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
                case "Chronus":
                    return Chronus.toStringEffect(this);
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
