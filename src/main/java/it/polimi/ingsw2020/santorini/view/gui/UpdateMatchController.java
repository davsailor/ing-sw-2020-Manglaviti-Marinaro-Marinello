package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.UpdateMessage;
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


public class UpdateMatchController {

    private Client client;
    private UpdateMessage updateMessage;
    private ArrayList<Player> players;

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setUpdateMessage(UpdateMessage updateMessage) {
        this.updateMessage = updateMessage;
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
     * This method initialize the text on the board after each update and send the messages
     */
    public void setText(){
        switch (updateMessage.getPhase()){
            case START_TURN:
                if(updateMessage.getCurrentPlayer().getNickname().equals(client.getUsername())){
                    text.setText(client.getUsername() + ", now is your turn!");
                    Message nextPhase = new Message(client.getUsername());
                    nextPhase.buildNextPhaseMessage();
                    client.getNetworkHandler().send(nextPhase);
                }
                else{
                    text.setText("Now is the turn of  "+ updateMessage.getCurrentPlayer().getNickname());
                }
                break;
            case STANDBY_PHASE_1 :
            case STANDBY_PHASE_2 :
            case STANDBY_PHASE_3 :
                if(updateMessage.getCurrentPlayer().getNickname().equals(client.getUsername())){
                    text.setText(updateMessage.getCurrentPlayer().getDivinePower().getName() + " accepted your request!");
                    Message nextPhase = new Message(client.getUsername());
                    nextPhase.buildNextPhaseMessage();
                    client.getNetworkHandler().send(nextPhase);
                }
                else{
                    text.setText(updateMessage.getCurrentPlayer().getDivinePower().getName() +" helped "+ updateMessage.getCurrentPlayer().getNickname());
                }
                break;
            case MOVE_PHASE:
            case BUILD_PHASE:
                if(updateMessage.getCurrentPlayer().getNickname().equals(client.getUsername())) {
                    Message nextPhase = new Message(client.getUsername());
                    nextPhase.buildNextPhaseMessage();
                    client.getNetworkHandler().send(nextPhase);
                }
                break;
            case END_TURN:
                if(updateMessage.getCurrentPlayer().getNickname().equals(client.getUsername())){
                    text.setText(client.getUsername()+ " , your turn is finished!");
                }else{
                    text.setText(updateMessage.getCurrentPlayer().getNickname()+ "'s turn is finished!");
                }
                Message nextPhase = new Message(client.getUsername());
                nextPhase.buildNextPhaseMessage();
                client.getNetworkHandler().send(nextPhase);
                break;
            default:
                break;
        }
    }

    @FXML
    public void setPos(ActionEvent actionEvent) {
        switch (updateMessage.getPhase()){
            case START_TURN:
                break;
            case STANDBY_PHASE_1:
                break;
            case MOVE_PHASE:
                break;
            case STANDBY_PHASE_2:
                break;
            case BUILD_PHASE:
                break;
            case STANDBY_PHASE_3:
                break;
            case END_TURN:
                break;
            default:
                break;
        }
    }

    /**
     * this method initializes the players on the update board screen
     * @param players is the ArrayList that contains the players
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
     * this method is used to initialize gods on the update board screen
     * @param name is the name of the god
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

    /**
     * this method initialize the update board with builders and buildings
     * @param board is the board of the match
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
     * this method is used to initialize each cell
     * @param builder is the builder of the player
     * @param button is the button on that cell
     * @param cell is the cell that the method initialize
     */
    private void initializeCell(Label builder, Button button, Cell cell) {
        builder.setText(AppGUI.gender(cell.getBuilder().getGender()));
        builder.setTextFill(Color.web(AppGUI.color(cell.getBuilder().getColor())));
        button.setDisable(true);
    }
}
