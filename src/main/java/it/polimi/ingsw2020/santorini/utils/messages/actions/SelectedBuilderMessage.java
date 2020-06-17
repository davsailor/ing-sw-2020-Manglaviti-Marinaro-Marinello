package it.polimi.ingsw2020.santorini.utils.messages.actions;

public class SelectedBuilderMessage {
    private char gender;

    public SelectedBuilderMessage(char gender) {
        this.gender = gender;
    }

    public char getGender() {
        return gender;
    }
}
