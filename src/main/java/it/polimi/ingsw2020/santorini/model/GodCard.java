package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.utils.TimingType;

public abstract class GodCard {
    private String name;
    private int maxPlayersNumber;
    private String timingName;
    private TimingType timing;

    /**
     * getter of class parameters, setter not needed since thy are considered final
     */
    public String getName() { return name; }
    public Integer getMaxPlayersNumber() { return maxPlayersNumber; }
    public String getTimingName() { return timingName; }
    public TimingType getTiming() { return timing; }

    /**
     * the function that express the power of the god. Each God will have its power
     * @param field is the board of the game you are playing
     * @param invoker is the player that activates the power
     */
    public abstract void invokeGod(Board field, Player invoker);

    /**
     * the function that explains the power of god
     * @return string format of god's name and its power. Each God will have its own
     */
    public abstract String toStringEffect();
}
