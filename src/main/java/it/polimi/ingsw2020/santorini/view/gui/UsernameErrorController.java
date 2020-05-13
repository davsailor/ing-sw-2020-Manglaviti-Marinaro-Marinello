package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.LoginMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class UsernameErrorController {

    private Client client;

    @FXML
    TextField usernameId;
    @FXML
    Button insert;

    public void newUsernameAction(ActionEvent actionEvent){
        String username = usernameId.getText();

        client.setUsername(username);

        Message message = new Message(client.getUsername());
        message.buildLoginMessage(new LoginMessage(client.getUsername(), client.getBirthDate(), client.getSelectedMatch()));
        client.getNetworkHandler().send(message);
    }




}
