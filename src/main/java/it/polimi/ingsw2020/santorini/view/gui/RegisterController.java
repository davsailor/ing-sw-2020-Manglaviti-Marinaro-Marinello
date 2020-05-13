package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.network.client.ServerAdapter;
import it.polimi.ingsw2020.santorini.network.client.ViewAdapter;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.LoginMessage;
import it.polimi.ingsw2020.santorini.view.AppGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Date;

public class RegisterController {

    private Client client;

    ObservableList list = FXCollections.observableArrayList(2,3);

    @FXML
    TextField indirizzoIp;

    @FXML
    TextField username;

    @FXML
    DatePicker birthDate;

    @FXML
    ChoiceBox<Integer> numberOfPlayers;

    @FXML
    Button signUpButton;


    public void initialize(){
        numberOfPlayers.setItems(list);
        numberOfPlayers.setValue(2);
    }


    @FXML
    public void RegisterAction(ActionEvent actionEvent) {
        String ip = indirizzoIp.getText();
        System.out.println(ip);

        client = AppGUI.getClient();

        client.setNetworkHandler(new ServerAdapter(client, ip));
        client.setViewHandler(new ViewAdapter(client));

        String usernameId = username.getText();
        java.sql.Date sqlDate = java.sql.Date.valueOf(birthDate.getValue());
        Date date = new Date(sqlDate.getTime());
        int numberPlayers;
        if (numberOfPlayers.getValue().equals(2))
            numberPlayers = 2;
        else
            numberPlayers = 3;

        client.getNetworkHandler().start();
        client.getViewHandler().start();

        client.setBirthDate(date);
        client.setUsername(usernameId);

        client.setSelectedMatch(numberPlayers);

        Message message = new Message(client.getUsername());
        message.buildLoginMessage(new LoginMessage(client.getUsername(), client.getBirthDate(), client.getSelectedMatch()));
        client.getNetworkHandler().send(message);


    }

}

