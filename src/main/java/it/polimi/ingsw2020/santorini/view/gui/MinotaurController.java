package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.Builder;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.Direction;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.MinotaurParamMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MinotaurController {

    private Stage stage;

    private Button[][] matrix = new Button[3][3];

    private Client client;

    private Builder chosen = null;

    private char yourBuilderGender;

    private MatchStateMessage matchStateMessage;

    private MinotaurParamMessage minotaurParamMessage = new MinotaurParamMessage();

    public void setClient(Client client) {
        this.client = client;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setMatchStateMessage(MatchStateMessage matchStateMessage) {
        this.matchStateMessage = matchStateMessage;
    }

    public MinotaurParamMessage getMinotaurParamMessage() {
        return minotaurParamMessage;
    }

    public Builder getChosen() {
        return chosen;
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

    @FXML
    public void push(ActionEvent actionEvent) {
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
        minotaurParamMessage.setOpponentBuilderDirection(direction);
        minotaurParamMessage.setPlayingBuilderSex(yourBuilderGender);
        stage.setOnCloseRequest(e->stage.close());
        stage.close();
    }

    public void initializeMinotaurMatrix(int[][] MinotaurMatrix){
        matrix[0][0] = b00;
        matrix[0][1] = b01;
        matrix[0][2] = b02;
        matrix[1][0] = b10;
        matrix[1][2] = b12;
        matrix[2][0] = b20;
        matrix[2][1] = b21;
        matrix[2][2] = b22;
        for(int i=0; i<3; ++i){
            for( int j=0 ; j < 3; ++j){
                if(i!=1 || j!= 1){
                    if(MinotaurMatrix[i][j]==0){
                        matrix[i][j].setDisable(true);
                        matrix[i][j].setStyle("-fx-background-color: #ff0000");
                    }
                }
            }
        }
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
        int[] posBuilder = new int[2];

        Direction direction = null;
        chosen = matchStateMessage.getCurrentPlayer().getBuilderM();
        posBuilder[0] = matchStateMessage.getCurrentPlayer().getBuilderM().getPosX();
        posBuilder[1] = matchStateMessage.getCurrentPlayer().getBuilderM().getPosY();


        chosen.setBoard(new Board(matchStateMessage.getBoard()));
        chosen.setPlayer(matchStateMessage.getCurrentPlayer());
        int[][] possibleSwap = Board.neighboringSwappingCell(chosen, AccessType.OCCUPIED);
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                if(possibleSwap[i][j] != 0)
                    try {
                        if (matchStateMessage.getBoard()[posBuilder[0] + (i - 1) * 2][posBuilder[1] + (j - 1) * 2].getStatus() != AccessType.FREE)
                            possibleSwap[i][j] = 0;
                    }catch (IndexOutOfBoundsException e){
                        possibleSwap[i][j] = 0;
                    }

        boolean allZeros = true;
        for(int i = 0; i < 3 && allZeros; ++i)
            for(int j = 0; j < 3 && allZeros; ++j)
                if(possibleSwap[i][j] != 0) allZeros = false;


        if(allZeros){
            M.setDisable(true);
            M.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px;");
        }else
            M.setStyle("-fx-border-color: #00ff00; -fx-border-width: 5px;");

        chosen = matchStateMessage.getCurrentPlayer().getBuilderF();
        posBuilder[0] = matchStateMessage.getCurrentPlayer().getBuilderF().getPosX();
        posBuilder[1] = matchStateMessage.getCurrentPlayer().getBuilderF().getPosY();
        chosen.setBoard(new Board(matchStateMessage.getBoard()));
        chosen.setPlayer(matchStateMessage.getCurrentPlayer());
        possibleSwap = Board.neighboringSwappingCell(chosen, AccessType.OCCUPIED);
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                if(possibleSwap[i][j] != 0)
                    try {
                        if (matchStateMessage.getBoard()[posBuilder[0] + (i - 1) * 2][posBuilder[1] + (j - 1) * 2].getStatus() != AccessType.FREE)
                            possibleSwap[i][j] = 0;
                    }catch (IndexOutOfBoundsException e){
                        possibleSwap[i][j] = 0;
                    }

        allZeros = true;
        for(int i = 0; i < 3 && allZeros; ++i)
            for(int j = 0; j < 3 && allZeros; ++j)
                if(possibleSwap[i][j] != 0) allZeros = false;
        if(allZeros){
            F.setDisable(true);
            F.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px;");
        }else
            F.setStyle("-fx-border-color: #00ff00; -fx-border-width: 5px;");
    }
}
