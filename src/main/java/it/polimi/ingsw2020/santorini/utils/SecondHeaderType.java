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
    INVALID_PARAMETERS,

    // riferiti a ASK

    // riferiti a DO
    NEXT_PHASE,

    // riferiti sia ad ASK che a DO
    ACTIVATE_GOD,
    SELECT_PARAMETERS,
    USE_POWER,
    MOVE,
    BUILD,
    SELECT_BUILDER;

    //UNDO, se vogliamo fare la FA
}
