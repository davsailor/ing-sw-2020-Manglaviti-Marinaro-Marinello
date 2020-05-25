package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.SecondHeaderType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;


public class InfoMatchController {

    private Client client;
    private ArrayList<Player> players;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
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
        Image image = new Image(getClass().getResourceAsStream("/images/Zeus.png"));
        username1.setText("Diana");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(153.0);
        imageView.setFitHeight(215.0);
        god1.setGraphic(imageView);

        image = new Image(getClass().getResourceAsStream("/images/Poseidon.png"));
        username2.setText("Andrea");
        imageView = new ImageView(image);
        imageView.setFitWidth(162.0);
        imageView.setFitHeight(215.0);
        god2.setGraphic(imageView);
    }


}
