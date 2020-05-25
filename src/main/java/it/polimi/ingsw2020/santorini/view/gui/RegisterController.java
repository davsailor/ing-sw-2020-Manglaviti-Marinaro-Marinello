package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.network.client.ServerAdapter;
import it.polimi.ingsw2020.santorini.network.client.ViewAdapter;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.LoginMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Date;

public class RegisterController {

    private Client client;

    private ObservableList list = FXCollections.observableArrayList(2,3);

    @FXML
    private TextField ipAddress;

    @FXML
    private TextField username;

    @FXML
    private DatePicker birthDate;

    @FXML
    private ChoiceBox<Integer> numberOfPlayers;

    @FXML
    private Button signUpButton;

    public void setClient(Client client) {
        this.client = client;
    }

    public void initialize(){
        numberOfPlayers.setItems(list);
        numberOfPlayers.setValue(2);
        ipAddress.clear();
    }

    @FXML
    public void registerAction(ActionEvent actionEvent) {
        String ip;
        boolean correct = true;
        ip = ipAddress.getText();
        if(!ip.isBlank()) {
            try {
                client.setNetworkHandler(new ServerAdapter(client, ip));
                client.setViewHandler(new ViewAdapter(client));
                client.getNetworkHandler().start();
                client.getViewHandler().start();
            } catch (IOException e) {
                Alert networkError = new Alert(Alert.AlertType.ERROR, "Cannot reach Olympus, try again with a new IP!");
                networkError.showAndWait()
                        .filter(action -> action == ButtonType.OK)
                        .ifPresent(action -> networkError.close());
                ipAddress.clear();
                correct = false;
            }
        } else
            correct = false;


        String usernameId = username.getText();
        Date date = new Date(1900, 0, 1);
        try {
            java.sql.Date sqlDate = java.sql.Date.valueOf(birthDate.getValue());
            date = new Date(sqlDate.getTime());
        } catch(Exception ignored) {}

        int numberPlayers;
        if (numberOfPlayers.getValue().equals(2))
            numberPlayers = 2;
        else
            numberPlayers = 3;

        client.setBirthDate(date);
        client.setUsername(usernameId);
        client.setSelectedMatch(numberPlayers);

        if(correct) {
            Message message = new Message(client.getUsername());
            message.buildLoginMessage(new LoginMessage(client.getUsername(), client.getBirthDate(), client.getSelectedMatch()));
            client.getNetworkHandler().send(message);
            /*
            Button button = (Button) actionEvent.getSource();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();

             */
        }
    }
}