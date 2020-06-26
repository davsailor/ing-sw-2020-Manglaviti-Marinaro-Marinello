package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.Builder;
import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.actions.SelectedBuilderMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import it.polimi.ingsw2020.santorini.view.AppGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ChooseBuilderController {

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
    Label text;

    /**
     * The method extracts from the actionEvent the gender of the builder chosen by the player, it adds the gender to a message and serialize it
     * @param actionEvent is the event of the click over one of the buttons representing the gender of the builder selected
     */
    @FXML
    public void getGender(ActionEvent actionEvent) {
        Message chosenBuilder = new Message(client.getUsername());
        Button pos = (Button) actionEvent.getSource();
        char gender = 'M';
        if(pos.equals(b00)){
            gender = matchStateMessage.getBoard()[1][1].getBuilder().getGender();
        }else if(pos.equals(b01)){
            gender = matchStateMessage.getBoard()[1][2].getBuilder().getGender();
        }else if(pos.equals(b02)){
            gender = matchStateMessage.getBoard()[1][3].getBuilder().getGender();
        }else if(pos.equals(b04)){
            gender = matchStateMessage.getBoard()[1][5].getBuilder().getGender();
        }else if(pos.equals(b10)){
            gender = matchStateMessage.getBoard()[2][1].getBuilder().getGender();
        }else if(pos.equals(b11)){
            gender = matchStateMessage.getBoard()[2][2].getBuilder().getGender();
        }else if(pos.equals(b12)){
            gender = matchStateMessage.getBoard()[2][3].getBuilder().getGender();
        }else if(pos.equals(b13)){
            gender = matchStateMessage.getBoard()[2][4].getBuilder().getGender();
        }else if(pos.equals(b14)){
            gender = matchStateMessage.getBoard()[2][5].getBuilder().getGender();
        }else if(pos.equals(b20)){
            gender = matchStateMessage.getBoard()[3][1].getBuilder().getGender();
        }else if(pos.equals(b21)){
            gender = matchStateMessage.getBoard()[3][2].getBuilder().getGender();
        }else if(pos.equals(b22)){
            gender = matchStateMessage.getBoard()[3][3].getBuilder().getGender();
        }else if(pos.equals(b23)){
            gender = matchStateMessage.getBoard()[3][4].getBuilder().getGender();
        }else if(pos.equals(b24)){
            gender = matchStateMessage.getBoard()[3][5].getBuilder().getGender();
        }else if(pos.equals(b30)){
            gender = matchStateMessage.getBoard()[4][1].getBuilder().getGender();
        }else if(pos.equals(b31)){
            gender = matchStateMessage.getBoard()[4][2].getBuilder().getGender();
        }else if(pos.equals(b32)){
            gender = matchStateMessage.getBoard()[4][3].getBuilder().getGender();
        }else if(pos.equals(b33)){
            gender = matchStateMessage.getBoard()[4][4].getBuilder().getGender();
        }else if(pos.equals(b34)){
            gender = matchStateMessage.getBoard()[4][5].getBuilder().getGender();
        }else if(pos.equals(b40)){
            gender = matchStateMessage.getBoard()[5][1].getBuilder().getGender();
        }else if(pos.equals(b41)){
            gender = matchStateMessage.getBoard()[5][2].getBuilder().getGender();
        }else if(pos.equals(b42)){
            gender = matchStateMessage.getBoard()[5][3].getBuilder().getGender();
        }else if(pos.equals(b43)){
            gender = matchStateMessage.getBoard()[5][4].getBuilder().getGender();
        }else if(pos.equals(b44)){
            gender = matchStateMessage.getBoard()[5][5].getBuilder().getGender();
        }
        if(gender=='♀'){
            gender = 'F';
        }else
            gender = 'M';
        chosenBuilder.buildSelectedBuilderMessage(new SelectedBuilderMessage(gender));
        client.getNetworkHandler().send(chosenBuilder);
    }

    /**
     * The method disables all the buttons of the matrix and
     */
    public void initializeBuilder(){
        disableAll();
        findBuilder(matchStateMessage.getCurrentPlayer().getBuilderF().getPosX(), matchStateMessage.getCurrentPlayer().getBuilderF().getPosY());
        findBuilder(matchStateMessage.getCurrentPlayer().getBuilderM().getPosX(), matchStateMessage.getCurrentPlayer().getBuilderM().getPosY());
    }

    /**
     * The method disables all the buttons
     */
    private void disableAll(){
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

    /**
     * The method disables button that is in the position (x,y)
     * @param x is the row of the cell where is placed of a builder and the row of the button that will be disabled
     * @param y is the column of the cell where is placed of a builder and the column of the button that will be disabled
     */
    private void findBuilder(int x, int y){
        Builder builder = matchStateMessage.getBoard()[x][y].getBuilder();
        builder.setBoard(new Board(matchStateMessage.getBoard()));
        builder.setPlayer(matchStateMessage.getCurrentPlayer());
        if( !builder.canMove()) {
            return;
        }
        if(x==1 && y==1){
            b00.setDisable(false);
            System.out.println(b00.isDisable());
        }else if(x==1 && y==2){
            b01.setDisable(false);
            System.out.println(b01.isDisable());
        }else if(x==1 && y==3){
            b02.setDisable(false);
            System.out.println(b02.isDisable());
        }else if(x==1 && y==4){
            b03.setDisable(false);
        }else if(x==1 && y==5){
            b04.setDisable(false);
        }else if(x==2 && y==1){
            b10.setDisable(false);
        }else if(x==2 && y==2){
            b11.setDisable(false);
        }else if(x==2 && y==3){
            b12.setDisable(false);
        }else if(x==2 && y==4){
            b13.setDisable(false);
        }else if(x==2 && y==5){
            b14.setDisable(false);
        }else if(x==3 && y==1){
            b20.setDisable(false);
        }else if(x==3 && y==2){
            b21.setDisable(false);
        }else if(x==3 && y==3){
            b22.setDisable(false);
        }else if(x==3 && y==4){
            b23.setDisable(false);
        }else if(x==3 && y==5){
            b24.setDisable(false);
        }else if(x==4 && y==1){
            b30.setDisable(false);
        }else if(x==4 && y==2){
            b31.setDisable(false);
        }else if(x==4 && y==3){
            b32.setDisable(false);
        }else if(x==4 && y==4){
            b33.setDisable(false);
        }else if(x==4 && y==5){
            b34.setDisable(false);
        }else if(x==5 && y==1){
            b40.setDisable(false);
        }else if(x==5 && y==2){
            b41.setDisable(false);
        }else if(x==5 && y==3){
            b42.setDisable(false);
        }else if(x==5 && y==4){
            b43.setDisable(false);
        }else if(x==5 && y==5){
            b44.setDisable(false);
        }
    }

    /**
     * the method sets the label on the Board window. The text's label that is set tells to the player what he has to do, so if he is the current
     * player he will have to place his builders, if he isn't he will have to wait for his turn
     */
    public void setText(){
        if(matchStateMessage.getCurrentPlayer().getNickname().equals(client.getUsername())){
            text.setText(client.getUsername() +", select the builder that you want to move");
        }else{
            text.setText("Now is the turn of "+ matchStateMessage.getCurrentPlayer().getNickname());
        }
    }

    /**
     * the method initialize the Board, before the choice of the player, checking if a cell is occupied or not. In the first case the method
     * invoke initializeCell method that will disable the button of the occupied cell and sets the builder that occupies the cell.
     * The method than sets in each cell its height
     * @param board is the matrix of the cell that represents the board
     */
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

    /**
     * the method is invoked by initializeBoard when it finds an occupied. It disable the button associated to the cell and sets the label
     * to show the builder that occupies the correspondent cell
     * @param builder is the label that displays the builder that occupies the cell
     * @param button is the button that represents the cell
     * @param cell is the occupied cell
     */
    private void initializeCell(Label builder, Button button, Cell cell) {
        builder.setText(AppGUI.gender(cell.getBuilder().getGender()));
        builder.setTextFill(Color.web(AppGUI.color(cell.getBuilder().getColor())));
        button.setDisable(true);
    }

    /**
     * the method sets the names of the players in the Board window and sets also the respective gods'images
     * @param players is the ArrayList containing all the names of the players
     */
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

    /**
     * The method is used to obtain the image of a specific god
     * @param name is the string containing the name of god of whom we want the image
     * @return the requested god's image
     */
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
