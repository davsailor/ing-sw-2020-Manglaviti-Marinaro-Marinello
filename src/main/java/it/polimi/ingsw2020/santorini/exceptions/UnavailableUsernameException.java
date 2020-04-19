package it.polimi.ingsw2020.santorini.exceptions;

public class UnavailableUsernameException extends Exception {
    public UnavailableUsernameException() {

    }

    public UnavailableUsernameException(String message) {
        super(message);
    }
}
