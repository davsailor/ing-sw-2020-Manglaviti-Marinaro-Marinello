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
    private void calcPosition(int[] builder, Button pos, char gender, String username){
        if (pos.equals(b00)){
            builder[0]=1;
            builder[1]=1;
            if(gender == '♀'){
                p00.setText("✿");
            }
            else
                p00.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p00.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p00.setTextFill(Color.web("#f44040"));
            }
            else{
                p00.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b01)){
            builder[0]=1;
            builder[1]=2;
            if(gender == '♀'){
                p01.setText("✿");
            }
            else
                p01.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p01.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p01.setTextFill(Color.web("#f44040"));
            }
            else{
                p01.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b02)){
            builder[0]=1;
            builder[1]=3;
            if(gender == '♀'){
                p02.setText("✿");
            }
            else
                p02.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p02.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p02.setTextFill(Color.web("#f44040"));
            }
            else{
                p02.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b03)){
            builder[0]=1;
            builder[1]=4;
            if(gender == '♀'){
                p03.setText("✿");
            }
            else
                p03.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p03.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p03.setTextFill(Color.web("#f44040"));
            }
            else{
                p03.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b04)){
            builder[0]=1;
            builder[1]=5;
            if(gender == '♀'){
                p04.setText("✿");
            }
            else
                p04.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p04.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p04.setTextFill(Color.web("#f44040"));
            }
            else{
                p04.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b10)){
            builder[0]=2;
            builder[1]=1;
            if(gender == '♀'){
                p10.setText("✿");
            }
            else
                p10.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p10.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p10.setTextFill(Color.web("#f44040"));
            }
            else{
                p10.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b11)){
            builder[0]=2;
            builder[1]=2;
            if(gender == '♀'){
                p11.setText("✿");
            }
            else
                p11.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p11.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p11.setTextFill(Color.web("#f44040"));
            }
            else{
                p11.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b12)){
            builder[0]=2;
            builder[1]=3;
            if(gender == '♀'){
                p12.setText("✿");
            }
            else
                p12.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p12.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p12.setTextFill(Color.web("#f44040"));
            }
            else{
                p12.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b13)){
            builder[0]=2;
            builder[1]=4;
            if(gender == '♀'){
                p13.setText("✿");
            }
            else
                p13.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p13.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p13.setTextFill(Color.web("#f44040"));
            }
            else{
                p13.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b14)){
            builder[0]=2;
            builder[1]=5;
            if(gender == '♀'){
                p14.setText("✿");
            }
            else
                p14.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p14.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p14.setTextFill(Color.web("#f44040"));
            }
            else{
                p14.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b20)){
            builder[0]=3;
            builder[1]=1;
            if(gender == '♀'){
                p20.setText("✿");
            }
            else
                p20.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p20.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p20.setTextFill(Color.web("#f44040"));
            }
            else{
                p20.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b21)){
            builder[0]=3;
            builder[1]=2;
            if(gender == '♀'){
                p21.setText("✿");
            }
            else
                p21.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p21.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p21.setTextFill(Color.web("#f44040"));
            }
            else{
                p21.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b22)){
            builder[0]=3;
            builder[1]=3;
            if(gender == '♀'){
                p22.setText("✿");
            }
            else
                p22.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p22.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p22.setTextFill(Color.web("#f44040"));
            }
            else{
                p22.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b23)){
            builder[0]=3;
            builder[1]=4;
            if(gender == '♀'){
                p23.setText("✿");
            }
            else
                p23.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p23.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p23.setTextFill(Color.web("#f44040"));
            }
            else{
                p23.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b24)){
            builder[0]=3;
            builder[1]=5;
            if(gender == '♀'){
                p24.setText("✿");
            }
            else
                p24.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p24.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p24.setTextFill(Color.web("#f44040"));
            }
            else{
                p24.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b30)){
            builder[0]=4;
            builder[1]=1;
            if(gender == '♀'){
                p30.setText("✿");
            }
            else
                p30.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p30.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p30.setTextFill(Color.web("#f44040"));
            }
            else{
                p30.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b31)){
            builder[0]=4;
            builder[1]=2;
            if(gender == '♀'){
                p31.setText("✿");
            }
            else
                p31.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p31.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p31.setTextFill(Color.web("#f44040"));
            }
            else{
                p31.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b32)){
            builder[0]=4;
            builder[1]=3;
            if(gender == '♀'){
                p32.setText("✿");
            }
            else
                p32.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p32.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p32.setTextFill(Color.web("#f44040"));
            }
            else{
                p32.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b33)){
            builder[0]=4;
            builder[1]=4;
            if(gender == '♀'){
                p33.setText("✿");
            }
            else
                p33.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p33.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p33.setTextFill(Color.web("#f44040"));
            }
            else{
                p33.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b34)){
            builder[0]=4;
            builder[1]=5;
            if(gender == '♀'){
                p34.setText("✿");
            }
            else
                p34.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p34.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p34.setTextFill(Color.web("#f44040"));
            }
            else{
                p34.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b40)){
            builder[0]=5;
            builder[1]=1;
            if(gender == '♀'){
                p40.setText("✿");
            }
            else
                p40.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p40.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p40.setTextFill(Color.web("#f44040"));
            }
            else{
                p40.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b41)){
            builder[0]=5;
            builder[1]=2;
            if(gender == '♀'){
                p41.setText("✿");
            }
            else
                p41.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p41.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p41.setTextFill(Color.web("#f44040"));
            }
            else{
                p41.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b42)){
            builder[0]=5;
            builder[1]=3;
            if(gender == '♀'){
                p42.setText("✿");
            }
            else
                p42.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p42.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p42.setTextFill(Color.web("#f44040"));
            }
            else{
                p42.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b43)){
            builder[0]=5;
            builder[1]=4;
            if(gender == '♀'){
                p43.setText("✿");
            }
            else
                p43.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p43.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p43.setTextFill(Color.web("#f44040"));
            }
            else{
                p43.setTextFill(Color.web("#b57fb8"));
            }
        } else if (pos.equals(b44)){
            builder[0]=5;
            builder[1]=5;
            if(gender == '♀'){
                p44.setText("✿");
            }
            else
                p44.setText("✱");
            if(username.equals(players.get(0).getNickname())){
                p44.setTextFill(Color.web("#00ffcc"));
            }
            else if(username.equals(players.get(1).getNickname())){
                p44.setTextFill(Color.web("#f44040"));
            }
            else{
                p44.setTextFill(Color.web("#b57fb8"));
            }
        }
        pos.setDisable(true);
    }






    public void initializePlayers(ArrayList<Player> players) {
        setPlayers(players);

        username1.setText(players.get(0).getNickname());
        username1.setAlignment(Pos.TOP_CENTER);
        username2.setText(players.get(1).getNickname());
        username2.setAlignment(Pos.TOP_CENTER);
        Image image = godImage(players.get(0).getDivinePower().getName());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(99.0);
        imageView.setFitHeight(108.0);
        god1.setGraphic(imageView);

        image = godImage(players.get(1).getDivinePower().getName());
        imageView = new ImageView(image);
        imageView.setFitWidth(99.0);
        imageView.setFitHeight(108.0);
        god2.setGraphic(imageView);


        if(players.size()==3){
            username3.setText(players.get(2).getNickname());
            username3.setAlignment(Pos.TOP_CENTER);
            image = godImage(players.get(2).getDivinePower().getName());
            imageView = new ImageView(image);
            imageView.setFitWidth(99.0);
            imageView.setFitHeight(108.0);
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
        if (board[1][1].getStatus()== AccessType.OCCUPIED){
            p00.setText(AppGUI.gender(board[1][1].getBuilder().getGender()));
            p00.setTextFill(Color.web(AppGUI.color(board[1][1].getBuilder().getColor())));
        }else if(board[1][2].getStatus()== AccessType.OCCUPIED){
            p01.setText(AppGUI.gender(board[1][2].getBuilder().getGender()));
            p01.setTextFill(Color.web(AppGUI.color(board[1][2].getBuilder().getColor())));
        }else if(board[1][3].getStatus()== AccessType.OCCUPIED){
            p02.setText(AppGUI.gender(board[1][3].getBuilder().getGender()));
            p02.setTextFill(Color.web(AppGUI.color(board[1][3].getBuilder().getColor())));
        }else if(board[1][4].getStatus()== AccessType.OCCUPIED){
            p03.setText(AppGUI.gender(board[1][4].getBuilder().getGender()));
            p03.setTextFill(Color.web(AppGUI.color(board[1][4].getBuilder().getColor())));
        }else if(board[1][5].getStatus()== AccessType.OCCUPIED){
            p04.setText(AppGUI.gender(board[1][5].getBuilder().getGender()));
            p04.setTextFill(Color.web(AppGUI.color(board[1][5].getBuilder().getColor())));
        }else if(board[2][1].getStatus()== AccessType.OCCUPIED){
            p10.setText(AppGUI.gender(board[2][1].getBuilder().getGender()));
            p10.setTextFill(Color.web(AppGUI.color(board[2][1].getBuilder().getColor())));
        }else if(board[2][2].getStatus()== AccessType.OCCUPIED){
            p11.setText(AppGUI.gender(board[2][2].getBuilder().getGender()));
            p11.setTextFill(Color.web(AppGUI.color(board[2][2].getBuilder().getColor())));
        }else if(board[2][3].getStatus()== AccessType.OCCUPIED){
            p12.setText(AppGUI.gender(board[2][3].getBuilder().getGender()));
            p12.setTextFill(Color.web(AppGUI.color(board[2][3].getBuilder().getColor())));
        }else if(board[2][4].getStatus()== AccessType.OCCUPIED){
            p13.setText(AppGUI.gender(board[2][4].getBuilder().getGender()));
            p13.setTextFill(Color.web(AppGUI.color(board[2][4].getBuilder().getColor())));
        }else if(board[2][5].getStatus()== AccessType.OCCUPIED){
            p14.setText(AppGUI.gender(board[2][5].getBuilder().getGender()));
            p14.setTextFill(Color.web(AppGUI.color(board[2][5].getBuilder().getColor())));
        }else if(board[3][1].getStatus()== AccessType.OCCUPIED){
            p20.setText(AppGUI.gender(board[3][1].getBuilder().getGender()));
            p20.setTextFill(Color.web(AppGUI.color(board[3][1].getBuilder().getColor())));
        }else if(board[3][2].getStatus()== AccessType.OCCUPIED){
            p21.setText(AppGUI.gender(board[3][2].getBuilder().getGender()));
            p21.setTextFill(Color.web(AppGUI.color(board[3][2].getBuilder().getColor())));
        }else if(board[3][3].getStatus()== AccessType.OCCUPIED){
            p22.setText(AppGUI.gender(board[3][3].getBuilder().getGender()));
            p22.setTextFill(Color.web(AppGUI.color(board[3][3].getBuilder().getColor())));
        }else if(board[3][4].getStatus()== AccessType.OCCUPIED){
            p23.setText(AppGUI.gender(board[3][4].getBuilder().getGender()));
            p23.setTextFill(Color.web(AppGUI.color(board[3][4].getBuilder().getColor())));
        }else if(board[3][5].getStatus()== AccessType.OCCUPIED){
            p24.setText(AppGUI.gender(board[3][5].getBuilder().getGender()));
            p24.setTextFill(Color.web(AppGUI.color(board[3][5].getBuilder().getColor())));
        }else if(board[4][1].getStatus()== AccessType.OCCUPIED){
            p30.setText(AppGUI.gender(board[4][1].getBuilder().getGender()));
            p30.setTextFill(Color.web(AppGUI.color(board[4][1].getBuilder().getColor())));
        }else if(board[4][2].getStatus()== AccessType.OCCUPIED){
            p31.setText(AppGUI.gender(board[4][2].getBuilder().getGender()));
            p31.setTextFill(Color.web(AppGUI.color(board[4][2].getBuilder().getColor())));
        }else if(board[4][3].getStatus()== AccessType.OCCUPIED){
            p32.setText(AppGUI.gender(board[4][3].getBuilder().getGender()));
            p32.setTextFill(Color.web(AppGUI.color(board[4][3].getBuilder().getColor())));
        }else if(board[4][4].getStatus()== AccessType.OCCUPIED){
            p33.setText(AppGUI.gender(board[4][4].getBuilder().getGender()));
            p33.setTextFill(Color.web(AppGUI.color(board[4][4].getBuilder().getColor())));
        }else if(board[4][5].getStatus()== AccessType.OCCUPIED){
            p34.setText(AppGUI.gender(board[4][5].getBuilder().getGender()));
            p34.setTextFill(Color.web(AppGUI.color(board[4][5].getBuilder().getColor())));
        }else if(board[5][1].getStatus()== AccessType.OCCUPIED){
            p40.setText(AppGUI.gender(board[5][1].getBuilder().getGender()));
            p40.setTextFill(Color.web(AppGUI.color(board[5][1].getBuilder().getColor())));
        }else if(board[5][2].getStatus()== AccessType.OCCUPIED){
            p41.setText(AppGUI.gender(board[5][2].getBuilder().getGender()));
            p41.setTextFill(Color.web(AppGUI.color(board[5][2].getBuilder().getColor())));
        }else if(board[5][3].getStatus()== AccessType.OCCUPIED){
            p42.setText(AppGUI.gender(board[5][3].getBuilder().getGender()));
            p42.setTextFill(Color.web(AppGUI.color(board[5][3].getBuilder().getColor())));
        }else if(board[5][4].getStatus()== AccessType.OCCUPIED){
            p43.setText(AppGUI.gender(board[5][4].getBuilder().getGender()));
            p43.setTextFill(Color.web(AppGUI.color(board[5][4].getBuilder().getColor())));
        }else if(board[5][5].getStatus()== AccessType.OCCUPIED){
            p44.setText(AppGUI.gender(board[5][5].getBuilder().getGender()));
            p44.setTextFill(Color.web(AppGUI.color(board[5][5].getBuilder().getColor())));
        }
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
}