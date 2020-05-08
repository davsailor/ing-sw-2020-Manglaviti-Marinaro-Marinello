package it.polimi.ingsw2020.santorini.utils.messages.actions;

import it.polimi.ingsw2020.santorini.utils.Direction;

public class SelectedBuildingMessage {
    private Direction direction;

    public SelectedBuildingMessage(Direction dir) {
        this.direction = dir;
        /* due possibilità:
            la prima è far inserire le coordinate della cella in possibleMoves e poi ricavare la direzione
            la seconda è dare come comandi possibili la pressione dei tasti da 1 a 8 e in base al tasto premuto abbiamo una direzione
            la seconda possibilità è più carina almeno secondo noi
        */
    }

    public Direction getDirection() {
        return direction;
    }
}
