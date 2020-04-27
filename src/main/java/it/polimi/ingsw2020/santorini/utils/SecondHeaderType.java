package it.polimi.ingsw2020.santorini.utils;

public enum SecondHeaderType {
    // specializzazione della prima classificazione

    // riferiti a SETUP e LOADING
    LOGIN,
    MATCH,
    PLAYER_SELECTION,

    // riferiti a SYNCHRONIZATION
    BEGIN_MATCH,

    // riferiti a VERIFY
    CORRECT_SELECTION_POS,

    // riferiti a ERROR
    USERNAME_ERROR,
    INVALID_CELL_SELECTION,

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
