package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.Builder;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.Direction;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.ApolloParamMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ApolloController {

    private Client client;

    private MatchStateMessage matchStateMessage;

    private ArrayList<Player> players;

    private  ApolloParamMessage apolloParamMessage = new ApolloParamMessage();

    private Stage stage;

    public  ApolloParamMessage getApolloParamMessage() {
        return apolloParamMessage;
    }

    @FXML
    Label text;
    @FXML
    Label text2;
    @FXML
    Button b00;
    @FXML
    Button b01;
    @FXML
    Button b02;
    @FXML
    Button b10;
    @FXML
    Button b12;
    @FXML
    Button b20;
    @FXML
    Button b21;
    @FXML
    Button b22;
    @FXML
    Button F;
    @FXML
    Button M;

    Builder chosen = null;

    private char yourBuilderGender;

    public char getYourBuilderGender() {
        return yourBuilderGender;
    }


    public void setClient(Client client) {
        this.client = client;
    }

    public void setMatchStateMessage(MatchStateMessage matchStateMessage) {
        this.matchStateMessage = matchStateMessage;
    }

    public void initializeApolloMatrix(int[][] apolloMatrix) {
        Button[][] buttonMatrix = new Button[3][3];
        buttonMatrix[0][0] = b00;
        buttonMatrix[0][1] = b01;
        buttonMatrix[0][2] = b02;
        buttonMatrix[1][0] = b10;
        buttonMatrix[1][2] = b12;
        buttonMatrix[2][0] = b20;
        buttonMatrix[2][1] = b21;
        buttonMatrix[2][2] = b22;
        for(int i=0; i<3; ++i){
            for( int j=0 ; j < 3; ++j){
                if(i!=1 || j!= 1){
                    if(apolloMatrix[i][j]==0){
                        buttonMatrix[i][j].setDisable(true);
                        buttonMatrix[i][j].setStyle("-fx-background-color: #ff0000");
                    }
                }
            }
        }
    }

    @FXML
    public void selectSwap(ActionEvent actionEvent){
        Button pos = (Button) actionEvent.getSource();
        Direction direction = null;
        if(pos.equals(b00)){
            direction = Direction.NORTH_WEST;
        }else if ( pos.equals(b01)){
            direction = Direction.NORTH;
        }else if ( pos.equals(b02)){
            direction = Direction.NORTH_EAST;
        }else if ( pos.equals(b10)){
            direction = Direction.WEST;
        }else if ( pos.equals(b12)){
            direction = Direction.EAST;
        }else if ( pos.equals(b20)){
            direction = Direction.SOUTH_WEST;
        }else if ( pos.equals(b21)){
            direction = Direction.SOUTH;
        }else if ( pos.equals(b22)){
            direction = Direction.SOUTH_EAST;
        }
        b00.setDisable(true);
        b01.setDisable(true);
        b02.setDisable(true);
        b10.setDisable(true);
        b12.setDisable(true);
        b20.setDisable(true);
        b21.setDisable(true);
        b22.setDisable(true);

        apolloParamMessage.setYourBuilderGender(getYourBuilderGender());
        apolloParamMessage.setOpponentBuilderDirection(direction);
        System.out.println(apolloParamMessage);
        stage.setOnCloseRequest(e->stage.close());
        stage.close();
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public Builder getChosen() {
        return chosen;
    }

    @FXML
    public void selectGender(ActionEvent actionEvent) {
        Button pos = (Button) actionEvent.getSource();
        if(pos.getId().equals("F")){
            chosen = matchStateMessage.getCurrentPlayer().getBuilderF();
            yourBuilderGender = 'F';
        }else{
            chosen = matchStateMessage.getCurrentPlayer().getBuilderM();
            yourBuilderGender = 'M';
        }
        chosen.setBoard(new Board(matchStateMessage.getBoard()));
        chosen.setPlayer(matchStateMessage.getCurrentPlayer());
        F.setDisable(true);
        M.setDisable(true);
        stage.setOnCloseRequest(e->stage.close());
        stage.close();
    }


    public void initializeButtons() {
        Builder chosen = matchStateMessage.getCurrentPlayer().getBuilderF();
        chosen.setBoard(new Board(matchStateMessage.getBoard()));
        chosen.setPlayer(matchStateMessage.getCurrentPlayer());
        int[][] neighboringSwappingCell = Board.neighboringSwappingCell(chosen, AccessType.OCCUPIED);
        boolean allZeros = true;
        for(int i = 0; i < 3 && allZeros; ++i)
            for(int j = 0; j < 3 && allZeros; ++j)
                if(neighboringSwappingCell[i][j] != 0) allZeros = false;
        if(allZeros){
            F.setDisable(true);
            F.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px;");
        }else
            F.setStyle("-fx-border-color: #00ff00; -fx-border-width: 5px;");
        chosen = matchStateMessage.getCurrentPlayer().getBuilderM();
        chosen.setBoard(new Board(matchStateMessage.getBoard()));
        chosen.setPlayer(matchStateMessage.getCurrentPlayer());
        neighboringSwappingCell = Board.neighboringSwappingCell(chosen, AccessType.OCCUPIED);
        allZeros = true;
        for(int i = 0; i < 3 && allZeros; ++i)
            for(int j = 0; j < 3 && allZeros; ++j)
                if(neighboringSwappingCell[i][j] != 0) allZeros = false;
        if(allZeros){
            M.setDisable(true);
            M.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px;");
        }else
            M.setStyle("-fx-border-color: #00ff00; -fx-border-width: 5px;");
    }
}
