package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class SelectionBuilderController {

    private Client client;
    private MatchStateMessage matchStateMessage;
    private ArrayList<Player> players;

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setMatchStateMessage(MatchStateMessage matchStateMessage) {
        this.matchStateMessage = matchStateMessage;
    }

    @FXML
    private Label title;
   @FXML
    Button b00;
    @FXML
    Button b01;
    @FXML
    Button b02;
    @FXML
    Button b03;
    @FXML
    Button b04;
    @FXML
    Button b10;
    @FXML
    Button b11;
    @FXML
    Button b12;
    @FXML
    Button b13;
    @FXML
    Button b14;
    @FXML
    Button b20;
    @FXML
    Button b21;
    @FXML
    Button b22;
    @FXML
    Button b23;
    @FXML
    Button b24;
    @FXML
    Button b30;
    @FXML
    Button b31;
    @FXML
    Button b32;
    @FXML
    Button b33;
    @FXML
    Button b34;
    @FXML
    Button b40;
    @FXML
    Button b41;
    @FXML
    Button b42;
    @FXML
    Button b43;
    @FXML
    Button b44;

    @FXML
    public void setPos(ActionEvent actionEvent) {
        title.setText("Clicca la cella dove vuoi inserire la tua costruttrice ");
        Button pos = (Button) actionEvent.getSource();
        if(client.getUsername().equals(matchStateMessage.getCurrentPlayer().getNickname())) {
            int[] builderM, builderF;
            builderM = new int[2];
            builderF = new int[2];
            builderF[0]=-1;
            builderF[1]=-1;

            if (pos.equals(b00)){
                 builderF[0]=1;
                 builderF[1]=1;
            }
            else if (pos.equals(b01)){
                 builderF[0]=1;
                 builderF[1]=2;
             }
             else if (pos.equals(b02)){
                 builderF[0]=1;
                 builderF[1]=3;
             }
             else if (pos.equals(b03)){
                 builderF[0]=1;
                 builderF[1]=4;
             }
             else if (pos.equals(b04)){
                 builderF[0]=1;
                 builderF[1]=5;
             }
             else if (pos.equals(b10)){
                 builderF[0]=2;
                 builderF[1]=1;
             }
             else if (pos.equals(b11)){
                 builderF[0]=2;
                 builderF[1]=2;
             }
             else if (pos.equals(b12)){
                 builderF[0]=2;
                 builderF[1]=3;
             }
             else if (pos.equals(b13)){
                 builderF[0]=2;
                 builderF[1]=4;
             }
             else if (pos.equals(b14)){
                 builderF[0]=2;
                 builderF[1]=5;
             }
             else if (pos.equals(b20)){
                 builderF[0]=3;
                 builderF[1]=1;
             }
             else if (pos.equals(b21)){
                 builderF[0]=3;
                 builderF[1]=2;
             }
             else if (pos.equals(b22)){
                 builderF[0]=3;
                 builderF[1]=3;
             }
             else if (pos.equals(b23)){
                 builderF[0]=3;
                 builderF[1]=4;
             }
             else if (pos.equals(b24)){
                 builderF[0]=3;
                 builderF[1]=5;
             }
             else if (pos.equals(b30)){
                 builderF[0]=4;
                 builderF[1]=1;
             }
             else if (pos.equals(b31)){
                 builderF[0]=4;
                 builderF[1]=2;
             }
             else if (pos.equals(b32)){
                 builderF[0]=4;
                 builderF[1]=3;
             }
             else if (pos.equals(b33)){
                 builderF[0]=4;
                 builderF[1]=4;
             }
             else if (pos.equals(b34)){
                 builderF[0]=4;
                 builderF[1]=5;
             }
             else if (pos.equals(b40)){
                 builderF[0]=5;
                 builderF[1]=1;
             }
             else if (pos.equals(b41)){
                 builderF[0]=5;
                 builderF[1]=2;
             }
             else if (pos.equals(b42)){
                 builderF[0]=5;
                 builderF[1]=3;
             }
             else if (pos.equals(b43)){
                 builderF[0]=5;
                 builderF[1]=4;
             }
             else if (pos.equals(b44)){
                 builderF[0]=5;
                 builderF[1]=5;
             }
            title.setText("Colonna: "+ builderF[1]+ "\nRighe: "+ builderF[0]);
        }
    }
}