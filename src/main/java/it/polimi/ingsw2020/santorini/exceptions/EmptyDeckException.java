package it.polimi.ingsw2020.santorini.exceptions;

public class EmptyDeckException extends Exception {
    public EmptyDeckException() {}

    public EmptyDeckException(String message) {
        super(message);
    }
}
