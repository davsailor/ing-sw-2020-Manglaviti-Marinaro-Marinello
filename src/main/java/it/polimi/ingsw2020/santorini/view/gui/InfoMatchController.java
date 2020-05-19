package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.SecondHeaderType;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


public class InfoMatchController {

    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    TextArea textArea;

    public void infoMatch(){

        TextArea textArea ;
        Message message = new Message(client.getUsername());
        message.buildSynchronizationMessage(SecondHeaderType.BEGIN_MATCH);
        client.getNetworkHandler().send(message);

    }


}
