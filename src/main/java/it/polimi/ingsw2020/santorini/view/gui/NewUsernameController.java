package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.LoginMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewUsernameController {

    private Client client;


    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    private TextField username;

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

            Button button = (Button) actionEvent.getSource();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();
        }
    }

}
