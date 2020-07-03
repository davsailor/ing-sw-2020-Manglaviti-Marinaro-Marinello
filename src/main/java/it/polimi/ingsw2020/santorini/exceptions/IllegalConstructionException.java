package it.polimi.ingsw2020.santorini.exceptions;

public class IllegalConstructionException extends Exception{
    String error;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     * @param error the error message
     */
    public IllegalConstructionException(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
