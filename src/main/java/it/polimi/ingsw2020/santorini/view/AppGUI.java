package it.polimi.ingsw2020.santorini.view;

import it.polimi.ingsw2020.santorini.network.client.Client;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AppGUI extends Application {

    @FXML
    Label messageLabel;

    private Client client;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/FXML/sample.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("sample");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void surpriseAction(ActionEvent actionEvent) {
        messageLabel.setText("La carne è TENERISSIMA");
    }
}
