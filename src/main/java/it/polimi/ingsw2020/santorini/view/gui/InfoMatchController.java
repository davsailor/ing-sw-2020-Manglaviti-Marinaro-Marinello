package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.SecondHeaderType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;


public class InfoMatchController {

    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    Label god1;
    @FXML
    Label god2;

    public void initialize(){

        //SETTARE NOMI DEI GIOCATORI E DELLA DIVINITA'


        Message message = new Message(client.getUsername());
        message.buildSynchronizationMessage(SecondHeaderType.BEGIN_MATCH);
        client.getNetworkHandler().send(message);

    }


}
