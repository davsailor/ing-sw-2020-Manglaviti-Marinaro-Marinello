package it.polimi.ingsw2020.santorini.utils;

public enum SecondHeaderType {
    // specializzazione della prima classificazione

    // riferiti a SETUP
    LOGIN,
    MATCH,

    // riferiti a ERROR
    USERNAME_ERROR,

    // riferiti a ASK
    POSSIBLE_MOVES,
    POSSIBLE_BUILDS,
    GOD_AVAILABLE,

    // riferiti a DO
    MOVE,
    BUILD,
    ACTIVATE_GOD;

    //UNDO, se vogliamo fare la FA
}
