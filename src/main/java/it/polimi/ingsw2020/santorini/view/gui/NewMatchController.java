package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.network.client.ServerAdapter;
import it.polimi.ingsw2020.santorini.network.client.ViewAdapter;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.NewMatchMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;



public class NewMatchController {

    private ObservableList list = FXCollections.observableArrayList(2,3);

    @FXML
    Button signUpButton;
    @FXML
    private ChoiceBox<Integer> numberOfPlayers;

    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    public void initialize(){
        numberOfPlayers.setItems(list);
        numberOfPlayers.setValue(2);
    }

    public void registerAction(ActionEvent actionEvent) {
        Message message = new Message(client.getUsername());
        client.setSelectedMatch((Integer.parseInt(String.valueOf(numberOfPlayers.getValue()))));
        message.buildNewMatchMessage(new NewMatchMessage(true, client.getSelectedMatch(), client.getBirthDate()));
        client.getNetworkHandler().send(message);
        signUpButton.setDisable(true);
    }

}
