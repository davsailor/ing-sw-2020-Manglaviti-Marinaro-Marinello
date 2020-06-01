package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class ParametersSelectionController {

    private Client client;
    private MatchStateMessage matchStateMessage;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setMatchStateMessage(MatchStateMessage matchStateMessage) {
        this.matchStateMessage = matchStateMessage;
    }

    @FXML
    public void selectParam(ActionEvent actionEvent){

    }

    public void setText(){
        switch (matchStateMessage.getCurrentPlayer().getDivinePower().getName()){

        }
    }



}
