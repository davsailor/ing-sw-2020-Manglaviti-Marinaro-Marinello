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

    public void setWinner(){
        winner.setText(client.getUsername());
        winner.setAlignment(Pos.TOP_CENTER);
    }

    public void setText(){
        Loser.setText(client.getUsername());
        Loser.setAlignment(Pos.TOP_CENTER);
    }


}
