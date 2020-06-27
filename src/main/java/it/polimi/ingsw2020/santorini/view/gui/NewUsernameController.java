package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.LoginMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class NewUsernameController {
    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    public Button confirmation;
    @FXML
    private TextField username;

    /**
     * this method is used to set a new username for the player
     * @param actionEvent is the event of the button clicked after the player inserts the new username
     */
    @FXML
    public void newUsernameSelection(ActionEvent actionEvent) {
        String newUsername;
        boolean correct = true;
        newUsername = username.getText();
        if(newUsername.isBlank()) correct = false;
        if(correct) {
            client.setUsername(newUsername);
            Message message = new Message(client.getUsername());
            message.buildLoginMessage(new LoginMessage(client.getUsername(), client.getBirthDate(), client.getSelectedMatch()));
            client.getNetworkHandler().send(message);
        }
    }
}
