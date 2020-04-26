package it.polimi.ingsw2020.santorini.utils;

public enum SecondHeaderType {
    // specializzazione della prima classificazione

    // riferiti a SETUP e LOADING
    LOGIN,
    MATCH,
    PLAYER_SELECTION,

    // riferiti a ERROR
    USERNAME_ERROR,
    INVALID_CELL_SELECTION,

    // riferiti a ASK
    SELECTION_ORDER,
    CORRECT_SELECTION_POS,
    POSSIBLE_MOVES,
    POSSIBLE_BUILDS,
    GOD_AVAILABLE,

    // riferiti a DO
    MOVE,
    BUILD,
    ACTIVATE_GOD;

    //UNDO, se vogliamo fare la FA
}
