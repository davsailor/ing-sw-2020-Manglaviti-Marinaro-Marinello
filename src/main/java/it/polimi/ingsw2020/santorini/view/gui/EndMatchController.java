package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import javafx.fxml.FXML;

import java.awt.*;

public class EndMatchController {
    @FXML
    Label winner;

    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setWinner(){
        winner.setText(client.getUsername());
    }


}
