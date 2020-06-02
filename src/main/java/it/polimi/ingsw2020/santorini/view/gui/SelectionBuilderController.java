package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.ApolloParamMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.SelectedBuilderPositionMessage;
import it.polimi.ingsw2020.santorini.view.AppGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;

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
    private int[] builderF;
    private int[] builderM;

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
    Label text;
    @FXML
    Label text2;

    @FXML
    Label username1;
    @FXML
    Label username2;
    @FXML
    Label username3;
    @FXML
    Label god1;
    @FXML
    Label god2;
    @FXML
    Label god3;

    @FXML
    Label p00;
    @FXML
    Label p01;
    @FXML
    Label p02;
    @FXML
    Label p03;
    @FXML
    Label p04;
    @FXML
    Label p10;
    @FXML
    Label p11;
    @FXML
    Label p12;
    @FXML
    Label p13;
    @FXML
    Label p14;
    @FXML
    Label p20;
    @FXML
    Label p21;
    @FXML
    Label p22;
    @FXML
    Label p23;
    @FXML
    Label p24;
    @FXML
    Label p30;
    @FXML
    Label p31;
    @FXML
    Label p32;
    @FXML
    Label p33;
    @FXML
    Label p34;
    @FXML
    Label p40;
    @FXML
    Label p41;
    @FXML
    Label p42;
    @FXML
    Label p43;
    @FXML
    Label p44;

    @FXML
    Label c00;
    @FXML
    Label c01;
    @FXML
    Label c02;
    @FXML
    Label c03;
    @FXML
    Label c04;
    @FXML
    Label c10;
    @FXML
    Label c11;
    @FXML
    Label c12;
    @FXML
    Label c13;
    @FXML
    Label c14;
    @FXML
    Label c20;
    @FXML
    Label c21;
    @FXML
    Label c22;
    @FXML
    Label c23;
    @FXML
    Label c24;
    @FXML
    Label c30;
    @FXML
    Label c31;
    @FXML
    Label c32;
    @FXML
    Label c33;
    @FXML
    Label c34;
    @FXML
    Label c40;
    @FXML
    Label c41;
    @FXML
    Label c42;
    @FXML
    Label c43;
    @FXML
    Label c44;

    @FXML
    public void setPos(ActionEvent actionEvent) {
        Button pos = (Button) actionEvent.getSource();
        if(client.getUsername().equals(matchStateMessage.getCurrentPlayer().getNickname())) {
            if(builderF == null) {
                builderF = new int[2];
                calcPosition(builderF, pos, '♀',client.getUsername());
            } else if(builderM == null){
                builderM = new int[2];
                calcPosition(builderM, pos,'♂', client.getUsername());
                Message message = new Message(client.getUsername());
                message.buildSelectedBuilderPosMessage(new SelectedBuilderPositionMessage(client.getUsername(), builderF, builderM));
                client.getNetworkHandler().send(message);
                b00.setDisable(true);
                b01.setDisable(true);
                b02.setDisable(true);
                b03.setDisable(true);
                b04.setDisable(true);
                b10.setDisable(true);
                b11.setDisable(true);
                b12.setDisable(true);
                b13.setDisable(true);
                b14.setDisable(true);
                b20.setDisable(true);
                b21.setDisable(true);
                b22.setDisable(true);
                b23.setDisable(true);
                b24.setDisable(true);
                b30.setDisable(true);
                b31.setDisable(true);
                b32.setDisable(true);
                b33.setDisable(true);
                b34.setDisable(true);
                b40.setDisable(true);
                b41.setDisable(true);
                b42.setDisable(true);
                b43.setDisable(true);
                b44.setDisable(true);
            }
        }
    }

    public void setText(){
        if(matchStateMessage.getCurrentPlayer().getNickname().equals(client.getUsername())){
            text.setText(client.getUsername() +", posiziona i tuoi builder");
            text.setAlignment(Pos.TOP_CENTER);
        }else{
            text.setText( matchStateMessage.getCurrentPlayer().getNickname()+" sta posizionando i suoi builder" );
            text.setAlignment(Pos.TOP_CENTER);
            text2.setText("Aspetta il tuo turno");
            text2.setAlignment(Pos.TOP_CENTER);
        }
    }

    private void calcPosition(int[] builder, Button pos, char gender, String username){
        if (pos.equals(b00)) setBuilder(builder, 1, 1, username, gender, p00);
        else if (pos.equals(b01)) setBuilder(builder, 1, 2, username, gender, p01);
        else if (pos.equals(b02)) setBuilder(builder, 1, 3, username, gender, p02);
        else if (pos.equals(b03)) setBuilder(builder, 1, 4, username, gender, p03);
        else if (pos.equals(b04)) setBuilder(builder, 1, 5, username, gender, p04);
        else if (pos.equals(b10)) setBuilder(builder, 2, 1, username, gender, p10);
        else if (pos.equals(b11)) setBuilder(builder, 2, 2, username, gender, p11);
        else if (pos.equals(b12)) setBuilder(builder, 2, 3, username, gender, p12);
        else if (pos.equals(b13)) setBuilder(builder, 2, 4, username, gender, p13);
        else if (pos.equals(b14)) setBuilder(builder, 2, 5, username, gender, p14);
        else if (pos.equals(b20)) setBuilder(builder, 3, 1, username, gender, p20);
        else if (pos.equals(b21)) setBuilder(builder, 3, 2, username, gender, p21);
        else if (pos.equals(b22)) setBuilder(builder, 3, 3, username, gender, p22);
        else if (pos.equals(b23)) setBuilder(builder, 3, 4, username, gender, p23);
        else if (pos.equals(b24)) setBuilder(builder, 3, 5, username, gender, p24);
        else if (pos.equals(b30)) setBuilder(builder, 4, 1, username, gender, p30);
        else if (pos.equals(b31)) setBuilder(builder, 4, 2, username, gender, p31);
        else if (pos.equals(b32)) setBuilder(builder, 4, 3, username, gender, p32);
        else if (pos.equals(b33)) setBuilder(builder, 4, 4, username, gender, p33);
        else if (pos.equals(b34)) setBuilder(builder, 4, 5, username, gender, p34);
        else if (pos.equals(b40)) setBuilder(builder, 5, 1, username, gender, p40);
        else if (pos.equals(b41)) setBuilder(builder, 5, 2, username, gender, p41);
        else if (pos.equals(b42)) setBuilder(builder, 5, 3, username, gender, p42);
        else if (pos.equals(b43)) setBuilder(builder, 5, 4, username, gender, p43);
        else if (pos.equals(b44)) setBuilder(builder, 5, 5, username, gender, p44);
        pos.setDisable(true);
    }

    private void setBuilder(int[] builder, int row, int col, String username, char gender, Label label) {
        builder[0] = row;
        builder[1] = col;
        if(gender == '♀') label.setText("✿");
        else label.setText("✱");
        if(username.equals(players.get(0).getNickname())) label.setTextFill(Color.web("#00ffcc"));
        else if(username.equals(players.get(1).getNickname())) label.setTextFill(Color.web("#f44040"));
        else label.setTextFill(Color.web("#b57fb8"));
    }

    public void initializePlayers(ArrayList<Player> players) {
        setPlayers(players);
        username1.setText(players.get(0).getNickname());
        username1.setAlignment(Pos.TOP_CENTER);
        username2.setText(players.get(1).getNickname());
        username2.setAlignment(Pos.TOP_CENTER);
        Image image = godImage(players.get(0).getDivinePower().getName());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(123.0);
        imageView.setFitHeight(169.0);
        god1.setGraphic(imageView);

        image = godImage(players.get(1).getDivinePower().getName());
        imageView = new ImageView(image);
        imageView.setFitWidth(123.0);
        imageView.setFitHeight(169.0);
        god2.setGraphic(imageView);


        if(players.size()==3) {
            username3.setText(players.get(2).getNickname());
            username3.setAlignment(Pos.TOP_CENTER);
            image = godImage(players.get(2).getDivinePower().getName());
            imageView = new ImageView(image);
            imageView.setFitWidth(123.0);
            imageView.setFitHeight(169.0);
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

    public void initializeBoard(Cell[][] board) {
        if(board[1][1].getStatus()== AccessType.OCCUPIED) initializeCell(p00, b00, board[1][1]);
        if(board[1][2].getStatus()== AccessType.OCCUPIED) initializeCell(p01, b01, board[1][2]);
        if(board[1][3].getStatus()== AccessType.OCCUPIED) initializeCell(p02, b02, board[1][3]);
        if(board[1][4].getStatus()== AccessType.OCCUPIED) initializeCell(p03, b03, board[1][4]);
        if(board[1][5].getStatus()== AccessType.OCCUPIED) initializeCell(p04, b04, board[1][5]);
        if(board[2][1].getStatus()== AccessType.OCCUPIED) initializeCell(p10, b10, board[2][1]);
        if(board[2][2].getStatus()== AccessType.OCCUPIED) initializeCell(p11, b11, board[2][2]);
        if(board[2][3].getStatus()== AccessType.OCCUPIED) initializeCell(p12, b12, board[2][3]);
        if(board[2][4].getStatus()== AccessType.OCCUPIED) initializeCell(p13, b13, board[2][4]);
        if(board[2][5].getStatus()== AccessType.OCCUPIED) initializeCell(p14, b14, board[2][5]);
        if(board[3][1].getStatus()== AccessType.OCCUPIED) initializeCell(p20, b20, board[3][1]);
        if(board[3][2].getStatus()== AccessType.OCCUPIED) initializeCell(p21, b21, board[3][2]);
        if(board[3][3].getStatus()== AccessType.OCCUPIED) initializeCell(p22, b22, board[3][3]);
        if(board[3][4].getStatus()== AccessType.OCCUPIED) initializeCell(p23, b23, board[3][4]);
        if(board[3][5].getStatus()== AccessType.OCCUPIED) initializeCell(p24, b24, board[3][5]);
        if(board[4][1].getStatus()== AccessType.OCCUPIED) initializeCell(p30, b30, board[4][1]);
        if(board[4][2].getStatus()== AccessType.OCCUPIED) initializeCell(p31, b31, board[4][2]);
        if(board[4][3].getStatus()== AccessType.OCCUPIED) initializeCell(p32, b32, board[4][3]);
        if(board[4][4].getStatus()== AccessType.OCCUPIED) initializeCell(p33, b33, board[4][4]);
        if(board[4][5].getStatus()== AccessType.OCCUPIED) initializeCell(p34, b34, board[4][5]);
        if(board[5][1].getStatus()== AccessType.OCCUPIED) initializeCell(p40, b40, board[5][1]);
        if(board[5][2].getStatus()== AccessType.OCCUPIED) initializeCell(p41, b41, board[5][2]);
        if(board[5][3].getStatus()== AccessType.OCCUPIED) initializeCell(p42, b42, board[5][3]);
        if(board[5][4].getStatus()== AccessType.OCCUPIED) initializeCell(p43, b43, board[5][4]);
        if(board[5][5].getStatus()== AccessType.OCCUPIED) initializeCell(p44, b44, board[5][5]);
        c00.setText(String.valueOf(board[1][1].getLevel().getHeight()));
        c01.setText(String.valueOf(board[1][2].getLevel().getHeight()));
        c02.setText(String.valueOf(board[1][3].getLevel().getHeight()));
        c03.setText(String.valueOf(board[1][4].getLevel().getHeight()));
        c04.setText(String.valueOf(board[1][5].getLevel().getHeight()));
        c10.setText(String.valueOf(board[2][1].getLevel().getHeight()));
        c11.setText(String.valueOf(board[2][2].getLevel().getHeight()));
        c12.setText(String.valueOf(board[2][3].getLevel().getHeight()));
        c13.setText(String.valueOf(board[2][4].getLevel().getHeight()));
        c14.setText(String.valueOf(board[2][5].getLevel().getHeight()));
        c20.setText(String.valueOf(board[3][1].getLevel().getHeight()));
        c21.setText(String.valueOf(board[3][2].getLevel().getHeight()));
        c22.setText(String.valueOf(board[3][3].getLevel().getHeight()));
        c23.setText(String.valueOf(board[3][4].getLevel().getHeight()));
        c24.setText(String.valueOf(board[3][5].getLevel().getHeight()));
        c30.setText(String.valueOf(board[4][1].getLevel().getHeight()));
        c31.setText(String.valueOf(board[4][2].getLevel().getHeight()));
        c32.setText(String.valueOf(board[4][3].getLevel().getHeight()));
        c33.setText(String.valueOf(board[4][4].getLevel().getHeight()));
        c34.setText(String.valueOf(board[4][5].getLevel().getHeight()));
        c40.setText(String.valueOf(board[5][1].getLevel().getHeight()));
        c41.setText(String.valueOf(board[5][2].getLevel().getHeight()));
        c42.setText(String.valueOf(board[5][3].getLevel().getHeight()));
        c43.setText(String.valueOf(board[5][4].getLevel().getHeight()));
        c44.setText(String.valueOf(board[5][5].getLevel().getHeight()));
    }

    private void initializeCell(Label builder, Button button, Cell cell) {
        builder.setText(AppGUI.gender(cell.getBuilder().getGender()));
        builder.setTextFill(Color.web(AppGUI.color(cell.getBuilder().getColor())));
        button.setDisable(true);
    }
}