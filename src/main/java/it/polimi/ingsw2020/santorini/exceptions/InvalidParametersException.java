package it.polimi.ingsw2020.santorini.exceptions;

public class InvalidParametersException extends Exception {
    public String error;
    public InvalidParametersException(String error) { this.error = error; }

    public String getError() {
        return error;
    }
}
