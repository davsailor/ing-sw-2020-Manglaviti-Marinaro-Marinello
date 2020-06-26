package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class EndMatchController {
    @FXML
    Label winner;
    @FXML
    Label Loser;

    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * this method is used to set the username of the winner on the winner screen
     */
    public void setWinner(){
        winner.setText(client.getUsername());
        winner.setAlignment(Pos.TOP_CENTER);
    }

    /**
     * this method is used to set the username of the loser on the loser screen
     */
    public void setText(){
        Loser.setText(client.getUsername());
        Loser.setAlignment(Pos.TOP_CENTER);
    }
}
