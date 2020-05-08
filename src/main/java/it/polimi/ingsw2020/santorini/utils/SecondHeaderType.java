package it.polimi.ingsw2020.santorini.utils;

public enum SecondHeaderType {
    // specializzazione della prima classificazione

    // riferiti a SETUP e LOADING
    LOGIN,
    MATCH,
    PLAYER_SELECTION,
    END_MATCH,

    // riferiti a SYNCHRONIZATION
    BEGIN_MATCH,

    // riferiti a VERIFY
    CORRECT_SELECTION_POS,

    // riferiti a ERROR
    USERNAME_ERROR,
    INVALID_CELL_SELECTION,
    INVALID_PARAMETERS,
    INVALID_MOVE,
    INVALID_BUILDING,

    // riferiti a ASK

    // riferiti a DO
    NEXT_PHASE,

    // riferiti sia ad ASK che a DO
    ACTIVATE_GOD,
    SELECT_PARAMETERS,
    SELECT_BUILDER,
    SELECT_CELL_MOVE,
    SELECT_CELL_BUILD;

    //UNDO, se vogliamo fare la FA
}
