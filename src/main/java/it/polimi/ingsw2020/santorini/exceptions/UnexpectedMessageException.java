package it.polimi.ingsw2020.santorini.exceptions;

public class UnexpectedMessageException extends Exception{
    public UnexpectedMessageException() {

    }

    public UnexpectedMessageException(String message) {
        super(message);
    }
}
