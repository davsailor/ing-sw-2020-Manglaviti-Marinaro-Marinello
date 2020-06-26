package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.NewMatchMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class NewMatchController {
    private ObservableList list = FXCollections.observableArrayList(2,3);

    @FXML
    Button signUpButton;
    @FXML
    private ChoiceBox<Integer> numberOfPlayers;

    private Client client;
    private Stage selection;

    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * this method initializes the ChoiceBox
     */
    public void initialize(){
        numberOfPlayers.setItems(list);
        numberOfPlayers.setValue(2);
    }

    /**
     * this method is used to send request for the players to play again
     * @param actionEvent is the event of the mouse clicked
     */
    @FXML
    public void registerAction(ActionEvent actionEvent) {
        Message message = new Message(client.getUsername());
        client.setSelectedMatch((Integer.parseInt(String.valueOf(numberOfPlayers.getValue()))));
        message.buildNewMatchMessage(new NewMatchMessage(true, client.getSelectedMatch(), client.getBirthDate()));
        client.getNetworkHandler().send(message);
        signUpButton.setDisable(true);
        selection.setOnCloseRequest(e -> selection.close());
        selection.close();
    }

    public void setStage(Stage selection) {
        this.selection = selection;
    }
}
