package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.SecondHeaderType;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.util.ArrayList;


public class InfoMatchController {

    private Client client;
    private boolean infoMatchDisplayed = false;

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    private Label god1;
    @FXML
    private Label god2;
    @FXML
    private Label god3;
    @FXML
    private Label username1;
    @FXML
    private Label username2;
    @FXML
    private Label username3;

    public void initialize(){
    }

    public void initializePlayers(ArrayList<Player> players){
        username1.setText(players.get(0).getNickname());
        username1.setAlignment(Pos.TOP_CENTER);
        username2.setText(players.get(1).getNickname());
        username2.setAlignment(Pos.TOP_CENTER);

        Image image = godImage(players.get(0).getDivinePower().getName());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(160.0);
        imageView.setFitHeight(217.0);
        god1.setGraphic(imageView);

        image = godImage(players.get(1).getDivinePower().getName());
        imageView = new ImageView(image);
        imageView.setFitWidth(160.0);
        imageView.setFitHeight(217.0);
        god2.setGraphic(imageView);

        if(players.size() == 3){
            username3.setText(players.get(2).getNickname());
            username3.setAlignment(Pos.TOP_CENTER);
            image = godImage(players.get(2).getDivinePower().getName());
            imageView = new ImageView(image);
            imageView.setFitWidth(160.0);
            imageView.setFitHeight(217.0);
            god3.setGraphic(imageView);
        }
    }

    private Image godImage(String name){
        switch (name){
            case "Apollo" :
                return new Image(getClass().getResourceAsStream("/images/Apollo.png"));
            case "Ares" :
                return new Image(getClass().getResourceAsStream("/images/Ares.png"));
            case "Artemis" :
                return new Image(getClass().getResourceAsStream("/images/Artemis.png"));
            case "Athena" :
                return new Image(getClass().getResourceAsStream("/images/Athena.png"));
            case "Atlas" :
                return new Image(getClass().getResourceAsStream("/images/Atlas.png"));
            case "Chronus" :
                return new Image(getClass().getResourceAsStream("/images/Chronus.png"));
            case "Demeter" :
                return new Image(getClass().getResourceAsStream("/images/Demeter.png"));
            case "Hephaestus" :
                return new Image(getClass().getResourceAsStream("/images/Hephaestus.png"));
            case "Hestia" :
                return new Image(getClass().getResourceAsStream("/images/Hestia.png"));
            case "Minotaur" :
                return new Image(getClass().getResourceAsStream("/images/Minotaur.png"));
            case "Pan" :
                return new Image(getClass().getResourceAsStream("/images/Pan.png"));
            case "Poseidon" :
                return new Image(getClass().getResourceAsStream("/images/Poseidon.png"));
            case "Prometheus" :
                return new Image(getClass().getResourceAsStream("/images/Prometheus.png"));
            case "Zeus" :
                return new Image(getClass().getResourceAsStream("/images/Zeus.png"));
        }
        return null;
    }
}
